package com.example.bisma.calendar_analyzer.helpers.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.example.bisma.calendar_analyzer.R;
import com.example.bisma.calendar_analyzer.helpers.Constants;
import com.example.bisma.calendar_analyzer.models.EventModelDep;
import com.example.bisma.calendar_analyzer.models.RemindersModel;
import com.example.bisma.calendar_analyzer.ui.MainActivity;
import com.example.bisma.calendar_analyzer.ui.NotificationHandlerActivity;


/**
 * Created by Ravi on 01/06/15.
 */
public class NotificationUtils {


    private String TAG = NotificationUtils.class.getSimpleName();

    private Context mContext;

    public NotificationUtils() {
    }

    private NotificationUtils(Context mContext) {
        this.mContext = mContext;
    }

    public static NotificationUtils newInstance(Context mContext) {
        return new NotificationUtils(mContext);
    }

    public void showGeneralNotification(RemindersModel reminder) {
        Notification notification = buildNotification(1, reminder).build();
        Uri uri = Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.notification_tone);
        notification.sound = uri;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

    protected NotificationCompat.Builder buildNotification(int notificationId, RemindersModel reminder) {
        Intent intent = new Intent(mContext, NotificationHandlerActivity.class);
        intent.putExtra(Constants.NOTIFICATION_DATA_PASS_KEY, reminder);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pIntent = PendingIntent.getActivity(
                mContext,
                notificationId,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                // Set Icon
                .setSmallIcon(R.drawable.ic_nav_add_reminder)
                // Set Ticker Message
                .setTicker(mContext.getString(R.string.app_name))
                // Dismiss Notification
                .setAutoCancel(true)
                // Set PendingIntent into Notification
                .setContentIntent(pIntent);
            // Build a simpler notification, without buttons
            //
            builder = builder.setContentTitle(mContext.getString(R.string.app_name))
                    .setContentText(reminder.getTitle())
                    .setSmallIcon(R.drawable.ic_nav_add_reminder);
        return builder;
    }




}