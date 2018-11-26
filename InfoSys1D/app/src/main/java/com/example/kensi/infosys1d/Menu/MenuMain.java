package com.example.kensi.infosys1d.Menu;

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

import com.example.kensi.infosys1d.Login.LoginMain;
import com.example.kensi.infosys1d.Login.LoginPostRequest;
import com.example.kensi.infosys1d.Product;
import com.example.kensi.infosys1d.R;
import com.example.kensi.infosys1d.VolleyCallback;

public class MenuMain extends AppCompatActivity {

    //a menu_layout_products to store all the products
    List<Product> productList;

    //the recyclerview
    RecyclerView recyclerView;




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


        //adding some items to our menu_layout_products
        productList.add(
                new Product(
                        1,
                        "Beef Lagsana",
                        7.90,
                        R.drawable.beef_lagsana));

        productList.add(
                new Product(
                        1,
                        "Beef Tacos",

                        8.50,
                        R.drawable.beef_tacos));

        productList.add(
                new Product(
                        1,
                        "Burger and Fries",

                        15,
                        R.drawable.burgerandfries));

        productList.add(
                new Product(
                        1,
                        "Chicken Kebab",

                        13,
                        R.drawable.chicken_kebab));

        productList.add(
                new Product(
                        1,
                        "Creamy Pumpkin Pasta",

                        13,
                        R.drawable.creamy_pumpkin_pasta));

        productList.add(
                new Product(
                        1,
                        "Pepperoni Pizza",

                        15,
                        R.drawable.pepperoni_pizza));
        productList.add(
                new Product(
                        1,
                        "Relish Hotog",

                        7.90,
                        R.drawable.relish_hotdog));




        //creating recyclerview adapter
        MenuProductAdapter adapter = new MenuProductAdapter(this, productList);

        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter);
    }

    //adds Menu to top bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_logout){
            LoginPostRequest.logout(MenuMain.this, new VolleyCallback(){
                @Override
                public void onSuccessResponse(String result) {
                    LoginMain.removeSessionCookie();
                    finish();
                }
            });
        }
        return true;
    }

    public void showToast(View view){
    }
}