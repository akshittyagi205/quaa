package com.quanutrition.app.general;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.quanutrition.app.R;
import com.quanutrition.app.Utils.Tools;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;

public class FeedbackUtils {

    private OnFeedbackRecievedListener onFeedbackRecievedListener;
    private Context context;
    static AlertDialog alertDialog1;

    public FeedbackUtils(Context context){
        this.context = context;
    }


    public interface OnFeedbackRecievedListener{
        void onFeedbackRecieved(int rating, String text);
    }

    public void setOnFeedbackListener(OnFeedbackRecievedListener onFeedbackRecievedListener){
        this.onFeedbackRecievedListener = onFeedbackRecievedListener;
    }

    int rating=0;
    public void getFeedBackDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        LayoutInflater linf = LayoutInflater.from(context);
        final View inflator = linf.inflate(R.layout.feedback_dialog, null);
        alertDialog.setView(inflator);
        alertDialog.setCancelable(true);
        final SmileRating ratingBar = inflator.findViewById(R.id.ratingBar);
        final EditText editText = inflator.findViewById(R.id.editText);
        Button button = inflator.findViewById(R.id.button);
        ratingBar.setSelectedSmile(BaseRating.GOOD);
        rating=3;
        ratingBar.setOnRatingSelectedListener(new SmileRating.OnRatingSelectedListener() {
            @Override
            public void onRatingSelected(int level, boolean reselected) {
                rating = level;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rating==0){
                    Tools.initCustomToast(context,"Please give rating before continuing");
                }else{
                    if(editText.getText().toString().trim().isEmpty()){
                        Tools.initCustomToast(context,"Please give comment before continuing");
                    }else{
                        onFeedbackRecievedListener.onFeedbackRecieved(rating,editText.getText().toString().trim());
                        alertDialog1.dismiss();
                    }
                }
            }
        });

        alertDialog1 = null;
        alertDialog1 = alertDialog.show();
        alertDialog1.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.dialog_background_drawable));
    }


}
