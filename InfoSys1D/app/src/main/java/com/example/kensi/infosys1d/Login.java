package com.example.kensi.infosys1d;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.preference.PreferenceManager;
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

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.Map;

public class Login extends AppCompatActivity {
    private static final String FCM_ID = "FCM_ID";
    String instanceID;
    int uploaded;
    private static final String SET_COOKIE_KEY = "Set-Cookie";
    private static final String COOKIE_KEY = "Cookie";
    private static final String SESSION_COOKIE = "session";
    private static final String SESSION_COOKIE2 = "remember_token";

    private static SharedPreferences _preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // To support POST/GET request policies
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Default creations
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        _preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String sessionId = _preferences.getString(SESSION_COOKIE, "");
        Log.d("LOGIN_START", "Check Pref: " + sessionId);

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
                //Intent i = new Intent(Login.this, Checkout.class);
                Intent i = new Intent(Login.this, Vendor.class);
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
                            Toast.makeText(Login.this, result, Toast.LENGTH_LONG).show();
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                int status = jsonObject.getInt("status");
                                int isVendor = jsonObject.getInt("is_vendor");
                                Log.d("Login", "Check isVendor: " + String.valueOf(isVendor));
                                if (status == 1) {
                                    //if successful, opens QR code reader
                                    Toast.makeText(Login.this, "Login success", Toast.LENGTH_LONG).show();
                                    Intent i;
                                    i = isVendor == 0 ? new Intent(Login.this, QRreader.class) : new Intent(Login.this, Vendor.class);
                                    startActivity(i);
                                } else if (status == -1) {
                                    Toast.makeText(Login.this, "Wrong E-mail/password", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(Login.this, "Server error", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
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

    public static void checkSessionCookie(Map<String, String> headers) {
        //Log.d("LOGIN", "My cookie: " + headers.get("Set-Cookie"));
        for(String key : headers.keySet()){
            Log.d("LOGIN", "Key: " + key);
            Log.d("LOGIN", "Value: " + headers.get(key));
            if(key.equals(SET_COOKIE_KEY)){
                String cookie = headers.get(SET_COOKIE_KEY);
                Log.d("LOGIN", "My cookie: " + cookie);
                //String currentCookie;
                //currentCookie = cookie.startsWith(SESSION_COOKIE) ? SESSION_COOKIE : "";
                //currentCookie = cookie.startsWith(SESSION_COOKIE2) ? SESSION_COOKIE2 : "";
                //if(currentCookie == "") return;

                if (cookie.length() > 0) {
                    String[] splitCookie = cookie.split(";");
                    String[] splitSessionId = splitCookie[0].split("=");
                    cookie = splitSessionId[1];
                    SharedPreferences.Editor prefEditor = _preferences.edit();
                    prefEditor.putString(SESSION_COOKIE, cookie);
                    prefEditor.commit();
                    Log.d("LOGIN", "Pref Save: " + _preferences.getString(SESSION_COOKIE, ""));
                }
            }
        }
    }

    /**
     * Adds session cookie to headers if exists.
     * @param headers
     */
    public static void addSessionCookie(Map<String, String> headers) {
        String sessionId = _preferences.getString(SESSION_COOKIE, "");
        Log.d("LOGIN", "Key: " + SESSION_COOKIE);
        Log.d("LOGIN", "Value: " + sessionId);
        if (sessionId.length() > 0) {
            StringBuilder builder = new StringBuilder();
            builder.append(SESSION_COOKIE);
            builder.append("=");
            builder.append(sessionId);
            if (headers.containsKey(COOKIE_KEY)) {
                builder.append("; ");
                builder.append(headers.get(COOKIE_KEY));
            }
            headers.put(COOKIE_KEY, builder.toString());
        }
        /*
        String remember_token = _preferences.getString(SESSION_COOKIE2, "");
        if (remember_token.length() > 0) {
            StringBuilder builder = new StringBuilder();
            builder.append(SESSION_COOKIE2);
            builder.append("=");
            builder.append(remember_token);
            if (headers.containsKey(COOKIE_KEY)) {
                builder.append("; ");
                builder.append(headers.get(COOKIE_KEY));
            }
            headers.put(COOKIE_KEY, builder.toString());
        }
        */
    }


}
