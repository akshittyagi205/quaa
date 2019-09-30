package com.quanutrition.app.payments;

import android.app.Activity;
import android.content.DialogInterface;
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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.quanutrition.app.MainActivity;
import com.quanutrition.app.R;
import com.quanutrition.app.Utils.Constants;
import com.quanutrition.app.Utils.NetworkManager;
import com.quanutrition.app.Utils.Tools;
//import com.razorpay.Checkout;
//import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;

public class CheckoutActivity extends AppCompatActivity
//        implements PaymentResultListener
{

    String programId,paymentId,amount,duration,days,finalAmount,discount,name,currency;
    boolean has_discount;
    LinearLayout couponDiscountLayout,promoLayout;
    EditText code;
    float couponDiscountAmount=0;
    String promoId="0";
    TextView txtPlan,txtDuration,amount_initial,discount_amount,couponDiscount,amount_total,apply,error,pay_now;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setTitle("Checkout");
//        Tools.setSystemBarColor(this,R.color.colorPrimary);

        getExtras();
//        Checkout.preload(getApplicationContext());
        txtPlan = findViewById(R.id.txtPlan);
        txtDuration = findViewById(R.id.txtDuration);
        amount_initial = findViewById(R.id.amount_initial);
        discount_amount = findViewById(R.id.discount_amount);
        couponDiscount = findViewById(R.id.couponDiscount);
        amount_total = findViewById(R.id.amount_total);
        couponDiscountLayout = findViewById(R.id.couponDiscountLayout);
        code = findViewById(R.id.code);
        apply = findViewById(R.id.apply);
        error = findViewById(R.id.error);
        promoLayout = findViewById(R.id.promoLayout);
        pay_now = findViewById(R.id.pay_now);

        error.setVisibility(View.GONE);
        couponDiscountLayout.setVisibility(View.GONE);

        txtPlan.setText(name);
        txtDuration.setText(duration);
        amount_initial.setText(amount+" "+currency);
        discount_amount.setText(discount+" "+currency);
        amount_total.setText(finalAmount+" "+currency);


        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(apply.getText().toString().equalsIgnoreCase("Cancel")){
                    code.setEnabled(true);
                    code.setText("");
                    code.requestFocus();
                    float am = Float.parseFloat(finalAmount);
                    am += couponDiscountAmount;
                    finalAmount = am+"";
                    apply.setText("APPLY");
                    code.setAlpha(1.0f);
                    couponDiscountLayout.setVisibility(View.GONE);
                    amount_total.setText(finalAmount+" "+currency);
                    couponDiscountAmount=0;
                    promoId="0";
                }else {
                    if (Tools.validateNormalText(code)) {
                        applyCoupon();
                    }
                }
            }
        });

        pay_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startPayment();
            }
        });

    }


//    void startPayment(){
//        Checkout checkout = new Checkout();
//        checkout.setImage(R.mipmap.ic_launcher);
//        final Activity activity = this;
//        try {
//            JSONObject options = new JSONObject();
//            options.put("name", Tools.getGeneralSharedPref(this).getString(Constants.CLINIC,"Dietitio"));
//            options.put("description",name);
//            options.put("currency", currency);
//
//            JSONObject prefill = new JSONObject();
//            prefill.put("email",Tools.getGeneralSharedPref(this).getString(Constants.PROFILE_EMAIL,""));
//            prefill.put("contact",Tools.getGeneralSharedPref(this).getString(Constants.PHONE,""));
//
//            options.put("prefill",prefill);
//
//            JSONArray notes = new JSONArray();
//            notes.put("Clinic name : "+Tools.getGeneralSharedPref(this).getString(Constants.CLINIC,"-"));
//            notes.put("Program :"+name);
//            notes.put("Promo Id : "+programId);
//            notes.put("Duration : "+days);
//
//            options.put("notes",notes);
//
//            DecimalFormat twoDForm = new DecimalFormat("#.##");
//            double amount = Double.valueOf(twoDForm.format(Float.parseFloat(finalAmount)));
//
//            options.put("amount", ((float)amount*100)+"");
//
////            options.put("amount", (Float.parseFloat("1")*100)+"");
//            checkout.setFullScreenDisable(true);
////            Checkout.clearUserData(Context)
//            checkout.open(activity, options);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }


    void getExtras(){
        Bundle extras = getIntent().getExtras();
        programId = extras.getString("programId");
        paymentId = extras.getString("paymentId");
        amount = extras.getString("amount");
        duration = extras.getString("duration");
        currency = extras.getString("currency");
        days = extras.getString("days");
        discount = extras.getString("discount");
        name = extras.getString("name");
        has_discount = extras.getBoolean("has_discount");

        finalAmount = String.valueOf(Float.parseFloat(amount)- Float.parseFloat(discount));
    }

    void applyCoupon() {
        final AlertDialog ad = Tools.getDialog("Applying Promo...", this);
        ad.show();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ad.dismiss();
                try {
                    JSONObject ob = new JSONObject(response);
                    if (ob.getInt("res") == 1) {
                        error.setVisibility(View.GONE);
                        couponDiscountLayout.setVisibility(View.VISIBLE);
                        code.setEnabled(false);
                        code.setAlpha(0.8f);
                        apply.setText("CANCEL");
                        finalAmount = getFinalAmount(ob.getInt("tag"), Integer.parseInt(ob.getString("claim"))) + "";
                        promoId = ob.getInt("promoId") + "";
                        amount_total.setText(finalAmount+" "+ currency);
                        couponDiscountLayout.setVisibility(View.VISIBLE);
                        couponDiscount.setText(couponDiscountAmount+" "+ currency);

                    } else {
                        error.setVisibility(View.VISIBLE);
                        error.setText(ob.optString("msg"));
                        //Remove any kind of discount if applied before
                    }
                    Log.d("response", response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("RESponse", response);

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("RESPONSE", "ERROR");
                ad.dismiss();
                Tools.initNetworkErrorToast(CheckoutActivity.this);
            }
        };

        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("promo_code", code.getText().toString().trim());
        paramMap.put("packageId", programId);
        paramMap.put("duration", duration);

        NetworkManager.getInstance(this).sendPostRequestWithHeader(Urls.apply_promo,paramMap,listener,errorListener,this);
    }

    float getFinalAmount(int tag,int discount){
        float previousAmt = Float.parseFloat(finalAmount);
        Log.d("Previous amount",previousAmt+"");
        Log.d("Str amount",finalAmount);
        if(tag==2){
            couponDiscountAmount = (float)((float)discount/100)*previousAmt;
            Log.d("discount",couponDiscount+"");
        }else{
            couponDiscountAmount = discount;
        }

        DecimalFormat twoDForm = new DecimalFormat("#.##");
        double amount = Double.valueOf(twoDForm.format(previousAmt-couponDiscountAmount));

        return (float)amount;
    }

    /*@Override
    public void onPaymentSuccess(String s) {
        Log.d("Response Success",s);
        savePayment(s);
    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.d("Response Error",s);
        String message = "";
        if(i == Checkout.NETWORK_ERROR){
            message = "Payment was cancelled due to network error! Please check your internet connection and try again.";
        }else if(i == Checkout.PAYMENT_CANCELED){
            message = "Payment cancelled! Please try again later!";
        }else if(i == Checkout.INVALID_OPTIONS){
            message = "Some unknown error occurred! Please try again later!";
        }else{
            message = "Something went wrong. Please contact your dietitian.";
        }

        showDialog(message);
    }*/


    void showDialog(String message) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        // Setting Dialog Message
        alertDialog.setTitle("Error!");
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                startPayment();
            }
        });
        alertDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.show();
    }

    void savePayment(String transactionId) {
        final AlertDialog ad = Tools.getDialog("Verifying Payment...", this);
        ad.show();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ad.dismiss();
                try {
                    JSONObject ob = new JSONObject(response);
                    if (ob.getInt("res") == 1) {
                        finish();
                        startActivity(new Intent(CheckoutActivity.this, MainActivity.class));
                    }
                    Tools.initCustomToast(CheckoutActivity.this,ob.getString("msg"));
                    Log.d("response", response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("RESponse", response);

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("RESPONSE", "ERROR");
                ad.dismiss();
                Tools.initNetworkErrorToast(CheckoutActivity.this);
            }
        };

        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("programId", programId);
        paramMap.put("duration", days);
        paramMap.put("amount",finalAmount);
        paramMap.put("promoId",promoId);
        paramMap.put("transactionId",transactionId);
        paramMap.put("couponDiscount",couponDiscountAmount+"");
        paramMap.put("userDiscount",discount);
        paramMap.put("paymentId",paymentId);
        if(getIntent().hasExtra("is_extended"))
            paramMap.put("isExtend","1");
        if(getIntent().hasExtra("is_renew"))
            paramMap.put("isRenew","1");


        NetworkManager.getInstance(this).sendPostRequestWithHeader(Urls.save_payment,paramMap,listener,errorListener,this);
    }


}
