package com.example.bisma.calendar_analyzer.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.bisma.calendar_analyzer.R;
import com.example.bisma.calendar_analyzer.adapters.CalendarViewPagerAdapter;
import com.example.bisma.calendar_analyzer.db.source.TasksSource;
import com.example.bisma.calendar_analyzer.fragment.CalendarViewFragment;
import com.example.bisma.calendar_analyzer.helpers.Constants;
import com.example.bisma.calendar_analyzer.helpers.UtilHelpers;
import com.example.bisma.calendar_analyzer.models.EventModelDep;
import com.google.api.client.util.DateTime;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.internal.Util;


public class CalendarView extends Fragment implements View.OnClickListener {

    private FloatingActionButton addTaskBtn;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1_calendar, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        addTaskBtn = rootView.findViewById(R.id.add_task);
        addTaskBtn.setOnClickListener(this);
        viewPager = rootView.findViewById(R.id.calendar_view);
        viewPager.setAdapter(new CalendarViewPagerAdapter(getActivity(), getActivity().getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(1);
        viewPager.setCurrentItem(1000 / 2);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_task) {
            startActivity(new Intent(getActivity(), CreateEventActivity.class));
        }
    }
}