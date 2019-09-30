package com.quanutrition.app.blogs;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.quanutrition.app.R;
import com.quanutrition.app.Utils.Tools;


import java.util.ArrayList;


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

    private ArrayList<ListModel> list;
    private Context mCtx;


    public ListAdapter(ArrayList<ListModel> list, Context mCtx) {
        this.list = list;
        this.mCtx = mCtx;
    }




    public class MyViewHolder extends RecyclerView.ViewHolder {


        ImageView image;
        public MyViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.image);


        }
    }


    @Override
    public ListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dashboard_banner_list_item, parent, false);

        return new ListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListAdapter.MyViewHolder holder, final int position) {

        final ListModel model = list.get(position);
        Tools.loadImageIntoImageView(model.getName(),holder.image);

    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }



}