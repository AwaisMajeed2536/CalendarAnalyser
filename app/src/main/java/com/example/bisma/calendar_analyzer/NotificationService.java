package com.example.bisma.calendar_analyzer;

/**
 * Created by Awais Majeed on 08-Jan-18.
 */

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.os.Handler;
import android.os.IBinder;
import android.app.Service;
import android.content.Intent;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bisma.calendar_analyzer.helpers.Constants;
import com.example.bisma.calendar_analyzer.helpers.UtilHelpers;
import com.example.bisma.calendar_analyzer.models.RemindersModel;
import com.example.bisma.calendar_analyzer.ui.MainActivity;

import java.util.Calendar;

public class NotificationService extends Service {

    private final String LOG_TAG = "Notification Service";
    Notification status;
    RemindersModel intentData;

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            intentData = intent.getParcelableExtra(Constants.SERVICE_DATA_PASS_KEY);
            if (intentData != null) {
                getTotalTime(intentData.getStartDateTime(), intentData.getEndDateTime());
                mHandler.sendEmptyMessage(MSG_START_TIMER);
            } else {
                Toast.makeText(this, "Error Running BG Service", Toast.LENGTH_SHORT).show();
            }
            if (intent.getAction().equals(Constants.STARTFOREGROUND_ACTION)) {
                Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();

            } else if (intent.getAction().equals(Constants.STOPFOREGROUND_ACTION)) {
                Log.i(LOG_TAG, "Received Stop Foreground Intent");
                Toast.makeText(this, "Service Stoped", Toast.LENGTH_SHORT).show();
                stopForeground(true);
                stopSelf();
            }
        } else {
            Toast.makeText(this, "Error Running BG Service", Toast.LENGTH_SHORT).show();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void showNotification() {
// Using RemoteViews to bind custom layouts into Notification
        RemoteViews views = new RemoteViews(getPackageName(),
                R.layout.status_bar);
        RemoteViews bigViews = new RemoteViews(getPackageName(),
                R.layout.status_bar_expanded);

// showing default album image
        views.setViewVisibility(R.id.status_bar_icon, View.VISIBLE);
        views.setViewVisibility(R.id.status_bar_album_art, View.GONE);
        bigViews.setImageViewBitmap(R.id.status_bar_album_art,
                Constants.getDefaultAlbumArt(this));

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.putExtra(Constants.TITLE_PASS_KEY, intentData.getTitle());
        notificationIntent.putExtra(Constants.DESC_PASS_KEY, intentData.getText());
        notificationIntent.putExtra(Constants.SEC_PASS_KEY, secs);
        notificationIntent.putExtra(Constants.MIN_PASS_KEY, mins);
        notificationIntent.putExtra(Constants.HOUR_PASS_KEY, hours);
        notificationIntent.setAction(Constants.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Intent closeIntent = new Intent(this, NotificationService.class);
        closeIntent.setAction(Constants.STOPFOREGROUND_ACTION);
        PendingIntent pcloseIntent = PendingIntent.getService(this, 0,
                closeIntent, 0);

        views.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);

        views.setTextViewText(R.id.status_bar_track_name, intentData.getTitle());
        bigViews.setTextViewText(R.id.status_bar_track_name, intentData.getTitle());


        status = new Notification.Builder(this).build();
        status.contentView = views;
        status.bigContentView = bigViews;
        status.flags = Notification.FLAG_ONGOING_EVENT;
        status.icon = R.mipmap.app_icon;
        status.contentIntent = pendingIntent;
        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, status);
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
                    if (mins < 10) {
                        showNotification();
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
                    break;

                default:
                    break;
            }
        }
    };

}