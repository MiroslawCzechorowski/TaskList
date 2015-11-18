package com.example.czechorowski.tasklist.db;

import android.provider.BaseColumns;

/**
 * Created by MiroslawCzechorowski on 18.11.2015.
 */
public class Database {
    public static final String DB_NAME="com.example.czechoroski.tasklist.db.tasks";
    public static final int DB_VERSION=1;
    public static final String TABLE="tasks";

    public class Columns{
        public static final String IS_DONE="IS_DONE";
        public static final String TASK="TASK";
        public static final String _ID= BaseColumns._ID;
    }
}
