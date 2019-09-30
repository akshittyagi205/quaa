package com.quanutrition.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.quanutrition.app.Utils.Tools;
import com.quanutrition.app.general.SignInActivity;

public class HandleTheFuckingLink extends AppCompatActivity {

    TextView textview,text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_the_fucking_link);
        Tools.setSystemBarColorCustom(this,R.color.colorPrimary);
        textview = findViewById(R.id.data_link_id);
        text = findViewById(R.id.text);

        String t="";
        if(!getIntent().getBooleanExtra("is_user",false)){
           t = getIntent().getStringExtra("d_name")+" Just Referred you to start your health journey";
        }else
        t = getIntent().getStringExtra("name")+" just referred you to "+getIntent().getStringExtra("d_name");

        text.setText(t);

        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(HandleTheFuckingLink.this, SignInActivity.class);
                intent.putExtra("dietitianId",getIntent().getStringExtra("d_id"));
                intent.putExtra("user_ref",getIntent().getStringExtra("user_ref"));
                intent.putExtra("dietitianCode",getIntent().getStringExtra("dietitian_ref"));
                startActivity(intent);
            }
        });


        /*FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();

                            Log.d("Recieved Link", String.valueOf(deepLink));
                            textview.setText(String.valueOf(deepLink) + "        " +  deepLink.getQueryParameter("user"));
                        }


                        // Handle the deep link. For example, open the linked
                        // content, or apply promotional credit to the user's
                        // account.
                        // ...

                        // ...
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("mytag", "getDynamicLink:onFailure", e);
                    }
                });*/


    }
}
