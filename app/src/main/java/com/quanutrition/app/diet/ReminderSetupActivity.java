package com.quanutrition.app.diet;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.quanutrition.app.R;
import com.quanutrition.app.Utils.Constants;
import com.quanutrition.app.Utils.MyAlarmManager;
import com.quanutrition.app.Utils.Tools;

import java.text.ParseException;
import java.util.Calendar;

public class ReminderSetupActivity extends AppCompatActivity {

    CheckBox check_early,check_breakfast,check_mid,check_lunch,check_evening,check_late_evening,check_dinner,check_post;
    TextView text_early,text_breakfast,text_mid,text_lunch,text_evening,text_late_evening,text_dinner,text_post;
    TextView time_early,time_breakfast,time_mid,time_lunch,time_evening,time_late_evening,time_dinner,time_post;
    Button save;
    String userId;
    boolean[] checkList = new boolean[8];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_setup);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Meal Reminder Setup");
        setViews();

        SharedPreferences sharedPreferences= Tools.getGeneralSharedPref(this);
        userId=sharedPreferences.getString(Constants.USER_ID,"30");
        time_early.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkList[0]) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(ReminderSetupActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            time_early.setText(Tools.getFormattedTimeAMPM(selectedHour,selectedMinute));
                        }
                    }, hour, minute, false);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }else{
                    Tools.initCustomToast(ReminderSetupActivity.this,"Tick the checkbox then select time!");
                }
            }
        });


        time_breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkList[1]) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(ReminderSetupActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            time_breakfast.setText(Tools.getFormattedTimeAMPM(selectedHour,selectedMinute));
                        }
                    }, hour, minute, false);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }else{
                    Tools.initCustomToast(ReminderSetupActivity.this,"Tick the checkbox then select time!");
                }

            }

        });


        time_lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkList[3]) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(ReminderSetupActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            time_lunch.setText(Tools.getFormattedTimeAMPM(selectedHour,selectedMinute));
                        }
                    }, hour, minute, false);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }else{
                    Tools.initCustomToast(ReminderSetupActivity.this,"Tick the checkbox then select time!");
                }
            }
        });


        time_mid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkList[2]) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(ReminderSetupActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            time_mid.setText(Tools.getFormattedTimeAMPM(selectedHour,selectedMinute));
                        }
                    }, hour, minute, false);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }else{
                    Tools.initCustomToast(ReminderSetupActivity.this,"Tick the checkbox then select time!");
                }
            }
        });


        time_evening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkList[4]) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(ReminderSetupActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            time_evening.setText(Tools.getFormattedTimeAMPM(selectedHour,selectedMinute));
                        }
                    }, hour, minute, false);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }else{
                    Tools.initCustomToast(ReminderSetupActivity.this,"Tick the checkbox then select time!");
                }
            }
        });

        time_late_evening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkList[5]) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(ReminderSetupActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            time_late_evening.setText(Tools.getFormattedTimeAMPM(selectedHour,selectedMinute));
                        }
                    }, hour, minute, false);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }else{
                    Tools.initCustomToast(ReminderSetupActivity.this,"Tick the checkbox then select time!");
                }
            }
        });

        time_dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkList[6]) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(ReminderSetupActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            time_dinner.setText( Tools.getFormattedTimeAMPM(selectedHour,selectedMinute));
                        }
                    }, hour, minute, false);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }else{
                    Tools.initCustomToast(ReminderSetupActivity.this,"Tick the checkbox then select time!");
                }
            }
        });

        time_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkList[7]) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(ReminderSetupActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            time_post.setText( Tools.getFormattedTimeAMPM(selectedHour,selectedMinute));
                        }
                    }, hour, minute, false);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }else{
                    Tools.initCustomToast(ReminderSetupActivity.this,"Tick the checkbox then select time!");
                }
            }
        });

        check_early.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(check_early.isChecked())
                {
                    text_early.setTextColor(getResources().getColor(R.color.grey_600));
                    time_early.setTextColor(getResources().getColor(R.color.colorAccent));
                }
                else
                {
                    text_early.setTextColor(getResources().getColor(R.color.grey_400));
                    time_early.setTextColor(getResources().getColor(R.color.grey_400));
                }
                checkList[0]=isChecked;
            }
        });


        check_breakfast.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(check_breakfast.isChecked())
                {

                    text_breakfast.setTextColor(getResources().getColor(R.color.grey_600));
                    time_breakfast.setTextColor(getResources().getColor(R.color.colorAccent));
                }
                else
                {
                    text_breakfast.setTextColor(getResources().getColor(R.color.grey_400));
                    time_breakfast.setTextColor(getResources().getColor(R.color.grey_400));
                }
                checkList[1]=isChecked;
            }
        });



        check_mid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(check_mid.isChecked())
                {
                    text_mid.setTextColor(getResources().getColor(R.color.grey_600));
                    time_mid.setTextColor(getResources().getColor(R.color.colorAccent));
                }

                else
                {
                    text_mid.setTextColor(getResources().getColor(R.color.grey_400));
                    time_mid.setTextColor(getResources().getColor(R.color.grey_400));
                }
                checkList[2]=isChecked;
            }
        });



        check_lunch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(check_lunch.isChecked())
                {
                    text_lunch.setTextColor(getResources().getColor(R.color.grey_600));
                    time_lunch.setTextColor(getResources().getColor(R.color.colorAccent));
                }
                else
                {
                    text_lunch.setTextColor(getResources().getColor(R.color.grey_400));
                    time_lunch.setTextColor(getResources().getColor(R.color.grey_400));
                }
                checkList[3]=isChecked;
            }
        });


        check_evening.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(check_evening.isChecked())
                {
                    text_evening.setTextColor(getResources().getColor(R.color.grey_600));
                    time_evening.setTextColor(getResources().getColor(R.color.colorAccent));
                }
                else
                {
                    text_evening.setTextColor(getResources().getColor(R.color.grey_400));
                    time_evening.setTextColor(getResources().getColor(R.color.grey_400));
                }
                checkList[4]=isChecked;
            }
        });

        check_late_evening.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(check_late_evening.isChecked())
                {
                    text_late_evening.setTextColor(getResources().getColor(R.color.grey_600));
                    time_late_evening.setTextColor(getResources().getColor(R.color.colorAccent));
                }
                else
                {
                    text_late_evening.setTextColor(getResources().getColor(R.color.grey_400));
                    time_late_evening.setTextColor(getResources().getColor(R.color.grey_400));
                }
                checkList[5]=isChecked;
            }
        });

        check_dinner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(check_dinner.isChecked())
                {
                    text_dinner.setTextColor(getResources().getColor(R.color.grey_600));
                    time_dinner.setTextColor(getResources().getColor(R.color.colorAccent));
                }
                else
                {
                    text_dinner.setTextColor(getResources().getColor(R.color.grey_400));
                    time_dinner.setTextColor(getResources().getColor(R.color.grey_400));
                }
                checkList[6]=isChecked;
            }
        });

        check_post.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(check_post.isChecked())
                {
                    text_post.setTextColor(getResources().getColor(R.color.grey_600));
                    time_post.setTextColor(getResources().getColor(R.color.colorAccent));
                }
                else
                {
                    text_post.setTextColor(getResources().getColor(R.color.grey_400));
                    time_post.setTextColor(getResources().getColor(R.color.grey_400));
                }
                checkList[7]=isChecked;
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SharedPreferences.Editor editor = Tools.getGeneralEditor(ReminderSetupActivity.this);
                    editor.putString("1", Tools.getUnformattedTime(time_early.getText().toString()));
                    editor.putBoolean("1Status", checkList[0]);
                    editor.putString("2", Tools.getUnformattedTime(time_breakfast.getText().toString()));
                    editor.putBoolean("2Status", checkList[1]);
                    editor.putString("3", Tools.getUnformattedTime(time_mid.getText().toString()));
                    editor.putBoolean("3Status", checkList[2]);
                    editor.putString("4", Tools.getUnformattedTime(time_lunch.getText().toString()));
                    editor.putBoolean("4Status", checkList[3]);
                    editor.putString("5", Tools.getUnformattedTime(time_evening.getText().toString()));
                    editor.putBoolean("5Status", checkList[4]);
                    editor.putString("6", Tools.getUnformattedTime(time_late_evening.getText().toString()));
                    editor.putBoolean("6Status", checkList[5]);
                    editor.putString("7", Tools.getUnformattedTime(time_dinner.getText().toString()));
                    editor.putBoolean("7Status", checkList[6]);
                    editor.putString("8", Tools.getUnformattedTime(time_post.getText().toString()));
                    editor.putBoolean("8Status", checkList[7]);
                    editor.commit();
                    setUpAlarm();
                    Tools.initCustomToast(ReminderSetupActivity.this, "Reminders setup successful!");
                    finish();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        loadData();
    }

    void setUpAlarm(){
        SharedPreferences sharedPreferences = Tools.getGeneralSharedPref(this);
        for(int i=1;i<=8;i++){
            String time = sharedPreferences.getString(i+"","0:0");
            boolean status = sharedPreferences.getBoolean(i+"Status",false);
            int hour = Integer.parseInt(time.split(":")[0]);
            int min = Integer.parseInt((time.split(":")[1]).split(" ")[0]);
            setAlarm(i+"",hour,min,status);
        }
    }
    public void setAlarm(String mealTime, int time, int min, boolean status){
        AlarmManager alarmMgr;
        PendingIntent alarmIntent;
        alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyAlarmManager.class);
        intent.putExtra("mealTime",mealTime);
        intent.putExtra("status",status);
        alarmIntent = PendingIntent.getBroadcast(this, Integer.parseInt(mealTime), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, time);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND,0);

        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * 60*24, alarmIntent);
        Log.d("Setting Alarm",time+" : "+min+" "+status);
    }
    private void setViews() {

        check_early = findViewById(R.id.checkboxEarly);
        check_breakfast = findViewById(R.id.checkboxBreak);
        check_mid = findViewById(R.id.checkboxMidMorning);
        check_lunch = findViewById(R.id.checkboxLunch);
        check_evening = findViewById(R.id.checkboxEvening);
        check_late_evening = findViewById(R.id.checkboxLateEvening);
        check_dinner = findViewById(R.id.checkboxDinner);
        check_post = findViewById(R.id.checkboxPostDinner);

        time_early = findViewById(R.id.EMtimetxt);
        time_breakfast = findViewById(R.id.BtimeTxt);
        time_mid = findViewById(R.id.MMtimeTxt);
        time_lunch = findViewById(R.id.LtimeTxt);
        time_evening = findViewById(R.id.EtimeTxt);
        time_late_evening = findViewById(R.id.LateEtimeTxt);
        time_dinner = findViewById(R.id.DtimeTxt);
        time_post = findViewById(R.id.PDtimeTxt);

        text_early = findViewById(R.id.earlyMorningTxt);
        text_breakfast = findViewById(R.id.breakfastTxt);
        text_mid = findViewById(R.id.MidMorningText);
        text_lunch = findViewById(R.id.lunchTxt);
        text_late_evening = findViewById(R.id.LateEveningTxt);
        text_evening = findViewById(R.id.EveningTxt);
        text_dinner = findViewById(R.id.DinnerTxt);
        text_post = findViewById(R.id.postDinnerTxt);

        save = findViewById(R.id.saveButton);



    }

    private void loadData(){
        try {
            SharedPreferences sharedPreferences = Tools.getGeneralSharedPref(this);
            time_early.setText(Tools.getformattedTime(sharedPreferences.getString("1", "7:00")));
            check_early.setChecked(sharedPreferences.getBoolean("1Status", false));
            time_breakfast.setText(Tools.getformattedTime(sharedPreferences.getString("2", "8:30")));
            check_breakfast.setChecked(sharedPreferences.getBoolean("2Status", false));
            time_mid.setText(Tools.getformattedTime(sharedPreferences.getString("3", "11:30")));
            check_mid.setChecked(sharedPreferences.getBoolean("3Status", false));
            time_lunch.setText(Tools.getformattedTime(sharedPreferences.getString("4", "13:00")));
            check_lunch.setChecked(sharedPreferences.getBoolean("4Status", false));
            time_evening.setText(Tools.getformattedTime(sharedPreferences.getString("5", "16:00")));
            check_evening.setChecked(sharedPreferences.getBoolean("5Status", false));
            time_late_evening.setText(Tools.getformattedTime(sharedPreferences.getString("6", "18:00")));
            check_late_evening.setChecked(sharedPreferences.getBoolean("6Status", false));
            time_dinner.setText(Tools.getformattedTime(sharedPreferences.getString("7", "20:00")));
            check_dinner.setChecked(sharedPreferences.getBoolean("7Status", false));
            time_post.setText(Tools.getformattedTime(sharedPreferences.getString("8", "22:00")));
            check_post.setChecked(sharedPreferences.getBoolean("8Status", false));

            for (int i = 0; i < 8; i++) {
                checkList[i] = sharedPreferences.getBoolean((i + 1) + "Status", false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


