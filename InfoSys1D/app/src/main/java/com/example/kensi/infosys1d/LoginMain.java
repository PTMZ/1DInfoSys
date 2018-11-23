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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class LoginMain extends AppCompatActivity {
    private static final String FCM_ID = "FCM_ID";
    String instanceID;
    int uploaded;
    private static final String SET_COOKIE_KEY = "Set-Cookie";
    private static final String COOKIE_KEY = "Cookie";
    private static final String SESSION_COOKIE = "session";
    //private static final String SESSION_COOKIE2 = "remember_token";
    private static final  int CHECKOUT_REQ = 1;
    private static final  int VENDOR_REQ = 2;

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
        // SharedPreferences.Editor prefEditor = _preferences.edit();
        // prefEditor.clear();
        // prefEditor.apply();
        String sessionId = _preferences.getString(SESSION_COOKIE, "");
        String emailPref = _preferences.getString("email", "");
        String passwordPref = _preferences.getString("password", "");
        if(passwordPref.length() > 0){
            loginRequest(passwordPref, emailPref, true);
        }
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
                Intent i = new Intent(LoginMain.this, RegistrationMain.class);
                startActivity(i);
            }
        });

        //temporary button to open checkout menu
        //todo change function
        buttonForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i = new Intent(LoginMain.this, CheckoutMain.class);
                Intent i = new Intent(LoginMain.this, CheckoutMain.class);
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
                    Boolean remember = checkRemember.isChecked();

                    // Make POST request to /admin/login
                    loginRequest(password, email, remember);

                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CHECKOUT_REQ){
            if(data!= null && data.getBooleanExtra("BackPress", false)){
                finish();
            }
        }
        else if(requestCode == VENDOR_REQ){
            if(data!=null && data.getBooleanExtra("BackPress", false)){
                finish();
            }
        }
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
                if(cookie == null) return;
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

    public void loginRequest(final String password, final String email, final boolean remember){
        LoginPostRequest.login(getApplicationContext(), password, email, remember, new VolleyCallback() {
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
                        i = (isVendor == 0) ? new Intent(LoginMain.this, CheckoutMain.class) : new Intent(LoginMain.this, Vendor.class);
                        int reqCode = (isVendor == 0) ? CHECKOUT_REQ : VENDOR_REQ;
                        SharedPreferences.Editor prefEditor = _preferences.edit();
                        if(remember){
                            prefEditor.putString("email",email);
                            prefEditor.putString("password",password);
                            prefEditor.apply();
                        }
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
