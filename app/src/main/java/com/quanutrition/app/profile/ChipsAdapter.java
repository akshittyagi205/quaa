package com.quanutrition.app.profile;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.quanutrition.app.R;

import java.util.ArrayList;


public class ChipsAdapter extends RecyclerView.Adapter<ChipsAdapter.MyViewHolder> {

    private ArrayList<ChipsModel> chips;
    private Context mCtx;
    private OnClickListener onClickListener;

    public ChipsAdapter(ArrayList<ChipsModel> chips, Context mCtx, OnClickListener onClickListener) {
        this.chips = chips;
        this.mCtx = mCtx;
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener{
        void onClose(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView letter,label;
        ImageView close;
        public MyViewHolder(View view) {
            super(view);
            letter = view.findViewById(R.id.letter);
            label =  view.findViewById(R.id.label);
            close = view.findViewById(R.id.close);
        }
    }


    @Override
    public ChipsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chip_item, parent, false);

        return new ChipsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ChipsAdapter.MyViewHolder holder, final int position) {

        final ChipsModel chip = chips.get(position);
        holder.label.setText((chip.name.charAt(0)+"").toUpperCase()+chip.name.substring(1));
        holder.letter.setText(chip.name.charAt(0)+"");
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
        return chips.size();
    }



}