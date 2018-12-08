package com.example.kensi.infosys1d.Menu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kensi.infosys1d.Login.LoginMain;
import com.example.kensi.infosys1d.Product;
import com.example.kensi.infosys1d.R;
import com.example.kensi.infosys1d.Registration.RegistrationMain;
import com.example.kensi.infosys1d.RequestUtils;

import java.util.List;

public class SlideAdapter extends PagerAdapter {
    Context context;
    LayoutInflater inflater;


    public SlideAdapter(Context context) {
        this.context = context;
    }


    @Override
    public int getCount() {
        return MenuMain.getProductList().size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view == (LinearLayout) o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.menu_slide, container, false);
        LinearLayout layoutslide = view.findViewById(R.id.slidelinearlayout);
        ImageView imgslide = view.findViewById(R.id.slideimg);
        TextView txttitle = view.findViewById(R.id.txttitle);
        TextView description = view.findViewById(R.id.txtdescription);
        description.setMovementMethod(new ScrollingMovementMethod());
        TextView itemprice = view.findViewById(R.id.itemprice);
        final TextView itemqty = view.findViewById(R.id.itemqty);
        Button decreasebutton = view.findViewById(R.id.decreasebutton);
        Button increasebutton = view.findViewById(R.id.increasebutton);


        String downloadKey = MenuMain.getProductList().get(position).getImageURL();
        if (downloadKey.length() > 0) {
            RequestUtils.downloadFile(context, downloadKey, imgslide);
        }

        itemqty.setText(String.valueOf(MenuMain.getProductList().get(position).getQty()));
        txttitle.setText(MenuMain.getProductList().get(position).getTitle());
        description.setText(MenuMain.getProductList().get(position).getShortdesc());
        itemprice.setText(MenuMain.priceConversion(Double.valueOf(MenuMain.getProductList().get(position).getPrice())));
        Log.d("MENU_POS", "Position: " + String.valueOf(position));

        //checks if qty is below 100, then adds 1 if button is pressed
        increasebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currNumItems = MenuMain.getProductList().get(position).getQty();
                if (currNumItems < 100) {
                    MenuMain.getProductList().get(position).setQty(currNumItems + 1);
                    itemqty.setText(String.valueOf(currNumItems + 1));
                }
            }
        });

        //checks if qty is above 0, then subtracts 1 if button is pressed
        decreasebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currNumItems = MenuMain.getProductList().get(position).getQty();
                if (currNumItems > 0) {
                    MenuMain.getProductList().get(position).setQty(currNumItems - 1);
                    itemqty.setText(String.valueOf(currNumItems - 1));
                }
            }
        });

        container.addView(view);

        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}