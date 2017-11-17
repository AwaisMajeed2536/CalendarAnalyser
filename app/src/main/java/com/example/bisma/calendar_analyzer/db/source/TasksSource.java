package com.example.bisma.calendar_analyzer.db.source;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.example.bisma.calendar_analyzer.db.source.core.BaseDataSource;
import com.example.bisma.calendar_analyzer.models.EventModelDep;

import java.util.ArrayList;
import java.util.List;

import static com.example.bisma.calendar_analyzer.db.DBConstants.Tasks.TABLE_NAME;
import static com.example.bisma.calendar_analyzer.db.DBConstants.Tasks.ID;
import static com.example.bisma.calendar_analyzer.db.DBConstants.Tasks.TITLE;
import static com.example.bisma.calendar_analyzer.db.DBConstants.Tasks.DESCRIPTION;
import static com.example.bisma.calendar_analyzer.db.DBConstants.Tasks.START_DATE_TIME;
import static com.example.bisma.calendar_analyzer.db.DBConstants.Tasks.END_DATE_TIME;
import static com.example.bisma.calendar_analyzer.db.DBConstants.Tasks.IS_SCHEDULED;

/**
 * Created by Devprovider on 15/08/2017.
 */

public class TasksSource extends BaseDataSource<EventModelDep> {

    public TasksSource() {
    }

    public static TasksSource newInstance(){
        return new TasksSource();
    }

    @Override
    protected void fillValues(EventModelDep model, ContentValues values) {
        values.put(ID, model.getEventID());
        values.put(TITLE, model.getEventTitle());
        values.put(DESCRIPTION, model.getDescription());
        values.put(START_DATE_TIME, model.getStartDate());
        values.put(END_DATE_TIME, model.getEndDate());
        values.put(IS_SCHEDULED,model.isScheduled());
    }

    @NonNull
    @Override
    protected EventModelDep getModelFromCursor(Cursor cursor) {
        EventModelDep model = new EventModelDep();
        model.setEventID(cursor.getInt(cursor.getColumnIndex(ID)));
        model.setEventTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
        model.setDescription(cursor.getString(cursor.getColumnIndex(DESCRIPTION)));
        model.setStartDate(cursor.getString(cursor.getColumnIndex(START_DATE_TIME)));
        model.setEndDate(cursor.getString(cursor.getColumnIndex(END_DATE_TIME)));
        model.setScheduled(cursor.getInt(cursor.getColumnIndex(IS_SCHEDULED)));
        return model;
    }

    public List<EventModelDep> getByDate(String date){
        List<EventModelDep> toReturn = new ArrayList<>();
        List<EventModelDep> allTasks = getAll();
        for (EventModelDep obj : allTasks) {
            if (obj.getStartDate().contains(date)) {
                toReturn.add(obj);
            }
        }
        return toReturn;
    }

   /* public void updateReminder(EventModelDep model){
        String query = "UPDATE " + TABLE_NAME + " SET " + TITLE + " = \"" + model.getTitle() +
                "\" AND " + TEXT + " = \"" + model.getText() +"\" AND " + DATE_TIME + " = \"" + model.getDateTime() +
                "\" WHERE " + ID + "= "+ model.getId();
    }*/

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected String getFilterKey() {
        return ID;
    }
}
