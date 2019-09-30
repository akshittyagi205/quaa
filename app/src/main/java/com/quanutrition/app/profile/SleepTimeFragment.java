package com.quanutrition.app.profile;

import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.quanutrition.app.R;
import com.quanutrition.app.Utils.NetworkManager;
import com.quanutrition.app.Utils.Tools;
import com.quanutrition.app.selectiondialogs.DialogUtils;
import com.quanutrition.app.selectiondialogs.MultipleSelectionModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SleepTimeFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    View rootView;
    RecyclerView re;
    ArrayList<TimeInputModel> list;
    TimeInputAdapter adapter;
    EditText days,fromTime,toTime;
    TextView add;
    ArrayList<MultipleSelectionModel> days_base;

    public SleepTimeFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_travel_time, container, false);
        re = rootView.findViewById(R.id.re);
        days = rootView.findViewById(R.id.days);
        fromTime = rootView.findViewById(R.id.fromTime);
        toTime = rootView.findViewById(R.id.toTime);
        add = rootView.findViewById(R.id.add);

        days_base = new ArrayList<>();
        final String[] daysArray = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
        for(int i=0;i<7;i++){
            days_base.add(new MultipleSelectionModel(i+"",daysArray[i],false));
        }

        days.setFocusable(false);
        fromTime.setFocusable(false);
        toTime.setFocusable(false);

        days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                DialogUtils.getMultipleSearchDialog(getActivity(), days_base, new DialogUtils.OnMultipleItemsSelected() {

                    @Override
                    public void onMultipleItemsSelected(ArrayList<MultipleSelectionModel> items) {

                        String string = "";
                        ArrayList<MultipleSelectionModel> temp = new ArrayList<>();
                        for (int i = 0; i < items.size(); i++) {
                            if (items.get(i).isSelected()) {
                                temp.add(items.get(i));
                                // diseasesList.add(items.get(i));
                            }
//                            string+=items.get(i).getLabel()+"   ";
                        }

                        if (items.size() > 0) {
                            if (temp.size() == 1) {
                                string = temp.get(0).getLabel();
                            } else if (temp.size() == 0) {
                                string = "";

                            } else {
                                string = temp.size() + " days selected";

                            }
                            days.setText(string);

                        }
                    }
                });
            }
        });

        fromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker(fromTime);
            }
        });

        toTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker(toTime);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Tools.validateNormalText(fromTime)&&Tools.validateNormalText(toTime)&&Tools.validateNormalText(days)) {
                    if(Tools.validateTime(Tools.getText(fromTime),Tools.getText(toTime))) {
                    for (int i = 0; i < days_base.size(); i++) {
                        if (days_base.get(i).isSelected()) {
                            int flag = 0;
                            for (int j = 0; j < list.size(); j++) {
                                if (list.get(j).getDay().equalsIgnoreCase(days_base.get(i).getLabel())) {
                                    flag++;
                                    list.get(j).addListItem(new TimeInputChildModel(Tools.getText(fromTime), Tools.getText(toTime)));
                                }
                            }
                            if (flag == 0) {
                                ArrayList<TimeInputChildModel> childList = new ArrayList<>();
                                childList.add(new TimeInputChildModel(Tools.getText(fromTime), Tools.getText(toTime)));
                                list.add(new TimeInputModel(i + "", days_base.get(i).getLabel(), childList));
                            }
                        }
                    }
//                    list = Tools.sortDayWise(list);
                    adapter.notifyDataSetChanged();

                    days_base = new ArrayList<>();
                    final String[] daysArray = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
                    for (int i = 0; i < 7; i++) {
                        days_base.add(new MultipleSelectionModel(i + "", daysArray[i], false));
                    }
                    days.setText("");
                    fromTime.setText("");
                    toTime.setText("");
                    }else{
                        Tools.initCustomToast(getActivity(),"Start Time cannot be greater than or equal to End Time!");
                    }
                }else{
                    Tools.initCustomToast(getActivity(),"Please enter all the data!");
                }
            }
        });

        list = new ArrayList<>();

        adapter = new TimeInputAdapter(list,getActivity());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        re.setLayoutManager(layoutManager);
        re.setAdapter(adapter);

        rootView.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepareSave();
            }
        });

        requestFetch();
        return rootView;
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


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    void showTimePicker(final EditText view){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String time = Tools.getFormattedTimeAMPM(selectedHour,selectedMinute);
                view.setText(time);
            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    void prepareSave(){
        JSONArray timeArray = new JSONArray();
        for(int i=0;i<list.size();i++){

            for(int j=0;j<list.get(i).getList().size();j++){
                JSONObject ob = new JSONObject();
                try {
                    ob.put("day",list.get(i).getDay());
                    ob.put("from",Tools.getUnformattedTime(list.get(i).getList().get(j).getFrom()));
                    ob.put("to",Tools.getUnformattedTime(list.get(i).getList().get(j).getTo()));

                    timeArray.put(ob);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        requestSave(timeArray);
    }

    void requestSave(JSONArray array){

        final AlertDialog ad = Tools.getDialog("Requesting ..",getActivity());
        ad.show();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ad.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    int res = object.getInt("res");

                    Tools.initCustomToast(getActivity().getApplicationContext(),object.getString("msg"));

                } catch (JSONException e) {
                    e.printStackTrace();
                    Tools.initCustomToast(getActivity().getApplicationContext(),"Some Error");

                }

                Log.d("Response",response);
                Log.d("myTag","I am here");

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ad.dismiss();
                Tools.initNetworkErrorToast(getActivity());
                Log.d("Error",error.toString());
                Log.d("myTag","I am here");
            }
        };
        Map<String, String> params = new HashMap<>();
        params.put("data",array+"");
        params.put("type","0");


        NetworkManager.getInstance(getActivity()).sendPostRequestWithHeader(Urls.save_ts_info,params,listener,errorListener,getActivity());

    }

    void requestFetch(){
        final AlertDialog ad = Tools.getDialog("Requesting ..",getActivity());
        ad.show();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ad.dismiss();
                Log.d("Response",response);
                try {
                    JSONObject result = new JSONObject(response);
                    int res = result.getInt("res");
                    String msg = result.getString("msg");
                    if(res ==1) {

                        JSONObject data = result.getJSONObject("data");
                        JSONArray dataArray = data.getJSONArray("data");
                        list.clear();
                        for(int i=0;i<dataArray.length();i++){
                            JSONObject ob = dataArray.getJSONObject(i);
                            String day = ob.getString("day");
                            String from = Tools.getformattedTime(ob.getString("from"));
                            String to = Tools.getformattedTime(ob.getString("to"));
                            int flag=0;
                            for (int j = 0; j < list.size(); j++) {
                                if (list.get(j).getDay().equalsIgnoreCase(day)) {
                                    flag++;
                                    list.get(j).addListItem(new TimeInputChildModel(from,to));
                                }
                            }
                            if (flag == 0) {
                                ArrayList<TimeInputChildModel> childList = new ArrayList<>();
                                childList.add(new TimeInputChildModel(from,to));
                                list.add(new TimeInputModel(i + "", day, childList));
                            }
                        }
//                        list = Tools.sortDayWise(list);
                        adapter.notifyDataSetChanged();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ad.dismiss();
                Tools.initNetworkErrorToast(getActivity());
                Log.d("Error",error.toString());
                Log.d("myTag","I am here");
            }
        };

        HashMap<String,String> params = new HashMap<>();
        params.put("type","0");
        // params.put("gender",genderString);

        NetworkManager.getInstance(getActivity()).sendPostRequestWithHeader(Urls.get_ts_info,params,listener,errorListener,getActivity());
    }
}
