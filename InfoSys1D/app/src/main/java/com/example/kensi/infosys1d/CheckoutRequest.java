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
    public static List<CheckoutProduct> request_iterate(String[] items, int[] qty, String serverReply) {
        List<CheckoutProduct> checkoutProductList = new ArrayList<>();
        try {
            JSONArray jsonData = new JSONObject(serverReply).getJSONArray("data");
            for(int i=0; i<jsonData.length(); i++){
                JSONObject curProduct = jsonData.getJSONObject(i);
                for(int j=0; j<items.length; j++){
                    if(items[j].equals(curProduct.getString("item_name"))){
                        checkoutProductList.add(
                                new CheckoutProduct(
                                        j+1,
                                        curProduct.getString("item_name"),
                                        curProduct.getString("description"),
                                        curProduct.getString("category"),
                                        CheckoutMain.priceConversion(Double.parseDouble(curProduct.getString("price"))),
                                        R.drawable.burger,
                                        qty[j]));
                    }
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return checkoutProductList;

    }

    //access the json file from the server
    public static void request_call_me(final Context context, final String storeID, final VolleyCallback callback) {
        VendorRequests.request_call_me(context, storeID, callback);
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

}
