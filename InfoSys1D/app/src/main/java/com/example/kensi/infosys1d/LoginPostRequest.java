package com.example.kensi.infosys1d;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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


    public static void login(final Context context, final String password, final String email, boolean remember) {
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
                    JSONParser parser = new JSONParser();
                    try {
                        response = (org.json.simple.JSONObject) parser.parse(result);
                        Long status = (Long) response.get("status");
                        if (status == 1) {
                            Toast.makeText(context, "Login success", Toast.LENGTH_LONG).show();
                        } else if (status == -1) {
                            Toast.makeText(context, "Wrong E-mail/password", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "Server error", Toast.LENGTH_LONG).show();
                        }
                    } catch (ParseException e) {
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
                    params.put("remember", strRemember);
                    return params;
                }

                // Set the Header of the POST request
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
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


    public static String login_call_me(String password, String email, String rememberString) throws Exception {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("password", password);
        parameters.put("email", email);
        parameters.put("remember", rememberString);
        String data = ParameterStringBuilder.getParamsString(parameters);
        String url = "https://chocolatepie.tech/admin/login";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes(data);
        out.flush();
        out.close();
        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }


    public static String registration(String password, String email, String username, boolean vendor) {
        try {
            String vendorString;
            if (vendor) {
                vendorString = "1";
            } else {
                vendorString = "0";
            }
            return registration_call_me(password, email, username, vendorString);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MYAPP", "exception", e);
            return "check log";
        }
    }


    public static String registration_call_me(String password, String email, String username, String vendorString) throws Exception {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("password", password);
        parameters.put("email", email);
        parameters.put("username", username);
        parameters.put("is_vendor", vendorString);
        String data = ParameterStringBuilder.getParamsString(parameters);
        String url = "https://chocolatepie.tech/admin/register";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes(data);
        out.flush();
        out.close();
        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }


    //not implemented
    public static boolean shortChecker(String email, String password) {
        if (email.length() < 128 && email.length() > 5 && password.length() > 6 && password.length() < 128) {
            return true;
        } else {
            return false;
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


    public static String jsonParse(String serverReply) {
        if (serverReply != null) {
            try {
                JSONObject jsonObj = new JSONObject(serverReply);
                String ans = jsonObj.getString("status");
                return ans;
            } catch (final JSONException e) {
                return "Generic Error";
            }

            } else {
            return "Server Error";
        }
    }
}