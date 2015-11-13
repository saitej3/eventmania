package com.example.saiteja.eventmania;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import com.example.saiteja.eventmania.helper.network.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Sai Teja on 11/13/2015.
 */
public class AboutEventActivity extends AppCompatActivity {

    String id;
    ConnectionDetector cd;
    TextView eventName,eventTime,eventDesc,eventVenue,eventpreq,eventcname1,eventcname2,eventcno1,eventcno2,eventDate,eventPeople;
    Button eventGoing;
    NetworkImageView image;
    private ProgressDialog pDialog;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

//        getActionBar().setBackgroundDrawable(
//                new ColorDrawable(Color.parseColor("#F3A300")));

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

        eventGoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setText("Going!");
                button.setBackgroundColor(Color.parseColor("#bbdefb"));
                button.setEnabled(false);
            }
        });

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

        getData("1");
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
                    eventDesc.setText(json.getString("content"));
                    eventVenue.setText(json.getString("venue"));
                    eventcname1.setText(json.getString("contact_name_1"));
                    eventcname2.setText(json.getString("contact_name_2"));
                    eventcno1.setText(json.getString("contact_number_1"));
                    eventcno2.setText(json.getString("contact_number_2"));
                    String path=json.getString("image");
                    path="http://"+path;
                    Log.d("Image",path);
                    image.setImageUrl(path,imageLoader);
                    String datetime=json.getString("date");
                    String date,time;
                    String [] arr=datetime.split("T");
                    date=arr[0];
                    time=arr[1];
                    eventTime.setText(time);
                    eventDate.setText(date);
                    eventPeople.setText(json.getString("user_count")+" Going");
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

}
