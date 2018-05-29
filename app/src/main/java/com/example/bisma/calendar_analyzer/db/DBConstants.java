package com.example.bisma.calendar_analyzer.db;

/**
 * A helper class to store all database contracts in one place. Each
 * class in {@link DBConstants} represents a table will fields equal to
 * columns.
 * <p>
 * Created on 2016-12-03 14:56.
 *
 * @author M.Allaudin
 */
public class DBConstants {

    public static class Tasks {
        public static final String TABLE_NAME = "tasks";

        public static final String ID = "id";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String START_DATE_TIME = "start_date_time";
        public static final String END_DATE_TIME = "end_date_time";
        public static final String IS_SCHEDULED = "is_local";
        public static final String STATUS = "completed";
    }//Feed

    public static class Reminders {
        public static final String TABLE_NAME = "reminders";

        public static final String ID = "id";
        public static final String TITLE = "title";
        public static final String TEXT = "text";
        public static final String START_DATE_TIME = "start_date_time";
        public static final String END_DATE_TIME = "end_date_time";
    }//Feed

} // DBConstants
