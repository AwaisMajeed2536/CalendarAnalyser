package com.example.bisma.calendar_analyzer.helpers;

import android.app.Application;

import com.example.bisma.calendar_analyzer.db.core.CalendarAnalyzerSQliteHelper;
import com.example.bisma.calendar_analyzer.db.core.DatabaseConnection;

/**
 * Created by Devprovider on 16/08/2017.
 */

public class CalendarAnalyzerApplication extends Application {
    private static final String DATABASE_NAME = "calendaranalyzerdb.db";
    private static final int DATABASE_VERSION = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            CalendarAnalyzerSQliteHelper helper = CalendarAnalyzerSQliteHelper.newInstance(this, DATABASE_NAME, DATABASE_VERSION);
            DatabaseConnection.init(helper);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
