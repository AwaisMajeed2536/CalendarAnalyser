package com.example.bisma.calendar_analyzer.db.core;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import java.util.Arrays;

/**
 * <p>It manages every operation related to database e.g. reading,
 * writing, update tables, opening and closing database connections etc.
 * This should be the only class in the application which will talk to database.</p>
 * <p><b>Every single database operation should be routed through this class.</b></p>
 * <p>
 * Created on 2016-12-03 14:06.
 *
 * @author M.Allaudin
 * @version 1.0
 * @since App Version (17)
 */

public class DatabaseManager {

    private static final boolean LOG = false;

    private DatabaseManager() {
        throw new AssertionError("Instance is not allowed.");
    } // DatabaseManager

    /**
     * Insert a single row in a table.
     *
     * @param tableName table to be updated
     * @param values    values to be inserted in the table
     * @return id - insert id of new record
     */
    public static long insert(String tableName, @NonNull ContentValues values) {
        if (LOG) {
            log("insert -> %s -> size[%s]", tableName, values.size());
        }

        SQLiteDatabase db = DatabaseConnection.getAndOpenConnection();
        long insertId = db.insertWithOnConflict(tableName, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        DatabaseConnection.closeConnection();
        return insertId;
    } // insertOrUpdate

    /**
     * <p>Insert multiple rows in the table with name equalt to @tableName. Size
     * meaures, how many time {@link InsertCallback} will be called.</p>
     *
     * @param tableName      table to be updated
     * @param size           how many times callback should be called
     * @param insertCallback callback to get values to be inserted in the table
     */
    public static void insert(String tableName, int size, @NonNull InsertCallback insertCallback) {
        if (LOG) {
            log("insert -> %s size[%d]", tableName, size);
        }

        SQLiteDatabase db = DatabaseConnection.getAndOpenConnection();
        for (int i = 0; i < size; i++) {
            db.insertWithOnConflict(tableName, null, insertCallback.getValues(i), SQLiteDatabase.CONFLICT_REPLACE);
        }
        DatabaseConnection.closeConnection();
    } // insertOrUpdate




    /**
     * Execute raw query on table and return data.
     *
     * @param query      table to be queried
     * @param cursorCallback callback for passing Data to caller
     */
    public static void queryRaw(String query, @NonNull CursorCallback cursorCallback) {


        SQLiteDatabase db = DatabaseConnection.getAndOpenConnection();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                cursorCallback.fetchData(cursor);
            } while (cursor.moveToNext());
        }
        cursor.close();
        DatabaseConnection.closeConnection();
    } // update

    public static void insertAsync(final String tableName, final int size, @NonNull final InsertCallback insertCallback) {

        DbUtils.run(new Runnable() {
            @Override
            public void run() {

                if (LOG) {
                    log("insertAsync -> %s size[%d]", tableName, size);
                }

                SQLiteDatabase db = DatabaseConnection.getAndOpenConnection();
                for (int i = 0; i < size; i++) {
                    db.insertWithOnConflict(tableName, null, insertCallback.getValues(i), SQLiteDatabase.CONFLICT_REPLACE);
                }
                DatabaseConnection.closeConnection();

            }
        });

    } // insertOrUpdate

    /**
     * <p>Update a values (withou any criteria of selection) in the table</p>
     *
     * @param tableName table to be upated
     * @param values    new values
     * @return effected row
     */
    public static int update(String tableName, @NonNull ContentValues values) {
        if (LOG) {
            log("update -> %s %s", tableName, values.toString());
        }

        SQLiteDatabase db = DatabaseConnection.getAndOpenConnection();
        int effectedRow = db.updateWithOnConflict(tableName, values, null, null, SQLiteDatabase.CONFLICT_REPLACE);
        DatabaseConnection.closeConnection();
        return effectedRow;
    } // update

    /**
     * <p>Update row in the table with given criteria</p>
     *
     * @param tableName   table to be updated
     * @param values      new values
     * @param whereClause where selection string
     * @param whereArgs   arguments for where clause
     * @return int rows effected.
     */
    public static int update(String tableName, ContentValues values, String whereClause, String... whereArgs) {
        if (LOG) {
            log("update -> %s %s %s %s", tableName, values.toString(), whereClause, Arrays.toString(whereArgs));
        }

        SQLiteDatabase db = DatabaseConnection.getAndOpenConnection();
        int effectedRow = db.updateWithOnConflict(tableName, values, whereClause, whereArgs, SQLiteDatabase.CONFLICT_REPLACE);
        DatabaseConnection.closeConnection();

        return effectedRow;
    } // update

    /**
     * <p>Read all rows from the table</p>
     *
     * @param tableName      table to query
     * @param cursorCallback callback for passing Data to caller
     */
    public static void query(String tableName, @NonNull CursorCallback cursorCallback) {

        SQLiteDatabase db = DatabaseConnection.getAndOpenConnection();
        Cursor cursor = db.query(tableName, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                cursorCallback.fetchData(cursor);
            } while (cursor.moveToNext());
        }

        if (LOG) {
            log("query -> %s, records[%d]", tableName, cursor.getCount());
        }

        cursor.close();
        DatabaseConnection.closeConnection();
    } // update


    /**
     * <p>Read all rows from the table and return limited.</p>
     *
     * @param tableName      table to query
     * @param limit          limit clause
     * @param cursorCallback callback for passing Data to caller
     */
    public static void queryLimited(String tableName, String limit, SortOrder sortOrder, String column, @NonNull CursorCallback cursorCallback) {

        SQLiteDatabase db = DatabaseConnection.getAndOpenConnection();
        Cursor cursor = db.query(tableName, null, null, null, null, null, String.format("%s %s", column, sortOrder), limit);
        if (cursor.moveToFirst()) {
            do {
                cursorCallback.fetchData(cursor);
            } while (cursor.moveToNext());
        }

        if (LOG) {
            log("queryLimited -> %s, records[%d], limit[%s]", tableName, cursor.getCount(), limit);
        }

        cursor.close();
        DatabaseConnection.closeConnection();
    } // update


    /**
     * <p>Read all rows from the table with sort order</p>
     *
     * @param tableName      table to query
     * @param sortOrder      sort order of returned records
     * @param cursorCallback callback for passing Data to caller
     * @see SortOrder
     */
    public static void query(String tableName, String column, String sortOrder, @NonNull CursorCallback cursorCallback) {

        SQLiteDatabase db = DatabaseConnection.getAndOpenConnection();
        Cursor cursor = db.query(tableName, null, null, null, null, null, String.format("%s %s", column, sortOrder));
        if (cursor.moveToFirst()) {
            do {
                cursorCallback.fetchData(cursor);
            } while (cursor.moveToNext());
        }

        if (LOG) {
            log("query -> %s column[%s], sortOrder[%s], records[%d]", tableName, column, sortOrder, cursor.getCount());
        }

        cursor.close();
        DatabaseConnection.closeConnection();
    } // update

    /**
     * <p>Update column where id is equal to value.</p>
     *
     * @param tableName table name
     * @param column    update column
     * @param colValue  column value
     * @param idColumn  record id
     * @param idValue   record id value
     */
    public static void updateColumn(String tableName, String column, String colValue, String idColumn, String idValue) {
        if (LOG) {
            log("updateColumn -> %s col[%s, %s], id[%s, %s]", tableName, column, colValue, idColumn, idValue);
        }

        SQLiteDatabase db = DatabaseConnection.getAndOpenConnection();

        /**
         * This query is vulnerable and will be replaced with safe query
         * in future versions.
         *  - allaudin
         */

        // TODO: 2017-02-02 replace with safe query
        String query = String.format("UPDATE %s SET %s=? WHERE %s=?", tableName, column, idColumn);
        db.rawQuery(query, new String[]{colValue, idValue});
        DatabaseConnection.closeConnection();
    }

    /**
     * Query a table with given criteria specified in where clause.
     *
     * @param tableName      table to be queried
     * @param selection      where clause
     * @param selectionArgs  where arguments
     * @param cursorCallback callback for passing Data to caller
     */
    public static void query(String tableName, String selection, String[] selectionArgs, @NonNull CursorCallback cursorCallback) {


        SQLiteDatabase db = DatabaseConnection.getAndOpenConnection();
        Cursor cursor = db.query(tableName, null, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                cursorCallback.fetchData(cursor);
            } while (cursor.moveToNext());
        }

        if (LOG) {
            log("query -> %s selection[%s]  args %s , records [%d]", tableName, selection, Arrays.toString(selectionArgs), cursor.getCount());
        }
        cursor.close();
        DatabaseConnection.closeConnection();
    } // update


    /**
     * Query a table with given criteria specified in where clause and return unique records.
     *
     * @param tableName      table to be queried
     * @param selection      where clause
     * @param selectionArgs  where arguments
     * @param cursorCallback callback for passing Data to caller
     */
    public static void queryUnique(String tableName, String selection, String[] selectionArgs, String groupBy, @NonNull CursorCallback cursorCallback) {


        SQLiteDatabase db = DatabaseConnection.getAndOpenConnection();
        Cursor cursor = db.query(tableName, null, selection, selectionArgs, groupBy, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                cursorCallback.fetchData(cursor);
            } while (cursor.moveToNext());
        }

        if (LOG) {
            log("query -> %s selection[%s]  args %s , records [%d]", tableName, selection, Arrays.toString(selectionArgs), cursor.getCount());
        }
        cursor.close();
        DatabaseConnection.closeConnection();
    } // update

    /**
     * Delete all values from a table.
     *
     * @param tableName table to be cleared
     */
    public static void deleteAll(String tableName) {
        if (LOG) {
            log("deleteAll -> %s", tableName);
        }

        SQLiteDatabase db = DatabaseConnection.getAndOpenConnection();
        db.delete(tableName, null, null);
        DatabaseConnection.closeConnection();
    } // deleteAll

    /**
     * Delete a vlaue from table with given criteria.
     *
     * @param tableName   table from which values will be deleted
     * @param whereClause where clause
     * @param whereArgs   argumetns for where clause
     */
    public static void delete(String tableName, String whereClause, String... whereArgs) {
        if (LOG) {
            log("delete -> %s where[%s] args %s", tableName, whereClause, Arrays.toString(whereArgs));
        }

        SQLiteDatabase db = DatabaseConnection.getAndOpenConnection();
        db.delete(tableName, whereClause, whereArgs);
        DatabaseConnection.closeConnection();
    } // delete

    /**
     * Delete all recods where key column matches a value in <em>in clause</em>
     *
     * @param tableName table name
     * @param keyCol    column to run query against
     * @param inClause  in clause for where query
     */
    public static void deleteRaw(String tableName, String keyCol, String inClause) {
        SQLiteDatabase db = DatabaseConnection.getAndOpenConnection();
        db.execSQL(String.format("DELETE FROM %s WHERE %s IN ( %s )", tableName, keyCol, inClause));
        DatabaseConnection.closeConnection();
    } // delete

    private static void log(String format, Object... args) {
        //Utility.log("DatabaseManager", String.format(format, args));
    } // log

    /**
     * Callback for inserting multiple values from the list into the
     * database. {@link InsertCallback#getValues(int)} will be called
     * for every item in the list and returned content value will be
     * sent to database.
     *
     * @see {@link DatabaseManager#insert(String, int, InsertCallback)} for underlying
     * functionality.
     */
    public interface InsertCallback {
        @NonNull
        ContentValues getValues(@IntRange int position);
    } // InsertCallback

    /**
     * Callback for querying multiple row from the database. {@link DatabaseManager#query}
     * will iterate over cursor, passing the next cursor to {@link CursorCallback#fetchData(Cursor)}.
     * <p>
     * This callback will <b>not</b> be called if cursor is <em>empty</em>.
     *
     * @see {@link DatabaseManager#query} for underlying
     * functionality.
     */

    public interface CursorCallback {
        void fetchData(@NonNull Cursor cursor);
    } // CursorCallback

} // DatabaseManager
