package com.example.bisma.calendar_analyzer.db;


import com.example.bisma.calendar_analyzer.db.core.QueryGenerator;

/**
 * <p>Contains create queries for every table in the app.</p>
 * <p>
 * Created on 2016-12-03 14:58.
 *
 * @author M.Allaudin
 */
public class Queries {

    public static String createTasks() {
        return QueryGenerator.getInstance()
                .addIntegerPrimaryKey(DBConstants.Tasks.ID)
                .addTextField(DBConstants.Tasks.TITLE).addTextField(DBConstants.Tasks.DESCRIPTION)
                .addTextField(DBConstants.Tasks.START_DATE_TIME)
                .addTextField(DBConstants.Tasks.END_DATE_TIME)
                .addIntegerField(DBConstants.Tasks.IS_SCHEDULED)
                .generate(DBConstants.Tasks.TABLE_NAME);
    } // createTasks

    public static String createReminders() {
        return QueryGenerator.getInstance()
                .addIntegerPrimaryKey(DBConstants.Reminders.ID)
                .addTextField(DBConstants.Reminders.TITLE).addTextField(DBConstants.Reminders.TEXT)
                .addTextField(DBConstants.Reminders.START_DATE_TIME)
                .addTextField(DBConstants.Reminders.END_DATE_TIME)
                .generate(DBConstants.Reminders.TABLE_NAME);
    } // createReminders

    public static String drop(String tableName) {
        return String.format("DROP TABLE IF EXISTS %s", tableName);
    } // drop

    public static String truncate(String tableName) {
        return String.format("DELETE FROM %s", tableName);
    } // truncate

} // Queries
