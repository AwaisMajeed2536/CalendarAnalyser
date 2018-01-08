package com.example.bisma.calendar_analyzer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.bisma.calendar_analyzer.NotificationService;
import com.example.bisma.calendar_analyzer.R;
import com.example.bisma.calendar_analyzer.adapters.TodaysTasksAdapter;
import com.example.bisma.calendar_analyzer.db.source.TasksSource;
import com.example.bisma.calendar_analyzer.helpers.Constants;
import com.example.bisma.calendar_analyzer.models.EventModelDep;
import com.example.bisma.calendar_analyzer.models.RemindersModel;
import com.example.bisma.calendar_analyzer.ui.CreateEventActivity;
import com.example.bisma.calendar_analyzer.ui.NotificationHandlerActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TodaysTasksFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    protected ListView todaysTasksLv;
    protected FloatingActionButton textualReportBtn;
    protected FloatingActionButton barChartBtn;
    protected FloatingActionButton addTaskBtn;
    private List<EventModelDep> dataList = new ArrayList<>();
    private TodaysTasksAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todays_tasks, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        todaysTasksLv = view.findViewById(R.id.todays_tasks_lv);
        todaysTasksLv.setOnItemClickListener(this);
        textualReportBtn = view.findViewById(R.id.textual_report);
        textualReportBtn.setOnClickListener(TodaysTasksFragment.this);
        barChartBtn = view.findViewById(R.id.bar_chart);
        barChartBtn.setOnClickListener(TodaysTasksFragment.this);
        addTaskBtn = view.findViewById(R.id.add_task);
        addTaskBtn.setOnClickListener(TodaysTasksFragment.this);
        getTodaysTasks();
        adapter = new TodaysTasksAdapter(getActivity(), dataList);
        todaysTasksLv.setAdapter(adapter);
    }

    public void getTodaysTasks() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String fromDateString = calendar.get(Calendar.YEAR) + "-" +
                String.format("%02d", calendar.get(Calendar.MONTH) + 1) + "-" +
                String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)) + " 00:00:01";

        dataList = TasksSource.newInstance().getByDate(fromDateString);
        adapter = new TodaysTasksAdapter(getActivity(), dataList);
        todaysTasksLv.setAdapter(adapter);
    }

    private void getData() {
        List<EventModelDep> taskList = TasksSource.newInstance().getAll();
        for (EventModelDep obj : taskList) {
            if (!dataList.contains(obj)) {
                dataList.add(obj);
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_task) {
            startActivity(new Intent(getActivity(), CreateEventActivity.class));
        } else if (view.getId() == R.id.textual_report) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_holder, new TodaysTextualReportFragment())
                    .addToBackStack(null).commitAllowingStateLoss();
        } else if (view.getId() == R.id.bar_chart) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_holder, new ScheduleBarChartFragment())
                    .addToBackStack(null).commitAllowingStateLoss();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        EventModelDep clickedEvent = (EventModelDep) parent.getAdapter().getItem(position);
        if (!clickedEvent.getEventTitle().equalsIgnoreCase("")) {
            RemindersModel model = new RemindersModel(0, clickedEvent.getEventTitle(), clickedEvent.getDescription(),
                    clickedEvent.getStartDate(), clickedEvent.getEndDate());
            Intent intent = new Intent(getActivity(), NotificationHandlerActivity.class);
            intent.putExtra(Constants.NOTIFICATION_DATA_PASS_KEY, model);
            startActivity(intent);
        }
    }
}
