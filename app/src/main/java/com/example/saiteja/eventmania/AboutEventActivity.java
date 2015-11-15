package com.example.saiteja.eventmania;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.example.saiteja.eventmania.app.AppController;
import com.example.saiteja.eventmania.helper.Util;
import com.example.saiteja.eventmania.helper.network.ConnectionDetector;
import com.example.saiteja.eventmania.model.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Sai Teja on 11/13/2015.
 */
public class AboutEventActivity extends AppCompatActivity implements FeedBackDialog.Communicator {

    String id;
    ConnectionDetector cd;
    TextView eventName,eventTime,eventDesc,eventVenue,eventcname1,eventcname2,eventcno1,eventcno2,eventDate,eventPeople;
    Button eventGoing;
    NetworkImageView image;
    private ProgressDialog pDialog;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    SharedPreferences sharedPreferences;
    String email;
    String event_id;
    Date date;
    Intent intent;
    int  started=0,going=0,feedback=0;
    Calendar cal;

    String eventNameCal;
    String Location;
    String date1,time;
    String datetime;

    private Toolbar toolbar;
    TextView tabText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            event_id=String.valueOf(extras.getInt("id"));

        }

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("");
        toolbar.setLogo(R.mipmap.event_hub);
        tabText = (TextView) findViewById(R.id.tabText);

        sharedPreferences=getSharedPreferences("user", Context.MODE_PRIVATE);
        email=sharedPreferences.getString("email", "default");

        eventName= (TextView) findViewById(R.id.aeventName);
        eventTime= (TextView) findViewById(R.id.aeventTime);
        eventGoing= (Button) findViewById(R.id.going);
        eventPeople= (TextView) findViewById(R.id.people_going);
        eventDate= (TextView) findViewById(R.id.aeventDate);
        eventDesc= (TextView) findViewById(R.id.aeventDesc);
        eventVenue= (TextView) findViewById(R.id.aeventVenue);
        eventcname1= (TextView) findViewById(R.id.aeventContact1);
        eventcname2= (TextView) findViewById(R.id.aeventContact2);
        eventcno1= (TextView) findViewById(R.id.aeventContactno1);
        eventcno2= (TextView) findViewById(R.id.aeventContactno2);

        image= (NetworkImageView) findViewById(R.id.eventimage);

        cal = Calendar.getInstance();

        intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");

        eventPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPersonDialog();
            }
        });

        eventGoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(eventGoing.getText().equals("Give Feedback"))
                {
                    started=1;
                    feedback=1;
                    showFeedDialog();

                }
                else
                {
                    going=1;
                    going(email);
                }
            }
        });

        buttonCheck();

        eventcno1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                String s="tel:"+eventcno1.getText().toString();
                callIntent.setData(Uri.parse(s));
                startActivity(callIntent);
            }
        });

        eventcno2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                String s="tel:"+eventcno2.getText().toString();
                callIntent.setData(Uri.parse(s));
                startActivity(callIntent);
            }
        });

        cd=new ConnectionDetector(getApplicationContext());
        if(!cd.isConnectingToInternet())
        {
            Toast.makeText(this, "Please checkyour Network", Toast.LENGTH_SHORT).show();
            return;
        }

        getData(event_id);



    }

    public void showFeedDialog() {
        buttonCheck();
        DialogFragment newFragment = new FeedBackDialog();
        newFragment.show(getFragmentManager(), "feedBack");
    }

    public void buttonCheck()
    {
        if(started==0 && going==0)
        {
            eventGoing.setEnabled(true);
            eventGoing.setText("I Am Going");
            eventGoing.setBackgroundColor(Color.parseColor("#558fed"));
        }
        else if(started==0 && going==1)
        {
            eventGoing.setEnabled(false);
            eventGoing.setText("Going");
            eventGoing.setBackgroundColor(Color.parseColor("#b0caf4"));
        }
        else if(started==1 && feedback==0)
        {
            eventGoing.setEnabled(true);
            eventGoing.setText("Give Feedback");
            eventGoing.setBackgroundColor(Color.parseColor("#ffdd00"));
        }
        else
        {
            eventGoing.setEnabled(false);
            eventGoing.setText("Feedback");
            eventGoing.setBackgroundColor(Color.parseColor("#ffeb70"));
        }
    }

    public void showPersonDialog() {
        DialogFragment newFragment = new PeopleFragment();
        newFragment.show(getFragmentManager(), "persons");
    }

    private void getData(final String event) {
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        String tag_string_req = "req_event";
        pDialog.setMessage("Getting Event Details ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                "http://eventmnitw.herokuapp.com/getevent/", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Tag_event", "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject json = new JSONObject(response);
                    if(json==null)
                    {
                        Toast.makeText(AboutEventActivity.this,"Please check the network",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    eventName.setText(json.getString("name"));
                    eventNameCal=json.getString("name");
                    eventDesc.setText(json.getString("content"));
                    eventVenue.setText(json.getString("venue"));
                    Location=json.getString("venue");
                    eventcname1.setText(json.getString("contact_name_1"));
                    eventcname2.setText(json.getString("contact_name_2"));
                    eventcno1.setText(json.getString("contact_number_1"));
                    eventcno2.setText(json.getString("contact_number_2"));
                    String path=json.getString("image");
                    started=Integer.valueOf(json.getString("started"));
                    feedback=Integer.valueOf(json.getString("feedback"));
                    going=Integer.valueOf(json.getString("going"));
                    path="http://"+path;
                    Log.d("Image",path);
                    image.setImageUrl(path,imageLoader);
                    datetime = json.getString("date");
                    String [] arr=datetime.split("T");
                    date1=arr[0];
                    time=arr[1];
                    datetime=date1+" "+time;
                    eventTime.setText(time);
                    eventDate.setText(date1);
                    eventPeople.setText(json.getString("user_count")+" Going");

                    buttonCheck();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "There was an Error please try again", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", event);
                params.put("email",email);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    public void going(final String email)
    {
        buttonCheck();
        String tag_string_req = "req_event";
        pDialog.setMessage("Registering You ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                "http://eventmnitw.herokuapp.com/iamgoing/", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Tag_event", "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject json = new JSONObject(response);
                    if(json==null)
                    {
                        Toast.makeText(AboutEventActivity.this,"Please check the network",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(json.getString("success").equalsIgnoreCase("1"))
                    {
                        Log.d("time",datetime.toString());
                        Timestamp t=Timestamp.valueOf(datetime);
                        eventGoing.setText("Going!");
                        eventGoing.setEnabled(false);
                        intent.putExtra("beginTime", t.getTime());
                        intent.putExtra("allDay", false);
                        intent.putExtra("rrule", "FREQ=DAILY");
                        intent.putExtra("eventLocation", Location);
                        intent.putExtra("title", eventNameCal);
                        startActivity(intent);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "There was an Error please try again", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("event_id",event_id);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void onDialogMessage(String message, int rating) {
        //sendData(message, rating);
        new SendTask().execute(message,String.valueOf(rating));
    }



    public class SendTask extends AsyncTask<String,Void,HashMap<String ,String>> {

        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(AboutEventActivity.this);
            progressDialog.setMessage("Sending Feedback..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected HashMap<String ,String> doInBackground(String... strings) {

            if (strings==null||strings.length<2) return null;


            HashMap<String,String> hashMapPostData=new HashMap();

            hashMapPostData.put("email", email);
            hashMapPostData.put("event_id", event_id);
            hashMapPostData.put("feedback",strings[0]);
            hashMapPostData.put("rating",strings[1]);


            String jsonstr= Util.getStringFromURL("http://eventmnitw.herokuapp.com/addfeedback/", hashMapPostData);
            //  Log.d("JSON Response",jsonstr);
            if (jsonstr!=null) {
                Log.d("GOT FROM HTTP", jsonstr);
                try {
                    JSONObject jsonObject=new JSONObject(jsonstr);
                    Log.d("json",jsonstr.toString());
                    if(jsonObject.getString("sucess").equalsIgnoreCase("1")) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("success", "1");
                        return hashMap;
                    }
                    else {
                        HashMap<String, String> hashMap=null;
                        return hashMap;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(HashMap<String ,String> hashMap) {
            super.onPostExecute(hashMap);
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }
            if(hashMap!=null)
            {
                if(hashMap.get("success").equalsIgnoreCase("1"))
                {
                    Toast.makeText(AboutEventActivity.this, "Thank You", Toast.LENGTH_SHORT).show();
                }

            }

        }
    }


}
