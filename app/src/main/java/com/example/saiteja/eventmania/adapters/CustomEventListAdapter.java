package com.example.saiteja.eventmania.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.saiteja.eventmania.R;
import com.example.saiteja.eventmania.helper.gmailLetter.ColorGenerator;
import com.example.saiteja.eventmania.helper.gmailLetter.TextDrawable;
import com.example.saiteja.eventmania.model.Event;

import java.util.List;

/**
 * Created by Sai Teja on 10/22/2015.
 */
public class CustomEventListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Event> eventItems;

    public CustomEventListAdapter(Activity activity, List<Event> eventItems)
    {
        this.activity=activity;
        this.eventItems=eventItems;
    }


    @Override
    public int getCount() {
        return eventItems.size();
    }


    @Override
    public Object getItem(int location) {
        return eventItems.get(location);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.event_row, null);

        TextView eventName = (TextView) convertView.findViewById(R.id.eventName);
        TextView eventTime = (TextView) convertView.findViewById(R.id.eventTime);
        TextView eventDesc = (TextView) convertView.findViewById(R.id.eventVenue);
        ImageView icon=(ImageView)convertView.findViewById(R.id.imageIcon);
        // getting movie data for the row
        Event e = eventItems.get(position);
        eventName.setText(e.getEventName());
        eventDesc.setText(e.getEventVenue());
        eventTime.setText(e.getEventTime());
        String eventChar=e.getEventName();
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color1 = generator.getRandomColor();
        TextDrawable drawable2 = TextDrawable.builder()
                .buildRound(String.valueOf(eventChar.charAt(0)).toUpperCase(), color1);
        icon.setImageDrawable(drawable2);
        return convertView;
    }
}
