package com.quanutrition.app.diet;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.quanutrition.app.R;
import com.quanutrition.app.Utils.NetworkManager;
import com.quanutrition.app.Utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DietPlanFragment extends Fragment implements View.OnClickListener{
  
    private OnFragmentInteractionListener mListener;
    View rootView;
    RecyclerView recyclerView;
    ArrayList<MealModel> data;
    MealAdapter adapter;
    LinearLayout mon,tue,wed,thu,fri,sat,sun;
    TextView monTxt,tueTxt,wedTxt,thuTxt,friTxt,satTxt,sunTxt;
    ArrayList<MealModel> monData,tueData,wedData,thuData,friData,satData,sunData;
    int[] days = {R.id.mon,R.id.tue,R.id.wed,R.id.thu,R.id.fri,R.id.sat,R.id.sun};
    int[] daysText = {R.id.monTxt,R.id.tueTxt,R.id.wedTxt,R.id.thuTxt,R.id.friTxt,R.id.satTxt,R.id.sunTxt};
    RelativeLayout noData;
    String dietNote = "";
    int lastId;

    public DietPlanFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_diet_plan, container, false);

        recyclerView = rootView.findViewById(R.id.re);
        mon = rootView.findViewById(R.id.mon);
        tue = rootView.findViewById(R.id.tue);
        wed = rootView.findViewById(R.id.wed);
        thu = rootView.findViewById(R.id.thu);
        fri = rootView.findViewById(R.id.fri);
        sat = rootView.findViewById(R.id.sat);
        sun = rootView.findViewById(R.id.sun);
        monTxt = rootView.findViewById(R.id.monTxt);
        tueTxt = rootView.findViewById(R.id.tueTxt);
        wedTxt = rootView.findViewById(R.id.wedTxt);
        thuTxt = rootView.findViewById(R.id.thuTxt);
        friTxt = rootView.findViewById(R.id.friTxt);
        satTxt = rootView.findViewById(R.id.satTxt);
        sunTxt = rootView.findViewById(R.id.sunTxt);
        noData = rootView.findViewById(R.id.noData);

        mon.setOnClickListener(this);
        tue.setOnClickListener(this);
        wed.setOnClickListener(this);
        thu.setOnClickListener(this);
        fri.setOnClickListener(this);
        sat.setOnClickListener(this);
        sun.setOnClickListener(this);

        data = new ArrayList<>();
        monData = new ArrayList<>();
        tueData = new ArrayList<>();
        wedData = new ArrayList<>();
        thuData = new ArrayList<>();
        friData = new ArrayList<>();
        satData = new ArrayList<>();
        sunData = new ArrayList<>();

        adapter = new MealAdapter(data,getActivity());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);



        lastId = daysText[Tools.getDayNumberToday()];
        onClick(rootView.findViewById(days[Tools.getDayNumberToday()]));


        fetchData();

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

    @Override
    public void onClick(View view) {
        int id = view.getId();
        ((TextView)rootView.findViewById(lastId)).setTextColor(getResources().getColor(R.color.colorPrimary));
        ((TextView)rootView.findViewById(lastId)).setBackground(null);
        if(id == R.id.mon){
            lastId = R.id.monTxt;
            monTxt.setTextColor(Color.parseColor("#FFFFFF"));
            monTxt.setBackgroundResource(R.drawable.circle_yellow);
            MealAdapter adapter = new MealAdapter(monData, getActivity());
            recyclerView.setAdapter(adapter);
            if(monData.size()==0){
                noData.setVisibility(View.VISIBLE);
            }else{
                noData.setVisibility(View.GONE);
            }

        }else if(id == R.id.tue){
            lastId = R.id.tueTxt;
            tueTxt.setTextColor(Color.parseColor("#FFFFFF"));
            tueTxt.setBackgroundResource(R.drawable.circle_yellow);
            MealAdapter adapter = new MealAdapter(tueData,getActivity());
            recyclerView.setAdapter(adapter);
            if(tueData.size()==0){
                noData.setVisibility(View.VISIBLE);
            }else{
                noData.setVisibility(View.GONE);
            }
        }else if(id == R.id.wed){
            lastId = R.id.wedTxt;
            wedTxt.setTextColor(Color.parseColor("#FFFFFF"));
            wedTxt.setBackgroundResource(R.drawable.circle_yellow);
            MealAdapter adapter = new MealAdapter(wedData,getActivity());
            recyclerView.setAdapter(adapter);
            if(wedData.size()==0){
                noData.setVisibility(View.VISIBLE);
            }else{
                noData.setVisibility(View.GONE);
            }
        }
        else if(id == R.id.thu){
            lastId = R.id.thuTxt;
            thuTxt.setTextColor(Color.parseColor("#FFFFFF"));
            thuTxt.setBackgroundResource(R.drawable.circle_yellow);
            MealAdapter adapter = new MealAdapter(thuData,getActivity());
            recyclerView.setAdapter(adapter);
            if(thuData.size()==0){
                noData.setVisibility(View.VISIBLE);
            }else{
                noData.setVisibility(View.GONE);
            }
        }
        else if(id == R.id.fri){
            lastId = R.id.friTxt;
            friTxt.setTextColor(Color.parseColor("#FFFFFF"));
            friTxt.setBackgroundResource(R.drawable.circle_yellow);
            MealAdapter adapter = new MealAdapter(friData,getActivity());
            recyclerView.setAdapter(adapter);
            if(friData.size()==0){
                noData.setVisibility(View.VISIBLE);
            }else{
                noData.setVisibility(View.GONE);
            }
        }
        else if(id == R.id.sat){
            lastId = R.id.satTxt;
            satTxt.setTextColor(Color.parseColor("#FFFFFF"));
            satTxt.setBackgroundResource(R.drawable.circle_yellow);
            MealAdapter adapter = new MealAdapter(satData,getActivity());
            recyclerView.setAdapter(adapter);
            if(satData.size()==0){
                noData.setVisibility(View.VISIBLE);
            }else{
                noData.setVisibility(View.GONE);
            }
        }else if(id == R.id.sun){
            lastId = R.id.sunTxt;
            sunTxt.setTextColor(Color.parseColor("#FFFFFF"));
            sunTxt.setBackgroundResource(R.drawable.circle_yellow);
            MealAdapter adapter = new MealAdapter(sunData,getActivity());
            recyclerView.setAdapter(adapter);
            if(sunData.size()==0){
                noData.setVisibility(View.VISIBLE);
            }else{
                noData.setVisibility(View.GONE);
            }
        }

        recyclerView.startAnimation(AnimationUtils.loadAnimation(getActivity(),android.R.anim.fade_in));
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    boolean cal_flag,time_flag,macro_flag;
    void fetchData() {
        final AlertDialog ad = Tools.getDialog("Fetching...", getActivity());
        ad.show();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ad.dismiss();
                try {
                    Log.d("Response", response);
                    JSONObject result = new JSONObject(response);
                    if (result.getInt("res") == 1) {
                        rootView.findViewById(R.id.noData).setVisibility(View.GONE);
                        JSONObject data = result.getJSONObject("data");
                        JSONObject foodData = data.getJSONObject("data");
                        dietNote = data.getString("notes");

                        cal_flag = data.getBoolean("cal_status");
                        time_flag = data.getBoolean("time_status");
                        macro_flag = data.getBoolean("macro_status");

                        getData(monData,foodData.getJSONObject("Monday").getJSONArray("diet"));
                        getData(tueData,foodData.getJSONObject("Tuesday").getJSONArray("diet"));
                        getData(wedData,foodData.getJSONObject("Wednesday").getJSONArray("diet"));
                        getData(thuData,foodData.getJSONObject("Thursday").getJSONArray("diet"));
                        getData(friData,foodData.getJSONObject("Friday").getJSONArray("diet"));
                        getData(satData,foodData.getJSONObject("Saturday").getJSONArray("diet"));
                        getData(sunData,foodData.getJSONObject("Sunday").getJSONArray("diet"));


                        lastId = daysText[Tools.getDayNumberToday()];
                        onClick(rootView.findViewById(days[Tools.getDayNumberToday()]));
                        /*

                        adapter.notifyDataSetChanged();

                        if(mealList.size()==0){
                            rootView.findViewById(R.id.noData).setVisibility(View.VISIBLE);
                        }*/
                    }else{
                        rootView.findViewById(R.id.noData).setVisibility(View.VISIBLE);
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
                Tools.initNetworkErrorToast(getActivity());
                Log.d("Error", error.toString());
                Log.d("myTag", "I am here");
            }
        };
        Map<String, String> params = new HashMap<>();
//        params.put("date", date);

        NetworkManager.getInstance(getActivity()).sendPostRequestWithHeader(Urls.FETCH_PARTICULAR_DIET, params, listener, errorListener, getActivity());
    }

    void getData(ArrayList<MealModel> mealList,JSONArray foodData)throws JSONException {
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
            meal.setRecipe_flag(false);
            if(foodList.size()>0)
                mealList.add(meal);
        }
    }

}
