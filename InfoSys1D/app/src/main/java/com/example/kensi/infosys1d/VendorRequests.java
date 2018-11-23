package com.example.kensi.infosys1d;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Base64;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VendorRequests {

    public static void request_call_me(final Context context, final String storeID, final VolleyCallback callback) {
        try {
            // Define the url
            String endpoint = "/inventory/listItem/" + storeID;
            String url = RequestUtils.BASE_URL + endpoint;

            // Send form POST request
            RequestUtils.sendGetStringReq(context,url,callback);

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
            // Define the url
            String endpoint = "/inventory/add";
            String url = RequestUtils.BASE_URL + endpoint;

            // Make params
            Map<String, String> params = new HashMap<>();
            params.put("item_name", itemName);
            params.put("description", description);
            params.put("category", category);
            params.put("price", price);

            // Send form POST request
            RequestUtils.sendPostStringReq(context, url, params, callback);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MYAPP", "exception", e);
//            return "check log";
        }
    }

    public static void updateProduct(final Context context, final String itemName, final String description, final String category, final String price, final VolleyCallback callback) {
        try {
            // Define the url
            String endpoint = "/inventory/update";
            String url = RequestUtils.BASE_URL + endpoint;

            // Make params
            Map<String, String> params = new HashMap<>();
            params.put("item_name", itemName);
            params.put("description", description);
            params.put("category", category);
            params.put("price", price);

            // Send form POST request
            RequestUtils.sendPostStringReq(context, url, params, callback);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MYAPP", "exception", e);
//            return "check log";
        }
    }

    public static void removeProduct(final Context context, final String itemName, final VolleyCallback callback) {
        try {
            // Define the url
            String endpoint = "/inventory/remove";
            String url = RequestUtils.BASE_URL + endpoint;

            // Make params
            Map<String, String> params = new HashMap<>();
            params.put("item_name", itemName);

            // Send form POST request
            RequestUtils.sendPostStringReq(context, url, params, callback);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MYAPP", "exception", e);
//            return "check log";
        }
    }

    public static void uploadImage(final Context context, final String itemName, final Bitmap bitmap, final VolleyCallback callback) {
        try {
            // Define the url
            String endpoint = "/inventory/uploadImg";
            String url = RequestUtils.BASE_URL + endpoint;

            // Make params
            Map<String, String> params = new HashMap<>();
            params.put("item_name", itemName);
            params.put("file", imageToString(bitmap));

            // Send form POST request
            RequestUtils.sendPostStringReq(context, url, params, callback);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MYAPP", "exception", e);
//            return "check log";
        }
    }

    private static String imageToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return new String(imgBytes, Charset.forName("UTF-8"));
    }


}
