package com.quanutrition.app.payments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.quanutrition.app.R;

import java.util.ArrayList;

public class PaymentHistoryAdapter extends RecyclerView.Adapter<com.quanutrition.app.payments.PaymentHistoryAdapter.MyViewHolder> {

    private ArrayList<PaymentHistoryModel> paymentHistoryModelArrayList;
    private Context mCtx;
    private AlertDialog alertDialog1;
    private com.quanutrition.app.payments.PaymentHistoryAdapter.OnItemClicked onItemClicked;

    public interface OnItemClicked{
        void onExtension(View view, int position);
        void onRenewal(View v, int position);
    }

    public PaymentHistoryAdapter(ArrayList<PaymentHistoryModel> paymentHistoryModelArrayList, Context mCtx, com.quanutrition.app.payments.PaymentHistoryAdapter.OnItemClicked onItemClicked) {
        this.paymentHistoryModelArrayList = paymentHistoryModelArrayList;
        this.mCtx = mCtx;
        this.onItemClicked = onItemClicked;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView packageName_textView,packageDuration_textView,packageAmount_textView,packageDiscount_textView,purchaseDate_textView;
        TextView extension_button,receipt;
        View divider;

        public MyViewHolder(View view) {
            super(view);
            packageName_textView = view.findViewById(R.id.packageName_id);
            packageDuration_textView =  view.findViewById(R.id.packageDuration_id);
            packageAmount_textView = view.findViewById(R.id.packageAmount_id);
            packageDiscount_textView = view.findViewById(R.id.packageDiscount_id);
            purchaseDate_textView = view.findViewById(R.id.packageDate_id);
            extension_button = view.findViewById(R.id.extension_id);
            receipt = view.findViewById(R.id.receipt);
            divider = view.findViewById(R.id.divider);
        }
    }


    @Override
    public com.quanutrition.app.payments.PaymentHistoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.payment_history_item, parent, false);

        return new com.quanutrition.app.payments.PaymentHistoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final com.quanutrition.app.payments.PaymentHistoryAdapter.MyViewHolder holder, final int position) {

        final PaymentHistoryModel model = paymentHistoryModelArrayList.get(position);
        holder.packageName_textView.setText(model.getProgramName());
        holder.packageDuration_textView.setText(model.getProgramDuration());
        holder.packageAmount_textView.setText(model.getAmount());
        holder.purchaseDate_textView.setText(model.getPurchaseDate());
        holder.packageDiscount_textView.setText(model.getDiscount());
        if(model.isExtension())
        {
            holder.divider.setVisibility(View.VISIBLE);
            holder.extension_button.setText("Extend");
        }
        else if(model.isRenewal())
        {
            holder.divider.setVisibility(View.VISIBLE);
            holder.extension_button.setText("Renew");

        }
        else
        {
            holder.extension_button.setVisibility(View.GONE);
            holder.divider.setVisibility(View.GONE);

        }

        if(model.getReceipts().size()<1){
            holder.receipt.setVisibility(View.GONE);
        }else{
            holder.receipt.setVisibility(View.VISIBLE);
        }

        holder.receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mCtx);
                LayoutInflater linf = LayoutInflater.from(mCtx);
                final View inflator = linf.inflate(R.layout.selection_dialog, null);
                alertDialog.setView(inflator);
                alertDialog.setCancelable(true);
                RecyclerView recyclerView = inflator.findViewById(R.id.selection_re);

                PaymentReceiptAdapter adapter = new PaymentReceiptAdapter(model.getReceipts(),mCtx);

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mCtx);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                alertDialog1 = null;
                alertDialog1 = alertDialog.show();
                alertDialog1.getWindow().setBackgroundDrawable(mCtx.getResources().getDrawable(R.drawable.dialog_background_drawable));
            }
        });

        holder.extension_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.extension_button.getText().toString().equalsIgnoreCase("Extend"))
                    onItemClicked.onExtension(view,holder.getAdapterPosition());
                else
                    onItemClicked.onRenewal(view,holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return paymentHistoryModelArrayList.size();
    }



}