package com.example.bisma.calendar_analyzer.db.core;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.example.bisma.calendar_analyzer.db.DBConstants;
import com.example.bisma.calendar_analyzer.db.Queries;


/**
 * Created on 2016-12-03 15:01.
 *
 * @author M.Allaudin
 */
public class CalendarAnalyzerSQliteHelper extends SQLiteOpenHelper {

    private CalendarAnalyzerSQliteHelper(Context ctx, String name, int version) {
        super(ctx, name, null, version);
    } // CalendarAnalyzerSQliteHelper

    @NonNull
    public static CalendarAnalyzerSQliteHelper newInstance(Context ctx, String name, int version) {
        return new CalendarAnalyzerSQliteHelper(ctx, name, version);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        db.execSQL(Queries.createTasks());
        db.execSQL(Queries.createReminders());
    } // onCreate

    /**
     * Drop all tables on upgrade and create a backup of user table.
     * Restore user table in (onCreate).
     */
    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTables(db);
        onCreate(db);
    } // onUpgrade

    private void dropTables(@NonNull SQLiteDatabase db) {
        db.execSQL(Queries.drop(DBConstants.Tasks.TABLE_NAME));
        db.execSQL(Queries.drop(DBConstants.Reminders.TABLE_NAME));
    } // dropTables

    public static void truncateTables(SQLiteDatabase db) {
        db.execSQL(Queries.truncate(DBConstants.Tasks.TABLE_NAME));
        db.execSQL(Queries.truncate(DBConstants.Reminders.TABLE_NAME));

    }//truncateTables
} // CalendarAnalyzerSQliteHelper
