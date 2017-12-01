package com.example.bisma.calendar_analyzer.ui;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.bisma.calendar_analyzer.R;
import com.example.bisma.calendar_analyzer.adapters.TodaysTasksAdapter;
import com.example.bisma.calendar_analyzer.db.source.RemindersSource;
import com.example.bisma.calendar_analyzer.db.source.TasksSource;
import com.example.bisma.calendar_analyzer.helpers.Constants;
import com.example.bisma.calendar_analyzer.helpers.UtilHelpers;
import com.example.bisma.calendar_analyzer.helpers.notifications.AlarmReceiver;
import com.example.bisma.calendar_analyzer.models.EventModel;
import com.example.bisma.calendar_analyzer.models.EventModelDep;
import com.example.bisma.calendar_analyzer.models.RemindersModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class TodaysTasksActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    protected ListView todaysTasksLv;
    protected FloatingActionButton addTaskBtn;
    private List<EventModelDep> dataList = new ArrayList<>();
    private TodaysTasksAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_tasks);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<EventModelDep> taskList = TasksSource.newInstance().getAll();
        for (EventModelDep obj : taskList) {
            if (!dataList.contains(obj)) {
                dataList.add(obj);

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_report_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_view_report) {
            //Todo generate graph activity and call here
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        todaysTasksLv = findViewById(R.id.todays_tasks_lv);
        todaysTasksLv.setOnItemClickListener(this);
        addTaskBtn = findViewById(R.id.add_task);
        addTaskBtn.setOnClickListener(TodaysTasksActivity.this);
        adapter = new TodaysTasksAdapter(this, dataList);
        todaysTasksLv.setAdapter(adapter);
        dataList = TasksSource.newInstance().getAll();
        getTodaysTasks();
    }

    public void getTodaysTasks() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String fromDateString = calendar.get(Calendar.YEAR) + "-" +
                String.format("%02d", calendar.get(Calendar.MONTH) + 1) + "-" +
                String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)) + " 00:00:01";

        dataList = TasksSource.newInstance().getByDate(fromDateString);
        adapter = new TodaysTasksAdapter(this, dataList);
        todaysTasksLv.setAdapter(adapter);
    }

    private void setAlarm(Calendar calendar, RemindersModel model) {
        int alarmKey = calendar.get(Calendar.YEAR) + calendar.get(Calendar.MONTH) + calendar.get(Calendar.DAY_OF_MONTH) +
                calendar.get(Calendar.HOUR_OF_DAY) + calendar.get(Calendar.MINUTE) + calendar.get(Calendar.SECOND);
        model.setId(alarmKey);
        RemindersSource.newInstance().insertOrUpdate(model);
        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        intent.putExtra(Constants.REMINDER_ID_PASS_KEY, alarmKey);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getBaseContext(), alarmKey, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                pendingIntent);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_task) {
            startActivity(new Intent(this, CreateLocalEventActivity.class));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        EventModelDep clickedEvent = (EventModelDep) parent.getAdapter().getItem(position);
        if (!clickedEvent.getEventTitle().equalsIgnoreCase("")) {
            RemindersModel model = new RemindersModel(0, clickedEvent.getEventTitle(), clickedEvent.getDescription(),
                    clickedEvent.getStartDate(), clickedEvent.getEndDate());
            Intent intent = new Intent(this, NotificationHandlerActivity.class);
            intent.putExtra(Constants.NOTIFICATION_DATA_PASS_KEY, model);
            startActivity(intent);
        }
    }


    private boolean isTodayEvent(String dateString) {
        try {
            Calendar toCal = Calendar.getInstance();
            int today, toMonth, toYear, day, month, year;
            today = toCal.get(Calendar.DAY_OF_MONTH);
            toMonth = toCal.get(Calendar.MONTH);
            toYear = toCal.get(Calendar.YEAR);
            SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
            Date date = sdf.parse(dateString);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            day = cal.get(Calendar.DAY_OF_MONTH);
            month = cal.get(Calendar.MONTH);
            year = cal.get(Calendar.YEAR);
            return today == day && month == toMonth && toYear == year;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
