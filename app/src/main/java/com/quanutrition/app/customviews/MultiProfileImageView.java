package com.quanutrition.app.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.quanutrition.app.R;
import com.quanutrition.app.Utils.Tools;

import de.hdodenhof.circleimageview.CircleImageView;

public class MultiProfileImageView extends LinearLayout {
    Context context;
    AttributeSet attributeSet;
    CircleImageView image1,image2;
    public MultiProfileImageView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public MultiProfileImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attributeSet = attrs;
        init();
    }

    public MultiProfileImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.attributeSet = attrs;
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.multi_profile_imageview, null);
        view.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        image1 = view.findViewById(R.id.image1);
        image2 = view.findViewById(R.id.image2);
        addView(view);
    }

    public void setImages(String url1,String url2){

        if(image1!=null)
            Tools.loadSquareImage(url1,image1);

        if(image2!=null)
            Tools.loadSquareImage(url2,image2);
    }
}
