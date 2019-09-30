package com.quanutrition.app.profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.quanutrition.app.R;
import com.quanutrition.app.Utils.Constants;
import com.quanutrition.app.Utils.NetworkManager;
import com.quanutrition.app.Utils.Tools;
import com.quanutrition.app.selectiondialogs.DialogUtils;
import com.quanutrition.app.selectiondialogs.MultipleSelectionModel;
import com.quanutrition.app.selectiondialogs.SingleSelectionModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.xiaofeng.flowlayoutmanager.Alignment;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MedicalHistory extends AppCompatActivity implements View.OnClickListener {

    RecyclerView reports_re;
    String bloodGroup_String ;
    Uri currImageURI;
    AlertDialog alertDialog1;
    String url;
    LinearLayout femaleDetails;
    ArrayList<ReportsModel> reportsList;
    ReportsGridAdapter adapter;
    TextView upload_textView, save;
    RadioGroup pregnant_radio, lactation_radio,anyMedicalHistory;
    static RadioButton radioButton;
    JSONArray url_array,diseases_array;
    String pregnant_string, lactation_string,physicalActivity_string,anyMedicalHistoryTxt;
    EditText bloodGroup, physicalActivity, diseases,editAnyHistory;
    ArrayList<MultipleSelectionModel> diseasesList = new ArrayList<>();
    ArrayList<MultipleSelectionModel> diseasesListFull = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_history);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
//        Tools.setSystemBarColorCustom(this, R.color.colorAccent);
        getSupportActionBar().setTitle("Medical History");

        reports_re = findViewById(R.id.reports_re);
        upload_textView = findViewById(R.id.upload);
        save = findViewById(R.id.save);
        bloodGroup = findViewById(R.id.bloodGroup);
        physicalActivity = findViewById(R.id.physicalActivity);
        diseases = findViewById(R.id.diseases);
        pregnant_radio = findViewById(R.id.pregnant);
        lactation_radio = findViewById(R.id.lactating);
        femaleDetails = findViewById(R.id.femaleDetails);
        editAnyHistory = findViewById(R.id.editAnyHistory);
        anyMedicalHistory = findViewById(R.id.anyMedicalHistory);

        editAnyHistory.setVisibility(View.GONE);

        bloodGroup.setFocusable(false);
        physicalActivity.setFocusable(false);
        diseases.setFocusable(false);
        if(Tools.getGeneralSharedPref(this).getString(Constants.GENDER,"male").equalsIgnoreCase("female")){
            femaleDetails.setVisibility(View.VISIBLE);
        }else{
            femaleDetails.setVisibility(View.GONE);
        }

        upload_textView.setOnClickListener(this);
        save.setOnClickListener(this);
        bloodGroup.setOnClickListener(this);
        physicalActivity.setOnClickListener(this);
        diseases.setOnClickListener(this);

      //  requestFetchDiseases();

        //diseasesListFull = DialogUtils.getMultipleArrayListWithResource(this, R.array.diseases);

        reportsList = new ArrayList<>();
        physical = new ArrayList<>();
        Uri uri = null;

        adapter = new ReportsGridAdapter(reportsList, this, new ReportsGridAdapter.OnClickListener() {
            @Override
            public void onClose(View view, int position) {
                reportsList.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
        RecyclerView.LayoutManager flowLayoutManager = new FlowLayoutManager();
        ((FlowLayoutManager) flowLayoutManager).setAlignment(Alignment.LEFT);
        ((FlowLayoutManager) flowLayoutManager).setAutoMeasureEnabled(true);
        ((FlowLayoutManager) flowLayoutManager).maxItemsPerLine(5);

        reports_re.setLayoutManager(flowLayoutManager);
        reports_re.setAdapter(adapter);

        pregnant_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = pregnant_radio.getCheckedRadioButtonId();
                radioButton = findViewById(id);
                if (radioButton != null) {
                    pregnant_string = radioButton.getText().toString();
                }
            }
        });

        lactation_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = lactation_radio.getCheckedRadioButtonId();
                radioButton = findViewById(id);
                if (radioButton != null) {
                    lactation_string = radioButton.getText().toString();
                }
            }
        });

        anyMedicalHistory.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = anyMedicalHistory.getCheckedRadioButtonId();
                radioButton = findViewById(id);
                if(id == R.id.yesMH){
                    editAnyHistory.setVisibility(View.VISIBLE);
                }else{
                    editAnyHistory.setText("");
                    editAnyHistory.setVisibility(View.GONE);
                }
            }
        });

        requestFetch();


    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.upload) {

//            showSelctionDialog(2);

            Tools.openGallery(2, MedicalHistory.this);
        } else if (id == R.id.save) {

            android.util.Log.d("myTag","I am here");

            bloodGroup_String = bloodGroup.getText().toString();
            physicalActivity_string = physicalActivity.getText().toString().replace('\n',' ');
            int radioId = anyMedicalHistory.getCheckedRadioButtonId();
            radioButton = findViewById(radioId);
            if(radioButton.getText().toString().equalsIgnoreCase("yes")){
                anyMedicalHistoryTxt = editAnyHistory.getText().toString().trim();
            }else{
                anyMedicalHistoryTxt = "No";
            }
            url_array = new JSONArray();
            for(int i =0;i<reportsList.size();i++)
            {
                url_array.put(reportsList.get(i).getUrl());
            }
            diseases_array = new JSONArray();
            for(int i =0;i<diseasesList.size();i++)
            {
                try {
                JSONObject object = new JSONObject();
                object.put("id",diseasesList.get(i).getId());
                object.put("name",diseasesList.get(i).getLabel());
                /*object.put("duration","");
                object.put("severity","");*/

                diseases_array.put(object);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            if(bloodGroup_String.equalsIgnoreCase("") || physicalActivity_string.equals("") || pregnant_string.equals("")||lactation_string.equals("")||anyMedicalHistoryTxt.equals(""))
            {
                Tools.initCustomToast(getApplicationContext(),"Please fill all the information to continue");
            }
            else {
                requestSave();
            }



        } else if (id == R.id.bloodGroup) {
            String[] array = getResources().getStringArray(R.array.BloodGroups);
            DialogUtils.getCustomPicker(this, "Select Blood Group", array, new DialogUtils.OnCustomItemPicked() {
                @Override
                public void onNumberPicked(String selected) {
                    bloodGroup.setText(selected);
                }
            });
        } else if (id == R.id.physicalActivity) {
            DialogUtils.getSingleSelectionDialog(this, physical, new DialogUtils.OnSingleItemSelectedListener() {
                @Override
                public void onItemSelected(int position, SingleSelectionModel item) {
                    physicalActivity.setText(item.getLabel());
                }
            });
        } else if (id == R.id.diseases) {
            DialogUtils.getMultipleSearchDialog(this, diseasesListFull, new DialogUtils.OnMultipleItemsSelected() {



                @Override
                public void onMultipleItemsSelected(ArrayList<MultipleSelectionModel> items) {

                    if (items.size() > 0) {
                        setDiseasesText(items);

                    }
                }
            });
        }

    }


    void showSelctionDialog(final int pos) {
        Log.d("manya","manya");
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MedicalHistory.this);
        alertDialog.setTitle("Choose Media Type");
        final String[] listItems = {"Open Camera", "Select from Gallery"};


        alertDialog.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)  {
                alertDialog1.dismiss();
                if (i == 0) {
                    Log.d("manya","0");


                    Tools.getImageFromCamera(pos, MedicalHistory.this, "/dietitio");
                } else if (i == 1) {
                    Tools.openGallery(2, MedicalHistory.this);

                    //Tools.openGallery(pos, MedicalHistory.this);
                }
            }
        });

        alertDialog1 = alertDialog.create();
        alertDialog1.show();
    }

    FirebaseStorage mFirebaseStorage;
    StorageReference mStorageReference;
    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        android.util.Log.d("Activity Result", requestCode + " ::    " + resultCode + "  ::  ");
        mFirebaseStorage = FirebaseStorage.getInstance();
        mStorageReference = mFirebaseStorage.getReference().child("chat_photos");
        try{
            currImageURI = data.getData();
            Uri selectedImageUri = data.getData();
            final StorageReference ref1 = mStorageReference.child(selectedImageUri.getLastPathSegment());
            UploadTask uploadTask = ref1.putFile(selectedImageUri);
            final androidx.appcompat.app.AlertDialog ad = Tools.getDialog("Uploading Image...",this);
            ad.show();
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        ad.dismiss();
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return ref1.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    ad.dismiss();
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        reportsList.add(new ReportsModel(downloadUri+"", currImageURI));
                        adapter.notifyDataSetChanged();
                    } else {
                        // Handle failures
                        // ...
                        Tools.initNetworkErrorToast(MedicalHistory.this);
                    }
                }
            });
        }catch (Exception e){
            Tools.initCustomToast(this,"Some error occurred. Please try again!");
        }
        /*try {
            uploadPhoto(currImageURI,MedicalHistory.this);
            adapter.notifyDataSetChanged();

        } catch (IOException e) {
            e.printStackTrace();
        }*/


    }

    void requestSave(){
        final androidx.appcompat.app.AlertDialog ad = Tools.getDialog("Requesting ...",this);
        ad.show();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ad.dismiss();

                try {
                    JSONObject object = new JSONObject(response);
                    int res = object.getInt("res");
                    if(res==1)
                    {
                        if(diseasesList.size()>0) {
                            ArrayList<String> list = new ArrayList<>();
                            for (int i = 0; i < diseasesList.size(); i++) {
                                list.add(diseasesList.get(i).getLabel());
                            }
                            Intent intent = new Intent(MedicalHistory.this, DiseaseDetailsActivity.class);
                            intent.putStringArrayListExtra("diseases", list);
                            finish();
                            startActivity(intent);
                        }else{
                            finish();
                        }

                    }
                    Tools.initCustomToast(getApplicationContext(),object.getString("msg"));

                } catch (JSONException e) {
                    e.printStackTrace();
                    Tools.initCustomToast(getApplicationContext(),"Some Error");
                }

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ad.dismiss();
                Tools.initNetworkErrorToast(MedicalHistory.this);
                Log.d("Error",error.toString());
                Log.d("myTag","I am here");
            }
        };
        Map<String, String> params = new HashMap<>();
        params.put("bloodgroup",bloodGroup_String);
        params.put("pregnant",pregnant_string);
        params.put("lactating",lactation_string);
        params.put("physicalactivity",physicalActivity_string);
        params.put("url",url_array+"");
        params.put("any_medical",anyMedicalHistoryTxt);
        params.put("disease",diseases_array+"");

        Log.d("data",diseases_array+"");


        NetworkManager.getInstance(this).sendPostRequestWithHeader(Urls.save_medical_info,params,listener,errorListener,this);

    }


    ArrayList<SingleSelectionModel> physical;
    void requestFetch(){
        final androidx.appcompat.app.AlertDialog ad = Tools.getDialog(" Please Wait ...",this);
        ad.show();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ad.dismiss();
                Log.d("medical",response);
                try {
                    JSONObject medicalInfoOuter = new JSONObject(response);
                    int res = medicalInfoOuter.getInt("res");
                    String msg = medicalInfoOuter.getString("msg");
                    JSONObject medicalInfodata = medicalInfoOuter.getJSONObject("data");
                    if(!medicalInfodata.getString("bloodgroup").equalsIgnoreCase("N.A"))
                    bloodGroup.setText(medicalInfodata.getString("bloodgroup"));
                    if(!medicalInfodata.getString("physicalactivity").equalsIgnoreCase("N.A"))
                    physicalActivity.setText(medicalInfodata.getString("physicalactivity"));
                    setRadio(medicalInfodata.getString("pregnant"),"pregnant");
                    setRadio(medicalInfodata.getString("lactating"),"lactating");
                    JSONArray dataArray = medicalInfodata.getJSONArray("d_list");

                    physical = new ArrayList<>();


                    if(Tools.getGeneralSharedPref(MedicalHistory.this).getString(Constants.GENDER,"male").equalsIgnoreCase("female")){
                        femaleDetails.setVisibility(View.VISIBLE);
                    }else{
                        femaleDetails.setVisibility(View.GONE);
                    }

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject diseaseDataObject = dataArray.getJSONObject(i);
                        diseasesListFull.add(new MultipleSelectionModel(diseaseDataObject.getInt("id") + "", diseaseDataObject.getString("name"), false));

                    }

                    JSONArray pa_list = medicalInfodata.getJSONArray("pa_list");
                    for(int i=0;i<pa_list.length();i++){
                        physical.add(new SingleSelectionModel("1",pa_list.getString(i)));
                    }


                    JSONArray reportsArray = medicalInfodata.getJSONArray("report");
                    for(int i =0;i<reportsArray.length();i++)
                    {
                        reportsList.add(new ReportsModel(reportsArray.getString(i), null));adapter.notifyDataSetChanged();
                    }
                    JSONArray diseasesArray = medicalInfodata.getJSONArray("disease");
                    setDiseases(diseasesArray);
                    setDiseasesText(diseasesListFull);



                    if(medicalInfodata.optString("any_medical","No").equalsIgnoreCase("No")){
                        ((RadioButton)findViewById(R.id.noMH)).setChecked(true);
                        editAnyHistory.setVisibility(View.GONE);
                    }else{
                        ((RadioButton)findViewById(R.id.yesMH)).setChecked(true);
                        editAnyHistory.setVisibility(View.VISIBLE);
                        editAnyHistory.setText(medicalInfodata.optString("any_medical",""));
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
                Tools.initNetworkErrorToast(MedicalHistory.this);
                Log.d("Error",error.toString());
                Log.d("myTag","I am here");
            }
        };



        NetworkManager.getInstance(this).sendPostRequestWithoutParams(Urls.get_medical_info,listener,errorListener,this);

    }

    public void setDiseases(JSONArray diseases_array)
    {
        try {
            for (int i = 0; i < diseases_array.length(); i++) {
                JSONObject diseaseObject = diseases_array.getJSONObject(i);
                for (int j = 0; j < diseasesListFull.size(); j++) {
                    if (diseasesListFull.get(j).getLabel().equalsIgnoreCase(diseaseObject.getString("name"))) {
                        diseasesListFull.get(j).setSelected(true);
                    }
                }
                setDiseasesText(diseasesListFull);

            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    public void setRadio(String selection, String type )
    {
        if(type.equalsIgnoreCase("pregnant"))
        {
            if(selection.equalsIgnoreCase("No"))
            {
                pregnant_radio.check(R.id.noPregnant);
            }
            else {
                pregnant_radio.check(R.id.yesPregnant);
            }

        }
        else
        {
            if(selection.equalsIgnoreCase("Yes"))
            {
                lactation_radio.check(R.id.yesLactating);
            }
            else
            {
                lactation_radio.check(R.id.noLactating);
            }
        }
    }

    public void setDiseasesText(ArrayList<MultipleSelectionModel> item_list)
    {
        String string = "";
        ArrayList<MultipleSelectionModel> temp = new ArrayList<>();
        for (int i = 0; i < item_list.size(); i++) {
            if (item_list.get(i).isSelected()) {
                temp.add(item_list.get(i));
                // diseasesList.add(items.get(i));
            }
//                            string+=items.get(i).getLabel()+"   ";
        }
        diseasesList = temp;
        if (temp.size() == 1) {
            string = temp.get(0).getLabel();
        } else if (temp.size() == 0) {
            string = "";

        } else {
            string = temp.size() + " diseases selected";

        }
        diseases.setText(string);


    }

    public  void uploadPhoto(final Uri currImageURI, final Context context) throws IOException {

        Map config = new HashMap();
        config.put("cloud_name", "dietncureapp");
        config.put("api_key", "797379138679915");
        config.put("api_secret", "SG2PMPFpZh8Etio6vw6DyYUtu5Y");
        final Cloudinary cloudinary = new Cloudinary(config);
        final androidx.appcompat.app.AlertDialog ad = Tools.getDialog("Uploading...", context);
        ad.show();
        try {
            final Runnable runnable = new Runnable() {

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
                        ad.dismiss();
                        reportsList.add(new ReportsModel(url, currImageURI));
                        adapter.notifyDataSetChanged();
                       // callAdapter();

                        url = Objects.requireNonNull(uploadResult.get("url")).toString() ;

                    } else {
                        ad.dismiss();
                        Log.d("hey", "error");

                    }

                }
            };

            new Thread(runnable).start();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }




}
