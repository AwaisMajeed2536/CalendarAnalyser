package com.example.bisma.calendar_analyzer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.DragAndDropPermissions;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bisma.calendar_analyzer.R;
import com.example.bisma.calendar_analyzer.helpers.Constants;
import com.example.bisma.calendar_analyzer.models.EventModel;
import com.example.bisma.calendar_analyzer.models.EventModelDep;
import com.github.mikephil.charting.data.BarEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Devprovider on 29/08/2017.
 */

public class TextualReportFragment extends Fragment {
    protected TextView reportTv;
    protected TextView resultTv;
    private List<EventModelDep> dataList;
    boolean resultOk;

    public static TextualReportFragment newInstance(ArrayList<EventModelDep> dataList) {

        Bundle args = new Bundle();
        args.putParcelableArrayList(Constants.TEXTUAL_REPORT_PASS_KEY, dataList);
        TextualReportFragment fragment = new TextualReportFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_textual_report, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        reportTv = (TextView) rootView.findViewById(R.id.report_tv);
        resultTv = (TextView) rootView.findViewById(R.id.result_tv);
        dataList = getArguments().getParcelableArrayList(Constants.TEXTUAL_REPORT_PASS_KEY);
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
