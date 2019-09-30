package com.quanutrition.app.selectiondialogs;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.quanutrition.app.R;

import java.util.ArrayList;


public class SingleSelectionAdapter extends RecyclerView.Adapter<SingleSelectionAdapter.MyViewHolder> {

    private ArrayList<SingleSelectionModel> list;
    private Context mCtx;
    private OnClickListener clickListener;

    public interface OnClickListener{
        void onItemSelected(View view, int position);
    }


    public SingleSelectionAdapter(ArrayList<SingleSelectionModel> list, Context mCtx, OnClickListener clickListener) {
        this.list = list;
        this.mCtx = mCtx;
        this.clickListener = clickListener;
    }




    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView label;
        public MyViewHolder(View view) {
            super(view);
            label = view.findViewById(R.id.label);
        }
    }


    @Override
    public SingleSelectionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_selection_list_row, parent, false);

        return new SingleSelectionAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SingleSelectionAdapter.MyViewHolder holder, final int position) {

        final SingleSelectionModel item = list.get(position);
        holder.label.setText(item.getLabel());
        holder.label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemSelected(view,holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }



}