package com.quanutrition.app.profile;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.quanutrition.app.R;
import com.quanutrition.app.Utils.OnClickListener;
import com.quanutrition.app.Utils.Tools;
import com.quanutrition.app.diet.FoodAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class TimeInputAdapter extends RecyclerView.Adapter<TimeInputAdapter.MyViewHolder> {

    private ArrayList<TimeInputModel> list;
    private Context mCtx;


    public TimeInputAdapter(ArrayList<TimeInputModel> list, Context mCtx) {
        this.list = list;
        this.mCtx = mCtx;
    }




    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView day;
        LinearLayout headLayout;
        RecyclerView item_re;
        ImageView indicator;
        public MyViewHolder(View view) {
            super(view);
            day = view.findViewById(R.id.day);
            headLayout = view.findViewById(R.id.headLayout);
            item_re = view.findViewById(R.id.item_re);
            indicator = view.findViewById(R.id.indicator);
        }
    }


    @Override
    public TimeInputAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.time_input_list_row, parent, false);

        return new TimeInputAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TimeInputAdapter.MyViewHolder holder, final int position) {

        final TimeInputModel meal = list.get(position);
        holder.item_re.setVisibility(View.GONE);
        holder.day.setText(meal.getDay());

        holder.headLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.item_re.getVisibility()== View.GONE) {
                    Animation an = new RotateAnimation(0.0f, 180.0f, holder.indicator.getPivotX(), holder.indicator.getPivotY());
                    an.setDuration(300);               // duration in ms
                    an.setRepeatCount(0);                // -1 = infinite repeated
                    an.setRepeatMode(Animation.REVERSE); // reverses each repeat
                    an.setFillAfter(true);               // keep rotation after animation
                    // Aply animation to image view
                    holder.indicator.startAnimation(an);
                    final TimeInputChildAdapter adapter = new TimeInputChildAdapter(meal.getList(), mCtx);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mCtx);
                    holder.item_re.setLayoutManager(layoutManager);
                    holder.item_re.setAdapter(adapter);
                    holder.item_re.setVisibility(View.VISIBLE);

                    adapter.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(int pos) {
                            meal.removeItem(pos);
                            if(meal.getList().size()==0){
                                list.remove(holder.getAdapterPosition());
                            }
                            adapter.notifyDataSetChanged();
                            notifyDataSetChanged();
                        }
                    });

                }else{
                    Animation an = new RotateAnimation(180.0f, 0.0f, holder.indicator.getPivotX(), holder.indicator.getPivotY());
                    an.setDuration(300);               // duration in ms
                    an.setRepeatCount(0);                // -1 = infinite repeated
                    an.setRepeatMode(Animation.REVERSE); // reverses each repeat
                    an.setFillAfter(true);               // keep rotation after animation
                    // Aply animation to image view
                    holder.indicator.startAnimation(an);
                    holder.item_re.setVisibility(View.GONE);
                }
            }
        });

        holder.headLayout.callOnClick();

    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }


}