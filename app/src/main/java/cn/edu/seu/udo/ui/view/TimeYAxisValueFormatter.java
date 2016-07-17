package cn.edu.seu.udo.ui.view;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;

import cn.edu.seu.udo.utils.DateUtil;

/**
 * Author: Jeremy Xu on 2016/7/17 16:03
 * E-mail: jeremy_xm@163.com
 */
public class TimeYAxisValueFormatter implements YAxisValueFormatter {

    @Override
    public String getFormattedValue(float value, YAxis yAxis) {
        return DateUtil.timeStrFrom(value);
    }
}
