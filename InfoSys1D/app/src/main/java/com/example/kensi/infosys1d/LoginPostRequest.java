package com.example.kensi.infosys1d;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class LoginPostRequest {

    // Define all the constant
    private static String BASE_URL = "https://chocolatepie.tech";
    private static org.json.simple.JSONObject response;
    private static JSONObject testResponse;


    public static void login(final Context context, final String password, final String email, boolean remember, final VolleyCallback callback) {
        try {
            // Convert boolean to 1 or 0
            final String strRemember;
            if (remember) {
                strRemember = "1";
            } else {
                strRemember = "0";
            }

            // Get the RequestQueue and Response.ErrorListener instance(Singleton)
            RequestQueue queue = SingletonRequestQueue.getInstance(context).getRequestQueue();
            Response.ErrorListener errorListener = SingletonRequestQueue.getInstance(context).getErrorListener();

            // Define the url
            String endpoint = "/admin/login";
            String url = BASE_URL + endpoint;

            // Make form POST request
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                // Response Handler
                @Override
                public void onResponse(String result) {
                    VolleyLog.wtf(result);
//                    Toast.makeText(context, result, Toast.LENGTH_LONG).show();
                    callback.onSuccessResponse(result);
                }
            }, errorListener) {

                // Set the task priority
                @Override
                public Priority getPriority() {
                    return Priority.IMMEDIATE;
                }

                // Set the form parameters
                @Override
                public Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", email);
                    params.put("password", password);
                    params.put("remember", strRemember);
                    return params;
                }

                // Set the Header of the POST request
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                    Login.addSessionCookie(headers);
                    return headers;
                }

                // Define the Response Content Type
                @Override
                public String getBodyContentType() {
                    return "application/json";
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    // since we don't know which of the two underlying network vehicles
                    // will Volley use, we have to handle and store session cookies manually
                    Login.checkSessionCookie(response.headers);
                    return super.parseNetworkResponse(response);
                }
            };

            // Add the POST form request to the Volley RequestQueue
            queue.add(stringRequest);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MYAPP", "exception", e);
//            return "check log";
        }
    }


    public static void registration(final Context context, final String password, final String email, final String username, final boolean vendor, final VolleyCallback callback) {
        try {
            // Convert boolean to 1 or 0
            final String strVendor;
            if (vendor) {
                strVendor = "1";
            } else {
                strVendor = "0";
            }

            // Get the RequestQueue and Response.ErrorListener instance(Singleton)
            RequestQueue queue = SingletonRequestQueue.getInstance(context).getRequestQueue();
            Response.ErrorListener errorListener = SingletonRequestQueue.getInstance(context).getErrorListener();

            // Define the url
            String endpoint = "/admin/register";
            String url = BASE_URL + endpoint;

            // Make form POST request
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                // Response Handler
                @Override
                public void onResponse(String result) {
                    VolleyLog.wtf(result);
//                    Toast.makeText(context, result, Toast.LENGTH_LONG).show();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String status = Integer.toString(jsonObject.getInt("status"));
                        callback.onSuccessResponse(status);
                    } catch (JSONException e) {
                        Log.e("MYAPP", "Parse exception", e);
                    }
                }
            }, errorListener) {

                // Set the task priority
                @Override
                public Priority getPriority() {
                    return Priority.IMMEDIATE;
                }

                // Set the form parameters
                @Override
                public Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", email);
                    params.put("password", password);
                    params.put("username", username);
                    params.put("is_vendor", strVendor);
                    return params;
                }

                // Set the Header of the POST request
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                    Login.addSessionCookie(headers);
                    return headers;
                }

                // Define the Response Content Type
                @Override
                public String getBodyContentType() {
                    return "application/json";
                }
            };

            // Add the POST form request to the Volley RequestQueue
            queue.add(stringRequest);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MYAPP", "exception", e);
//            return "check log";
        }
    }

    public static String registrationChecker(String email, String username, String password0, String password1) {
        String errorMsg = longChecker(email, password0);
        if (!errorMsg.equals("no_error")) {
            return errorMsg;
        } else if (username.length() < 6) {
            return "Username too short";
        } else if (username.length() > 64) {
            return "Username too long";
        } else if (!password0.equals(password1)) {
            return "Passwords do not match";
        } else {
            return "no_error";
        }
    }

    //checks email and password
    public static String longChecker(String email, String password) {
        if (!emailCheck(email)) {
            return "E-mail wrong format";
        }
        if (password.length() < 6) {
            return "Password too short";
        }
        if (password.length() > 128) {
            return "Password too long";
        } else {
            return "no_error";
        }
    }

    public static boolean emailCheck(String email) {
        if (email.length() < 7 || email.length() > 128) {
            return false;
        }
        char[] emailChar = email.toCharArray();
        int[] unwanted = {32, 40, 41, 60, 62, 72, 73};
        for (char c : emailChar) {
            int ascii = (int) c;
            if (ascii < 0 || ascii > 176 || contains(unwanted, ascii)) {
                return false;
            }
        }
        if (!email.contains(".")) {
            return false;
        }
        String[] atCheck = email.split("@");
        if (atCheck.length != 2) {
            return false;
        }
        if (atCheck[0].length() < 1 || atCheck[0].length() > 64 || atCheck[1].length() < 1) {
            return false;
        }
        return true;
    }


    public static boolean contains(int[] arr, int check) {
        for (int n : arr) {
            if (n == check) {
                return true;
            }
        }
        return false;
    }

}