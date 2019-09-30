package com.quanutrition.app.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.quanutrition.app.R;
import com.quanutrition.app.Utils.NetworkManager;
import com.quanutrition.app.Utils.Tools;
import com.quanutrition.app.selectiondialogs.DialogUtils;
import com.quanutrition.app.selectiondialogs.SingleSelectionModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DiseaseDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    EditText diabetesDuration,thyroidDuration,pcosDuration,cholesterolDuration,physicalInjuryDuration,hypertensionDuration;
    EditText diabetesSevere,thyroidSevere,cholesterolSevere,hypertensionSevere;
    ArrayList<String> diseasesList = new ArrayList<>();
    TextView saveInfo;
    JSONArray diseasearray = new JSONArray();
    ArrayList<SingleSelectionModel> duration;
    LinearLayout layout_diabetes,layout_pcos,layout_thyroid,layout_cholestrol,layout_physicalInjury,layout_hypertension;
    ArrayList<SingleSelectionModel> list_diabetes,list_pcos,list_thyroid,list_cholestrol,list_physicalInjury,list_hypertension;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_details);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        Tools.setSystemBarColor(this, R.color.colorPrimary);
        getSupportActionBar().setTitle("Tell us more about...");

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        diseasesList = bundle.getStringArrayList("diseases");
        list_diabetes = new ArrayList<>();
        duration = new ArrayList<>();
        list_pcos = new ArrayList<>();
        list_thyroid = new ArrayList<>();
        list_cholestrol = new ArrayList<>();
        list_physicalInjury = new ArrayList<>();
        list_hypertension = new ArrayList<>();


        diabetesDuration = findViewById(R.id.diabetesDuration);
        thyroidDuration = findViewById(R.id.thyroidDuration);
        pcosDuration = findViewById(R.id.pcosDuration);
        cholesterolDuration = findViewById(R.id.cholesterolDuration);
        physicalInjuryDuration = findViewById(R.id.physicalInjuryDuration);
        hypertensionDuration = findViewById(R.id.hypertensionDuration);
        diabetesSevere = findViewById(R.id.diabetesSevere);
        thyroidSevere = findViewById(R.id.thyroidSevere);
        cholesterolSevere = findViewById(R.id.cholesterolSevere);
        hypertensionSevere = findViewById(R.id.hypertensionSevere);
        layout_diabetes = findViewById(R.id.diabetesLayout);
        layout_thyroid = findViewById(R.id.ThyroidLayout);
        layout_pcos = findViewById(R.id.PCOSLayout);
        layout_cholestrol = findViewById(R.id.CholesterolLayout);
        layout_physicalInjury = findViewById(R.id.PhysicalInjuryLayout);
        layout_hypertension = findViewById(R.id.hypertensionLayout);
        saveInfo = findViewById(R.id.save_diseaseInfo);


        diabetesDuration.setFocusable(false);
        thyroidDuration.setFocusable(false);
        pcosDuration.setFocusable(false);
        cholesterolDuration.setFocusable(false);
        physicalInjuryDuration.setFocusable(false);
        hypertensionDuration.setFocusable(false);
        diabetesSevere.setFocusable(false);
        thyroidSevere.setFocusable(false);
        cholesterolSevere.setFocusable(false);
        hypertensionSevere.setFocusable(false);

        diabetesDuration.setOnClickListener(this);
        thyroidDuration.setOnClickListener(this);
        pcosDuration.setOnClickListener(this);
        cholesterolDuration.setOnClickListener(this);
        physicalInjuryDuration.setOnClickListener(this);
        hypertensionDuration.setOnClickListener(this);
        diabetesSevere.setOnClickListener(this);
        thyroidSevere.setOnClickListener(this);
        cholesterolSevere.setOnClickListener(this);
        hypertensionSevere.setOnClickListener(this);
        saveInfo.setOnClickListener(this);


        setVisibility();
        requestFetchDiseases();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id== R.id.diabetesDuration){
            DialogUtils.getSingleSelectionDialog(this,duration, new DialogUtils.OnSingleItemSelectedListener() {
                @Override
                public void onItemSelected(int position, SingleSelectionModel item) {
                    diabetesDuration.setText(item.getLabel());

                }
            });
        }else if(id == R.id.thyroidDuration){
            DialogUtils.getSingleSelectionDialog(this,duration, new DialogUtils.OnSingleItemSelectedListener() {
                @Override
                public void onItemSelected(int position, SingleSelectionModel item) {
                    thyroidDuration.setText(item.getLabel());
                }
            });
        }else if(id == R.id.pcosDuration){
            DialogUtils.getSingleSelectionDialog(this,duration, new DialogUtils.OnSingleItemSelectedListener() {
                @Override
                public void onItemSelected(int position, SingleSelectionModel item) {
                    pcosDuration.setText(item.getLabel());
                }
            });
        }else if(id == R.id.cholesterolDuration){
            DialogUtils.getSingleSelectionDialog(this,duration, new DialogUtils.OnSingleItemSelectedListener() {
                @Override
                public void onItemSelected(int position, SingleSelectionModel item) {
                    cholesterolDuration.setText(item.getLabel());
                }
            });
        }else if(id == R.id.physicalInjuryDuration){
            DialogUtils.getSingleSelectionDialog(this,duration, new DialogUtils.OnSingleItemSelectedListener() {
                @Override
                public void onItemSelected(int position, SingleSelectionModel item) {
                    physicalInjuryDuration.setText(item.getLabel());
                }
            });
        }else if(id == R.id.hypertensionDuration){
            DialogUtils.getSingleSelectionDialog(this,duration, new DialogUtils.OnSingleItemSelectedListener() {
                @Override
                public void onItemSelected(int position, SingleSelectionModel item) {
                    hypertensionDuration.setText(item.getLabel());
                }
            });
        }else if(id == R.id.diabetesSevere){
            DialogUtils.getSingleSelectionDialog(this,list_diabetes, new DialogUtils.OnSingleItemSelectedListener() {
                @Override
                public void onItemSelected(int position, SingleSelectionModel item) {
                    diabetesSevere.setText(item.getLabel());
                }
            });
        }else if(id == R.id.thyroidSevere){
            DialogUtils.getSingleSelectionDialog(this,list_thyroid, new DialogUtils.OnSingleItemSelectedListener() {
                @Override
                public void onItemSelected(int position, SingleSelectionModel item) {
                    thyroidSevere.setText(item.getLabel());
                }
            });
        }else if(id == R.id.cholesterolSevere){
            DialogUtils.getSingleSelectionDialog(this,list_cholestrol, new DialogUtils.OnSingleItemSelectedListener() {
                @Override
                public void onItemSelected(int position, SingleSelectionModel item) {
                    cholesterolSevere.setText(item.getLabel());
                }
            });
        }else if(id == R.id.hypertensionSevere){
            DialogUtils.getSingleSelectionDialog(this,list_hypertension, new DialogUtils.OnSingleItemSelectedListener() {
                @Override
                public void onItemSelected(int position, SingleSelectionModel item) {
                    hypertensionSevere.setText(item.getLabel());
                }
            });
        }
        else if(id == R.id.save_diseaseInfo)
        {
            diseasearray = new JSONArray();

            if(layout_diabetes.getVisibility() == View.VISIBLE) {
                try {
                    JSONObject innerObject = new JSONObject();
                    innerObject.put("id", list_diabetes.get(list_diabetes.size()-1).getId());
                    innerObject.put("name", "DIABETES");
                    innerObject.put("duration", diabetesDuration.getText().toString());
                    innerObject.put("severity", diabetesSevere.getText().toString());
                    diseasearray.put(innerObject);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
            if(layout_pcos.getVisibility() == View.VISIBLE) {
                try {
                    JSONObject innerObject = new JSONObject();
                    innerObject.put("id", list_pcos.get(list_pcos.size()-1).getId());
                    innerObject.put("name", "PCOS");
                    innerObject.put("duration", pcosDuration.getText().toString());
                    innerObject.put("severity", "");
                    diseasearray.put(innerObject);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }

            if(layout_hypertension.getVisibility() == View.VISIBLE) {
                try {
                    JSONObject innerObject = new JSONObject();
                    innerObject.put("id", list_hypertension.get(list_hypertension.size()-1).getId());
                    innerObject.put("name", "HYPERTENSION");
                    innerObject.put("duration", hypertensionDuration.getText().toString());
                    innerObject.put("severity", hypertensionSevere.getText().toString());
                    diseasearray.put(innerObject);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }

            if(layout_cholestrol.getVisibility() == View.VISIBLE) {
                try {
                    JSONObject innerObject = new JSONObject();
                    innerObject.put("id", list_cholestrol.get(list_cholestrol.size()-1).getId());
                    innerObject.put("name", "CHOLESTEROL");
                    innerObject.put("duration", cholesterolDuration.getText().toString());
                    innerObject.put("severity", cholesterolSevere.getText().toString());
                    diseasearray.put(innerObject);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }

            if(layout_thyroid.getVisibility() == View.VISIBLE) {
                try {
                    JSONObject innerObject = new JSONObject();
                    innerObject.put("id", list_thyroid.get(list_thyroid.size()-1).getId());
                    innerObject.put("name", "THYROID");
                    innerObject.put("duration", thyroidDuration.getText().toString());
                    innerObject.put("severity", thyroidSevere.getText().toString());
                    diseasearray.put(innerObject);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }

            if(layout_physicalInjury.getVisibility() == View.VISIBLE) {
                try {
                    JSONObject innerObject = new JSONObject();
                    innerObject.put("id", list_physicalInjury.get(list_physicalInjury.size()-1).getId());
                    innerObject.put("name", "PHYSICAL INJURY");
                    innerObject.put("duration", physicalInjuryDuration.getText().toString());
                    innerObject.put("severity", "");
                    diseasearray.put(innerObject);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }

            requestSave();
        }
    }


    private void setVisibility() {

        layout_diabetes.setVisibility(View.GONE);
        layout_thyroid.setVisibility(View.GONE);
        layout_pcos.setVisibility(View.GONE);
        layout_cholestrol.setVisibility(View.GONE);
        layout_physicalInjury.setVisibility(View.GONE);
        layout_physicalInjury.setVisibility(View.GONE);
        layout_hypertension.setVisibility(View.GONE);


        for(int i =0;i<diseasesList.size();i++)
        {



            if(diseasesList.get(i).equalsIgnoreCase("DIABETES"))
            {
                layout_diabetes.setVisibility(View.VISIBLE);
            }
            if(diseasesList.get(i).equalsIgnoreCase("pcos"))
            {
                layout_pcos.setVisibility(View.VISIBLE);

            }
            if(diseasesList.get(i).equalsIgnoreCase("thyroid"))
            {
                layout_thyroid.setVisibility(View.VISIBLE);

            }

            if(diseasesList.get(i).equalsIgnoreCase("hypertension"))
            {
                layout_hypertension.setVisibility(View.VISIBLE);

            }

            if(diseasesList.get(i).equalsIgnoreCase("cholesterol"))
            {
                layout_cholestrol.setVisibility(View.VISIBLE);

            }

            if(diseasesList.get(i).equalsIgnoreCase("physical Injury"))
            {
                layout_physicalInjury.setVisibility(View.VISIBLE);
            }

        }





    }


    void requestFetchDiseases(){
        final androidx.appcompat.app.AlertDialog ad = Tools.getDialog(" Please Wait ...",this);
        ad.show();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ad.dismiss();
                Log.d("medical",response);
                try {
                    JSONObject diseaseObject = new JSONObject(response);
                    int res = diseaseObject.getInt("res");
                    String msg = diseaseObject.getString("msg");
                    if(res ==1) {

                        JSONObject data = diseaseObject.getJSONObject("data");

                        JSONArray diseasesArray = data.getJSONArray("disease");
                        for(int k=0;k<diseasesArray.length();k++){
                            JSONObject disOb = diseasesArray.getJSONObject(k);
                            if(disOb.getString("name").equalsIgnoreCase("Cholesterol")){
                                cholesterolSevere.setText(disOb.getString("severity"));
                                cholesterolDuration.setText(disOb.getString("duration"));
                            }else if(disOb.getString("name").equalsIgnoreCase("PCOS")){
//                                cholesterolSevere.setText(disOb.getString("severity"));
                                pcosDuration.setText(disOb.getString("duration"));
                            }else if(disOb.getString("name").equalsIgnoreCase("PHYSICAL INJURY")){
//                                cholesterolSevere.setText(disOb.getString("severity"));
                                physicalInjuryDuration.setText(disOb.getString("duration"));
                            }else if(disOb.getString("name").equalsIgnoreCase("DIABETES")){
                                diabetesSevere.setText(disOb.getString("severity"));
                                diabetesDuration.setText(disOb.getString("duration"));
                            }else if(disOb.getString("name").equalsIgnoreCase("hypertension")){
                                hypertensionSevere.setText(disOb.getString("severity"));
                                hypertensionDuration.setText(disOb.getString("duration"));
                            }else if(disOb.getString("name").equalsIgnoreCase("thyroid")){
                                thyroidSevere.setText(disOb.getString("severity"));
                                thyroidDuration.setText(disOb.getString("duration"));
                            }
                        }

                        JSONArray dataArray = data.getJSONArray("data");

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject diseaseDataObject = dataArray.getJSONObject(i);
                            String name = diseaseDataObject.getString("name");
                            int id = diseaseDataObject.getInt("id");

                            JSONObject innerObject = diseaseDataObject.getJSONObject("data");
                            JSONArray durationArray = innerObject.getJSONArray("duration");
                            duration.clear();
                            for (int j = 0; j < durationArray.length(); j++) {

                                    duration.add(new SingleSelectionModel(id + "", durationArray.getString(j)));

                            }
                            
                            if (!(name.equalsIgnoreCase("PCOS")||name.equalsIgnoreCase("PHYSICAL INJURY"))) {
                                JSONArray severityArray = innerObject.getJSONArray("severity");
                                for (int j = 0; j < severityArray.length(); j++) {

                                    if (name.equalsIgnoreCase("DIABETES")) {
                                        list_diabetes.add(new SingleSelectionModel(id + "", severityArray.getString(j)));

                                    } else if (name.equalsIgnoreCase("hypertension")) {
                                        list_hypertension.add(new SingleSelectionModel(id + "", severityArray.getString(j)));

                                    } else if (name.equalsIgnoreCase("cholesterol")) {
                                        list_cholestrol.add(new SingleSelectionModel(id + "", severityArray.getString(j)));

                                    } else if (name.equalsIgnoreCase("thyroid")) {
                                        list_thyroid.add(new SingleSelectionModel(id + "", severityArray.getString(j)));

                                    }
                                }
                            }else{
                                if(name.equalsIgnoreCase("pcos"))
                                {
                                    list_pcos.add(new SingleSelectionModel(id+"",""));
                                }else if(name.equalsIgnoreCase("physical Injury"))
                                {
                                    list_physicalInjury.add(new SingleSelectionModel(id+"",""));
                                }
                            }
                        }



                    }
                    else
                    {
                        Tools.initCustomToast(getApplicationContext(),msg);
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
                Tools.initNetworkErrorToast(DiseaseDetailsActivity.this);
                Log.d("Error",error.toString());
                Log.d("myTag","I am here");
            }
        };



        NetworkManager.getInstance(this).sendPostRequestWithoutParams(Urls.get_disease_info,listener,errorListener,this);

    }


    void requestSave(){
        final androidx.appcompat.app.AlertDialog ad = Tools.getDialog("Requesting ...",this);
        ad.show();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ad.dismiss();

                try {
                    JSONObject object = new JSONObject(response);
                    int res = object.getInt("res");
                    if(res==1)
                    {

                        finish();

                    }
                    Tools.initCustomToast(getApplicationContext(),object.getString("msg"));

                } catch (JSONException e) {
                    e.printStackTrace();
                    Tools.initCustomToast(getApplicationContext(),"Some Error");
                }

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ad.dismiss();
                Tools.initNetworkErrorToast(DiseaseDetailsActivity.this);
                Log.d("Error",error.toString());
                Log.d("myTag","I am here");
            }
        };
        Map<String, String> params = new HashMap<>();
        params.put("diseasedetails",diseasearray+"");

        Log.d("Disease Array",diseasearray+"");

        NetworkManager.getInstance(this).sendPostRequestWithHeader(Urls.save_disease_info,params,listener,errorListener,this);

    }


}
