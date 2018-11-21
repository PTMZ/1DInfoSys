package com.example.kensi.infosys1d;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VendorRequests {

    private static String BASE_URL = "https://chocolatepie.tech";
    private static org.json.simple.JSONObject response;

    public static void request_call_me(final Context context, final String storeID, final VolleyCallback callback) {
        try {
            // Get the RequestQueue and Response.ErrorListener instance(Singleton)
            RequestQueue queue = SingletonRequestQueue.getInstance(context).getRequestQueue();
            Response.ErrorListener errorListener = SingletonRequestQueue.getInstance(context).getErrorListener();

            // Define the url
            String endpoint = "/inventory/listItem/" + storeID;
            String url = BASE_URL + endpoint;

            // Make form POST request
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

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
                    return Priority.HIGH;
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
        }
    }

    public static List<CheckoutProduct> request_iterate(String serverReply) {
        List<CheckoutProduct> checkoutProductList = new ArrayList<>();
        try {
            JSONArray jsonData = new JSONObject(serverReply).getJSONArray("data");
            for(int i=0; i<jsonData.length(); i++){
                JSONObject curProduct = jsonData.getJSONObject(i);
                checkoutProductList.add(
                        new CheckoutProduct(
                                i+1,
                                curProduct.getString("item_name"),
                                curProduct.getString("description"),
                                curProduct.getString("category"),
                                curProduct.getString("price"),
                                R.drawable.burger, 1));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return checkoutProductList;
    }


    public static void addProduct(final Context context, final String itemName, final String description, final String category, final String price, final VolleyCallback callback) {
        try {

            // Get the RequestQueue and Response.ErrorListener instance(Singleton)
            RequestQueue queue = SingletonRequestQueue.getInstance(context).getRequestQueue();
            Response.ErrorListener errorListener = SingletonRequestQueue.getInstance(context).getErrorListener();

            // Define the url
            String endpoint = "/inventory/add";
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
                        callback.onSuccessResponse(String.valueOf(status));
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
                    params.put("item_name", itemName);
                    params.put("description", description);
                    params.put("category", category);
                    params.put("price", price);
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
            };

            // Add the POST form request to the Volley RequestQueue
            queue.add(stringRequest);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MYAPP", "exception", e);
//            return "check log";
        }
    }

    public static void updateProduct(final Context context, final String itemName, final String description, final String category, final String price, final VolleyCallback callback) {
        try {

            // Get the RequestQueue and Response.ErrorListener instance(Singleton)
            RequestQueue queue = SingletonRequestQueue.getInstance(context).getRequestQueue();
            Response.ErrorListener errorListener = SingletonRequestQueue.getInstance(context).getErrorListener();

            // Define the url
            String endpoint = "/inventory/update";
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
                        callback.onSuccessResponse(String.valueOf(status));
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
                    params.put("item_name", itemName);
                    params.put("description", description);
                    params.put("category", category);
                    params.put("price", price);
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
            };

            // Add the POST form request to the Volley RequestQueue
            queue.add(stringRequest);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MYAPP", "exception", e);
//            return "check log";
        }
    }

    public static void removeProduct(final Context context, final String itemName, final VolleyCallback callback) {
        try {

            // Get the RequestQueue and Response.ErrorListener instance(Singleton)
            RequestQueue queue = SingletonRequestQueue.getInstance(context).getRequestQueue();
            Response.ErrorListener errorListener = SingletonRequestQueue.getInstance(context).getErrorListener();

            // Define the url
            String endpoint = "/inventory/remove";
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
                        callback.onSuccessResponse(String.valueOf(status));
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
                    params.put("item_name", itemName);
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
            };

            // Add the POST form request to the Volley RequestQueue
            queue.add(stringRequest);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MYAPP", "exception", e);
//            return "check log";
        }
    }

    public static void uploadImage(final Context context, final File fileBinaryData, final String itemName, String storeId, final VolleyCallback callback) {
        try {
            // Get the RequestQueue and Response.ErrorListener instance(Singleton)
            RequestQueue queue = SingletonRequestQueue.getInstance(context).getRequestQueue();
            Response.ErrorListener errorListener = SingletonRequestQueue.getInstance(context).getErrorListener();

            // Define the url
            String endpoint = "/inventory/uploadImg";
            String url = BASE_URL + endpoint;


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MYAPP", "exception", e);
//            return "check log";
        }
    }

}
