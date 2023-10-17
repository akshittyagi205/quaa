package com.quanutrition.app.diet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.quanutrition.app.R;
import com.quanutrition.app.Utils.NetworkManager;
import com.quanutrition.app.Utils.Tools;
import com.quanutrition.app.chat.ChatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class DietPlanViewActivity extends AppCompatActivity {

    ArrayList<MealModel> mealList;
    MealAdapter adapter;
    RecyclerView diet_main_re;
    TextView toolbar_text;
    String dateTxt;
    TextView basicBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_plan_view);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar_text = findViewById(R.id.toolbar_text);
        dateTxt = Tools.getFormattedDateToday();
        toolbar_text.setText("Diet Plan (" + dateTxt + ")");
        findViewById(R.id.calendarView).setVisibility(View.GONE);
        basicBtn = findViewById(R.id.basicBtn);
        findViewById(R.id.noData).setVisibility(View.GONE);


        basicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DietPlanViewActivity.this, ChatActivity.class));
            }
        });


        toolbar_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (findViewById(R.id.calendarView).getVisibility() == View.GONE)
                    findViewById(R.id.calendarView).setVisibility(View.VISIBLE);
                else
                    findViewById(R.id.calendarView).setVisibility(View.GONE);

            }
        });

        setUpCalender();

        fetchData(dateTxt);

        diet_main_re = findViewById(R.id.diet_main_re);
        /*FoodOptionsModel optionsModel = new FoodOptionsModel("1", "Dosa (Plain)", "120", "3 dosa", "You can fry the dosa in olive oil than regular oil since it contains comparatively less trans fat", true);
        ArrayList<FoodOptionsModel> optionsList = new ArrayList<>();
        optionsList.add(optionsModel);
        optionsList.add(optionsModel);
        FoodDataModel foodDataModel = new FoodDataModel("1", "Dosa (Plain)", "120", "3 dosa", "You can fry the dosa in olive oil than regular oil since it contains comparatively less trans fat", true, optionsList);
        FoodDataModel foodDataModel1 = new FoodDataModel("1", "Dosa (Plain)", "120", "3 dosa", "You can fry the dosa in olive oil than regular oil since it contains comparatively less trans fat", false, new ArrayList<FoodOptionsModel>());

        ArrayList<FoodDataModel> foodDataList = new ArrayList<>();
        foodDataList.add(foodDataModel);
        foodDataList.add(foodDataModel1);
        MealModel mealModel = new MealModel("1", "Breakfast", "10:00 A.M.", "250", false, false, foodDataList);
        MealModel mealModel1 = new MealModel("2", "Lunch", "10:00 A.M.", "250", true, true, foodDataList);
        MealModel mealModel2 = new MealModel("3", "Lunch", "10:00 A.M.", "250", false, true, foodDataList);*/
        mealList = new ArrayList<>();
//        mealList.add(mealModel);
//        mealList.add(mealModel1);
//        mealList.add(mealModel2);
        adapter = new MealAdapter(mealList, this,false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        diet_main_re.setLayoutManager(layoutManager);
        diet_main_re.setAdapter(adapter);
    }

    String dietNote = "";
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Log.d("Home Clicked", "I am Clicked!");
            finish();
        }else if(item.getItemId() == R.id.notes){
            if(dietNote.isEmpty()||dietNote.trim().equalsIgnoreCase("No")){
                Tools.initCustomToast(this,"No Diet notes found for this date!");
            }else {
                Intent intent = new Intent(DietPlanViewActivity.this, DietNotesActivity.class);
                intent.putExtra("note",dietNote);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    void setUpCalender() {
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

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
                toolbar_text.setText("Diet Plan (" + dateTxt + ")");
                findViewById(R.id.calendarView).setVisibility(View.GONE);
                fetchData(dateTxt);
            }

            @Override
            public void onCalendarScroll(HorizontalCalendarView calendarView, int dx, int dy) {

            }
        });
    }

    void fetchData(final String date) {
        final AlertDialog ad = Tools.getDialog("Fetching...", this);
        ad.show();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ad.dismiss();
                try {
                    Log.d("Response", response);
                    JSONObject result = new JSONObject(response);
                    if (result.getInt("res") == 1) {
                        findViewById(R.id.noData).setVisibility(View.GONE);
                        JSONObject data = result.getJSONObject("data");
                        JSONArray foodData = data.getJSONArray("data");
                        dietNote = data.getString("notes");
                        boolean cal_flag,time_flag,macro_flag;
                        cal_flag = data.getBoolean("cal_status");
                        time_flag = data.getBoolean("time_status");
                        macro_flag = data.getBoolean("macro_status");

                        mealList.clear();
                        for(int i=0;i<foodData.length();i++){
                            JSONObject ob = foodData.getJSONObject(i);
                            JSONObject macro = ob.getJSONObject("mealmacro");
                            JSONArray foodArray = ob.getJSONArray("fooddata");
                            ArrayList<FoodDataModel> foodList = new ArrayList<>();
                            for(int j=0;j<foodArray.length();j++){
                                JSONObject food = foodArray.getJSONObject(j);
                                JSONObject foodMacro = food.getJSONObject("macro");
                                JSONArray options = food.getJSONArray("options");
                                ArrayList<FoodOptionsModel> optionsList = new ArrayList<>();
                                for(int k=0;k<options.length();k++){
                                    JSONObject optionFood = options.getJSONObject(k);
                                    JSONObject optionMacro = optionFood.getJSONObject("macro");
                                    FoodOptionsModel optionsModel = new FoodOptionsModel(optionFood.getString("id"),optionFood.getString("name"),optionMacro.getString("cal"),optionFood.getString("quantity")+" "+optionFood.getString("measure"),optionFood.optString("note",""),cal_flag);
                                    optionsList.add(optionsModel);
                                }
                                FoodDataModel foodDataModel = new FoodDataModel(food.getString("id"),food.getString("name"),foodMacro.getString("cal"),food.getString("quantity")+" "+food.getString("measure"),food.getString("note"),cal_flag,optionsList);
                                foodList.add(foodDataModel);
                            }
                            MealModel meal = new MealModel(i+"",ob.getString("mealname"),ob.getString("mealtime"),macro.getString("cal"),time_flag,cal_flag,foodList);
                            meal.setMealNotes(ob.getString("mealnote"));
                            meal.setMacro_flag(macro_flag);
                            meal.setMacros(macro.toString());
                            meal.setRecipe_flag(data.getBoolean("recipe"));
                            if(foodList.size()>0)
                            mealList.add(meal);
                        }
                        adapter.notifyDataSetChanged();

                        if(mealList.size()==0){
                            findViewById(R.id.noData).setVisibility(View.VISIBLE);
                        }
                    }else{
                        findViewById(R.id.noData).setVisibility(View.VISIBLE);
//                        image.playAnimation();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
//                Log.d("Response",response);
                Log.d("myTag", "I am here");
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ad.dismiss();
                Tools.initNetworkErrorToast(DietPlanViewActivity.this);
                Log.d("Error", error.toString());
                Log.d("myTag", "I am here");
            }
        };
        Map<String, String> params = new HashMap<>();
        params.put("date", date);

        NetworkManager.getInstance(this).sendPostRequestWithHeader(Urls.FETCH_PARTICULAR_DIET, params, listener, errorListener, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.diet_notes_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


}