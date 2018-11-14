package com.example.kensi.infosys1d;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Checkout extends AppCompatActivity {

    TextView textViewTotalPrice;
    RecyclerView recyclerView;
    ProductAdapter adapter;
    List<Product> productList;
    private static final String TAG = "Checkout";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: checkout" );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        productList = new ArrayList<>();
        textViewTotalPrice = findViewById(R.id.textViewTotalPrice);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productList.add(
                new Product(
                        1,
                        "Chicken Burger",
                        "No chicken",
                        13,
                        R.drawable.burger, 5));

        productList.add(
                new Product(
                        2,
                        "Mushroom",
                        "14 inch, Gray, 1.659 kg",
                        13,
                        R.drawable.mushroomsoup, 5));

        productList.add(
                new Product(
                        3,
                        "Microsoft Surface Pro 4 Core m3 6th Gen - (4 GB/128 GB SSD/Windows 10)",
                        "13.3 inch, Silver, 1.35 kg",
                        9,
                        R.drawable.marinabayotter,5));
        productList.add(
                new Product(
                        3,
                        "Microsoft Surface Pro 4 Core m3 6th Gen - (4 GB/128 GB SSD/Windows 10)",
                        "13.3 inch, Silver, 1.35 kg",
                        9,
                        R.drawable.marinabayotter,5));
        productList.add(
                new Product(
                        3,
                        "Microsoft Surface Pro 4 Core m3 6th Gen - (4 GB/128 GB SSD/Windows 10)",
                        "13.3 inch, Silver, 1.35 kg",
                        9,
                        R.drawable.marinabayotter, 5));
        adapter = new ProductAdapter(this, productList);
        recyclerView.setAdapter(adapter);
        textViewTotalPrice.setText(getPrice(productList));
//        Toolbar myToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(myToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private String getPrice(List<Product> list){
        double totalPrice = 0;
        for (Product p : list){
            totalPrice += p.getPrice()*p.getQty();
        }
        String totalPriceString = String.valueOf(totalPrice);
        if (totalPriceString.charAt(String.valueOf(totalPrice).length()-2)=='.'){
            return "$ "+totalPriceString+"0";
        } else{
            return totalPriceString;
        }

    }
}
