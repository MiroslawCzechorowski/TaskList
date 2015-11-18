package com.example.czechorowski.tasklist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by MiroslawCzechorowski on 18.11.2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    public DatabaseHelper(Context context) {
        super(context, Database.DB_NAME, null, Database.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS "+Database.TABLE);
        String sqlQuery=String.format("CREATE TABLE %s (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "%s TEXT, %s TEXT)", Database.TABLE, Database.Columns.TASK,Database.Columns.IS_DONE);
        db.execSQL(sqlQuery);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+Database.TABLE);
        onCreate(db);
    }
}
