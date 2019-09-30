package com.quanutrition.app.googlefit;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class StepsGraphActivity extends AppCompatActivity implements View.OnClickListener{

    BarChart chart;
    LinearLayout setGoal;
    TextView goalText,current,dailyGoal,progress;
    String goal,currentAchieved;
    ArrayList<GraphModel> graphData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_graph);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        Tools.setSystemBarColorCustom(this,R.color.colorPrimary);
        chart = findViewById(R.id.barGraph);
        setGoal = findViewById(R.id.setGoal);
        goalText = findViewById(R.id.goalText);
        current = findViewById(R.id.current);
        dailyGoal = findViewById(R.id.goal);
        progress = findViewById(R.id.progress);

        FirebaseUtils mFirebaseUtils = new FirebaseUtils(this);
        mFirebaseUtils.getStepsGraph(new FirebaseUtils.OnGraphReady() {
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

        goal = getIntent().getStringExtra("goal");


        GoogleFitUtils googleFitUtils = new GoogleFitUtils(this, new GoogleFitUtils.OnDataReady() {
            @Override
            public void onStepsReady(String data) {

                currentAchieved = data;
                setProgressText(currentAchieved);

            }

            @Override
            public void onCaloriesReady(String totalCal, String walking, String running, String other) {

            }

            @Override
            public void onWeeklyDataReady(ArrayList<String> days, ArrayList<String> steps) {

            }
        });
        googleFitUtils.setFlag(false);
        googleFitUtils.init();


        setGoal.setOnClickListener(this);
    }


    void setProgressText(String data){
        dailyGoal.setText(goal+" Steps");
        current.setText(data + " Steps");
        float curr = Float.parseFloat(data);
        float pro = (curr/ Float.parseFloat(goal))*100;
        Log.d("steps",pro+"");

        if(pro<=100) {
            progress.setText("You've taken " + (int)pro + "% of your daily steps goal");
        } else {
            progress.setText("Daily Steps Goal Achieved!");
        }
    }

    void setUpBarGraph(){
        goalText.setText(goal+" Steps per Day");
        dailyGoal.setText(goal+" Steps");
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
            String[] stepsData = {"1000","1500","2000","2500","3000","3500","4000","4500","5000","5500","6000","6500","7000","7500","8000","8500","9000","9500","10000","11000","12000","13000","14000","15000","16000","17000","18000","19000","20000"};

            DialogUtils.getCustomPicker(this, "Select Daily Goal", stepsData, new DialogUtils.OnCustomItemPicked() {
                @Override
                public void onNumberPicked(String selected) {
                    goalText.setText(selected+" Steps Per Day");
                    goal = selected;
                    setGoal(goal);
                }
            });
        }
    }

    void setGoal(String goal){
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
                    } else {
                        Tools.initCustomToast(StepsGraphActivity.this, "Some error occured! Try again later.");
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
                Tools.initNetworkErrorToast(StepsGraphActivity.this);
                Log.d("Error", error.toString());
                Log.d("myTag", "I am here");
            }
        };
        Map<String, String> params = new HashMap<>();
        params.put("tag","3");
        params.put("value",goal);

        NetworkManager.getInstance(this).sendPostRequestWithHeader(Urls.Save_Goal, params, listener, errorListener, this);

    }
}
