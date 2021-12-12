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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TrainingScheduleFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    View rootView;
    RecyclerView re;
    ArrayList<TimeInputModel> list;
    TimeInputAdapter adapter;
    EditText days,fromTime,toTime,activity;
    TextView add;
    ArrayList<MultipleSelectionModel> days_base;

    public TrainingScheduleFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_training_time, container, false);
        re = rootView.findViewById(R.id.re);
        days = rootView.findViewById(R.id.days);
        fromTime = rootView.findViewById(R.id.fromTime);
        toTime = rootView.findViewById(R.id.toTime);
        add = rootView.findViewById(R.id.add);
        activity = rootView.findViewById(R.id.activity);

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


                DialogUtils.getMultipleSelectionDialogWithTitle(getActivity(),"Select Days", days_base, new DialogUtils.OnMultipleItemsSelected() {

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

                if(Tools.validateNormalText(fromTime)&&Tools.validateNormalText(toTime)&&Tools.validateNormalText(days)&&Tools.validateNormalText(activity)) {
                    if(Tools.validateTime(Tools.getText(fromTime),Tools.getText(toTime))) {
                        for (int i = 0; i < days_base.size(); i++) {
                            if (days_base.get(i).isSelected()) {
                                int flag = 0;
                                for (int j = 0; j < list.size(); j++) {
                                    if (list.get(j).getDay().equalsIgnoreCase(days_base.get(i).getLabel())) {
                                        flag++;
                                        TimeInputChildModel cm = new TimeInputChildModel(Tools.getText(fromTime), Tools.getText(toTime));
                                        cm.setActivity(Tools.getText(activity));
                                        list.get(j).addListItem(cm);
                                    }
                                }
                                if (flag == 0) {
                                    ArrayList<TimeInputChildModel> childList = new ArrayList<>();
                                    TimeInputChildModel cm = new TimeInputChildModel(Tools.getText(fromTime), Tools.getText(toTime));
                                    cm.setActivity(Tools.getText(activity));
                                    childList.add(cm);
                                    list.add(new TimeInputModel(i + "", days_base.get(i).getLabel(), childList));
                                }
                            }
                        }
                        Collections.sort(list);
                        adapter.notifyDataSetChanged();

                        days_base = new ArrayList<>();
                        final String[] daysArray = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
                        for (int i = 0; i < 7; i++) {
                            days_base.add(new MultipleSelectionModel(i + "", daysArray[i], false));
                        }
                        days.setText("");
                        fromTime.setText("");
                        toTime.setText("");
                        activity.setText("");
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
        JSONObject timeObject = new JSONObject();
        try {
        for(int i=0;i<list.size();i++){
            JSONArray array = new JSONArray();
            for(int j=0;j<list.get(i).getList().size();j++){
                JSONObject ob = new JSONObject();

                    ob.put("Activity",list.get(i).getList().get(j).getActivity());
                    ob.put("From",list.get(i).getList().get(j).getFrom());
                    ob.put("To",list.get(i).getList().get(j).getTo());

                    array.put(ob);


            }
            timeObject.put(list.get(i).getDay().toLowerCase(),array);
        }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestSave(timeObject);
    }

    void requestSave(JSONObject ob){

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
        params.put("trainingSchedule",ob+"");


        NetworkManager.getInstance(getActivity()).sendPostRequestWithHeader(Urls.save_activity_info,params,listener,errorListener,getActivity());

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
                        JSONObject trainingArray = data.getJSONObject("trainingActivity");
                        if(trainingArray.has("monday"))
                            loadDataFromList(trainingArray.getJSONArray("monday"),"Monday","0");
                        if(trainingArray.has("tuesday"))
                            loadDataFromList(trainingArray.getJSONArray("tuesday"),"Tuesday","1");
                        if(trainingArray.has("wednesday"))
                            loadDataFromList(trainingArray.getJSONArray("wednesday"),"Wednesday","2");
                        if(trainingArray.has("thursday"))
                            loadDataFromList(trainingArray.getJSONArray("thursday"),"Thursday","3");
                        if(trainingArray.has("friday"))
                            loadDataFromList(trainingArray.getJSONArray("friday"),"Friday","4");
                        if(trainingArray.has("saturday"))
                            loadDataFromList(trainingArray.getJSONArray("saturday"),"Saturday","5");
                        if(trainingArray.has("sunday"))
                            loadDataFromList(trainingArray.getJSONArray("sunday"),"Sunday","6");

                        Collections.sort(list);
                        adapter.notifyDataSetChanged();

//                        list = Tools.sortDayWise(list);
//                        adapter.notifyDataSetChanged();
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

        // params.put("cuisine",);
        // params.put("gender",genderString);

        NetworkManager.getInstance(getActivity()).sendPostRequestWithoutParams(Urls.get_activity_info,listener,errorListener,getActivity());


    }

    void loadDataFromList(JSONArray array,String day,String id) throws JSONException {

        ArrayList<TimeInputChildModel> childList = new ArrayList<>();
        for(int i=0;i<array.length();i++){
            JSONObject ob = array.getJSONObject(i);
            TimeInputChildModel childModel = new TimeInputChildModel(ob.getString("From"),ob.getString("To"));
            childModel.setActivity(ob.getString("Activity"));

            childList.add(childModel);

        }
        list.add(new TimeInputModel(id,day,childList));

    }
}
