package com.quanutrition.app.general;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.quanutrition.app.R;
import com.quanutrition.app.Utils.Constants;
import com.quanutrition.app.Utils.Tools;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClientProfile extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    CircleImageView image;
    TextView name,plan,planDuration;

    ArrayList<InclusionsModel> list;
    InclusionsAdapter adapter;
    RecyclerView re;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        image = findViewById(R.id.image);
        name = findViewById(R.id.name);
        plan = findViewById(R.id.plan);
        planDuration = findViewById(R.id.planDuration);
        re = findViewById(R.id.re);

        sharedPreferences = Tools.getGeneralSharedPref(this);

        Tools.loadProfileImage(sharedPreferences.getString(Constants.PROFILE_IMAGE,"ksdmskdmkd"),image);
        name.setText(sharedPreferences.getString(Constants.PROFILE_NAME,""));
        plan.setText("Plan : Weight Loss/ Sports Diet");
        planDuration.setText("Duration : 2 Months");

        list = new ArrayList<>();

        list.add(new InclusionsModel("One on One consultation","",true));
        list.add(new InclusionsModel("Follow up Call 1","",true));
        list.add(new InclusionsModel("Follow up Call 2","",false));
        list.add(new InclusionsModel("NCE Delivery","",false));

        adapter = new InclusionsAdapter(list,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        re.setLayoutManager(layoutManager);
        re.setAdapter(adapter);
    }
}
