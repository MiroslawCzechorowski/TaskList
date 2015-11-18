package com.example.czechorowski.tasklist;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.czechorowski.tasklist.db.Database;
import com.example.czechorowski.tasklist.db.DatabaseHelper;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    //UI
    private ListView listView;
    DatabaseHelper helper;
    private TextView task,is_Done;
    private EditText newTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView=(ListView)findViewById(R.id.listView);
        newTask=(EditText)findViewById(R.id.addNewTaskEditText);

        updateData();
    }

    private void updateData() {
        helper = new DatabaseHelper(MainActivity.this);
        SQLiteDatabase sqlDB=helper.getReadableDatabase();

        Cursor cursor=sqlDB.query(Database.TABLE, new String[]{Database.Columns._ID,Database.Columns.TASK, Database.Columns.IS_DONE},
                null, null, null, null, null);
        CustomCursorAdapter listAdapter= new CustomCursorAdapter(
                this, R.layout.task_adapter,cursor,new String[]{Database.Columns.TASK,Database.Columns.IS_DONE},
                new int[]{R.id.taskViewText,R.id.statusViewText},
                0
        );
        listAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                int nCheckedIndex = cursor.getColumnIndexOrThrow(Database.Columns.IS_DONE);
                if (columnIndex == nCheckedIndex) {
                    TextView cb = (TextView) view;
                    String bChecked = cursor.getString(nCheckedIndex);
                    if (bChecked.equalsIgnoreCase("UNDONE")) {
                        cb.setText("UNDONE");
                        cb.setTextColor(Color.RED);
                    } else {
                        cb.setText("DONE");
                        cb.setTextColor(Color.GREEN);
                    }
                    return true;
                }
                return false;
            }
        });

        listView.setAdapter(listAdapter);
    }


    public void addTask(View view) {
        String addNewTask = newTask.getText().toString().trim();
        if (addNewTask.length() < 3) {
            Toast.makeText(getApplicationContext(), "Your task name is empty", Toast.LENGTH_LONG).show();
            newTask.setText("");
        } else {
            helper = new DatabaseHelper(MainActivity.this);
            SQLiteDatabase db = helper.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.clear();
            values.put(Database.Columns.TASK, addNewTask);
            values.put(Database.Columns.IS_DONE, "UNDONE");

            db.insertWithOnConflict(Database.TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            updateData();
            newTask.setText("");
        }
    }

    public void deleteTask(View view) {
        View v=(View) view.getParent();
        task=(TextView)v.findViewById(R.id.taskViewText);
        String selected_Task = task.getText().toString();

        String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
                Database.TABLE,
                Database.Columns.TASK,
                selected_Task);
        helper = new DatabaseHelper(MainActivity.this);
        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        sqlDB.execSQL(sql);
        updateData();
    }

    public void changeTaskStatus(View view){
        View v = (View) view.getParent();
        task=(TextView)v.findViewById(R.id.taskViewText);
        is_Done=(TextView)v.findViewById(R.id.statusViewText);
        String selectedTask=task.getText().toString().trim();
        String taskStatus=is_Done.getText().toString().trim();

        String isDone="";
        if (taskStatus.equalsIgnoreCase("UNDONE")) {
            isDone="DONE";
        }else if (taskStatus.equalsIgnoreCase("DONE")){
            isDone="UNDONE";
        }
        String sql = String.format("UPDATE %s " +
                        "SET %s = '%s'," +
                        " %s = '%s'" +
                        " WHERE %s = '%s';",
                Database.TABLE, Database.Columns.TASK, selectedTask, Database.Columns.IS_DONE,isDone, Database.Columns.TASK, selectedTask);
        helper = new DatabaseHelper(MainActivity.this);
        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        sqlDB.execSQL(sql);
        updateData();
    }
}
