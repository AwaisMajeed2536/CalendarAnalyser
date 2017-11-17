package com.example.bisma.calendar_analyzer.db.source.core;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.bisma.calendar_analyzer.db.core.DatabaseManager;
import com.example.bisma.calendar_analyzer.db.core.SortOrder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>Abstract implementation of {@link DataSource}, which acts
 * on underlying database. It will primarily add/remove Data
 * from database.</p>
 * <p>
 * Created on 2016-12-12 15:35.
 *
 * @param <T> the type of elements added or retreived from database
 * @author M.Allaudin
 * @version 1.0
 * @since App Version (17)
 */
public abstract class BaseDataSource<T> implements DataSource<T> {

    /**
     * This is supposed to be a primary key id and it will only return a single record.
     * For getting list of records, use {@link BaseDataSource#getAllWhere(String, String)}
     *
     * @param id id of row to be fetched
     * @return T
     */
    @Nullable
    @Override
    public T getById(int id) {
        final List<T> records = new ArrayList<>();
        DatabaseManager.query(getTableName(), getFilterKey() + " = ?", new String[]{String.valueOf(id)}, new DatabaseManager.CursorCallback() {
            @Override
            public void fetchData(@NonNull Cursor cursor) {
                records.add(getModelFromCursor(cursor));
            }
        });
        if (records.size() > 0) {
            return records.get(0);
        } else {
            return null;
        }
    } // getById

    /**
     * <p>Same as {@link BaseDataSource#getById(int)} (int)}, but returns list of recods.</p>
     *
     * @param id id of the records
     * @return T list of records
     */
    @NonNull
    @Override
    public List<T> getAllById(int id) {
        final List<T> records = new ArrayList<>();
        DatabaseManager.query(getTableName(), getFilterKey() + " = ?", new String[]{String.valueOf(id)}, new DatabaseManager.CursorCallback() {
            @Override
            public void fetchData(@NonNull Cursor cursor) {
                records.add(getModelFromCursor(cursor));
            }
        });
        return records;
    } // getById


    /**
     * <p>Returns records from databse with in a given range.</p>
     *
     * @param start start offset
     * @param end   end offset
     * @return T
     */
    @NonNull
    @Override
    public List<T> getByLimit(int start, int end, SortOrder order, String sortColumn) {
        final List<T> records = new ArrayList<>();
        String offset = String.format("%s, %s", start, end);

        DatabaseManager.queryLimited(getTableName(), offset, order, sortColumn, new DatabaseManager.CursorCallback() {
            @Override
            public void fetchData(@NonNull Cursor cursor) {
                records.add(getModelFromCursor(cursor));
            }
        });
        return records;
    }

    /**
     * <p>Get all records where column matches the value.</p>
     *
     * @param column column name
     * @param value  column value
     * @return T list of records
     */
    @NonNull
    @Override
    public List<T> getAllWhere(String column, String value) {
        final List<T> records = new ArrayList<>();
        DatabaseManager.query(getTableName(), column + " = ?", new String[]{value}, new DatabaseManager.CursorCallback() {
            @Override
            public void fetchData(@NonNull Cursor cursor) {
                records.add(getModelFromCursor(cursor));
            }
        });
        return records;
    } // getAllWhere


    /**
     * <p>Get all values where columns equal to values.</p>
     *
     * @param columns columns to be matched with values
     * @param values  vales required for columns
     * @return T list of records
     */
    @NonNull
    @Override
    public List<T> getAllByColumns(@NonNull String[] columns, String... values) {
        final List<T> records = new ArrayList<>();
        StringBuilder whereBuilder = new StringBuilder(200);

        for (int i = 0; i < columns.length - 1; i++) {
            whereBuilder.append(columns[i])
                    .append(" = ? ").append(" AND ");
        }

        whereBuilder.append(columns[columns.length - 1]).append(" = ?");

        DatabaseManager.query(getTableName(), whereBuilder.toString(), values, new DatabaseManager.CursorCallback() {
            @Override
            public void fetchData(@NonNull Cursor cursor) {
                records.add(getModelFromCursor(cursor));
            }
        });
        return records;
    }

    /**
     * <p>Get all values where columns equal to values and return unique records</p>
     *
     * @param columns columns to be matched with values
     * @param values  vales required for columns
     * @return T list of records
     */
    @NonNull
    @Override
    public List<T> getAllByColumnsUnique(@NonNull String groupBy, @NonNull String[] columns, String... values) {
        final List<T> records = new ArrayList<>();
        StringBuilder whereBuilder = new StringBuilder(200);

        for (int i = 0; i < columns.length - 1; i++) {
            whereBuilder.append(columns[i])
                    .append(" = ? ").append(" AND ");
        }

        whereBuilder.append(columns[columns.length - 1]).append(" = ?");

        DatabaseManager.queryUnique(getTableName(), whereBuilder.toString(), values, groupBy, new DatabaseManager.CursorCallback() {
            @Override
            public void fetchData(@NonNull Cursor cursor) {
                records.add(getModelFromCursor(cursor));
            }
        });
        return records;
    }

    /**
     * <p>Update record while checking multiple columns with <b>AND</b> condition. </p>
     *
     * @param columns columns to be checked
     * @param values  column values
     */
    @Override
    public void updateWhere(ContentValues content, @NonNull String[] columns, String... values) {
        StringBuilder whereBuilder = new StringBuilder(200);

        for (int i = 0; i < columns.length - 1; i++) {
            whereBuilder.append(columns[i])
                    .append(" = ? ").append(" AND ");
        }

        whereBuilder.append(columns[columns.length - 1]).append(" = ? ");
        DatabaseManager.update(getTableName(), content, whereBuilder.toString(), values);
    }

    /**
     * <p>Fetch records with given where clause and values</p>
     *
     * @param whereClause where clause for record selection
     * @param values      values for where clause
     * @return T list of records
     */
    @NonNull
    @Override
    public List<T> get(String whereClause, String... values) {
        final List<T> records = new ArrayList<>();
        DatabaseManager.query(getTableName(), whereClause, values, new DatabaseManager.CursorCallback() {
            @Override
            public void fetchData(@NonNull Cursor cursor) {
                records.add(getModelFromCursor(cursor));
            }
        });
        return records;
    }

    /**
     * <p>Delete all records which matches the ids.</p>
     *
     * @param ids ids of records to be deleted
     */
    @Override
    public void deleteRaw(int... ids) {
        String args = Arrays.toString(ids).replaceAll("\\[|\\]", "");
        DatabaseManager.deleteRaw(getTableName(), getFilterKey(), args);
    }


    /**
     * <p>Update column where id is equal to key.</p>
     *
     * @param column column to update
     * @param value  new value for column
     * @param key    key of record which will be udpated
     */
    @Override
    public void updateColumn(String column, String value, int key) {
        DatabaseManager.updateColumn(getTableName(), column, value, getFilterKey(), String.valueOf(key));
    } // updateColumn

    /**
     * @return T
     * @see {@link DatabaseManager#query(String, DatabaseManager.CursorCallback)}
     */
    @NonNull
    @Override
    public List<T> getAll() {
        final List<T> records = new ArrayList<>();
        DatabaseManager.query(getTableName(), new DatabaseManager.CursorCallback() {
            @Override
            public void fetchData(@NonNull Cursor cursor) {
                records.add(getModelFromCursor(cursor));
            }
        });
        return records;
    } // getAll

    @Override
    public void deleteWhere(String column, String value) {
        DatabaseManager.delete(getTableName(), column + " = ?", value);
    } // delete

    /**
     * @param models Data to be inserted in the table
     * @see {@link DatabaseManager#insert(String, int, DatabaseManager.InsertCallback)}
     */
    @Override
    public void insertOrUpdate(@NonNull final List<T> models) {
        final ContentValues values = new ContentValues();
        DatabaseManager.insert(getTableName(), models.size(), new DatabaseManager.InsertCallback() {
            @NonNull
            @Override
            public ContentValues getValues(@IntRange int position) {
                fillValues(models.get(position), values);
                return values;
            }
        });
    } // insertOrUpdate


    @Override
    public void insertOrUpdateAsync(@NonNull final List<T> models) {
        final ContentValues values = new ContentValues();
        DatabaseManager.insertAsync(getTableName(), models.size(), new DatabaseManager.InsertCallback() {
            @NonNull
            @Override
            public ContentValues getValues(@IntRange int position) {
                fillValues(models.get(position), values);
                return values;
            }
        });
    } // insertOrUpdate

    /**
     * @param model Data to be inserted in the table
     * @see {@link DatabaseManager#insert(String, ContentValues)}
     */

    @Override
    public void insertOrUpdate(T model) {
        ContentValues values = new ContentValues();
        fillValues(model, values);
        DatabaseManager.insert(getTableName(), values);
    } // insertOrUpdate

    /**
     * @param id id of row to be deleted
     * @see {@link DatabaseManager#delete(String, String, String...)}
     */
    @Override
    public void delete(int id) {
        DatabaseManager.delete(getTableName(), getFilterKey() + " = ?", String.valueOf(id));
    }

    /**
     * @see {@link DatabaseManager#deleteAll(String)}
     */
    @Override
    public void deleteAll() {
        DatabaseManager.deleteAll(getTableName());
    }

    @NonNull
    @Override
    public List<T> getAllSorted(@NonNull SortOrder sortOrder) {
        final List<T> records = new ArrayList<>();
        DatabaseManager.query(getTableName(), getFilterKey(), sortOrder.name(), new DatabaseManager.CursorCallback() {
            @Override
            public void fetchData(@NonNull Cursor cursor) {
                records.add(getModelFromCursor(cursor));
            }
        });
        return records;
    } // getAllSorted


    /**
     * Fill content values with values from the model. It
     * will be called by {@link BaseDataSource} whenever needed.
     *
     * @param model  model to fetch values from
     * @param values values to fill from model
     */
    protected abstract void fillValues(T model, ContentValues values);

    /**
     * Fetch values from the cursor and return model of respective type.
     * It will be called by {@link BaseDataSource} when needed.
     *
     * @param cursor cursor to fetch values from
     * @return T
     */
    @NonNull
    protected abstract T getModelFromCursor(Cursor cursor);

    /**
     * Get table name on which all the actions in this source will be taken.
     *
     * @return table name underlying table for current Data source
     */
    protected abstract String getTableName();

    /**
     * Get primary key of table. It will be used by <p>queries</p>
     * when id is required.
     *
     * @return string primary key of table
     * @see {@link BaseDataSource#getById(int)}
     * @see {@link BaseDataSource#delete(int)}
     */
    protected abstract String getFilterKey();



    public void updateByRawQuery(String query) {
        DatabaseManager.queryRaw(query, new DatabaseManager.CursorCallback() {
            @Override
            public void fetchData(@NonNull Cursor cursor) {

            }
        });
    }

} // BaseDataSource
