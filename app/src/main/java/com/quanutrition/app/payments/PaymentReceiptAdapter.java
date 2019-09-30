package com.quanutrition.app.payments;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.quanutrition.app.R;


import java.util.ArrayList;


public class PaymentReceiptAdapter extends RecyclerView.Adapter<PaymentReceiptAdapter.MyViewHolder> {

    private ArrayList<PaymentReceiptModel> list;
    private Context mCtx;


    public PaymentReceiptAdapter(ArrayList<PaymentReceiptModel> list, Context mCtx) {
        this.list = list;
        this.mCtx = mCtx;
    }




    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView date,mode,amount;
        public MyViewHolder(View view) {
            super(view);
            date = view.findViewById(R.id.date);
            mode = view.findViewById(R.id.mode);
            amount = view.findViewById(R.id.amount);
        }
    }


    @Override
    public PaymentReceiptAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.payement_receipt_list_item, parent, false);

        return new PaymentReceiptAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PaymentReceiptAdapter.MyViewHolder holder, final int position) {

        final PaymentReceiptModel model = list.get(position);
        holder.date.setText("Date : "+model.getDate());
        holder.mode.setText("Mode : "+model.getMode());
        holder.amount.setText("Amount : "+model.getAmount());
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }



}