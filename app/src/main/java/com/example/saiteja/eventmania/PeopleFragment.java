package com.example.saiteja.eventmania;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.saiteja.eventmania.adapters.CustomPeopleAdapter;
import com.example.saiteja.eventmania.app.AppController;
import com.example.saiteja.eventmania.app.URL;
import com.example.saiteja.eventmania.model.Person;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sai Teja on 11/14/2015.
 */
public class PeopleFragment extends DialogFragment  {


    private List<Person> personList;
    private ProgressDialog pDialog;

    private ListView listView;
    private CustomPeopleAdapter adapter;
    String event_id;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_people,null);
        getDialog().setTitle("People Going!");

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            event_id=String.valueOf(extras.getInt("id"));

        }
        listView= (ListView) v.findViewById(R.id.listPerson);
        personList = new ArrayList<Person>();
        personList.clear();

        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        adapter = new CustomPeopleAdapter(getActivity(), personList);
        listView.setAdapter(adapter);

        getData(event_id);
        return v;
    }

    public void getData(final String id)
    {
        String tag_string_req = "people_event";
        pDialog.setMessage("Getting People ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL.geteventusers, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Tag_event", "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    if(jObj==null)
                    {
                        Toast.makeText(getActivity(), "Please check the network", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    JSONArray jsonArray=jObj.getJSONArray("users");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        String name=jsonArray.getString(i);
                        Person p=new Person();
                        p.setName(name);
                        personList.add(p);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", "Registration Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        "There was an Error please try again", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
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
