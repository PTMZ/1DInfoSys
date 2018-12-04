package com.example.kensi.infosys1d.Vendor;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.example.kensi.infosys1d.Product;
import com.example.kensi.infosys1d.RequestUtils;
import com.example.kensi.infosys1d.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VendorRequests {

    public static void request_call_me(final Context context, final String storeID, final VolleyCallback callback) {
        try {
            // Define the url
            String endpoint = "/inventory/listItem" + (storeID.length()>0 ? "/" + storeID : "");
            String url = RequestUtils.BASE_URL + endpoint;

            // Send form POST request
            RequestUtils.sendGetStringReq(context,url,callback);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MYAPP", "exception", e);
        }
    }

    public static List<Product> request_iterate(String serverReply) {
        List<Product> checkoutProductList = new ArrayList<>();
        try {
            JSONArray jsonData = new JSONObject(serverReply).getJSONArray("data");
            for(int i=0; i<jsonData.length(); i++){
                JSONObject curProduct = jsonData.getJSONObject(i);
                checkoutProductList.add(
                        new Product(
                                i+1,
                                curProduct.getString("item_name"),
                                curProduct.getString("description"),
                                curProduct.getString("category"),
                                curProduct.getString("price"),
                                curProduct.getString("image_url"),
                                1));
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

    public static void getJobs(final Context context, final VolleyCallback callback) {
        try {
            // Define the url
            String endpoint = "/sales/getTasks";
            String url = RequestUtils.BASE_URL + endpoint;

            // Send form POST request
            RequestUtils.sendGetStringReq(context, url, callback);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MYAPP", "exception", e);
//            return "check log";
        }
    }

    public static List<Job> jobs_iterate(String serverReply) {
        List<Job> jobList = new ArrayList<>();
        try {
            JSONArray jsonData = new JSONObject(serverReply).getJSONArray("jobs");
            for(int i=0; i<jsonData.length(); i++){
                JSONObject curJob = jsonData.getJSONObject(i);
                jobList.add(new Job(
                        curJob.getInt("task_id"),
                        curJob.getString("item_name"),
                        curJob.getInt("qty"),
                        curJob.getInt("table_id")
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jobList;
    }

    public static void removeJob(final Context context, final int taskId, final VolleyCallback callback) {
        try {
            // Define the url
            String endpoint = "/sales/removeTask";
            String url = RequestUtils.BASE_URL + endpoint;

            // Make params
            Map<String, String> params = new HashMap<>();
            params.put("task_id", String.valueOf(taskId));

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
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }


}
