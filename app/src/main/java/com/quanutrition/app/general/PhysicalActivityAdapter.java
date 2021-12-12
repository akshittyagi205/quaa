package com.quanutrition.app.general;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.quanutrition.app.R;

import java.util.ArrayList;
import java.util.Random;


public class PhysicalActivityAdapter extends RecyclerView.Adapter<PhysicalActivityAdapter.MyViewHolder> {

    private ArrayList<PhysicalActivityModel> list;
    private Context mCtx;
    private String[] colors = {"#f9404e","#8147bd","#62c37b","#2385be","#f49152","#7a64eb","#b14277"};

    public PhysicalActivityAdapter(ArrayList<PhysicalActivityModel> list, Context mCtx) {
        this.list = list;
        this.mCtx = mCtx;
    }




    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView placeHolder,title,duration,notes;
        public MyViewHolder(View view) {
            super(view);
            placeHolder = view.findViewById(R.id.placeHolder);
            title =  view.findViewById(R.id.title);
            duration = view.findViewById(R.id.duration);
            notes = view.findViewById(R.id.notes);
        }
    }


    @Override
    public PhysicalActivityAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.physical_activity_list_row, parent, false);

        return new PhysicalActivityAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PhysicalActivityAdapter.MyViewHolder holder, final int position) {

        final PhysicalActivityModel model = list.get(position);
        holder.title.setText(model.getTitle());
        holder.duration.setText("From : "+model.getFrom()+"\nTo : "+model.getTo());
        holder.placeHolder.setText(model.getTitle().charAt(0)+"");

        if(model.getNotes().isEmpty()){
            holder.notes.setVisibility(View.GONE);
        }else{
            holder.notes.setText(model.getNotes());
        }

        holder.placeHolder.setBackgroundColor(Color.parseColor(colors[new Random().nextInt(7)]));

    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }



}