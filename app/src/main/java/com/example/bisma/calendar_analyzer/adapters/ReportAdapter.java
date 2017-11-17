package com.example.bisma.calendar_analyzer.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import com.example.bisma.calendar_analyzer.R;
import com.example.bisma.calendar_analyzer.models.EventModelDep;


public class ReportAdapter extends ArrayAdapter<EventModelDep> {
    private Context context;
    private List<EventModelDep> dataList;

    public ReportAdapter(@NonNull Context context, List<EventModelDep> dataList) {
        super(context, -1, dataList);
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @NonNull
    @Override

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.report_list_item, parent, false);
        TextView title = (TextView) convertView.findViewById(R.id.event_title_tv);
        TextView sDate = (TextView) convertView.findViewById(R.id.event_sdate_tv);
        TextView eDate = (TextView) convertView.findViewById(R.id.event_edate_tv);
        TextView sTime = (TextView) convertView.findViewById(R.id.event_stime_tv);
        TextView eTime = (TextView) convertView.findViewById(R.id.event_etime_tv);

        EventModelDep modelDep = dataList.get(position);

        title.setText(modelDep.getEventTitle());
        sDate.setText(modelDep.getStartDate());
        eDate.setText(modelDep.getEndDate());

        return convertView;
    }
}
