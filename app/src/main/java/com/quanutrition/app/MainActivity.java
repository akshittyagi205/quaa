package com.quanutrition.app;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.quanutrition.app.Utils.Constants;
import com.quanutrition.app.Utils.MyAlarmManager;
import com.quanutrition.app.Utils.NetworkManager;
import com.quanutrition.app.Utils.SampleBootReciever;
import com.quanutrition.app.Utils.Tools;
import com.quanutrition.app.blogs.BlogsFragment;
import com.quanutrition.app.blogs.RecipesFragment;
import com.quanutrition.app.blogs.SocialMediaGridActivity;
import com.quanutrition.app.bottomtabs.DashBoardFragment;
import com.quanutrition.app.bottomtabs.MoreFragment;
import com.quanutrition.app.bottomtabs.ProgressFragment;
import com.quanutrition.app.chat.ChatActivity;
import com.quanutrition.app.diet.DietPlanFragment;
import com.quanutrition.app.diet.DietPlanViewActivity;
import com.quanutrition.app.firebaseUtils.FirebaseUtils;
import com.quanutrition.app.general.AboutDietitianActivity;
import com.quanutrition.app.general.ContactUsActivity;
import com.quanutrition.app.general.FeedbackUtils;
import com.quanutrition.app.general.ReferActivity;
import com.quanutrition.app.general.SettingsActivity;
import com.quanutrition.app.general.SignInActivity;
import com.quanutrition.app.general.Urls;
import com.quanutrition.app.googlefit.GoogleFitUtils;
import com.quanutrition.app.profile.MeasurementsActivity;
import com.quanutrition.app.waterintake.WaterReminderReciever;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DashBoardFragment.OnFragmentInteractionListener, ProgressFragment.OnFragmentInteractionListener,
        MoreFragment.OnFragmentInteractionListener, RecipesFragment.OnFragmentInteractionListener, DietPlanFragment.OnFragmentInteractionListener, View.OnClickListener {

    TabLayout tab_layout;
    Fragment fragment;
    FragmentTransaction transaction;
    FragmentManager fragmentManager;
    ImageView home_tab,progress_tab,blog_tab,more_tab;
    LinearLayout home,progress,blog,more;
    FloatingActionButton diet_fab;
    GoogleFitUtils googleFitUtils;
    int lastSelected=-1;
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    DatabaseReference readReference;
    TextView chatItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        ((ShimmerFrameLayout)findViewById(R.id.shimmer_view_container)).startShimmerAnimation();
        setViews();
        fragment = new DashBoardFragment();
        lastSelected=R.id.home_tab;
        setSelected();
        fragmentManager = getSupportFragmentManager();
        getSupportActionBar().setTitle("Home");
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
        transaction.replace(R.id.main_fragment_frame, fragment).commit();
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        initComponent();
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(false);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_reorder, getTheme());
        toggle.setHomeAsUpIndicator(drawable);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        TextView dtName = header.findViewById(R.id.dtName);
        TextView clinicName = header.findViewById(R.id.clinicName);
        ImageView chat = header.findViewById(R.id.chat);
        ImageView call = header.findViewById(R.id.call);
        TextView experience = header.findViewById(R.id.experience);
        TextView clients = header.findViewById(R.id.clients);
        CircleImageView image = header.findViewById(R.id.image);

        final SharedPreferences sharedPreferences = getSharedPreferences(Constants.MyPreferences, Context.MODE_PRIVATE);
        dtName.setText(sharedPreferences.getString(Constants.DIETITIAN_NAME,"Unknown"));
        clinicName.setText(sharedPreferences.getString(Constants.CLINIC,""));
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ChatActivity.class));
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" +sharedPreferences.getString(Constants.DIETITIAN_PHONE,"")));
                startActivity(intent);
            }
        });
//        experience.setText(sharedPreferences.getString(Constants.DIETITIAN_EXPERIENCE,"-"));
//        clients.setText(sharedPreferences.getString(Constants.Clients,"-"));

        Tools.loadProfileImage(sharedPreferences.getString(Constants.DIETITIAN_PIC,"-"),image);

//        Tools.loadProfileImage("https://www.ryanfernando.in/wp-content/uploads/2018/07/about-thumb.jpg",image);




        readReference = FirebaseDatabase.getInstance().getReference().child("Read").child(Tools.getGeneralSharedPref(this).getString(Constants.USER_ID,"0"));


        final FirebaseUtils mFirebaseUtils = new FirebaseUtils(this);
//        mFirebaseUtils.addWater();

        /*googleFitUtils = new GoogleFitUtils(this, new GoogleFitUtils.OnDataReady() {
            @Override
            public void onStepsReady(String steps) {
//                Tools.initCustomToast(MainActivity.this,"Steps : "+steps);
                mFirebaseUtils.syncStepsData(steps);
            }

            @Override
            public void onCaloriesReady(String totalCal, String walking, String running, String other) {
//                Tools.initCustomToast(MainActivity.this,"Calories : "+totalCal);
                mFirebaseUtils.syncCaloriesData(totalCal);
            }

            @Override
            public void onWeeklyDataReady(ArrayList<String> days, ArrayList<String> steps) {

            }
        });
        googleFitUtils.setFlag(false);
        googleFitUtils.init();*/

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Token", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        saveToken(token);

                        // Log and toast
                        Log.d("Token", token);

//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        ComponentName receiver = new ComponentName(this, SampleBootReciever.class);
        PackageManager pm = getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        SharedPreferences reminderPrefs = Tools.getGeneralSharedPref(this);
        int tag = reminderPrefs.getInt("reminderTag", 0);
        if(tag==0) {
//                    setAlarm("07",7);
            setAlarm("1", 7, 0);
            setAlarm("2", 8, 30);
            setAlarm("3", 11, 30);
            setAlarm("4", 13, 0);
            setAlarm("5", 16, 0);
            setAlarm("6", 18, 0);
            setAlarm("7", 20, 0);
            setAlarm("8", 22, 0);
            setAlarm("10", 21, 0);
            setAlarm("9",9,0);
            setWaterAlarm();
            SharedPreferences.Editor editor = reminderPrefs.edit();
            editor.putInt("reminderTag", 1);
            editor.putString("1", "7:00");
            editor.putBoolean("1Status", true);
            editor.putString("2", "8:30");
            editor.putBoolean("2Status", true);
            editor.putString("3", "11:30");
            editor.putBoolean("3Status", true);
            editor.putString("4", "13:00");
            editor.putBoolean("4Status", true);
            editor.putString("5", "16:00");
            editor.putBoolean("5Status", true);
            editor.putString("6", "18:00");
            editor.putBoolean("6Status", true);
            editor.putString("7", "20:00");
            editor.putBoolean("7Status", true);
            editor.putString("8", "22:00");
            editor.putBoolean("8Status", true);
            editor.putString("10", "21:00");
            editor.putBoolean("10Status", true);
            editor.putString("9", "9:00");
            editor.putBoolean("9Status", true);
            editor.commit();

        }

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(60)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);


        int versionCode = 17;
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            versionCode = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Map<String, Object> remoteConfigDefaults = new HashMap<>();
        remoteConfigDefaults.put("version", versionCode+"");
        mFirebaseRemoteConfig.setDefaults(remoteConfigDefaults);

        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            boolean updated = task.getResult();
                            Log.d("Remote Config", "Config params updated: " + updated);
                            /*Toast.makeText(MainActivity.this, "Fetch and activate succeeded",
                                    Toast.LENGTH_SHORT).show();*/

                        } else {
                            /*Toast.makeText(MainActivity.this, "Fetch failed",
                                    Toast.LENGTH_SHORT).show();*/
                        }
                        showUpdateDialog();
                    }
                });

    }

    private void showUpdateDialog() {
        int versionCode = 10;
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionCode = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = mFirebaseRemoteConfig.getString("version");
        Log.d("Firebase config",version);
        Log.d("My version code",versionCode+"");
        if (Integer.parseInt(version)>versionCode){
//            Tools.initCustomToast(this,"Update Required!");

            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            // Setting Dialog Message
            alertDialog.setTitle("Update Required!");
            alertDialog.setIcon(R.mipmap.ic_launcher);
            alertDialog.setCancelable(false);
            alertDialog.setMessage("Newer version of the app is available on play store. Click update to install the latest version of the application.");
            alertDialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.quanutrition.app"));
                    startActivity(intent);

                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            alertDialog.show();
        }
    }


    public void setWaterAlarm(){
        AlarmManager alarmMgr;
        PendingIntent alarmIntent;
        alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, WaterReminderReciever.class);
        alarmIntent = PendingIntent.getBroadcast(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        SharedPreferences sharedPreferences = Tools.getGeneralSharedPref(this);
        String fromTime = sharedPreferences.getString("from","10:00");
        String toTime = sharedPreferences.getString("to","22:00");
        long interval=60*60*1000;
        Log.d("from Time",fromTime);
        Log.d("to Time",toTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

            Calendar c1 = Calendar.getInstance();
            c1.set(Calendar.HOUR_OF_DAY, Integer.parseInt(fromTime.split(":")[0]));
            c1.set(Calendar.MINUTE, Integer.parseInt(fromTime.split(":")[1]));

            Calendar c2 = Calendar.getInstance();
            c2.set(Calendar.HOUR_OF_DAY, Integer.parseInt(toTime.split(":")[0]));
            c2.set(Calendar.MINUTE, Integer.parseInt(toTime.split(":")[1]));
            long interval1 = (c2.getTimeInMillis()-c1.getTimeInMillis())/12;
            interval = sharedPreferences.getLong("interval",interval1);
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(fromTime.split(":")[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(fromTime.split(":")[1]));


        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString("from",fromTime);
        editor.putString("to",toTime);
        editor.putLong("interval",interval);
        editor.commit();
        calendar.set(Calendar.SECOND,0);
//        Log.e("Time",time+" : "+min);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                interval, alarmIntent);

    }

    public void setAlarm(String mealTime, int time, int min){
        AlarmManager alarmMgr;
        PendingIntent alarmIntent;
        alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyAlarmManager.class);
        intent.putExtra("mealTime",mealTime);
        intent.putExtra("status",true);
        alarmIntent = PendingIntent.getBroadcast(this, Integer.parseInt(mealTime), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, time);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND,0);
        Log.e("Time",time+" : "+min);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * 60*24, alarmIntent);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        final MenuItem menuItem = menu.findItem(R.id.chat);

//        View actionView = MenuItemCompat.getActionView(menuItem);

        View actionView  = menuItem.getActionView();

        chatItem = (TextView) actionView.findViewById(R.id.cart_badge);
        chatItem.setVisibility(View.GONE);

//        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }

    /*void setupBadge(){
        readReference.addValueEventListener(valueEventListener);
    }*/

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Log.d("Data changed",dataSnapshot.toString());
            if(chatItem!=null) {
                if (dataSnapshot.hasChild("read")) {
                    if (dataSnapshot.child("read").getValue().toString().equalsIgnoreCase("false")) {
                        chatItem.setVisibility(View.VISIBLE);
                    } else {
                        chatItem.setVisibility(View.GONE);
                    }
                } else {
                    chatItem.setVisibility(View.GONE);
                }
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    @Override
    protected void onPause() {
        readReference.removeEventListener(valueEventListener);
        super.onPause();
    }

    @Override
    protected void onResume() {
        readReference.addValueEventListener(valueEventListener);
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.chat) {
            readReference.child("read").setValue("true");
            startActivity(new Intent(MainActivity.this, ChatActivity.class));

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.feedback) {
            if(Tools.getGeneralSharedPref(this).getString(Constants.DIETITIAN_ID,"-1").equalsIgnoreCase("-1")){
                Tools.initCustomToast(this,"Dietitian Not assigned yet!");
            }else {
                FeedbackUtils feedbackUtils = new FeedbackUtils(this);
                feedbackUtils.getFeedBackDialog();
                feedbackUtils.setOnFeedbackListener(new FeedbackUtils.OnFeedbackRecievedListener() {
                    @Override
                    public void onFeedbackRecieved(int rating, String text) {
                        saveFeedBack(rating + "", text);
                    }
                });
            }
//        } else if (id == R.id.refer) {
//            startActivity(new Intent(MainActivity.this, ReferActivity.class));
        } else if (id == R.id.settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        }else if (id == R.id.about) {
            if(Tools.getGeneralSharedPref(this).getString(Constants.DIETITIAN_ID,"-1").equalsIgnoreCase("-1")){
                Tools.initCustomToast(this,"Dietitian Not assigned yet!");
            }else {
                startActivity(new Intent(MainActivity.this, AboutDietitianActivity.class));
            }
        }else if(id == R.id.logout){
            showLoginDialog();
        } else if(id == R.id.socialMedia){
            startActivity(new Intent(MainActivity.this, SocialMediaGridActivity.class));
        } else if(id == R.id.contact){
            startActivity(new Intent(MainActivity.this, ContactUsActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onClick(View view) {
        int Viewid = view.getId();
        int id=0;
        if(Viewid==R.id.home){
            id = R.id.home_tab;
        }else if(Viewid == R.id.progress){
            id = R.id.progress_tab;
        }else if(Viewid == R.id.blog){
            id = R.id.blog_tab;
        }else if(Viewid == R.id.more){
            id = R.id.more_tab;
        }

        if(lastSelected!=id) {
            setUnSelected();
            lastSelected = id;
            if (id == R.id.home_tab) {
                getSupportActionBar().setTitle("Home");
                fragment = new DashBoardFragment();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                transaction.replace(R.id.main_fragment_frame, fragment).commit();
            } else if (id == R.id.progress_tab) {
                getSupportActionBar().setTitle("Healthy Recipes");
                fragment = new RecipesFragment();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                transaction.replace(R.id.main_fragment_frame, fragment).commit();
            } else if (id == R.id.blog_tab) {
                getSupportActionBar().setTitle("Diet Plan");
                fragment = new DietPlanFragment();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                transaction.replace(R.id.main_fragment_frame, fragment).commit();
            } else if (id == R.id.more_tab) {
                getSupportActionBar().setTitle("More");
                fragment = new MoreFragment();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                transaction.replace(R.id.main_fragment_frame, fragment).commit();
            }
            setSelected();
        }
    }

    void setUnSelected(){
        if(lastSelected!=-1)
        ((ImageView)findViewById(lastSelected)).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
    }

    void setSelected(){
        if(lastSelected!=-1)
        ((ImageView)findViewById(lastSelected)).setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
    }

    void setViews(){
        home_tab  = findViewById(R.id.home_tab);
        blog_tab = findViewById(R.id.blog_tab);
        progress_tab = findViewById(R.id.progress_tab);
        more_tab = findViewById(R.id.more_tab);
        diet_fab = findViewById(R.id.diet_fab);
        home = findViewById(R.id.home);
        progress = findViewById(R.id.progress);
        blog = findViewById(R.id.blog);
        more = findViewById(R.id.more);
        /*home_tab.setOnClickListener(this);
        blog_tab.setOnClickListener(this);
        progress_tab.setOnClickListener(this);
        more_tab.setOnClickListener(this);*/

        more.setOnClickListener(this);
        progress.setOnClickListener(this);
        blog.setOnClickListener(this);
        home.setOnClickListener(this);

        diet_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, DietPlanViewActivity.class));
            }
        });
    }


    void saveFeedBack(String rating, String text){
        final AlertDialog ad = Tools.getDialog("Saving feedback...",this);
        ad.show();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ad.dismiss();
                Log.d("ResponseSlots",response);
                try {
                    JSONObject ob = new JSONObject(response);
                    Tools.initCustomToast(MainActivity.this,ob.getString("msg"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("myTag","I am here");
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ad.dismiss();
                Tools.initNetworkErrorToast(MainActivity.this);
                Log.d("Error",error.toString());
                Log.d("myTag","I am here");
            }
        };
        Map<String, String> params = new HashMap<>();
        params.put(Constants.DIETITIAN_ID,Tools.getGeneralSharedPref(this).getString(Constants.DIETITIAN_ID,"0"));
        params.put("rating",rating);
        params.put("feedback",text);

        NetworkManager.getInstance(this).sendPostRequestWithHeader(Urls.Feedback,params,listener,errorListener,this);

    }


    void saveToken(String token){
        /*final AlertDialog ad = Tools.getDialog("Saving feedback...",this);
        ad.show();*/
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                ad.dismiss();
                Log.d("ResponseSlots",response);
                try {
                    JSONObject ob = new JSONObject(response);
//                    Tools.initCustomToast(MainActivity.this,ob.getString("msg"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("myTag","I am here");
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                ad.dismiss();
                Tools.initNetworkErrorToast(MainActivity.this);
                Log.d("Error",error.toString());
                Log.d("myTag","I am here");
            }
        };
        Map<String, String> params = new HashMap<>();
        params.put("token",token);

        NetworkManager.getInstance(this).sendPostRequestWithHeader(Urls.save_token,params,listener,errorListener,this);

    }


    void saveLogoutToken(String token){
        /*final AlertDialog ad = Tools.getDialog("Saving feedback...",this);
        ad.show();*/
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                ad.dismiss();
                Log.d("ResponseSlots",response);
                try {
                    JSONObject ob = new JSONObject(response);
//                    Tools.initCustomToast(MainActivity.this,ob.getString("msg"));
                    SharedPreferences.Editor editor = Tools.getGeneralEditor(MainActivity.this);
                    editor.clear();
                    editor.commit();
                    NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancelAll();
//                    Checkout.clearUserData(getApplicationContext());
                    finish();
                    startActivity(new Intent(MainActivity.this,SignInActivity.class));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("myTag","I am here");
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                ad.dismiss();
                Tools.initNetworkErrorToast(MainActivity.this);
                Log.d("Error",error.toString());
                Log.d("myTag","I am here");
            }
        };
        Map<String, String> params = new HashMap<>();
        params.put("token",token);

        NetworkManager.getInstance(this).sendPostRequestWithHeader(Urls.save_token,params,listener,errorListener,this);

    }


    void showLoginDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        // Setting Dialog Message
        alertDialog.setTitle("Confirm Logout");
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Do you really want to logout?");
        alertDialog.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                saveToken("");
                saveLogoutToken("");

            }
        });
        alertDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==1&&resultCode==-1){
            if(lastSelected==R.id.home_tab){
                ((DashBoardFragment)fragment).setUpTrackers();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
