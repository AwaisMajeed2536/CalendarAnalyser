package com.example.bisma.calendar_analyzer.ui;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.bisma.calendar_analyzer.R;
import com.example.bisma.calendar_analyzer.db.source.TasksSource;
import com.example.bisma.calendar_analyzer.helpers.Constants;
import com.example.bisma.calendar_analyzer.models.EventModelDep;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class ScheduleBarChartActivity extends Fragment {

    public static ScheduleBarChartActivity newInstance(String startDate, String endDate) {

        Bundle args = new Bundle();
        args.putString(Constants.START_DATE_PASS_KEY, startDate);
        args.putString(Constants.END_DATE_PASS_KEY, endDate);
        ScheduleBarChartActivity fragment = new ScheduleBarChartActivity();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_schedule_bar_chart, container, false);
        createGraph(rootView);
        return rootView;
    }

    void createGraph(View view) {
        List<EventModelDep> data = new ArrayList<>();
        if (getActivity().getIntent().getExtras() == null) {
            data = TasksSource.newInstance().getAll();
        } else {
            Intent intent = getActivity().getIntent();
            data = TasksSource.newInstance().getInRange(intent.getStringExtra(Constants.START_DATE_PASS_KEY),
                    intent.getStringExtra(Constants.END_DATE_PASS_KEY));
        }
        BarChart barChart = view.findViewById(R.id.bar_chart);
        ListView titlesLv = view.findViewById(R.id.titles_lv);
        List<BarEntry> entries = new ArrayList<>();
        long scheduledValue = 0;
        long unScheduledValue = 0;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isScheduled() == 0) {
                scheduledValue++;
            } else {
                unScheduledValue++;
            }
        }
        entries.add(new BarEntry(1f, unScheduledValue));
        entries.add(new BarEntry(2f, scheduledValue));
        BarDataSet dataSet = new BarDataSet(entries, "Tasks");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.9f); // set custom bar width
        barChart.setData(barData);
        barChart.setFitBars(true); // make the x-axis fit exactly all bars
        barChart.invalidate(); // refresh
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_list_item,
                getTaskTitles());
        titlesLv.setAdapter(adapter);
    }

    private ArrayList<String> getTaskTitles() {
        ArrayList<String> returner = new ArrayList<>();
        returner.add("Scheduled");
        returner.add("UnScheduled");
        return returner;
    }
}
