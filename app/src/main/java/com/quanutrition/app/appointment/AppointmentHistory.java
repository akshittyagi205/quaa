package com.quanutrition.app.appointment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.quanutrition.app.R;
import com.quanutrition.app.Utils.Tools;
import com.quanutrition.app.chat.ChatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class AppointmentHistory extends Fragment {
    private static final String ARG_PARAM1 = "param1";

    private String mParam1;
    private Context context;
    private View rootView;
    RecyclerView recyclerView;
    ArrayList<AppointmentHistoryModel> list;
    UpcomingHistoryAdapter adapter;
    RelativeLayout noData;
    TextView basicBtn;

    private OnFragmentInteractionListener mListener;

    public AppointmentHistory() {
        // Required empty public constructor
    }

    public static AppointmentHistory newInstance(String param1) {
        AppointmentHistory fragment = new AppointmentHistory();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_appointment_history, container, false);
        context = getActivity();
        context = getActivity();
        recyclerView = rootView.findViewById(R.id.re);
        noData = rootView.findViewById(R.id.noData);
        noData.setVisibility(View.GONE);
        basicBtn = rootView.findViewById(R.id.basicBtn);
        basicBtn.setVisibility(View.GONE);
        basicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("action","%%BOOKAPPOINTMENT%%");
                startActivity(intent);
            }
        });

        list = new ArrayList<>();
        /*AppointmentHistoryModel model = new AppointmentHistoryModel("1","22-04-2019","12:22 P.M.","Dt. Test","Call","Confirmed","");
        AppointmentHistoryModel model1 = new AppointmentHistoryModel("1","22-04-2019","12:22 P.M.","Dt. Test","Call","Confirmed","This is a sample note that comes with this appointment.");

        list.add(model);
        list.add(model1);*/


        try {
            JSONObject ob = new JSONObject(mParam1);
                if (ob.getInt("res") == 1) {
                    JSONArray data = ob.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject appt = data.getJSONObject(i);
                        /*Calendar c = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                        Date d = sdf.parse(appt.getString("date") + " " + appt.getString("time"));
                        sdf.setTimeZone(TimeZone.getDefault());
                        String localDateTime = sdf.format(d);
                        d = sdf.parse(localDateTime);
                        if (c.getTimeInMillis() > d.getTime()) {*/
                        AppointmentHistoryModel model = new AppointmentHistoryModel(appt.getString("appointmentId"),appt.getString("date"), appt.getString("time"), appt.getString("dietitian"), appt.getString("type"), appt.getString("status"), appt.getString("clinic"));
                        model.setActivity(appt.getString("type_activity"));
                        list.add(model);
//                        }
                    }
                    if(list.size()>0){
                        noData.setVisibility(View.GONE);
                    }else{
                        noData.setVisibility(View.VISIBLE);
                    }
                }else{
                    noData.setVisibility(View.VISIBLE);
                }

        }catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new UpcomingHistoryAdapter(list,context);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

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
}
