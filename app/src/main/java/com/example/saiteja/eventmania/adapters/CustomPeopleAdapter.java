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
import com.example.saiteja.eventmania.model.Person;

import java.util.List;

/**
 * Created by Sai Teja on 11/14/2015.
 */
public class CustomPeopleAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Person> personItems;

    public CustomPeopleAdapter(Activity activity, List<Person> personItems)
    {
        this.activity=activity;
        this.personItems=personItems;
    }


    @Override
    public int getCount() {
        return personItems.size();
    }


    @Override
    public Object getItem(int location) {
        return personItems.get(location);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.person_row, null);

        TextView personName = (TextView) convertView.findViewById(R.id.personName);
        ImageView personImage=(ImageView)convertView.findViewById(R.id.imagePerson);
        // getting movie data for the row

        Person p=personItems.get(position);
        personName.setText(p.getName());
        String eventChar=p.getName();
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color1 = generator.getRandomColor();
        TextDrawable drawable2 = TextDrawable.builder()
                .buildRound(String.valueOf(eventChar.charAt(0)).toUpperCase(), color1);
        personImage.setImageDrawable(drawable2);
        return convertView;
    }
}
