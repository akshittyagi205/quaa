package com.quanutrition.app.general;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.quanutrition.app.R;
import com.quanutrition.app.Utils.NetworkManager;
import com.quanutrition.app.Utils.Tools;
import com.quanutrition.app.firebaseUtils.FirebaseUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class DailyDiaryActivity extends AppCompatActivity implements View.OnClickListener{

    CheckBox check_early,check_breakfast,check_mid,check_lunch,check_evening,check_dinner,check_post;
    TextView text_early,text_breakfast,text_mid,text_lunch,text_evening,text_dinner,text_post;
    TextView time_early,time_breakfast,time_mid,time_lunch,time_evening,time_dinner,time_post;
    EditText edit_early,edit_breakfast,edit_mid,edit_lunch,edit_evening,edit_dinner,edit_post;
    LinearLayout layPD,layD,layE,layL,layMM,layEM,layB;
    TextView saveDiary,stepsText,calText,waterText,toolbar_text;
    String dateTxt,stepsString,calString,waterString;
    int flag = 0;
    String earlyTimeString,breakfastTimeString,midTimeString,lunchTimeString,eveningTimeString,dinnerTimeString,postTimeString;
    String earlytextString,breakfastTextString,midTextString,lunchTextString,eveningTextString,dinnerTextString,postTextString;
    JSONArray diaryArray = new JSONArray();

    int checkedNo=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_diary);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_diary));
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        setViews();
        setUpTrackers(Tools.getFormattedDateToday());
        setUpCalender();

        requestFetch();
        setListeners();

    }
    
    private void setListeners(){
        time_early.setOnClickListener(this);
        time_breakfast.setOnClickListener(this);
        time_lunch.setOnClickListener(this);
        time_mid.setOnClickListener(this);
        time_evening.setOnClickListener(this);
        time_dinner.setOnClickListener(this);
        time_post.setOnClickListener(this);
        saveDiary.setOnClickListener(this);

        toolbar_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (findViewById(R.id.calendarView).getVisibility() == View.GONE) {
                    findViewById(R.id.calendarView).setVisibility(View.VISIBLE);

                }

                else
                    findViewById(R.id.calendarView).setVisibility(View.GONE);

            }
        });

        check_early.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(check_early.isChecked())
                {
                    text_early.setTextColor(getResources().getColor(R.color.colorAccent));
                    time_early.setTextColor(getResources().getColor(R.color.colorAccent));
                    edit_early.setVisibility(View.VISIBLE);
                    checkedNo++;
                    if(flag ==0)
                    showTimePicker(time_early);
                }
                else
                {
                    text_early.setTextColor(getResources().getColor(R.color.grey_40));
                    time_early.setTextColor(getResources().getColor(R.color.grey_40));
                    edit_early.setVisibility(View.GONE);
                    checkedNo--;
                }
            }
        });


        check_breakfast.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(check_breakfast.isChecked())
                {

                    text_breakfast.setTextColor(getResources().getColor(R.color.colorAccent));
                    time_breakfast.setTextColor(getResources().getColor(R.color.colorAccent));
                    edit_breakfast.setVisibility(View.VISIBLE);
                    checkedNo++;
                    if(flag ==0)
                    showTimePicker(time_breakfast);
                }
                else
                {
                    text_breakfast.setTextColor(getResources().getColor(R.color.grey_40));
                    time_breakfast.setTextColor(getResources().getColor(R.color.grey_40));
                    edit_breakfast.setVisibility(View.GONE);
                    checkedNo--;
                    edit_breakfast.setText("");
                }
            }
        });



        check_mid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(check_mid.isChecked())
                {
                    edit_mid.setVisibility(View.VISIBLE);

                    text_mid.setTextColor(getResources().getColor(R.color.colorAccent));

                    time_mid.setTextColor(getResources().getColor(R.color.colorAccent));

                    checkedNo++;
                    if(flag ==0)
                    showTimePicker(time_mid);
                }

                else
                {
                    edit_mid.setVisibility(View.GONE);
                    text_mid.setTextColor(getResources().getColor(R.color.grey_40));

                    time_mid.setTextColor(getResources().getColor(R.color.grey_40));
                    checkedNo--;
                    edit_mid.setText("");
                }
            }
        });



        check_lunch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(check_lunch.isChecked())
                {
                    edit_lunch.setVisibility(View.VISIBLE);
                    text_lunch.setTextColor(getResources().getColor(R.color.colorAccent));

                    time_lunch.setTextColor(getResources().getColor(R.color.colorAccent));

                    checkedNo++;
                    if(flag ==0)
                    showTimePicker(time_lunch);
                }
                else
                {
                    text_lunch.setTextColor(getResources().getColor(R.color.grey_40));

                    time_lunch.setTextColor(getResources().getColor(R.color.grey_40));
                    edit_lunch.setVisibility(View.GONE);
                    checkedNo--;
                    edit_lunch.setText("");
                }
            }
        });


        check_evening.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(check_evening.isChecked())
                {
                    edit_evening.setVisibility(View.VISIBLE);

                    text_evening.setTextColor(getResources().getColor(R.color.colorAccent));

                    time_evening.setTextColor(getResources().getColor(R.color.colorAccent));

                    checkedNo++;
                    if(flag ==0)
                    showTimePicker(time_evening);

                }
                else
                {
                    text_evening.setTextColor(getResources().getColor(R.color.grey_40));

                    time_evening.setTextColor(getResources().getColor(R.color.grey_40));
                    edit_evening.setVisibility(View.GONE);
                    checkedNo--;

                    edit_evening.setText("");
                }
            }
        });


        check_dinner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(check_dinner.isChecked())
                {
                    edit_dinner.setVisibility(View.VISIBLE);
                    text_dinner.setTextColor(getResources().getColor(R.color.colorAccent));

                    time_dinner.setTextColor(getResources().getColor(R.color.colorAccent));

                    checkedNo++;
                    if(flag ==0)
                    showTimePicker(time_dinner);

                }
                else
                {
                    text_dinner.setTextColor(getResources().getColor(R.color.grey_40));

                    time_dinner.setTextColor(getResources().getColor(R.color.grey_40));
                    edit_dinner.setVisibility(View.GONE);
                    checkedNo--;
                    edit_dinner.setText("");
                }
            }
        });

        check_post.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(check_post.isChecked())
                {
                    edit_post.setVisibility(View.VISIBLE);
                    text_post.setTextColor(getResources().getColor(R.color.colorAccent));

                    time_post.setTextColor(getResources().getColor(R.color.colorAccent));
                    checkedNo++;
                    if(flag ==0)
                    showTimePicker(time_post);


                }
                else
                {
                    text_post.setTextColor(getResources().getColor(R.color.grey_40));

                    time_post.setTextColor(getResources().getColor(R.color.grey_40));
                    edit_post.setVisibility(View.GONE);
                    edit_post.setText("");
                    checkedNo--;
                }
            }
        });
    }

    private void setViews() {

        check_early = findViewById(R.id.checkboxEarly);
        check_breakfast = findViewById(R.id.checkboxBreak);
        check_mid = findViewById(R.id.checkboxMidMorning);
        check_lunch = findViewById(R.id.checkboxLunch);
        check_evening = findViewById(R.id.checkboxEvening);
        check_dinner = findViewById(R.id.checkboxDinner);
        check_post = findViewById(R.id.checkboxPostDinner);

        time_early = findViewById(R.id.EMtimetxt);
        time_breakfast = findViewById(R.id.BtimeTxt);
        time_mid = findViewById(R.id.MMtimeTxt);
        time_lunch = findViewById(R.id.LtimeTxt);
        time_evening = findViewById(R.id.EtimeTxt);
        time_dinner = findViewById(R.id.DtimeTxt);
        time_post = findViewById(R.id.PDtimeTxt);

        text_early = findViewById(R.id.earlyMorningTxt);
        text_breakfast = findViewById(R.id.breakfastTxt);
        text_mid = findViewById(R.id.MidMorningText);
        text_lunch = findViewById(R.id.lunchTxt);
        text_evening = findViewById(R.id.EveningTxt);
        text_dinner = findViewById(R.id.DinnerTxt);
        text_post = findViewById(R.id.postDinnerTxt);

        edit_early = findViewById(R.id.meal_early);
        edit_breakfast = findViewById(R.id.meal_breakfast);
        edit_mid = findViewById(R.id.meal_midMor);
        edit_lunch = findViewById(R.id.meal_lunch);
        edit_evening = findViewById(R.id.meal_evening);
        edit_dinner = findViewById(R.id.meal_dinner);
        edit_post = findViewById(R.id.meal_postDinner);

        layB = findViewById(R.id.layB);
        layEM = findViewById(R.id.layEM);
        layMM = findViewById(R.id.layMM);
        layL = findViewById(R.id.layL);
        layE = findViewById(R.id.layE);
        layD = findViewById(R.id.layD);
        layPD = findViewById(R.id.layPD);

        saveDiary = findViewById(R.id.save_diary);
        stepsText = findViewById(R.id.steps_diary);
        calText = findViewById(R.id.cal_diary);
        waterText = findViewById(R.id.waterText);

        toolbar_text = findViewById(R.id.toolbar_text_diary);
        dateTxt = Tools.getFormattedDateToday();
        toolbar_text.setText("Diet Diary (" + dateTxt + ")");
        findViewById(R.id.calendarView).setVisibility(View.GONE);
    }

    void showTimePicker(final TextView view){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(DailyDiaryActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String time = Tools.getFormattedTimeAMPM(selectedHour,selectedMinute);
                view.setText(time);
            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.save_diary)
        {
            Log.d("manya","lol");
            int flag=0;
            try {
                if (check_early.isChecked()&&Tools.validateNormalText(edit_early)) {
                    flag++;
                }
                    JSONObject diaryObject = new JSONObject();
                    earlytextString = Tools.getText(edit_early);
                    earlyTimeString = time_early.getText().toString()+"";

                    diaryObject.put("mealname", "Early Morning");
                    diaryObject.put("mealtime", earlyTimeString);
                    diaryObject.put("data", earlytextString);
                    diaryArray.put(diaryObject);


                if (check_breakfast.isChecked()&&Tools.validateNormalText(edit_breakfast)) {
                    flag++;
                }
                    JSONObject diaryObject1 = new JSONObject();
                    breakfastTextString = Tools.getText(edit_breakfast);
                    breakfastTimeString = time_breakfast.getText().toString()+"";
                    diaryObject1.put("mealname", "Breakfast");
                    diaryObject1.put("mealtime", breakfastTimeString);
                    diaryObject1.put("data", breakfastTextString);
                    diaryArray.put(diaryObject1);

                if (check_lunch.isChecked()&&Tools.validateNormalText(edit_lunch)) {
                    flag++;
                }
                    JSONObject diaryObject2 = new JSONObject();
                    lunchTextString = Tools.getText(edit_lunch);
                    lunchTimeString = time_lunch.getText().toString()+"";
                    diaryObject2.put("mealname", "Lunch");
                    diaryObject2.put("mealtime", lunchTimeString);
                    diaryObject2.put("data", lunchTextString);
                    diaryArray.put(diaryObject2);

                if (check_mid.isChecked()&&Tools.validateNormalText(edit_mid)) {
                    flag++;
                }
                    JSONObject diaryObject3 = new JSONObject();
                    midTextString = Tools.getText(edit_mid);
                    midTimeString = time_mid.getText().toString()+"";
                    diaryObject3.put("mealname", "Mid Morning");
                    diaryObject3.put("mealtime", midTimeString);
                    diaryObject3.put("data", midTextString);
                    diaryArray.put(diaryObject3);


                if (check_evening.isChecked()&&Tools.validateNormalText(edit_evening)) {
                    flag++;
                }
                    JSONObject diaryObject4 = new JSONObject();
                    eveningTextString = Tools.getText(edit_evening);
                    eveningTimeString = time_evening.getText().toString()+"";
                    diaryObject4.put("mealname", "Evening");
                    diaryObject4.put("mealtime", eveningTimeString);
                    diaryObject4.put("data", eveningTextString);
                    diaryArray.put(diaryObject4);

                if (check_dinner.isChecked()&&Tools.validateNormalText(edit_dinner)) {
                    flag++;
                }
                    JSONObject diaryObject5 = new JSONObject();
                    dinnerTextString = Tools.getText(edit_dinner);
                    dinnerTimeString = time_dinner.getText().toString()+"";
                    diaryObject5.put("mealname", "Dinner");
                    diaryObject5.put("mealtime", dinnerTimeString);
                    diaryObject5.put("data", dinnerTextString);
                    diaryArray.put(diaryObject5);

                if (check_post.isChecked()&&Tools.validateNormalText(edit_post)) {
                    flag++;
                }
                    JSONObject diaryObject6 = new JSONObject();
                    postTextString = Tools.getText(edit_post);
                    postTimeString = time_post.getText().toString()+"";
                    diaryObject6.put("mealname", "Post Dinner");
                    diaryObject6.put("mealtime", postTimeString);
                    diaryObject6.put("data", postTextString);
                    diaryArray.put(diaryObject6);

                    if(flag>0) {
                        requestSave();

                        JSONObject diaryObject9 = new JSONObject();
                        diaryObject9.put("mealname", "Late Evening");
                        diaryObject9.put("mealtime", "");
                        diaryObject9.put("data", "");
                        diaryArray.put(diaryObject9);
                    }
                    else
                        Tools.initCustomToast(this,"Please fill at least one item in the diary!");
            }
            catch (JSONException e)
            {
                Log.d("error",e+"");
            }


        }
        else {
            showTimePicker((TextView) view);
        }
    }

    void setUpTrackers(String date){


        /*GoogleFitUtils googleFitUtils = new GoogleFitUtils(DailyDiaryActivity.this, new GoogleFitUtils.OnDataReady() {
            @Override
            public void onStepsReady(String steps) {
                stepsText.setText("You have taken " +steps+" Steps Today!");
                stepsString = steps;

            }

            @Override
            public void onCaloriesReady(String totalCal, String walking, String running, String other) {
                calText.setText("You have burnt "+totalCal+" KCal. today!");
                calString = totalCal;

            }

            @Override
            public void onWeeklyDataReady(ArrayList<String> days, ArrayList<String> steps) {

            }
        });
        googleFitUtils.init();*/
        FirebaseUtils firebaseUtils = new FirebaseUtils(this);
        firebaseUtils.getDataAnyDay(new FirebaseUtils.OnDailyStatsReady() {
            @Override
            public void onDataReady(String steps, String cal, String water) {
                stepsText.setText("You have taken " +steps+" Steps");
                calText.setText("You have burnt "+cal+" KCal.");
                waterText.setText("You drank "+water+" Glasses of water");
                stepsString = steps;
                calString = cal;
                waterString = water;
                Log.d("Water",water);
            }
        },date);
    }


    void setUpCalender() {
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
//        endDate.add(Calendar.MONTH, 1);

        Calendar setDate = Calendar.getInstance();
        setDate.add(Calendar.DATE, -6);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(7)
                .defaultSelectedDate(setDate)
                .build();


        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                //do something
                dateTxt = Tools.getFormattedDate(date.getTimeInMillis());

                toolbar_text.setText("Diet Diary (" + dateTxt + ")");
                findViewById(R.id.calendarView).setVisibility(View.GONE);
                requestFetch();
                setUpTrackers(dateTxt);

            }

            @Override
            public void onCalendarScroll(HorizontalCalendarView calendarView, int dx, int dy) {

            }
        });
    }


    void requestSave(){
        final AlertDialog ad = Tools.getDialog("Requesting...",this);
        ad.show();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ad.dismiss();


                Log.d("Response",response);
                Log.d("myTag","I am here");

                try {
                    JSONObject result = new JSONObject(response);
                    Tools.initCustomToast(DailyDiaryActivity.this,result.getString("msg"));
                    if(result.getInt("res")==1){
                        finish();
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
                Tools.initNetworkErrorToast(DailyDiaryActivity.this);
                Log.d("Error",error.toString());
                Log.d("myTag","I am here");
            }
        };
        Map<String, String> params = new HashMap<>();
        params.put("date",dateTxt);
        params.put("diary",diaryArray+"");
        params.put("steps",stepsString);
        params.put("cal",calString);
        params.put("water",waterString);
        Log.d("Water",waterString);
        Log.d("diary",dateTxt);

        // params.put("cuisine",);
        // params.put("gender",genderString);

        NetworkManager.getInstance(this).sendPostRequestWithHeader(Urls.save_diet_diary,params,listener,errorListener,this);

    }


    void requestFetch(){

        final AlertDialog ad = Tools.getDialog("Requesting ..",this);
        ad.show();
        clearData();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ad.dismiss();

                try {
                    JSONObject result = new JSONObject(response);
                    Log.d("data",response);
                    int res = result.getInt("res");
                    String msg = result.getString("msg");
                    if(res==1) {
                        JSONObject diaryObject = result.getJSONObject("response");
                        /*stepsString = diaryObject.getString("steps");
                        calString = diaryObject.getString("cal");
                        waterString = diaryObject.getString("water");*/

                        JSONArray diaryDataArray = diaryObject.getJSONArray("diary");
                        for (int i = 0; i < diaryDataArray.length(); i++) {
                            JSONObject diaryDataObject = diaryDataArray.getJSONObject(i);
                            String meal = diaryDataObject.getString("mealname");
                            if(!diaryDataObject.getString("data").isEmpty())
                            preFillData(diaryDataObject.getString("mealname"), diaryDataObject.getString("mealtime"), diaryDataObject.getString("data"));
                        }

                        setUpTrackers(dateTxt);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"You haven't saved anything for this date ", Toast.LENGTH_LONG).show();
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
                Tools.initNetworkErrorToast(DailyDiaryActivity.this);
                Log.d("Error",error.toString());
                Log.d("myTag","I am here");
            }
        };
        Map<String, String> params = new HashMap<>();
        params.put("date",dateTxt);
        Log.d("datefetch",dateTxt);

        // params.put("cuisine",);
        // params.put("gender",genderString);

        NetworkManager.getInstance(this).sendPostRequestWithHeader(Urls.get_diet_diary,params,listener,errorListener,this);

    }

    public void preFillData(String mealname, String time, String text )
    {
        flag =1;
        if ( mealname.equalsIgnoreCase("Early Morning"))
        {
            time_early.setText(time);
            check_early.setChecked(true);
            edit_early.setText(text);
        }
         if ( mealname.equalsIgnoreCase("Breakfast"))
        {
            time_breakfast.setText(time);
            check_breakfast.setChecked(true);
            edit_breakfast.setText(text);
        }

        if ( mealname.equalsIgnoreCase("Mid Morning"))
        {
            time_mid.setText(time);
            check_mid.setChecked(true);
            edit_mid.setText(text);
        }

        if ( mealname.equalsIgnoreCase("Lunch"))
        {
            time_lunch.setText(time);
            check_lunch.setChecked(true);
            edit_lunch.setText(text);
        }
        if ( mealname.equalsIgnoreCase("Evening"))
        {
            time_evening.setText(time);
            check_evening.setChecked(true);
            edit_evening.setText(text);
        }
        if ( mealname.equalsIgnoreCase("Dinner"))
        {
            time_dinner.setText(time);
            check_dinner.setChecked(true);
            edit_dinner.setText(text);
        }
        if ( mealname.equalsIgnoreCase("Post Dinner"))
        {
            time_post.setText(time);
            check_post.setChecked(true);
            edit_post.setText(text);
        }


        flag=0;

    }

    public void clearData()
    {
        check_early.setChecked(false);
        check_breakfast.setChecked(false);
        check_mid.setChecked(false);
        check_lunch.setChecked(false);
        check_evening.setChecked(false);
        check_dinner.setChecked(false);
        check_post.setChecked(false);


        edit_early.setText("");
        edit_breakfast.setText("");
        edit_mid.setText("");
        edit_lunch.setText("");
        edit_evening.setText("");
        edit_dinner.setText("");
        edit_post.setText("");

        time_early.setText("");
        time_breakfast.setText("");
        time_mid.setText("");
        time_lunch.setText("");
        time_evening.setText("");
        time_dinner.setText("");
        time_post.setText("");

        flag =0;


    }
}

