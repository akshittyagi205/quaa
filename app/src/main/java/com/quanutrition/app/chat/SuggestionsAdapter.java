package com.quanutrition.app.chat;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.quanutrition.app.R;

import java.util.ArrayList;


public class SuggestionsAdapter extends RecyclerView.Adapter<SuggestionsAdapter.MyViewHolder> {

    private ArrayList<SuggestionsModel> list;
    private Context mCtx;
    private OnSuggestionSelected onSuggestionSelected;

    public interface OnSuggestionSelected{
        void onClick(String text);
    }


    public SuggestionsAdapter(ArrayList<SuggestionsModel> list, Context mCtx, OnSuggestionSelected onSuggestionSelected) {
        this.list = list;
        this.mCtx = mCtx;
        this.onSuggestionSelected = onSuggestionSelected;
    }




    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView text;
        public MyViewHolder(View view) {
            super(view);
            text = view.findViewById(R.id.text);


        }
    }


    @Override
    public SuggestionsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.suggestions_list_item, parent, false);

        return new SuggestionsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SuggestionsAdapter.MyViewHolder holder, final int position) {

        final SuggestionsModel model = list.get(position);
        holder.text.setText(model.getSuggestions());
        holder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSuggestionSelected.onClick(model.getSuggestions());
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }



}