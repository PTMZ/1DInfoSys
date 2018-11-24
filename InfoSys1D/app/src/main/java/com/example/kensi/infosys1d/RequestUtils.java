package com.example.kensi.infosys1d;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RequestUtils {
    public static String BASE_URL = "https://chocolatepie.tech";
    public static String S3_BUCKET = "tech.chocolatepie";
    public static String POOL_ID = "ap-southeast-1:5608e437-bc6d-40ad-b5fb-7e177d00bbfd";
    private static org.json.simple.JSONObject response;

    private static AmazonS3 s3;
    private static TransferUtility transferUtility;

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

    public static void downloadFile(final Context context, String downloadKey, ImageView imgView){

        credentialsProvider(context);

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR, +1);
        Date oneHourLater = cal.getTime();
        URL url = s3.generatePresignedUrl(
                S3_BUCKET,
                downloadKey,
                oneHourLater
        );
        String urlString = url.toString();
        Glide.with(context).load(urlString).into(imgView);
    }

    private static void credentialsProvider(final Context context){
        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                context,
                POOL_ID, // Identity Pool ID
                Regions.AP_SOUTHEAST_1 // Region
        );
        setAmazonS3Client(credentialsProvider);
    }

    private static void setAmazonS3Client(CognitoCachingCredentialsProvider credentialsProvider){
        // Create an S3 client
        s3 = new AmazonS3Client(credentialsProvider);
        // Set the region of your S3 bucket
        s3.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_1));

    }
}
