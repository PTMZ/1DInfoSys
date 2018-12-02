package com.example.kensi.infosys1d;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.graphics.drawable.Animatable2Compat;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kensi.infosys1d.Login.LoginMain;
import com.example.kensi.infosys1d.Menu.MenuMain;
import com.example.kensi.infosys1d.QRreader.QRreaderMain;

@TargetApi(21)
public class PaymentConfirmationMain extends AppCompatActivity {

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_confirmation_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button buttonQRScanner = findViewById(R.id.buttonQRScanner);
        Button buttonMenu = findViewById(R.id.buttonMenu);
        ImageView imageConfirm = findViewById(R.id.imageConfirm);
        TextView textPayment = findViewById(R.id.textPayment);
        final AnimatedVectorDrawable drawable = (AnimatedVectorDrawable) getDrawable(R.drawable.confirmanim);
        imageConfirm.setImageDrawable(drawable);

        //puts the animation on a loop
        drawable.registerAnimationCallback(new Animatable2.AnimationCallback() {
            @Override
            public void onAnimationEnd(Drawable a) {
                drawable.start();
            }
        });
        drawable.start();

        //Gets totalPrice data from checkout menu
        String totalPriceString;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                totalPriceString = "INSERT_PRICE_HERE";
            } else {
                totalPriceString = extras.getString("totalPriceString");
            }
        } else {
            totalPriceString = (String) savedInstanceState.getSerializable("totalPriceString");
        }
        //Prints total price paid
        textPayment.setText("Payment of " + totalPriceString + " has been sent.");

        //button to go back to QRreader Activity
        buttonQRScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PaymentConfirmationMain.this, QRreaderMain.class);
                startActivity(i);
            }
        });

        //button to go back to Menu Activity
        buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PaymentConfirmationMain.this, MenuMain.class);
                startActivity(i);
            }
        });
    }
}
