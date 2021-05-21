// COMP4521     WAN Yuxuan  20493150    ywanaf@connect.ust.hk
// COMP4521     REN Jiming  20493019    jrenaf@connect.ust.hk
// COMP4521     YIN Yue     20493368    yyinai@connect.ust.hk
package com.example.officeathome;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {
    public static final String KEY_PREF_EXAMPLE_SWITCH = "example_switch";

    /**
     * Replaces the content with the Fragment to display it.
     *
     * @param savedInstanceState Instance state bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.settings);
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new SettingFragment()).commit();
    }
}