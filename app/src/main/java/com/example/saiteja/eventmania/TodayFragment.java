package com.example.saiteja.eventmania;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.saiteja.eventmania.adapters.CustomEventListAdapter;
import com.example.saiteja.eventmania.app.AppController;
import com.example.saiteja.eventmania.app.URL;
import com.example.saiteja.eventmania.helper.network.ConnectionDetector;
import com.example.saiteja.eventmania.model.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sai Teja on 11/12/2015.
 */
public class TodayFragment extends Fragment {

    ConnectionDetector cd;
    private List<Event> eventList;
    private ProgressDialog pDialog;

    private ListView listView;
    private CustomEventListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.today_fragment, container, false);
        listView= (ListView) rootView.findViewById(R.id.list);
        eventList = new ArrayList<Event>();
        eventList.clear();
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        adapter = new CustomEventListAdapter(getActivity(), eventList);
        listView.setAdapter(adapter);
//        Event e=new Event();
//        e.setEventName("Hover Mania");
//        e.setEventTime("12:30 am");
//        e.setEventVenue("innovation gaarage");
//        eventList.add(e);
//        e=new Event();
//        e.setEventName("Rangoli");
//        e.setEventTime("2:00 am");
//        e.setEventVenue("beside garage");
//        eventList.add(e);

        //adapter.notifyDataSetChanged();

        getData("2015-11-13");

        return rootView;
    }


    private void getData(final String date) {


        String tag_string_req = "req_event";
        pDialog.setMessage("Getting Event Details ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL.registerEventUrl, new Response.Listener<String>() {

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

                    JSONArray jsonArray=jObj.getJSONArray("events");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        Event e=new Event();
                        e.setId(Integer.valueOf(jsonObject.getString("id")));
                        e.setEventName(jsonObject.getString("name"));
                        e.setEventVenue(jsonObject.getString("venue"));
                        e.setEventTime(jsonObject.getString("date"));

                        eventList.add(e);
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
                params.put("date", date);
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