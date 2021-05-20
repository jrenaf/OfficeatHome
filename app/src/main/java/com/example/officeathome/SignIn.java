package com.example.officeathome;


import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPassword;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);

        mEmail = findViewById(R.id.name_text);
        mPassword = findViewById(R.id.email_text);
        mAuth = FirebaseAuth.getInstance();
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

        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        if (email.equals("") || password.equals("")){
            Toast.makeText(SignIn.this, "Please type in your email and password.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Log.d("TAG", "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            //Log.w("TAG", "signInWithEmail:failed", task.getException());
                            Toast.makeText(SignIn.this, "Email or password is not correct.", Toast.LENGTH_SHORT).show();
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());

                        } else {
                            //checkIfEmailVerified();
                            Intent intent = new Intent(SignIn.this, MainSearch.class);
                            intent.putExtra("myID", email);
                            startActivity(intent);
                            Toast.makeText(SignIn.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }

    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
    }
}
