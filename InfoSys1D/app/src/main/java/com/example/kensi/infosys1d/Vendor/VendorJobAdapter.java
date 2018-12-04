package com.example.kensi.infosys1d.Vendor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.kensi.infosys1d.MyClickListener;
import com.example.kensi.infosys1d.R;

import java.lang.ref.WeakReference;
import java.util.List;

public class VendorJobAdapter extends RecyclerView.Adapter<VendorJobAdapter.ProductViewHolder> {

    private Context mCtx;
    private List<Job> jobsList;
    private final MyClickListener listener;

    public VendorJobAdapter(Context mCtx, List<Job> jobsList, MyClickListener listener) {
        this.mCtx = mCtx;
        this.jobsList = jobsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.vendor_layout_jobs, null);
        ProductViewHolder holder = new ProductViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Job job = jobsList.get(position);
        holder.textViewItemName.setText(job.getItemName());
        holder.textViewTaskNo.setText(job.getTaskId());
        holder.textViewTableNo.setText(job.getTableId());
        holder.textViewQty.setText("x "+String.valueOf(job.getQty()));
        //holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(checkoutProduct.getImage()));
    }

    @Override
    public int getItemCount() {
        return jobsList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textViewItemName, textViewTaskNo, textViewTableNo, textViewQty;
        ImageButton removeButton;
        private WeakReference<MyClickListener> listenerRef;

        public ProductViewHolder(@NonNull View itemView) {

            super(itemView);
            listenerRef = new WeakReference<>(listener);


            removeButton = itemView.findViewById(R.id.removeButton);
            textViewQty = itemView.findViewById(R.id.textViewQty);
            textViewItemName = itemView.findViewById(R.id.textViewItemName);
            textViewTaskNo = itemView.findViewById(R.id.textViewTaskNo);
            textViewTableNo = itemView.findViewById(R.id.textViewTableNo);

            removeButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            String actionType = "";
            if (v.getId() == removeButton.getId()){
                Log.d("Job Adapter", "Remove item... " + String.valueOf(getAdapterPosition()));
                actionType = "REMOVE";
            }
            listenerRef.get().onPositionClicked(getAdapterPosition(), actionType);
        }
    }

}
