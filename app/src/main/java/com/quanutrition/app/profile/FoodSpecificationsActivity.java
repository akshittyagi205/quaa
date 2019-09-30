package com.quanutrition.app.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.chip.Chip;
import com.quanutrition.app.R;
import com.quanutrition.app.Utils.NetworkManager;
import com.quanutrition.app.Utils.Tools;
import com.quanutrition.app.selectiondialogs.DialogUtils;
import com.quanutrition.app.selectiondialogs.MultipleSelectionModel;
import com.quanutrition.app.selectiondialogs.SingleSelectionModel;
import com.xiaofeng.flowlayoutmanager.Alignment;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FoodSpecificationsActivity extends AppCompatActivity implements View.OnClickListener{

    RecyclerView nv_exempt_re,fast_re,cuisines_re;
    ArrayList<ChipsModel> nv_exempt,fast,cuisines;
    ArrayList<MultipleSelectionModel> nv_exempt_base,fast_base,cuisinesBase;
    ChipsAdapter nv_exempt_Adapter,fastAdapter,cuisinesAdapter;
    EditText foodPref,nonVegPref_editText,culture;
    TextView addCuisine,addNonVegExempt,addFast,foodSavePref;
    String foodSpecs_string;
    LinearLayout layNVExemption;
    ArrayList<MultipleSelectionModel> nonVegList;
    ArrayList<MultipleSelectionModel> nonVegSelected = new ArrayList<>();
    ArrayList<SingleSelectionModel> culture_list = new ArrayList<>();
    LinearLayout nonVegPrefLayout;
//    JSONArray allergiesArray = new JSONArray();
    JSONArray nv_exempt_Array = new JSONArray();
    JSONArray fastArray = new JSONArray();
    JSONArray foodPefArray = new JSONArray();
    JSONArray cuisineArray = new JSONArray();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_specifications);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
//        Tools.setSystemBarColorCustom(this, R.color.colorAccent);
        getSupportActionBar().setTitle("Food Specifications");

//        allegies_re = findViewById(R.id.allergies_re);
        fast_re = findViewById(R.id.fast_re);
        nv_exempt_re = findViewById(R.id.nv_exempt_re);
        cuisines_re = findViewById(R.id.cuisine_re);
        foodPref = findViewById(R.id.foodPref);
        addCuisine = findViewById(R.id.addCuisine);
        culture = findViewById(R.id.culture);
        layNVExemption = findViewById(R.id.layNVExemption);
//        addAllergies = findViewById(R.id.addAllergies);
        addNonVegExempt = findViewById(R.id.addNonVegExempt);
        addFast = findViewById(R.id.addFast);
        foodSavePref = findViewById(R.id.save_food);
        nonVegPref_editText = findViewById(R.id.nonVegPref);
        nonVegPrefLayout = findViewById(R.id.nonVegPrefLayout);

//        allergies = new ArrayList<>();
        nv_exempt = new ArrayList<>();
        fast = new ArrayList<>();
        cuisines = new ArrayList<>();
//        allergiesBase = new ArrayList<>();
        nv_exempt_base = new ArrayList<>();
        fast_base = new ArrayList<>();
        cuisinesBase = new ArrayList<>();

        /*final String[] daysArray = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
        for(int i=0;i<7;i++){
            nv_exempt_base.add(new MultipleSelectionModel(i+"",daysArray[i],false));
        }

        for(int i=0;i<7;i++){
            fast_base.add(new MultipleSelectionModel(i+"",daysArray[i],false));
        }*/

        foodPref.setFocusable(false);
        nonVegPref_editText.setFocusable(false);
        culture.setFocusable(false);
        nonVegPrefLayout.setVisibility(View.GONE);
        layNVExemption.setVisibility(View.GONE);
        nv_exempt_re.setVisibility(View.GONE);

        foodPref.setOnClickListener(this);
        addCuisine.setOnClickListener(this);
        culture.setOnClickListener(this);
//        addAllergies.setOnClickListener(this);
        addFast.setOnClickListener(this);
        addNonVegExempt.setOnClickListener(this);
        foodSavePref.setOnClickListener(this);
        nonVegPref_editText.setOnClickListener(this);

        nonVegList = DialogUtils.getMultipleArrayListWithResource(this, R.array.nonVeg);


        nv_exempt_Adapter = new ChipsAdapter(nv_exempt, this, new ChipsAdapter.OnClickListener() {
            @Override
            public void onClose(View view, int position) {
                for(int i=0;i<nv_exempt_base.size();i++){
                    if(nv_exempt.get(position).getId().equalsIgnoreCase(nv_exempt_base.get(i).getId())){
                        nv_exempt_base.get(i).setSelected(false);
                    }
                }
                nv_exempt.remove(position);
                nv_exempt_Adapter.notifyDataSetChanged();
            }
        });
        RecyclerView.LayoutManager flowLayoutManager = new FlowLayoutManager();
        ((FlowLayoutManager) flowLayoutManager).setAlignment(Alignment.LEFT);
        ((FlowLayoutManager) flowLayoutManager).setAutoMeasureEnabled(true);
        ((FlowLayoutManager) flowLayoutManager).maxItemsPerLine(3);
        nv_exempt_re.setLayoutManager(flowLayoutManager);
        nv_exempt_re.setAdapter(nv_exempt_Adapter);

        fastAdapter = new ChipsAdapter(fast, this, new ChipsAdapter.OnClickListener() {
            @Override
            public void onClose(View view, int position) {
                for(int i=0;i<fast_base.size();i++){
                    if(fast.get(position).getId().equalsIgnoreCase(fast_base.get(i).getId())){
                        fast_base.get(i).setSelected(false);
                    }
                }
                fast.remove(position);
                fastAdapter.notifyDataSetChanged();
            }
        });
        RecyclerView.LayoutManager flowLayoutManager2 = new FlowLayoutManager();
        ((FlowLayoutManager) flowLayoutManager2).setAlignment(Alignment.LEFT);
        ((FlowLayoutManager) flowLayoutManager2).setAutoMeasureEnabled(true);
        ((FlowLayoutManager) flowLayoutManager2).maxItemsPerLine(3);
        fast_re.setLayoutManager(flowLayoutManager2);
        fast_re.setAdapter(fastAdapter);




        cuisinesAdapter = new ChipsAdapter(cuisines, this, new ChipsAdapter.OnClickListener() {
            @Override
            public void onClose(View view, int position) {
                for(int i=0;i<cuisinesBase.size();i++){
                    if(cuisines.get(position).getId().equalsIgnoreCase(cuisinesBase.get(i).getId())){
                        cuisinesBase.get(i).setSelected(false);
                    }
                }
                cuisines.remove(position);
                cuisinesAdapter.notifyDataSetChanged();

            }
        });
        RecyclerView.LayoutManager flowLayoutManager1 = new FlowLayoutManager();
        ((FlowLayoutManager) flowLayoutManager1).setAlignment(Alignment.LEFT);
        ((FlowLayoutManager) flowLayoutManager1).setAutoMeasureEnabled(true);
        ((FlowLayoutManager) flowLayoutManager1).maxItemsPerLine(3);
        cuisines_re.setLayoutManager(flowLayoutManager1);
        cuisines_re.setAdapter(cuisinesAdapter);

        requestFetch();

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id==R.id.foodPref){
            DialogUtils.getSingleSelectionDialog(this,DialogUtils.getSingleArrayListWithResource(this,R.array.foodSpecidata), new DialogUtils.OnSingleItemSelectedListener() {
                @Override
                public void onItemSelected(int position, SingleSelectionModel item) {
                    foodPref.setText(item.getLabel());
                    if(item.getLabel().equalsIgnoreCase("Non Vegetarian")){
                        nonVegPrefLayout.setVisibility(View.VISIBLE);
                        layNVExemption.setVisibility(View.VISIBLE);
                        nv_exempt_re.setVisibility(View.VISIBLE);
                    }else{
                        nonVegPrefLayout.setVisibility(View.GONE);
                        layNVExemption.setVisibility(View.GONE);
                        nv_exempt_re.setVisibility(View.GONE);
                        nonVegSelected.clear();
                        nv_exempt.clear();
                        for(int i=0;i<nv_exempt_base.size();i++){
                            nv_exempt_base.get(i).setSelected(false);
                        }
                        nonVegPref_editText.setText("");
                        for(int i=0;i<nonVegList.size();i++){
                            nonVegList.get(i).setSelected(false);
                        }
                    }
                }
            });
        }
        else if(id==R.id.culture){
            DialogUtils.getSingleSelectionDialog(this,culture_list, new DialogUtils.OnSingleItemSelectedListener() {
                @Override
                public void onItemSelected(int position, SingleSelectionModel item) {
                    culture.setText(item.getLabel());
                }
            });
        }
        else if(id == R.id.addFast){

            DialogUtils.getMultipleSearchDialog(this, fast_base, new DialogUtils.OnMultipleItemsSelected() {
                @Override
                public void onMultipleItemsSelected(ArrayList<MultipleSelectionModel> items) {
                    fast_base = items;
                    fast.clear();
                    for(int i=0;i<fast_base.size();i++){
                        if(fast_base.get(i).isSelected()){
                            fast.add(new ChipsModel(fast_base.get(i).getId(),fast_base.get(i).getLabel()));
                        }
                    }
                    fastAdapter.notifyDataSetChanged();
                }
            });

        } else if(id == R.id.addNonVegExempt){

            DialogUtils.getMultipleSearchDialog(this, nv_exempt_base, new DialogUtils.OnMultipleItemsSelected() {
                @Override
                public void onMultipleItemsSelected(ArrayList<MultipleSelectionModel> items) {
                    nv_exempt_base = items;
                    nv_exempt.clear();
                    for(int i=0;i<nv_exempt_base.size();i++){
                        if(nv_exempt_base.get(i).isSelected()){
                            nv_exempt.add(new ChipsModel(nv_exempt_base.get(i).getId(),nv_exempt_base.get(i).getLabel()));
                        }
                    }
                    nv_exempt_Adapter.notifyDataSetChanged();
                }
            });

        } else if(id == R.id.addCuisine){

            DialogUtils.getMultipleSearchDialog(this, cuisinesBase, new DialogUtils.OnMultipleItemsSelected() {
                @Override
                public void onMultipleItemsSelected(ArrayList<MultipleSelectionModel> items) {
                    cuisinesBase = items;
                    cuisines.clear();
                    for(int i=0;i<cuisinesBase.size();i++){
                        if(cuisinesBase.get(i).isSelected()){
                            cuisines.add(new ChipsModel(cuisinesBase.get(i).getId(),cuisinesBase.get(i).getLabel()));
                        }
                    }
                    cuisinesAdapter.notifyDataSetChanged();
                }
            });
        }
        else if(id== R.id.save_food) {
            foodSpecs_string = foodPref.getText().toString();
            nv_exempt_Array = new JSONArray();
            for (int i = 0; i < nv_exempt.size(); i++) {
                nv_exempt_Array.put(nv_exempt.get(i).getName());
            }

            fastArray = new JSONArray();
            for (int i = 0; i < fast.size(); i++) {
                fastArray.put(fast.get(i).getName());
            }

            cuisineArray = new JSONArray();
            for (int i = 0; i < cuisines.size(); i++) {
                cuisineArray.put(cuisines.get(i).getName());
            }

            foodPefArray = new JSONArray();
            for( int i =0;i< nonVegSelected.size() ; i++)
            {
                foodPefArray.put(nonVegSelected.get(i).getLabel());
            }
            requestSave();
//            startActivity(new Intent(FoodSpecificationsActivity.this,GeneralDataActivity.class));
        }
        else if(id == R.id.nonVegPref)
        {
            DialogUtils.getMultipleSearchDialog(this, nonVegList, new DialogUtils.OnMultipleItemsSelected() {

                @Override
                public void onMultipleItemsSelected(ArrayList<MultipleSelectionModel> items) {
                    String string = "";
                    if (items.size() > 0) {
                       setNvPreftext(nonVegList);
                    }
                }
            });
        }

    }


    void requestSave(){


        JSONObject foodPrefOb = new JSONObject();
        try {
            foodPrefOb.put("type", foodSpecs_string);
            foodPrefOb.put("food",foodPefArray);
            foodPrefOb.put("nonveg_except",nv_exempt_Array);
        }catch (Exception e){
            e.printStackTrace();
        }

        final AlertDialog ad = Tools.getDialog("Requesting ..",this);
        ad.show();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ad.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    int res = object.getInt("res");

                    Tools.initCustomToast(getApplicationContext(),object.getString("msg"));
                    if(res==1) {
//                        startActivity(new Intent(FoodSpecificationsActivity.this,GeneralDataActivity.class));
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Tools.initCustomToast(getApplicationContext(),"Some Error");

                }

                Log.d("Response",response);
                Log.d("myTag","I am here");

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ad.dismiss();
                Tools.initNetworkErrorToast(FoodSpecificationsActivity.this);
                Log.d("Error",error.toString());
                Log.d("myTag","I am here");
            }
        };
        Map<String, String> params = new HashMap<>();
        try {
            params.put("culture", Tools.getText(culture));
            params.put("cuisine", Tools.getStringFromArray(cuisineArray));
            params.put("foodpref", foodPrefOb.toString());
            params.put("fast_days", Tools.getStringFromArray(fastArray));
        }catch (Exception e){
            e.printStackTrace();
        }

        Log.d("cuisine",cuisineArray+"");
        Log.d("fast_days",fastArray+"");
        Log.d("foodPefArray",foodPefArray+"");

        NetworkManager.getInstance(this).sendPostRequestWithHeader(Urls.save_food_info,params,listener,errorListener,this);

    }


    void requestFetch(){
        final AlertDialog ad = Tools.getDialog("Requesting ..",this);
        ad.show();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ad.dismiss();
                Log.d("Response",response);
                try {
                    JSONObject foodObjectOuter = new JSONObject(response);
                    int res = foodObjectOuter.getInt("res");
                    String msg = foodObjectOuter.getString("msg");
                    if(res ==1) {
                        JSONObject foodPrefData = foodObjectOuter.getJSONObject("data");
                        /*if(!foodPrefData.getString("pref").trim().equalsIgnoreCase("N.A"))
                            foodPref.setText(foodPrefData.getString("pref"));
                        else
                            foodPref.setText("");

                        if(foodPrefData.getString("pref").equalsIgnoreCase("Non Vegetarian")){
                            nonVegPrefLayout.setVisibility(View.VISIBLE);
                            layNVExemption.setVisibility(View.VISIBLE);
                            nv_exempt_re.setVisibility(View.VISIBLE);
                        }*/
                        /*JSONArray allergiesArray = foodPrefData.getJSONArray("allergies");
                        for (int i = 0; i < allergiesArray.length(); i++) {
                            allergies.add(new ChipsModel("1", allergiesArray.getString(i)));

                        }
                        allergiesAdapter.notifyDataSetChanged();*/

                        JSONObject foodPrefOb = foodPrefData.getJSONObject("food_pref");
                        foodPref.setText(foodPrefOb.getString("type"));

                        if(foodPrefOb.getString("type").equalsIgnoreCase("Non Vegetarian")){
                            nonVegPrefLayout.setVisibility(View.VISIBLE);
                            layNVExemption.setVisibility(View.VISIBLE);
                            nv_exempt_re.setVisibility(View.VISIBLE);
                        }

                        culture.setText(foodPrefData.getString("culture"));


                        JSONArray cuisineArray = foodPrefData.getJSONArray("cuisine");
                        for (int i = 0; i < cuisineArray.length(); i++) {
                            if(!cuisineArray.getString(i).isEmpty())
                            cuisines.add(new ChipsModel("1", cuisineArray.getString(i)));
                        }
                        cuisinesAdapter.notifyDataSetChanged();

                        JSONArray cultureArray = foodPrefData.getJSONArray("culture_list");
                        culture_list.clear();
                        for(int i=0;i<cultureArray.length();i++){
                            culture_list.add(new SingleSelectionModel("1",cultureArray.getString(i)));
                        }


                        nonVegList.clear();
                        JSONArray ng_base = foodPrefData.getJSONArray("ng_list");
                        for(int i=0;i<ng_base.length();i++){
                            nonVegList.add(new MultipleSelectionModel(i+"",ng_base.getString(i),false));
                        }


                        JSONArray nvPrefArray = foodPrefOb.getJSONArray("food");

                        for (int i = 0; i < nvPrefArray.length(); i++) {
                            for (int j = 0; j < nonVegList.size(); j++) {
                                if (nonVegList.get(j).getLabel().equalsIgnoreCase(nvPrefArray.getString(i))) {
                                    nonVegList.get(j).setSelected(true);
                                }
                            }
                        }
                        setNvPreftext(nonVegList);

                        JSONArray ng_exceptArray = foodPrefOb.getJSONArray("nonveg_except");
                        for (int i = 0; i < ng_exceptArray.length(); i++) {
                            if(!ng_exceptArray.getString(i).isEmpty())
                                nv_exempt.add(new ChipsModel("1", ng_exceptArray.getString(i)));
                        }
                        nv_exempt_Adapter.notifyDataSetChanged();


                        JSONArray fastDaysArray = foodPrefData.getJSONArray("fast_days");
                        for (int i = 0; i < fastDaysArray.length(); i++) {
                            if(!fastDaysArray.getString(i).isEmpty())
                                fast.add(new ChipsModel("1", fastDaysArray.getString(i)));
                        }
                        fastAdapter.notifyDataSetChanged();

                        JSONArray cuisine_base = foodPrefData.getJSONArray("cuisine_list");
                        cuisinesBase.clear();
                        for (int i = 0; i < cuisine_base.length(); i++) {
                            int flag = 0;
                            for (int j = 0; j < cuisines.size(); j++) {
                                if (cuisines.get(j).getName().equalsIgnoreCase(cuisine_base.getString(i))) {
                                    flag++;
                                }
                            }
                            if(!cuisine_base.getString(i).isEmpty()) {
                                if (flag > 0)
                                    cuisinesBase.add(new MultipleSelectionModel(i + "", cuisine_base.getString(i), true));
                                else
                                    cuisinesBase.add(new MultipleSelectionModel(i + "", cuisine_base.getString(i), false));
                            }
                        }


                        /*JSONArray allergies_base = foodPrefData.getJSONArray("allergies_list");
                        allergiesBase.clear();
                        for (int i = 0; i < allergies_base.length(); i++) {
                            int flag = 0;
                            for (int j = 0; j < allergies.size(); j++) {
                                if (allergies.get(j).getName().equalsIgnoreCase(allergies_base.getString(i))) {
                                    flag++;
                                }
                            }
                            if (flag > 0)
                                allergiesBase.add(new MultipleSelectionModel(i + "", allergies_base.getString(i), true));
                            else
                                allergiesBase.add(new MultipleSelectionModel(i + "", allergies_base.getString(i), false));

                        }*/

                        String[] days = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
                        for(int i=0;i<7;i++){

                            int flag1 = 0,flag2 = 0;
                            for (int j = 0; j < fast.size(); j++) {
                                if (fast.get(j).getName().equalsIgnoreCase(days[i])) {
                                    flag1++;
                                }
                            }

                            for (int j = 0; j < nv_exempt.size(); j++) {
                                if (nv_exempt.get(j).getName().equalsIgnoreCase(days[i])) {
                                    flag2++;
                                }
                            }


                            if(flag1>0)
                                fast_base.add(new MultipleSelectionModel(i+"",days[i],true));
                            else
                                fast_base.add(new MultipleSelectionModel(i+"",days[i],false));

                            if(flag2>0)
                                nv_exempt_base.add(new MultipleSelectionModel(i+"",days[i],true));
                            else
                                nv_exempt_base.add(new MultipleSelectionModel(i+"",days[i],false));
                        }

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
                Tools.initNetworkErrorToast(FoodSpecificationsActivity.this);
                Log.d("Error",error.toString());
                Log.d("myTag","I am here");
            }
        };

        // params.put("cuisine",);
        // params.put("gender",genderString);

        NetworkManager.getInstance(this).sendPostRequestWithoutParams(Urls.get_food_info,listener,errorListener,this);

    }

    public void setNvPreftext(ArrayList<MultipleSelectionModel> items)
    {  String string = "";
        ArrayList<MultipleSelectionModel> temp = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).isSelected()) {
                temp.add(items.get(i));
                // diseasesList.add(items.get(i));
            }
//                            string+=items.get(i).getLabel()+"   ";
        }
        nonVegSelected = temp;
        if (temp.size() == 1) {
            string = temp.get(0).getLabel();
        } else if (temp.size() == 0) {
            string = "";
        } else {
            string = temp.size() + " Pref selected";
        }
        nonVegPref_editText.setText(string);


    }

    void clearNonVegPrefList(){

    }
}
