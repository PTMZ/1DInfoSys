package com.example.kensi.infosys1d;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class Registration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        final EditText inputEmail = findViewById(R.id.inputRegEmail);
        final EditText inputPassword = findViewById(R.id.inputRegPassword);
        final Button buttonSubmit = findViewById(R.id.buttonRegSubmit);
        final EditText inputPassword2 = findViewById(R.id.inputRegPassword2);
        final EditText inputUser = findViewById(R.id.inputRegUser);
        final CheckBox checkVendor = findViewById(R.id.checkRegVendor);

        //Back button implementation
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Submission press
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmail.getText().toString();
                String errorMsg = LoginPostRequest.longChecker(email, inputPassword.getText().toString());
                if (!errorMsg.equals("no_error")) {
                    Toast.makeText(Registration.this, errorMsg, Toast.LENGTH_LONG).show();
                } else if (inputUser.getText().toString().length() < 6 && inputUser.getText().toString().length() < 64) {
                    Toast.makeText(Registration.this, R.string.username_short, Toast.LENGTH_LONG).show();
                } else if (inputUser.getText().toString().length() > 64) {
                    Toast.makeText(Registration.this, R.string.username_long, Toast.LENGTH_LONG).show();
                } else if (!inputPassword.getText().toString().equals(inputPassword2.getText().toString())) {
                    Toast.makeText(Registration.this, R.string.password_mismatch, Toast.LENGTH_LONG).show();
                } else {
                    String reply = LoginPostRequest.registration(inputPassword.getText().toString(), email, inputUser.getText().toString(), checkVendor.isChecked());
                    String parsedReply = LoginPostRequest.jsonParse(reply);
                    if (parsedReply.equals("1")) {
                        Toast.makeText(Registration.this, "Registration success", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(Registration.this, Login.class);
                        startActivity(i);
                    } else if (parsedReply.equals("0")) {
                        Toast.makeText(Registration.this, "Server error", Toast.LENGTH_LONG).show();
                    } else if (parsedReply.equals("-1")) {
                        Toast.makeText(Registration.this, "E-mail already used", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Registration.this, "Generic error", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }
        //Back button function
        public boolean onOptionsItemSelected (MenuItem item) {
            Intent myIntent = new Intent(getApplicationContext(), Login.class);
            startActivityForResult(myIntent, 0);
            return true;
        }

}
