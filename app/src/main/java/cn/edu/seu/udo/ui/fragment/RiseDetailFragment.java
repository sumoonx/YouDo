package cn.edu.seu.udo.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import cn.edu.seu.udo.R;
import cn.edu.seu.udo.mvp.presenter.RiseDetailPresenter;
import cn.edu.seu.udo.mvp.view.RiseDetailIView;
import cn.edu.seu.udo.ui.model.HourFormatter;
import cn.edu.seu.udo.ui.view.HourMarkerView;
import cn.edu.seu.udo.ui.view.TimeYAxisValueFormatter;
import cn.edu.seu.udo.utils.DateUtil;

/**
 * Author: Jeremy Xu on 2016/7/17 10:57
 * E-mail: jeremy_xm@163.com
 */
public class RiseDetailFragment extends ScreenFragment implements RiseDetailIView {

    public static final String TAG = "RiseDetailFragment";

    public static final String START = ScreenFragment.START + TAG;

    private static final int LINE_CHART_ANITIME = 1500;
    private static final int GRID_COLOR = Color.GRAY;
    private static final float GRID_WIDTH = 1f;
    private static final int LINE_COLOR = Color.DKGRAY;
    private static final float LINE_WIDTH = 2f;

    private RiseDetailPresenter presenter;

    private List<Date> riseDates;

    @BindView(R.id.rise_time_text) TextView riseTime;
    @BindView(R.id.rise_line_chart) LineChart lineChart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        presenter.takeView(this);
        setupLineChart();
        presenter.getDates();
        return view;
    }

    @Override
    public void onDestroyView() {
        presenter.dropView();
        super.onDestroyView();
    }

    @Override
    protected void initInjector() {
        super.initInjector();
        presenter = new RiseDetailPresenter();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_rise_detail;
    }

    @Override
    public String getTitle() {
        return "早起历史";
    }

    @Override
    public String getIntent() {
        return START;
    }

    @Override
    public void renderRiseTime(Date date) {
        riseTime.setText(DateUtil.getTimeStr(date));
    }

    @Override
    public void renderRiseDates(List<Date> dates) {
        riseDates = dates;
        lineChart.setData(getLineData(dates));
        highlightLast();
        //resizeYLineAxis(studyTimeModels);
        lineChart.animateX(LINE_CHART_ANITIME);
    }

    private void setupLineChart() {
        setupLineBorder();
        reduceLineChart();
        setupLineInteraction();
        lineChart.setMarkerView(getLineMakerView());
        setupXLineAxis();
        setupYLineAxises();
    }

    private List<String> lineXString;

    private LineData getLineData(List<Date> dates) {
        lineXString = new ArrayList<>();
        for (Date date : dates) {
            lineXString.add(DateUtil.getDateStr(date));
        }

        ArrayList<ILineDataSet> lineDataSets = getLineDataSets(dates);

        LineData lineData = new LineData(lineXString, lineDataSets);
        lineData.setHighlightEnabled(true);
        lineData.setValueFormatter(new HourFormatter());
        return lineData;
    }

    @NonNull
    private ArrayList<ILineDataSet> getLineDataSets(List<Date> dates) {
        final LineDataSet lineDataSet = new LineDataSet(getYLineData(dates), "this is rise time");

        lineDataSet.setColor(LINE_COLOR);
        lineDataSet.setLineWidth(LINE_WIDTH);
        lineDataSet.setCircleColor(Color.WHITE);
        lineDataSet.setHighLightColor(Color.YELLOW);
        lineDataSet.setHighlightEnabled(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawVerticalHighlightIndicator(false);
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawValues(false);

        return new ArrayList<ILineDataSet>() {{add(lineDataSet);}};
    }

    private void setupLineBorder() {
        lineChart.setDrawBorders(true);
        lineChart.setBorderColor(GRID_COLOR);
        lineChart.setBorderWidth(GRID_WIDTH);
    }

    private void reduceLineChart() {
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription("");       //hide description
        lineChart.getLegend().setEnabled(false);    //hide legend
    }

    private void setupLineInteraction() {
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);
        lineChart.setPinchZoom(false);
        lineChart.setHighlightPerTapEnabled(true);

        lineChart.setSelected(true);
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                Date date = riseDates.get(e.getXIndex());
                highlightAt(e.getXIndex(), dataSetIndex);
                renderRiseTime(date);
            }

            @Override
            public void onNothingSelected() {
                remainHighlight();
            }
        });
    }

    private void setupXLineAxis() {
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setDrawLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawLimitLinesBehindData(true);

        xAxis.setDrawGridLines(true);
        xAxis.setGridColor(GRID_COLOR);
        xAxis.setGridLineWidth(GRID_WIDTH);

        xAxis.setDrawAxisLine(false);
        xAxis.setAxisLineColor(GRID_COLOR);
        xAxis.setAxisLineWidth(GRID_WIDTH);
        xAxis.setAxisMinValue(-0.5f);
        xAxis.setAxisMaxValue(6.5f);
        xAxis.setAvoidFirstLastClipping(false);
    }

    private void setupYLineAxises() {
        setupYAxis(lineChart.getAxisLeft());
        setupYAxis(lineChart.getAxisRight());
    }

    private void setupYAxis(YAxis yAxis) {
        yAxis.setValueFormatter(new TimeYAxisValueFormatter());
        yAxis.setDrawLabels(true);
        yAxis.setInverted(true);
        yAxis.setDrawTopYLabelEntry(false);

        yAxis.setDrawGridLines(true);
        yAxis.setGridColor(GRID_COLOR);
        yAxis.setGridLineWidth(GRID_WIDTH);
    }

    @NonNull
    private ArrayList<Entry> getYLineData(List<Date> dates) {
        ArrayList<Entry> yValues = new ArrayList<>();
        for (int i = 0; i < dates.size(); i++) {
            Date date = dates.get(i);
            yValues.add(new Entry(DateUtil.getTimeFloat(date), i));
        }
        return yValues;
    }

    @NonNull
    private HourMarkerView getLineMakerView() {
        return new HourMarkerView(getActivity(), R.color.rise_detail_markview_color);
    }

    private int xIndex;
    private int dataSetIndex;

    private void highlightAt(int xIndex, int dataSetIndex) {
        this.xIndex = xIndex;
        this.dataSetIndex = dataSetIndex;
        lineChart.highlightValue(xIndex, dataSetIndex);
    }

    private void remainHighlight() {
        lineChart.highlightValue(xIndex, dataSetIndex);
    }

    private void highlightLast() {
        ILineDataSet lineDataSet = lineChart.getLineData().getDataSets().get(0);
        xIndex = lineDataSet.getEntryCount() - 1;
        dataSetIndex = 0;
        remainHighlight();
    }
}
