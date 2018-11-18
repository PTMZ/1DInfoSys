package com.example.kensi.infosys1d;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    private static final String FCM_ID = "FCM_ID";
    String instanceID;
    int uploaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // To support POST/GET request policies
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Default creations
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Input instantiations from UI
        final EditText inputEmail = findViewById(R.id.inputRegUser);
        final EditText inputPassword = findViewById(R.id.inputRegEmail);
        final CheckBox checkRemember = findViewById(R.id.checkRemember);
        final Button buttonSubmit = findViewById(R.id.buttonRegSubmit);
        final Button buttonForget = findViewById(R.id.buttonForget);
        final Button buttonSign = findViewById(R.id.buttonSign);

        // Get data from SharedPreference
        SharedPreferences settings = getSharedPreferences(FCM_ID, MODE_PRIVATE);
        instanceID = settings.getString("ID", "");
        uploaded = settings.getInt("uploaded", 0);

        // Save the instance ID if it has not saved into the SharedPreference
        if (instanceID.equals("")) {
            setFCMInstanceID();
        }
        Log.d("Token", instanceID);

        // Define all the UI listener
        buttonSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, Registration.class);
                startActivity(i);
            }
        });

        //temporary button to open checkout menu
        //todo change function
        buttonForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, Checkout.class);
                startActivity(i);
            }
        });

        // Submission press
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmail.getText().toString();
                // Check email syntax
                String errorMsg = LoginPostRequest.longChecker(email, inputPassword.getText().toString());
                if (!errorMsg.equals("no_error")) {
                    // Display error message if the email syntax is invalid
                    Toast.makeText(Login.this, errorMsg, Toast.LENGTH_LONG).show();
                } else {
                    // Get the data from the UI
                    String password = inputPassword.getText().toString();
                    Boolean remember = checkRemember.isChecked();

                    // Make POST request to /admin/login
                    LoginPostRequest.login(getApplicationContext(), password, email, remember, new VolleyCallback() {
                        //decides what to do from post request reponse
                        @Override
                        public void onSuccessResponse(String result) {
                            int status = Integer.parseInt(result);
                            if (status == 1) {
                                //if successful, opens QR code reader
                                Toast.makeText(Login.this, "Login success", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(Login.this, QRreader.class);
                                startActivity(i);
                            } else if (status == -1) {
                                Toast.makeText(Login.this, "Wrong E-mail/password", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(Login.this, "Server error", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }
            }
        });
    }


    /*
        This private method is used to save the FCM Instance ID into SharedPreference.
        Saved the FCM Instance ID into SharedPreference.
        SharedPreference ID: FCM_ID
        key: ID (Instance ID)
        key: uploaded (Flag for uploading the Instance ID to database)
     */
    private void setFCMInstanceID() {

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    Log.w("access_token", "getInstanceId failed", task.getException());
                    return;
                }

                // Get new Instance ID token
                instanceID = task.getResult().getToken();

                // Save into SharedPreference
                SharedPreferences FCM = getSharedPreferences(FCM_ID, MODE_PRIVATE);
                SharedPreferences.Editor editor = FCM.edit();
                editor.putString("ID", instanceID);
                editor.putInt("uploaded", 0);
                editor.apply();

                // Log
                String msg = "Access Token: " + instanceID;
                Log.d("access_token", msg);
            }
        });
    }



}
