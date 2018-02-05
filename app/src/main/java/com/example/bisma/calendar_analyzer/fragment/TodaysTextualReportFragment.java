package com.example.bisma.calendar_analyzer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bisma.calendar_analyzer.R;
import com.example.bisma.calendar_analyzer.db.source.TasksSource;
import com.example.bisma.calendar_analyzer.helpers.Constants;
import com.example.bisma.calendar_analyzer.models.EventModelDep;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Awais Majeed on 08-Jan-18.
 */

public class TodaysTextualReportFragment extends Fragment {

    protected TextView scheduledTasksTv;
    protected TextView scheduledHoursTv;
    protected TextView unscheduledTasksTv;
    protected TextView unscheduledHoursTv;
    private List<EventModelDep> dataList;
    private int scheduledTasksCount, unScheduledTasksCount;
    private double scheduledHoursCount, unScheduledHoursCount;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_todays_textual_report, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        dataList = TasksSource.newInstance().getTodayEvents();
        scheduledTasksTv = rootView.findViewById(R.id.scheduled_tasks_tv);
        scheduledHoursTv = rootView.findViewById(R.id.scheduled_hours_tv);
        unscheduledTasksTv = rootView.findViewById(R.id.unscheduled_tasks_tv);
        unscheduledHoursTv = rootView.findViewById(R.id.unscheduled_hours_tv);
        setResultText();
    }

    private void setResultText() {
        DecimalFormat df2 = new DecimalFormat(".##");
        analyzeDate();
        scheduledTasksTv.setText(String.valueOf(scheduledTasksCount));
        scheduledHoursTv.setText(df2.format(scheduledHoursCount));
        unscheduledTasksTv.setText(String.valueOf(unScheduledTasksCount));
        unscheduledHoursTv.setText(df2.format(unScheduledHoursCount));
    }

    private void analyzeDate() {
        for (EventModelDep obj : dataList) {
            if (obj.isScheduled() == 1)
                scheduledTasksCount++;
            else
                unScheduledTasksCount++;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
                Date sDate = sdf.parse(obj.getStartDate());
                Date eDate = sdf.parse(obj.getEndDate());
                long difference = eDate.getTime() - sDate.getTime();
                scheduledHoursCount += (difference / (1000.0d * 60.0d * 60.0d)) % 24.0d;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        scheduledHoursCount = 8.0d - scheduledHoursCount;
    }
}
