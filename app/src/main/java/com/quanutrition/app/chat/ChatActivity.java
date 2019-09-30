package com.quanutrition.app.chat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.quanutrition.app.R;
import com.quanutrition.app.Utils.Constants;
import com.quanutrition.app.Utils.NetworkManager;
import com.quanutrition.app.Utils.SqliteDbHelper;
import com.quanutrition.app.Utils.Tools;
import com.quanutrition.app.appointment.AppointModel;
import com.quanutrition.app.appointment.AppointParentAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
//import com.google.firebase.ml.naturallanguage.smartreply.FirebaseSmartReply;
//import com.google.firebase.ml.naturallanguage.smartreply.FirebaseTextMessage;
//import com.google.firebase.ml.naturallanguage.smartreply.SmartReplySuggestion;
//import com.google.firebase.ml.naturallanguage.smartreply.SmartReplySuggestionResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class ChatActivity extends AppCompatActivity {
    public static BottomSheetBehavior bottomSheetBehavior;
    private String mParam1;
    private String mParam2;
    RecyclerView chatmessages;
    ArrayList<ChatMessageModel> chats;
    ChatAdapter chatAdapter;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mMessagesDatabaseReference,readReference;
    ChildEventListener mChildEventLintener;
    FirebaseStorage mFirebaseStorage;
    StorageReference mStorageReference;
    private ImageView mPhotoPickerButton;
    private EmojiconEditText mMessageEditText;
    private ImageView emojibtn;
    private ImageButton mSendButton;
    ProgressDialog pb;
    static int flag = 0;
    String dietitianId, userId;
    String startMessage = "Hey! I am your Nutritionist. I'd be happy to help you out in achieving your health Goals and create a customised diet for you.";
    String closeMessage = "We are closed right now!\nPlease book a call appointment for tomorrow by clicking the icon on the top.";
//    private ChatFragment.OnFragmentInteractionListener mListener;

    /**
     * Dialog box variable
     */
    ArrayList<AppointModel.ChildListAppoint> allSlots;
    public static AlertDialog alertDialog1;
    RecyclerView recyclerView;
    ArrayList<AppointModel> parentData;
    ArrayList<AppointModel.ChildListAppoint> morning, afternoon, evening, night;
    ArrayList<Integer> time;
    public static TextView dateText;
    public static long dateCalendarStart,dateCalendarEnd;
    int tim,openflag=0;
    boolean availabe = true;
    private DatePickerDialog pickerDialog;
    private SimpleDateFormat dateFormatter;
    RelativeLayout loadingLayout;
    AppointParentAdapter adapter;

    RecyclerView suggestions;
    ArrayList<SuggestionsModel> suggesionList;
    SuggestionsAdapter suggestionsAdapter;

    private BottomSheetBehavior mBehavior;
    private BottomSheetDialog mBottomSheetDialog;
    private View bottom_sheet;
//    String clinicId;


    //Firebase Replies
//    List<FirebaseTextMessage> conversation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chat);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setTitle("Connect With Us");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //        showDialog();
        /**
         * getting views
         */

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(1);

        mPhotoPickerButton = (ImageView) findViewById(R.id.photoPickerButton);
        mMessageEditText = (EmojiconEditText) findViewById(R.id.editTextMessage);
        emojibtn = (ImageView) findViewById(R.id.buttonEmoji);
        LinearLayout rootView = (LinearLayout) findViewById(R.id.root);
        EmojIconActions emojIcon=new EmojIconActions(ChatActivity.this,rootView,mMessageEditText,emojibtn);
        emojIcon.ShowEmojIcon();
        mSendButton = (ImageButton) findViewById(R.id.sendButton);
        bottom_sheet = findViewById(R.id.bottom_sheet);
        mBehavior = BottomSheetBehavior.from(bottom_sheet);

        suggestions = findViewById(R.id.suggestions);
        suggesionList = new ArrayList<>();
        suggestionsAdapter = new SuggestionsAdapter(suggesionList, this, new SuggestionsAdapter.OnSuggestionSelected() {
            @Override
            public void onClick(String text) {
                Calendar calendar = Calendar.getInstance();
                Date c = calendar.getTime();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                TimeZone obj = TimeZone.getTimeZone("UTC");
                df.setTimeZone(obj);
                String time = df.format(c);
                if(chats.size()>0){
                    ChatMessageModel model = chats.get(chats.size()-1);
                    Log.d("index",chats.size()-1+"");
                    Log.d("TIME",time);
                    if(!(model.getTimestamp().split(" ")[0].equals(time.split(" ")[0]))){
                        mMessagesDatabaseReference.push().setValue(new ChatMessageModel(null,null,time,null,"0"));
                    }
                }else{
                    mMessagesDatabaseReference.push().setValue(new ChatMessageModel(null,null,time,null,"0"));
                }


                ChatMessageModel message = new ChatMessageModel(userId,text, time, null,"0");
                mMessagesDatabaseReference.push().setValue(message);

                String name  = Tools.getGeneralSharedPref(ChatActivity.this).getString(Constants.PROFILE_NAME,"Dieter "+userId);
                NetworkManager.getInstance(ChatActivity.this).sendNotification(ChatActivity.this,"New Message From "+name,text,"1");
                String image = Tools.getGeneralSharedPref(ChatActivity.this).getString(Constants.PROFILE_IMAGE,"");
                String custom_id = Tools.getGeneralSharedPref(ChatActivity.this).getString(Constants.CUSTOM_ID,"unknown");
                ChatOverviewModel overviewModel = new ChatOverviewModel(text,time,"false",userId,name,image,custom_id);
                DatabaseReference overviewReference = mFirebaseDatabase.getReference().child("Overview").child(dietitianId);
                overviewReference.child(userId).setValue(overviewModel);


                /*if(Integer.parseInt(time.split(" ")[1].split(":")[0])>19|| Integer.parseInt(time.split(" ")[1].split(":")[0])<9){
                    mMessagesDatabaseReference.push().setValue(new ChatMessageModel(dietitianId,closeMessage,time,null,"0"));
                }*/
                suggesionList.clear();
                suggestionsAdapter.notifyDataSetChanged();
            }
        });
        RecyclerView.LayoutManager suggestionsLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false);
        suggestions.setLayoutManager(suggestionsLayoutManager);
        suggestions.setAdapter(suggestionsAdapter);

        /**
         * initializing database and storage reference
         */
        /*if(flag==0) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            flag=1;
        }*/
        mFirebaseDatabase = FirebaseDatabase.getInstance();
//        mFirebaseDatabase.setPersistenceEnabled(true);
        mFirebaseStorage = FirebaseStorage.getInstance();
        SharedPreferences sharedPreferences= ChatActivity.this.getSharedPreferences(Constants.MyPreferences, MODE_PRIVATE);
        userId=sharedPreferences.getString(Constants.USER_ID,"5");
        dietitianId=sharedPreferences.getString(Constants.DIETITIAN_ID,"2");
//        clinicId = sharedPreferences.getString(Constants.CLINIC_ID,"0");
//        conversation = new ArrayList<>();
        readReference = FirebaseDatabase.getInstance().getReference().child("Read").child(userId);
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("Messages").child(userId);
        //Check if the node exists

        mFirebaseDatabase.getReference().child("Messages").child(userId).limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("Akshit test 1",dataSnapshot.toString());

                if(dataSnapshot.getValue()==null) {
                    Log.d("Data child node", "Does not exist");
                    Calendar calendar = Calendar.getInstance();
                    Date c = calendar.getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    TimeZone obj = TimeZone.getTimeZone("UTC");
                    df.setTimeZone(obj);
                    String time = df.format(c);
                    mMessagesDatabaseReference.push().setValue(new ChatMessageModel(null, null, time, null, "0"));
                    ChatMessageModel message = new ChatMessageModel(dietitianId, startMessage, time, null, "0");
                    mMessagesDatabaseReference.push().setValue(message);

                    /*ChatMessageModel message1 = new ChatMessageModel(dietitianId, "%%BOOKAPPOINTMENT%%", time, null, "0");
                    mMessagesDatabaseReference.push().setValue(message1);*/
                    DatabaseReference overviewReference = mFirebaseDatabase.getReference().child("Overview").child(dietitianId);
                    String image = Tools.getGeneralSharedPref(ChatActivity.this).getString(Constants.PROFILE_IMAGE,"");
                    String name = Tools.getGeneralSharedPref(ChatActivity.this).getString(Constants.PROFILE_NAME,"User");
//                    ChatOverviewModel overviewModel = new ChatOverviewModel(mMessageEditText.getText().toString().trim(),time,"false",userId,name,image);
//                    overviewReference.child(userId).setValue(overviewModel);
//                    NetworkManager.getInstance(ChatActivity.this).sendNotification(ChatActivity.this,"New Message From "+name,mMessageEditText.getText().toString().trim(),"1");
                }else{
                    sendR();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


       /* mFirebaseDatabase.getReference().child("Messages").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.hasChild(userId)) {
                    // run some code
                    Log.d("Data child node","Does not exist");
                    Calendar calendar = Calendar.getInstance();
                    Date c = calendar.getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String time = df.format(c);
                    mMessagesDatabaseReference.push().setValue(new ChatMessageModel(null,null,time,null,"0"));
                    ChatMessageModel message = new ChatMessageModel(dietitianId,startMessage, time, null,"0");
                    mMessagesDatabaseReference.push().setValue(message);
                }else{
                    if(!snapshot.child(userId).hasChild(clinicId)){
                        Calendar calendar = Calendar.getInstance();
                        Date c = calendar.getTime();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String time = df.format(c);
                        mMessagesDatabaseReference.push().setValue(new ChatMessageModel(null,null,time,null,"0"));
                        ChatMessageModel message = new ChatMessageModel(dietitianId,startMessage, time, null,"0");
                        mMessagesDatabaseReference.push().setValue(message);
                        ChatMessageModel bookAppoint = new ChatMessageModel(dietitianId,"%%BOOKAPPOINTMENT%%",time,null,"0");
                        mMessagesDatabaseReference.push().setValue(bookAppoint);
                    }else{
                        Log.d("Data",snapshot+"");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

*/

        mStorageReference = mFirebaseStorage.getReference().child("chat_photos");


        mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showBottomSheetDialog();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), 12345);

            }
        });

        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(250)});
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 250) {
                    Tools.initCustomToast(ChatActivity.this,"Text Limit reached!");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mMessageEditText.getText().toString().trim().equals("")) {

                    Calendar calendar = Calendar.getInstance();
                    Date c = calendar.getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Log.d("Time Local",df.format(c));
                    TimeZone obj = TimeZone.getTimeZone("UTC");
                    df.setTimeZone(obj);
                    String time = df.format(c);
                    Log.d("Time in UTC",time);
                    if(chats.size()>0){
                        ChatMessageModel model = chats.get(chats.size()-1);
                        Log.d("index",chats.size()-1+"");
                        Log.d("TIME",time);
                        if(!(model.getTimestamp().split(" ")[0].equals(time.split(" ")[0]))){
                            mMessagesDatabaseReference.push().setValue(new ChatMessageModel(null,null,time,null,"0"));
                        }
                    }else{
                        mMessagesDatabaseReference.push().setValue(new ChatMessageModel(null,null,time,null,"0"));
                    }


                    ChatMessageModel message = new ChatMessageModel(userId,mMessageEditText.getText().toString().trim(), time, null,"0");
                    mMessagesDatabaseReference.push().setValue(message);
                    String name  = Tools.getGeneralSharedPref(ChatActivity.this).getString(Constants.PROFILE_NAME,"Dieter "+userId);
                    NetworkManager.getInstance(ChatActivity.this).sendNotification(ChatActivity.this,"New Message From "+name,mMessageEditText.getText().toString().trim(),"1");
                    DatabaseReference overviewReference = mFirebaseDatabase.getReference().child("Overview").child(dietitianId);
                    String image = Tools.getGeneralSharedPref(ChatActivity.this).getString(Constants.PROFILE_IMAGE,"");
                    String custom_id = Tools.getGeneralSharedPref(ChatActivity.this).getString(Constants.CUSTOM_ID,"unknown");
                    ChatOverviewModel overviewModel = new ChatOverviewModel(mMessageEditText.getText().toString().trim(),time,"false",userId,name,image,custom_id);
                    overviewReference.child(userId).setValue(overviewModel);
                    /*if(Integer.parseInt(time.split(" ")[1].split(":")[0])>19|| Integer.parseInt(time.split(" ")[1].split(":")[0])<9){
                        mMessagesDatabaseReference.push().setValue(new ChatMessageModel(dietitianId,closeMessage,time,null,"0"));
                    }*/
                    mMessageEditText.setText("");
                }
            }
        });

        /**
         * Data to disease type
         */
        chats = new ArrayList<>();
//        chats.add(new ChatMessageModel("Customer","hello","12-09-2018",null));
//        chats.add(new ChatMessageModel("Customer","hello","12-09-2018",null));
//        chats.add(new ChatMessageModel("1","hello","12-09-2018",null));
//        chats.add(new ChatMessageModel("Customer","hello","12-09-2018",null));
//        chats.add(new ChatMessageModel("1","hello","12-09-2018",null));
//        chats.add(new ChatMessageModel("2","hello","12-09-2018",null));
//        chats.add(new ChatMessageModel("Customer","hello","12-09-2018",null));


        /**
         * Recycler View for putting data into list item
         */
        chatmessages = findViewById(R.id.chatmessages);
        chatmessages.setHasFixedSize(true);
        chatmessages.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        chatAdapter = new ChatAdapter(chats,ChatActivity.this);
        chatmessages.setAdapter(chatAdapter);
        pb = new ProgressDialog(ChatActivity.this);
        pb.setMessage("Fetching Chats");

        chatAdapter.setOnItemAddedListener(new ChatAdapter.OnItemAddedListener() {
            @Override
            public void onAdded() {
                chatmessages.scrollToPosition(chats.size()-1);
            }
        });


//        pb.show();
//        sendR();
        initalize();
        // get the bottom sheet view

        openflag=0;
//        showDialog();
        // set callback for changes

        if(getIntent().hasExtra("action")){
            Calendar calendar = Calendar.getInstance();
            Date c = calendar.getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            TimeZone obj = TimeZone.getTimeZone("UTC");
            df.setTimeZone(obj);
            String time = df.format(c);
            chats.add(new ChatMessageModel(userId,"%%BOOKAPPOINTMENT%%",time,null,"0"));
            chatAdapter.notifyDataSetChanged();
            chatmessages.scrollToPosition(chats.size()-1);
        }

    }



    /*void getReplies(){
        FirebaseSmartReply smartReply = FirebaseNaturalLanguage.getInstance().getSmartReply();
        smartReply.suggestReplies(conversation)
                .addOnSuccessListener(new OnSuccessListener<SmartReplySuggestionResult>() {
                    @Override
                    public void onSuccess(SmartReplySuggestionResult result) {
                        if (result.getStatus() == SmartReplySuggestionResult.STATUS_NOT_SUPPORTED_LANGUAGE) {
                            // The conversation's language isn't supported, so the
                            // the result doesn't contain any suggestions.
                        } else if (result.getStatus() == SmartReplySuggestionResult.STATUS_SUCCESS) {
                            // Task completed successfully
                            // ...
                            suggesionList.clear();
                            for (SmartReplySuggestion suggestion : result.getSuggestions()) {
                                String replyText = suggestion.getText();
                                if(suggestionFlag)
                                suggesionList.add(new SuggestionsModel(replyText));
                                Log.d("Replies : ",replyText);
                            }
                            suggestionsAdapter.notifyDataSetChanged();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Task failed with an exception
                        // ...
                    }
                });
    }*/

    boolean suggestionFlag = false;
    boolean firstFlag = false;
    void initalize(){
        if(mChildEventLintener==null) {
//            final AlertDialog ad = Tools.getDialog("Fetching Data...",this);
//            ad.show();
            mChildEventLintener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    pb.dismiss();
//                    ad.dismiss();
                    if(firstFlag) {
                        ChatMessageModel message = dataSnapshot.getValue(ChatMessageModel.class);
                        message.setKey(dataSnapshot.getKey());
                        chats.add(message);
                        Log.d("I'm here", message.getTimestamp());
                        Collections.sort(chats);
                        chatAdapter.notifyDataSetChanged();
                        chatmessages.scrollToPosition(chats.size() - 1);
                        /*if (message.getMessage() != null) {
                            conversation = new ArrayList<>();
                            suggesionList.clear();
                            suggestionsAdapter.notifyDataSetChanged();
                            if (message.getSender().equalsIgnoreCase(userId)) {
                                conversation.add(FirebaseTextMessage.createForLocalUser(
                                        message.getMessage(), System.currentTimeMillis()));
                                suggestionFlag = false;
                            } else {
                                conversation.add(FirebaseTextMessage.createForRemoteUser(
                                        message.getMessage(), System.currentTimeMillis(), message.getSender()));
                                if (!message.getMessage().equalsIgnoreCase(closeMessage)) {
                                    suggestionFlag = true;
                                    getReplies();
                                } else {
                                    suggestionFlag = false;
                                }
                            }
                        }*/
                    }else{
                        firstFlag= true;
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    pb.dismiss();
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    pb.dismiss();
//                    ad.dismiss();
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    pb.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    pb.dismiss();
//                    ad.dismiss();
                }
            };
            mMessagesDatabaseReference.limitToLast(1).addChildEventListener(mChildEventLintener);



        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==12345&&resultCode==RESULT_OK){
            Uri selectedImageUri = data.getData();
            final StorageReference ref1 = mStorageReference.child(selectedImageUri.getLastPathSegment());
            UploadTask uploadTask = ref1.putFile(selectedImageUri);
            final AlertDialog ad = Tools.getDialog("Uploading Image...",this);
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

                        Calendar calendar = Calendar.getInstance();
                        Date c = calendar.getTime();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        TimeZone obj = TimeZone.getTimeZone("UTC");
                        df.setTimeZone(obj);
                        String time = df.format(c);
                        ChatMessageModel message = new ChatMessageModel(userId,null, time, downloadUri.toString(),"1");
                        mMessagesDatabaseReference.push().setValue(message);
                        String name  = Tools.getGeneralSharedPref(ChatActivity.this).getString(Constants.PROFILE_NAME,"Dieter "+userId);
                        NetworkManager.getInstance(ChatActivity.this).sendNotification(ChatActivity.this,"New Message From "+name,"Sent an Attachment","1");
                        DatabaseReference overviewReference = mFirebaseDatabase.getReference().child("Overview").child(dietitianId);
                        String image = Tools.getGeneralSharedPref(ChatActivity.this).getString(Constants.PROFILE_IMAGE,"");
                        String custom_id = Tools.getGeneralSharedPref(ChatActivity.this).getString(Constants.CUSTOM_ID,"unknown");
                        ChatOverviewModel overviewModel = new ChatOverviewModel("Sent an attachment",time,"false",userId,name,image,custom_id);
                        overviewReference.child(userId).setValue(overviewModel);
                        /*if(Integer.parseInt(time.split(" ")[1].split(":")[0])>19|| Integer.parseInt(time.split(" ")[1].split(":")[0])<9){
                            mMessagesDatabaseReference.push().setValue(new ChatMessageModel(dietitianId,closeMessage,time,null,"0"));
                        }*/
                    } else {
                        // Handle failures
                        // ...
                        Tools.initNetworkErrorToast(ChatActivity.this);
                    }
                }
            });
//            .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Uri downloadUrl = taskSnapshot.getUploadSessionUri();
//                    FriendlyMessage friendlyMessage = new FriendlyMessage(null,mUsername,downloadUrl.toString());
//                    mMessagesDatabaseReference.push().setValue(friendlyMessage);
//                }
//            });
        }
    }

    /**
     * dialog for Appointment
     */
    public void showDialog() {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        // Get the layout inflater
//        LayoutInflater linf = LayoutInflater.from(getActivity());
//        final View inflator = linf.inflate(R.layout.activity_appointment, null);
//        inflator.findViewById(R.id.bt_close).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                alertDialog1.dismiss();
//            }
//        });
        recyclerView = findViewById(R.id.recAppointment);
        dateText = findViewById(R.id.txtTodaysDate);
        loadingLayout = findViewById(R.id.loadingLayout);
        parentData = new ArrayList<>();
        morning = new ArrayList<>();
        afternoon = new ArrayList<>();
        evening = new ArrayList<>();
        night = new ArrayList<>();

//        timeDta();
        loadingLayout.setVisibility(View.VISIBLE);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        dateText.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date()) + "");
        allSlots = new ArrayList<>();
        fetchSlots(dateText.getText().toString().trim());
        dateCalendarStart = Calendar.getInstance().getTimeInMillis();
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog

                DatePickerDialog datePickerDialog = new DatePickerDialog(ChatActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                int month = monthOfYear + 1;
                                String Date1;
                                String calendar;
                                if (month/ 10 == 0) {

                                    Date1 = dayOfMonth + "-0" + month + "-" + year;
                                    calendar=year+""+"0" + month+""+dayOfMonth;

                                } else {
                                    Date1 = dayOfMonth + "-" + month + "-" + year;
                                    calendar=year+""+"" + month+""+dayOfMonth;
                                }
                                dateText.setText(Date1);
                                loadingLayout.setVisibility(View.VISIBLE);
                                fetchSlots(dateText.getText().toString().trim());


                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });

        RecyclerView recyclerView = findViewById(R.id.recAppointment);
        recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        adapter = new AppointParentAdapter(ChatActivity.this, parentData);
        recyclerView.setAdapter(adapter);

//        alertDialog.setView(inflator);
//        alertDialog1 = alertDialog.show();

    }

    /**
     * static data for dialog
     */
    private void timeDta(ArrayList<AppointModel.ChildListAppoint> time) {
        parentData.clear();
        morning.clear();
        afternoon.clear();
        evening.clear();
        night.clear();
        for (int i = 0; i < time.size(); i++) {
            AppointModel.ChildListAppoint row = time.get(i);
            tim = Integer.parseInt(row.getTime().split(":")[0]);
            /**
             * Morning
             */
            if (tim >= 8 && tim < 12) {
                morning.add(row);

            }
            /**
             * Afternoon
             */
            if (tim >= 12 && tim < 16) {
                afternoon.add(row);
            }
            /**
             * Evening
             */
            if (tim >= 16 && tim < 19) {
                evening.add(row);
            }
            /**
             * Night
             */
            if (tim >= 19 && tim < 21) {
                night.add(row);
            }
        }

        if (!morning.isEmpty())
            parentData.add(new AppointModel("Morning",getResources().getDrawable(R.drawable.ic_morning), morning));
        if (!afternoon.isEmpty())

            parentData.add(new AppointModel("Afternoon",getResources().getDrawable(R.drawable.ic_afternoon), afternoon));
        if (!evening.isEmpty())
            parentData.add(new AppointModel("Evening",getResources().getDrawable(R.drawable.ic_evening), evening));
        if (!night.isEmpty())
            parentData.add(new AppointModel("Night",getResources().getDrawable(R.drawable.ic_night), night));

        adapter.notifyDataSetChanged();

    }

    void fetchSlots(String date){
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loadingLayout.setVisibility(View.GONE);
                Log.d("ResponseSlots",response);
                try {
                    allSlots.clear();
                    JSONObject ob = new JSONObject(response);
                    if(ob.getInt("res")==1){
                        JSONObject res = ob.getJSONObject("response");
                        JSONArray slots = res.getJSONArray("slots");
                        for(int i=0;i<slots.length();i++){
                            JSONObject slot = slots.optJSONObject(i);
                            if(slot.getInt("status")==1){
                                availabe = true;
                            }else{
                                availabe=false;
                            }
                            allSlots.add(new AppointModel.ChildListAppoint(slot.getString("slot"),availabe));
                        }
                        timeDta(allSlots);
                    }else{

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

                loadingLayout.setVisibility(View.GONE);
                Tools.initNetworkErrorToast(ChatActivity.this);
                Log.d("Error",error.toString());
                Log.d("myTag","I am here");
            }
        };
        Map<String, String> params = new HashMap<>();
        params.put(Constants.DIETITIAN_ID,dietitianId);
        params.put("date",date);

        NetworkManager.getInstance(ChatActivity.this).sendPostRequest(Urls.Get_Slots,params,listener,errorListener,ChatActivity.this);

    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.hasChild("read")) {
                if (dataSnapshot.child("read").getValue().toString().equalsIgnoreCase("false")) {
                    readReference.child("read").setValue("true");
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    @Override
    public void onPause() {
        super.onPause();
        chats.clear();
        chatAdapter.notifyDataSetChanged();
        readReference.removeEventListener(valueEventListener);
        if (mChildEventLintener != null) {
            mMessagesDatabaseReference.removeEventListener(mChildEventLintener);
            mChildEventLintener = null;
        }
//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    @Override
    public void onResume() {
        super.onResume();
        readReference.addValueEventListener(valueEventListener);
        final int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(ChatActivity.this);
        if (status == ConnectionResult.SUCCESS) {
            Log.d("Google Play Services", "Available");
        }

        Log.e("Google Play Services", "Google Play Services not available: " + GooglePlayServicesUtil.getErrorString(status));

        if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
            final Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(status, ChatActivity.this, 1);
            if (errorDialog != null) {
                errorDialog.show();
            }
        }
        sendR();
        firstFlag = false;
        initalize();
    }

//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    private void showBottomSheetDialog() {
        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        final View view = getLayoutInflater().inflate(R.layout.sheet_floating, null);

        LinearLayout image,pdf,appointment,mealChange,extension;
        image = view.findViewById(R.id.image);
        pdf = view.findViewById(R.id.pdf);
        appointment = view.findViewById(R.id.appointment);
        mealChange = view.findViewById(R.id.mealChange);
        extension = view.findViewById(R.id.extensionRequest);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.hide();
//                openGallery();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), 12345);
            }
        });

        appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.hide();
                Calendar calendar = Calendar.getInstance();
                Date c = calendar.getTime();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                TimeZone obj = TimeZone.getTimeZone("UTC");
                df.setTimeZone(obj);
                String time = df.format(c);
                chats.add(new ChatMessageModel(userId,"%%BOOKAPPOINTMENT%%",time,null,"0"));
                chatAdapter.notifyDataSetChanged();
                chatmessages.scrollToPosition(chats.size()-1);
            }
        });
        mealChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.hide();
                Calendar calendar = Calendar.getInstance();
                Date c = calendar.getTime();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                TimeZone obj = TimeZone.getTimeZone("UTC");
                df.setTimeZone(obj);
                String time = df.format(c);
                chats.add(new ChatMessageModel(userId,"%%MEALCHANGE%%",time,null,"0"));
                chatAdapter.notifyDataSetChanged();
                chatmessages.scrollToPosition(chats.size()-1);

            }
        });
        extension.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.hide();
                Calendar calendar = Calendar.getInstance();
                Date c = calendar.getTime();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                TimeZone obj = TimeZone.getTimeZone("UTC");
                df.setTimeZone(obj);
                String time = df.format(c);
                chats.add(new ChatMessageModel(userId,"%%EXTENSION%%",time,null,"0"));
                chatAdapter.notifyDataSetChanged();
                chatmessages.scrollToPosition(chats.size()-1);
            }
        });
        mBottomSheetDialog = new BottomSheetDialog(this);
        mBottomSheetDialog.setContentView(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        // set background transparent
        ((View) view.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));

        mBottomSheetDialog.show();
        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mBottomSheetDialog = null;
            }
        });

    }

    void openGallery(){
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                        startActivityForResult(Intent.createChooser(intent, "Complete action using"), 12345);
                        SqliteDbHelper.fn_imagespath(ChatActivity.this);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            // navigate user to app settings
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    public void sendR(){

        final AlertDialog ad = Tools.getDialog("Fetching data...",this);
        ad.show();
        String apiURL = "https://quanutrition-f5d3f.firebaseio.com/Messages/"+userId+"/.json?print=pretty&orderBy=\"$key\"";
        Log.d("Call to URL ",apiURL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ad.dismiss();
//                text.append(response);
                try {
                    JSONObject data = new JSONObject(response);
                    Iterator<String> keys = data.keys();
                    chats.clear();
                    while(keys.hasNext()){
                        String key = keys.next();
                        if (data.get(key) instanceof JSONObject) {
                            JSONObject ob = data.getJSONObject(key);
                            ChatMessageModel message = new ChatMessageModel(ob.optString("sender",null),ob.optString("message",null),ob.optString("timestamp",null),ob.optString("imageurl",null),ob.optString("fileflag",null));
                            chats.add(message);
                        }

                    }
                    chatAdapter.notifyDataSetChanged();
                    chatmessages.scrollToPosition(chats.size()-1);
                    if(getIntent().hasExtra("action")){
                        Calendar calendar = Calendar.getInstance();
                        Date c = calendar.getTime();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        TimeZone obj = TimeZone.getTimeZone("UTC");
                        df.setTimeZone(obj);
                        String time = df.format(c);
                        chats.add(new ChatMessageModel(userId,"%%BOOKAPPOINTMENT%%",time,null,"0"));
                        chatAdapter.notifyDataSetChanged();
                        chatmessages.scrollToPosition(chats.size()-1);
                    }
                    /*if(chats.get(chats.size()-1).getMessage()!=null)
                    if(!chats.get(chats.size()-1).getSender().equalsIgnoreCase(userId)){
                        if(!chats.get(chats.size()-1).getMessage().equalsIgnoreCase(closeMessage)){
                            conversation = new ArrayList<>();
                            ChatMessageModel message = chats.get(chats.size()-1);
                            conversation.add(FirebaseTextMessage.createForRemoteUser(
                                    message.getMessage(), System.currentTimeMillis(), message.getSender()));
                            suggestionFlag=true;
                            getReplies();
                        }
                    }*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ad.dismiss();
            }
        });
        int MY_SOCKET_TIMEOUT_MS = 50000;
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}
