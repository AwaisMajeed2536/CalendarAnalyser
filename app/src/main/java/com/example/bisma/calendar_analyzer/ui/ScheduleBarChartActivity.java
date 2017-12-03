package com.example.bisma.calendar_analyzer.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.bisma.calendar_analyzer.R;
import com.example.bisma.calendar_analyzer.db.source.TasksSource;
import com.example.bisma.calendar_analyzer.models.EventModelDep;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class ScheduleBarChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_bar_chart);
        createGraph();
    }

    void createGraph() {
        List<EventModelDep> data = TasksSource.newInstance().getAll();
        BarChart barChart = findViewById(R.id.bar_chart);
        ListView titlesLv = findViewById(R.id.titles_lv);
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.simple_list_item,
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
