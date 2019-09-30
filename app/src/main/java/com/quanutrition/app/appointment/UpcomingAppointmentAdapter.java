package com.quanutrition.app.appointment;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.quanutrition.app.R;
import com.quanutrition.app.Utils.Constants;
import com.quanutrition.app.Utils.NetworkManager;
import com.quanutrition.app.Utils.Tools;
import com.quanutrition.app.chat.Urls;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class UpcomingAppointmentAdapter extends RecyclerView.Adapter<UpcomingAppointmentAdapter.MyViewHolder> {

    private ArrayList<AppointmentHistoryModel> list;
    private Context mCtx;
    OnRescheduleClicked onRescheduleClicked;
    AlertDialog ad1=null;
    RelativeLayout loadingLayout;
    ArrayList<AppointModel> parentData;
    ArrayList<AppointModel.ChildListAppoint> morning, afternoon, evening, night;
    ArrayList<AppointModel.ChildListAppoint> allSlots;
    AppointParentAdapter adapter;
    boolean availabe = true;
    int tim;


    public UpcomingAppointmentAdapter(ArrayList<AppointmentHistoryModel> list, Context mCtx) {
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


        TextView title,date,time,status,type,notes,reschedule;
        TextView editDate,editTime,cancel;
        LinearLayout layNotes;
        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            date =  view.findViewById(R.id.date);
            time = view.findViewById(R.id.time);
            status = view.findViewById(R.id.status);
            type = view.findViewById(R.id.type);
            notes = view.findViewById(R.id.notes);
            layNotes = view.findViewById(R.id.layNotes);
            reschedule = view.findViewById(R.id.reschedule);
            editDate = view.findViewById(R.id.editDate);
            editTime = view.findViewById(R.id.editTime);
            cancel = view.findViewById(R.id.cencel);

        }
    }


    @Override
    public UpcomingAppointmentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.upcoming_appointment_list_row, parent, false);

        return new UpcomingAppointmentAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final UpcomingAppointmentAdapter.MyViewHolder holder, final int position) {

        final AppointmentHistoryModel appointment = list.get(position);
        holder.title.setText("Appointment with "+appointment.getDietitianName());
        holder.date.setText(appointment.getDate());
        holder.time.setText(appointment.getTime());
        holder.status.setText(appointment.getStatus());
        holder.type.setText(appointment.getType());
        holder.notes.setText(appointment.getNotes());
        holder.editTime.setVisibility(View.GONE);
        holder.editDate.setVisibility(View.GONE);
        holder.cancel.setVisibility(View.GONE);
        if(appointment.getNotes().trim().isEmpty()){
            holder.layNotes.setVisibility(View.INVISIBLE);
        }else{
            holder.layNotes.setVisibility(View.VISIBLE);
        }
        holder.reschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(holder.reschedule.getText().toString().equalsIgnoreCase("reschedule")){
                    holder.editTime.setVisibility(View.VISIBLE);
                    holder.editDate.setVisibility(View.VISIBLE);
                    holder.cancel.setVisibility(View.VISIBLE);
                    parentData = new ArrayList<>();
                    morning = new ArrayList<>();
                    afternoon = new ArrayList<>();
                    evening = new ArrayList<>();
                    night = new ArrayList<>();
                    allSlots = new ArrayList<>();
                    fetchSlots(holder.date.getText().toString());
                    holder.reschedule.setText("Confirm");
                }else{

                    if(holder.time.getText().toString().isEmpty()){
                        Tools.initCustomToast(mCtx,"Please Select a Time Slot!");
                    }else {

                        sendR(holder.date.getText().toString(), Tools.getUnformattedTime(holder.time.getText().toString()), appointment.getId());

                        holder.editTime.setVisibility(View.GONE);
                        holder.editDate.setVisibility(View.GONE);
                        holder.cancel.setVisibility(View.GONE);
                        holder.reschedule.setText("Reschedule");
                    }

                }

            }
        });

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.date.setText(appointment.getDate());
                holder.time.setText(appointment.getTime());
                holder.editTime.setVisibility(View.GONE);
                holder.editDate.setVisibility(View.GONE);
                holder.cancel.setVisibility(View.GONE);
                holder.reschedule.setText("Reschedule");
            }
        });

        holder.editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cur_calender = Calendar.getInstance();
//                cur_calender.add(Calendar.YEAR, -5);
                DatePickerDialog datePicker = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, monthOfYear);
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                long date_ship_millis = calendar.getTimeInMillis();

                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                Date c = calendar.getTime();
                                String date = sdf.format(c);
                                int yearNow = Integer.parseInt(date.split("-")[2]);
                                Log.d("date",date);

                                holder.date.setText(sdf.format(date_ship_millis));
                                holder.time.setText("");
                                fetchSlots(holder.date.getText().toString().trim());

                            }
                        },
                        cur_calender.get(Calendar.YEAR),
                        cur_calender.get(Calendar.MONTH),
                        cur_calender.get(Calendar.DAY_OF_MONTH)
                );
                //set dark light
                datePicker.setThemeDark(false);
                datePicker.setAccentColor(mCtx.getResources().getColor(R.color.textColorLight));
                datePicker.setMinDate(cur_calender);
                datePicker.show(((Activity)mCtx).getFragmentManager(), "Date");
            }
        });

        holder.editTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSlots(holder);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }


    void showSlots(final UpcomingAppointmentAdapter.MyViewHolder holder){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(mCtx);
        // Get the layout inflater
        LayoutInflater linf = LayoutInflater.from(mCtx);

        final View inflator = linf.inflate(R.layout.activity_appointment, null);
        RecyclerView recyclerView = inflator.findViewById(R.id.recAppointment);
//        TextView select = inflator.findViewById(R.id.select);
//        loadingLayout = inflator.findViewById(R.id.loadingLayout);

        recyclerView.setLayoutManager(new LinearLayoutManager(mCtx));
        adapter = new AppointParentAdapter(mCtx, parentData);
        recyclerView.setAdapter(adapter);
        alertDialog.setView(inflator);
        ad1 = alertDialog.show();
        ad1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        /*select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String slot = adapter.getSelectedItem();
                if(slot.equals("-1")){
                    Tools.initCustomToast(mCtx,"Please select a post!");
                }else {
                    holder.timeEdit.setText(slot);
                    AppointParentAdapter.selectedSlot="-1";
                    ad1.dismiss();
                }
            }
        });*/

        adapter.setOnSlotSelected(new AppointParentAdapter.OnSlotSelected() {
            @Override
            public void onSelected(String slot) {
                holder.time.setText(Tools.getTime(slot));
                ad1.dismiss();

            }
        });

        timeDta(allSlots);
    }

    private void timeDta(ArrayList<AppointModel.ChildListAppoint> time) {
        parentData.clear();
        morning.clear();
        afternoon.clear();
        evening.clear();
        night.clear();
        for (int i = 0; i < time.size(); i++) {
            AppointModel.ChildListAppoint row = time.get(i);
            tim = Integer.parseInt(row.getTime().split(":")[0]);
            /**
             * Morning
             */
            if (tim >= 6 && tim < 12) {
                morning.add(row);

            }
            /**
             * Afternoon
             */
            if (tim >= 12 && tim < 16) {
                afternoon.add(row);
            }
            /**
             * Evening
             */
            if (tim >= 16 && tim < 19) {
                evening.add(row);
            }
            /**
             * Night
             */
            if (tim >= 19) {
                night.add(row);
            }
        }

        if (!morning.isEmpty())
            parentData.add(new AppointModel("Morning",mCtx.getResources().getDrawable(R.drawable.ic_morning), morning));
        if (!afternoon.isEmpty())

            parentData.add(new AppointModel("Afternoon",mCtx.getResources().getDrawable(R.drawable.ic_afternoon), afternoon));
        if (!evening.isEmpty())
            parentData.add(new AppointModel("Evening",mCtx.getResources().getDrawable(R.drawable.ic_evening), evening));
        if (!night.isEmpty())
            parentData.add(new AppointModel("Night",mCtx.getResources().getDrawable(R.drawable.ic_night), night));

        adapter.notifyDataSetChanged();

    }

    void fetchSlots(String date){
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ResponseSlots",response);
                try {
                    allSlots.clear();
                    org.json.JSONObject ob = new org.json.JSONObject(response);
                    if(ob.getInt("res")==1){
                        JSONArray slots = ob.getJSONArray("data");
                        for(int i=0;i<slots.length();i++){
                            org.json.JSONObject slot = slots.optJSONObject(i);
                            if(slot.getInt("status")==1){
                                availabe = true;
                            }else{
                                availabe=false;
                            }
                            allSlots.add(new AppointModel.ChildListAppoint(Tools.getLocalSlot(slot.getString("slot")),availabe));
                        }
//                        timeDta(allSlots);
                    }else{

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("myTag","I am here");
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Tools.initNetworkErrorToast(mCtx);
                Log.d("Error",error.toString());
                Log.d("myTag","I am here");
            }
        };
        /*Map<String,String> params = new HashMap<>();
        params.put(Constants.DIETITIAN_ID,dietitianId);
        params.put("date",date);*/

        String[] range = Tools.getDateTimeRange(date);
        String url = Urls.Get_Slots+"?date="+date+"&dietitianId="+Tools.getGeneralSharedPref(mCtx).getString(Constants.DIETITIAN_ID,"0")+"&start="+range[0]+"&end="+range[1];

        NetworkManager.getInstance(mCtx).sendGetRequest(url,listener,errorListener,mCtx);

    }

    private void sendR(final String date, final String slot, String id) {
        final AlertDialog ad = Tools.getDialog("Booking...",mCtx);
        ad.show();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ad.dismiss();
                Log.d("ResponseSlots",response);
                try {
                    allSlots.clear();
                    org.json.JSONObject ob = new org.json.JSONObject(response);
                    if(ob.getInt("res")==1) {


                        showCalendarDialog(Tools.getBeginTimeInMilli(date, slot), Tools.getEndTimeInMilli(date, slot));
                    }
                        Tools.initCustomToast(mCtx,ob.getString("msg"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("myTag","I am here");
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ad.dismiss();
                Tools.initNetworkErrorToast(mCtx);
                Log.d("Error",error.toString());
                Log.d("myTag","I am here");
            }
        };
        Map<String, String> params = new HashMap<>();
        params.put(Constants.DIETITIAN_ID,Tools.getGeneralSharedPref(mCtx).getString(Constants.DIETITIAN_ID,"0"));
        params.put("date",Tools.getUTCSlot(date,slot).split(" ")[0]);
        params.put("appointId",id);
        params.put("slots",Tools.getUTCSlot(date,slot).split(" ")[1]);

        NetworkManager.getInstance(mCtx).sendPostRequestWithHeader(com.quanutrition.app.appointment.Urls.Reschedule_appointment,params,listener,errorListener,mCtx);
    }


    void showCalendarDialog(final long begin, final long end) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(mCtx);
        // Setting Dialog Message
        alertDialog.setTitle("Appointment Booked!");
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Do you want to add this appointment to your calendar?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.Events.TITLE, "Appointment with "+Tools.getGeneralSharedPref(mCtx).getString(Constants.DIETITIAN_NAME,"Dietitian"))
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, begin)
                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end);
                if (intent.resolveActivity(mCtx.getPackageManager()) != null) {
                    mCtx.startActivity(intent);
                }
            }
        });
        alertDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.show();
    }

}