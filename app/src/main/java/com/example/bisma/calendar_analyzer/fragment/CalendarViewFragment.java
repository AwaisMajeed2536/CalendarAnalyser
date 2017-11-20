package com.example.bisma.calendar_analyzer.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.bisma.calendar_analyzer.R;
import com.example.bisma.calendar_analyzer.adapters.CalendarGridViewAdapter;
import com.example.bisma.calendar_analyzer.db.source.TasksSource;
import com.example.bisma.calendar_analyzer.helpers.Constants;
import com.example.bisma.calendar_analyzer.helpers.UtilHelpers;
import com.example.bisma.calendar_analyzer.models.CalendarGridViewModel;
import com.example.bisma.calendar_analyzer.models.EventModelDep;
import com.example.bisma.calendar_analyzer.ui.GoogleApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Devprovider on 05/07/2017.
 */

public class CalendarViewFragment extends Fragment implements AdapterView.OnItemClickListener {

    private CalendarGridViewAdapter adapter;
    private GridView calendarGv;
    private TextView monthNameTv;
    private TextView yearNameTv;
    private Date date;
    private List<EventModelDep> eventsList = new ArrayList<>();
    public static final String CAL_DATE = "cal_data";

    public static CalendarViewFragment newInstance(Date date) {

        Bundle args = new Bundle();
        args.putLong(CAL_DATE, date.getTime());
        CalendarViewFragment fragment = new CalendarViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar_view, container, false);
        long time = getArguments().getLong(CAL_DATE);
        date = new Date();
        date.setTime(time);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        calendarGv = rootView.findViewById(R.id.calendar_gv);
        monthNameTv = rootView.findViewById(R.id.month_tv);
        yearNameTv = rootView.findViewById(R.id.year_tv);
        eventsList = TasksSource.newInstance().getAll();
        setData();
    }

    private void setData(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int monthNumber = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        String monthName = UtilHelpers.getMonthName(monthNumber);
        String yearName = String.valueOf(year);
        monthNameTv.setText(monthName);
        yearNameTv.setText(yearName);
        int daysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        List<CalendarGridViewModel> dataList = getCalendarData(daysOfMonth, monthNumber, year);
        adapter = new CalendarGridViewAdapter(getActivity(), dataList, monthNumber);
        calendarGv.setAdapter(adapter);
        calendarGv.setOnItemClickListener(this);
    }


    private List<CalendarGridViewModel> getCalendarData(int daysOfMonth, int currentMonth, int year) {
        List<CalendarGridViewModel> list = new ArrayList<>();
        for (int i = 1; i <= daysOfMonth; i++) {
            String today = year + "-" + String.format("%02d", currentMonth + 1) + "-" + String.format("%02d", i);
            boolean flag = false;
            int j = 0;
            while (j < eventsList.size()) {
                if (eventsList.get(j).getStartDate().contains(today)) {
                    flag = true;
                    break;
                }
                j++;
            }
            if (flag) {
                list.add(new CalendarGridViewModel(i, eventsList.get(j).getEventTitle(), getDayName(i, currentMonth, year),
                        UtilHelpers.getMonthName(currentMonth), year));
            } else {
                list.add(new CalendarGridViewModel(i, "", getDayName(i, currentMonth, year),
                        UtilHelpers.getMonthName(currentMonth), year));
            }
        }
        return list;
    }

    public String getDayName(int dayOfMonth, int currentMonth, int year) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.DATE, dayOfMonth);
        cal.set(Calendar.MONTH, currentMonth);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        switch (cal.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                return "S";
            case Calendar.MONDAY:
                return "M";
            case Calendar.TUESDAY:
                return "T";
            case Calendar.WEDNESDAY:
                return "W";
            case Calendar.THURSDAY:
                return "T";
            case Calendar.FRIDAY:
                return "F";
            case Calendar.SATURDAY:
                return "S";
        }
        return null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CalendarGridViewModel model = (CalendarGridViewModel) parent.getAdapter().getItem(position);
        new AlertDialog.Builder(getActivity()).setTitle(model.getTask() + " " + model.getDay()
        + "/" +model.getMonthName()+"/"+model.getYear()).setMessage("").create().show();
    }
}
