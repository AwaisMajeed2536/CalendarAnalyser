package com.example.bisma.calendar_analyzer.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bisma.calendar_analyzer.R;
import com.example.bisma.calendar_analyzer.models.CalendarGridViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Devprovider on 05/07/2017.
 */

public class CalendarGridViewAdapter extends BaseAdapter {

    private Context context;
    private List<CalendarGridViewModel> dataList;
    private int currentMonth;

    public CalendarGridViewAdapter(Context context, List<CalendarGridViewModel> dataList, int currentMonth) {
        this.context = context;
        this.currentMonth = currentMonth;
        int steps = getSteps(dataList.get(0).getDayName(),dataList.get(1).getDayName());
        this.dataList = shiftToRight(steps, dataList);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CalViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new CalViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.calendar_gridview_item, parent, false);

            viewHolder.dayWrapper = (LinearLayout) convertView.findViewById(R.id.day_wrapper);
            viewHolder.datTv = (TextView) convertView.findViewById(R.id.month_day_tv);
            viewHolder.taskTv = (TextView) convertView.findViewById(R.id.task_tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CalViewHolder) convertView.getTag();
        }

        CalendarGridViewModel item = dataList.get(position);
        viewHolder.datTv.setText("" + item.getDay());
        viewHolder.taskTv.setText("" + item.getTask());
        if(item.getDay() == -1){
            viewHolder.datTv.setText("");
            viewHolder.taskTv.setText("");
        }

        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int onGoingMonth = cal.get(Calendar.MONTH);
        int currentDay =  cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        if (onGoingMonth == currentMonth && year == item.getYear()) {
            if (item.getDay() == currentDay) {
                viewHolder.dayWrapper.setBackgroundColor(ContextCompat.getColor(context, R.color.black));
                viewHolder.datTv.setTextColor(ContextCompat.getColor(context, R.color.white));
                viewHolder.taskTv.setTextColor(ContextCompat.getColor(context, R.color.white));
            } else {
                viewHolder.dayWrapper.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                viewHolder.datTv.setTextColor(ContextCompat.getColor(context, R.color.black));
                viewHolder.taskTv.setTextColor(ContextCompat.getColor(context, R.color.black));
            }
        }


        return convertView;
    }


    class CalViewHolder{
        LinearLayout dayWrapper;
        TextView datTv;
        TextView taskTv;
    }

    List<CalendarGridViewModel> shiftToRight(int steps, List<CalendarGridViewModel> dataList){
        List<CalendarGridViewModel> returnList = new ArrayList<>();
        for(int i=0; i<steps; i++){
            returnList.add(new CalendarGridViewModel(-1, "","","",-1));
        }
        for(int i= 0; i <dataList.size(); i++){
            returnList.add(dataList.get(i));
        }
        return returnList;
    }

    int getSteps(String currentDayName, String nextDayName){
        if(currentDayName.equalsIgnoreCase("S")) {
            if (nextDayName.equalsIgnoreCase("M")) {
                return 0;
            } else{
                return 6;
            }
        } else if(currentDayName.equalsIgnoreCase("M")){
            return 1;
        } else if(currentDayName.equalsIgnoreCase("T")){
            if (nextDayName.equalsIgnoreCase("W")) {
                return 2;
            } else{
                return 4;
            }
        } else if(currentDayName.equalsIgnoreCase("W")){
            return 3;
        } else if(currentDayName.equalsIgnoreCase("F")){
            return 5;
        }
        return 0;
    }

}
