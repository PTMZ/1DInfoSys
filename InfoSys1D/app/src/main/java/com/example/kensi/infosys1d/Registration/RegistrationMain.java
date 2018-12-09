package com.example.kensi.infosys1d.Registration;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kensi.infosys1d.Login.LoginMain;
import com.example.kensi.infosys1d.Login.LoginPostRequest;
import com.example.kensi.infosys1d.R;
import com.example.kensi.infosys1d.VolleyCallback;

import org.json.JSONObject;

public class RegistrationMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        final EditText inputEmail = findViewById(R.id.inputRegEmail);
        final EditText inputPassword0 = findViewById(R.id.inputRegPassword0);
        final Button buttonSubmit = findViewById(R.id.buttonRegSubmit);
        final EditText inputPassword1 = findViewById(R.id.inputRegPassword1);
        final EditText inputUser = findViewById(R.id.inputRegUser);
        final CheckBox checkVendor = findViewById(R.id.checkRegVendor);
        final EditText inputPhone = findViewById(R.id.inputRegPhone);

        //Back button implementation
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Submission press
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmail.getText().toString();
                //checks registration for error
                String errorMsg = LoginPostRequest.registrationChecker (email, inputUser.getText().toString(), inputPassword0.getText().toString(), inputPassword1.getText().toString());
                if (!errorMsg.equals("no_error")) {
                    Toast.makeText(RegistrationMain.this, errorMsg, Toast.LENGTH_LONG).show();
                } else {
                    LoginPostRequest.registration(getApplicationContext(),
                            inputPassword0.getText().toString(),
                            email,
                            inputUser.getText().toString(),
                            checkVendor.isChecked(),
                            inputPhone.getText().toString(),
                            new VolleyCallback() {
                        //calls for response from postrequest
                        @Override
                        public void onSuccessResponse(String result) {
                            //different scenarios from post request
                            int status = -3;
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                status = jsonObject.getInt("status");
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                            if (status == 1) {
                                //If successful, opens login page
                                Toast.makeText(RegistrationMain.this, "Registration success", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(RegistrationMain.this, LoginMain.class);
                                startActivity(i);
                            } else if (status == 0 ) {
                                Toast.makeText(RegistrationMain.this, "Server error", Toast.LENGTH_LONG).show();
                            } else if (status == -1) {
                                Toast.makeText(RegistrationMain.this, "E-mail already used", Toast.LENGTH_LONG).show();
                            } else if (status == -2) {
                                Toast.makeText(RegistrationMain.this, "Invalid phone number", Toast.LENGTH_LONG).show();
                            }
                            //if cannot find "status" in json return
                            else if (status == -3){
                                Toast.makeText(RegistrationMain.this,"Server Error",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

    }
        //Back button function
        public boolean onOptionsItemSelected (MenuItem item) {
            Intent myIntent = new Intent(getApplicationContext(), LoginMain.class);
            startActivityForResult(myIntent, 0);
            return true;
        }

}
