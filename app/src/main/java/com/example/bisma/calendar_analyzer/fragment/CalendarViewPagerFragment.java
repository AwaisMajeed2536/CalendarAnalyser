package com.example.bisma.calendar_analyzer.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bisma.calendar_analyzer.R;
import com.example.bisma.calendar_analyzer.adapters.CalendarViewPagerAdapter;
import com.example.bisma.calendar_analyzer.ui.CreateEventActivity;


public class CalendarViewPagerFragment extends Fragment implements View.OnClickListener {

    private FloatingActionButton addTaskBtn;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar_view_pager, container, false);
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