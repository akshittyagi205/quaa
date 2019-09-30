package com.quanutrition.app.appointment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.quanutrition.app.R;
import com.quanutrition.app.Utils.ClickListener;
import com.quanutrition.app.Utils.RecyclerTouchListener;
import com.quanutrition.app.Utils.Tools;

import java.util.ArrayList;

public class AppointParentAdapter extends RecyclerView.Adapter<AppointParentAdapter.AppointParentViewHolder> {
    private Context mCtx;
    private ArrayList<AppointModel> mdata;
    private ArrayList<Integer> counter;
    public static String selectedSlot="-1";
    public static String previous="0";
    OnSlotSelected onSlotSelected;

    public interface OnSlotSelected{
        void onSelected(String slot);
    }

    public void setOnSlotSelected(OnSlotSelected onSlotSelected){
        this.onSlotSelected = onSlotSelected;
    }

    public AppointParentAdapter(Context mCtx, ArrayList<AppointModel> mdata) {
        this.mCtx = mCtx;
        this.mdata = mdata;
        counter = new ArrayList<>();
        for (int i = 0; i < mdata.size(); i++) {
            counter.add(0);
        }
    }

    @NonNull
    @Override
    public AppointParentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.appointment_list_row, parent, false);
        final AppointParentViewHolder holder = new AppointParentViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AppointParentViewHolder holder, final int position) {
        final AppointModel model = mdata.get(position);
        int numb = 0;
        for (int i = 0; i < mdata.get(position).getChildListAppoint().size(); i++) {
            if (!mdata.get(position).getChildListAppoint().get(i).isAvailabilty()) {
                numb++;
            }

        }
        numb = model.getChildListAppoint().size() - numb;
        holder.textView.setText(model.getDaySection());
        holder.number.setText(numb + " Free Slots");
        holder.image.setImageDrawable(model.getImage());
        holder.recyclerView.setLayoutManager(new GridLayoutManager(mCtx, 3));
        holder.recyclerView.setNestedScrollingEnabled(false);
        holder.recyclerView.setAdapter(new AppointChildAdapter(mCtx, model.getChildListAppoint()));
        holder.recyclerView.addOnItemTouchListener(new RecyclerTouchListener(mCtx, holder.recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                AppointModel.ChildListAppoint model1 = model.getChildListAppoint().get(position);
                if(model1.isAvailabilty()) {
                    String time = String.valueOf(model1.getTime());

                    if (time.length() == 4) {
                        time = new StringBuffer(time).insert(2, ":").toString();
                    }
                    if (time.length() == 3) {
                        time = new StringBuffer(time).insert(1, ":").toString();
                    }
                    selectedSlot = time;
//                    notifyDataSetChanged();
                    onSlotSelected.onSelected(selectedSlot);
                    Log.d("Selected time", selectedSlot);
                }else{
                    Tools.initCustomToast(mCtx,"This slot is already booked");
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public String getSelectedItem() {
        return selectedSlot;
    }

    public class AppointParentViewHolder extends RecyclerView.ViewHolder {
        TextView textView, number;
        RecyclerView recyclerView;
        LinearLayout layout;
        ImageView image;

        public AppointParentViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.txtAppointment);
            number = itemView.findViewById(R.id.txtAppointmentNumber);
            layout = itemView.findViewById(R.id.clickOpen);
            recyclerView = itemView.findViewById(R.id.recTimingSlots);
            image = itemView.findViewById(R.id.image);

        }
    }
}
