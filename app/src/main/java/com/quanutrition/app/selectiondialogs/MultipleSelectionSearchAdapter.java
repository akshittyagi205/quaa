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


public class MultipleSelectionSearchAdapter extends RecyclerView.Adapter<MultipleSelectionSearchAdapter.MyViewHolder> {

    private ArrayList<MultipleSelectionModel> list;
    private ArrayList<MultipleSelectionModel> baseList;
    private Context mCtx;

    public interface OnClickListener{
        void onItemSelected(View view, int position);
    }


    public MultipleSelectionSearchAdapter(ArrayList<MultipleSelectionModel> list, Context mCtx, ArrayList<MultipleSelectionModel> baseList) {
        this.list = list;
        this.mCtx = mCtx;
        this.baseList = baseList;
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
    public MultipleSelectionSearchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.multiple_selection_list_row, parent, false);

        return new MultipleSelectionSearchAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MultipleSelectionSearchAdapter.MyViewHolder holder, final int position) {

        final MultipleSelectionModel item = list.get(position);
        holder.label.setText(item.getLabel());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(item.isSelected()){
                    item.setSelected(false);
                    holder.indicator.setVisibility(View.GONE);
                    holder.label.setTextColor(mCtx.getColor(R.color.grey_800));
                    updateBaseList(false,item);
                }else{
                    item.setSelected(true);
                    holder.indicator.setVisibility(View.VISIBLE);
                    holder.label.setTextColor(mCtx.getColor(R.color.colorAccent));
                    updateBaseList(true,item);
                }
            }
        });
        if(item.isSelected()){
            holder.indicator.setVisibility(View.VISIBLE);
            holder.label.setTextColor(mCtx.getColor(R.color.colorAccent));
        }else{
            holder.indicator.setVisibility(View.GONE);
            holder.label.setTextColor(mCtx.getColor(R.color.grey_800));
        }

    }

    void updateBaseList(boolean action,MultipleSelectionModel model){
        for(int i=0;i<baseList.size();i++){
            if(baseList.get(i).getId().equalsIgnoreCase(model.getId())){
                baseList.get(i).setSelected(action);
            }
        }
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }


}