package cn.edu.seu.udo.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import cn.edu.seu.udo.R;
import cn.edu.seu.udo.model.entities.AppUsage;
import cn.edu.seu.udo.model.entities.StudyTime;
import cn.edu.seu.udo.mvp.presenter.StudyDetailPresenter;
import cn.edu.seu.udo.mvp.view.StudyDetailIView;
import cn.edu.seu.udo.ui.HourFormatter;
import cn.edu.seu.udo.ui.StudyDetailMarkerView;

/**
 * Author: Jeremy Xu on 2016/6/6 13:17
 * E-mail: jeremy_xm@163.com
 */
public class StudyDetailFragment extends BaseFragment implements StudyDetailIView {

    public static final String TAG = "study_detail";

    public static final String START = "start_study_detail";

    private static final int LINE_CHART_ANITIME = 1500;
    private static final int PIE_CHART_ANITIME_X = 1500;
    private static final int PIE_CHART_ANITIME_Y = 1500;

    private static final int GRID_COLOR = Color.WHITE;
    private static final float GRID_WIDTH = 1f;
    private static final int LINE_COLOR = Color.WHITE;
    private static final float LINE_WIDTH = 2f;

    private StudyDetailPresenter presenter;

    @BindView(R.id.tv_study_rank) TextView rankTextView;
    @BindView(R.id.lc_study_rank) LineChart lineChart;
    @BindView(R.id.pc_app_while_study) PieChart pieChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initInjector();
        presenter.takeView(this);
        setupLineChart();
        setupPieChart();
        presenter.getStudyTimes();
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.dropView();
    }

    @Override
    public String getName() {
        return TAG;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_study_detail;
    }

    private void initInjector() {
        presenter =  new StudyDetailPresenter();
    }

    @Override
    public void renderRank(int rank) {
        if (rank != 0) {
            rankTextView.setText(String.valueOf(rank));
        } else {
            rankTextView.setText("尚未产生");
        }
    }

    @Override
    public void renderStudyTimes(List<StudyTime> studyTimeModels) {
        lineChart.setData(getLineData(studyTimeModels));
        highlightLast();
        resizeYLineAxis(studyTimeModels);
        lineChart.animateX(LINE_CHART_ANITIME);
    }


    @Override
    public void renderAppUsages(List<AppUsage> appUsageModels) {
        pieChart.setData(getPieData(appUsageModels));
        pieChart.animateXY(PIE_CHART_ANITIME_X, PIE_CHART_ANITIME_Y);
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

    private LineData getLineData(List<StudyTime> studyTimeModels) {
        lineXString = new ArrayList<>();
        for (StudyTime studyTimeModel : studyTimeModels) {
            lineXString.add(studyTimeModel.getDay());
        }

        ArrayList<ILineDataSet> lineDataSets = getLineDataSets(studyTimeModels);

        LineData lineData = new LineData(lineXString, lineDataSets);
        lineData.setHighlightEnabled(true);
        lineData.setValueFormatter(new HourFormatter());
        return lineData;
    }

    @NonNull
    private ArrayList<ILineDataSet> getLineDataSets(List<StudyTime> studyTimeModels) {
        final LineDataSet lineDataSet = new LineDataSet(getYLineData(studyTimeModels), "this is study time");

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

    private List<Integer> pieColors;

    private void setupPieChart() {
        reducePieChart();
        getPieColors();

        pieChart.setRotationEnabled(false);
        pieChart.setRotationAngle(0);
        pieChart.setUsePercentValues(true);
    }

    private void reducePieChart() {
        pieChart.setDescription("");
        pieChart.setDrawCenterText(false);
        pieChart.setDrawHoleEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.setDrawSliceText(true);
    }

    private void getPieColors() {
        pieColors = new ArrayList<>();
        pieColors.add(ContextCompat.getColor(getActivity(), R.color.pc_study_detail_0));
        pieColors.add(ContextCompat.getColor(getActivity(), R.color.pc_study_detail_1));
        pieColors.add(ContextCompat.getColor(getActivity(), R.color.pc_study_detail_2));
        pieColors.add(ContextCompat.getColor(getActivity(), R.color.pc_study_detail_3));
        pieColors.add(ContextCompat.getColor(getActivity(), R.color.pc_study_detail_4));
        pieColors.add(ContextCompat.getColor(getActivity(), R.color.pc_study_detail_5));
    }

    private PieData getPieData(List<AppUsage> appUsageModels) {
        return new PieData(getXPieValues(appUsageModels), getPieDataSet(appUsageModels));
    }

    @NonNull
    private PieDataSet getPieDataSet(List<AppUsage> appUsageModels) {
        PieDataSet result = new PieDataSet(getYPieValues(appUsageModels), "App time consume");
        result.setSliceSpace(0f);
        if (pieColors == null) {
            getPieColors();
        }
        result.setColors(pieColors.subList(0, StudyTime.BRIEF_SIZE));

        result.setSelectionShift(0);
        result.setDrawValues(false);        //hide percent values
        result.setValueTextSize(12f);
        return result;
    }

    @NonNull
    private List<Entry> getYPieValues(List<AppUsage> appUsageModels) {
        List<Entry> result = new ArrayList<>();
        for (int i = 0; i < appUsageModels.size(); i++) {
            result.add(new Entry(appUsageModels.get(i).getHour(), i));
        }
        return result;
    }

    @NonNull
    private List<String> getXPieValues(List<AppUsage> appUsageModels) {
        List<String> result = new ArrayList<>();
        Collections.sort(appUsageModels);
        Collections.reverse(appUsageModels);
        for (AppUsage appUsageModel : appUsageModels) {
            result.add(appUsageModel.getAppName());
        }
        return result;
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

    private void resizeYLineAxis(@NonNull List<StudyTime> studyTimeModels) {
        float min = Collections.min(studyTimeModels).getTotalHour();
        float max = Collections.max(studyTimeModels).getTotalHour();
        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.calcMinMax(min, max);
    }

    @NonNull
    private ArrayList<Entry> getYLineData(List<StudyTime> studyTimeModels) {
        ArrayList<Entry> yValues = new ArrayList<>();
        for (int i = 0; i < studyTimeModels.size(); i++) {
            yValues.add(new Entry(studyTimeModels.get(i).getTotalHour(), i));
        }
        return yValues;
    }

    private void setupYLineAxises() {
        setupYAxis(lineChart.getAxisLeft());
        setupYAxis(lineChart.getAxisRight());
    }

    private void reduceLineChart() {
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription("");       //hide description
        lineChart.getLegend().setEnabled(false);    //hide legend
    }

    private void setupLineBorder() {
        lineChart.setDrawBorders(true);
        lineChart.setBorderColor(GRID_COLOR);
        lineChart.setBorderWidth(GRID_WIDTH);
    }

    @NonNull
    private StudyDetailMarkerView getLineMakerView() {
        return new StudyDetailMarkerView(getActivity(), R.layout.study_detail_marker_view);
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
                String daySelected = lineXString.get(e.getXIndex());
                highlightAt(e.getXIndex(), dataSetIndex);
                presenter.getRank(daySelected);
                presenter.getAppUsage(daySelected);
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

    private void setupYAxis(YAxis yAxis) {
        yAxis.setDrawLabels(false);
        yAxis.setDrawTopYLabelEntry(false);

        yAxis.setDrawGridLines(true);
        yAxis.setGridColor(GRID_COLOR);
        yAxis.setGridLineWidth(GRID_WIDTH);
    }
}
