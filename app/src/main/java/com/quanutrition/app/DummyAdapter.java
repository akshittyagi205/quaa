package com.quanutrition.app;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.quanutrition.app.diet.FoodDataModel;

import java.util.ArrayList;


public class DummyAdapter extends RecyclerView.Adapter<DummyAdapter.MyViewHolder> {

    private ArrayList<FoodDataModel> list;
    private Context mCtx;


    public DummyAdapter(ArrayList<FoodDataModel> list, Context mCtx) {
        this.list = list;
        this.mCtx = mCtx;
    }




    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView mealName;
        public MyViewHolder(View view) {
            super(view);
            mealName = view.findViewById(R.id.mealName);


        }
    }


    @Override
    public DummyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meal_list_row, parent, false);

        return new DummyAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DummyAdapter.MyViewHolder holder, final int position) {

        final FoodDataModel model = list.get(position);
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }



}