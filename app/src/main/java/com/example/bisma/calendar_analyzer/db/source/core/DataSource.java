package com.example.bisma.calendar_analyzer.db.source.core;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.example.bisma.calendar_analyzer.db.core.SortOrder;

import java.util.List;



/**
 * Created on 2016-12-12 15:36.
 * <p>
 * Datasource is any source from where Data can be retreived. Every
 * Data source must implement this contract. </p>
 *
 * @param <T> the type of elements added or retreived from database
 * @author M.Allaudin
 */

@SuppressWarnings("unused")
public interface DataSource<T> {

    @NonNull
    List<T> getAll();

    @NonNull
    List<T> getAllSorted(SortOrder sortOrder);

    @NonNull
    List<T> getAllWhere(String column, String value);

    @NonNull
    List<T> get(String whereClause, String... values);

    @NonNull
    List<T> getByLimit(int start, int end, SortOrder sortOrder, String sortColumn);

    T getById(int id);

    @NonNull
    List<T> getAllById(int id);

    @NonNull
    List<T> getAllByColumns(String[] columns, String... values);

    @NonNull
    List<T> getAllByColumnsUnique(@NonNull String groupBy, @NonNull String[] columns, String... values);

    void insertOrUpdate(T model);

    void insertOrUpdate(List<T> models);

    void insertOrUpdateAsync(List<T> models);

    void updateColumn(String column, String value, int rowId);

    void updateWhere(ContentValues content, String[] columns, String... values);

    void delete(int id);

    void deleteRaw(int... id);

    void deleteWhere(String column, String value);

    void deleteAll();

} // DataSource
