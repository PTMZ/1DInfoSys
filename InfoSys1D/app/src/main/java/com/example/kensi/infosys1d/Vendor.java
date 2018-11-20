package com.example.kensi.infosys1d;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Vendor extends AppCompatActivity {

    //TextView textViewFileName;
    RecyclerView recyclerView;
    Button buttonUploadImage;
    Button buttonAddProduct;
    ProductAdapter adapter;
    List<Product> productList;
    private static final String TAG = "Vendor";
    String storeID = "cffde47dcc0f3f7a92ae96e1650d5b306382ce6e97bd14373b3aa96ffe54a986219e5b0e0632d7bb899c8a5d5ccea092beee41e2798c9dddfa03e11b71083080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: vendor" );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor);

        //creation of a list for each individual item
        productList = new ArrayList<>();
        buttonAddProduct = findViewById(R.id.buttonAddProduct);
        buttonUploadImage = findViewById(R.id.buttonUploadImage);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        //Testing StoreID, to be passed on from previous activity

        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Vendor.this, VendorAddForm.class);
                startActivity(i);
            }
        });

        buttonUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            VendorRequests.request_call_me(Vendor.this, storeID, new VolleyCallback() {
                @Override
                public void onSuccessResponse(String result) {
                    refreshRecycler();
                }
            });
        }
        return true;
    }

    public void refreshRecycler(){
        VendorRequests.request_call_me(Vendor.this, storeID, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                //Updates productList with full details from items in checkMap
                //Toast.makeText(getApplicationContext(),  "Refresh", Toast.LENGTH_LONG).show();
                productList = VendorRequests.request_iterate(result);
                //Updates Recycleview
                adapter = new ProductAdapter(Vendor.this, productList, new MyClickListener() {
                    @Override
                    public void onPositionClicked(int position, String type) {
                        String itemName = productList.get(position).getTitle();

                        if(type == "REMOVE"){
                            removeItem(itemName);
                        }
                        else if(type == "UPDATE"){
                            Intent i = new Intent(Vendor.this, VendorUpdateForm.class);
                            i.putExtra("itemName", itemName);
                            startActivity(i);
                        }

                    }
                });
                recyclerView.setAdapter(adapter);
            }
        });
    }

    public void removeItem(final String itemName){
        VendorRequests.removeProduct(Vendor.this, itemName, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                Toast.makeText(getApplicationContext(), "Removed: " + itemName, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "RemoveItem: " + itemName);
                refreshRecycler();
            }
        });
    }

}


