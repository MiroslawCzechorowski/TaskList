package com.example.czechorowski.tasklist;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.example.czechorowski.tasklist.db.Database;
import com.example.czechorowski.tasklist.db.DatabaseHelper;

import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer{

    //User interface
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
        helper=new DatabaseHelper(MainActivity.this);
        helper.addObserver(this);
        update(helper,this);
    }

    //Add new task to database
    public void addTask(View view) {
        String addNewTask = newTask.getText().toString().trim();
        if (addNewTask.length() < 3) {
            Toast.makeText(getApplicationContext(), "Your task name is empty", Toast.LENGTH_LONG).show();
            newTask.setText("");
        } else {
            ContentValues values = new ContentValues();
            values.clear();
            values.put(Database.Columns.TASK, addNewTask);
            values.put(Database.Columns.IS_DONE, "UNDONE");
            helper.addTask(values);
            update(helper,this);
            newTask.setText("");
        }
    }
    // deleting selected task from database
    public void deleteTask(View view) {
        View v=(View) view.getParent();
        task=(TextView)v.findViewById(R.id.taskViewText);
        final String selected_Task = task.getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder
                .setMessage("Are you sure?")
                .setPositiveButton("Yes",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        helper.deleteTask(selected_Task);
                        update(helper,this);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }
    //Change selected task status in database
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
        helper.changeTaskStatus(selectedTask,isDone);
        update(helper,this);
    }
    //Update data when observer send notifyObservers();
    @Override
    public void update(Observable observable, Object data) {
        Cursor cursor = helper.updateData();
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
}
