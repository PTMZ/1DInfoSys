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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kensi.infosys1d.Login.LoginMain;
import com.example.kensi.infosys1d.Login.LoginPostRequest;
import com.example.kensi.infosys1d.Menu.MenuMain;
import com.example.kensi.infosys1d.MyClickListener;
import com.example.kensi.infosys1d.PaymentConfirmationMain;
import com.example.kensi.infosys1d.Product;
import com.example.kensi.infosys1d.R;
import com.example.kensi.infosys1d.Registration.RegistrationMain;
import com.example.kensi.infosys1d.VolleyCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckoutMain extends AppCompatActivity {

    Button buttonPlaceOrder;
    TextView textViewTotalPrice;
    RecyclerView recyclerView;
    CheckoutProductAdapter adapter;
    List<Product> checkoutList;
    private static final String TAG = "CheckoutMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: checkout");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_main);
        //creation of a list for each individual item
        textViewTotalPrice = findViewById(R.id.textViewTotalPrice);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        buttonPlaceOrder = findViewById(R.id.buttonPlaceOrder);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
        final double totalPriceDouble = getPrice(checkoutList);
        final String totalPriceString = CheckoutMain.priceConversion(totalPriceDouble);
        textViewTotalPrice.setText(totalPriceString);

        //Clicking the Place Order button
        buttonPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CheckoutMain.this, PaymentConfirmationMain.class);
                i.putExtra("totalPriceDouble", totalPriceDouble);
                i.putExtra("totalPriceString", totalPriceString);
                startActivity(i);
            }
        });
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
                    Intent i = new Intent(CheckoutMain.this, LoginMain.class);
                    startActivity(i);
                }
            });
            return true;
        }
        return false;
    }

    //Calculates total value from individual item Strings
    private Double getPrice(List<Product> list) {
        double totalPrice = 0;
        for (Product p : list) {
            totalPrice += Double.valueOf(p.getPrice()) * p.getQty();
        }
        return totalPrice;

    }

    //Converts each item's double into strings
    public static String priceConversion(double price) {
        String totalPriceString = String.valueOf(price);
        if (totalPriceString.charAt(String.valueOf(price).length() - 2) == '.') {
            return "$" + totalPriceString + "0";
        } else if (totalPriceString.charAt(String.valueOf(price).length() - 3) != '.') {
            int dotNum = totalPriceString.indexOf('.');
            if (dotNum == -1) {
                return "$" + totalPriceString + ".00";
            } else {
                return "$" + totalPriceString.substring(0, dotNum+3);
            }
        } else {
            return "$" + totalPriceString;
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
