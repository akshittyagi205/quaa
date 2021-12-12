package com.quanutrition.app.dietrecall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.L;
import com.quanutrition.app.R;
import com.quanutrition.app.Utils.OnClickListener;
import com.quanutrition.app.databinding.DietRecallMealListBinding;
import com.quanutrition.app.selectiondialogs.DialogUtils;

import java.util.ArrayList;

public class DietRecallAdapter extends RecyclerView.Adapter<DietRecallAdapter.MyViewHolder> {
    private ArrayList<DietRecallModel> list;
    private Context mCtx;
    private DietRecallMealListBinding binding;
    private OnClickListener onClickListener;
    private OnClickListener onMealNameSelected;

    public void setOnMealNameSelected(OnClickListener onMealNameSelected) {
        this.onMealNameSelected = onMealNameSelected;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public DietRecallAdapter(ArrayList<DietRecallModel> list, Context mCtx) {
        this.list = list;
        this.mCtx = mCtx;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mealName,mealTime;
        RecyclerView re;
        TextView addMeal,addFood;
        public MyViewHolder(View view) {
            super(view);
            mealName = binding.mealName;
            mealTime = binding.mealTime;
            re = binding.mealItemRe;
            addMeal = binding.addMeal;
            addFood = binding.addFood;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        binding = DietRecallMealListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        View itemView = binding.getRoot();

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final DietRecallModel model = list.get(position);
        holder.mealName.setText(model.getMealname());
        holder.mealTime.setText(model.getMealTime());

        final FoodAdapter adapter = new FoodAdapter(model.getFoodList(),mCtx);
        holder.re.setLayoutManager(new LinearLayoutManager(mCtx));
        holder.re.setAdapter(adapter);

        adapter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(int pos) {
                model.getFoodList().remove(pos);
                if(model.getFoodList().size()>0)
                    adapter.notifyDataSetChanged();
                else{
                    list.remove(holder.getAdapterPosition());
                    notifyDataSetChanged();
                }

            }
        });

        holder.addMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListener!=null)
                onClickListener.onClick(holder.getAdapterPosition());
            }
        });

        holder.addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.getFoodList().add(new FoodModel("",""));
                adapter.notifyDataSetChanged();
            }
        });

        holder.mealTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.getTimePicker(mCtx, true, new DialogUtils.OnCustomItemPicked() {
                    @Override
                    public void onNumberPicked(String selected) {
                        holder.mealTime.setText(selected);
                        model.setMealTime(selected);
                    }
                });
            }
        });

        holder.mealName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onMealNameSelected!=null)
                    onMealNameSelected.onClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}