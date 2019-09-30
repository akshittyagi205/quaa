package com.quanutrition.app.payments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.quanutrition.app.programs.ProgramsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PaymentHistory extends AppCompatActivity {

    RecyclerView payment_history_rv;
    ArrayList<PaymentHistoryModel> paymentList;
    PaymentHistoryAdapter paymentHistoryAdapter;
    LinearLayout browsePlans;
    RelativeLayout noData;
    TextView basicBtn;
    Context context;
    boolean isRenewalAvailable, isExtentionAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setTitle("Payment History");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;
        payment_history_rv = findViewById(R.id.payment_history_rv_id);
        browsePlans = findViewById(R.id.browsePlans);
        basicBtn = findViewById(R.id.basicBtn);
        noData = findViewById(R.id.noData);
        paymentList = new ArrayList<>();
        paymentHistoryAdapter = new PaymentHistoryAdapter(paymentList, context, new PaymentHistoryAdapter.OnItemClicked() {
            @Override
            public void onExtension(View view, int position) {

                Intent intent = new Intent(PaymentHistory.this,ExtendDurationActivity.class);
                intent.putExtra("id",paymentList.get(position).getProgramId());
                startActivity(intent);
            }

            @Override
            public void onRenewal(View v, int position) {
                getRenewInfo(paymentList.get(position).getProgramId());
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        payment_history_rv.setLayoutManager(layoutManager);
        payment_history_rv.setAdapter(paymentHistoryAdapter);

        requestFetch();

        browsePlans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PaymentHistory.this, ProgramsActivity.class));
            }
        });

        basicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentHistory.this, ProgramsActivity.class));
            }
        });

        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                browsePlans.setVisibility(View.GONE);
            }
        });

    }



    void requestFetch(){

        final AlertDialog ad = Tools.getDialog("Requesting ..",this);
        ad.show();

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
                        JSONArray paymentsArray = result.getJSONArray("data");
                        for(int i =0;i<paymentsArray.length();i++) {
                            JSONObject paymentObject = paymentsArray.getJSONObject(i);
                            String programName = paymentObject.getString("name");
                            String duration = paymentObject.getString("duration");
                            String startDate = paymentObject.getString("start_date");
                            String amount = paymentObject.getString("total_amount");
//                            String paidAmount = paymentObject.getString("paid_amount");
//                            String mode = paymentObject.getString("mode");
//                            String discount = paymentObject.getString("discount");
                            String currency = paymentObject.getString("currency");
                            String programId = paymentObject.getString("id");
                            boolean status = paymentObject.optBoolean("payment_status");
                            /*isExtentionAvailable = paymentObject.optBoolean("extensible",false);
                            isRenewalAvailable = paymentObject.optBoolean("renewable",false);*/

                            isExtentionAvailable = false;
                            isRenewalAvailable = false;
                            String time="";
                            if(duration.equalsIgnoreCase("1")){
                                time = duration+" Day";
                            }else{
                                time = duration+" Days";
                            }

                            ArrayList<PaymentReceiptModel> receiptList = new ArrayList<>();
                            JSONArray recipts = paymentObject.getJSONArray("receipt");
                            for(int j=0;j<recipts.length();j++){
                                JSONObject ob = recipts.getJSONObject(j);
                                receiptList.add(new PaymentReceiptModel(ob.getString("amount")+" "+currency,ob.getString("mode"),ob.getString("date")));
                            }

                            PaymentHistoryModel model = new PaymentHistoryModel(programName, time,amount+" "+currency,"","",programId,startDate,isExtentionAvailable,isRenewalAvailable);
                            model.setReceipts(receiptList);
                            paymentList.add(model);

                        }
                        paymentHistoryAdapter.notifyDataSetChanged();
                        if(paymentList.size()>0){
                            noData.setVisibility(View.GONE);
                        }else{
                            noData.setVisibility(View.VISIBLE);
                        }

                    }
                    else
                    {
                        noData.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(),"No Payments yet!", Toast.LENGTH_LONG).show();
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
                Tools.initNetworkErrorToast(PaymentHistory.this);
                noData.setVisibility(View.VISIBLE);
                Log.d("Error",error.toString());
                Log.d("myTag","I am here");
            }
        };

        // params.put("cuisine",);
        // params.put("gender",genderString);

        NetworkManager.getInstance(this).sendGetRequest(Urls.payment_history,listener,errorListener,this);

    }

    void getRenewInfo(final String paymentId){

        final AlertDialog ad = Tools.getDialog("Requesting...",this);
        ad.show();

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
                        JSONObject data = result.getJSONObject("data");
                        Intent intent = new Intent(PaymentHistory.this, CheckoutActivity.class);
                        intent.putExtra("paymentId",paymentId);
                        intent.putExtra("name",data.getString("name"));
                        intent.putExtra("duration",data.getString("plan"));
                        intent.putExtra("days",data.getString("duration"));
                        intent.putExtra("amount",data.getString("price")+"");
                        intent.putExtra("currency",data.getString("currency"));
                        intent.putExtra("discount","0");
                        intent.putExtra("is_renew","1");
                        intent.putExtra("has_discount",false);
                        intent.putExtra("programId",data.getInt("id")+"");
                        startActivity(intent);
                    }
                    else
                    {
                        Tools.initNetworkErrorToast(PaymentHistory.this);
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
                Tools.initNetworkErrorToast(PaymentHistory.this);
                Log.d("Error",error.toString());
                Log.d("myTag","I am here");
            }
        };

        // params.put("cuisine",);
        // params.put("gender",genderString);
        String url = Urls.get_renew_info+"?paymentId="+paymentId;
        NetworkManager.getInstance(this).sendGetRequest(url,listener,errorListener,this);

    }
}
