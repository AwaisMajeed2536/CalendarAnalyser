package com.example.bisma.calendar_analyzer.helpers;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.example.bisma.calendar_analyzer.db.core.CalendarAnalyzerSQliteHelper;
import com.example.bisma.calendar_analyzer.db.core.DatabaseConnection;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Devprovider on 16/08/2017.
 */

public class CalendarAnalyzerApplication extends Application {
    private static final String DATABASE_NAME = "calendaranalyzerdb.db";
    private static final int DATABASE_VERSION = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        try {
            CalendarAnalyzerSQliteHelper helper = CalendarAnalyzerSQliteHelper.newInstance(this, DATABASE_NAME, DATABASE_VERSION);
            DatabaseConnection.init(helper);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
