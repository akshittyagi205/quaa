/*
package com.quanutrition.app.bottomtabs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.quanutrition.app.R;
import com.quanutrition.app.Utils.NetworkManager;
import com.quanutrition.app.Utils.Tools;
import com.quanutrition.app.profile.BasicInfoActivity;
import com.quanutrition.app.selectiondialogs.DialogUtils;
import com.quanutrition.app.selectiondialogs.SingleSelectionModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProgressFragment extends Fragment implements View.OnClickListener{

    private OnFragmentInteractionListener mListener;
    View rootView;
    LineChart chart;
    CircularProgressBar progress_bar,progress_bar_bmi;
    TextView currentWeight,currentBMI,goalText;
    RelativeLayout noProgress;
    TextView basicBtn;

    public ProgressFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_progress, container, false);

        chart = rootView.findViewById(R.id.chart);
        progress_bar = rootView.findViewById(R.id.progress_bar);
        progress_bar_bmi = rootView.findViewById(R.id.progress_bar_bmi);
        currentWeight = rootView.findViewById(R.id.currentWeight);
        currentBMI = rootView.findViewById(R.id.currentBMI);
        goalText = rootView.findViewById(R.id.goalText);
        noProgress = rootView.findViewById(R.id.noProgress);
        basicBtn = rootView.findViewById(R.id.basicBtn);
        noProgress.setVisibility(View.GONE);
        goalText.setOnClickListener(this);
        fetchData();

        basicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BasicInfoActivity.class));
            }
        });

        return rootView;
    }


    void setUpGraph(ArrayList<Entry> values){
//        chart.setBackgroundColor(Color.WHITE);
        chart.getDescription().setEnabled(false);
        chart.setTouchEnabled(true);
        chart.setDrawGridBackground(false);
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);
        chart.setBackgroundColor(Color.parseColor("#424242"));
        chart.setPinchZoom(false);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMinimum(1f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawAxisLine(true);
        xAxis.setTextSize(12f);
        chart.animateY(500);
        chart.getLegend().setEnabled(false);


        YAxis yAxis;
        {
            yAxis = chart.getAxisLeft();
            yAxis.setDrawGridLines(false);
            yAxis.setTextSize(12f);
            yAxis.setTextColor(Color.WHITE);
            yAxis.setDrawAxisLine(true);
            yAxis.setEnabled(true);
            chart.getAxisRight().setEnabled(false);
        }

        LineDataSet set1;
        set1 = new LineDataSet(values, "DataSet 1");
        set1.setDrawIcons(false);
        set1.setColor(getResources().getColor(R.color.colorPrimary));
        set1.setCircleColor(getResources().getColor(R.color.colorPrimary));
        set1.setLineWidth(2f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setFormSize(15.f);
        set1.setValueTextSize(9f);
        set1.setDrawCircles(true);
        set1.setDrawValues(false);
        set1.setDrawFilled(false);
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        LineData data = new LineData(dataSets);
        chart.setData(data);
        chart.invalidate();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    AlertDialog alertDialog1;
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.goalText){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            LayoutInflater linf = LayoutInflater.from(getActivity());
            final View inflator = linf.inflate(R.layout.dialog_update_weight, null);
            alertDialog.setView(inflator);
            alertDialog.setCancelable(true);
            final EditText editText = inflator.findViewById(R.id.editText);
            final EditText selectWeightUnit = inflator.findViewById(R.id.selectWeightUnit);

            selectWeightUnit.setFocusable(false);
            selectWeightUnit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogUtils.getSingleSelectionDialog(getActivity(), DialogUtils.getSingleArrayListWithResource(getActivity(), R.array.weightUnit), new DialogUtils.OnSingleItemSelectedListener() {
                        @Override
                        public void onItemSelected(int position, SingleSelectionModel item) {
                            selectWeightUnit.setText(item.getLabel());
                        }
                    });
                }
            });

            Button done = inflator.findViewById(R.id.done);

            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(Tools.validateNormalText(editText)){
                        float weight = Float.parseFloat(editText.getText().toString().trim());
                        if(selectWeightUnit.getText().toString().trim().equalsIgnoreCase("Kgs")){
                            if(weight>250f){
                                Tools.initCustomToast(getActivity(),"Weight cannot be greater 250 Kgs");
                            }else {
                                updateWeight(weight + "");
                                alertDialog1.dismiss();
                            }
                        }else{
                            weight = weight*0.45f;
                            if(weight>250f){
                                Tools.initCustomToast(getActivity(),"Weight cannot be greater 550 Lbs");
                            }else {
                                updateWeight(weight + "");
                                alertDialog1.dismiss();
                            }
                        }


                    }else{
                        Tools.initCustomToast(getActivity(),"Weight cannot be empty!");
                    }
                }
            });

            alertDialog1 = null;
            alertDialog1 = alertDialog.show();
            alertDialog1.getWindow().setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.dialog_background_drawable));
        }
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    void fetchData(){
        final AlertDialog ad = Tools.getDialog("Fetching data...", getActivity());
        ad.show();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ad.dismiss();
                try {
                    JSONObject result = new JSONObject(response);
                    if (result.getInt("res") == 1) {
                        if(result.getBoolean("has_dob")) {
                            noProgress.setVisibility(View.GONE);
                            JSONArray data = result.getJSONArray("data");
                            ArrayList<Entry> values = new ArrayList<>();
                            String date = "";
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject ob = data.getJSONObject(i);
                                int day = Integer.parseInt(ob.getString("day"));
                                float val = Float.parseFloat(ob.getString("weight"));
                                date = ob.getString("date");
                                values.add(new Entry(day, val, getResources().getDrawable(R.drawable.circle_blue)));
                            }
                            setUpGraph(values);
                            if (!date.equals(""))
                                goalText.setText("Last updated on " + date);
                            else
                                goalText.setText("Click to update your weight");
                            JSONObject med_data = result.getJSONObject("med_data");
                            float currentWeight, targetWeight, currentBMI, targetBMI;
                            currentWeight = Float.parseFloat(med_data.getString("current_weight"));
                            targetWeight = Float.parseFloat(med_data.getString("ideal_weight"));
                            currentBMI = Float.parseFloat(med_data.getString("current_bmi"));
                            targetBMI = Float.parseFloat(med_data.getString("ideal_bmi"));

                            progress_bar.setProgress(100 - ((Math.abs(currentWeight - targetWeight) / targetWeight) * 100));
                            progress_bar_bmi.setProgress(100 - ((Math.abs(currentBMI - targetBMI) / targetBMI) * 100));

                            progress_bar.animate();
                            progress_bar_bmi.animate();

                            ProgressFragment.this.currentWeight.setText(currentWeight + "Kgs");
                            ProgressFragment.this.currentBMI.setText(currentBMI + "");
                        }else{
                            noProgress.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Tools.initCustomToast(getActivity(), "Some error occured! Try again later.");
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
                Tools.initNetworkErrorToast(getActivity());
                Log.d("Error", error.toString());
                Log.d("myTag", "I am here");
            }
        };
        Map<String, String> params = new HashMap<>();


        NetworkManager.getInstance(getActivity()).sendPostRequestWithHeader(Urls.Get_Progress, params, listener, errorListener, getActivity());

    }

    void updateWeight(String weight){
        final AlertDialog ad = Tools.getDialog("Saving data...", getActivity());
        ad.show();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ad.dismiss();
                try {
                    JSONObject result = new JSONObject(response);
                    if (result.getInt("res") == 1) {
                        Tools.initCustomToast(getActivity(), "Weight Updated Successfully!");
                        goalText.setText("Last updated on "+Tools.getFormattedDateToday());
                        fetchData();
                    } else {
                        Tools.initCustomToast(getActivity(), "Some error occured! Try again later.");
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
                Tools.initNetworkErrorToast(getActivity());
                Log.d("Error", error.toString());
                Log.d("myTag", "I am here");
            }
        };
        Map<String, String> params = new HashMap<>();
        params.put("weight",weight);

        NetworkManager.getInstance(getActivity()).sendPostRequestWithHeader(Urls.Save_Progress, params, listener, errorListener, getActivity());

    }

}
*/
