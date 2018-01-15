package com.example.bisma.calendar_analyzer.helpers.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.bisma.calendar_analyzer.db.source.RemindersSource;
import com.example.bisma.calendar_analyzer.helpers.Constants;
import com.example.bisma.calendar_analyzer.models.RemindersModel;

/**
 * Created by Devprovider on 10/08/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras() != null) {
            int id = intent.getIntExtra(Constants.REMINDER_ID_PASS_KEY,0);
            RemindersModel reminder = RemindersSource.newInstance().getById(id);
            if (reminder != null) {
//                NotificationUtils.newInstance(context).showGeneralNotification(reminder);
                RemindersSource.newInstance().delete(reminder.getId());
            }
        }
    }

}
