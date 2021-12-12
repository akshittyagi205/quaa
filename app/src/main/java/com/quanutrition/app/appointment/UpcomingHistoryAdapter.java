package com.quanutrition.app.appointment;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.quanutrition.app.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class UpcomingHistoryAdapter extends RecyclerView.Adapter<UpcomingHistoryAdapter.MyViewHolder> {

    private ArrayList<AppointmentHistoryModel> list;
    private Context mCtx;
    OnRescheduleClicked onRescheduleClicked;


    public UpcomingHistoryAdapter(ArrayList<AppointmentHistoryModel> list, Context mCtx) {
        this.list = list;
        this.mCtx = mCtx;
    }

    public interface OnRescheduleClicked{
        void onClick(View view, int position);
    }

    public void setOnClickListener(OnRescheduleClicked onClickListener){
        this.onRescheduleClicked = onClickListener;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView title,date,time,status,type,notes,newDate,newYear,activity_type;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            date =  view.findViewById(R.id.date);
            time = view.findViewById(R.id.time);
            status = view.findViewById(R.id.status);
            type = view.findViewById(R.id.type);
            notes = view.findViewById(R.id.notes);
            newDate = view.findViewById(R.id.newDate);
            newYear = view.findViewById(R.id.newYear);
            activity_type = view.findViewById(R.id.activity_type);
        }
    }


    @Override
    public UpcomingHistoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.appointment_history_list_row, parent, false);

        return new UpcomingHistoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final UpcomingHistoryAdapter.MyViewHolder holder, final int position) {

        final AppointmentHistoryModel appointment = list.get(position);
        holder.title.setText("Appointment with "+appointment.getDietitianName());
        holder.date.setText(appointment.getDate()+" "+appointment.getTime());
        holder.time.setText(appointment.getTime());
        holder.status.setText("Status : "+appointment.getStatus());
        holder.type.setText("Type : "+appointment.getType());
        holder.notes.setText("Clinic : "+appointment.getNotes());
        holder.activity_type.setText("Activity : "+appointment.getActivity());
        if(appointment.getNotes().trim().isEmpty()){
            holder.notes.setVisibility(View.GONE);
        }else{
            holder.notes.setVisibility(View.VISIBLE);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy");

        try {
            Date d = sdf.parse(appointment.getDate());
            String formattedDate = sdf1.format(d);
            holder.newDate.setText(formattedDate.split("-")[0]+" "+formattedDate.split("-")[1]);
            holder.newYear.setText(formattedDate.split("-")[2]);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        /*holder.reschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRescheduleClicked.onClick(view,holder.getAdapterPosition());
            }
        });*/
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }



}