package com.quanutrition.app.general;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.quanutrition.app.MainActivity;
import com.quanutrition.app.R;
import com.quanutrition.app.Utils.Constants;
import com.quanutrition.app.Utils.NetworkManager;
import com.quanutrition.app.Utils.Tools;
import com.quanutrition.app.profile.BasicInfoActivity;
import com.quanutrition.app.selectiondialogs.DialogUtils;
import com.quanutrition.app.selectiondialogs.SingleSelectionModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SignUpInfo extends AppCompatActivity implements View.OnClickListener {


    EditText firstName_edit, lastname_edit, goal_edit, gender_edit,dietitian_code,ref_code,dob;
    RadioGroup refRG;
    RadioButton yes,no;
    TextView save;
    ImageView femaleIcon,maleIcon;
    TextView femaleText,maleText;
    String firstName,goal;
    int female_flag = 0, male_flag = 0;
    String gender = "";

    LinearLayout male_view, female_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_info);
//        Tools.setSystemBarColorDark(this,R.color.colorPrimary);

        firstName_edit = findViewById(R.id.editFirstName);
        lastname_edit = findViewById(R.id.editLastName);
        male_view = findViewById(R.id.male);
        female_view = findViewById(R.id.female);findViewById(R.id.referCode).setVisibility(View.GONE);
        goal_edit = findViewById(R.id.goal_choose);
        save = findViewById(R.id.save_signUpInfo_button);
        femaleIcon = findViewById(R.id.female_icon);
        maleIcon = findViewById(R.id.male_icon);
        dob = findViewById(R.id.dob);
        femaleText = findViewById(R.id.female_text);
        maleText = findViewById(R.id.male_text);
        dietitian_code = findViewById(R.id.dietitian_code);
        ref_code = findViewById(R.id.ref_code);
        refRG = findViewById(R.id.refRG);
        yes = findViewById(R.id.yes);
        no = findViewById(R.id.no);


       /* refRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton  = findViewById(refRG.getCheckedRadioButtonId());
                if(radioButton.getText().toString().equalsIgnoreCase("yes")){
                    ref_code.setVisibility(View.VISIBLE);
                    dietitian_code.setText("");
                    dietitian_code.setVisibility(View.GONE);
                }else{
                    dietitian_code.setVisibility(View.VISIBLE);
                    ref_code.setText("");
                    ref_code.setVisibility(View.GONE);
                }
            }
        });*/

        findViewById(R.id.referCode).setVisibility(View.GONE);

        ref_code.setText(Tools.getGeneralSharedPref(this).getString("user_ref",""));
        yes.setChecked(true);

        if(!(Tools.getGeneralSharedPref(this).getString("user_ref","").equalsIgnoreCase(""))){

            ref_code.setVisibility(View.GONE);
        }

        goal_edit.setFocusable(false);
        dob.setFocusable(false);
        goal_edit.setOnClickListener(this);
        save.setOnClickListener(this);
        female_view.setOnClickListener(this);
        male_view.setOnClickListener(this);
        dob.setOnClickListener(this);



    }




    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id == R.id.save_signUpInfo_button) {

             goal = goal_edit.getText().toString().toLowerCase();
            firstName= firstName_edit.getText().toString();


            if(goal.equalsIgnoreCase("")|| firstName.equalsIgnoreCase("")||Tools.getText(dob).isEmpty())
            {
                Tools.initCustomToast(SignUpInfo.this,"Please Fill all the details to continue");

            }
            else {
                /*RadioButton radioButton  = findViewById(refRG.getCheckedRadioButtonId());
                if(radioButton.getText().toString().equalsIgnoreCase("yes")&&(ref_code.getText().toString().trim().isEmpty())){
                    Log.d("In if","yes Selected");
                    Tools.initCustomToast(SignUpInfo.this,"Please Enter Referral Code!");
                }else if(radioButton.getText().toString().equalsIgnoreCase("no")&&(dietitian_code.getText().toString().trim().isEmpty())){
                    Log.d("In if","yes Selected");
                    Tools.initCustomToast(SignUpInfo.this,"Please Enter Dietitian Code!");
                }else {
                    if(radioButton.getText().toString().equalsIgnoreCase("yes"))
                    requestFetch("1");
                    else
                        requestFetch("0");

                }*/
                requestFetch("0");
            }

        }
        if(id== R.id.goal_choose)
        {
            DialogUtils.getSingleSelectionDialog(this, DialogUtils.getSingleArrayListWithResource(this, R.array.goal), new DialogUtils.OnSingleItemSelectedListener() {
                @Override
                public void onItemSelected(int position, SingleSelectionModel item) {
                    goal_edit.setText(item.getLabel());
                }
            });
        }
        if(id==R.id.female)
        {

            if(gender.equalsIgnoreCase("male"))
            {

                femaleIcon.setColorFilter(getResources().getColor(R.color.pink_200), PorterDuff.Mode.SRC_IN);
                femaleText.setTextColor(getResources().getColor(R.color.pink_200));
                maleIcon.setColorFilter(getResources().getColor(R.color.dark_gray), PorterDuff.Mode.SRC_IN);
                maleText.setTextColor(getResources().getColor(R.color.dark_gray));


                gender = "female";

            }
            else
            {
                femaleIcon.setColorFilter(getResources().getColor(R.color.pink_200), PorterDuff.Mode.SRC_IN);
                femaleText.setTextColor(getResources().getColor(R.color.pink_200));
                gender = "female";
            }



        }
        if(id==R.id.male)
        {
            if(gender.equalsIgnoreCase("female"))
            {


                femaleIcon.setColorFilter(getResources().getColor(R.color.dark_gray), PorterDuff.Mode.SRC_IN);
                femaleText.setTextColor(getResources().getColor(R.color.dark_gray));
                maleIcon.setColorFilter(getResources().getColor(R.color.blue_200), PorterDuff.Mode.SRC_IN);
                maleText.setTextColor(getResources().getColor(R.color.blue_200));
                gender = "male";

            }
            else
            {
                maleIcon.setColorFilter(getResources().getColor(R.color.blue_200), PorterDuff.Mode.SRC_IN);
                maleText.setTextColor(getResources().getColor(R.color.blue_200));
                gender = "male";
            }

        }

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
                                Tools.initCustomToast(SignUpInfo.this,"Age cannot be less than 5 years!");
                            }else {
                                dob.setText(Date1);
                            }

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setSpinnersShown(true);
            datePickerDialog.getDatePicker().setCalendarViewShown(false);
            datePickerDialog.show();
        }
    }


    void requestFetch(String flag) {
        final AlertDialog ad = Tools.getDialog("Please Wait...", this);
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
                    if (res == 1) {
                        Log.d("manya",response);
                        Tools.initCustomToast(getApplicationContext(), msg);
                        SharedPreferences.Editor editor = Tools.getGeneralEditor(SignUpInfo.this);
                        JSONObject data = basicInfoOuter.getJSONObject("data");
                        JSONObject user = data.getJSONObject("user");
                        editor.putString(Constants.USER_ID,user.getInt("userId")+"");
                        editor.putString(Constants.PHONE, user.optString("phone"));
                        editor.putString(Constants.GENDER,gender);
                        editor.putString(Constants.PROFILE_EMAIL, user.optString("email"));
                        editor.putString(Constants.PROFILE_NAME, user.optString("user_name", "-"));
                        editor.putString(Constants.PROFILE_IMAGE, user.optString("photo"));
                        if(data.getInt("dietitian_status")==1) {

                            JSONObject dietitian = data.optJSONObject("dietitian");
                            editor.putString(Constants.DIETITIAN_NAME, dietitian.optString("name"));
                            editor.putString(Constants.DIETITIAN_PIC, dietitian.getString("photo"));
                            editor.putString(Constants.DIETITIAN_ID, dietitian.optInt("dietitianId", 0) + "");
                            editor.putString(Constants.DIETITIAN_PHONE, dietitian.getString("phone"));
//                            editor.putString(Constants.CLINIC, dietitian.getString("clinic"));
//                            editor.putString(Constants.CLINIC_ID, dietitian.optString("clinicId", "0"));
//                            editor.putString(Constants.DIETITIAN_EXPERIENCE, dietitian.getString("exp"));
                        }
                        editor.commit();
                        finish();
                        startActivity(new Intent(SignUpInfo.this, MainActivity.class));

                    } else {
                        Tools.initCustomToast(getApplicationContext(),basicInfoOuter.getString("msg"));
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
                Tools.initNetworkErrorToast(getApplicationContext());
                Log.d("Error", error.toString());
                Log.d("myTag", "I am here");
            }
        };
        Map<String, String> params = new HashMap<>();
        params.put("first_name",firstName);
        params.put("last_name",lastname_edit.getText().toString());
        params.put("gender",gender);
        params.put("goal",goal);
        params.put("dob",Tools.getText(dob));
        params.put("dietitianCode",dietitian_code.getText().toString());
        params.put("ref_code",ref_code.getText().toString());
        params.put("is_ref",flag);


        NetworkManager.getInstance(this).sendPostRequestWithHeader(Urls.save_signUp_info, params,listener, errorListener, this);

    }

}


