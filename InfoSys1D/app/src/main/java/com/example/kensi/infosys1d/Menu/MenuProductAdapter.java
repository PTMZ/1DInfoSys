package com.example.kensi.infosys1d.Menu;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.kensi.infosys1d.Product;
import com.example.kensi.infosys1d.R;
import com.example.kensi.infosys1d.Registration.RequestUtils;

import java.util.List;


public class MenuProductAdapter extends RecyclerView.Adapter<MenuProductAdapter.ProductViewHolder> {


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a menu_layout_products
    private List<Product> productList;

    //getting the context and product menu_layout_products with constructor
    public MenuProductAdapter(Context mCtx, List<Product> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.menu_layout_products, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        //getting the product of the specified position
        Product product = productList.get(position);

        //binding the data with the viewholder views
        holder.textViewTitle.setText(product.getTitle());
       // holder.textViewShortDesc.setText(product.getShortdesc());
        //holder.textViewRating.setText(String.valueOf(product.getRating()));
        holder.textViewPrice.setText(String.valueOf(product.getPrice()));

//        holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(product.getImage()));
        Log.d("IMAGE_URL",product.getImageURL());
        String downloadKey = product.getImageURL();
        if(downloadKey.length()>0){
            RequestUtils.downloadFile(mCtx, downloadKey, holder.imageView);
        }

    }


    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewShortDesc, textViewRating, textViewPrice;
        ImageView imageView;

        public ProductViewHolder(final View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            //textViewShortDesc = itemView.findViewById(R.id.textViewShortDesc);
            //textViewRating = itemView.findViewById(R.id.textViewRating);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            imageView = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(itemView.getContext(), MenuPop.class);


                    intent.putExtra("viewpager_position", getAdapterPosition());

                    itemView.getContext().startActivity(intent);
                }


            });
        }
    }
}
