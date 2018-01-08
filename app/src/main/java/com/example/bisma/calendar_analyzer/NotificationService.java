package com.example.bisma.calendar_analyzer;

/**
 * Created by Awais Majeed on 08-Jan-18.
 */

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
        intentData = intent.getParcelableExtra(Constants.SERVICE_DATA_PASS_KEY);
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        intentData = intent.getParcelableExtra(Constants.SERVICE_DATA_PASS_KEY);
        if (intent.getAction().equals(Constants.STARTFOREGROUND_ACTION)) {
            showNotification();
            Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();

        } else if (intent.getAction().equals(Constants.PLAY_ACTION)) {
            Toast.makeText(this, "Clicked Play", Toast.LENGTH_SHORT).show();
            Log.i(LOG_TAG, "Clicked Play");
        } else if (intent.getAction().equals(Constants.PAUSE_ACTION)) {
            Toast.makeText(this, "Clicked Pause", Toast.LENGTH_SHORT).show();
            Log.i(LOG_TAG, "Clicked Previous");
        } else if (intent.getAction().equals(Constants.STOP_ACTION)) {
            Toast.makeText(this, "Clicked Stop", Toast.LENGTH_SHORT).show();
            Log.i(LOG_TAG, "Clicked Next");
        } else if (intent.getAction().equals(
                Constants.STOPFOREGROUND_ACTION)) {
            Log.i(LOG_TAG, "Received Stop Foreground Intent");
            Toast.makeText(this, "Service Stoped", Toast.LENGTH_SHORT).show();
            stopForeground(true);
            stopSelf();
        }
        return START_STICKY;
    }

    private void showNotification() {
// Using RemoteViews to bind custom layouts into Notification
        RemoteViews views = new RemoteViews(getPackageName(),
                R.layout.status_bar);
        View view = View.inflate(this, R.layout.status_bar_expanded, null);
        RemoteViews bigViews = new RemoteViews(getPackageName(),
                R.layout.status_bar_expanded);

// showing default album image
        views.setViewVisibility(R.id.status_bar_icon, View.VISIBLE);
        views.setViewVisibility(R.id.status_bar_album_art, View.GONE);
        bigViews.setImageViewBitmap(R.id.status_bar_album_art,
                Constants.getDefaultAlbumArt(this));

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Constants.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Intent playIntent = new Intent(this, NotificationService.class);
        playIntent.setAction(Constants.PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                playIntent, 0);

        Intent pauseIntent = new Intent(this, NotificationService.class);
        pauseIntent.setAction(Constants.PAUSE_ACTION);
        PendingIntent ppauseIntent = PendingIntent.getService(this, 0,
                pauseIntent, 0);

        Intent stopIntent = new Intent(this, NotificationService.class);
        stopIntent.setAction(Constants.STOP_ACTION);
        PendingIntent pstopIntent = PendingIntent.getService(this, 0,
                stopIntent, 0);

        Intent closeIntent = new Intent(this, NotificationService.class);
        closeIntent.setAction(Constants.STOPFOREGROUND_ACTION);
        PendingIntent pcloseIntent = PendingIntent.getService(this, 0,
                closeIntent, 0);

        views.setOnClickPendingIntent(R.id.play_btn, pplayIntent);
        bigViews.setOnClickPendingIntent(R.id.play_btn, pplayIntent);

        views.setOnClickPendingIntent(R.id.pause_btn, ppauseIntent);
        bigViews.setOnClickPendingIntent(R.id.pause_btn, ppauseIntent);

        views.setOnClickPendingIntent(R.id.stop_btn, pstopIntent);
        bigViews.setOnClickPendingIntent(R.id.stop_btn, pstopIntent);

        views.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);

        views.setImageViewResource(R.id.status_bar_play,
                R.drawable.ic_pause);
        bigViews.setImageViewResource(R.id.status_bar_play,
                R.drawable.ic_pause);

        views.setTextViewText(R.id.status_bar_track_name, intentData.getTitle());
        bigViews.setTextViewText(R.id.status_bar_track_name, intentData.getTitle());

        String totalTime = getTotalTime(intentData.getStartDateTime(), intentData.getEndDateTime());
        String[] times = totalTime.split(",");
        bigViews.setTextViewText(R.id.hour_tv, times[0]);
        bigViews.setTextViewText(R.id.min_tv, times[1]);
        bigViews.setTextViewText(R.id.sec_tv, times[2]);

        status = new Notification.Builder(this).build();
        status.contentView = views;
        status.bigContentView = bigViews;
        status.flags = Notification.FLAG_ONGOING_EVENT;
        status.icon = R.mipmap.app_icon;
        status.contentIntent = pendingIntent;
        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, status);
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

}