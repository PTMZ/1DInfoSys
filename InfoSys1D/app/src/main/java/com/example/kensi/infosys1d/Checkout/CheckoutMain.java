package com.example.kensi.infosys1d.Checkout;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.kensi.infosys1d.Login.LoginMain;
import com.example.kensi.infosys1d.Login.LoginPostRequest;
import com.example.kensi.infosys1d.Menu.MenuMain;
import com.example.kensi.infosys1d.MyClickListener;
import com.example.kensi.infosys1d.Product;
import com.example.kensi.infosys1d.R;
import com.example.kensi.infosys1d.VolleyCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckoutMain extends AppCompatActivity {

    TextView textViewTotalPrice;
    RecyclerView recyclerView;
    CheckoutProductAdapter adapter;
    List<Product> checkoutList;
    private static final String TAG = "CheckoutMain";
    //Testing StoreID, to be passed on from previous activity
    String storeID = "cffde47dcc0f3f7a92ae96e1650d5b306382ce6e97bd14373b3aa96ffe54a986219e5b0e0632d7bb899c8a5d5ccea092beee41e2798c9dddfa03e11b71083080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: checkout");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_main);
        //creation of a list for each individual item
        textViewTotalPrice = findViewById(R.id.textViewTotalPrice);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        //Creation of testing hashmap; this is to be passed on from previous activity in the future
//        Map<String, Integer> checkMap = new HashMap<String, Integer>();
//        checkMap.put("Fish Fillet", 3);
//        checkMap.put("Salted Egg Chicken",5);
//        checkMap.put("Steam Egg", 15);
//
//        //Creation of Array from Item hashmap, as Volley requires final
//        final String[] items = CheckoutRequest_NOT_IN_USE.keyMapToArray(checkMap);
//        final int[] qty = CheckoutRequest_NOT_IN_USE.valueMapToArray(checkMap);
//        //Volley to server
//
//        CheckoutRequest_NOT_IN_USE.request_call_me(CheckoutMain.this, storeID, new VolleyCallback() {
//                    @Override
//                    public void onSuccessResponse(String result) {
//                        //Updates checkoutProductList with full details from items in checkMap
//                        checkoutProductList = CheckoutRequest_NOT_IN_USE.request_iterate(items, qty, result);
//                        //Updates Recycleview
//
//
//                    }
//                });


        //Creates a new list from MenuMain's productList, removing all the QTY=0 elements
        checkoutList = removeZeroQtyList(MenuMain.productList);
        adapter = new CheckoutProductAdapter(CheckoutMain.this, checkoutList, new MyClickListener() {
            @Override
            public void onPositionClicked(int position, String type) {
                // do something
            }
        });
        recyclerView.setAdapter(adapter);
        //Set total value
        textViewTotalPrice.setText(CheckoutMain.priceConversion(Double.valueOf(getPrice(checkoutList))));
    }

    //adds Menu to top bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            LoginPostRequest.logout(CheckoutMain.this, new VolleyCallback() {
                @Override
                public void onSuccessResponse(String result) {
                    LoginMain.removeSessionCookie();
                    finish();
                }
            });
        }
        return true;
    }

    //Calculates total value from individual item Strings
    private String getPrice(List<Product> list) {
        double totalPrice = 0;
        for (Product p : list) {
            totalPrice += Double.valueOf(p.getPrice()) * Double.valueOf(p.getQty());
        }
        return String.valueOf(totalPrice);

    }

    //Converts each item's double into strings
    public static String priceConversion(double price) {
        String totalPriceString = String.valueOf(price);
        if (totalPriceString.charAt(String.valueOf(price).length() - 2) == '.') {
            return "$ " + totalPriceString + "0";
        } else {
            return totalPriceString;
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        i.putExtra("BackPress", true);
        setResult(RESULT_OK, i);
        finish();
    }


    //removes all the QTY=0 elements
    public List<Product> removeZeroQtyList(List<Product> productList) {
        List<Product> checkoutList = new ArrayList<>();
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getQty() != 0) {
                checkoutList.add(productList.get(i));
            }
        }
        return checkoutList;
    }
}
