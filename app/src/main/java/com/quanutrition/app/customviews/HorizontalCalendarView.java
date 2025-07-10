package com.quanutrition.app.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.quanutrition.app.R;
import com.quanutrition.app.Utils.Tools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class HorizontalCalendarView extends LinearLayout {

    Context context;
    AttributeSet attributeSet;
    RecyclerView recyclerView;
    HorizontalCalendarAdapter adapter;

    public interface OnCalendarListener{
        void onDateSelected(String date);
    }

    public HorizontalCalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attributeSet = attrs;
        init();
    }

    public HorizontalCalendarView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public HorizontalCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.attributeSet = attrs;
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.horizontal_calander_view, null);

        TextView textView = view.findViewById(in.akshit.horizontalcalendar.R.id.text);
        recyclerView = view.findViewById(in.akshit.horizontalcalendar.R.id.re);

        view.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        if(attributeSet!=null) {
            TypedArray attrs = context.obtainStyledAttributes(attributeSet, in.akshit.horizontalcalendar.R.styleable.HorizontalCalendarView);
            textView.setText(attrs.getString(in.akshit.horizontalcalendar.R.styleable.HorizontalCalendarView_text));
            attrs.recycle();
        }else{
            textView.setText("No Text Provided");
        }
        textView.setVisibility(GONE);

        addView(view);
    }

    public void setUpCalendar(long start, long end, ArrayList<String> dates, OnCalendarListener onCalendarListener){

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(start);

        ArrayList<HorizontalCalendarModel> list = new ArrayList<>();

        long today = Tools.getTimeInMillis(Tools.getFormattedDateToday());

        long current = start;
        int i=0;
        int pos = 0;
        while(current<end){

            Calendar c1 = Calendar.getInstance();
            c1.setTimeInMillis(start);
            c1.add(Calendar.DATE,i);

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

            if(sdf.format(c1.getTimeInMillis()).equalsIgnoreCase(sdf.format(today))){
                pos =i;
                Log.d("Postion",pos+"");
            }

            HorizontalCalendarModel model = new HorizontalCalendarModel(c1.getTimeInMillis());
            if(dates.contains(sdf.format(c1.getTimeInMillis()))){
                model.setStatus(1);
            }

            list.add(model);

            current = c1.getTimeInMillis();
            i++;
            Log.d("Setting data",sdf.format(c1.getTimeInMillis()));
        }

        adapter = new HorizontalCalendarAdapter(list,context);
        adapter.setOnCalendarListener(onCalendarListener);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL,false);
//        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context,7,RecyclerView.VERTICAL,false);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setPos(pos);
        adapter.notifyDataSetChanged();

        recyclerView.scrollToPosition(pos);
    }

    public void updateCalendar(long start, long end, String selectedDate, OnCalendarListener onCalendarListener) {

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(start);

        ArrayList<HorizontalCalendarModel> list = new ArrayList<>();

        long today = Tools.getTimeInMillis(Tools.getFormattedDateToday());

        long current = start;
        int i = 0;
        int pos = 0;
        while (current < end) {

            Calendar c1 = Calendar.getInstance();
            c1.setTimeInMillis(start);
            c1.add(Calendar.DATE, i);

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

            /*if (sdf.format(c1.getTimeInMillis()).equalsIgnoreCase(sdf.format(today))) {
                pos = i;
                Log.d("Postion", pos + "");
            }*/

            HorizontalCalendarModel model = new HorizontalCalendarModel(c1.getTimeInMillis());
            if (selectedDate.equalsIgnoreCase(sdf.format(c1.getTimeInMillis()))) {
                pos = i;
                Log.d("Postion", pos + "");
            }

            list.add(model);

            current = c1.getTimeInMillis();
            i++;
            Log.d("Setting data", sdf.format(c1.getTimeInMillis()));
        }
        adapter.setPos(pos);
        adapter.setData(list);
        adapter.setOnCalendarListener(onCalendarListener);
    }
}
