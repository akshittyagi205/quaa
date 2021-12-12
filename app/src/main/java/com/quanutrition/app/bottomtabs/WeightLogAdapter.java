package com.quanutrition.app.bottomtabs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.quanutrition.app.R;

import java.util.ArrayList;

public class WeightLogAdapter extends RecyclerView.Adapter<WeightLogAdapter.MyViewHolder> {
    private ArrayList<WeightLogModel> list;
    private Context mCtx;


    public WeightLogAdapter(ArrayList<WeightLogModel> list, Context mCtx) {
        this.list = list;
        this.mCtx = mCtx;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView date,weight;
        ImageView indicator;
        public MyViewHolder(View view) {
            super(view);
            date = view.findViewById(R.id.date);
            weight = view.findViewById(R.id.weight);
            indicator = view.findViewById(R.id.indicator);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weight_log_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final WeightLogModel model = list.get(position);
        holder.date.setText(model.getDate());
        holder.weight.setText(model.getWeight()+" Kgs");

        /*if(model.isUpDown()){
            holder.indicator.setImageResource(R.drawable.ic_weight_up);
        }else{
            holder.indicator.setImageResource(R.drawable.ic_weight_down);
        }*/

//        if(model.isIncreased()==0){
            holder.indicator.setVisibility(View.INVISIBLE);
        /*}else if(model.isIncreased()==1){
            holder.indicator.setVisibility(View.VISIBLE);
            holder.indicator.setColorFilter(mCtx.getColor(R.color.red_500));
        }else if(model.isIncreased()==2){
            holder.indicator.setVisibility(View.VISIBLE);
            holder.indicator.setColorFilter(mCtx.getColor(R.color.green_500));
        }else if(model.isIncreased()==3){
            holder.indicator.setImageResource(R.drawable.ic_tick);
            holder.indicator.setColorFilter(mCtx.getColor(R.color.green_500));
        }*/
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}