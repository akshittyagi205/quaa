package com.quanutrition.app.questionnaire;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.recyclerview.widget.RecyclerView;

import com.quanutrition.app.R;

import java.util.ArrayList;

public class RadioChoiceAdapter extends RecyclerView.Adapter<RadioChoiceAdapter.MyViewHolder> {
    private ArrayList<QuestionModel.ChoiceModel> list;
    private Context mCtx;
    private OnItemClicked onItemClicked;

    public interface OnItemClicked{
        void onClick(int pos);
    }

    public void setOnItemClicked(OnItemClicked onItemClicked) {
        this.onItemClicked = onItemClicked;
    }

    public RadioChoiceAdapter(ArrayList<QuestionModel.ChoiceModel> list, Context mCtx) {
        this.list = list;
        this.mCtx = mCtx;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        RadioButton rad;

        public MyViewHolder(View view) {
            super(view);
            rad = view.findViewById(R.id.rad);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.radio_button_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final QuestionModel.ChoiceModel model = list.get(position);

        holder.rad.setText(model.getVal());
        holder.rad.setChecked(false);
        if(model.getSelected()==1)
            holder.rad.setChecked(true);
        holder.rad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(model.getSelected()!=1) {
                        onItemClicked.onClick(holder.getAdapterPosition());
                        Log.d("In On checked", model.getVal());
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}