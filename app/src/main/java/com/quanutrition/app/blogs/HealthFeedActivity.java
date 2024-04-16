package com.quanutrition.app.blogs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.net.Uri;
import android.os.Bundle;

import com.quanutrition.app.R;
import com.quanutrition.app.appointment.UpcomingAppointmentFragment;

public class HealthFeedActivity extends AppCompatActivity implements BlogsFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_feed);

        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        if (getIntent().getStringExtra("type").equalsIgnoreCase("2")){
            getSupportActionBar().setTitle("Videos");
        }else {
            getSupportActionBar().setTitle("Blog Posts");
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = BlogsFragment.newInstance(getIntent().getStringExtra("type"));
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.replace(R.id.main_fragment_frame, fragment).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
