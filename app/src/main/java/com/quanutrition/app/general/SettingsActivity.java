package com.quanutrition.app.general;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.quanutrition.app.R;
import com.quanutrition.app.Utils.Constants;
import com.quanutrition.app.Utils.MyAlarmManager;
import com.quanutrition.app.Utils.NetworkManager;
import com.quanutrition.app.Utils.Tools;
import com.quanutrition.app.diet.ReminderSetupActivity;
import com.quanutrition.app.selectiondialogs.DialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    Switch generalSwitch,waterSwitch,mealSwitch,meditationSwitch;
    LinearLayout heightUnit,weightUnit,volumeUnit,energyUnit,mealReminders,meditation;
    String heightTxt="Feet and Inches",weightTxt="Kilograms",volumeTxt="Liters",energyTxt="Kilo Calories";
    TextView save,height,weight,volume,energy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        generalSwitch = findViewById(R.id.generalSwitch);
        waterSwitch = findViewById(R.id.waterSwitch);
        mealSwitch = findViewById(R.id.mealSwitch);
        heightUnit = findViewById(R.id.heightUnit);
        weightUnit = findViewById(R.id.weightUnit);
        volumeUnit = findViewById(R.id.volumeUnit);
        energyUnit = findViewById(R.id.energyUnit);
        mealReminders = findViewById(R.id.mealReminders);
        meditationSwitch = findViewById(R.id.meditationSwitch);
        meditation = findViewById(R.id.meditation);
        save = findViewById(R.id.save);
        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);
        volume = findViewById(R.id.volume);
        energy = findViewById(R.id.energy);


        generalSwitch.setOnCheckedChangeListener(this);
        waterSwitch.setOnCheckedChangeListener(this);
        mealSwitch.setOnCheckedChangeListener(this);
        meditationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = Tools.getGeneralEditor(SettingsActivity.this);
                editor.putBoolean("9Status",isChecked);
                editor.commit();
            }
        });

        generalSwitch.setChecked(true);
        waterSwitch.setChecked(true);
        mealSwitch.setChecked(true);
        meditationSwitch.setChecked(Tools.getGeneralSharedPref(this).getBoolean("9Status",true));

        Log.d("General",generalSwitch.isChecked()+"");

        heightUnit.setOnClickListener(this);
        weightUnit.setOnClickListener(this);
        volumeUnit.setOnClickListener(this);
        energyUnit.setOnClickListener(this);
        mealReminders.setOnClickListener(this);
        meditation.setOnClickListener(this);
        save.setOnClickListener(this);

        fetchData();
    }

    void fetchData(){
        final AlertDialog ad = Tools.getDialog("Fetching data...", this);
        ad.show();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ad.dismiss();
                try {
                    JSONObject result = new JSONObject(response);
                    if(result.getInt("res")==1){
                        JSONObject data = result.getJSONObject("data");
                        generalSwitch.setChecked(data.getBoolean("general"));
                        mealSwitch.setChecked(data.getBoolean("meal"));
                        waterSwitch.setChecked(data.getBoolean("water"));
                        JSONObject units = data.getJSONObject("units");
                        heightTxt = units.getString("height");
                        height.setText(heightTxt);
                        weightTxt = units.getString("weight");
                        weight.setText(weightTxt);
                        volumeTxt = units.getString("volume");
                        volume.setText(volumeTxt);
                        energyTxt = units.getString("energy");
                        energy.setText(energyTxt);
                    }else{
                        Tools.initCustomToast(SettingsActivity.this, result.getString("msg"));
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
                Tools.initNetworkErrorToast(SettingsActivity.this);
                Log.d("Error", error.toString());
                Log.d("myTag", "I am here");
            }
        };


        NetworkManager.getInstance(this).sendGetRequest(Urls.Get_Reminders, listener, errorListener, this);

    }

    void saveReminder(String general, String meal, String water, String height, String weight, String volume, String energy){
        final AlertDialog ad = Tools.getDialog("Saving reminders...", this);
        ad.show();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ad.dismiss();
                try {
                    JSONObject result = new JSONObject(response);
                    Tools.initCustomToast(SettingsActivity.this, result.getString("msg"));

                    SharedPreferences.Editor editor = Tools.getGeneralEditor(SettingsActivity.this);
                    editor.putBoolean(Constants.NOTIFICATION_GENERAL,generalSwitch.isChecked());
                    editor.putBoolean(Constants.NOTIFICATION_MEAL,mealSwitch.isChecked());
                    editor.putBoolean(Constants.NOTIFICATION_WATER,waterSwitch.isChecked());
                    editor.commit();

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
                Tools.initNetworkErrorToast(SettingsActivity.this);
                Log.d("Error", error.toString());
                Log.d("myTag", "I am here");
            }
        };
        Map<String, String> params = new HashMap<>();
        params.put("general",general);
        params.put("meals",meal);
        params.put("water",water);
        params.put("height",height);
        params.put("weight",weight);
        params.put("volume",volume);
        params.put("energy",energy);

        NetworkManager.getInstance(this).sendPostRequestWithHeader(Urls.Save_Reminders, params, listener, errorListener, this);

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.heightUnit){
            final String[] heightData = {"Feet and Inches","Centimeters"};
            DialogUtils.getCustomPicker(this, "Select Height Unit", heightData, new DialogUtils.OnCustomItemPicked() {
                @Override
                public void onNumberPicked(String selected) {

                    heightTxt = selected;
                    height.setText(selected);
                }
            });
        }else if(id == R.id.weightUnit){
            String[] weightData = {"Kilograms","Pounds"};
            DialogUtils.getCustomPicker(this, "Select Weight Unit", weightData, new DialogUtils.OnCustomItemPicked() {
                @Override
                public void onNumberPicked(String selected) {
                    weightTxt = selected;
                    weight.setText(selected);
                }
            });
        }else if(id == R.id.volumeUnit){
            String[] volumeData = {"Liters","Ounce"};
            DialogUtils.getCustomPicker(this, "Select Volume Unit", volumeData, new DialogUtils.OnCustomItemPicked() {
                @Override
                public void onNumberPicked(String selected) {
                    volumeTxt = selected;
                    volume.setText(selected);
                }
            });
        }else if(id == R.id.energyUnit){
            String[] energyData = {"Kilo Calories","Calories"};
            DialogUtils.getCustomPicker(this, "Select Energy Unit", energyData, new DialogUtils.OnCustomItemPicked() {
                @Override
                public void onNumberPicked(String selected) {
                    energyTxt = selected;
                    energy.setText(selected);
                }
            });
        }else if(id == R.id.save){
            saveReminder(generalSwitch.isChecked()+"",mealSwitch.isChecked()+"",waterSwitch.isChecked()+"",heightTxt,weightTxt,volumeTxt,energyTxt);
        }else if(id == R.id.mealReminders){
            startActivity(new Intent(this, ReminderSetupActivity.class));
        }else if(id == R.id.meditation){
            showTimePicker();
        }
    }
    void showTimePicker(){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(SettingsActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String time = Tools.getFormattedTimeAMPM(selectedHour,selectedMinute);
                SharedPreferences.Editor editor = Tools.getGeneralEditor(SettingsActivity.this);
                editor.putString("9",selectedHour+":"+selectedMinute);
                editor.commit();
                setAlarm("9",selectedHour,selectedMinute);
//                view.setText(time);
            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }


    public void setAlarm(String mealTime, int time, int min){
        AlarmManager alarmMgr;
        PendingIntent alarmIntent;
        alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyAlarmManager.class);
        intent.putExtra("mealTime",mealTime);
        intent.putExtra("status",true);
        alarmIntent = PendingIntent.getBroadcast(this, Integer.parseInt(mealTime), intent, PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, time);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND,0);
        Log.e("Time",time+" : "+min);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * 60*24, alarmIntent);

        Tools.initCustomToast(this,"Meditation Reminder set for "+Tools.getFormattedTimeAMPM(time,min));

    }

}
