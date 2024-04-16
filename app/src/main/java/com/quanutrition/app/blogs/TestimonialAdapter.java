package com.quanutrition.app.blogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quanutrition.app.R;
import com.quanutrition.app.Utils.Tools;

import java.util.ArrayList;

public class TestimonialAdapter extends RecyclerView.Adapter<TestimonialAdapter.MyViewHolder>{

    private ArrayList<TestimonialModel> testmonials;
    private Context mCtx;
    private OnClickListener clickListener;

    public TestimonialAdapter(ArrayList<TestimonialModel> testimonial, Context mCtx, OnClickListener clickListener) {
        this.testmonials = testimonial;
        this.mCtx = mCtx;
        this.clickListener = clickListener;
    }

    public interface OnClickListener{
        void onClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name,content;
        LinearLayout backLayout;
        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            backLayout =  view.findViewById(R.id.backLayout);
            content = view.findViewById(R.id.content);
        }
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.testimonial_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TestimonialAdapter.MyViewHolder holder, int position) {
        final TestimonialModel testimonialModel = testmonials.get(position);

        if (!testimonialModel.getUser().isEmpty()){
            holder.name.setText("By : "+testimonialModel.getUser());
        }
        Tools.setHTMLData(holder.content,testimonialModel.getContent());
        holder.backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onClick(view,holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return testmonials.size();
    }

}

