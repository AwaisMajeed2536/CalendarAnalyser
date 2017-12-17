package com.example.bisma.calendar_analyzer.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bisma.calendar_analyzer.R;
import com.example.bisma.calendar_analyzer.helpers.Constants;
import com.example.bisma.calendar_analyzer.models.EventModelDep;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Awais Majeed on 23/09/2017.
 */

public class TodaysTasksAdapter extends ArrayAdapter<EventModelDep> {

    public static final String FROM_HOUR = "FROM_HOUR";
    public static final String TO_HOUR = "TO_HOUR";
    private Context context;
    private List<EventModelDep> dataList = new ArrayList<>();

    public TodaysTasksAdapter(Context context, List<EventModelDep> dataList) {
        super(context, -1, dataList);
        this.context = context;
        dataList = manipulateList(dataList);
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Nullable
    @Override
    public EventModelDep getItem(int position) {
        return dataList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.li_todays_tasks, parent, false);
            convertView.setTag(new TTViewHolder(convertView));
        }
        TTViewHolder holder = (TTViewHolder) convertView.getTag();
        holder.setData(getItem(position));
        return convertView;
    }

    private class TTViewHolder {
        TextView timeTv, titleTv;

        public TTViewHolder(View view) {
            timeTv = (TextView) view.findViewById(R.id.time_tv);
            titleTv = (TextView) view.findViewById(R.id.event_title_tv);
        }

        private void setData(EventModelDep obj) {
            String time = obj.getStartDate();
            if (time.split(" ").length > 1)
                timeTv.setText(time.split(" ")[1]);
            else
                timeTv.setText(time);
            titleTv.setText(obj.getEventTitle());
        }
    }

    private List<EventModelDep> manipulateList(List<EventModelDep> dataList) {
        List<EventModelDep> toReturn = new ArrayList<>();
        int position = 0;
        for (int i = 0; i < 24; i++) {
            EventModelDep obj = new EventModelDep();
            if (dataList.size() > position)
                obj = dataList.get(position);
            HashMap<String, Integer> hours = getHours(obj.getStartDate(), obj.getEndDate());
            if (hours.get(FROM_HOUR) == i) {
                toReturn.add(new EventModelDep(obj.getEventID(), obj.getEventTitle(), obj.getDescription(),
                        obj.getStartDate(), obj.getEndDate()));
                position++;
            } else {
                toReturn.add(new EventModelDep(i + 1, "", "",
                        String.format("%02d", i) + ":00:00", String.format("%02d", i + 1) + ":00:00"));
            }
        }
        return toReturn;
    }

    private HashMap<String, Integer> getHours(String from, String to) {
        HashMap<String, Integer> toReturn = new HashMap<>();
        if (from.length() == 0) {
            toReturn.put(FROM_HOUR, -1);
            toReturn.put(TO_HOUR, -1);
            return toReturn;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
        try {
            Date fromHour = sdf.parse(from);
            Date toHour = sdf.parse(to);
            toReturn.put(FROM_HOUR, fromHour.getHours());
            toReturn.put(TO_HOUR, toHour.getHours());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return toReturn;
    }
}
