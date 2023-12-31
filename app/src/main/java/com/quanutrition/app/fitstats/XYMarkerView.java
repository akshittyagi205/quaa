package com.quanutrition.app.fitstats;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.quanutrition.app.R;

import java.text.DecimalFormat;

/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 */
@SuppressLint("ViewConstructor")
public class XYMarkerView extends MarkerView {

    private final TextView tvContent;
//    private final ValueFormatter xAxisValueFormatter;
    private String unit;
    private final DecimalFormat format;

    public XYMarkerView(Context context,String unit) {
        super(context, R.layout.custom_marker_view);

//        this.xAxisValueFormatter = xAxisValueFormatter;
        tvContent = findViewById(R.id.tvContent);
        this.unit = unit;
        format = new DecimalFormat("######");
    }

    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        /*if(unit.equalsIgnoreCase("sleep")){
            String sleepTime = new SleepTrackerUtils().calculateDurationFromMinutes(format.format(e.getY()));
            tvContent.setText(String.format("%s",sleepTime));
        }else*/
        tvContent.setText(String.format("%s %s",format.format(e.getY()),unit));

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
