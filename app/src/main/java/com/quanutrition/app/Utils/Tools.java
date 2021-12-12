package com.quanutrition.app.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.PagerAdapter;

import com.quanutrition.app.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.quanutrition.app.profile.TimeInputChildModel;
import com.quanutrition.app.profile.TimeInputModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {


    static AlertDialog ad;
    public static boolean validateNormalText(EditText editText){
        if(TextUtils.isEmpty(editText.getText().toString().trim())){
            return false;
        }else{
            return true;
        }
    }
    public static String getText(EditText editText){
        return editText.getText().toString().trim();
    }
    public static boolean validateEmail(EditText ed){
        if(TextUtils.isEmpty(ed.getText().toString().trim())){
            return false;
        }
        else{
            Pattern p = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(ed.getText().toString().trim());
            boolean b = m.matches();
            return b;
        }
    }

    public static boolean isValidEmail(EditText editText) {
        String target = editText.getText().toString().trim();
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static boolean validatePassword(EditText ed){
        if(TextUtils.isEmpty(ed.getText().toString().trim())){
            return false;
        }
        else{
            String m = ed.getText().toString().trim();
            if(m.length()<6){
                return false;
            }else{
                return true;
            }

        }
    }

    public static void loadImageIntoImageView(String url, ImageView img){
        Picasso.get().load(url).placeholder(R.drawable.ic_no_image).error(R.drawable.ic_no_image).into(img);
    }

    public static void loadProfileImage(String url, ImageView img){
        Picasso.get().load(url).resize(250,250).onlyScaleDown().centerCrop().placeholder(R.drawable.ic_no_image).error(R.drawable.ic_no_image).into(img);
    }

    public static void loadProgram(String url, ImageView img){
        Picasso.get().load(url).resize(250,150).onlyScaleDown().placeholder(R.drawable.ic_no_image).error(R.drawable.program_test).into(img);
    }

    public static void loadMythFact(String url, ImageView img){
        Picasso.get().load(url).resize(1024,1024).onlyScaleDown().placeholder(R.drawable.ic_no_image).error(R.drawable.ic_no_image).into(img);
    }

    public static void loadBlogImage(String url, ImageView img){
        Picasso.get().load(url).resizeDimen(R.dimen.blogWidth,R.dimen.blogHeight).placeholder(R.drawable.ic_no_image).error(R.drawable.ic_no_image).into(img);
    }

    public static void initCustomToast(Context c, String message){
        Toast.makeText(c,message, Toast.LENGTH_SHORT).show();
    }


    public static void initNetworkErrorToast(Context c){
        Toast.makeText(c,"Something went wrong! Check your internet connection and try again.", Toast.LENGTH_SHORT).show();
    }


    public static AlertDialog getDialog(String msg, Context mCtx){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mCtx);
            LayoutInflater linf = LayoutInflater.from(mCtx);
            final View inflator = linf.inflate(R.layout.loading_layout, null);
            inflator.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            TextView tv = (TextView) inflator.findViewById(R.id.msg);
            tv.setText(msg);
            alertDialog.setView(inflator);
            alertDialog.setCancelable(false);
            return alertDialog.create();
    }
    public static String longText(String msg, int length){
        if(msg.length()<(length-3)){
            return msg;
        }else{
            String trim = msg.substring(0,length-4);
            trim+="...";
            return trim;
        }
    }
    public static String longBlogDesc(String msg, int length){
        if(msg.length()<(length-3)){
            return msg;
        }else{
            String trim = msg.substring(0,length-12);
            trim+="..Read More";
            return trim;
        }
    }

    public static void setSystemBarColor(Activity act, @ColorRes int color) {

            Window window = act.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.parseColor("#bE66B9A4"));

//        Window window = act.getWindow();
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.setStatusBarColor(Color.TRANSPARENT);

    }

    public static void setSystemBarColorCustom(Activity act, @ColorRes int color) {

        Window window = act.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(act.getResources().getColor(color));

//        Window window = act.getWindow();
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.setStatusBarColor(Color.TRANSPARENT);

    }

    public static void setSystemBarColorDark(Activity act, @ColorRes int color) {

        Window window = act.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.parseColor("#66B9A4"));

//        Window window = act.getWindow();
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.setStatusBarColor(Color.TRANSPARENT);

    }

    public static long getBeginTimeInMilli(String date, String time) {
        Calendar c = Calendar.getInstance();
        c.set(Integer.parseInt(date.split("-")[2]), Integer.parseInt(date.split("-")[1])-1, Integer.parseInt(date.split("-")[0]));
        c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.split(":")[0]));
        c.set(Calendar.MINUTE, Integer.parseInt(time.split(":")[1]));
        c.set(Calendar.SECOND,0);
        c.set(Calendar.MILLISECOND,0);
        return c.getTimeInMillis();
    }

    public static long getDateTimeInMilli(String date, String time) {
        Calendar c = Calendar.getInstance();
        c.set(Integer.parseInt(date.split("-")[0]), Integer.parseInt(date.split("-")[1])-1, Integer.parseInt(date.split("-")[2]));
        c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.split(":")[0]));
        c.set(Calendar.MINUTE, Integer.parseInt(time.split(":")[1]));
        c.set(Calendar.SECOND, Integer.parseInt(time.split(":")[2]));
        c.set(Calendar.MILLISECOND,0);
        return c.getTimeInMillis();
    }

    public static long getEndTimeInMilli(String date, String time) {
        Calendar c = Calendar.getInstance();
        c.set(Integer.parseInt(date.split("-")[2]), Integer.parseInt(date.split("-")[1])-1, Integer.parseInt(date.split("-")[0]));
        c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.split(":")[0]));
        c.set(Calendar.MINUTE, Integer.parseInt(time.split(":")[1]));
        c.set(Calendar.SECOND,0);
        c.set(Calendar.MILLISECOND,0);
        c.add(Calendar.MINUTE,15);
        return c.getTimeInMillis();
    }

    public static String[] getDateTimeRange(String date){
        Calendar c = Calendar.getInstance();
        c.set(Integer.parseInt(date.split("-")[2]), Integer.parseInt(date.split("-")[1])-1, Integer.parseInt(date.split("-")[0]));
        c.set(Calendar.HOUR_OF_DAY,0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        c.set(Calendar.MILLISECOND,0);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        String[] range = new String[2];

        range[0] = sdf.format(c.getTime());

        c.set(Calendar.HOUR_OF_DAY,23);
        c.set(Calendar.MINUTE,59);
        c.set(Calendar.SECOND,0);
        c.set(Calendar.MILLISECOND,0);

        range[1] = sdf.format(c.getTime());

        return range;
    }

    public static String getLocalSlot(String dtSlot) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String slot = "";
        try {
            Date d = sdf.parse(dtSlot);
            SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
            sdf1.setTimeZone(TimeZone.getDefault());
            slot = sdf1.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return slot;
    }

    public static String getTime(String time) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.split(":")[0]));
        c.set(Calendar.MINUTE, Integer.parseInt(time.split(":")[1]));
        c.set(Calendar.SECOND,0);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String time1 = sdf.format(c.getTime());
        return time1;
    }

    public static String getLocalTime(String timestamp) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TimeZone obj = TimeZone.getTimeZone("UTC");
        df.setTimeZone(obj);
        Date d;
        String Time="";
        try{
            d = df.parse(timestamp);
            TimeZone ob = TimeZone.getDefault();
            df.setTimeZone(ob);
            Time = df.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Time;
    }

    /*public static String getLocalSlot(String time){
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        TimeZone obj = TimeZone.getTimeZone("UTC");
        df.setTimeZone(obj);
        String LocalTime = "";
        try{
            Date d = df.parse(time);
            TimeZone ob = TimeZone.getDefault();
            df.setTimeZone(ob);
            LocalTime = df.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return LocalTime;
    }*/

    public static String getLocalTimeAMPM(String time){
        SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
        TimeZone obj = TimeZone.getTimeZone("UTC");
        df.setTimeZone(obj);
        String LocalTime = "";
        try{
            Date d = df.parse(time);
            TimeZone ob = TimeZone.getDefault();
            df.setTimeZone(ob);
            LocalTime = df.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return LocalTime;
    }

    public static String getLocalDate(String time){
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        TimeZone obj = TimeZone.getTimeZone("UTC");
        df.setTimeZone(obj);
        String LocalTime = "";
        try{
            Date d = df.parse(time);
            TimeZone ob = TimeZone.getDefault();
            df.setTimeZone(ob);
            LocalTime = df.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return LocalTime;
    }

    public static String getUTCSlot(String date, String time){
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        TimeZone obj = TimeZone.getDefault();
        df.setTimeZone(obj);
        String UTC = "";
        try{
            Date d = df.parse(date+" "+time);
            TimeZone ob = TimeZone.getTimeZone("UTC");
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            sdf.setTimeZone(ob);
            UTC = sdf.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return UTC;
    }

    public static String getStringFromArray(JSONArray array) {
        String result = "";
        try {
            for (int i = 0; i < array.length(); i++) {
                if (i == 0)
                    result += array.getString(i);
                else
                    result += ","+array.getString(i);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return result;
    }

    public static boolean validateTime(String from, String to) {
        boolean result = false;
        if(from.equalsIgnoreCase(to)){
            return result;
        }else{
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
            try {
                Date dateFrom = sdf.parse(getFormattedDateToday() + " " + from);
                Date dateTo = sdf.parse(getFormattedDateToday()+" "+to);
                if(dateFrom.getTime()>dateTo.getTime())
                    return false;
                else
                    return true;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }
    }

    public static String getDuration(String from, String to) {
        String duration = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        try {
            Date dateFrom = sdf.parse(getFormattedDateToday() + " " + from);
            Date dateTo = sdf.parse(getFormattedDateToday()+" "+to);

            long milli = dateTo.getTime()-dateFrom.getTime();
            float min = (milli/1000)/60;


            Log.d("Division",min/60 +"");
            if(min/60<1f){
                duration = (int)min+" min.";
            }else{
                if(min/60<2f)
                    duration = (int)(min/60)+" hour "+(int)(min%60)+" min.";
                else
                    duration = (int)(min/60)+" hours "+(int)(min%60)+" min.";
            }

            return duration;

        }catch (Exception e){
            e.printStackTrace();
            return duration;
        }
    }


    public interface onClickListener{
        void onClick(String s);
    }

    public static void getDialog(onClickListener clickListener){
        clickListener.onClick("");
    }

    public static int dpToPx(Context c, int dp) {
        Resources r = c.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public static String getFormattedTimeAMPM(int hour, int min){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,hour);
        c.set(Calendar.MINUTE,min);
        c.set(Calendar.SECOND,0);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String time = sdf.format(c.getTime());
        return time;
    }

    public static String getUnformattedTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        Date d = new Date();
        try {
            d = sdf.parse(time);
        }catch (ParseException e){
            e.printStackTrace();
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
        return sdf1.format(d.getTime());
    }

    public static String getformattedTime(String time) {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        try {

            SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
            d = sdf1.parse(time);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return sdf.format(d.getTime());
    }

    public static String getFormattedDate(long timeinmillis){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String time = sdf.format(timeinmillis);
        return time;
    }

    public static long getTimeInMillis(String date){
        Calendar c = Calendar.getInstance();
        c.set(Integer.parseInt(date.split("-")[2]), Integer.parseInt(date.split("-")[1])-1, Integer.parseInt(date.split("-")[0]));
        c.set(Calendar.HOUR_OF_DAY,0);
        c.set(Calendar.MINUTE,1);
        c.set(Calendar.SECOND,1);
        c.set(Calendar.MILLISECOND,0);
        return c.getTimeInMillis();
    }

    public static String getDayFromDate(String date){
        Calendar c = Calendar.getInstance();
        c.set(Integer.parseInt(date.split("-")[2]), Integer.parseInt(date.split("-")[1])-1, Integer.parseInt(date.split("-")[0]));
        c.set(Calendar.HOUR_OF_DAY,0);
        c.set(Calendar.MINUTE,1);
        c.set(Calendar.SECOND,1);
        c.set(Calendar.MILLISECOND,0);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd-MM-yyyy");
        return sdf.format(c.getTimeInMillis());
    }

    public static String getFormattedDateToday(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String time = sdf.format(c.getTimeInMillis());
        return time;
    }

    public static ArrayList<String> getLastSevenDates(){
        ArrayList<String> dates = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd-MM-yyyy");
        for(int i=0;i<7;i++){
            if(i>0)
            c.add(Calendar.DATE,-1);

            dates.add(sdf.format(c.getTimeInMillis()));
        }
        return dates;
    }

    public static int getDayNumberToday(){
        Calendar c = Calendar.getInstance();
        int position=0;
        String[] days = {"MON","TUE","WED","THU","FRI","SAT","SUN"};
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd-MM-yyyy");
        String today = sdf.format(c.getTimeInMillis()).split(" ")[0];
        for(int i=0;i<days.length;i++){
            if(days[i].equalsIgnoreCase(today))
                position = i;
        }
        return position;
    }

    public static SharedPreferences getGeneralSharedPref(Context context){
        return context.getSharedPreferences(Constants.MyPreferences, Context.MODE_PRIVATE);
    }

    public static SharedPreferences.Editor getGeneralEditor(Context context){
        return context.getSharedPreferences(Constants.MyPreferences, Context.MODE_PRIVATE).edit();
    }




    public  static void getImageFromCamera(final int pos, final Activity activity, final String fileLocation) {
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Log.d("manya","we are in tools now");
        final Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        Dexter.withActivity(activity)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(
                    new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Dexter.withActivity(activity)
                    .withPermission(Manifest.permission.CAMERA)
                    .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                    File photo = null;
                    try {
                    //                            startActivityForResult(intent,pos);

                    photo = createFile(fileLocation);
                    } catch (Exception e) {
                    // some action
                    }
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));

                    activity.startActivityForResult(cameraIntent, pos + 10);

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

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }

                    }
                    )
                    .check();
    }



    private static File createFile(String mImageFileLocation) throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        final String imageFileName = "IMAGE_" + timeStamp + "_";
        final File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        final File[] image = new File[1];

        try {
            image[0] = File.createTempFile(
                    imageFileName,
                    ".jpg",
                    storageDir
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        mImageFileLocation = image[0].getAbsolutePath();
        Log.d("Camera App", mImageFileLocation);
        return image[0];
    }


    public static void openGallery(final int pos, final Activity activity) {
        Log.d("manya", "we are in tools now");

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // 2. pick image only
        intent.setType("image/*");
        // 3. start activity
        activity.startActivityForResult(intent, pos);

    }


    public static void setHTMLData(TextView textView, String text){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT));
        } else {
            textView.setText(Html.fromHtml(text));
        }
    }


    public static ArrayList<TimeInputModel> sortDayWise(ArrayList<TimeInputModel> list){
        ArrayList<TimeInputModel> sortedList = new ArrayList<>();

        final String[] daysArray = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
        for(int i=0;i<7;i++){

            for(int j=0;j<list.size();j++){
                if(list.get(j).getDay().equalsIgnoreCase(daysArray[i])){
                    Log.e("day",daysArray[i]);
                    sortedList.add(list.get(j));
                }
            }

        }
        list = sortedList;
        return sortedList;
    }



}
