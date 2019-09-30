package com.quanutrition.app.profile;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.quanutrition.app.R;
import com.quanutrition.app.Utils.OnClickListener;

import java.util.ArrayList;


public class TimeInputChildAdapter extends RecyclerView.Adapter<TimeInputChildAdapter.MyViewHolder> {

    private ArrayList<TimeInputChildModel> list;
    private Context mCtx;
    private OnClickListener clickListener;


    public TimeInputChildAdapter(ArrayList<TimeInputChildModel> list, Context mCtx) {
        this.list = list;
        this.mCtx = mCtx;
    }

    public void setOnClickListener(OnClickListener clickListener){
        this.clickListener = clickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView fromTime,toTime,activity;
        ImageView close;
        View divider;
        LinearLayout activityLayout;
        public MyViewHolder(View view) {
            super(view);
            fromTime = view.findViewById(R.id.fromTime);
            toTime = view.findViewById(R.id.toTime);
            close = view.findViewById(R.id.close);
            divider = view.findViewById(R.id.divider);
            activity = view.findViewById(R.id.activity);
            activityLayout = view.findViewById(R.id.activityLayout);
        }
    }


    @Override
    public TimeInputChildAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.time_input_child_item, parent, false);

        return new TimeInputChildAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TimeInputChildAdapter.MyViewHolder holder, final int position) {

        final TimeInputChildModel model = list.get(position);
        holder.fromTime.setText(model.getFrom());
        holder.toTime.setText(model.getTo());

        holder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onClick(holder.getAdapterPosition());
            }
        });

        if(holder.getAdapterPosition()==list.size()-1){
            holder.divider.setVisibility(View.GONE);
        }else{
            holder.divider.setVisibility(View.VISIBLE);
        }

        if(model.getActivity()==null){
            holder.activityLayout.setVisibility(View.GONE);
        }else{
            holder.activityLayout.setVisibility(View.VISIBLE);
            holder.activity.setText(model.getActivity());
        }
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }



}