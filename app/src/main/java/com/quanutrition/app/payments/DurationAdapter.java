package com.quanutrition.app.payments;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.quanutrition.app.R;
import com.quanutrition.app.programs.DurationModel;

import java.util.ArrayList;


public class DurationAdapter extends RecyclerView.Adapter<DurationAdapter.MyViewHolder> {

    private ArrayList<DurationModel> list;
    private Context mCtx;
    private OnItemClick onClickListener;


    public interface OnItemClick{
        void onClick(int pos);
    }

    void setOnClickListener(OnItemClick onClickListener){
        this.onClickListener = onClickListener;
    }

    public DurationAdapter(ArrayList<DurationModel> list, Context mCtx) {
        this.list = list;
        this.mCtx = mCtx;
    }




    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView duration,amount,plan;
        CardView card;
        public MyViewHolder(View view) {
            super(view);
            duration = view.findViewById(R.id.duration);
            amount = view.findViewById(R.id.amount);
            card = view.findViewById(R.id.card);
            plan = view.findViewById(R.id.plan);
        }
    }


    @Override
    public DurationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.extension_list_item, parent, false);

        return new DurationAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DurationAdapter.MyViewHolder holder, final int position) {

        final DurationModel model = list.get(position);
        holder.duration.setText(model.getDuration());
        holder.amount.setText(model.getAmount());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onClick(holder.getAdapterPosition());
            }
        });
        holder.plan.setText(model.getName());

    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }



}