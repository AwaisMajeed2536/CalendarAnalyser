package com.example.bisma.calendar_analyzer.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.bisma.calendar_analyzer.R;
import com.example.bisma.calendar_analyzer.helpers.Constants;
import com.example.bisma.calendar_analyzer.models.EventModelDep;
import com.example.bisma.calendar_analyzer.models.PieDataModel;
import com.example.bisma.calendar_analyzer.ui.GoogleApi;
import com.example.bisma.calendar_analyzer.ui.ViewGraphActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class AnalyzerFragment extends Fragment implements View.OnClickListener {

    private Button generateReportButton;
    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main);
        View rootView = inflator.inflate(R.layout.fragment_analyzer, container, false);

        generateReportButton = rootView.findViewById(R.id.generate_report_button);
        generateReportButton.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.generate_report_button){
            DatePicker fromDatePicker = (DatePicker) getView().findViewById(R.id.from_date_picker);
            int startDay = fromDatePicker.getDayOfMonth();
            int startMonth = fromDatePicker.getMonth() + 1;
            int startYear = fromDatePicker.getYear();
            String fromDateString = startYear + "-" + String.format("%02d",startMonth) +
                    "-" + String.format("%02d",startDay) + " 00:01:00";
            DatePicker toDatePicker = (DatePicker) getView().findViewById(R.id.to_date_picker);
            int endDay = toDatePicker.getDayOfMonth();
            int endMonth = toDatePicker.getMonth() + 1;
            int endYear = toDatePicker.getYear();
            String toDateString = endYear + "-" + String.format("%02d",endMonth) +
                    "-" + String.format("%02d",endDay) + " 23:59:00";
            Intent intent = new Intent(getActivity(), GoogleApi.class);
            intent.putExtra(Constants.GOOGLE_API_CALL_TYPE_KEY, 0);
            intent.putExtra(Constants.GOOGLE_API_STARTDATE_PASS_KEY,fromDateString);
            intent.putExtra(Constants.GOOGLE_API_ENDDATE_PASS_KEY,toDateString);
            startActivityForResult(intent, Constants.GOOGLE_API_GET_CALL_KEY);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.GOOGLE_API_GET_CALL_KEY){
            if(resultCode == Activity.RESULT_OK){
                ArrayList<EventModelDep> taskList = data.getParcelableArrayListExtra(Constants.GOOGLE_API_RESULT_KEY);
                if (taskList == null || taskList.size() == 0){
                    Toast.makeText(getActivity(), "No Events Found!", Toast.LENGTH_SHORT).show();
                    return;
                }
                ArrayList<PieDataModel> analyzedData = analyzeData(taskList);
                Intent intent = new Intent(getActivity(), ViewGraphActivity.class);
                intent.putParcelableArrayListExtra(Constants.PIE_DATA_KEY, analyzedData);
                intent.putParcelableArrayListExtra(Constants.REPORT_DATA_KEY, taskList);
                startActivity(intent);
            } else if (resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(getActivity(), "Failed to get tasks!", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private ArrayList<PieDataModel> analyzeData(List<EventModelDep> dataList) {
        ArrayList<PieDataModel> analyzedData = new ArrayList<>();
        HashMap<String, Float> storeResult = new HashMap<>();
        for (EventModelDep obj : dataList) {
            if (storeResult.containsKey(obj.getEventTitle())) {
                float count = storeResult.get(obj.getEventTitle());
                storeResult.put(obj.getEventTitle(), ++count);
            } else {
                storeResult.put(obj.getEventTitle(), 1.0f);
            }
        }
        String[] tasks = new String[storeResult.size()];
        tasks = ((Set<String>) storeResult.keySet()).toArray(tasks);
        storeResult = convertCountToPercentage(tasks, storeResult);
        for (int i = 0; i < storeResult.size(); i++) {
            analyzedData.add(new PieDataModel(tasks[i], storeResult.get(tasks[i])));
        }
        return analyzedData;
    }



    private HashMap<String, Float> convertCountToPercentage(String[] tasks, HashMap<String, Float> storeResult) {
        HashMap<String, Float> convertedResult = new HashMap<>();
        Float[] values = new Float[storeResult.size()];
        values = storeResult.values().toArray(values);
        int total = 0;
        for (float val : values) {
            total += val;
        }

        for (int i = 0; i < storeResult.size(); i++) {
            float percentage = storeResult.get(tasks[i]) / total * 100;
            convertedResult.put(tasks[i], percentage);
        }

        return convertedResult;
    }
}