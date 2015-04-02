package com.powermoves.aleckson.stuckrn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by aleckson on 4/2/2015.
 */
public class SingleContactActivity extends  Activity {

    // JSON node keys
    private static final String TAG_GAME_TEXT = "name";
    private static final String TAG_DATE = "email";
    private static final String TAG_NETWORK = "mobile";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_contact);

        // getting intent data
        Intent in = getIntent();

        // Get JSON values from previous intent
        String name = in.getStringExtra(TAG_GAME_TEXT);
        String email = in.getStringExtra(TAG_DATE);
        String mobile = in.getStringExtra(TAG_NETWORK);

        // Displaying all values on the screen
        TextView lblName = (TextView) findViewById(R.id.name_label);
        TextView lblEmail = (TextView) findViewById(R.id.email_label);
        TextView lblMobile = (TextView) findViewById(R.id.mobile_label);

        lblName.setText(name);
        lblEmail.setText(email);
        lblMobile.setText(mobile);
    }
}
