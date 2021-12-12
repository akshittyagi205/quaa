package com.quanutrition.app.diet;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.quanutrition.app.R;
import com.quanutrition.app.Utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MyViewHolder> {

    private ArrayList<MealModel> mealList;
    private Context mCtx;
    private boolean defaultOpen = false;


    public MealAdapter(ArrayList<MealModel> mealList, Context mCtx) {
        this.mealList = mealList;
        this.mCtx = mCtx;
    }

    public void setDefaultOpen(boolean defaultOpen) {
        this.defaultOpen = defaultOpen;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView mealName,mealCalories,mealNotes;
        LinearLayout mealHeadLayout;
        RecyclerView meal_item_re;
        ImageView info;
        public MyViewHolder(View view) {
            super(view);
            mealName = view.findViewById(R.id.mealName);
            mealCalories =  view.findViewById(R.id.mealCalories);
            mealHeadLayout = view.findViewById(R.id.mealHeadLayout);
            meal_item_re = view.findViewById(R.id.meal_item_re);
            info = view.findViewById(R.id.info);
            mealNotes = view.findViewById(R.id.mealNotes);
        }
    }


    @Override
    public MealAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meal_list_row, parent, false);

        return new MealAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MealAdapter.MyViewHolder holder, final int position) {

        final MealModel meal = mealList.get(position);

        if(meal.isTimeFlag()&&(!meal.getMealTime().trim().isEmpty()))
        holder.mealName.setText(meal.getMealName()+" ("+Tools.getformattedTime(meal.getMealTime())+")");
        else
            holder.mealName.setText(meal.getMealName());

        if(meal.isCalorieFlag()){
            holder.mealCalories.setText(meal.getMealCalories()+" KCal.");
        }else{
            holder.mealCalories.setVisibility(View.GONE);
        }

        if(defaultOpen) {
            FoodAdapter foodAdapter = new FoodAdapter(meal.getMealData(), mCtx, meal.isRecipe_flag());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mCtx);
            holder.meal_item_re.setLayoutManager(layoutManager);
            holder.meal_item_re.setAdapter(foodAdapter);
            holder.meal_item_re.setVisibility(View.VISIBLE);
        }else{
            holder.meal_item_re.setVisibility(View.GONE);
        }

        holder.mealHeadLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.meal_item_re.getVisibility()== View.GONE) {
                    FoodAdapter foodAdapter = new FoodAdapter(meal.getMealData(), mCtx,meal.isRecipe_flag());
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mCtx);
                    holder.meal_item_re.setLayoutManager(layoutManager);
                    holder.meal_item_re.setAdapter(foodAdapter);
                    holder.meal_item_re.setVisibility(View.VISIBLE);

                }else{
                    holder.meal_item_re.setVisibility(View.GONE);
                }
            }
        });

        if(meal.getMealNotes().trim().isEmpty()){
            holder.mealNotes.setVisibility(View.GONE);
        }else{
            Tools.setHTMLData(holder.mealNotes,meal.getMealNotes());
        }

        if(meal.isMacro_flag()){
            holder.info.setVisibility(View.VISIBLE);
        }else{
            holder.info.setVisibility(View.GONE);
        }



        holder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogPolygon(position);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return mealList.size();
    }


    private void showDialogPolygon(int position) {
        final Dialog dialog = new Dialog(mCtx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_header_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        TextView fiberTxt,fatTxt,carbsTxt,protienTxt,cal;
        fiberTxt = dialog.findViewById(R.id.fiberTxt);
        fatTxt = dialog.findViewById(R.id.fatTxt);
        carbsTxt = dialog.findViewById(R.id.carbsTxt);
        protienTxt = dialog.findViewById(R.id.protienTxt);
        cal = dialog.findViewById(R.id.cal);
        MealModel model = mealList.get(position);
        try {
            Log.d("macro",model.getMacros());
            JSONObject macro = new JSONObject(model.getMacros());
            cal.setText("Calories : "+macro.optString("cal")+" KCal.");
            fiberTxt.setText("Fiber : " +macro.optString("fibre")+"g");
            fatTxt.setText("Fat : "+macro.optString("fats")+"g");
            carbsTxt.setText("Carbs : "+macro.optString("carbs")+"g");
            protienTxt.setText("Proteins : "+macro.optString("protein")+"g");
        }catch (JSONException e){
            e.printStackTrace();
        }
        dialog.show();
    }
}