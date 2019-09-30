package com.quanutrition.app.appointment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.quanutrition.app.R;
import com.quanutrition.app.Utils.NetworkManager;
import com.quanutrition.app.Utils.Tools;
import com.quanutrition.app.chat.ChatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AppointmentsActivity extends AppCompatActivity implements View.OnClickListener,
        UpcomingAppointmentFragment.OnFragmentInteractionListener,AppointmentHistory.OnFragmentInteractionListener{

    TextView upcoming,history;
    FloatingActionButton fab;
    ArrayList<AppointmentHistoryModel> upcomingList,historyList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        upcoming = findViewById(R.id.upcoming);
        history = findViewById(R.id.history);
        fab = findViewById(R.id.fab);

        upcomingList = new ArrayList<>();
        historyList = new ArrayList<>();

        upcoming.setOnClickListener(this);
        history.setOnClickListener(this);

        fab.setOnClickListener(this);


    }

    @Override
    protected void onResume() {
        fetchData();
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.upcoming){
            upcoming.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            upcoming.setTextColor(getResources().getColor(R.color.textColorLight));
            history.setBackgroundColor(getResources().getColor(R.color.transparent));
            history.setTextColor(getResources().getColor(R.color.colorAccent));

            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = UpcomingAppointmentFragment.newInstance(resData);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            transaction.replace(R.id.main_fragment_frame, fragment).commit();

        }else if(id == R.id.history){
            history.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            history.setTextColor(getResources().getColor(R.color.textColorLight));
            upcoming.setBackgroundColor(getResources().getColor(R.color.transparent));
            upcoming.setTextColor(getResources().getColor(R.color.colorAccent));

            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = AppointmentHistory.newInstance(resData);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            transaction.replace(R.id.main_fragment_frame, fragment).commit();
        }else if(id == R.id.fab){
            Intent intent = new Intent(AppointmentsActivity.this, ChatActivity.class);
            intent.putExtra("action","%%BOOKAPPOINTMENT%%");
            startActivity(intent);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    String resData;
    void fetchData(){
        final AlertDialog ad = Tools.getDialog("Fetching data...",this);
        ad.show();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ad.dismiss();
                Log.d("ResponseSlots",response);
                try {
                    JSONObject ob = new JSONObject(response);
                    upcoming.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    upcoming.setTextColor(getResources().getColor(R.color.textColorLight));
                    history.setBackgroundColor(getResources().getColor(R.color.transparent));
                    history.setTextColor(getResources().getColor(R.color.colorAccent));
                    resData = response;
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    Fragment fragment = UpcomingAppointmentFragment.newInstance(resData);
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    transaction.replace(R.id.main_fragment_frame, fragment).commit();

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
                Tools.initNetworkErrorToast(AppointmentsActivity.this);
                Log.d("Error",error.toString());
                Log.d("myTag","I am here");
            }
        };
        /*Map<String,String> params = new HashMap<>();
        params.put(Constants.DIETITIAN_ID,dietitianId);
        params.put("date",date);*/

        NetworkManager.getInstance(this).sendGetRequest(Urls.Get_Appointments,listener,errorListener,this);

    }
}
