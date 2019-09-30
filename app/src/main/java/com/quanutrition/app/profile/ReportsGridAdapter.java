package com.quanutrition.app.profile;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.quanutrition.app.R;
import com.quanutrition.app.Utils.Tools;

import java.util.ArrayList;


public class ReportsGridAdapter extends RecyclerView.Adapter<ReportsGridAdapter.MyViewHolder> {

    private ArrayList<ReportsModel> reports;
    private Context mCtx;
    private OnClickListener onClickListener;

    public ReportsGridAdapter(ArrayList<ReportsModel> reports, Context mCtx, OnClickListener onClickListener) {
        this.reports = reports;
        this.mCtx = mCtx;
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener{
        void onClose(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        FrameLayout imageLayout;
        ImageView image,close;
        public MyViewHolder(View view) {
            super(view);
            imageLayout = view.findViewById(R.id.imageLayout);
            image =  view.findViewById(R.id.image);
            close = view.findViewById(R.id.close);
        }
    }


    @Override
    public ReportsGridAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_grid_item, parent, false);

        return new ReportsGridAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ReportsGridAdapter.MyViewHolder holder, final int position) {

        final ReportsModel report = reports.get(position);
        if(report.getImageuri()==null){
            Tools.loadImageIntoImageView(report.getUrl(),holder.image);
        }else{
            holder.image.setImageURI(report.getImageuri());
        }
        holder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onClose(view,holder.getAdapterPosition());

            }
        });
    }

    @Override
    public int getItemCount()
    {
        Log.d("size",reports.size()+"");
        return reports.size();

    }



}