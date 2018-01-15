package com.example.bisma.calendar_analyzer.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bisma.calendar_analyzer.R;
import com.example.bisma.calendar_analyzer.TimerService;
import com.example.bisma.calendar_analyzer.db.source.TasksSource;
import com.example.bisma.calendar_analyzer.helpers.Constants;
import com.example.bisma.calendar_analyzer.helpers.UtilHelpers;
import com.example.bisma.calendar_analyzer.models.EventModelDep;
import com.example.bisma.calendar_analyzer.models.RemindersModel;

import java.util.Calendar;

/**
 * Created by Awais Majeed on 26/09/2017.
 */

public class NotificationHandlerActivity extends AppCompatActivity implements View.OnClickListener {

    protected TextView hourTv;
    protected TextView minTv;
    protected TextView secTv;
    protected TextView taskTitleTv;
    protected TextView taskDescriptionTv;
    protected Button startBtn;
    protected Button pauseBtn;
    protected Button stopBtn;
    RemindersModel intentData;
    private int id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_notification_handler);
        initView();
    }

    boolean isPlaying = true;

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.start_btn) {
            mHandler.sendEmptyMessage(MSG_START_TIMER);
            startService();
            startBtn.setEnabled(false);
        } else if (view.getId() == R.id.pause_btn) {
            if (isPlaying) {
                pauseBtn.setText("RESUME");
                mHandler.sendEmptyMessage(MSG_PAUSE_TIMER);
                isPlaying = false;
            } else {
                pauseBtn.setText("PAUSE");
                mHandler.sendEmptyMessage(MSG_START_TIMER);
                isPlaying = true;
            }
        } else if (view.getId() == R.id.stop_btn) {
            mHandler.sendEmptyMessage(MSG_STOP_TIMER);
            stopBtn.setEnabled(false);
            startBtn.setEnabled(false);
            pauseBtn.setEnabled(false);
        }
    }

    private void initView() {
        hourTv = findViewById(R.id.hour_tv);
        minTv = findViewById(R.id.min_tv);
        secTv = findViewById(R.id.sec_tv);
        taskTitleTv = findViewById(R.id.task_title_tv);
        taskDescriptionTv = findViewById(R.id.task_description_tv);
        startBtn = findViewById(R.id.start_btn);
        startBtn.setOnClickListener(NotificationHandlerActivity.this);
        pauseBtn = findViewById(R.id.pause_btn);
        pauseBtn.setOnClickListener(NotificationHandlerActivity.this);
        stopBtn = findViewById(R.id.stop_btn);
        stopBtn.setOnClickListener(NotificationHandlerActivity.this);
        setViewData();
    }

    private void setViewData() {
        intentData = getIntent().getParcelableExtra(Constants.NOTIFICATION_DATA_PASS_KEY);
        if (intentData != null) {
            id = intentData.getId();
            taskTitleTv.setText(intentData.getTitle());
            taskDescriptionTv.setText(intentData.getText());
            String totalTime = getTotalTime(intentData.getStartDateTime(), intentData.getEndDateTime());
            String[] times = totalTime.split(",");
            hourTv.setText(times[0]);
            minTv.setText(times[1]);
            secTv.setText(times[2]);
        } else {
            if (getIntent().getStringExtra(Constants.TITLE_PASS_KEY) != null) {
                Intent intent = getIntent();
                id = intent.getIntExtra(Constants.ID_PASS_KEY, 0);
                taskTitleTv.setText(intent.getStringExtra(Constants.TITLE_PASS_KEY));
                taskDescriptionTv.setText(intent.getStringExtra(Constants.DESC_PASS_KEY));
                hourTv.setText(String.format("%02d", intent.getIntExtra(Constants.HOUR_PASS_KEY, 0)));
                minTv.setText(String.format("%02d", intent.getIntExtra(Constants.MIN_PASS_KEY, 0)));
                secTv.setText(String.format("%02d", intent.getIntExtra(Constants.SEC_PASS_KEY, 0)));
                mHandler.sendEmptyMessage(MSG_START_TIMER);
                startBtn.setEnabled(false);
            }
        }
    }

    private String getTotalTime(String startDateTime, String endDateTime) {
        Calendar startTime = UtilHelpers.getCalendarFromString(startDateTime);
        Calendar endTime = UtilHelpers.getCalendarFromString(endDateTime);
        long diff = endTime.getTimeInMillis() - startTime.getTimeInMillis();
        int secs = (int) (diff / 1000) % 60;
        int mins = (int) (diff / 60000) % 60;
        int hours = (int) (diff / 3600000) % 60;
        return (String.format("%02d", hours) + "," + String.format("%02d", mins) + "," + String.format("%02d", secs));
    }


    final int MSG_START_TIMER = 0;
    final int MSG_STOP_TIMER = 1;
    final int MSG_PAUSE_TIMER = 2;
    final int MSG_UPDATE_TIMER = 3;
    int hours, mins, secs;
    final int REFRESH_RATE = 1000;

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_START_TIMER:
                    hours = Integer.parseInt(hourTv.getText().toString().equals("") ? "00" : hourTv.getText().toString());
                    mins = Integer.parseInt(minTv.getText().toString().equals("") ? "00" : minTv.getText().toString());
                    secs = Integer.parseInt(secTv.getText().toString().equals("") ? "00" : secTv.getText().toString());
                    mHandler.sendEmptyMessage(MSG_UPDATE_TIMER);
                    break;

                case MSG_UPDATE_TIMER:
                    if (--secs < 0) {
                        secs = 59;
                        mins--;
                        String.format("%02d", mins);
                    }
                    if (mins < 0) {
                        mins = 59;
                        hours--;
                        String.format("%02d", hours);
                    }
                    if (hours < 0) {
                        mHandler.removeMessages(MSG_UPDATE_TIMER); // no more updates.
                    }
                    secTv.setText(String.format("%02d", secs));
                    minTv.setText(String.format("%02d", mins));
                    hourTv.setText(String.format("%02d", hours));
                    mHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIMER, REFRESH_RATE); //text view is updated every second,
                    break;
                case MSG_PAUSE_TIMER:
                    hours = mins = secs = 0;
                    mHandler.removeMessages(MSG_UPDATE_TIMER); // no more updates.
                    break;
                case MSG_STOP_TIMER:
                    hours = mins = secs = 0;
                    secTv.setText(String.format("%02d", secs));
                    minTv.setText(String.format("%02d", mins));
                    hourTv.setText(String.format("%02d", hours));
                    mHandler.removeMessages(MSG_UPDATE_TIMER); // no more updates.
                    break;

                default:
                    break;
            }
        }
    };

    public void startService() {
        Intent serviceIntent = new Intent(this, TimerService.class);
        serviceIntent.putExtra(Constants.SERVICE_DATA_PASS_KEY, intentData);
        startService(serviceIntent);
        Constants.taskRunning = true;
        TasksSource.newInstance().insertOrUpdate(new EventModelDep(id, taskTitleTv.getText().toString(),
                taskDescriptionTv.getText().toString(), UtilHelpers.getDateInFormat(Calendar.getInstance(), true),
                UtilHelpers.getDateInFormat(Calendar.getInstance(), true), true));
    }
}
