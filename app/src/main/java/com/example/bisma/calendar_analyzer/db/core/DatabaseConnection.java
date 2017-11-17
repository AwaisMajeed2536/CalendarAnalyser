package com.example.bisma.calendar_analyzer.db.core;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>A <b>thread safe</b> connection manager for maintaing a
 * database connection effetively.</p>
 * <p>
 * <p>Before using any of methods in this class, you must call {@link DatabaseConnection#init}
 * with appropriate {@link SQLiteOpenHelper}, otherwise an {@link IllegalStateException} will
 * be thrown if instance is used without <b>initialization</b>.</p>
 * <p>
 * <p>Best place to initialize this class is {@link Application#onCreate()}.</p>
 * <p>
 * Created on 2016-12-03 14:08.
 *
 * @author M.Allaudin
 * @see DatabaseConnection#getInstance()
 */
public final class DatabaseConnection {

    private static DatabaseConnection mInstance;
    private static SQLiteOpenHelper mSqLiteOpenHelper;

    private AtomicInteger mCounter;
    private SQLiteDatabase mSqLiteDatabase;

    private DatabaseConnection() {
        mCounter = new AtomicInteger(0);
    }

    /**
     * Initialize this database connection with {@link SQLiteOpenHelper}. This
     * helper will be use {@link DatabaseConnection#openConnection} for opening
     * new database connections.
     *
     * @param helper {@link SQLiteOpenHelper}
     */
    public static void init(SQLiteOpenHelper helper) {
        if (mSqLiteOpenHelper == null) {
            mInstance = new DatabaseConnection();
            mSqLiteOpenHelper = helper;
        }
    } // init

    /**
     * Returns a singleton instance of {@link DatabaseConnection}.
     *
     * @return DatabaseConnection
     */
    private static DatabaseConnection getInstance() {

        if (mInstance == null) {
            throw new IllegalStateException(String.format("%s is not initialized.", DatabaseConnection.class.getName()));
        }

        return mInstance;
    } // getInstance

    /**
     * A static proxy for {@link DatabaseConnection#openConnection()}
     */
    public static SQLiteDatabase getAndOpenConnection() {
        return DatabaseConnection.getInstance().openConnection();
    } // getAndOpenConnection

    /**
     * A static proxy for {@link DatabaseConnection#close()}
     */
    public static void closeConnection() {
        DatabaseConnection.getInstance().close();
    } // getAndOpenConnection

    /**
     * Opens a new connection if there is no connection available.
     * Otherwise existing connection will be returned.
     *
     * @return writable {@link SQLiteDatabase}
     */
    private synchronized SQLiteDatabase openConnection() {

        if (mCounter.incrementAndGet() == 1) {
            mSqLiteDatabase = mSqLiteOpenHelper.getWritableDatabase();
        }

        return mSqLiteDatabase;
    } // openConnection

    /**
     * Close underlying database connection <em>if it is not being
     * used by any other thread</em>.
     */
    private synchronized void close() {

        if (mCounter.decrementAndGet() == 0) {
            mSqLiteDatabase.close();
        }

    } // close

} // DatabaseConnection
