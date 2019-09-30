package com.quanutrition.app.appointment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.quanutrition.app.R;
import com.quanutrition.app.Utils.Constants;
import com.quanutrition.app.Utils.NetworkManager;
import com.quanutrition.app.Utils.Tools;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class AppointChildAdapter extends RecyclerView.Adapter<AppointChildAdapter.AppointChildViewHolder> {
    private Context mCtx;
    private ArrayList<AppointModel.ChildListAppoint> mData;
    AlertDialog alertDialog2;
    long startMillis = 0;
    long endMillis = 0;

    public AppointChildAdapter(Context mCtx, ArrayList<AppointModel.ChildListAppoint> mData) {
        this.mCtx = mCtx;
        this.mData = mData;
    }

    @NonNull
    @Override
    public AppointChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.timing_list_row, parent, false);
        final AppointChildViewHolder holder = new AppointChildViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AppointChildViewHolder holder, int position) {
        final AppointModel.ChildListAppoint model = mData.get(position);
        String time = String.valueOf(model.getTime());

        if (time.length() == 4) {
            time = new StringBuffer(time).insert(2, ":").toString();
        }
        if (time.length() == 3) {
            time = new StringBuffer(time).insert(1, ":").toString();
        }

        holder.textView.setText(Tools.getTime(time));

        if (!model.isAvailabilty()) {
            holder.textView.setTextColor(mCtx.getResources().getColor(R.color.grey_400));
        } else {
            holder.textView.setTextColor(mCtx.getResources().getColor(R.color.colorAccent));
        }
        /*if(time.equals(AppointParentAdapter.selectedSlot)){
            holder.textView.setTextColor(mCtx.getResources().getColor(R.color.textColorLight));
            holder.textView.setBackground(mCtx.getResources().getDrawable(R.drawable.curvededge_button));
        }*/
//        holder.textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        holder.textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (model.isAvailabilty()) {
//                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(mCtx);
//                    LayoutInflater linf = LayoutInflater.from(mCtx);
//                    final View inflator = linf.inflate(R.layout.appoint_selected_dialog, null);
//                    alertDialog.setView(inflator);
//                    alertDialog2 = alertDialog.show();
//                    final TextView date, time;
//                    Button butConfirm = inflator.findViewById(R.id.butConfirmAppointment);
//                    date = inflator.findViewById(R.id.txtSelectedDate);
//                    time = inflator.findViewById(R.id.txtSelectedTime);
//                    final EditText editNoteToDoc = inflator.findViewById(R.id.editNoteToDoc);
//                    final String time1 = String.valueOf(model.getTime());
//                    startMillis = 0;
//                    endMillis = 0;
//                    time.setText(time1);
//                    date.setText(ChatFragment.dateText.getText() + "");
//                    String[] dateBreak = ChatFragment.dateText.getText().toString().split("-");
//                    String[] timeBreak = time1.split(":");
//                    Calendar beginTime = Calendar.getInstance();
//                    beginTime.set(Integer.parseInt(dateBreak[2].trim()), Integer.parseInt(dateBreak[1].trim())-1, Integer.parseInt(dateBreak[0].trim()), Integer.parseInt(timeBreak[0].trim()), Integer.parseInt(timeBreak[1].trim()));
//                    startMillis = beginTime.getTimeInMillis();
//                    endMillis = startMillis + (15 * 60 * 1000);
//
//                    butConfirm.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            alertDialog2.dismiss();
//                            ChatFragment.bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
//                            bookAppointment(date.getText().toString(),time.getText().toString(),editNoteToDoc.getText().toString());
//                        }
//                    });
//                }else{
//                    Tools.initCustomToast(mCtx,"Sorry this slot is already booked!");
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class AppointChildViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public AppointChildViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.txtTiming);
        }
    }

    void bookAppointment(String date, String time, String note){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHERED_PREF, Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString(Constants.USER_ID,"30");
        String dietitianId = sharedPreferences.getString(Constants.DIETITIAN_ID,"29");
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("Response",response);
                try {

                    JSONObject ob = new JSONObject(response);
                    if(ob.getInt("res")==1){
                        Tools.initCustomToast(mCtx,"Appointment Booked!");
                        final ContentResolver cr1 = mCtx.getContentResolver();
                        final ContentValues values1 = new ContentValues();
                        values1.put(CalendarContract.Events.DTSTART, startMillis);
                        values1.put(CalendarContract.Events.DTEND, endMillis);
                        values1.put(CalendarContract.Events.TITLE, "Appointment with nutritionist");
                        values1.put(CalendarContract.Events.DESCRIPTION, "Call appointment with nutritionist");
                        values1.put(CalendarContract.Events.CALENDAR_ID, 3);
                        TimeZone timeZone = TimeZone.getDefault();
                        values1.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
//                            values1.put(CalendarContract.Events.EVENT_TIMEZONE,"Asia/Kolkata");

//


                        Dexter.withActivity((Activity) mCtx)
                                .withPermission(Manifest.permission.WRITE_CALENDAR)
                                .withListener(new PermissionListener() {
                                    @Override
                                    public void onPermissionGranted(PermissionGrantedResponse response) {

                                        if (ActivityCompat.checkSelfPermission(mCtx, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
                                            // TODO: Consider calling

                                            Uri uri = cr1.insert(CalendarContract.Events.CONTENT_URI, values1);
//                                                Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
                                            long eventID = Long.parseLong(uri.getLastPathSegment());
                                            Log.d("Event id",eventID+"");
                                            Tools.initCustomToast(mCtx,"Event id is : "+eventID);
                                        }



                                    }

                                    @Override
                                    public void onPermissionDenied(PermissionDeniedResponse response) {
                                        // check for permanent denial of permission
                                        if (response.isPermanentlyDenied()) {
                                            // navigate user to app settings
                                        }
                                    }

                                    @Override
                                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                        token.continuePermissionRequest();
                                    }
                                }).check();
                    }else{
                        Tools.initNetworkErrorToast(mCtx);
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
        Map<String, String> params = new HashMap<>();
        params.put(Constants.DIETITIAN_ID,dietitianId);
        params.put(Constants.USER_ID,userId);
        params.put("date",date);
        params.put("slots",time);
        params.put("note",note);
        params.put("type","Call");

        NetworkManager.getInstance(mCtx).sendPostRequest(Urls.Get_Appointments,params,listener,errorListener,mCtx);

    }
}
