package com.example.bisma.calendar_analyzer.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import com.example.bisma.calendar_analyzer.adapters.ReportAdapter;
import com.example.bisma.calendar_analyzer.helpers.Constants;
import com.example.bisma.calendar_analyzer.models.EventModelDep;
import com.example.bisma.calendar_analyzer.R;

public class ViewReportTableFragment extends Fragment {

    protected ListView reportLv;
    private ReportAdapter adapter;
    private List<EventModelDep> dataList = new ArrayList<>();

    public static ViewReportTableFragment newInstance(ArrayList<EventModelDep> dataList) {

        Bundle args = new Bundle();
        args.putParcelableArrayList(Constants.REPORT_DATA_KEY, dataList);
        ViewReportTableFragment fragment = new ViewReportTableFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_view_report, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        reportLv = (ListView) rootView.findViewById(R.id.report_lv);
        View view = getActivity().getLayoutInflater().inflate(R.layout.report_list_header, null);
        reportLv.addHeaderView(view);
        receiveIntent();
    }

    private void receiveIntent() {
        dataList = getArguments().getParcelableArrayList(Constants.REPORT_DATA_KEY);
        adapter = new ReportAdapter(getActivity(), dataList);
        reportLv.setAdapter(adapter);
    }
}