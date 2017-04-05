package com.lab360io.jobio.officeApp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import coaching.CoachingPreference;

public class acStartupActivity extends AppCompatActivity {
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        if (CoachingPreference.needToShowPrompt(ctx, CoachingPreference.IS_APP_LOAD_FIRST_TIME)) {
            //CoachingPreference.updatePreference(ctx, CoachingPreference.IS_APP_LOAD_FIRST_TIME);
            Intent i = new Intent(ctx, acWelcome.class);
            startActivity(i);
            finish();
        } else {
            Intent i = new Intent(ctx, acSplash.class);
            startActivity(i);
            finish();
        }
    }
}

