package cn.edu.seu.udo.ui.view;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

import java.text.DecimalFormat;

import cn.edu.seu.udo.R;
import cn.edu.seu.udo.utils.DateUtil;

/**
 * Author: Jeremy Xu on 2016/4/12 20:10
 * E-mail: jeremy_xm@163.com
 */
public class HourMarkerView extends MarkerView {

    private TextView tvContent;

    private DecimalFormat decimalFormat;

    public HourMarkerView(Context context, int colorResource) {
        super(context, R.layout.study_detail_marker_view);
        // this markerview only displays a textview
        tvContent = (TextView) findViewById(R.id.tvContent);
        tvContent.setTextColor(colorResource);

        decimalFormat = new DecimalFormat("##.#");
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tvContent.setText(DateUtil.timeStrFrom(e.getVal()));
    }

    @Override
    public int getXOffset(float xpos) {
        // this will center the marker-view horizontally
        return -(getWidth() / 2);
    }

    @Override
    public int getYOffset(float ypos) {
        // this will cause the marker-view to be above the selected value
        return -getHeight();
    }
}