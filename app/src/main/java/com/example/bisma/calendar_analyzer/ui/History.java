package com.example.bisma.calendar_analyzer.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.bisma.calendar_analyzer.R;
import com.example.bisma.calendar_analyzer.helpers.Constants;
import com.example.bisma.calendar_analyzer.helpers.UtilHelpers;

public class History extends Fragment implements View.OnClickListener {

    protected Button analysisReport;
    protected Button employeeReport;

    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflator.inflate(R.layout.tab3_history, container, false);
        initView(rootView);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.analysisReport) {

        } else if (view.getId() == R.id.employeeReport) {

        }
    }

    private void initView(View rootView) {
        analysisReport = (Button) rootView.findViewById(R.id.analysisReport);
        analysisReport.setOnClickListener(History.this);
        employeeReport = (Button) rootView.findViewById(R.id.employeeReport);
        employeeReport.setOnClickListener(History.this);
        if (UtilHelpers.getUserType(getActivity()) != Constants.USER_TYPE_FACULTY ||
                UtilHelpers.getUserType(getActivity()) != Constants.USER_TYPE_STUDENT) {
            employeeReport.setVisibility(View.GONE);
        }
    }
}
