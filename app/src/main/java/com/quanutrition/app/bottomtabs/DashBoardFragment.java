package com.quanutrition.app.bottomtabs;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.quanutrition.app.BuildConfig;
import com.quanutrition.app.R;
import com.quanutrition.app.Utils.NetworkManager;
import com.quanutrition.app.Utils.Tools;
import com.quanutrition.app.blogs.BlogDashboardAdapter;
import com.quanutrition.app.blogs.BlogDetailsActivity;
import com.quanutrition.app.blogs.BlogsModel;
import com.quanutrition.app.blogs.HealthFeedActivity;
import com.quanutrition.app.blogs.ListAdapter;
import com.quanutrition.app.blogs.ListModel;
import com.quanutrition.app.blogs.TestimonialActivity;
import com.quanutrition.app.blogs.TestimonialAdapter;
import com.quanutrition.app.blogs.TestimonialModel;
import com.quanutrition.app.blogs.VideoDashboardAdapter;
import com.quanutrition.app.blogs.YoutubePlayActivity;
import com.quanutrition.app.firebaseUtils.FirebaseUtils;
import com.quanutrition.app.googlefit.CaloriesGraphActivity;
import com.quanutrition.app.googlefit.GoogleFitUtils;
import com.quanutrition.app.googlefit.StepsGraphActivity;
import com.quanutrition.app.googlefit.healthconnect.HealthConnectActivity;
import com.quanutrition.app.googlefit.healthconnect.HealthConnectAvailability;
import com.quanutrition.app.googlefit.healthconnect.HealthConnectConst;
import com.quanutrition.app.googlefit.healthconnect.HealthConnectManager;
import com.quanutrition.app.googlefit.healthconnect.SessionData;
import com.quanutrition.app.programs.ProgramDashboardAdapter;
import com.quanutrition.app.programs.ProgramDetailsActivity;
import com.quanutrition.app.programs.ProgramModel;
import com.quanutrition.app.programs.ProgramsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator2;

public class DashBoardFragment extends Fragment implements View.OnClickListener {
    private OnFragmentInteractionListener mListener;
    private View rootView;
    private RecyclerView programs_re, blogs_re,videos_re, tips_re,testimonials_re;
    private ProgramDashboardAdapter programAdapter;
    private ArrayList<ProgramModel> programs;
    private ArrayList<BlogsModel> videoList, blogsList;
    private ArrayList<TestimonialModel> testimonialsList;
    private ArrayList<ListModel> bannerList;
    ListAdapter bannerAdapter;
    BlogDashboardAdapter blogDashboardAdapter;
    TestimonialAdapter testimonialAdapter;

    VideoDashboardAdapter videoDashboardAdapter;
    private CardView step_card,calories_card;
//    FirebaseUtils firebaseUtils;
    TextView todaySteps,todayCal;
    String waterGoal="12",stepGoal="10000",calorieGoal="2500";
    String refCode="",zocolabLink="",zocolabBanner="";
    FirebaseUtils mFirebaseUtils;
    GoogleFitUtils googleFitUtils;
    CardView weight_card;
    TextView currentWeight,view_programs,view_blogs,view_videos,view_testimonials;

    LinearLayout planLabel,blogLabel,videoLabel,testimonialsLabel,lab_banner;
    CircleIndicator2 indicator;
    ImageView image;

    private HealthConnectManager healthConnectManager;

    public DashBoardFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        healthConnectManager = new HealthConnectManager(getActivity());
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_dash_board, container, false);
        //Get Views
        programs_re = rootView.findViewById(R.id.programs_re);
        view_programs = rootView.findViewById(R.id.view_programs);
        view_blogs = rootView.findViewById(R.id.view_blogs);
        view_videos = rootView.findViewById(R.id.view_videos);
        view_testimonials = rootView.findViewById(R.id.view_testimonials);
//        media_re = rootView.findViewById(R.id.media_re);
        blogs_re = rootView.findViewById(R.id.blogs_re);
        videos_re = rootView.findViewById(R.id.videos_re);
        testimonials_re = rootView.findViewById(R.id.testimonials_re);
        lab_banner = rootView.findViewById(R.id.lab_banner);
        image = rootView.findViewById(R.id.image);
        tips_re = rootView.findViewById(R.id.tips_re);
        step_card = rootView.findViewById(R.id.step_card);
        calories_card = rootView.findViewById(R.id.calories_card);
        todaySteps = rootView.findViewById(R.id.todaySteps);
        todayCal = rootView.findViewById(R.id.todayCal);
        planLabel = rootView.findViewById(R.id.planLabel);
        blogLabel = rootView.findViewById(R.id.blogLabel);
        testimonialsLabel = rootView.findViewById(R.id.testimonialLabel);
        videoLabel = rootView.findViewById(R.id.videoLabel);
        currentWeight = rootView.findViewById(R.id.currentWeight);
        weight_card = rootView.findViewById(R.id.weight_card);
        indicator = rootView.findViewById(R.id.indicator);


//        planLabel.setVisibility(View.GONE);

        //setListener
        step_card.setOnClickListener(this);
        calories_card.setOnClickListener(this);
        view_programs.setOnClickListener(this);
        view_blogs.setOnClickListener(this);
        view_videos.setOnClickListener(this);
        view_testimonials.setOnClickListener(this);
        lab_banner.setOnClickListener(this);


        //initialize
        programs = new ArrayList<>();
        videoList = new ArrayList<>();
        blogsList = new ArrayList<>();
        bannerList = new ArrayList<>();
        testimonialsList = new ArrayList<>();
//        firebaseUtils = new FirebaseUtils(getActivity());

        mFirebaseUtils = new FirebaseUtils(getActivity());
        programAdapter = new ProgramDashboardAdapter(programs, getActivity(), new ProgramDashboardAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity(), ProgramDetailsActivity.class);
                intent.putExtra("id", programs.get(position).getId());
                startActivity(intent);
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        programs_re.setLayoutManager(layoutManager);
        programs_re.setAdapter(programAdapter);

        blogDashboardAdapter = new BlogDashboardAdapter(blogsList, getActivity(), new BlogDashboardAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity(), BlogDetailsActivity.class);
                intent.putExtra("id", blogsList.get(position).getId());
                intent.putExtra("type", blogsList.get(position).getType() + "");
                startActivity(intent);
            }
        });
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        blogs_re.setLayoutManager(layoutManager2);
        blogs_re.setAdapter(blogDashboardAdapter);
        videoDashboardAdapter = new VideoDashboardAdapter(videoList, getActivity(), new VideoDashboardAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity(), YoutubePlayActivity.class);
                intent.putExtra("video",videoList.get(position).getUrl());
                startActivity(intent);
            }
        });
        RecyclerView.LayoutManager layoutManager4 = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        videos_re.setLayoutManager(layoutManager4);
        videos_re.setAdapter(videoDashboardAdapter);


        testimonialAdapter = new TestimonialAdapter(testimonialsList, getActivity(), new TestimonialAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {

            }
        });
        RecyclerView.LayoutManager layoutManager5 = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        testimonials_re.setLayoutManager(layoutManager5);
        testimonials_re.setAdapter(testimonialAdapter);

        bannerAdapter = new ListAdapter(bannerList,getActivity());
        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        tips_re.setLayoutManager(layoutManager3);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(tips_re);
        indicator.attachToRecyclerView(tips_re, snapHelper);
        tips_re.setAdapter(bannerAdapter);
        bannerAdapter.registerAdapterDataObserver(indicator.getAdapterDataObserver());


        weight_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), WeightProgressActivity.class));
            }
        });
        fetchData();
        return rootView;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.step_card) {
            if (todaySteps.getText().toString().equalsIgnoreCase("enable")) {
                if (BuildConfig.HEALTH_CONNECT.equalsIgnoreCase("YES")){
                    setUpTrackers(true);
                    HealthConnectAvailability availability = healthConnectManager.getAvailability().getValue();
                    if (availability.toString().equalsIgnoreCase("NOT_INSTALLED")){
                        Uri url = Uri.parse(getResources().getString(R.string.market_url)).buildUpon()
                                .appendQueryParameter("id", getResources().getString(R.string.health_connect_package))
                                .appendQueryParameter("url", getResources().getString(R.string.onboarding_url))
                                .build();
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url.toString())));
                    }else {
                        startActivity(new Intent(getActivity(), HealthConnectActivity.class));
                    }
                }else {
                    setUpTrackers(true);
                }
            }
//            else {
//                Intent i = new Intent(getActivity(), StepsGraphActivity.class);
//                i.putExtra("goal", stepGoal);
//                startActivity(i);
//            }
        } else if (id == R.id.calories_card) {
            if (todayCal.getText().toString().equalsIgnoreCase("enable")) {
                if (BuildConfig.HEALTH_CONNECT.equalsIgnoreCase("YES")){
                    setUpTrackers(true);
                    HealthConnectAvailability availability = healthConnectManager.getAvailability().getValue();
                    if (availability.toString().equalsIgnoreCase("NOT_INSTALLED")){
                        Uri url = Uri.parse(getResources().getString(R.string.market_url)).buildUpon()
                                .appendQueryParameter("id", getResources().getString(R.string.health_connect_package))
                                .appendQueryParameter("url", getResources().getString(R.string.onboarding_url))
                                .build();
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url.toString())));
                    }else {
                        startActivity(new Intent(getActivity(), HealthConnectActivity.class));
                    }
                }else {
//                googleFitUtils.setFlag(true);
                    setUpTrackers(true);
                }
            }
//            else {
//                Intent i = new Intent(getActivity(), CaloriesGraphActivity.class);
//                i.putExtra("goal", calorieGoal);
//                startActivity(i);
//            }

        }else if (id ==R.id.view_programs){
            startActivity(new Intent(getActivity(), ProgramsActivity.class));
        }else if (id ==R.id.view_blogs){
            Intent intent = new Intent(getActivity(),HealthFeedActivity.class);
            intent.putExtra("type","1");
            startActivity(intent);
        }else if (id ==R.id.view_videos){
            Intent intent = new Intent(getActivity(),HealthFeedActivity.class);
            intent.putExtra("type","2");
            startActivity(intent);
        }else if (id ==R.id.view_testimonials){
            startActivity(new Intent(getActivity(), TestimonialActivity.class));
        }else if (id ==R.id.lab_banner){
            if (zocolabLink.isEmpty()){
                Tools.initCustomToast(getActivity(),"Link is not available");
            }else{
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(zocolabLink));
                startActivity(browserIntent);
            }
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


    void fetchData() {
        final AlertDialog ad = Tools.getDialog("Fetching data...", getActivity());
        ad.show();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ad.dismiss();
                Log.d("ResponseSlots", response);
                try {
                    JSONObject ob = new JSONObject(response);
                    if (ob.getInt("res") == 1) {
                        JSONObject data = ob.getJSONObject("data");
                        JSONObject goals = data.getJSONObject("goal");
                        calorieGoal = goals.getString("cal_goal");
                        waterGoal = goals.getString("water_goal");
                        stepGoal = goals.getString("step_goal");
                        refCode = data.optString("referral_code");


                        if (getActivity() != null) {
                            setUpTrackers(false);
                        }

                        JSONArray bannerArray = data.getJSONArray("banner");
                        bannerList.clear();
                        for (int i = 0; i < bannerArray.length(); i++) {
                            ListModel model = new ListModel(bannerArray.getString(i), "1");
                            bannerList.add(model);
                        }
                        bannerAdapter.notifyDataSetChanged();

                        if (data.getJSONArray("blog").length() > 1) {
                            blogLabel.setVisibility(View.VISIBLE);
                            JSONArray blogArray = data.getJSONArray("blog");
                            blogsList.clear();
                            for (int i = 0; i < blogArray.length(); i++) {
                                JSONObject object = blogArray.getJSONObject(i);
                                BlogsModel mediaModel = new BlogsModel(object.getInt("id") + "", object.getString("title"), "", object.getString("image"), "","");
                                mediaModel.setType("1");
                                mediaModel.setAdded_on(object.getString("added_on"));
                                blogsList.add(mediaModel);
                            }
                            blogDashboardAdapter.notifyDataSetChanged();
                        } else {
                            blogLabel.setVisibility(View.GONE);
                        }

                        if (data.getJSONArray("program").length() > 1) {
                            planLabel.setVisibility(View.VISIBLE);
                            JSONArray program = data.getJSONArray("program");
                            programs.clear();
                            for (int i = 0; i < program.length(); i++) {
                                JSONObject programOb = program.getJSONObject(i);
                                ProgramModel model = new ProgramModel(programOb.getInt("id") + "", programOb.getString("name"), programOb.getString("image"), true);
                                programs.add(model);
                            }
                            programAdapter.notifyDataSetChanged();
                        } else {
                            planLabel.setVisibility(View.GONE);
                        }
                        if (data.getJSONArray("video").length() > 1) {
                            videoLabel.setVisibility(View.VISIBLE);
                            JSONArray videos = data.getJSONArray("video");
                            videoList.clear();
                            for (int i = 0; i < videos.length(); i++) {
                                JSONObject videosOb = videos.getJSONObject(i);
                                BlogsModel videoModel = new BlogsModel(videosOb.getInt("id")+"",videosOb.getString("title"),videosOb.getString("url"),videosOb.getString("added_on"));
                                videoModel.setType("2");
                                videoList.add(videoModel);
                            }
                            videoDashboardAdapter.notifyDataSetChanged();
                        } else {
                            videoLabel.setVisibility(View.GONE);
                        }
                        if (data.getJSONArray("testimonial").length() > 1) {
                            testimonialsLabel.setVisibility(View.VISIBLE);
                            JSONArray testimonial = data.getJSONArray("testimonial");
                            testimonialsList.clear();
                            for (int i = 0; i < testimonial.length(); i++) {
                                JSONObject testimonialOb = testimonial.getJSONObject(i);
                                TestimonialModel testimonialModel = new TestimonialModel(testimonialOb.getInt("id")+"",testimonialOb.getString("title"),testimonialOb.getString("user"),testimonialOb.getString("user_img"),testimonialOb.getString("content"),testimonialOb.getString("image"),testimonialOb.getString("added_on"));
                                testimonialsList.add(testimonialModel);
                            }
                            testimonialAdapter.notifyDataSetChanged();
                        } else {
                            videoLabel.setVisibility(View.GONE);
                        }
                        if (data.has("zocolabs")){
                            lab_banner.setVisibility(View.VISIBLE);
                            zocolabLink = data.getJSONObject("zocolabs").getString("link");
                            zocolabBanner = data.getJSONObject("zocolabs").getString("banner");
                            if (zocolabBanner.isEmpty()){
                                lab_banner.setVisibility(View.GONE);
                            }else {
                                lab_banner.setVisibility(View.VISIBLE);
                                Tools.loadImageIntoImageView(zocolabBanner,image);
                            }
                        }else {
                            lab_banner.setVisibility(View.GONE);
                        }
                        if (data.getString("weight").isEmpty()) {
                            currentWeight.setText("0.0" + " Kg");
                        } else {
                            currentWeight.setText(data.getString("weight") + " Kg");
                        }

                    } else {
                        Tools.initCustomToast(getActivity(), ob.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("myTag", "I am here");
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ad.dismiss();
                Tools.initNetworkErrorToast(getActivity());
                Log.d("Error", error.toString());
                Log.d("myTag", "I am here");
            }
        };
        NetworkManager.getInstance(getActivity()).sendGetRequest(Urls.Dashboard, listener, errorListener, getActivity());
    }


    void fetchGoals() {
        final AlertDialog ad = Tools.getDialog("Fetching data...", getActivity());
        ad.show();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ad.dismiss();
                Log.d("ResponseSlots", response);
                try {
                    JSONObject ob = new JSONObject(response);
                    if (ob.getInt("res") == 1) {
                        JSONObject data = ob.getJSONObject("data");
                        JSONObject goals = data.getJSONObject("goal");
                        calorieGoal = goals.getString("cal_goal");
                        waterGoal = goals.getString("water_goal");
                        stepGoal = goals.getString("step_goal");

                    } else {
                        Tools.initCustomToast(getActivity(), ob.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("myTag", "I am here");
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ad.dismiss();
                Tools.initNetworkErrorToast(getActivity());
                Log.d("Error", error.toString());
                Log.d("myTag", "I am here");
            }
        };
//        String url = Urls.Dashboard+"?dietitianId="+Tools.getGeneralSharedPref(getActivity()).getString(Constants.DIETITIAN_ID,"0");
        NetworkManager.getInstance(getActivity()).sendGetRequest(Urls.Dashboard, listener, errorListener, getActivity());

    }

    public void setUpTrackers(boolean flag) {

//        setUpWaterLoader();
        if (BuildConfig.HEALTH_CONNECT.equalsIgnoreCase("YES")){
            healthConnectManager = new HealthConnectManager(getActivity());
            HealthConnectAvailability availability = healthConnectManager.getAvailability().getValue();
            if (availability.toString().equalsIgnoreCase("NOT_INSTALLED")){
                todayCal.setText("Enable");
                todaySteps.setText("Enable");
            }else {
                if (Tools.getGeneralSharedPref(getActivity()).getBoolean(HealthConnectConst.HEALTH_CONNECT_ACTIVE,false)){
                    healthConnectManager.readStepsByWeekly(7, new HealthConnectManager.StepsByWeeklyCallback() {
                        @Override
                        public void onStepsByWeeklyResult(@NonNull List<? extends SessionData> result) {
                            if (result.isEmpty()) {
                                todaySteps.setText("Enable");
                                todayCal.setText("Enable");
                            } else {
                                for (int i = 0; i < result.size(); i++) {
                                    todaySteps.setText(result.get(0).getTotalSteps() + "");
                                    todayCal.setText((result.get(0).getTotalEnergyBurned()) + " Cal.");
                                }
                            }
                        }
                        @Override
                        public void onError(@NonNull Exception exception) {

                        }
                    });
                }else {
                    todayCal.setText("Enable");
                    todaySteps.setText("Enable");
                }
            }
        }else {
            googleFitUtils = new GoogleFitUtils(getActivity(), new GoogleFitUtils.OnDataReady() {
                @Override
                public void onStepsReady(String steps) {
                    if(steps.equalsIgnoreCase("-1"))
                        todaySteps.setText("Enable");
                    else {
                        todaySteps.setText(steps + "");
                        mFirebaseUtils.syncStepsData(steps);
                    }
                }
                @Override
                public void onCaloriesReady(String totalCal, String walking, String running, String other) {
                    if(totalCal.equalsIgnoreCase("-1"))
                        todayCal.setText("Enable");
                    else {
                        todayCal.setText("" + totalCal + " Cal.");
                        mFirebaseUtils.syncCaloriesData(totalCal);
                    }
                }
                @Override
                public void onWeeklyDataReady(ArrayList<String> days, ArrayList<String> steps) {

                }
            });
            googleFitUtils.setFlag(flag);
            googleFitUtils.setWeekly(true);
            googleFitUtils.init();
        }
    }

    @Override
    public void onResume() {
//        fetchGoals();

        if (BuildConfig.HEALTH_CONNECT.equalsIgnoreCase("YES")){
            healthConnectManager = new HealthConnectManager(getActivity());
            setUpTrackers(true);
            rootView.findViewById(R.id.step_card).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (todaySteps.getText().toString().equalsIgnoreCase("enable")) {
                        setUpTrackers(true);
                        HealthConnectAvailability availability = healthConnectManager.getAvailability().getValue();
                        if (availability.toString().equalsIgnoreCase("NOT_INSTALLED")){
                            Uri url = Uri.parse(getResources().getString(R.string.market_url)).buildUpon()
                                    .appendQueryParameter("id", getResources().getString(R.string.health_connect_package))
                                    .appendQueryParameter("url", getResources().getString(R.string.onboarding_url))
                                    .build();
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url.toString())));
                        }else {
                            startActivity(new Intent(getActivity(), HealthConnectActivity.class));
                        }
                    }
//                else {
//                    Intent i = new Intent(getActivity(), StepsGraphActivity.class);
//                    i.putExtra("goal", stepGoal);
//                    startActivity(i);
//                }
                }
            });

            rootView.findViewById(R.id.calories_card).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (todayCal.getText().toString().equalsIgnoreCase("enable")) {
                        setUpTrackers(true);
                        HealthConnectAvailability availability = healthConnectManager.getAvailability().getValue();
                        if (availability.toString().equalsIgnoreCase("NOT_INSTALLED")){
                            Uri url = Uri.parse(getResources().getString(R.string.market_url)).buildUpon()
                                    .appendQueryParameter("id", getResources().getString(R.string.health_connect_package))
                                    .appendQueryParameter("url", getResources().getString(R.string.onboarding_url))
                                    .build();
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url.toString())));
                        }else {
                            startActivity(new Intent(getActivity(), HealthConnectActivity.class));
                        }
                    }
                }
            });
        }
        super.onResume();
    }

//    @Override
//    public void onPause() {
//        healthConnectManager = new HealthConnectManager(getActivity());
//        setUpTrackers(true);
//        rootView.findViewById(R.id.step_card).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (todaySteps.getText().toString().equalsIgnoreCase("enable")) {
////                googleFitUtils.setFlag(true);
//                    setUpTrackers(true);
//                    HealthConnectAvailability availability = healthConnectManager.getAvailability().getValue();
//                    if (availability.toString().equalsIgnoreCase("NOT_INSTALLED")){
//                        Uri url = Uri.parse(getResources().getString(R.string.market_url)).buildUpon()
//                                .appendQueryParameter("id", getResources().getString(R.string.health_connect_package))
//                                .appendQueryParameter("url", getResources().getString(R.string.onboarding_url))
//                                .build();
//                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url.toString())));
//                    }else {
//                        startActivity(new Intent(getActivity(), HealthConnectActivity.class));
//                    }
//                }
////                else {
////                    Intent i = new Intent(getActivity(), StepsGraphActivity.class);
////                    i.putExtra("goal", stepGoal);
////                    startActivity(i);
////                }
//            }
//        });
//        rootView.findViewById(R.id.calories_card).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (todayCal.getText().toString().equalsIgnoreCase("enable")) {
////                googleFitUtils.setFlag(true);
//                    setUpTrackers(true);
//                    HealthConnectAvailability availability = healthConnectManager.getAvailability().getValue();
//                    if (availability.toString().equalsIgnoreCase("NOT_INSTALLED")){
//                        Uri url = Uri.parse(getResources().getString(R.string.market_url)).buildUpon()
//                                .appendQueryParameter("id", getResources().getString(R.string.health_connect_package))
//                                .appendQueryParameter("url", getResources().getString(R.string.onboarding_url))
//                                .build();
//                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url.toString())));
//                    }else {
//                        startActivity(new Intent(getActivity(), HealthConnectActivity.class));
//                    }
//                }
//            }
//        });
//        super.onPause();
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        Log.d("onActivity Result Frag",data.toString());
        if (requestCode == 0 && resultCode == -1) {
            setUpTrackers(false);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
