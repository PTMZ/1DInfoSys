package com.example.kensi.infosys1d;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SlideAdapter extends PagerAdapter {
    Context context;
    LayoutInflater inflater;


    //list of images
    public int[] lst_images = {
            R.drawable.beef_lagsana,
            R.drawable.beef_tacos,
            R.drawable.burgerandfries,
            R.drawable.chicken_kebab,
            R.drawable.creamy_pumpkin_pasta,
            R.drawable.pepperoni_pizza,
            R.drawable.relish_hotdog
    };

    //list of title
    public String[] lst_title = {
            "Beef Lasagna",
            "Beef Tacos",
            "Burger and Fries",
            "Chicken Kebab",
            "Creamy Pumpkin Pasta",
            "Pepperoni Pizza",
            "Relish Hotdog"
    };

    //list of descriptions
    public String[] lst_description = {
            "desc 1",
            "desc 2",
            "desc 3",
            "desc 4",
            "desc 5",
            "desc 6",
            "desc 7"
    };

    public String[] lst_price = {
            "$7.9",
            "$8.5",
            "$15.0",
            "$13.0",
            "$13.0",
            "$15.0",
            "$7.9"
    };


    public SlideAdapter(Context context) {
        this.context = context;
    }




    @Override
    public int getCount() {
        return lst_title.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view==(LinearLayout)o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.menu_slide,container,false);
        LinearLayout layoutslide = (LinearLayout) view.findViewById(R.id.slidelinearlayout);
        ImageView imgslide = (ImageView) view.findViewById(R.id.slideimg);
        TextView txttitle = (TextView) view.findViewById(R.id.txttitle);
        TextView description = (TextView) view.findViewById(R.id.txtdescription);
        TextView itemprice = (TextView)view.findViewById(R.id.itemprice);

        //layoutslide.setBackgroundColor(Color.WHITE);
        imgslide.setImageResource(lst_images[position]);
        txttitle.setText(lst_title[position]);
        description.setText(lst_description[position]);
        itemprice.setText(lst_price[position]);
        container.addView(view);

        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}
