package com.example.bisma.calendar_analyzer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.bisma.calendar_analyzer.R;
import com.example.bisma.calendar_analyzer.helpers.Constants;
import com.example.bisma.calendar_analyzer.models.EventModelDep;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Devprovider on 29/08/2017.
 */

public class BarChartFragment extends Fragment {
    protected BarChart barChart;
    protected ListView titlesLv;
    private List<EventModelDep> dataList = new ArrayList<>();

    public static BarChartFragment newInstance(ArrayList<EventModelDep> dataList) {

        Bundle args = new Bundle();
        args.putParcelableArrayList(Constants.BAR_CHART_PASS_KEY, dataList);
        BarChartFragment fragment = new BarChartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bar_chart, container, false);
        initView(view);
        return view;
    }

    private void initView(View rootView) {
        barChart = (BarChart) rootView.findViewById(R.id.bar_chart);
        titlesLv = (ListView) rootView.findViewById(R.id.titles_lv);
        dataList = getArguments().getParcelableArrayList(Constants.BAR_CHART_PASS_KEY);
        BarData barData = new BarData(analyzeData());
        barData.setBarWidth(0.9f); // set custom bar width
        barChart.setData(barData);
        barChart.setFitBars(true); // make the x-axis fit exactly all bars
        barChart.invalidate(); // refresh
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_list_item,
                getTaskTitles());
        titlesLv.setAdapter(adapter);
    }

    private BarDataSet analyzeData() {
        List<BarEntry> entries = new ArrayList<>();
        float i = 1f;
        for (EventModelDep obj : dataList) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
                Date sDate = sdf.parse(obj.getStartDate());
                Date eDate = sdf.parse(obj.getEndDate());
                long difference = eDate.getTime() - sDate.getTime();
                long result = TimeUnit.MILLISECONDS.toMinutes(difference);
                entries.add(new BarEntry(i++, result));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        BarDataSet dataSet = new BarDataSet(entries, "Tasks");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        return dataSet;
    }

    private ArrayList<String> getTaskTitles() {
        ArrayList<String> returner = new ArrayList<>();
        int i = 1;
        for (EventModelDep obj : dataList) {
            returner.add(i++ + ". " +obj.getEventTitle());
        }
        return returner;
    }
}
