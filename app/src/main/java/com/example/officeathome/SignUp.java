package com.example.officeathome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener{

    RadioGroup departmentRowOne;
    RadioGroup departmentRowTwo;

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
    }



    /*
    launchSignUp:
    1. Initiate an ID
    2. record the username, password, department and level to the database
    3. put the ID into intent
    4. send the ID
     */
    public void launchSignUp(View view) {
        Intent intent = new Intent(this, MainSearch.class);
        String message = "testingID";
        intent.putExtra("ID", message);
        startActivity(intent);
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
                    break;
                case R.id.RD:
                    departmentRowTwo.clearCheck();
                    departmentRowOne.check(R.id.RD);
                    break;
                case R.id.HR:
                    departmentRowTwo.clearCheck();
                    departmentRowOne.check(R.id.HR);
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
                    break;
                case R.id.Purchasing:
                    departmentRowOne.clearCheck();
                    departmentRowTwo.check(R.id.Purchasing);
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
        displayToast(spinnerLabel);
    }

    // Interface callback for when no spinner item is selected.
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Do nothing.
    }

}

