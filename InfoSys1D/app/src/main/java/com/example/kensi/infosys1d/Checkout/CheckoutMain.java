package com.example.kensi.infosys1d.Checkout;

import android.app.Activity;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.kensi.infosys1d.Checkout.Payment.PaymentConfirm;
import com.example.kensi.infosys1d.Checkout.Payment.PaymentLogin;
import com.example.kensi.infosys1d.Checkout.Payment.PaymentPostRequest;
import com.example.kensi.infosys1d.Login.LoginMain;
import com.example.kensi.infosys1d.Login.LoginPostRequest;
import com.example.kensi.infosys1d.Menu.MenuMain;
import com.example.kensi.infosys1d.OCBI_API.PayAnyone;
import com.example.kensi.infosys1d.OCBI_API.Utils;
import com.example.kensi.infosys1d.PaymentConfirmationMain;
import com.example.kensi.infosys1d.Product;
import com.example.kensi.infosys1d.QRreader.QRreaderMain;
import com.example.kensi.infosys1d.R;

import com.example.kensi.infosys1d.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CheckoutMain extends AppCompatActivity {

    Button buttonPlaceOrder;
    TextView textViewTotalPrice;
    RecyclerView recyclerView;
    EditText editTextTable;
    CheckoutProductAdapter adapter;

    List<Product> checkoutList;
    public JSONObject checkoutJSON;

    private static String session_token;
    private double totalPriceDouble = 0;
    private String totalPriceString = "";
    final int PAYMENT_REQUEST = 1;
    public static Activity CheckoutMain;

    private static final String TAG = "CheckoutMain";

    //For removing item from the checkout page
    public void removeItem(int position) {
        checkoutList.remove(position);
        adapter.notifyDataSetChanged();
        updateTotalPrice();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CheckoutMain = this;
        Log.d(TAG, "onCreate: checkout");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_main);
        //creation of a list for each individual item
        textViewTotalPrice = findViewById(R.id.textViewTotalPrice);
        editTextTable = findViewById(R.id.editTextTable);
        recyclerView = findViewById(R.id.recyclerView);
        buttonPlaceOrder = findViewById(R.id.buttonPlaceOrder);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Creates a new list from MenuMain's productList, removing all the QTY=0 elements
        checkoutList = removeZeroQtyList(MenuMain.productList);
        try {
            checkoutJSON = getJSONCheckout(MenuMain.productList);
        } catch (JSONException e) {
            Log.e(TAG, "JSON ERROR");
            e.printStackTrace();
        }
        adapter = new CheckoutProductAdapter(CheckoutMain.this, checkoutList, new MyClickListenerCheckout() {
            @Override
            public void onPositionClicked(int position, String type) {
                // do something
            }

            public void onDeleteClick(int position) {
                removeItem(position);
            }
        });
        recyclerView.setAdapter(adapter);

        //Set total value
        updateTotalPrice();


        // Clicking the Place Order button
        buttonPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalPriceDouble == 0) {
                    Toast.makeText(CheckoutMain.this,"Please add something to cart",Toast.LENGTH_LONG).show();
                } else {
                    //Intent i = new Intent(CheckoutMain.this, PaymentConfirmationMain.class);
                    if (session_token == null) { // Go to OCBC Login if there is no session token
                        if (Utils.isNetworkAvailable(CheckoutMain.this)) {
                            Intent i = new Intent(CheckoutMain.this, PaymentLogin.class);
                            startActivityForResult(i, PAYMENT_REQUEST);
                        } else {
                            Toast.makeText(CheckoutMain.this, getText(R.string.network_error), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (Utils.isNetworkAvailable(CheckoutMain.this)) {
                            PaymentProcessing paymentProcessing = new PaymentProcessing();
                            paymentProcessing.execute(session_token);
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PAYMENT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                //TODO: Make app go live
                //session_token = data.getStringExtra(PaymentLogin.SESSION_TOKEN);
                session_token = "9a948a49407c380ed0d0a07d995e9f38";

                if (data.getIntExtra(PaymentLogin.JUST_LOGIN,0) == 1){
                    String tableNo;
                    if (editTextTable.getText().toString().equals("")) {
                        tableNo = "0";
                    } else {
                        tableNo = editTextTable.getText().toString();
                    }
                    // Post Order to Server
                    try {
                        PaymentPostRequest.postPayment(CheckoutMain.this,
                                getJSONOrder(checkoutList), QRreaderMain.getStoreID(), tableNo,
                                new VolleyCallback() {
                                    @Override
                                    // Bring User to Payment Confirmed Activity on Success Response
                                    public void onSuccessResponse(String result) { // on success, go to next screen
                                        Intent i = new Intent(CheckoutMain.this, PaymentConfirmationMain.class);
                                        i.putExtra("totalPriceDouble", totalPriceDouble);
                                        i.putExtra("totalPriceString", totalPriceString);
                                        startActivity(i);
                                    }
                                });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    //adds Menu to top bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_handicap, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            LoginPostRequest.logout(CheckoutMain.this, new VolleyCallback() {
                @Override
                public void onSuccessResponse(String result) {
                    LoginMain.removeSessionCookie();
                    QRreaderMain.QRreaderMain.finish();
                    MenuMain.MenuMain.finish();
                    finish();
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

    //removes all the QTY=0 elements
    public List<JSONObject> getJSONOrder(List<Product> productList) throws JSONException {

        List<JSONObject> orders = new ArrayList<>();
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getQty() != 0) {
                orders.add(productList.get(i).getJSON());
            }
        }
        return orders;
    }

    //todo what to do here lol
    public JSONObject getJSONCheckout(List<Product> productList) throws JSONException {
        JSONObject out = new JSONObject();

        List<JSONObject> orders = getJSONOrder(productList);
        String vendor_id = "Bob";
        Integer table_id = 5;

        out.put("orders", orders);
        out.put("vendor_id", vendor_id);
        out.put("table_id", table_id);

        return out;
    }

    class PaymentProcessing extends AsyncTask<String, String, JSONObject> // <Input, Progress, Output>
    {
        JSONObject response_json;

        @Override
        protected JSONObject doInBackground(String... token) {
            if (Utils.isNetworkAvailable(CheckoutMain.this)) {
                PayAnyone payAnyone = new PayAnyone(
                        getString(R.string.client_id),
                        getString(R.string.test_bank_account),
                        totalPriceDouble,
                        "Bob",
                        "87654321",
                        "999999");
                String response = payAnyone.getResponseBody(token[0]);
                try {
                    response_json = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                    response_json = new JSONObject();
                }
            } else {
                Toast.makeText(CheckoutMain.this, "Network Unavailable", Toast.LENGTH_SHORT).show();
            }
            return response_json;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(JSONObject output) {
            super.onPostExecute(output);
//            response.setText(output);
            String success_response = null;
            try {
                // Response from OCBC
                success_response = output.getJSONObject("results").getString("success");
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(CheckoutMain.this, "Error", Toast.LENGTH_SHORT).show();
            }
            if (success_response == "true") {

                // POST Order to server
                try {
                    //checks if the table number field is empty, if empty send a 0
                    String tableNo;
                    if (editTextTable.getText().toString().equals("")) {
                        tableNo = "0";
                    } else {
                        tableNo = editTextTable.getText().toString();
                    }
                    // Post Order to Server
                    PaymentPostRequest.postPayment(CheckoutMain.this,
                            getJSONOrder(checkoutList), QRreaderMain.getStoreID(), tableNo,
                            new VolleyCallback() {
                                @Override
                                // Bring User to Payment Confirmed Activity on Success Response
                                public void onSuccessResponse(String result) { // on success, go to next screen
                                    Intent i = new Intent(CheckoutMain.this, PaymentConfirmationMain.class);
                                    i.putExtra("totalPriceDouble", totalPriceDouble);
                                    i.putExtra("totalPriceString", totalPriceString);
                                    startActivity(i);
                                }
                            });
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }

    //called to update the total price
    private void updateTotalPrice() {
        totalPriceDouble = getPrice(checkoutList);
        totalPriceString = MenuMain.priceConversion(totalPriceDouble);
        textViewTotalPrice.setText(totalPriceString);
    }

}

