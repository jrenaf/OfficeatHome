package com.example.officeathome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends Activity {
    private static int TIME_OUT = 2000; //Time to launch the another activity

    // An activity last for only 2 seconds and then go to the Sign in Activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MainActivity.this, SignIn.class);
                startActivity(i);
                finish();
            }
        }, TIME_OUT);
    }
}

