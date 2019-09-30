package com.quanutrition.app.profile;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.quanutrition.app.R;
import com.quanutrition.app.Utils.Constants;
import com.quanutrition.app.Utils.NetworkManager;
import com.quanutrition.app.Utils.SqliteDbHelper;
import com.quanutrition.app.Utils.Tools;
import com.quanutrition.app.selectiondialogs.CountryModel;
import com.quanutrition.app.selectiondialogs.DialogUtils;
import com.quanutrition.app.selectiondialogs.SingleSelectionModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BasicInfoActivity extends AppCompatActivity implements View.OnClickListener {

    EditText dob,gender,country,city,countryCode,height,weight,weightUnit,firstName_edit,lastname_edit,emailId_edit,phoneNo_edit;
    String unitWeight="Kgs",unitHeight="ft. in.",stateName = " ",firstName="",lastName ="",countryCode_string,phone,emailId ="",countryName ="" ,cityName="",dobString="",genderString="",heightString="",weightString="",weightUnitString="";
    int country_code=101;
    EditText bloodGroup, physicalActivity;
    String bloodGroup_String,physicalActivity_string;
    TextView saveBasicInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_info);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
//        Tools.setSystemBarColorCustom(this, R.color.colorAccent);
        getSupportActionBar().setTitle("Basic Information");

        dob = findViewById(R.id.dob);
        gender = findViewById(R.id.gender);
        country = findViewById(R.id.country);
        city = findViewById(R.id.city);
        countryCode = findViewById(R.id.countryCode);
        height = findViewById(R.id.height);
        bloodGroup = findViewById(R.id.bloodGroup);
        physicalActivity = findViewById(R.id.physicalActivity);
        weight = findViewById(R.id.weight);
        weightUnit = findViewById(R.id.selectWeightUnit);
        firstName_edit = findViewById(R.id.editFirstName);
        lastname_edit = findViewById(R.id.editLastName);
        emailId_edit = findViewById(R.id.email);
        phoneNo_edit = findViewById(R.id.phone_number);
        saveBasicInfo = findViewById(R.id.save_basic_button);

        dob.setFocusable(false);
        gender.setFocusable(false);
        country.setFocusable(false);
        city.setFocusable(false);
        countryCode.setFocusable(false);
        height.setFocusable(false);
        weightUnit.setFocusable(false);
        bloodGroup.setFocusable(false);
        physicalActivity.setFocusable(false);
        phoneNo_edit.setFocusable(false);

        requestFetch();

        dob.setOnClickListener(this);
        gender.setOnClickListener(this);
        country.setOnClickListener(this);
        city.setOnClickListener(this);
        bloodGroup.setOnClickListener(this);
        physicalActivity.setOnClickListener(this);
      //  countryCode.setOnClickListener(this);
        height.setOnClickListener(this);
        saveBasicInfo.setOnClickListener(this);
        weightUnit.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.dob) {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR); // current year
            int mMonth = c.get(Calendar.MONTH); // current month
            int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
            // date picker dialog

            android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(this,R.style.MyDialogTheme,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            // set day of month , month and year value in the edit text
                            int month = monthOfYear+1;
                            String Date1 = dayOfMonth+"-"+month+"-"+year;
                            Calendar calendar = Calendar.getInstance();
                            Date c = calendar.getTime();
                            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                            String date = df.format(c);
                            int yearNow = Integer.parseInt(date.split("-")[2]);
                            if(yearNow-year<=5){
                                Tools.initCustomToast(BasicInfoActivity.this,"Age cannot be less than 5 years!");
                            }else {
                                dob.setText(Date1);
                            }

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setSpinnersShown(true);
            datePickerDialog.getDatePicker().setCalendarViewShown(false);
            datePickerDialog.show();
        } else if (id == R.id.gender) {
            DialogUtils.getSingleSelectionDialog(this, DialogUtils.getSingleArrayListWithResource(this, R.array.gender), new DialogUtils.OnSingleItemSelectedListener() {
                @Override
                public void onItemSelected(int position, SingleSelectionModel item) {
//                Tools.initCustomToast(BasicInfoActivity.this,item.getLabel()+"   and id  "+item.getId());
                    gender.setText(item.getLabel());
                }
            });
        }
         else if (id == R.id.city) {
            if (country.getText().toString().trim().isEmpty()) {
                /*country.setText("India");
                country_code = 101;*/
                Tools.initCustomToast(this,"Please select your country first!");
            }else {
                SqliteDbHelper helper = new SqliteDbHelper(BasicInfoActivity.this);
                ArrayList<CountryModel> countryModel = helper.getCitites(country_code);
                ArrayList<SingleSelectionModel> list = new ArrayList<>();
                final ArrayList<String> state = new ArrayList<>();
                for (int i = 0; i < countryModel.size(); i++) {
                    list.add(new SingleSelectionModel(countryModel.get(i).getId() + "", countryModel.get(i).getName().split(";")[0]));
                    state.add(countryModel.get(i).getName().split(";")[1]);
                }

                DialogUtils.getSingleSearchDialog(this, list, new DialogUtils.OnSingleItemSelectedListener() {
                    @Override
                    public void onItemSelected(int position, SingleSelectionModel item) {
                        city.setText(item.getLabel());
                    /*SqliteDbHelper helper = new SqliteDbHelper(BasicInfoActivity.this);
                    // Log.d("state",helper.getStateId(Integer.parseInt(item.getId()))  +"");

                    stateName = helper.getStateName(helper.getStateId(Integer.parseInt(item.getId())));*/
                        stateName = state.get(position);

                        Log.d("state", stateName);

                    }
                });
            }

        } else if (id == R.id.countryCode) {
            SqliteDbHelper helper = new SqliteDbHelper(BasicInfoActivity.this);
            ArrayList<CountryModel> countryModel = helper.getCountries();
            ArrayList<SingleSelectionModel> list = new ArrayList<>();
            for (int i = 0; i < countryModel.size(); i++) {
                list.add(new SingleSelectionModel(countryModel.get(i).getId() + "", "+" + countryModel.get(i).getPhonecode() + " : " + countryModel.get(i).getName()));
            }

            DialogUtils.getSingleSearchDialog(this, list, new DialogUtils.OnSingleItemSelectedListener() {
                @Override
                public void onItemSelected(int position, SingleSelectionModel item) {
                    countryCode.setText(item.getLabel().split(":")[0].trim());
                    country.setText(item.getLabel().split(":")[1].trim());
                    country_code = Integer.parseInt(item.getId());
                }
            });
        }else if(id == R.id.country) {
            SqliteDbHelper helper = new SqliteDbHelper(BasicInfoActivity.this);
            final ArrayList<CountryModel> countryModel = helper.getCountries();
            ArrayList<SingleSelectionModel> list = new ArrayList<>();
            for (int i = 0; i < countryModel.size(); i++) {
                list.add(new SingleSelectionModel(countryModel.get(i).getId() + "",countryModel.get(i).getName()));
            }

            DialogUtils.getSingleSearchDialog(this, list, new DialogUtils.OnSingleItemSelectedListener() {
                @Override
                public void onItemSelected(int position, SingleSelectionModel item) {

                    if(!(Tools.getText(countryCode).equalsIgnoreCase(countryModel.get(position).getPhonecode()+"")))
                    {
                        country.setText(item.getLabel());
                        country_code = Integer.parseInt(item.getId());
                    }else{
                        Tools.initCustomToast(BasicInfoActivity.this,"Country and phone code cannot be different!");
                    }
                }
            });
        }else if (id == R.id.height) {
            DialogUtils.getSimpleNumberPickerHeight(this, "Select Your Height", unitHeight, 40, new DialogUtils.OnNumberPicked() {
                @Override
                public void onNumberPicked(String selected, int position, String unit) {
                    height.setText(selected);
                    unitHeight = unit;
                    Log.d("unit", unit);

                    setheightInInches(selected);


                }
            });
        } else if (id == R.id.bloodGroup) {
            String[] array = getResources().getStringArray(R.array.BloodGroups);
            DialogUtils.getCustomPicker(this, "Select Blood Group", array, new DialogUtils.OnCustomItemPicked() {
                @Override
                public void onNumberPicked(String selected) {
                    bloodGroup.setText(selected);
                }
            });
        } else if (id == R.id.physicalActivity) {
            DialogUtils.getSingleSelectionDialog(this, physical, new DialogUtils.OnSingleItemSelectedListener() {
                @Override
                public void onItemSelected(int position, SingleSelectionModel item) {
                    physicalActivity.setText(item.getLabel());
                    physicalActivity_string = item.getId();
                }
            });
        } else if (id == R.id.selectWeightUnit) {
            DialogUtils.getSingleSelectionDialog(this, DialogUtils.getSingleArrayListWithResource(this, R.array.weightUnit), new DialogUtils.OnSingleItemSelectedListener() {
                @Override
                public void onItemSelected(int position, SingleSelectionModel item) {
                    weightUnit.setText(item.getLabel());
                }
            });
        } else if (id == R.id.save_basic_button) {
            firstName = firstName_edit.getText().toString().trim();
            lastName = lastname_edit.getText().toString().trim();
            countryCode_string = countryCode.getText().toString().trim();
            phone = phoneNo_edit.getText().toString().trim();
            emailId = emailId_edit.getText().toString().trim();
            dobString = dob.getText().toString().trim();
            genderString = gender.getText().toString().trim();
            countryName = country.getText().toString().trim();
            cityName = city.getText().toString().trim();
            bloodGroup_String = bloodGroup.getText().toString();
//            physicalActivity_string = physicalActivity.getText().toString().replace('\n',' ');
            if(!height.getText().toString().trim().isEmpty())
            setheightInInches(height.getText().toString().trim());


            weightUnitString = weightUnit.getText().toString().trim();
            weightString = weight.getText().toString().trim();
            if (weightUnitString.equalsIgnoreCase("lbs")&&
                    (!weightString.isEmpty())) {
                Double weightInKgs = Float.parseFloat(weightString) * 0.453592;
                weightString = String.format("%.2f", weightInKgs);
            }

            dobString = dob.getText().toString();
            genderString = gender.getText().toString();
            Log.d("myTag",firstName+ "  "+emailId + "  "+ dobString + "  "+ genderString + "  " +heightString +"  "+ weightString);

            if(firstName.equals("")|| emailId.equals("") || height.getText().toString().equals("") || weightString.equals("") || dobString.equals("") || genderString.equals("") || bloodGroup_String.equals("")||physicalActivity_string.equals(""))
            {
                Toast.makeText(getApplicationContext(),"Please fill all the data to proceed! ", Toast.LENGTH_LONG).show();
            }
            else if(!(Tools.validateEmail(emailId_edit))) {
                Tools.initCustomToast(BasicInfoActivity.this, "Please enter valid email!");
//                            etEmailID.startAnimation(AnimationUtils.loadAnimation(SignUpActivity.this,R.anim.shake));
                emailId_edit.requestFocus();
            }
            else
                {
                    requestSave();
                }



        }
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
                    Tools.initCustomToast(BasicInfoActivity.this,result.getString("msg"));
                    if(result.getInt("res")==1){
                        SharedPreferences.Editor editor = Tools.getGeneralEditor(BasicInfoActivity.this);
                        editor.putString(Constants.PROFILE_EMAIL,emailId);
                        editor.putString(Constants.PROFILE_NAME,firstName+" "+lastName);
                        editor.putString(Constants.GENDER,genderString.toLowerCase());
                        editor.commit();
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
                Tools.initNetworkErrorToast(BasicInfoActivity.this);
                Log.d("Error",error.toString());
                Log.d("myTag","I am here");
            }
        };
        Map<String, String> params = new HashMap<>();
        params.put("firstname",firstName);
        params.put("lastname",lastName);
        params.put("gender",genderString);
        params.put("dob",dobString);
        params.put("code",countryCode_string);
        params.put("weight",weightString);
        params.put("height",heightString);
        params.put("state",stateName);
        params.put("country",countryName);
        params.put("city",cityName);
        params.put("email",emailId);
        params.put("bloodgroup",bloodGroup_String);
        params.put("physicalactivity",physicalActivity_string);

        NetworkManager.getInstance(this).sendPostRequestWithHeader(Urls.save_basic_info,params,listener,errorListener,this);

    }

    ArrayList<SingleSelectionModel> physical;
    void requestFetch(){
        final AlertDialog ad = Tools.getDialog(" Please Wait...",this);
        ad.show();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ad.dismiss();
                Log.d("Response",response);
                try {
                    JSONObject basicInfoOuter = new JSONObject(response);
                    int res = basicInfoOuter.getInt("res");
                    String msg = basicInfoOuter.getString("msg");
                    if(res==1)
                    {
                        JSONObject basicInfodata = basicInfoOuter.getJSONObject("data");
                        firstName_edit.setText(basicInfodata.getString("firstname"));
                        lastname_edit.setText(basicInfodata.getString("lastname"));
                        emailId_edit.setText(basicInfodata.getString("email"));
                        countryCode.setText(basicInfodata.getString("countrycode"));
                        phoneNo_edit.setText(basicInfodata.getString("phone"));
                        country.setText(basicInfodata.getString("country"));
                        city.setText(basicInfodata.getString("city"));
                        stateName = basicInfodata.getString("state");
                        gender.setText(basicInfodata.getString("gender"));
                        dob.setText(basicInfodata.getString("dob"));
                        if(!(basicInfodata.getString("weight").equalsIgnoreCase("0")))
                        weight.setText(basicInfodata.getString("weight"));
                        if(!(basicInfodata.getString("height").equalsIgnoreCase("0")||basicInfodata.getString("height").isEmpty()))
                        height.setText(getHeightToDispay(basicInfodata.getString("height")));
                        if(basicInfodata.getString("country").trim().equalsIgnoreCase(""))
                        setupCountry(basicInfodata.getString("countrycode"));
                        else
                            country.setEnabled(false);

                        if(basicInfodata.getString("bloodgroup")!=null||!(basicInfodata.getString("bloodgroup").equals("null")))
                        bloodGroup.setText(basicInfodata.getString("bloodgroup"));
                        physical = new ArrayList<>();
                        JSONArray pa_list = basicInfodata.getJSONArray("pa_list");
                        for(int i=0;i<pa_list.length();i++){
                            JSONObject ob = pa_list.getJSONObject(i);
                            physical.add(new SingleSelectionModel(ob.getString("val"),ob.getString("text")));
                        }

                        String physicalActVal = basicInfodata.getString("physicalactivity");
                        for(int i=0;i<physical.size();i++){
                            if(physical.get(i).getId().equals(physicalActVal)){
                                physicalActivity.setText(physical.get(i).getLabel());
                                physicalActivity_string = physicalActVal;
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
                Tools.initNetworkErrorToast(BasicInfoActivity.this);
                Log.d("Error",error.toString());
                Log.d("myTag","I am here");
            }
        };
        Map<String, String> params = new HashMap<>();


        NetworkManager.getInstance(this).sendPostRequestWithoutParams(Urls.get_basic_info,listener,errorListener,this);

    }

    void setupCountry(String code){
        SqliteDbHelper helper = new SqliteDbHelper(BasicInfoActivity.this);
        ArrayList<CountryModel> countryModel = helper.getCountries();
        int flag=0;
        for (int i = 0; i < countryModel.size(); i++) {
            if(code.equalsIgnoreCase("+"+countryModel.get(i).getPhonecode()+"")){
                country.setText(countryModel.get(i).getName());
                country_code = countryModel.get(i).getId();
                flag++;
            }
//            list.add(new SingleSelectionModel(countryModel.get(i).getId() + "", "+" + countryModel.get(i).getPhonecode() + " : " + countryModel.get(i).getName()));
        }
        if(flag>1){
            country.setText("");
            country.setEnabled(true);
        }else{
            country.setEnabled(false);
        }

    }

    public String getHeightToDispay(String height)
    {
        String ft = height.split("-")[0];
        String inch =  height.split("-")[1];

        return ft+"'"+inch+"\"";
    }

    public void setheightInInches(String selected)
    {
        if (unitHeight.equals("ft. in.")) {
            String ft = selected.split("'")[0];
            String inches_a = selected.split("\"")[0];
            String inches = inches_a.split("'")[1];

            heightString = ft + "-" + inches;

        } else {
            Double x = Integer.parseInt(selected) / 2.54;

            int ft = (int) (x / 12);

            int inches = (int) (x % 12);

            heightString = ft + "-" + inches;

        }
    }

}
