package com.example.officeathome;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener{

    RadioGroup departmentRowOne;
    RadioGroup departmentRowTwo;

    private FirebaseAuth mAuth;
    private EditText mEmail;
    private EditText mPassword;

    // Data need to be stored
    private String radioID = null;
    private String level = null;
    private FirebaseDatabase database = FirebaseDatabase.
            getInstance("https://officeathome-77d7b-default-rtdb.firebaseio.com/");
    private DatabaseReference myRef = database.getReference("user");

    private static final String TAG = "SignUp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        departmentRowOne = findViewById(R.id.department_group_one);
        departmentRowTwo = findViewById(R.id.department_group_two);
        // Get the intent and its data.
//        Intent intent = getIntent();
//        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
//        TextView textView = findViewById(R.id.order_textview);
//        textView.setText(message);

        // Create the spinner.
        Spinner spinner = findViewById(R.id.level_spinner);
        if (spinner != null) {
            spinner.setOnItemSelectedListener(this);
        }

        // Create an ArrayAdapter using the string array and default spinner
        // layout.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.spinner_values,
                android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner.
        if (spinner != null) {
            spinner.setAdapter(adapter);
        }

        departmentRowOne.setOnCheckedChangeListener(new SignUp.departmentRowOneListener());
        departmentRowTwo.setOnCheckedChangeListener(new SignUp.departmentRowTwoListener());
        departmentRowOne.clearCheck();
        departmentRowTwo.clearCheck();

        mAuth = FirebaseAuth.getInstance();
        mEmail = findViewById(R.id.name_text);
        mPassword = findViewById(R.id.password_text);

    }



    /*
    launchSignUp:
    1. Initiate an ID
    2. record the username, password, department and level to the database
    3. put the ID into intent
    4. send the ID
     */
    public void launchSignUp(View view) {

        //String email = "testingID";
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        if(validationForm()==false) {
            AlertDialog alertDialog = new AlertDialog.Builder(SignUp.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Alert message to be shown");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user != null){
                                sendVerificationEmail();
                            }
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUp.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
//        String username = "Hello Desmond";
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        writeNewUser(user.getUid(),email,username,password,level,false,radioID);


//            Intent intent = new Intent(this, MainSearch.class);
//            intent.putExtra("ID", email);
//            startActivity(intent);
    }

    private void writeNewUser(String uid, String email, String username, String password, String level,
                                     boolean availability,  String dep) {
        People user = new People(email, username, password, level, availability, dep);

        myRef.child(uid).setValue(user);
    }
    private boolean validationForm(){
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        Pattern pattern = Pattern.
                compile("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$");
        Matcher mail = pattern.matcher(email);

        if(level==null || radioID==null || mail.find()==false || password.length()<8 )
            return false;
        else
            return true;
    }


    private void sendVerificationEmail()
    {
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // email sent
                            //checkIfEmailVerified();
                            String username = "Hello Desmond";
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            writeNewUser(user.getUid(),email,username,password,level,false,radioID);

                            // after email is sent just logout the user and finish this activity
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(SignUp.this, MainSearch.class));
                            finish();
                        }
                        else
                        {
                            // email not sent, so display message and restart the activity or do whatever you wish to do

                            //restart this activity
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());

                        }
                    }
                });
    }

    private void checkIfEmailVerified()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified())
        {
            // user is verified, so you can finish this activity or send user to activity which you want.
            //finish();
            Intent intent = new Intent(this, SignIn.class);
            startActivity(intent);
            Toast.makeText(SignUp.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
        }
        else
        {
            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.
            FirebaseAuth.getInstance().signOut();

            //restart this activity
            overridePendingTransition(0, 0);
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());

        }
    }

    public void launchGoToSignIn(View view) {
        Intent intent = new Intent(this, SignIn.class);
        startActivity(intent);
    }

    private class departmentRowOneListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int position) {
            switch (position) {
                case R.id.Executives:
                    departmentRowTwo.clearCheck();
                    departmentRowOne.check(R.id.Executives);
                    radioID = "Executives";
                    break;
                case R.id.RD:
                    departmentRowTwo.clearCheck();
                    departmentRowOne.check(R.id.RD);
                    radioID = "RD";
                    break;
                case R.id.HR:
                    departmentRowTwo.clearCheck();
                    departmentRowOne.check(R.id.HR);
                    radioID = "HR";
                    break;
            }
        }
    }
    private class departmentRowTwoListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int position) {

            switch (position) {
                case R.id.Marketing:
                    departmentRowOne.clearCheck();
                    departmentRowTwo.check(R.id.Marketing);
                    radioID = "Marketing";
                    break;
                case R.id.Purchasing:
                    departmentRowOne.clearCheck();
                    departmentRowTwo.check(R.id.Purchasing);
                    radioID = "Purchasing";
                    break;
            }
        }
    }

    /**
     * Displays the actual message in a toast message.
     *
     * @param message Message to display.
     */
    public void displayToast(String message) {
        Toast.makeText(this, message,
                Toast.LENGTH_SHORT).show();
    }

    // Interface callback for when any spinner item is selected.
    @Override
    public void onItemSelected(AdapterView<?> adapterView,
                               View view, int i, long l) {
        String spinnerLabel = adapterView.getItemAtPosition(i).toString();
        level = adapterView.getItemAtPosition(i).toString();
        displayToast(spinnerLabel);
    }

    // Interface callback for when no spinner item is selected.
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Do nothing.
    }

}

