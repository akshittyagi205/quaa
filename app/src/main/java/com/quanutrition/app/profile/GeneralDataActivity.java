package com.quanutrition.app.profile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
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

public class GeneralDataActivity extends AppCompatActivity implements View.OnClickListener{

    EditText help,numberCooking,numberEat,unitEat,numberWater,unitWater,numberSmoke,unitSmoke,numberAlcohol,unitAlcohol,numberBirth,unitBirth;
    TextView addOffDays,save;
    LinearLayout birthLayout,alcoholLayout,smokeLayout;
    RecyclerView offDays_re;
    ArrayList<ChipsModel> offDays;
    ArrayList<MultipleSelectionModel> offDays_base;
    ChipsAdapter offDays_Adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_data);
        help = findViewById(R.id.help);
        numberCooking = findViewById(R.id.numberCooking);
        numberEat = findViewById(R.id.numberEat);
        unitEat = findViewById(R.id.unitEat);
        numberWater = findViewById(R.id.numberWater);
        unitWater = findViewById(R.id.unitWater);
        numberSmoke = findViewById(R.id.numberSmoke);
        unitSmoke = findViewById(R.id.unitSmoke);
        numberAlcohol = findViewById(R.id.numberAlcohol);
        unitAlcohol = findViewById(R.id.unitAlcohol);
        addOffDays = findViewById(R.id.addOffDays);
        offDays_re = findViewById(R.id.offDays_re);
        unitBirth = findViewById(R.id.unitBirth);
        numberBirth = findViewById(R.id.numberBirth);
        birthLayout = findViewById(R.id.birthLayout);
        smokeLayout = findViewById(R.id.smokeLayout);
        alcoholLayout = findViewById(R.id.alcoholLayout);
        save = findViewById(R.id.save);

        offDays = new ArrayList<>();
        offDays_base = new ArrayList<>();

        help.setFocusable(false);
        numberCooking.setFocusable(false);
        numberEat.setFocusable(false);
        unitEat.setFocusable(false);
        numberSmoke.setFocusable(false);
        unitSmoke.setFocusable(false);
        numberAlcohol.setFocusable(false);
        unitAlcohol.setFocusable(false);
        numberWater.setFocusable(false);
        unitWater.setFocusable(false);
        numberBirth.setFocusable(false);
        unitBirth.setFocusable(false);


        help.setOnClickListener(this);
        numberCooking.setOnClickListener(this);
        numberEat.setOnClickListener(this);
        unitEat.setOnClickListener(this);
        numberSmoke.setOnClickListener(this);
        unitSmoke.setOnClickListener(this);
        numberAlcohol.setOnClickListener(this);
        unitAlcohol.setOnClickListener(this);
        addOffDays.setOnClickListener(this);
        save.setOnClickListener(this);
        numberWater.setOnClickListener(this);
        unitWater.setOnClickListener(this);
        numberBirth.setOnClickListener(this);
        unitBirth.setOnClickListener(this);


        offDays_Adapter = new ChipsAdapter(offDays, this, new ChipsAdapter.OnClickListener() {
            @Override
            public void onClose(View view, int position) {
                for(int i=0;i<offDays_base.size();i++){
                    if(offDays.get(position).getId().equalsIgnoreCase(offDays_base.get(i).getId())){
                        offDays_base.get(i).setSelected(false);
                    }
                }
                offDays.remove(position);
                offDays_Adapter.notifyDataSetChanged();
            }
        });
        RecyclerView.LayoutManager flowLayoutManager = new FlowLayoutManager();
        ((FlowLayoutManager) flowLayoutManager).setAlignment(Alignment.LEFT);
        ((FlowLayoutManager) flowLayoutManager).setAutoMeasureEnabled(true);
        ((FlowLayoutManager) flowLayoutManager).maxItemsPerLine(3);
        offDays_re.setLayoutManager(flowLayoutManager);
        offDays_re.setAdapter(offDays_Adapter);

        requestFetch();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id==R.id.help){
           /* DialogUtils.getSingleSelectionDialog(this,DialogUtils.getSingleArrayListWithResource(this,R.array.helpAvailable), new DialogUtils.OnSingleItemSelectedListener() {
                @Override
                public void onItemSelected(int position, SingleSelectionModel item) {
                    help.setText(item.getLabel());
                }
            });*/

            String[] data = {"No","Cook"};
            DialogUtils.getCustomPicker(this, "Select Frequency", data, new DialogUtils.OnCustomItemPicked() {
                @Override
                public void onNumberPicked(String selected) {
                    help.setText(selected);
                }
            });
        }else if(id == R.id.numberCooking){
            String[] data = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20"};
            DialogUtils.getCustomPicker(this, "Select Number of People", data, new DialogUtils.OnCustomItemPicked() {
                @Override
                public void onNumberPicked(String selected) {
                    numberCooking.setText(selected);
                }
            });
        } else if(id == R.id.numberEat){
            String[] data = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20"};
            DialogUtils.getCustomPicker(this, "Select Eat Out Frequency", data, new DialogUtils.OnCustomItemPicked() {
                @Override
                public void onNumberPicked(String selected) {
                    numberEat.setText(selected);
                }
            });
        } else if(id == R.id.numberSmoke){
            String[] data = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20"};
            DialogUtils.getCustomPicker(this, "Select Smoke Frequency", data, new DialogUtils.OnCustomItemPicked() {
                @Override
                public void onNumberPicked(String selected) {
                    numberSmoke.setText(selected);
                }
            });
        } else if(id == R.id.numberBirth){
            String[] data = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20"};
            DialogUtils.getCustomPicker(this, "Birth Control Pill Frequency", data, new DialogUtils.OnCustomItemPicked() {
                @Override
                public void onNumberPicked(String selected) {
                    numberBirth.setText(selected);
                }
            });
        } else if(id == R.id.numberAlcohol){
            String[] data = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20"};
            DialogUtils.getCustomPicker(this, "Select Alcohol Frequency", data, new DialogUtils.OnCustomItemPicked() {
                @Override
                public void onNumberPicked(String selected) {
                    numberAlcohol.setText(selected);
                }
            });
        } else if(id == R.id.numberWater){
            String[] data = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20"};
            DialogUtils.getCustomPicker(this, "Select Water Frequency", data, new DialogUtils.OnCustomItemPicked() {
                @Override
                public void onNumberPicked(String selected) {
                    numberWater.setText(selected);
                }
            });
        } else if(id == R.id.unitWater){
            String[] data = {"Liter","Glasses"};
            DialogUtils.getCustomPicker(this, "Select Unit", data, new DialogUtils.OnCustomItemPicked() {
                @Override
                public void onNumberPicked(String selected) {
                    unitWater.setText(selected);
                }
            });
        } else if(id == R.id.unitEat){
            String[] data = {"Day","Week","Month"};
            DialogUtils.getCustomPicker(this, "Select Frequency", data, new DialogUtils.OnCustomItemPicked() {
                @Override
                public void onNumberPicked(String selected) {
                    unitEat.setText(selected);
                }
            });
        } else if(id == R.id.unitSmoke){
            String[] data = {"Day","Week","Month"};
            DialogUtils.getCustomPicker(this, "Select Frequency", data, new DialogUtils.OnCustomItemPicked() {
                @Override
                public void onNumberPicked(String selected) {
                    unitSmoke.setText(selected);
                }
            });
        } else if(id == R.id.unitBirth){
            String[] data = {"Day","Week","Month"};
            DialogUtils.getCustomPicker(this, "Select Frequency", data, new DialogUtils.OnCustomItemPicked() {
                @Override
                public void onNumberPicked(String selected) {
                    unitBirth.setText(selected);
                }
            });
        } else if(id == R.id.unitAlcohol){
            String[] data = {"Day","Week","Month"};
            DialogUtils.getCustomPicker(this, "Select Frequency", data, new DialogUtils.OnCustomItemPicked() {
                @Override
                public void onNumberPicked(String selected) {
                    unitAlcohol.setText(selected);
                }
            });
        } else if(id == R.id.addOffDays){

            DialogUtils.getMultipleSearchDialog(this, offDays_base, new DialogUtils.OnMultipleItemsSelected() {
                @Override
                public void onMultipleItemsSelected(ArrayList<MultipleSelectionModel> items) {
                    offDays_base = items;
                    offDays.clear();
                    for(int i=0;i<offDays_base.size();i++){
                        if(offDays_base.get(i).isSelected()){
                            offDays.add(new ChipsModel(offDays_base.get(i).getId(),offDays_base.get(i).getLabel()));
                        }
                    }
                    offDays_Adapter.notifyDataSetChanged();
                }
            });

        } else if(id == R.id.save){

            String offDayString = "";
            for (int i = 0; i < offDays.size(); i++) {
                if(i==0)
                offDayString += offDays.get(i).getName().toLowerCase();
                else
                    offDayString += ","+offDays.get(i).getName().toLowerCase();
            }

            if(!Tools.validateNormalText(numberEat)||!Tools.validateNormalText(numberWater)||
                    !Tools.validateNormalText(unitEat)||!Tools.validateNormalText(unitWater)){
                Tools.initCustomToast(this,"Eat out and Water frequency are required fields!");
            }else
            requestSave(offDayString);


//            startActivity(new Intent(GeneralDataActivity.this,TimeInputActivity.class));
        }
    }

    void requestSave(String offDayString){

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
                Tools.initNetworkErrorToast(GeneralDataActivity.this);
                Log.d("Error",error.toString());
                Log.d("myTag","I am here");
            }
        };
        Map<String, String> params = new HashMap<>();
        params.put("help",Tools.getText(help));
        params.put("howmany",Tools.getText(numberCooking));
        params.put("eat1",Tools.getText(numberEat));
        params.put("eat2",Tools.getText(unitEat));
        params.put("smoke1",Tools.getText(numberSmoke));
        params.put("smoke2",Tools.getText(unitSmoke));
        params.put("alcohol1",Tools.getText(numberAlcohol));
        params.put("alcohol2",Tools.getText(unitAlcohol));
        params.put("water1",Tools.getText(numberWater));
        params.put("water2",Tools.getText(unitWater));
        params.put("birth1",Tools.getText(numberBirth));
        params.put("birth2",Tools.getText(unitBirth));
        params.put("offdays1",offDayString);


        NetworkManager.getInstance(this).sendPostRequestWithHeader(Urls.save_general_info,params,listener,errorListener,this);

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
                        JSONObject data =foodObjectOuter.getJSONObject("data");
                        help.setText(data.getString("help"));
                        numberCooking.setText(data.getString("howmany"));
                        numberEat.setText(data.getString("eat1"));
                        numberAlcohol.setText(data.getString("alcohol1"));
                        numberSmoke.setText(data.getString("smoke1"));
                        numberWater.setText(data.getString("water1"));

                        unitEat.setText(data.getString("eat2"));
                        unitAlcohol.setText(data.getString("alcohol2"));
                        unitSmoke.setText(data.getString("smoke2"));
                        unitWater.setText(data.getString("water2"));


                        if(data.getString("smoke_flag").equalsIgnoreCase("1")){
                            smokeLayout.setVisibility(View.VISIBLE);
                        }else{
                            smokeLayout.setVisibility(View.GONE);
                        }

                        if(data.getString("alcohol_flag").equalsIgnoreCase("1")){
                            alcoholLayout.setVisibility(View.VISIBLE);
                        }else{
                            alcoholLayout.setVisibility(View.GONE);
                        }

                        if(data.getString("birth_flag").equalsIgnoreCase("1")){
                            birthLayout.setVisibility(View.VISIBLE);
                        }else{
                            birthLayout.setVisibility(View.GONE);
                        }

                        JSONArray offDayArray = data.getJSONArray("offdays1");
                        for (int i = 0; i < offDayArray.length(); i++) {
                            if(!offDayArray.getString(i).isEmpty())
                                offDays.add(new ChipsModel("1", offDayArray.getString(i)));
                        }
                        offDays_Adapter.notifyDataSetChanged();

                        String[] days = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
                        for(int i=0;i<7;i++){

                            int flag = 0;
                            for (int j = 0; j < offDays.size(); j++) {
                                if (offDays.get(j).getName().equalsIgnoreCase(days[i])) {
                                    flag++;
                                }
                            }
                            if(flag>0)
                                offDays_base.add(new MultipleSelectionModel(i+"",days[i],true));
                            else
                                offDays_base.add(new MultipleSelectionModel(i+"",days[i],false));
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
                Tools.initNetworkErrorToast(GeneralDataActivity.this);
                Log.d("Error",error.toString());
                Log.d("myTag","I am here");
            }
        };

        // params.put("cuisine",);
        // params.put("gender",genderString);

        NetworkManager.getInstance(this).sendPostRequestWithoutParams(Urls.get_general_info,listener,errorListener,this);

    }


}
