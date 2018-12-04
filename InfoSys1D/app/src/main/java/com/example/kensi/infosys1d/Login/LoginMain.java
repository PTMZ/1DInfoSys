package com.example.kensi.infosys1d.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kensi.infosys1d.Checkout.CheckoutMain;
import com.example.kensi.infosys1d.Menu.MenuMain;
import com.example.kensi.infosys1d.Menu.MenuRequest;
import com.example.kensi.infosys1d.PaymentConfirmationMain;
import com.example.kensi.infosys1d.QRreader.QRreaderMain;
import com.example.kensi.infosys1d.R;
import com.example.kensi.infosys1d.Registration.RegistrationMain;
import com.example.kensi.infosys1d.Vendor.VendorMain;
import com.example.kensi.infosys1d.VolleyCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class LoginMain extends AppCompatActivity {
    private static String deviceID;
    int uploaded;
    private static final String SET_COOKIE_KEY = "Set-Cookie";
    private static final String COOKIE_KEY = "Cookie";
    private static final String SESSION_COOKIE = "session";
    //private static final String SESSION_COOKIE2 = "remember_token";
    private static final int CHECKOUT_REQ = 1;
    private static final int VENDOR_REQ = 2;

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
        //SharedPreferences.Editor prefEditor = _preferences.edit();
        //prefEditor.clear();
        //prefEditor.apply();
        String sessionId = _preferences.getString(SESSION_COOKIE, "");
        String emailPref = _preferences.getString("email", "");
        String passwordPref = _preferences.getString("password", "");
        deviceID = _preferences.getString("deviceID", "");
        if(deviceID.equals("")) {
            setFCMInstanceID();
        }
        if (passwordPref.length() > 0) {
            loginRequest(passwordPref, emailPref, deviceID);
        }
        Log.d("LOGIN_START", "Check Pref: " + sessionId);

        //Input instantiations from UI
        final EditText inputEmail = findViewById(R.id.inputRegUser);
        final EditText inputPassword = findViewById(R.id.inputRegEmail);
        final CheckBox checkRemember = findViewById(R.id.checkRemember);
        final Button buttonSubmit = findViewById(R.id.buttonRegSubmit);
        final Button buttonTest = findViewById(R.id.buttonTest);
        final Button buttonSign = findViewById(R.id.buttonSign);


        // Define all the UI listener
        buttonSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginMain.this, RegistrationMain.class);
                startActivity(i);
            }
        });

        //temporary button
        //todo remove test button
        buttonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i = new Intent(LoginMain.this, CheckoutMain.class);
//                MenuRequest.request_call_me(LoginMain.this, "cffde47dcc0f3f7a92ae96e1650d5b306382ce6e97bd14373b3aa96ffe54a986219e5b0e0632d7bb899c8a5d5ccea092beee41e2798c9dddfa03e11b71083080", new VolleyCallback() {
//                    @Override
//                    public void onSuccessResponse(String result) {
//                        Intent i = new Intent(LoginMain.this, MenuMain.class);
//                        i.putExtra("ServerResult", result);
//                        startActivity(i);
//                    }
//                });
                Intent i = new Intent(LoginMain.this, PaymentConfirmationMain.class);
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
                    Toast.makeText(LoginMain.this, errorMsg, Toast.LENGTH_LONG).show();
                } else {
                    // Get the data from the UI
                    String password = inputPassword.getText().toString();

                    // Make POST request to /admin/login
                    loginRequest(password, email, deviceID);

                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHECKOUT_REQ) {
            if (data != null && data.getBooleanExtra("BackPress", false)) {
                finish();
            }
        } else if (requestCode == VENDOR_REQ) {
            if (data != null && data.getBooleanExtra("BackPress", false)) {
                finish();
            }
        }
    }

    private static void setFCMInstanceID() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    Log.w("access_token", "getInstanceId failed", task.getException());
                    return;
                }
                // Get new device ID
                LoginMain.deviceID = task.getResult().getToken();
                // Save the device ID
                SharedPreferences.Editor prefEditor = _preferences.edit();
                prefEditor.putString("deviceID", deviceID);
                prefEditor.apply();
                // Log
                String msg = "Access Token: " + deviceID;
                Log.d("access_token", msg);
            }
        });
    }

    public static void checkSessionCookie(Map<String, String> headers) {
        //Log.d("LOGIN", "My cookie: " + headers.get("Set-Cookie"));
        for (String key : headers.keySet()) {
            Log.d("LOGIN", "Key: " + key);
            Log.d("LOGIN", "Value: " + headers.get(key));
            if (key.equals(SET_COOKIE_KEY)) {
                String cookie = headers.get(SET_COOKIE_KEY);
                if (cookie == null) return;
                Log.d("LOGIN", "My cookie: " + cookie);

                if (cookie.length() > 0) {
                    String[] splitCookie = cookie.split(";");
                    String[] splitSessionId = splitCookie[0].split("=");
                    cookie = splitSessionId[1];
                    SharedPreferences.Editor prefEditor = _preferences.edit();
                    prefEditor.putString(SESSION_COOKIE, cookie);
                    prefEditor.apply();
                }
            }
        }
    }

    public static void addSessionCookie(Map<String, String> headers) {
        String sessionId = _preferences.getString(SESSION_COOKIE, "");
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
    }

    public static void removeSessionCookie() {
        SharedPreferences.Editor prefEditor = _preferences.edit();
        prefEditor.remove(SESSION_COOKIE);
        prefEditor.remove("email");
        prefEditor.remove("password");
        prefEditor.apply();
    }

    public void loginRequest(final String password, final String email, final String deviceID) {
        LoginPostRequest.login(getApplicationContext(), password, email, deviceID, new VolleyCallback() {
            //decides what to do from post request reponse
            @Override
            public void onSuccessResponse(String result) {
                Toast.makeText(LoginMain.this, result, Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int status = jsonObject.getInt("status");
                    int isVendor = jsonObject.getInt("is_vendor");
                    Log.d("LoginMain", "Check isVendor: " + String.valueOf(isVendor));
                    if (status == 1) {
                        //if successful, opens QR code reader
                        Toast.makeText(LoginMain.this, "LoginMain success", Toast.LENGTH_LONG).show();
                        Intent i;
                        i = (isVendor == 0) ? new Intent(LoginMain.this, QRreaderMain.class) : new Intent(LoginMain.this, VendorMain.class);
                        int reqCode = (isVendor == 0) ? CHECKOUT_REQ : VENDOR_REQ;
                        SharedPreferences.Editor prefEditor = _preferences.edit();
                        prefEditor.putString("email", email);
                        prefEditor.putString("password", password);
                        prefEditor.apply();
                        startActivityForResult(i, reqCode);

                    } else if (status == -1) {
                        Toast.makeText(LoginMain.this, "Wrong E-mail/password", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(LoginMain.this, "Server error", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

}
