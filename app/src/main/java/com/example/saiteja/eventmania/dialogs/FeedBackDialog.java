package com.example.saiteja.eventmania.dialogs;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.example.saiteja.eventmania.R;

/**
 * Created by Sai Teja on 10/23/2015.
 */
public class FeedBackDialog extends DialogFragment {

    EditText editText;
    RatingBar ratingBar;
    Button btnSubmit;
    Communicator communicator;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicator= (Communicator) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_ratedialog, container, false);

        getDialog().setTitle("Please give your Feedback");
        addListenerOnRatingBar(v);
        addListenerOnButton(v);

        return v;
    }


    public void addListenerOnRatingBar(View v) {

        ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

            }
        });
    }

    public void addListenerOnButton(View v) {

        ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);
        editText=(EditText)v.findViewById(R.id.editText);
        btnSubmit = (Button) v.findViewById(R.id.btnSubmit);

        //if click on me, then display the current rating value.
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                communicator.onDialogMessage(editText.getText().toString(), (int) ratingBar.getRating());
                dismiss();
            }

        });

    }
    public interface Communicator
    {
        public void onDialogMessage(String message, int rating);
    }
}
