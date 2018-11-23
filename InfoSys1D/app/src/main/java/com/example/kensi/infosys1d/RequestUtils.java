package com.example.kensi.infosys1d;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;
import java.util.Map;

public class RequestUtils {
    public static String BASE_URL = "https://chocolatepie.tech";
    private static org.json.simple.JSONObject response;

    public static void sendGetStringReq(final Context context, final String url, final VolleyCallback callback){
        RequestQueue queue = SingletonRequestQueue.getInstance(context).getRequestQueue();
        Response.ErrorListener errorListener = SingletonRequestQueue.getInstance(context).getErrorListener();
        StringRequest ans = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            // Response Handler
            @Override
            public void onResponse(String result) {
                VolleyLog.wtf(result);
                JSONParser parser = new JSONParser();
                try {
                    response = (org.json.simple.JSONObject) parser.parse(result);
                    callback.onSuccessResponse(response.toString());
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

            // Set the Header of the POST request
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                LoginMain.addSessionCookie(headers);
                return headers;
            }

            // Define the Response Content Type
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        queue.add(ans);
    }

    public static void sendPostStringReq(final Context context, final String url, final Map<String, String> params, final VolleyCallback callback){
        RequestQueue queue = SingletonRequestQueue.getInstance(context).getRequestQueue();
        Response.ErrorListener errorListener = SingletonRequestQueue.getInstance(context).getErrorListener();
        StringRequest ans = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            // Response Handler
            @Override
            public void onResponse(String result) {
                VolleyLog.wtf(result);
                JSONParser parser = new JSONParser();
                try {
                    response = (org.json.simple.JSONObject) parser.parse(result);
                    callback.onSuccessResponse(response.toString());
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
                return params;
            }

            // Set the Header of the POST request
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                LoginMain.addSessionCookie(headers);
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
                LoginMain.checkSessionCookie(response.headers);
                return super.parseNetworkResponse(response);
            }
        };

        queue.add(ans);
    }
}
