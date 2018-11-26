package com.example.kensi.infosys1d.Menu;

import android.content.Context;

import com.example.kensi.infosys1d.Checkout.CheckoutMain;
import com.example.kensi.infosys1d.Product;
import com.example.kensi.infosys1d.R;
import com.example.kensi.infosys1d.Vendor.VendorRequests;
import com.example.kensi.infosys1d.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MenuRequest {

    //String from Json is passed through here
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
                                        //todo modify the below line
                                        Double.parseDouble(curProduct.getString("price")),
                                        curProduct.getString("image_url")));

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
