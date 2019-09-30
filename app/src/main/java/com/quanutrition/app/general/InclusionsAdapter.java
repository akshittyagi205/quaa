package com.quanutrition.app.general;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.quanutrition.app.R;


import java.util.ArrayList;


public class InclusionsAdapter extends RecyclerView.Adapter<InclusionsAdapter.MyViewHolder> {

    private ArrayList<InclusionsModel> list;
    private Context mCtx;


    public InclusionsAdapter(ArrayList<InclusionsModel> list, Context mCtx) {
        this.list = list;
        this.mCtx = mCtx;
    }




    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView name,description;
        ImageView done;
        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            description = view.findViewById(R.id.description);
            done = view.findViewById(R.id.done);


        }
    }


    @Override
    public InclusionsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inclusions_list_row, parent, false);

        return new InclusionsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(InclusionsAdapter.MyViewHolder holder, final int position) {

        final InclusionsModel model = list.get(position);
        holder.name.setText(model.getName());
        if(model.getDescription().isEmpty()){
            holder.description.setVisibility(View.GONE);
        }else{
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText(model.getDescription());
        }

        if(model.isDone()){
            holder.done.setVisibility(View.VISIBLE);
        }else{
            holder.done.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }



}