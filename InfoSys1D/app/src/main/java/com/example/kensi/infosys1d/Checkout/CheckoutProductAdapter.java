package com.example.kensi.infosys1d.Checkout;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kensi.infosys1d.Menu.MenuMain;
import com.example.kensi.infosys1d.MyClickListener;
import com.example.kensi.infosys1d.Product;
import com.example.kensi.infosys1d.R;
import com.example.kensi.infosys1d.RequestUtils;

import java.lang.ref.WeakReference;
import java.util.List;

public class CheckoutProductAdapter extends RecyclerView.Adapter<CheckoutProductAdapter.ProductViewHolder> {

    private Context mCtx;
    private List<Product> checkoutProductList;
    private final MyClickListenerCheckout listener;

    public CheckoutProductAdapter(Context mCtx, List<Product> checkoutProductList, MyClickListenerCheckout listener) {
        this.mCtx = mCtx;
        this.checkoutProductList = checkoutProductList;
        this.listener = listener;
    }



    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.checkout_layout_products, null);
        ProductViewHolder holder = new ProductViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        final Product checkoutProduct = checkoutProductList.get(position);
        holder.textViewTitle.setText(checkoutProduct.getTitle());
        holder.textViewDesc.setText(checkoutProduct.getShortdesc());
        holder.textViewQty.setText("x " + String.valueOf(checkoutProduct.getQty()));
        //Changes the price amount to a $X.XX representation
        holder.textViewPrice.setText(MenuMain.priceConversion(Double.valueOf(checkoutProduct.getPrice())));
        Log.d("IMAGE_URL", checkoutProduct.getImageURL());
        String downloadKey = checkoutProduct.getImageURL();
        if (downloadKey.length() > 0) {
            RequestUtils.downloadFile(mCtx, downloadKey, holder.imageView);
        }
    }



    @Override
    public int getItemCount() {
        return checkoutProductList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView textViewTitle, textViewDesc, textViewPrice, textViewQty;
        ImageButton removeButton;
        private WeakReference<MyClickListenerCheckout> listenerRef;

        public ProductViewHolder(@NonNull final View itemView) {

            super(itemView);
            listenerRef = new WeakReference<>(listener);

            imageView = itemView.findViewById(R.id.imageView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDesc = itemView.findViewById(R.id.textViewShortDesc);
            removeButton = itemView.findViewById(R.id.removeButton);
            textViewPrice = itemView.findViewById(R.id.textViewTotalName);
            textViewQty = itemView.findViewById(R.id.textViewQty);

            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            try{Product checkoutProduct = checkoutProductList.get(position);
                                listener.onDeleteClick(position);

                                int idxRemoved = MenuMain.productList.indexOf(checkoutProduct);
                                MenuMain.getProductList().get(idxRemoved).setQty(0);

                            } catch (IndexOutOfBoundsException ex) {
                                Toast.makeText(removeButton.getContext(),"Item Failed to be removed",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(removeButton.getContext(), MenuMain.class);
                                removeButton.getContext().startActivity(intent);
                            }

                        }
                    }
                }
            });



        }


        @Override
        public void onClick(View v) {
            String actionType = "";
            listenerRef.get().onPositionClicked(getAdapterPosition(), actionType);
        }
    }


}
