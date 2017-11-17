package com.example.bisma.calendar_analyzer.db.core;

/**
 * Created on 2016-12-03 14:01.
 *
 * @author M.Allaudin
 */

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>A helper class to generate table creation queries for sqlite database.It contains
 * methods to add fields in the table with respective schemas.</p>
 * <p>
 * <p class="note"><strong>Note:</strong> This is a singleton class and only one object will be created for all create queries. </p>
 * <p>
 * Created on 2016-09-11 17:44.
 *
 * @author M.Allaudin
 */
@SuppressWarnings("ALL")
public final class QueryGenerator {

    /**
     * Stores single instance of this class. It will be returned
     * everytime getInstance is invoked.
     */
    private static QueryGenerator mInstance;
    /**
     * Stores table columns with their sqlite types and constraints.
     */
    private Map<String, String> mFieldsMap;

    /**
     * Restrict access to the constructor outside of this class and
     * enforce singleton property.
     */
    private QueryGenerator() {
        mFieldsMap = new HashMap<>();
    } // QueryGenerator

    /**
     * Returns a new instance of query generator if this is
     * method is called for the first time, otherwise a
     * cached instance will be returned.
     *
     * @return QueryGenerator instance of query generator
     */
    public static QueryGenerator getInstance() {
        if (mInstance == null) {
            mInstance = new QueryGenerator();
        }
        return mInstance;
    } // getInstance

    /**
     * Store filed and it's type in field map.
     *
     * @param fieldName name of the field to be added in create query
     * @param fieldType type of the field to be added in crate query
     * @return this  object
     */
    @NonNull
    private QueryGenerator addField(String fieldName, @NonNull SQLiteTypes fieldType) {
        mFieldsMap.put(fieldName, fieldType.toString());
        return this;
    } // addField

    /**
     * Store filed, type and its contstraint in field map
     *
     * @param fieldName   name of the field to be added in create query
     * @param fieldType   type of the field to be added in crate query
     * @param constraints type of constraint to be applied on field
     * @return this object
     */
    @NonNull
    private QueryGenerator addFieldWithConstraint(String fieldName, @NonNull SQLiteTypes fieldType, SQLiteConstraints... constraints) {
        String constr = String.format("%s %s", fieldType.toString(), TextUtils.join(" ", constraints));
        mFieldsMap.put(fieldName, constr);
        return this;
    } // addFieldWithConstraint

    /**
     * Delegates to {@link QueryGenerator#addFieldWithConstraint} with proper sql type.
     */
    @NonNull
    public QueryGenerator addIntegerWithConstraints(String fieldName, SQLiteConstraints... sqLiteConstraints) {
        return addFieldWithConstraint(fieldName, SQLiteTypes.INTEGER, sqLiteConstraints);
    }

    /**
     * Delegates to {@link QueryGenerator#addFieldWithConstraint} with proper sql type.
     */
    @NonNull
    public QueryGenerator addIntegerField(String fieldName) {
        return addField(fieldName, SQLiteTypes.INTEGER);
    } // addIntegerField

    /**
     * Delegates to {@link QueryGenerator#addFieldWithConstraint} with proper sql type and constraint.
     */
    @NonNull
    public QueryGenerator addUniqueIntegerField(String fieldName) {
        return addFieldWithConstraint(fieldName, SQLiteTypes.INTEGER, SQLiteConstraints.UNIQUE);
    } // addUniqueIntegerField

    /**
     * Delegates to {@link QueryGenerator#addFieldWithConstraint} with proper sql type and constraint.
     */
    @NonNull
    public QueryGenerator addNonNullIntegerField(String fieldName) {
        return addFieldWithConstraint(fieldName, SQLiteTypes.INTEGER, SQLiteConstraints.NOT_NULL);
    } // addNonNullIntegerField

    /**
     * Delegates to {@link QueryGenerator#addFieldWithConstraint} with proper sql type and constraint.
     */
    @NonNull
    public QueryGenerator addUniqueNonNullIntegerField(String fieldName) {
        return addFieldWithConstraint(fieldName, SQLiteTypes.INTEGER, SQLiteConstraints.UNIQUE, SQLiteConstraints.NOT_NULL);
    } // addUniqueNonNullIntegerField


    /**
     * Delegates to {@link QueryGenerator#addField} with proper sql type.
     */
    @NonNull
    public QueryGenerator addTextField(String fieldName) {
        return addField(fieldName, SQLiteTypes.TEXT);
    } // addTextField

    /**
     * Adds array of filed with the same types in field map.
     * Under the hood, {@link QueryGenerator#addField} is called to add each
     * field in field map.
     */
    @NonNull
    public QueryGenerator addTextFields(@NonNull String... fieldNames) {
        for (String fieldName : fieldNames) {
            addField(fieldName, SQLiteTypes.TEXT);
        }
        return this;
    } // addTextField

    /**
     * Adds array of filed with the same types in field map.
     * Under the hood, {@link QueryGenerator#addField} is called to add each
     * field in field map.
     */
    @NonNull
    public QueryGenerator addIntegerFields(@NonNull String... fieldNames) {
        for (String fieldName : fieldNames) {
            addField(fieldName, SQLiteTypes.INTEGER);
        }
        return this;
    } // addIntegerFields

    /**
     * Delegates to {@link QueryGenerator#addField} with proper sql type.
     */
    @NonNull
    public QueryGenerator addTextUniqueField(String fieldName) {
        return addFieldWithConstraint(fieldName, SQLiteTypes.TEXT, SQLiteConstraints.UNIQUE);
    } // addTextUniqueField


    /**
     * Delegates to {@link QueryGenerator#addFieldWithConstraint} with proper sql type.
     */
    @NonNull
    public QueryGenerator addNonNullTextField(String fieldName) {
        return addFieldWithConstraint(fieldName, SQLiteTypes.TEXT, SQLiteConstraints.NOT_NULL);
    } // addNonNullTextField

    /**
     * Delegates to {@link QueryGenerator#addFieldWithConstraint} with proper sql type.
     */
    @NonNull
    public QueryGenerator addUniqueNonNullTextField(String fieldName) {
        return addFieldWithConstraint(fieldName, SQLiteTypes.TEXT, SQLiteConstraints.UNIQUE, SQLiteConstraints.NOT_NULL);
    } // addUniqueNonNullTextField

    /**
     * Delegates to {@link QueryGenerator#addField} with proper sql type.
     */
    @NonNull
    public QueryGenerator addBlobField(String fieldName) {
        return addField(fieldName, SQLiteTypes.BLOB);
    } // addBlobField

    /**
     * Delegates to {@link QueryGenerator#addFieldWithConstraint} with proper sql type.
     */
    @NonNull
    public QueryGenerator addBlobFieldWithConstraints(String fieldName, SQLiteConstraints... sqLiteConstraintses) {
        return addFieldWithConstraint(fieldName, SQLiteTypes.BLOB, sqLiteConstraintses);
    } // addBlobFieldWithConstraints

    /**
     * Delegates to {@link QueryGenerator#addField} with proper sql type.
     */
    @NonNull
    public QueryGenerator addRealField(String fieldName) {
        return addField(fieldName, SQLiteTypes.REAL);
    } // addRealField


    /**
     * Delegates to {@link QueryGenerator#addFieldWithConstraint} with proper sql type.
     */
    @NonNull
    public QueryGenerator addRealFieldWithConstraints(String fieldName, SQLiteConstraints... sqLiteConstraintses) {
        return addFieldWithConstraint(fieldName, SQLiteTypes.REAL, sqLiteConstraintses);
    } // addRealFieldWithConstraints

    /**
     * Delegates to {@link QueryGenerator#addField} with proper sql type.
     */
    @NonNull
    public QueryGenerator addIntegerPrimaryKeyAutoIncrement(String keyName) {
        return addFieldWithConstraint(keyName, SQLiteTypes.INTEGER, SQLiteConstraints.PRIMARY_KEY,
                SQLiteConstraints.AUTOINCREMENT);
    } // addIntegerPrimaryKey

    /**
     * Delegates to {@link QueryGenerator#addField} with proper sql type.
     */
    @NonNull
    public QueryGenerator addIntegerPrimaryKey(String keyName) {
        return addFieldWithConstraint(keyName, SQLiteTypes.INTEGER, SQLiteConstraints.PRIMARY_KEY);
    } // addIntegerPrimaryKey

    /**
     * Delegates to {@link QueryGenerator#addField} with proper sql type.
     */
    @NonNull
    public QueryGenerator addTextPrimaryKey(String keyName) {
        return addFieldWithConstraint(keyName, SQLiteTypes.TEXT, SQLiteConstraints.PRIMARY_KEY);
    } // addTextPrimaryKey

    /**
     * Builds the query from fields added before calling this methods. This
     * method clears all the fields after generating the query in order to
     * avoid adding the same fields in next generation.
     *
     * @param tableName name of the table for which query is generated.
     * @return create query for table
     */
    public String generate(String tableName) {
        StringBuilder builder = new StringBuilder(200);
        builder.append("CREATE TABLE IF NOT EXISTS ").append(tableName).append(" ( ");
        for (String key : mFieldsMap.keySet()) {
            builder.append(key).append(" ")
                    .append(mFieldsMap.get(key))
                    .append(", ");
        }
        int index = builder.lastIndexOf(",");
        builder.deleteCharAt(index).append(")");
        resetGenerator();
        return builder.toString();
    } // generate

    /**
     * Clears the fields map.
     */
    private void resetGenerator() {
        mFieldsMap.clear();
    }

    /**
     * Represents sqlite Data types. Each type
     * represents the valid Data type in SQLite.
     * <p>
     * This enum will be used in conjunction with QueryGenerator class where string
     * representation of given type is used to generate a `create query`
     * for the table.
     */
    private enum SQLiteTypes {

        REAL("REAL"),
        TEXT("TEXT"),
        BLOB("BLOB"),
        INTEGER("INTEGER");

        private String value;

        SQLiteTypes(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }

    } // SQLiteTypes


    /**
     * Represents SQLite constraints. e.g PRIMARY KEY,
     * UNIQUE, NOT NULL etc.
     */
    enum SQLiteConstraints {

        PRIMARY_KEY("PRIMARY KEY"),
        NOT_NULL("NOT NULL"),
        UNIQUE("UNIQUE"),
        AUTOINCREMENT("AUTOINCREMENT");

        private String value;

        SQLiteConstraints(String value) {
            this.value = value;
        } // SQLiteConstraints

        @Override
        public String toString() {
            return value;
        }
    } // SQLiteConstraints

} // QueryGenerator