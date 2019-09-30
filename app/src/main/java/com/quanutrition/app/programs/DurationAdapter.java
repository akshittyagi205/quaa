package com.quanutrition.app.programs;


import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.quanutrition.app.R;

import java.util.ArrayList;


public class DurationAdapter extends RecyclerView.Adapter<DurationAdapter.MyViewHolder> {

    private ArrayList<DurationModel> list;
    private Context mCtx;
    private OnItemClick onClickListener;


    public interface OnItemClick{
        void onClick(int pos);
    }

    void setOnClickListener(DurationAdapter.OnItemClick onClickListener){
        this.onClickListener = onClickListener;
    }

    public DurationAdapter(ArrayList<DurationModel> list, Context mCtx) {
        this.list = list;
        this.mCtx = mCtx;
    }




    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView duration,discount,amount;
        CardView card;
        public MyViewHolder(View view) {
            super(view);
            duration = view.findViewById(R.id.duration);
            discount = view.findViewById(R.id.discount);
            amount = view.findViewById(R.id.amount);
            card = view.findViewById(R.id.card);
        }
    }


    @Override
    public DurationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.duration_list_item, parent, false);

        return new DurationAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DurationAdapter.MyViewHolder holder, final int position) {

        final DurationModel model = list.get(position);
        holder.duration.setText(model.getDuration());
        holder.amount.setText(model.getAmount());
        if(!model.isHas_discount()){
            holder.discount.setVisibility(View.INVISIBLE);
        }else{
            holder.discount.setVisibility(View.VISIBLE);
            float finalAmount = Float.parseFloat(model.getAmount().split(" ")[0])-(Float.parseFloat(model.getDiscount()));
            holder.amount.setText((int)finalAmount+" "+model.getAmount().split(" ")[1]);
            holder.discount.setText(model.getAmount());
            holder.discount.setPaintFlags(holder.discount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }



}