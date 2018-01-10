package com.example.bisma.calendar_analyzer.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.bisma.calendar_analyzer.R;
import com.example.bisma.calendar_analyzer.helpers.Constants;
import com.example.bisma.calendar_analyzer.helpers.UtilHelpers;
import com.example.bisma.calendar_analyzer.ui.ReportsHistoryActivity;

public class HistoryFragment extends Fragment implements View.OnClickListener {

    protected Button analysisReport;
    protected Button employeeReport;

    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflator.inflate(R.layout.fragment_history, container, false);
        initView(rootView);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), ReportsHistoryActivity.class);
        if (view.getId() == R.id.analysisReport) {
            intent.putExtra(Constants.REPORT_TYPE_PASS_KEY, 0);
        } else if (view.getId() == R.id.employeeReport) {
            intent.putExtra(Constants.REPORT_TYPE_PASS_KEY, 1);
        }
        startActivity(intent);
    }

    private void initView(View rootView) {
        analysisReport = rootView.findViewById(R.id.analysisReport);
        analysisReport.setOnClickListener(HistoryFragment.this);
        employeeReport = rootView.findViewById(R.id.employeeReport);
        employeeReport.setOnClickListener(HistoryFragment.this);
        if (UtilHelpers.getUserType(getActivity()) == Constants.USER_TYPE_FACULTY) {
            employeeReport.setVisibility(View.GONE);
        }
    }
}
