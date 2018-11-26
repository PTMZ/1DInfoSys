package com.example.kensi.infosys1d.Vendor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.kensi.infosys1d.Login.LoginMain;
import com.example.kensi.infosys1d.Login.LoginPostRequest;
import com.example.kensi.infosys1d.MyClickListener;
import com.example.kensi.infosys1d.Product;
import com.example.kensi.infosys1d.R;
import com.example.kensi.infosys1d.VolleyCallback;

import java.util.ArrayList;
import java.util.List;

public class VendorMain extends AppCompatActivity {

    //TextView textViewFileName;
    RecyclerView recyclerView;
    Button buttonUploadImage;
    Button buttonAddProduct;
    VendorProductAdapter adapter;
    List<Product> checkoutProductList;
    private static final String TAG = "VendorMain";
    private static final int ADD_FORM_REQ_CODE = 1;
    private static final int UPDATE_FORM_REQ_CODE = 2;
    private static final int UPLOAD_FORM_REQ_CODE = 3;

    String storeID = "cffde47dcc0f3f7a92ae96e1650d5b306382ce6e97bd14373b3aa96ffe54a986219e5b0e0632d7bb899c8a5d5ccea092beee41e2798c9dddfa03e11b71083080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: vendor" );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor);

        //creation of a list for each individual item
        checkoutProductList = new ArrayList<>();
        buttonAddProduct = findViewById(R.id.buttonAddProduct);
        buttonUploadImage = findViewById(R.id.buttonUploadImage);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        //Testing StoreID, to be passed on from previous activity

        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(VendorMain.this, VendorAddForm.class);
                startActivityForResult(i, ADD_FORM_REQ_CODE);
            }
        });

        buttonUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(VendorMain.this, VendorUploadImage.class);
                startActivityForResult(i, UPLOAD_FORM_REQ_CODE);
            }
        });

        //Volley to server
        refreshRecycler();

    }

    //adds Menu to top bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_refresh){
            VendorRequests.request_call_me(VendorMain.this, storeID, new VolleyCallback() {
                @Override
                public void onSuccessResponse(String result) {
                    refreshRecycler();
                }
            });
        }
        if(item.getItemId() == R.id.action_logout){
            LoginPostRequest.logout(VendorMain.this, new VolleyCallback(){
                @Override
                public void onSuccessResponse(String result) {
                    LoginMain.removeSessionCookie();
                    finish();
                }
            });
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ADD_FORM_REQ_CODE){
            refreshRecycler();
        }
        else if(requestCode == UPDATE_FORM_REQ_CODE){
            refreshRecycler();
        }
        else if(requestCode == UPLOAD_FORM_REQ_CODE){
            refreshRecycler();
        }
    }

    public void refreshRecycler(){
        VendorRequests.request_call_me(VendorMain.this, storeID, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                //Updates checkoutProductList with full details from items in checkMap
                //Toast.makeText(getApplicationContext(),  "Refresh", Toast.LENGTH_LONG).show();
                checkoutProductList = VendorRequests.request_iterate(result);
                //Updates Recycleview
                adapter = new VendorProductAdapter(VendorMain.this, checkoutProductList, new MyClickListener() {
                    @Override
                    public void onPositionClicked(int position, String type) {
                        String itemName = checkoutProductList.get(position).getTitle();

                        if(type == "REMOVE"){
                            removeItem(itemName);
                        }
                        else if(type == "UPDATE"){
                            Intent i = new Intent(VendorMain.this, VendorUpdateForm.class);
                            i.putExtra("itemName", itemName);
                            i.putExtra("category", checkoutProductList.get(position).getCategory());
                            i.putExtra("price", checkoutProductList.get(position).getPrice());
                            i.putExtra("description", checkoutProductList.get(position).getShortdesc());
                            startActivityForResult(i,UPDATE_FORM_REQ_CODE);
                        }

                    }
                });
                recyclerView.setAdapter(adapter);
            }
        });
    }

    public void removeItem(final String itemName){
        VendorRequests.removeProduct(VendorMain.this, itemName, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                Toast.makeText(getApplicationContext(), "Removed: " + itemName, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "RemoveItem: " + itemName);
                refreshRecycler();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        i.putExtra("BackPress",true);
        setResult(RESULT_OK, i);
        finish();
    }

}


