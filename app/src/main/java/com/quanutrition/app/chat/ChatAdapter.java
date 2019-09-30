package com.quanutrition.app.chat;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.CalendarContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.quanutrition.app.R;
import com.quanutrition.app.Utils.Constants;
import com.quanutrition.app.Utils.NetworkManager;
import com.quanutrition.app.Utils.Tools;
import com.quanutrition.app.appointment.AppointModel;
import com.quanutrition.app.appointment.AppointParentAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.cloudinary.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

import static android.content.Context.MODE_PRIVATE;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    private ArrayList<ChatMessageModel> chatList;
    Context mCtx;
    String userId = "0",dietitianId="0";
    AlertDialog ad1=null;
    RelativeLayout loadingLayout;
    ArrayList<AppointModel> parentData;
    ArrayList<AppointModel.ChildListAppoint> morning, afternoon, evening, night;
    ArrayList<AppointModel.ChildListAppoint> allSlots;
    AppointParentAdapter adapter;
    boolean availabe = true;
    int tim;
    OnItemAddedListener onItemAdded;

    public ChatAdapter(ArrayList<ChatMessageModel> chatList, Context mCtx) {
        this.chatList = chatList;
        this.mCtx = mCtx;
    }

    public interface OnItemAddedListener{
        void onAdded();
    }

    public void setOnItemAddedListener(OnItemAddedListener onItemAdded){
        this.onItemAdded = onItemAdded;
    }

    static String date = "";



    public class MyViewHolder extends RecyclerView.ViewHolder {

        EmojiconTextView chatTextLeft,chatTextRight;
        RelativeLayout sentMessageLayout,recievedMessageLayout;
        ImageView imageLeft,imageRight;
        TextView date,timeLeft,timeRight;
        CircleImageView profileImageLeft,profileImageRight;
        LinearLayout updateProgress;
//        public LinearLayout updateProgress,bookAppointment,mealChange;

//        public TextView timeLeft,timeRight,date,confirm,dateEdit,timeEdit,mealconfirm,mealdateEdit,startdateEdit,extensionconfirm;
//        public EditText noteEdit,days;
//        public LinearLayout appointmentDate,appointmentTime,appointmentNote,appointmentType,mealType,mealDate,startDate,extensionDays,extension;
//        public Spinner typeSpinner,mealtypeSpinner;

        public MyViewHolder(View view) {
            super(view);
            chatTextLeft = view.findViewById(R.id.message_left);
            chatTextRight = view.findViewById(R.id.message_right);
            sentMessageLayout= view.findViewById(R.id.sent_message_layout);
            recievedMessageLayout= view.findViewById(R.id.recieved_message_layout);
            imageLeft = view.findViewById(R.id.img_chat_left);
            imageRight = view.findViewById(R.id.img_chat_right);
            profileImageLeft = view.findViewById(R.id.profileImageLeft);
            profileImageRight = view.findViewById(R.id.profileImageRight);
            date = view.findViewById(R.id.date);
            timeLeft = view.findViewById(R.id.timeLeft);
            timeRight = view.findViewById(R.id.timeRight);
            updateProgress = view.findViewById(R.id.updateProgress);

        }
    }


    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_list_row, parent, false);

        return new ChatAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final ChatAdapter.MyViewHolder holder, final int position) {


        final ChatMessageModel chatModel = chatList.get(position);
        if(chatModel.getSender()==null){
            holder.date.setVisibility(View.VISIBLE);
            holder.recievedMessageLayout.setVisibility(View.GONE);
            holder.sentMessageLayout.setVisibility(View.GONE);
            holder.date.setText(Tools.getLocalTime(chatModel.getTimestamp()).split(" ")[0]);
        }
        else {
            holder.date.setVisibility(View.GONE);
            holder.recievedMessageLayout.setVisibility(View.VISIBLE);
            holder.sentMessageLayout.setVisibility(View.VISIBLE);
            String time = Tools.getLocalTime(chatModel.getTimestamp()).split(" ")[1];
            int newHour = Integer.parseInt(time.split(":")[0]);
            int newMin = Integer.parseInt(time.split(":")[1]);
            String ampm = "AM";
            if (newHour == 12) {
                ampm = "PM";
            } else if (newHour > 12 && newHour < 24) {
                newHour -= 12;
                ampm = "PM";
            } else if(newHour==0){
                newHour += 12;
                ampm = "AM";
            }
            String effectiveTime = newHour + ":" + time.split(":")[1] + " " + ampm;

            SharedPreferences sharedPreferences= mCtx.getSharedPreferences(Constants.MyPreferences, MODE_PRIVATE);
            userId=sharedPreferences.getString(Constants.USER_ID,"30");
            dietitianId=sharedPreferences.getString(Constants.DIETITIAN_ID,"2");
//            clinicId = sharedPreferences.getString(Constants.CLINIC_ID,"0");
            if (chatModel.getSender().equals(userId)) {
                //sent messages here

                holder.recievedMessageLayout.setVisibility(View.GONE);
                holder.sentMessageLayout.setVisibility(View.VISIBLE);
                if (!(chatModel.getImageurl() == null)) {
                    holder.chatTextRight.setVisibility(View.GONE);
                    holder.imageRight.setVisibility(View.VISIBLE);
                    Glide.with(holder.imageRight.getContext())
                            .load(chatModel.getImageurl())
                            .override(400, 400)
                            .animate(android.R.anim.fade_in)
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    return false;
                                }
                            })
                            .into(holder.imageRight);
                    holder.imageRight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(mCtx, ImageViewActivity.class);
                            i.putExtra("url", chatModel.getImageurl());
                            mCtx.startActivity(i);
                        }
                    });
                } else {
                    holder.imageRight.setVisibility(View.GONE);
                    holder.chatTextRight.setText(chatModel.getMessage());
                }
                holder.timeRight.setText(effectiveTime);

            } else {
                holder.updateProgress.setVisibility(View.GONE);
                holder.sentMessageLayout.setVisibility(View.GONE);
                holder.recievedMessageLayout.setVisibility(View.VISIBLE);
                if (!(chatModel.getImageurl() == null)) {
                    holder.chatTextLeft.setVisibility(View.GONE);
                    holder.imageLeft.setVisibility(View.VISIBLE);
                    Glide.with(holder.imageLeft.getContext())
                            .load(chatModel.getImageurl())
                            .override(400, 400)
                            .animate(android.R.anim.fade_in)
                            .into(holder.imageLeft);
                    holder.imageLeft.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(mCtx, ImageViewActivity.class);
                            i.putExtra("url", chatModel.getImageurl());
                            mCtx.startActivity(i);
                        }
                    });

                } else {
                    holder.imageLeft.setVisibility(View.GONE);
                    if(chatModel.getMessage().equals("%%BOOKAPPOINTMENT%%")){
                        holder.updateProgress.setVisibility(View.VISIBLE);
                        holder.chatTextLeft.setVisibility(View.GONE);
                        holder.updateProgress.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                                showDialog();
                                Calendar calendar = Calendar.getInstance();
                                Date c = calendar.getTime();
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                TimeZone obj = TimeZone.getTimeZone("UTC");
                                df.setTimeZone(obj);
                                String time = df.format(c);
                                chatList.add(new ChatMessageModel(userId,"%%BOOKAPPOINTMENT%%",time,null,"0"));
                                notifyDataSetChanged();
                                onItemAdded.onAdded();
                            }
                        });
                    }else {
                        holder.chatTextLeft.setVisibility(View.VISIBLE);
                        holder.chatTextLeft.setText(chatModel.getMessage());
                    }
                }
                holder.timeLeft.setText(effectiveTime);
            }
        }
    }
    @Override
    public int getItemCount()
    {
        return chatList.size();
    }

}