package com.example.kensi.infosys1d.Menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.widget.Button;
import android.widget.Toast;

import com.example.kensi.infosys1d.Checkout.CheckoutMain;
import com.example.kensi.infosys1d.Login.LoginMain;
import com.example.kensi.infosys1d.Login.LoginPostRequest;
import com.example.kensi.infosys1d.Product;
import com.example.kensi.infosys1d.R;
import com.example.kensi.infosys1d.VolleyCallback;

public class MenuMain extends AppCompatActivity {

    //a menu_layout_products to store all the products
    public static List<Product> productList;

    //the recyclerview
    RecyclerView recyclerView;

    String storeID = "cffde47dcc0f3f7a92ae96e1650d5b306382ce6e97bd14373b3aa96ffe54a986219e5b0e0632d7bb899c8a5d5ccea092beee41e2798c9dddfa03e11b71083080";

    public static List<Product> getProductList() {
        return productList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        //getting the recyclerview from xml
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        //initializing the productlist
        productList = new ArrayList<>();

        MenuRequest.request_call_me(MenuMain.this, storeID, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                //Updates checkoutProductList with full details from items in checkMap
                productList = MenuRequest.request_iterate(result);
                //Updates Recycleview
                //creating recyclerview adapter
                MenuProductAdapter adapter = new MenuProductAdapter(MenuMain.this, productList);


                //setting adapter to recyclerview
                recyclerView.setAdapter(adapter);
            }
        });

    }

    //adds Menu to top bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_logout:
                LoginPostRequest.logout(MenuMain.this, new VolleyCallback(){
                    @Override
                    public void onSuccessResponse(String result) {
                        LoginMain.removeSessionCookie();
                        finish();
                    }
                });
            case R.id.action_cart:
//                Toast.makeText(MenuMain.this, "Place Order & Pay", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MenuMain.this, CheckoutMain.class);
                startActivity(intent);

        }
        return true;
    }


    public void showToast(View view){
    }
}