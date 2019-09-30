package com.quanutrition.app.selectiondialogs;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.quanutrition.app.R;

import java.util.ArrayList;


public class MultipleSelectionAdapter extends RecyclerView.Adapter<MultipleSelectionAdapter.MyViewHolder> {

    private ArrayList<MultipleSelectionModel> list;
    private Context mCtx;

    public interface OnClickListener{
        void onItemSelected(View view, int position);
    }


    public MultipleSelectionAdapter(ArrayList<MultipleSelectionModel> list, Context mCtx) {
        this.list = list;
        this.mCtx = mCtx;
    }




    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView label;
        ImageView indicator;
        LinearLayout layout;
        public MyViewHolder(View view) {
            super(view);
            label = view.findViewById(R.id.label);
            indicator = view.findViewById(R.id.indicator);
            layout = view.findViewById(R.id.layout);
        }
    }


    @Override
    public MultipleSelectionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.multiple_selection_list_row, parent, false);

        return new MultipleSelectionAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MultipleSelectionAdapter.MyViewHolder holder, final int position) {

        final MultipleSelectionModel item = list.get(position);
        holder.label.setText(item.getLabel());
        holder.layout.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
        holder.label.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(item.isSelected()){
                    item.setSelected(false);
                    holder.indicator.setVisibility(View.INVISIBLE);
                    holder.label.setTextColor(mCtx.getColor(R.color.grey_200));
                    notifyDataSetChanged();
                }else{
                    item.setSelected(true);
                    holder.indicator.setVisibility(View.VISIBLE);
                    holder.label.setTextColor(mCtx.getColor(R.color.colorAccent));
                    notifyDataSetChanged();
                }
            }
        });
        if(item.isSelected()){
            holder.indicator.setVisibility(View.VISIBLE);
            holder.label.setTextColor(mCtx.getColor(R.color.colorAccent));
        }else{
            holder.indicator.setVisibility(View.INVISIBLE);
            holder.label.setTextColor(mCtx.getColor(R.color.grey_200));
        }

    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }


}