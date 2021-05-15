package com.example.officeathome;


import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SignIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
    }

    public void launchGoToSignUp(View view) {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }

    /*
    launchSignIn:
    1. Read the password and username
    2. check with the database with the password and username
    3. if corrected: get the ID
        4. create the intent, put in ID
        5. send the intent
    6. if not corrected: show the toast password error
    7. restart the activity
     */
    public void launchSignIn(View view) {
        Intent intent = new Intent(this, MainSearch.class);
        String message = "testingID";
        intent.putExtra("ID", message);
        startActivity(intent);
    }

    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
    }
}
