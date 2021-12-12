package com.quanutrition.app.programs;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.quanutrition.app.R;
import com.quanutrition.app.Utils.Tools;

import java.util.ArrayList;


public class ProgramDashboardAdapter extends RecyclerView.Adapter<ProgramDashboardAdapter.MyViewHolder> {

    private ArrayList<ProgramModel> programs;
    private Context mCtx;
    private OnClickListener clickListener;


    public ProgramDashboardAdapter(ArrayList<ProgramModel> programs, Context mCtx, OnClickListener clickListener) {
        this.programs = programs;
        this.mCtx = mCtx;
        this.clickListener = clickListener;
    }

    public interface OnClickListener{
        void onClick(View view, int position);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView name;
        LinearLayout backLayout;
        ImageView image;
        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            backLayout =  view.findViewById(R.id.backLayout);
            image = view.findViewById(R.id.image);

        }
    }


    @Override
    public ProgramDashboardAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.programs_dashborad_list_item, parent, false);

        return new ProgramDashboardAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ProgramDashboardAdapter.MyViewHolder holder, final int position) {

        final ProgramModel model = programs.get(position);
        /*if(model.hasImage){
            holder.image.setVisibility(View.VISIBLE);
            Tools.loadProgram(model.imageLink,holder.image);
        }else{
            holder.image.setVisibility(View.GONE);
        }*/
        if(!model.getImageLink().isEmpty())
        Tools.loadImageIntoImageView(model.getImageLink(),holder.image);
        holder.name.setText(model.name);
        holder.backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onClick(view,holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return programs.size();
    }



}