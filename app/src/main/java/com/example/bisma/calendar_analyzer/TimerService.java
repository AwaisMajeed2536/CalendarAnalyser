package com.example.bisma.calendar_analyzer;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import com.example.bisma.calendar_analyzer.db.source.TasksSource;
import com.example.bisma.calendar_analyzer.helpers.Constants;
import com.example.bisma.calendar_analyzer.helpers.UtilHelpers;
import com.example.bisma.calendar_analyzer.helpers.notifications.NotificationUtils;
import com.example.bisma.calendar_analyzer.models.EventModelDep;
import com.example.bisma.calendar_analyzer.models.RemindersModel;

import java.util.Calendar;

/**
 * Created by Awais Majeed on 15-Jan-18.
 */

public class TimerService extends IntentService {

    private final String LOG_TAG = "Notification Service";
    Notification status;
    RemindersModel intentData = null;

    public TimerService() {
        super("TimerService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            intentData = intent.getParcelableExtra(Constants.SERVICE_DATA_PASS_KEY);
            getTotalTime(intentData.getStartDateTime(), intentData.getEndDateTime());
            mHandler.sendEmptyMessage(MSG_START_TIMER);
        }
    }

    private void getTotalTime(String startDateTime, String endDateTime) {
        Calendar startTime = UtilHelpers.getCalendarFromString(startDateTime);
        Calendar endTime = UtilHelpers.getCalendarFromString(endDateTime);
        long diff = endTime.getTimeInMillis() - startTime.getTimeInMillis();
        secs = (int) (diff / 1000) % 60;
        mins = (int) (diff / 60000) % 60;
        hours = (int) (diff / 3600000) % 60;
    }

    final int MSG_START_TIMER = 0;
    final int MSG_STOP_TIMER = 1;
    final int MSG_PAUSE_TIMER = 2;
    final int MSG_UPDATE_TIMER = 3;
    int hours, mins, secs;
    final int REFRESH_RATE = 1000;
    private boolean shouldShowNotification = true;
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_START_TIMER:
                    mHandler.sendEmptyMessage(MSG_UPDATE_TIMER);
                    break;

                case MSG_UPDATE_TIMER:
                    if (--secs < 0) {
                        secs = 59;
                        mins--;
                    }
                    if (mins < 0) {
                        mins = 59;
                        hours--;
                    }
                    if (hours < 0) {
                        mHandler.removeMessages(MSG_UPDATE_TIMER); // no more updates.
                    }
                    if (mins < 10 && shouldShowNotification) {
                        showNotification();
                        shouldShowNotification = false;
                    }
                    mHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIMER, REFRESH_RATE); //text view is updated every second,
                    break;
                case MSG_PAUSE_TIMER:
                    hours = mins = secs = 0;
                    mHandler.removeMessages(MSG_UPDATE_TIMER); // no more updates.
                    break;
                case MSG_STOP_TIMER:
                    hours = mins = secs = 0;
                    mHandler.removeMessages(MSG_UPDATE_TIMER); // no more updates.
                    updateTaskSource(2);
                    break;

                default:
                    break;
            }
        }
    };

    private void showNotification() {
        NotificationUtils.newInstance(this).showGeneralNotification(intentData.getId(), intentData.getTitle(), intentData.getText(),
                hours, mins, secs);
    }

    private void updateTaskSource(int status){
        TasksSource.newInstance().update(new EventModelDep(intentData.getId(), intentData.getTitle(),
                intentData.getText(), UtilHelpers.getDateInFormat(Calendar.getInstance(), true),
                UtilHelpers.getDateInFormat(Calendar.getInstance(), true), status));
    }
}
