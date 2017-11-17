package com.example.bisma.calendar_analyzer.db.source;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.example.bisma.calendar_analyzer.db.source.core.BaseDataSource;
import com.example.bisma.calendar_analyzer.models.RemindersModel;

import static com.example.bisma.calendar_analyzer.db.DBConstants.Reminders.END_DATE_TIME;
import static com.example.bisma.calendar_analyzer.db.DBConstants.Reminders.ID;
import static com.example.bisma.calendar_analyzer.db.DBConstants.Reminders.START_DATE_TIME;
import static com.example.bisma.calendar_analyzer.db.DBConstants.Reminders.TABLE_NAME;
import static com.example.bisma.calendar_analyzer.db.DBConstants.Reminders.TITLE;
import static com.example.bisma.calendar_analyzer.db.DBConstants.Reminders.TEXT;

/**
 * Created by Devprovider on 15/08/2017.
 */

public class RemindersSource extends BaseDataSource<RemindersModel> {

    public RemindersSource() {
    }

    public static RemindersSource newInstance() {
        return new RemindersSource();
    }

    @Override
    protected void fillValues(RemindersModel model, ContentValues values) {
        values.put(ID, model.getId());
        values.put(TITLE, model.getTitle());
        values.put(TEXT, model.getText());
        values.put(START_DATE_TIME, model.getStartDateTime());
        values.put(END_DATE_TIME, model.getEndDateTime());
    }

    @NonNull
    @Override
    protected RemindersModel getModelFromCursor(Cursor cursor) {
        RemindersModel model = new RemindersModel();
        model.setId(cursor.getInt(cursor.getColumnIndex(ID)));
        model.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
        model.setText(cursor.getString(cursor.getColumnIndex(TEXT)));
        model.setStartDateTime(cursor.getString(cursor.getColumnIndex(START_DATE_TIME)));
        model.setEndDateTime(cursor.getString(cursor.getColumnIndex(END_DATE_TIME)));
        return model;
    }

    public void updateReminder(RemindersModel model) {
        String query = "UPDATE " + TABLE_NAME + " SET " + TITLE + " = \"" + model.getTitle() +
                "\" AND " + TEXT + " = \"" + model.getText() + "\" AND " + START_DATE_TIME + " = \"" + model.getStartDateTime() +
                "\" WHERE " + ID + "= " + model.getId();
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected String getFilterKey() {
        return ID;
    }
}
