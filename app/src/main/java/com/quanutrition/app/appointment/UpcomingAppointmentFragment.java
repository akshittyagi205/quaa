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
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

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


public class UpcomingAppointmentFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";

    private String mParam1;
    private View rootView;
    private Context context;
    RecyclerView upcoming_re;
    ArrayList<AppointmentHistoryModel> list;
    UpcomingAppointmentAdapter adapter;
    RelativeLayout noData;
    TextView basicBtn;


    private OnFragmentInteractionListener mListener;

    public UpcomingAppointmentFragment() {
        // Required empty public constructor
    }

    public static UpcomingAppointmentFragment newInstance(String param1) {
        UpcomingAppointmentFragment fragment = new UpcomingAppointmentFragment();
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
        rootView =  inflater.inflate(R.layout.fragment_upcoming_appointment, container, false);
        context = getActivity();
        upcoming_re = rootView.findViewById(R.id.upcoming_re);
        noData = rootView.findViewById(R.id.noData);
        noData.setVisibility(View.GONE);
        basicBtn = rootView.findViewById(R.id.basicBtn);
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
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                    Date d = sdf.parse(appt.getString("date") + " " + appt.getString("time"));
                    sdf.setTimeZone(TimeZone.getDefault());
                    String localDateTime = sdf.format(d);
                    d = sdf.parse(localDateTime);
                    if (c.getTimeInMillis() <= d.getTime()) {
                        list.add(new AppointmentHistoryModel(appt.getInt("id") + "", Tools.getLocalDate(appt.getString("date")), Tools.getLocalTimeAMPM(appt.getString("time")), appt.getString("dietitian"), appt.getString("type"), appt.getString("status"), appt.getString("note")));
                    }
                }

                if(list.size()>0){
                    noData.setVisibility(View.GONE);
                }else{
                    noData.setVisibility(View.VISIBLE);
                }
            }else{
                noData.setVisibility(View.VISIBLE);
            }
        }catch (JSONException | ParseException e) {
            e.printStackTrace();
        }

        adapter = new UpcomingAppointmentAdapter(list,context);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false);
        upcoming_re.setLayoutManager(layoutManager);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(upcoming_re);
        upcoming_re.setAdapter(adapter);

        /*adapter.setOnClickListener(new UpcomingAppointmentAdapter.OnRescheduleClicked() {
            @Override
            public void onClick(View view, int position) {

            }
        });*/
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
