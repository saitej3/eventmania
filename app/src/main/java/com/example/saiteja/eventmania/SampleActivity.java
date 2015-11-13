package com.example.saiteja.eventmania;

/**
 * Created by Sai Teja on 11/13/2015.
 */
import java.io.InputStream;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.saiteja.eventmania.helper.gmailLetter.ColorGenerator;
import com.example.saiteja.eventmania.helper.gmailLetter.TextDrawable;

public class SampleActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample);
        ImageView image = (ImageView) findViewById(R.id.image_view);
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color1 = generator.getRandomColor();
        TextDrawable drawable2 = TextDrawable.builder()
                .buildRound("A", color1);
        image.setImageDrawable(drawable2);

    }

    protected void onStart() {
        super.onStart();
    }

    protected void onStop() {
        super.onStop();

    }



}