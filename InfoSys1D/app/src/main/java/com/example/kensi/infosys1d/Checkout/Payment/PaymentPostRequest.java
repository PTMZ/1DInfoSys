package com.example.kensi.infosys1d.Checkout.Payment;

import android.content.Context;
import android.util.Log;

import com.example.kensi.infosys1d.RequestUtils;
import com.example.kensi.infosys1d.VolleyCallback;

import org.json.JSONObject;

import java.util.List;

public class PaymentPostRequest {

    public static void postPayment(final Context context, final List<JSONObject> orders,
                                   final String vendor_id, final String table_id,
                                   final VolleyCallback callback) {
        try {
            // Define the url
            String endpoint = "/sales/submit";
            String url = RequestUtils.BASE_URL + endpoint;

            JSONObject JSON_params = new JSONObject();

            JSON_params.put("orders", orders.toString());
            JSON_params.put("vendor_id", vendor_id);
            JSON_params.put("table_id", table_id);

            // Send form POST request
            RequestUtils.sendPostJSONReq(context, url, JSON_params, callback);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MYAPP", "exception", e);
//            return "check log";
        }
    }
}
