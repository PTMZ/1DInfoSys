package com.example.kensi.infosys1d;

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
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context mCtx;
    private List<Product> productList;
    private final MyClickListener listener;

    public ProductAdapter(Context mCtx, List<Product> productList, MyClickListener listener) {
        this.mCtx = mCtx;
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_layout_checkout, null);
        ProductViewHolder holder = new ProductViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.textViewTitle.setText(product.getTitle());
        holder.textViewDesc.setText(product.getShortdesc());
        holder.textViewQty.setText("x "+String.valueOf(product.getQty()));
        holder.textViewPrice.setText(String.valueOf(product.getPrice()));
        holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(product.getImage()));
    }

    @Override
    public int getItemCount() {
        return productList.size();
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
                Log.d("ProductAdapter", "Remove item... " + String.valueOf(getAdapterPosition()));
                actionType = "REMOVE";
            }
            else if(v.getId() == updateButton.getId()){
                Log.d("ProductAdapter", "Update item... " + String.valueOf(getAdapterPosition()));
                actionType = "UPDATE";
            }
            listenerRef.get().onPositionClicked(getAdapterPosition(), actionType);
        }
    }

}
