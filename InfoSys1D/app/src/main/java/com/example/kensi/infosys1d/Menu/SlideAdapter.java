package com.example.kensi.infosys1d.Menu;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kensi.infosys1d.Product;
import com.example.kensi.infosys1d.R;
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

        String downloadKey = MenuMain.getProductList().get(position).getImageURL();
        if(downloadKey.length() >0) {
            RequestUtils.downloadFile(context, downloadKey, imgslide);
        }

        txttitle.setText(MenuMain.getProductList().get(position).getTitle());
        description.setText(MenuMain.getProductList().get(position).getShortdesc());
        itemprice.setText(MenuMain.getProductList().get(position).getPrice());
        Log.d("MENU_POS", "Position: " + String.valueOf(position));

//        imgslide.setImageResource(lst_images[position]);
//        txttitle.setText(lst_title[position]);
//        description.setText(lst_description[position]);
//        itemprice.setText(lst_price[position]);
        container.addView(view);

        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}
