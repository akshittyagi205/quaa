package com.quanutrition.app.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;
import com.quanutrition.app.R;
import com.quanutrition.app.bottomtabs.DummyFragment;

import java.util.ArrayList;
import java.util.List;

public class TimeInputActivity extends AppCompatActivity implements DummyFragment.OnFragmentInteractionListener,TravelTimeFragment.OnFragmentInteractionListener,
SleepTimeFragment.OnFragmentInteractionListener, TrainingScheduleFragment.OnFragmentInteractionListener{

    ViewPager view_pager;
    TabLayout tab_layout;
    int flagFragment = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_input);
        initComponent();
    }


    private void initComponent() {
        view_pager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(view_pager);

        tab_layout = (TabLayout) findViewById(R.id.tab_layout);
        tab_layout.setupWithViewPager(view_pager);
        view_pager.setOffscreenPageLimit(3);
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TravelTimeFragment(),"Travel Time");
        adapter.addFragment(new SleepTimeFragment(), "Sleep Time");
        adapter.addFragment(new TrainingScheduleFragment(), "Training Schedule");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            flagFragment=position+1;
            Log.d("FLag",flagFragment+"");
            return mFragmentList.get(position);

        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
