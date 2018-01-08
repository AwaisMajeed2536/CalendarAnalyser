package com.example.bisma.calendar_analyzer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bisma.calendar_analyzer.R;
import com.example.bisma.calendar_analyzer.db.source.TasksSource;
import com.example.bisma.calendar_analyzer.helpers.Constants;
import com.example.bisma.calendar_analyzer.models.EventModelDep;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Awais Majeed on 08-Jan-18.
 */

public class TodaysTextualReportFragment extends Fragment {

    protected TextView reportTv;
    protected TextView resultTv;
    private List<EventModelDep> dataList;
    boolean resultOk;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_textual_report, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        reportTv = rootView.findViewById(R.id.report_tv);
        resultTv = rootView.findViewById(R.id.result_tv);
        dataList = TasksSource.newInstance().getTodayEvents();
        reportTv.setText(analyzeDate());
        setResultText();
    }

    private void setResultText() {
        if (resultOk) {
            resultTv.setText(Constants.RESULT_OK);
            resultTv.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.holo_green_light));
        } else {
            resultTv.setText(Constants.RESULT_FAIL);
            resultTv.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.holo_red_light));
        }
    }

    private String analyzeDate() {
        String returner = "Number of tasks scheduled is " + dataList.size() + "\n";
        long hours = 0;
        for (EventModelDep obj : dataList) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
                Date sDate = sdf.parse(obj.getStartDate());
                Date eDate = sdf.parse(obj.getEndDate());
                long difference = eDate.getTime() - sDate.getTime();
                hours += TimeUnit.MILLISECONDS.toHours(difference);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        returner += "Numbers of hours scheduled is " + hours + "\n";
        long rep = (dataList.size() * 8) - hours;
        returner += "Number of hours unscheduled is " + rep + "\n";
        resultOk = hours -rep > ((dataList.size() * 8) / 2);
        return returner;
    }
}
