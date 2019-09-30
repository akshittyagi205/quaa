package com.quanutrition.app.general;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.quanutrition.app.R;
import com.quanutrition.app.Utils.Tools;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class ReferAdapter extends RecyclerView.Adapter<ReferAdapter.MyViewHolder> {

    private ArrayList<ReferModel> list;
    private Context mCtx;


    public ReferAdapter(ArrayList<ReferModel> list, Context mCtx) {
        this.list = list;
        this.mCtx = mCtx;
    }




    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView name,joined,status;
        CircleImageView image;
        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            joined = view.findViewById(R.id.joined);
            status = view.findViewById(R.id.status);
            image = view.findViewById(R.id.image);
        }
    }


    @Override
    public ReferAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.refer_list_row, parent, false);

        return new ReferAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReferAdapter.MyViewHolder holder, final int position) {

        final ReferModel model = list.get(position);
        holder.name.setText(model.getName());
        holder.status.setText("Status : "+model.getStatus());
        holder.joined.setText("Joined On : "+model.getJoined());
        Tools.loadProfileImage(model.getImage(),holder.image);
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }



}