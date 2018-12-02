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
                                        curProduct.getString("description"),
                                        curProduct.getString("category"),
                                        curProduct.getString("price"),
                                        curProduct.getString("image_url"),
                                0));

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

}
