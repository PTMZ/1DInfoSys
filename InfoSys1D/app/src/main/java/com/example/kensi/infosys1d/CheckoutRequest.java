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
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class CheckoutRequest {

    private static String BASE_URL = "https://chocolatepie.tech";
    private static org.json.simple.JSONObject response;

    //String from Json is passed through here
    public static List<Product> request_iterate(List<Product> productList, String[] check, int[] qty, String serverReply) {
        int itemNum = qty.length-1;
        //Creates map: Request_check helps sieve out only items in 'check' and grabs additional data from serverReply
        Map<String, String> map = request_check(check, serverReply);
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            //Splits each 'value' from the hashmap from request_check
            //value 0 = description
            //value 1 = image URL
            //value 2 = price
            String value[] = pair.getValue().toString().split("@@@");
            //adds each item into producList Hashmap to be shown via recycleview
            productList.add(
                    new Product(
                            itemNum,
                            pair.getKey().toString(),
                            value[0],
                            Checkout.priceConversion(Double.parseDouble(value[2])),
                            R.drawable.burger, qty[itemNum]));
            itemNum--;
            it.remove();
        }
        return productList;
    }

    //Request_check helps sieve out only items in 'check' and grabs additional data from serverReply
    public static Map<String, String> request_check(String[] check, String serverReply) {
        try {
            JSONArray jsonData = new JSONObject(serverReply).getJSONArray("data");
            Map<String, String> map = new HashMap<String, String>();
            //goes through each item in JSON data
            for (int i = 0; i < jsonData.length() - 1; i++) {
                //check if it's inside String Array 'check'
                if (stringContains(check, jsonData.getJSONObject(i).getString("item_name"))) {
                    //if inside String array, it gets added to Hashmap 'map' with additional details from JsonData. All seperated with '@@@' so it's easier to seperate later on
                    String value = jsonData.getJSONObject(i).getString("description") + "@@@" + jsonData.getJSONObject(i).getString("image_url") + "@@@" + jsonData.getJSONObject(i).getString("price");
                    map.put(jsonData.getJSONObject(i).getString("item_name"), value);
                    Log.d(TAG, "Json File Spits: "+ value);
                }
            }
            return map;

        } catch (
                Exception e) {
            e.printStackTrace();
            Log.e("MYAPP", "exception", e);
            Map<String, String> map = new HashMap<String, String>();
            return map;
        }

    }

    //access the json file from the server
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
                    return Priority.IMMEDIATE;
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

    //converts hashmap's value to an int array
    public static int[] valueMapToArray(Map<String, Integer> checkMap) {
        int[] check = new int[checkMap.size()];
        int i = 0;
        Iterator checkIt = checkMap.entrySet().iterator();
        while (checkIt.hasNext()) {
            Map.Entry pair = (Map.Entry) checkIt.next();
            check[i] = Integer.valueOf(pair.getValue().toString());
            i++;
        }
        return check;
    }


    //converts hashmap's key to a string array
    public static String[] keyMapToArray(Map<String, Integer> checkMap) {
        String[] check = new String[checkMap.size()];
        int i = 0;
        Iterator checkIt = checkMap.entrySet().iterator();
        while (checkIt.hasNext()) {
            Map.Entry pair = (Map.Entry) checkIt.next();
            check[i] = pair.getKey().toString();
            i++;
        }
        return check;
    }

    //Find out if a String array contains a particular string
    public static boolean stringContains(String[] arr, String check) {
        for (String n : arr) {
            if (n.equals(check)) {
                return true;
            }
        }
        return false;
    }
}
