package com.example.czechorowski.tasklist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Observable;

/**
 * Created by MiroslawCzechorowski on 18.11.2015.
 */
public class DatabaseHelper extends Observable {
    private SQLiteDatabase sqlDB2;
    private final SQLiteDatabase sqlDB;
    private final SQLiteOpenHelper helper;
    //Create/Connect/Upgrade database
    public DatabaseHelper(final Context context) {

        this.helper = new SQLiteOpenHelper(context, Database.DB_NAME, null, Database.DB_VERSION) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL("DROP TABLE IF EXISTS " + Database.TABLE);
                String sqlQuery = String.format("CREATE TABLE %s (" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s TEXT, %s TEXT)", Database.TABLE, Database.Columns.TASK, Database.Columns.IS_DONE);
                db.execSQL(sqlQuery);
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                db.execSQL("DROP TABLE IF EXISTS " + Database.TABLE);
                onCreate(db);
            }
        };
        this.sqlDB = this.helper.getWritableDatabase();
    }
    //Add task to database
    public void addTask(ContentValues data){
        this.sqlDB.insertWithOnConflict(Database.TABLE, null, data, SQLiteDatabase.CONFLICT_IGNORE);
        notifyObservers();
    }
    //Delete selected task from database
    public void deleteTask(final String field_parms){
        String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
                Database.TABLE,
                Database.Columns.TASK,
                field_parms);
        sqlDB.execSQL(sql);
        notifyObservers();
    }
    //Download data from database
    public Cursor updateData(){
        this.sqlDB2 = this.helper.getReadableDatabase();
        Cursor cursor=sqlDB2.query(Database.TABLE, new String[]{Database.Columns._ID,Database.Columns.TASK, Database.Columns.IS_DONE},
                null, null, null, null, null);
        notifyObservers();
        return cursor;

    }
    // Change selected task status in database
    public void changeTaskStatus(String selectedTask, String isDone){
        String sql = String.format("UPDATE %s " +
                        "SET %s = '%s'," +
                        " %s = '%s'" +
                        " WHERE %s = '%s';",
                Database.TABLE, Database.Columns.TASK, selectedTask, Database.Columns.IS_DONE,isDone, Database.Columns.TASK, selectedTask);
        sqlDB.execSQL(sql);
        notifyObservers();
    }
}