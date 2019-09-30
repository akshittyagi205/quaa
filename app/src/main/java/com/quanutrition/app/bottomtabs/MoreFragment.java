package com.quanutrition.app.bottomtabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.quanutrition.app.R;
import com.quanutrition.app.Utils.Constants;
import com.quanutrition.app.Utils.NetworkManager;
import com.quanutrition.app.Utils.Tools;
import com.quanutrition.app.appointment.AppointmentsActivity;
import com.quanutrition.app.blogs.HealthFeedActivity;
import com.quanutrition.app.general.ClientProfile;
import com.quanutrition.app.general.DailyDiaryActivity;
import com.quanutrition.app.general.PhysicalActivity;
import com.quanutrition.app.payments.PaymentHistory;
import com.quanutrition.app.profile.BasicInfoActivity;
import com.quanutrition.app.profile.FoodSpecificationsActivity;
import com.quanutrition.app.profile.GeneralDataActivity;
import com.quanutrition.app.profile.MedicalHistory;
import com.quanutrition.app.profile.TimeInputActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class MoreFragment extends Fragment implements View.OnClickListener{

    private OnFragmentInteractionListener mListener;
    View rootView;
    NestedScrollView nested_scroll_view;
    CircleImageView image;
    TextView user_name,user_email,user_number;
    private LinearLayout profile_child_layout,clientProfile,general_data_layout,time_info_layout,workout_layout,assessment_layout,feed_layout,profile_layout,basic_info_layout,medical_info_layout,food_info_layout,plan_layout;

    public MoreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_more, container, false);
        profile_layout = rootView.findViewById(R.id.profile_layout);
        profile_child_layout = rootView.findViewById(R.id.profile_child_layout);
        basic_info_layout = rootView.findViewById(R.id.basic_info_layout);
        medical_info_layout = rootView.findViewById(R.id.medical_info_layout);
        food_info_layout = rootView.findViewById(R.id.food_info_layout);
        plan_layout = rootView.findViewById(R.id.plan_layout);
        assessment_layout = rootView.findViewById(R.id.assessment_layout);
        time_info_layout = rootView.findViewById(R.id.time_info_layout);
        general_data_layout = rootView.findViewById(R.id.general_data_layout);
        feed_layout = rootView.findViewById(R.id.feed_layout);
        workout_layout = rootView.findViewById(R.id.workout_layout);
        clientProfile = rootView.findViewById(R.id.clientProfile);
        image = rootView.findViewById(R.id.image);
        user_name = rootView.findViewById(R.id.user_name);
        user_email = rootView.findViewById(R.id.user_email);
        user_number = rootView.findViewById(R.id.user_number);
        nested_scroll_view = rootView.findViewById(R.id.nested_scroll_view);


        SharedPreferences sharedPreferences  = getActivity().getSharedPreferences(Constants.MyPreferences, Context.MODE_PRIVATE);
        user_name.setText(sharedPreferences.getString(Constants.PROFILE_NAME,"Unknown").trim());
        user_email.setText(sharedPreferences.getString(Constants.PROFILE_EMAIL,"Unknown Email").trim());
        user_number.setText(sharedPreferences.getString(Constants.PHONE,"-").trim());
        Tools.loadProfileImage(sharedPreferences.getString(Constants.PROFILE_IMAGE,"-"),image);

        if(sharedPreferences.getString(Constants.PROFILE_EMAIL,"Unknown Email").trim().isEmpty()){
            user_email.setVisibility(View.GONE);
        }else{
            user_email.setVisibility(View.VISIBLE);
        }


        profile_layout.setOnClickListener(this);
        basic_info_layout.setOnClickListener(this);
        medical_info_layout.setOnClickListener(this);
        food_info_layout.setOnClickListener(this);
        plan_layout.setOnClickListener(this);
        feed_layout.setOnClickListener(this);
        general_data_layout.setOnClickListener(this);
        time_info_layout.setOnClickListener(this);
        clientProfile.setOnClickListener(this);
        assessment_layout.setOnClickListener(this);
        workout_layout.setOnClickListener(this);
        image.setOnClickListener(this);

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
        if(id==R.id.profile_layout){
            if(profile_child_layout.getVisibility()== View.VISIBLE){
                profile_child_layout.setVisibility(View.GONE);
            }else{
                profile_child_layout.setVisibility(View.VISIBLE);
                nested_scroll_view.post(new Runnable() {
                    @Override
                    public void run() {
                        nested_scroll_view.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }
        }else if(id==R.id.basic_info_layout){
            startActivity(new Intent(getActivity(), BasicInfoActivity.class));
        }else if(id == R.id.medical_info_layout){
            startActivity(new Intent(getActivity(), MedicalHistory.class));
        }else if(id == R.id.food_info_layout){
            startActivity(new Intent(getActivity(), FoodSpecificationsActivity.class));
        }else if(id == R.id.plan_layout){
            startActivity(new Intent(getActivity(), PaymentHistory.class));
        }else if(id == R.id.feed_layout){
            startActivity(new Intent(getActivity(), HealthFeedActivity.class));
        }else if(id == R.id.assessment_layout){
//            startActivity(new Intent(getActivity(), DailyDiaryActivity.class));
        }else if(id == R.id.workout_layout){
            startActivity(new Intent(getActivity(), PhysicalActivity.class));
        }else if(id == R.id.image){
           openGallery();
        }else if(id == R.id.general_data_layout){
            startActivity(new Intent(getActivity(), GeneralDataActivity.class));
        }else if(id == R.id.time_info_layout){
            startActivity(new Intent(getActivity(), TimeInputActivity.class));
        }else if(id == R.id.clientProfile){
            startActivity(new Intent(getActivity(), ClientProfile.class));
        }
    }


    void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // 2. pick image only
        intent.setType("image/*");
        // 3. start activity
        startActivityForResult(intent, 6678);
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    AlertDialog ad;
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            Uri currImageURI = data.getData();

              ad = Tools.getDialog("Uploading...",getActivity());
            ad.show();
            uploadPhoto(currImageURI, getActivity(), new OnImageUpload() {
                @Override
                public void onUpload(String url) {
                    uploadImageToDb(url);
                }
            });

        }catch (Exception e){

            Tools.initCustomToast(getActivity(),"Couldn't get the image!");

            e.printStackTrace();
        }
    }

    public interface OnImageUpload{
        void onUpload(String url);
    }
    public  void uploadPhoto(final Uri currImageURI, final Context context, final OnImageUpload onImageUpload) throws IOException {

        Map config = new HashMap();
        config.put("cloud_name", "dietitioweb");
        config.put("api_key", "932386756689735");
        config.put("api_secret", "vE8qSCHOAcdL9K45C76I5QLdz9A");
        final Cloudinary cloudinary = new Cloudinary(config);
//        final androidx.appcompat.app.AlertDialog ad = Tools.getDialog("Uploading...", context);
//        ad.show();
        try {
            new Thread(new Runnable() {

                @Override
                public void run() {

                    InputStream in = null;

                    try {
                        in = context.getContentResolver().openInputStream(currImageURI);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    Map uploadResult = null;
                    try {
                        uploadResult = cloudinary.uploader().upload(in, ObjectUtils.emptyMap());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (uploadResult.containsKey("url")) {
                        Log.d("hey", uploadResult.get("url") + "");

//                        dialog.dismiss();
//                        ad.dismiss();
                        /*getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });*/
                        onImageUpload.onUpload(uploadResult.get("url")+"");
//                        uploadImageToDb(uploadResult.get("url")+"");

                    } else {
                        ad.dismiss();
                        Log.d("hey", "error");

                    }

                }
            }).start();

//            new Thread(runnable).start();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    void uploadImageToDb(final String url){

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ad.dismiss();
                Log.d("Response",response);
                try {
                    JSONObject ob = new JSONObject(response);
                    if(ob.getInt("res")==1){
                        Tools.getGeneralEditor(getActivity()).putString(Constants.PROFILE_IMAGE,url).commit();
                        Tools.loadProfileImage(url,image);
                    }else
                    Tools.initCustomToast(getActivity(),ob.getString("msg"));

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
        Map<String, String> params = new HashMap<>();
        params.put("imageUrl",url);

        NetworkManager.getInstance(getActivity()).sendPostRequestWithHeader(Urls.Save_Pic,params,listener,errorListener,getActivity());

    }
}
