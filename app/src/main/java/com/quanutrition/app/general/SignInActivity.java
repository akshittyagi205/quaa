package com.quanutrition.app.general;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.quanutrition.app.BuildConfig;
import com.quanutrition.app.MainActivity;
import com.quanutrition.app.R;
import com.quanutrition.app.Utils.Constants;
import com.quanutrition.app.Utils.NetworkManager;
import com.quanutrition.app.Utils.SqliteDbHelper;
import com.quanutrition.app.Utils.Tools;
import com.quanutrition.app.selectiondialogs.CountryModel;
import com.quanutrition.app.selectiondialogs.DialogUtils;
import com.quanutrition.app.selectiondialogs.SingleSelectionModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {

    TextView pin;
    EditText phone;
    LinearLayout proceed;
    CheckBox agree;
    int country_code = 101;
    int time=1;
    AlertDialog alertDialog1;
    private CountDownTimer countDownTimer;
    TextView resend_code,timer_time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
//        Tools.setSystemBarColorCustom(this,R.color.colorPrimary);
        pin = findViewById(R.id.pin);
        phone = findViewById(R.id.phone);
        proceed = findViewById(R.id.proceed);
//        agree = findViewById(R.id.agree);
        pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SqliteDbHelper helper = new SqliteDbHelper(SignInActivity.this);
                ArrayList<CountryModel> countryModel = helper.getCountries();
                ArrayList<SingleSelectionModel> list = new ArrayList<>();
                for(int i=0;i<countryModel.size();i++){
                    list.add(new SingleSelectionModel(countryModel.get(i).getId()+"","+"+countryModel.get(i).getPhonecode()+" : "+countryModel.get(i).getName()));
                }

                DialogUtils.getSingleSearchDialog(SignInActivity.this, list, new DialogUtils.OnSingleItemSelectedListener() {
                    @Override
                    public void onItemSelected(int position, SingleSelectionModel item) {
                        pin.setText(item.getLabel().split(":")[0].trim());
                        country_code = Integer.parseInt(item.getId());
                    }
                });
            }
        });
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if (pin.getText().toString().equalsIgnoreCase("+91")) {
                        if (phone.getText().toString().trim().length() == 10 && PhoneNumberUtils.isGlobalPhoneNumber(phone.getText().toString().trim())) {
                            //valid indian number
                            checkLayout();
                        } else {
                            Tools.initCustomToast(SignInActivity.this, "Please enter a valid number");
                        }
                    } else {
                        //valid international number
                        checkLayout();
                    }

            }
        });
    }

    void checkLayout(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        // Setting Dialog Message
        alertDialog.setTitle("Verify number");
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setMessage("We will be verifying the phone number : \n" + pin.getText() + " " + phone.getText().toString().trim() + "\nYou will receive an OTP on this number.");
        alertDialog.setPositiveButton("Verify", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                                    Tools.initCustomToast(getActivity(),"Verify");
                time=1;
                requestOTP(pin.getText().toString(),phone.getText().toString().trim());
//                                    showDialog(pin.getText()+" "+editPhoneNumber.getText().toString().trim());
            }
        });
        alertDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.show();
    }


    void requestOTP(final String code, final String Phone){
        final AlertDialog ad = Tools.getDialog("Requesting OTP...",this);
        ad.show();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ad.dismiss();
                try {
                    JSONObject result = new JSONObject(response);
                    if(result.getInt("res")==1){
//                        user_created_status=result.getInt("status");
//                        Tools.initCustomToast(SignInActivity.this,result.getString("otp"));
                        Tools.getGeneralEditor(SignInActivity.this).putString(Constants.USER_ID,result.getInt("id")+"").commit();
                        showDialog(Phone,code,result.getInt("status"),result.getString("otp"),result.getString("type"));

                    }else{
                        Tools.initCustomToast(SignInActivity.this,"Some error occured! Try again later.");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("Response",response);
                Log.d("myTag","I am here");

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ad.dismiss();
                Tools.initNetworkErrorToast(SignInActivity.this);
                Log.d("Error",error.toString());
                Log.d("myTag","I am here");
            }
        };
        Map<String, String> params = new HashMap<>();
        params.put("mobile",code+"-"+Phone);

        NetworkManager.getInstance(this).sendPostRequest(Urls.Request_OTP,params,listener,errorListener,this);

    }


    LinearLayout checkAgree;

    void showDialog(final String PhoneNumber, final String code, final int status, String otpText, final String type){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater linf = LayoutInflater.from(this);
        final View inflator = linf.inflate(R.layout.dialog_otp_verify, null);
        alertDialog.setView(inflator);
        alertDialog.setCancelable(false);
        final Button verify = inflator.findViewById(R.id.btnVerify);
        final TextView phone = inflator.findViewById(R.id.phone);
        ImageView close = inflator.findViewById(R.id.close);
        resend_code = inflator.findViewById(R.id.resend_code);
        checkAgree = inflator.findViewById(R.id.checkAgree);
        agree = inflator.findViewById(R.id.agree);

        if(status==1){
            checkAgree.setVisibility(View.VISIBLE);
        }else{
            checkAgree.setVisibility(View.GONE);
        }
        timer_time = inflator.findViewById(R.id.timer_time);
        resend_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                    time = 1;
                    requestOTP(code, PhoneNumber);
                    if(countDownTimer!=null)
                        countDownTimer.cancel();
                    alertDialog1.dismiss();

            }
        });
        timer();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog1.dismiss();
                if(countDownTimer!=null)
                    countDownTimer.cancel();
            }
        });
        final EditText otp = inflator.findViewById(R.id.otp);
        phone.setText("Verify "+code+PhoneNumber);

        /*if(BuildConfig.DEBUG)
            otp.setText(otpText);*/

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Tools.validateNormalText(otp)){
                    if(otp.getText().toString().trim().length()!=6){
                        Tools.initCustomToast(SignInActivity.this,"Please enter valid otp!");
                    }else{
                        //Send Request to server
                        if(agree.isChecked()||status==0) {
                        alertDialog1.dismiss();
                        verifyOTP(otp.getText().toString().trim(),type);
                    }else{
                        Tools.initCustomToast(SignInActivity.this,"Please agree to the terms and conditions before continuing!");
                    }
                    }
                }else{
                    Tools.initCustomToast(SignInActivity.this,"Please enter the otp!");
                }
            }
        });
        alertDialog1 = alertDialog.show();
        alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void timer(){
        resend_code.setTextColor(getResources().getColor(R.color.grey_400));
        long milli;
        if(time<3) {
            milli = 30000 * time;
        }else{
            milli = 5*60*60*1000*time;
        }
        time++;
        countDownTimer = new CountDownTimer(milli, 1000) {

            public void onTick(long millisUntilFinished) {
                resend_code.setClickable(false);
                timer_time.setText(""+(millisUntilFinished % 60000 / 1000));

            }

            public void onFinish() {
                resend_code.setClickable(true);
                resend_code.setTextColor(getResources().getColor(R.color.colorAccent));
            }

        }.start();
    }

    void verifyOTP(final String otp, final String type){
        final AlertDialog ad = Tools.getDialog("Verifying OTP...",this);
        ad.show();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ad.dismiss();
                try {
                    Log.d("Response", response);
                    JSONObject result = new JSONObject(response);
                    if (result.getInt("res") == 1) {
                        if(type.equalsIgnoreCase("1")){
                        SharedPreferences.Editor editor = Tools.getGeneralEditor(SignInActivity.this);
                        JSONObject data = result.getJSONObject("data");
                        editor.putString(Constants.AUTH_TOKEN,data.optString("token"));
                        JSONObject user = data.getJSONObject("user");
                        editor.putString(Constants.PROFILE_EMAIL, user.optString("email"));
                        editor.putString(Constants.PROFILE_NAME, user.optString("name", "-"));
                        editor.putString(Constants.USER_ID,user.getInt("userId")+"");
                        editor.putString(Constants.PHONE, user.optString("phone"));
//                        editor.putString(Constants.REFER_CODE,user.getString("user_code"));
                        editor.putString(Constants.PROFILE_IMAGE, user.optString("photo"));
//                        editor.putString(Constants.CUSTOM_ID, user.optString("custom_id"));
                        if(data.getInt("dietitian_status")==1) {
                            editor.putString(Constants.GENDER,user.getString("gender").toLowerCase());

                            JSONObject dietitian = data.optJSONObject("dietitian");
                            editor.putString(Constants.DIETITIAN_NAME, dietitian.optString("name"));
                            editor.putString(Constants.DIETITIAN_PIC, dietitian.getString("photo"));
                            editor.putString(Constants.DIETITIAN_ID, dietitian.optInt("dietitianId", 0) + "");
                            editor.putString(Constants.DIETITIAN_PHONE, dietitian.getString("phone"));
//                            editor.putString(Constants.CLINIC, dietitian.getString("clinic"));
//                            editor.putString(Constants.CLINIC_ID, dietitian.optString("clinicId", "0"));
//                            editor.putString(Constants.DIETITIAN_EXPERIENCE, dietitian.getString("exp"));
//                            editor.commit();
//                            finish();
//                            startActivity(new Intent(SignInActivity.this, MainActivity.class));
                        }
                            editor.commit();
                            //Call to temp info activity

                                Intent intent = new Intent(SignInActivity.this,MainActivity.class);
                                finish();
                                startActivity(intent);
                            }else {
                                Intent intent = new Intent(SignInActivity.this, SignUpInfo.class);
                                if (getIntent().hasExtra("user_ref")) {
                                    intent.putExtra("user_ref", getIntent().getStringExtra("user_ref"));
                                }
                                finish();
                                startActivity(intent);
                            }

                    }else{
                        Tools.initCustomToast(SignInActivity.this,result.getString("msg"));
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
//                Log.d("Response",response);
                Log.d("myTag","I am here");
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ad.dismiss();
                Tools.initNetworkErrorToast(SignInActivity.this);
                Log.d("Error",error.toString());
                Log.d("myTag","I am here");
            }
        };
        Map<String, String> params = new HashMap<>();
        params.put("userId",Tools.getGeneralSharedPref(this).getString(Constants.USER_ID,"0"));
        params.put("otp",otp);
        params.put("type",type);

        NetworkManager.getInstance(this).sendPostRequest(Urls.Verify_OTP,params,listener,errorListener,this);

    }

}
