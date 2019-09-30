package com.quanutrition.app.bottomtabs;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.quanutrition.app.R;
import com.quanutrition.app.Utils.Constants;
import com.quanutrition.app.Utils.NetworkManager;
import com.quanutrition.app.Utils.Tools;
import com.quanutrition.app.blogs.BlogDetailsActivity;
import com.quanutrition.app.blogs.BlogsAdapter;
import com.quanutrition.app.blogs.BlogsModel;
import com.quanutrition.app.blogs.ListAdapter;
import com.quanutrition.app.blogs.ListModel;
import com.quanutrition.app.blogs.SocialMediaAdapter;
import com.quanutrition.app.blogs.SocialMediaGridActivity;
import com.quanutrition.app.firebaseUtils.FirebaseUtils;
import com.quanutrition.app.googlefit.CaloriesGraphActivity;
import com.quanutrition.app.googlefit.GoogleFitUtils;
import com.quanutrition.app.googlefit.StepsGraphActivity;
import com.quanutrition.app.programs.ProgramDashboardAdapter;
import com.quanutrition.app.programs.ProgramDetailsActivity;
import com.quanutrition.app.programs.ProgramModel;
import com.quanutrition.app.waterintake.WaterGraphActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.mikhaellopez.circularfillableloaders.CircularFillableLoaders;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DashBoardFragment extends Fragment implements View.OnClickListener{
    private OnFragmentInteractionListener mListener;
    private View rootView;
    private RecyclerView programs_re,media_re,blogs_re,tips_re;
    private ProgramDashboardAdapter programAdapter;
    private ArrayList<ProgramModel> programs;
    private ArrayList<BlogsModel> mediaList,blogsList;
    private ArrayList<ListModel> bannerList;
    ListAdapter bannerAdapter;
    SocialMediaAdapter mediaAdapter;
    BlogsAdapter blogsAdapter;
    private CardView step_card,calories_card;
    FirebaseUtils firebaseUtils;
    TextView todaySteps,todayCal,planLabel;
    String waterGoal="12",stepGoal="10000",calorieGoal="250";
    String refCode="";

    public DashBoardFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_dash_board, container, false);
        //Get Views
        programs_re = rootView.findViewById(R.id.programs_re);
        media_re = rootView.findViewById(R.id.media_re);
        blogs_re = rootView.findViewById(R.id.blogs_re);
        tips_re = rootView.findViewById(R.id.tips_re);
        step_card = rootView.findViewById(R.id.step_card);
        calories_card = rootView.findViewById(R.id.calories_card);
        todaySteps = rootView.findViewById(R.id.todaySteps);
        todayCal = rootView.findViewById(R.id.todayCal);
        planLabel = rootView.findViewById(R.id.planLabel);


//        planLabel.setVisibility(View.GONE);

        //setListener
        step_card.setOnClickListener(this);
        calories_card.setOnClickListener(this);


        //initialize
        programs = new ArrayList<>();
        mediaList = new ArrayList<>();
        blogsList = new ArrayList<>();
        bannerList = new ArrayList<>();
        firebaseUtils = new FirebaseUtils(getActivity());

        //static data
       /* ProgramModel programModel = new ProgramModel("1","Sports Nutrition","https://quanutrition.com/images/package/1.jpg",true);
        ProgramModel programModel1 = new ProgramModel("2","Weight Loss","https://quanutrition.com/images/package/2.jpg",true);
        programs.add(programModel);
        programs.add(programModel1);
        programs.add(programModel);
        programs.add(programModel1);
        programs.add(programModel);
        programs.add(programModel1);

        BlogsModel mediaModel = new BlogsModel("1","test test","","https://res.cloudinary.com/dietitioweb/image/upload/v1567162378/46231111_327952294466473_4343606653915100417_n.jpg","","");
        BlogsModel mediaModel1 = new BlogsModel("1","test test","","https://res.cloudinary.com/dietitioweb/image/upload/v1567162378/42817614_185216395759153_7235482963145162043_n.jpg","","");
        BlogsModel mediaModel2 = new BlogsModel("1","test test","","https://res.cloudinary.com/dietitioweb/image/upload/v1567162378/45392915_323938901762199_7613445403551522007_n.jpg","","");
        mediaList.add(mediaModel);
        mediaList.add(mediaModel1);
        mediaList.add(mediaModel2);
        mediaList.add(mediaModel);
        mediaList.add(mediaModel1);
        mediaList.add(mediaModel2);

        BlogsModel model = new BlogsModel("1","7 ways to Lower Your Alzheimer’s Risk ","https://www.quanutrition.com/nutritionalfacts/ways-to-lower-your-alzheimers-risk/","https://www.quanutrition.com/nutritionalfacts/wp-content/uploads/2019/08/qua-nutrition-176x176.png","Qua Nutrition","Alzheimer’s is among the many diseases most people want to avoid, and for good reasons. Nonetheless, the statistics of people suffering from Alzheimer’s disease (AD) is staggering.");
        BlogsModel model1 = new BlogsModel("2","Foods to Eat on the Ketogenic Diet","https://www.quanutrition.com/nutritionalfacts/foods-to-eat-on-the-ketogenic-diet/","https://www.quanutrition.com/nutritionalfacts/wp-content/uploads/2019/07/5d3fe508e4cf3-176x176.png","Qua","The keto diet is gaining more popularity now more than ever as one of the main diets that people are using when it comes to various avenues of diets.");
        model.setType("1");
        model1.setType("1");
        blogsList.add(model);
        blogsList.add(model1);
        blogsList.add(model);
        blogsList.add(model1);
        blogsList.add(model);
        blogsList.add(model1);

        ListModel banner = new ListModel("https://quanutrition.com/images/bg/bg-001.jpg","1");
        bannerList.add(banner);
        bannerList.add(banner);
        bannerList.add(banner);
        bannerList.add(banner);*/

        //Program RecyclerView code
        programAdapter = new ProgramDashboardAdapter(programs, getActivity(), new ProgramDashboardAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
//                Tools.initCustomToast(getActivity(),"Toast");
                Intent intent =  new Intent(getActivity(), ProgramDetailsActivity.class);
                intent.putExtra("id",programs.get(position).getId());
                startActivity(intent);
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL,false);
        programs_re.setLayoutManager(layoutManager);
        programs_re.setAdapter(programAdapter);


        mediaAdapter = new SocialMediaAdapter(getActivity(), mediaList, new SocialMediaAdapter.OnShare() {
            @Override
            public void onShare(String message, Uri image) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, image);
                shareIntent.putExtra(Intent.EXTRA_TEXT,message);
                shareIntent.setType("image/*");
                startActivity(Intent.createChooser(shareIntent, "Share Image"));
            }

            @Override
            public void onSeeMoreClick(View v, int position) {
                startActivity(new Intent(getActivity(), SocialMediaGridActivity.class));
            }
        });
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        media_re.setLayoutManager(layoutManager1);
        media_re.setAdapter(mediaAdapter);


        blogsAdapter = new BlogsAdapter(blogsList, getActivity(), new BlogsAdapter.OnItemClicked() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity(), BlogDetailsActivity.class);
                intent.putExtra("id", blogsList.get(position).getId() + "");
                final ActivityOptions options =
                        ActivityOptions.makeSceneTransitionAnimation(getActivity(), view, view.getTransitionName());
                startActivity(intent,options.toBundle());
            }
        });
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
        blogs_re.setLayoutManager(layoutManager2);
        blogs_re.setAdapter(blogsAdapter);


        bannerAdapter = new ListAdapter(bannerList,getActivity());
        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        tips_re.setLayoutManager(layoutManager3);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(tips_re);
        tips_re.setAdapter(bannerAdapter);

        fetchData();

//        buildDeepLink();



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
        if(id == R.id.step_card){
            Intent i = new Intent(getActivity(), StepsGraphActivity.class);
            i.putExtra("goal",stepGoal);
            startActivity(i);
        }else if(id == R.id.calories_card){
            Intent i = new Intent(getActivity(), CaloriesGraphActivity.class);
            i.putExtra("goal",calorieGoal);
            startActivity(i);

        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


    void fetchData(){
        final AlertDialog ad = Tools.getDialog("Fetching data...",getActivity());
        ad.show();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ad.dismiss();
                Log.d("ResponseSlots",response);
                try {
                    JSONObject ob = new JSONObject(response);
                    if(ob.getInt("res")==1){
                        JSONObject data = ob.getJSONObject("data");
                        JSONObject goals = data.getJSONObject("goal");
                        calorieGoal = goals.getString("cal_goal");
                        waterGoal = goals.getString("water_goal");
                        stepGoal = goals.getString("step_goal");
//                        refCode = data.optString("referral_code");



                        if(getActivity()!=null){
                            setUpTrackers();
                        }

                        JSONArray bannerArray = data.getJSONArray("banner");
                        bannerList.clear();
                        for(int i=0;i<bannerArray.length();i++){
                            ListModel model = new ListModel(bannerArray.getString(i),"1");
                            bannerList.add(model);
                        }
                        bannerAdapter.notifyDataSetChanged();

                        JSONArray mediaArray = data.getJSONArray("media");
                        mediaList.clear();
                        for(int i=0;i<mediaArray.length();i++){
                            JSONObject object = mediaArray.getJSONObject(i);
                            BlogsModel mediaModel = new BlogsModel(object.getInt("id")+"",object.getString("title"),"",object.getString("image"),"","");
                            mediaList.add(mediaModel);
                        }
                        mediaAdapter.notifyDataSetChanged();

                        JSONArray blogArray = data.getJSONArray("blog");
                        blogsList.clear();
                        for(int i=0;i<blogArray.length();i++){
                            JSONObject object = blogArray.getJSONObject(i);
                            BlogsModel mediaModel = new BlogsModel(object.getInt("id")+"",object.getString("title"),"",object.getString("image"),"",object.getString("content"));
                            mediaModel.setType("1");
                            blogsList.add(mediaModel);
                        }
                        blogsAdapter.notifyDataSetChanged();

                        if(data.getJSONArray("program").length()>1){
                            planLabel.setVisibility(View.VISIBLE);
                            JSONArray program = data.getJSONArray("program");
                            programs.clear();
                            for(int i=0;i<program.length();i++){
                                JSONObject programOb = program.getJSONObject(i);
                                ProgramModel model = new ProgramModel(programOb.getInt("id")+"",programOb.getString("name"),programOb.getString("name"),true);
                                programs.add(model);
                            }
                            programAdapter.notifyDataSetChanged();
                        }else{
                            planLabel.setVisibility(View.GONE);
                        }
                    }else{
                        Tools.initCustomToast(getActivity(),ob.getString("msg"));
                    }
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
                Tools.initNetworkErrorToast(getActivity());
                Log.d("Error",error.toString());
                Log.d("myTag","I am here");
            }
        };
//        String url = Urls.Dashboard+"?dietitianId="+Tools.getGeneralSharedPref(getActivity()).getString(Constants.DIETITIAN_ID,"0");
        NetworkManager.getInstance(getActivity()).sendGetRequest(Urls.Dashboard,listener,errorListener,getActivity());

    }


    void fetchGoals(){
        final AlertDialog ad = Tools.getDialog("Fetching data...",getActivity());
        ad.show();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ad.dismiss();
                Log.d("ResponseSlots",response);
                try {
                    JSONObject ob = new JSONObject(response);
                    if(ob.getInt("res")==1) {
                        JSONObject data = ob.getJSONObject("data");
                        JSONObject goals = data.getJSONObject("goal");
                        calorieGoal = goals.getString("cal_goal");
                        waterGoal = goals.getString("water_goal");
                        stepGoal = goals.getString("step_goal");

                    }else{
                        Tools.initCustomToast(getActivity(),ob.getString("msg"));
                    }
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
                Tools.initNetworkErrorToast(getActivity());
                Log.d("Error",error.toString());
                Log.d("myTag","I am here");
            }
        };
//        String url = Urls.Dashboard+"?dietitianId="+Tools.getGeneralSharedPref(getActivity()).getString(Constants.DIETITIAN_ID,"0");
        NetworkManager.getInstance(getActivity()).sendGetRequest(Urls.Dashboard,listener,errorListener,getActivity());

    }

    public void setUpTrackers(){

//        setUpWaterLoader();

        final FirebaseUtils mFirebaseUtils = new FirebaseUtils(getActivity());

        GoogleFitUtils googleFitUtils = new GoogleFitUtils(getActivity(), new GoogleFitUtils.OnDataReady() {
            @Override
            public void onStepsReady(String steps) {
                todaySteps.setText(steps+"");
                mFirebaseUtils.syncStepsData(steps);

            }

            @Override
            public void onCaloriesReady(String totalCal, String walking, String running, String other) {
                todayCal.setText(""+totalCal+" KCal.");
                mFirebaseUtils.syncCaloriesData(totalCal);
            }

            @Override
            public void onWeeklyDataReady(ArrayList<String> days, ArrayList<String> steps) {

            }
        });
        googleFitUtils.init();
    }




    @Override
    public void onResume() {
        fetchGoals();
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        Log.d("onActivity Result Frag",data.toString());
        if(requestCode==1&&resultCode==-1){
            setUpTrackers();
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
