package com.quanutrition.app.waterintake;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.quanutrition.app.R;
import com.quanutrition.app.Utils.NetworkManager;
import com.quanutrition.app.Utils.Tools;
import com.quanutrition.app.firebaseUtils.FirebaseUtils;
import com.quanutrition.app.firebaseUtils.GraphModel;
import com.quanutrition.app.selectiondialogs.DialogUtils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class WaterGraphActivity extends AppCompatActivity implements View.OnClickListener{

    BarChart chart;
    LinearLayout setGoal;
    TextView goalText,current,dailyGoal,progress,from,to;
    String goal,currentAchieved;
    ArrayList<GraphModel> graphData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_graph);
//        Tools.setSystemBarColorCustom(this,R.color.colorPrimary);
        chart = findViewById(R.id.barGraph);
        setGoal = findViewById(R.id.setGoal);
        goalText = findViewById(R.id.goalText);
        current = findViewById(R.id.current);
        dailyGoal = findViewById(R.id.goal);
        progress = findViewById(R.id.progress);
        from = findViewById(R.id.from);
        to = findViewById(R.id.to);

        goal = getIntent().getStringExtra("goal");


        FirebaseUtils mFirebaseUtils = new FirebaseUtils(this);
        mFirebaseUtils.getWaterGraph(new FirebaseUtils.OnGraphReady() {
            @Override
            public void onDataReady(ArrayList<GraphModel> data) {

                Collections.sort(data);

                /*for(int i=0;i<data.size();i++){
                    Log.d("Water Graph Data : "+data.get(i).getDate(),data.get(i).getValue());
                }*/
                graphData = data;
                setUpBarGraph();
            }

        });


        mFirebaseUtils.getWaterToday(new FirebaseUtils.OnDataRecieved() {
            @Override
            public void onDataReady(String data) {
                currentAchieved = data;
                setProgressText(currentAchieved);

            }
        });


        setGoal.setOnClickListener(this);
        from.setOnClickListener(this);
        to.setOnClickListener(this);

        String fromTime = Tools.getGeneralSharedPref(this).getString("from","10:00");
        from.setText(Tools.getFormattedTimeAMPM(Integer.parseInt(fromTime.split(":")[0]), Integer.parseInt(fromTime.split(":")[1])));
        String toTime = Tools.getGeneralSharedPref(this).getString("to","22:00");
        to.setText(Tools.getFormattedTimeAMPM(Integer.parseInt(toTime.split(":")[0]), Integer.parseInt(toTime.split(":")[1])));

    }

    void setProgressText(String data){
        if(data.equalsIgnoreCase("1")){
            current.setText("1 Glass");
        }else
            current.setText(data+" Glasses");
        float curr = Float.parseFloat(data);
        float pro = (curr/ Float.parseFloat(goal))*100;
        if(pro<=100) {
            progress.setText("You've drank " + (int)pro + "% of your daily water goal");
        }else{
            progress.setText("Daily Water Goal Achieved!");
        }
    }

    void setUpBarGraph(){
        dailyGoal.setText(goal +" Glasses");
        goalText.setText(goal+" Glasses Per Day");
        chart.getDescription().setEnabled(false);
        chart.setPinchZoom(false);
        chart.setDrawBarShadow(false);
        chart.setDrawGridBackground(false);
        chart.setAutoScaleMinMaxEnabled(true);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        chart.animateY(1500);
        chart.getLegend().setEnabled(false);
        ArrayList<BarEntry> values = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            float val = Float.parseFloat(graphData.get(i).getValue());
            values.add(new BarEntry(i, val));
        }
        BarDataSet set1;
        set1 = new BarDataSet(values, "Steps");
        set1.setValues(values);
        set1.setDrawValues(false);
        set1.setColor(Color.parseColor("#FFFFFF"));
        set1.setDrawValues(false);
        set1.setHighLightAlpha(0);
        set1.setBarBorderWidth(1f);
        set1.setBarBorderColor(Color.parseColor("#FFFFFF"));
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        BarData data = new BarData(dataSets);
        chart.setData(data);
        chart.getBarData().setBarWidth(0.5f);
        chart.setFitBars(true);
        chart.setMaxVisibleValueCount(7);
        chart.setHorizontalScrollBarEnabled(true);
        chart.setPinchZoom(false);
        chart.setPinchZoom(false);
        chart.setDragEnabled(false);
        chart.setDrawBarShadow(false);
        chart.setDrawGridBackground(false);
        YAxis yAxiesRight = chart.getAxisRight();
        yAxiesRight.setEnabled(false);
        YAxis yAxis = chart.getAxisLeft();
        LimitLine ll2 = new LimitLine(Float.parseFloat(goal), "Daily Goal");
        ll2.setLineWidth(1f);
        ll2.setLineColor(Color.parseColor("#FFFFFF"));
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll2.setTextSize(10f);
        ll2.setTextColor(Color.parseColor("#FFFFFF"));
        yAxis.setDrawLimitLinesBehindData(false);
        yAxis.addLimitLine(ll2);
        yAxis.setTextColor(getResources().getColor(R.color.transparent));
        yAxis.setTextSize(0f);
        yAxis.setDrawGridLines(false);
        yAxis.setEnabled(true);
        yAxis.setDrawAxisLine(false);

        xAxis.setDrawAxisLine(false);
        xAxis.setTextSize(12.0f);
        xAxis.setAxisLineColor(Color.parseColor("#FFFFFF"));
        xAxis.setGridColor(Color.parseColor("#FFFFFF"));
        xAxis.setAxisLineWidth(2);
        xAxis.setTextColor(Color.parseColor("#FFFFFF"));
        final ArrayList<String> xLabel = new ArrayList<>();
        xAxis.setAxisMinimum(-0.5f);

        for(int j=0;j<7;j++){
            xLabel.add(Tools.getDayFromDate(graphData.get(j).getDate()).split(" ")[0].toUpperCase());
        }

        /*xLabel.add("MON");
        xLabel.add("TUE");
        xLabel.add("WED");
        xLabel.add("THU");
        xLabel.add("FRI");
        xLabel.add("SAT");
        xLabel.add("SUN");*/
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xLabel.get((int)value);
            }
        });

        chart.invalidate();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.setGoal){
            String[] waterData = {"2","3","4","5","6","7","8","9","10","12","15","20"};
            DialogUtils.getCustomPicker(this, "Select Water Goal", waterData, new DialogUtils.OnCustomItemPicked() {
                @Override
                public void onNumberPicked(String selected) {
                    goalText.setText(selected+" Glasses Per Day");
                    goal = selected;
                    fetchGoal(goal);
                }
            });
        }else if(id == R.id.from){
            showTimePicker(from,"from");
        }else if(id == R.id.to){
            showTimePicker(to,"to");
        }
    }

    void fetchGoal(final String goal){
        final AlertDialog ad = Tools.getDialog("Saving goal...", this);
        ad.show();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ad.dismiss();
                try {
                    JSONObject result = new JSONObject(response);
                    if (result.getInt("res") == 1) {
                        setProgressText(currentAchieved);
                        setUpBarGraph();

                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(Tools.getGeneralSharedPref(getApplicationContext()).getString("to","22:00").split(":")[0]));
                        c.set(Calendar.MINUTE, Integer.parseInt(Tools.getGeneralSharedPref(getApplicationContext()).getString("to","22:00").split(":")[1]));
                        c.set(Calendar.SECOND,0);
                        c.set(Calendar.MILLISECOND,0);

                        Calendar c1 = Calendar.getInstance();
                        c1.set(Calendar.HOUR_OF_DAY, Integer.parseInt(Tools.getGeneralSharedPref(getApplicationContext()).getString("from","10:00").split(":")[0]));
                        c1.set(Calendar.MINUTE, Integer.parseInt(Tools.getGeneralSharedPref(getApplicationContext()).getString("from","10:00").split(":")[0]));
                        c1.set(Calendar.SECOND,0);
                        c1.set(Calendar.MILLISECOND,0);

                        float interval = (c.getTimeInMillis()-c1.getTimeInMillis())/(Integer.parseInt(goal)+1);

                        SharedPreferences.Editor editor = Tools.getGeneralEditor(WaterGraphActivity.this);
                        editor.putLong("interval",(long)interval);
                        editor.commit();

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(Tools.getGeneralSharedPref(getApplicationContext()).getString("from","10:00").split(":")[0]));
                        calendar.set(Calendar.MINUTE, Integer.parseInt(Tools.getGeneralSharedPref(getApplicationContext()).getString("from","10:00").split(":")[1]));
                        AlarmManager alarmMgr;
                        PendingIntent alarmIntent;
                        alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                        Intent intent = new Intent(WaterGraphActivity.this, WaterReminderReciever.class);
                        alarmIntent = PendingIntent.getBroadcast(WaterGraphActivity.this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                (long)interval, alarmIntent);


                    } else {
                        Tools.initCustomToast(WaterGraphActivity.this, "Some error occured! Try again later.");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("Response", response);
                Log.d("myTag", "I am here");

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ad.dismiss();
                Tools.initNetworkErrorToast(WaterGraphActivity.this);
                Log.d("Error", error.toString());
                Log.d("myTag", "I am here");
            }
        };
        Map<String, String> params = new HashMap<>();
        params.put("tag","2");
        params.put("value",goal);

        NetworkManager.getInstance(this).sendPostRequestWithHeader(Urls.Save_Goal, params, listener, errorListener, this);

    }

    void showTimePicker(final TextView view, final String flag){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(WaterGraphActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                if(flag.equalsIgnoreCase("to")){
                    int fromHour = Integer.parseInt(Tools.getGeneralSharedPref(WaterGraphActivity.this).getString("from","07:00").split(":")[0]);
                    if(fromHour>=selectedHour){
                        Tools.initCustomToast(WaterGraphActivity.this,"End Time should be at least one hour greater than start time!");
                    }else{
                        String time = Tools.getFormattedTimeAMPM(selectedHour,selectedMinute);
                        view.setText(time);
                        SharedPreferences.Editor editor = Tools.getGeneralEditor(WaterGraphActivity.this);
                        editor.putString(flag,selectedHour+":"+selectedMinute);
                        editor.commit();

                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(Tools.getGeneralSharedPref(getApplicationContext()).getString("to","22:00").split(":")[0]));
                        c.set(Calendar.MINUTE, Integer.parseInt(Tools.getGeneralSharedPref(getApplicationContext()).getString("to","22:00").split(":")[1]));
                        c.set(Calendar.SECOND,0);
                        c.set(Calendar.MILLISECOND,0);

                        Calendar c1 = Calendar.getInstance();
                        c1.set(Calendar.HOUR_OF_DAY, Integer.parseInt(Tools.getGeneralSharedPref(getApplicationContext()).getString("from","10:00").split(":")[0]));
                        c1.set(Calendar.MINUTE, Integer.parseInt(Tools.getGeneralSharedPref(getApplicationContext()).getString("from","10:00").split(":")[0]));
                        c1.set(Calendar.SECOND,0);
                        c1.set(Calendar.MILLISECOND,0);

                        float interval = (c.getTimeInMillis()-c1.getTimeInMillis())/(Integer.parseInt(goal)+1);

                        SharedPreferences.Editor editor1 = Tools.getGeneralEditor(WaterGraphActivity.this);
                        editor1.putLong("interval",(long)interval);
                        editor1.commit();

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(Tools.getGeneralSharedPref(getApplicationContext()).getString("from","10:00").split(":")[0]));
                        calendar.set(Calendar.MINUTE, Integer.parseInt(Tools.getGeneralSharedPref(getApplicationContext()).getString("from","10:00").split(":")[1]));
                        AlarmManager alarmMgr;
                        PendingIntent alarmIntent;
                        alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                        Intent intent = new Intent(WaterGraphActivity.this, WaterReminderReciever.class);
                        alarmIntent = PendingIntent.getBroadcast(WaterGraphActivity.this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                (long)interval, alarmIntent);

                    }

                }else{
                    int toHour = Integer.parseInt(Tools.getGeneralSharedPref(WaterGraphActivity.this).getString("to","22:00").split(":")[0]);
                    if(toHour<=selectedHour){
                        Tools.initCustomToast(WaterGraphActivity.this,"Start Time should be at least one hour less than end time!");
                    }else{
                        String time = Tools.getFormattedTimeAMPM(selectedHour,selectedMinute);
                        view.setText(time);
                        SharedPreferences.Editor editor = Tools.getGeneralEditor(WaterGraphActivity.this);
                        editor.putString(flag,selectedHour+":"+selectedMinute);
                        editor.commit();


                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(Tools.getGeneralSharedPref(getApplicationContext()).getString("to","22:00").split(":")[0]));
                        c.set(Calendar.MINUTE, Integer.parseInt(Tools.getGeneralSharedPref(getApplicationContext()).getString("to","22:00").split(":")[1]));
                        c.set(Calendar.SECOND,0);
                        c.set(Calendar.MILLISECOND,0);

                        Calendar c1 = Calendar.getInstance();
                        c1.set(Calendar.HOUR_OF_DAY, Integer.parseInt(Tools.getGeneralSharedPref(getApplicationContext()).getString("from","10:00").split(":")[0]));
                        c1.set(Calendar.MINUTE, Integer.parseInt(Tools.getGeneralSharedPref(getApplicationContext()).getString("from","10:00").split(":")[0]));
                        c1.set(Calendar.SECOND,0);
                        c1.set(Calendar.MILLISECOND,0);

                        float interval = (c.getTimeInMillis()-c1.getTimeInMillis())/(Integer.parseInt(goal)+1);

                        SharedPreferences.Editor editor1 = Tools.getGeneralEditor(WaterGraphActivity.this);
                        editor1.putLong("interval",(long)interval);
                        editor1.commit();


                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(Tools.getGeneralSharedPref(getApplicationContext()).getString("from","10:00").split(":")[0]));
                        calendar.set(Calendar.MINUTE, Integer.parseInt(Tools.getGeneralSharedPref(getApplicationContext()).getString("from","10:00").split(":")[1]));
                        AlarmManager alarmMgr;
                        PendingIntent alarmIntent;
                        alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                        Intent intent = new Intent(WaterGraphActivity.this, WaterReminderReciever.class);
                        alarmIntent = PendingIntent.getBroadcast(WaterGraphActivity.this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                (long)interval, alarmIntent);
                    }
                }



            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }
}
