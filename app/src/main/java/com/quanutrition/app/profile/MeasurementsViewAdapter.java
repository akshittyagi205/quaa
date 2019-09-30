package com.quanutrition.app.profile;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.quanutrition.app.R;

import java.util.ArrayList;


public class MeasurementsViewAdapter extends RecyclerView.Adapter<MeasurementsViewAdapter.MyViewHolder> {

    private ArrayList<MeasurementsModel> list;
    private Context mCtx;


    public MeasurementsViewAdapter(ArrayList<MeasurementsModel> list, Context mCtx) {
        this.list = list;
        this.mCtx = mCtx;
    }




    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView date,chest,arms,waist,abd,hips,thigh;
        public MyViewHolder(View view) {
            super(view);
            date = view.findViewById(R.id.date);
            chest = view.findViewById(R.id.chest);
            arms = view.findViewById(R.id.arms);
            waist = view.findViewById(R.id.waist);
            abd = view.findViewById(R.id.abd);
            hips = view.findViewById(R.id.hips);
            thigh = view.findViewById(R.id.thigh);
        }
    }


    @Override
    public MeasurementsViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.measurements_list_item, parent, false);

        return new MeasurementsViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MeasurementsViewAdapter.MyViewHolder holder, final int position) {

        final MeasurementsModel model = list.get(position);
        holder.date.setText(model.getDate());
        if(model.getAbd().trim().isEmpty())
            holder.abd.setText("-");
        else
            holder.abd.setText(model.getAbd()+" inches");
        if(model.getForearm().trim().isEmpty())
            holder.arms.setText("-");
        else
            holder.arms.setText(model.getForearm()+" inches");
        if(model.getChest().trim().isEmpty())
            holder.chest.setText("-");
        else
            holder.chest.setText(model.getChest()+" inches");
        if(model.getHips().trim().isEmpty())
            holder.hips.setText("-");
        else
            holder.hips.setText(model.getHips()+" inches");
        if(model.getThigh().trim().isEmpty())
            holder.thigh.setText("-");
        else
            holder.thigh.setText(model.getThigh()+" inches");
        if(model.getWaist().trim().isEmpty())
            holder.waist.setText("-");
        else
            holder.waist.setText(model.getWaist()+" inches");
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }



}