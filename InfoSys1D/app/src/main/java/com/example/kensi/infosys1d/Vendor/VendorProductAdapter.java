package com.example.kensi.infosys1d.Vendor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kensi.infosys1d.MyClickListener;
import com.example.kensi.infosys1d.Product;
import com.example.kensi.infosys1d.R;
import com.example.kensi.infosys1d.Registration.RequestUtils;

import java.lang.ref.WeakReference;
import java.util.List;

public class VendorProductAdapter extends RecyclerView.Adapter<VendorProductAdapter.ProductViewHolder> {

    private Context mCtx;
    private List<Product> checkoutProductList;
    private final MyClickListener listener;

    public VendorProductAdapter(Context mCtx, List<Product> checkoutProductList, MyClickListener listener) {
        this.mCtx = mCtx;
        this.checkoutProductList = checkoutProductList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.vendor_layout_products, null);
        ProductViewHolder holder = new ProductViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product checkoutProduct = checkoutProductList.get(position);
        holder.textViewTitle.setText(checkoutProduct.getTitle());
        holder.textViewDesc.setText(checkoutProduct.getShortdesc());
        holder.textViewQty.setText("x "+String.valueOf(checkoutProduct.getQty()));
        holder.textViewPrice.setText(String.valueOf(checkoutProduct.getPrice()));
        //holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(checkoutProduct.getImage()));
        Log.d("IMAGE_URL",checkoutProduct.getImageURL());
        String downloadKey = checkoutProduct.getImageURL();
        if(downloadKey.length()>0){
            RequestUtils.downloadFile(mCtx, downloadKey, holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return checkoutProductList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imageView;
        TextView textViewTitle, textViewDesc, textViewPrice, textViewQty;
        ImageButton removeButton;
        ImageButton updateButton;
        private WeakReference<MyClickListener> listenerRef;

        public ProductViewHolder(@NonNull View itemView) {

            super(itemView);
            listenerRef = new WeakReference<>(listener);

            imageView = itemView.findViewById(R.id.imageView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDesc = itemView.findViewById(R.id.textViewShortDesc);
            removeButton = itemView.findViewById(R.id.removeButton);
            updateButton = itemView.findViewById(R.id.updateButton);
            textViewPrice = itemView.findViewById(R.id.textViewTotalName);
            textViewQty = itemView.findViewById(R.id.textViewQty);

            removeButton.setOnClickListener(this);
            updateButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            String actionType = "";
            if (v.getId() == removeButton.getId()){
                Log.d("CheckoutProductAdapter", "Remove item... " + String.valueOf(getAdapterPosition()));
                actionType = "REMOVE";
            }
            else if(v.getId() == updateButton.getId()){
                Log.d("CheckoutProductAdapter", "Update item... " + String.valueOf(getAdapterPosition()));
                actionType = "UPDATE";
            }
            listenerRef.get().onPositionClicked(getAdapterPosition(), actionType);
        }
    }

}
