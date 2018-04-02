package com.example.bisma.calendar_analyzer.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bisma.calendar_analyzer.R;
import com.example.bisma.calendar_analyzer.interfaces.TaskEditCallback;
import com.example.bisma.calendar_analyzer.adapters.TasksAdapter;
import com.example.bisma.calendar_analyzer.helpers.Constants;
import com.example.bisma.calendar_analyzer.models.EventModelDep;

import java.util.ArrayList;

import static com.example.bisma.calendar_analyzer.helpers.Constants.GOOGLE_API_CALL_TYPE_KEY;
import static com.example.bisma.calendar_analyzer.helpers.Constants.GOOGLE_API_DELETE_CALL_KEY;
import static com.example.bisma.calendar_analyzer.helpers.Constants.GOOGLE_API_EVENT_ID_PASS_KEY;

/**
 * Created by Devprovider on 16/08/2017.
 */

public class ManipulateTasksActivity extends AppCompatActivity implements TaskEditCallback, TextWatcher {

    protected ListView tasksLv;
    private TasksAdapter adapter;
    private EditText searchEt;
    private ArrayList<EventModelDep> dataList = new ArrayList<>();
    private int eventId = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_manipulate_tasks);
        initView();
    }

    private void initView() {
        tasksLv = findViewById(R.id.tasks_lv);
        searchEt = findViewById(R.id.search_et);
        searchEt.addTextChangedListener(this);
        Intent intent = new Intent(this, GoogleApi.class);
        intent.putExtra(GOOGLE_API_CALL_TYPE_KEY, 0);
        startActivityForResult(intent, Constants.GOOGLE_API_GET_CALL_KEY);
    }

    @Override
    public void onTaskEditClicked(final EventModelDep item) {
        new AlertDialog.Builder(this).setTitle("Choose Operation")
                .setMessage("what do you want to do")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showDeleteDialog(item);
                    }
                }).setNegativeButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(ManipulateTasksActivity.this, CreateEventActivity.class);
                intent.putExtra(Constants.EVENT_UPDATE_PASS_KEY, item);
                startActivity(intent);
            }
        }).create().show();

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        adapter.getFilter().filter(s);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void deleteEvent() {
        Intent intent = new Intent(this, GoogleApi.class);
        intent.putExtra(GOOGLE_API_CALL_TYPE_KEY, 1);
        intent.putExtra(GOOGLE_API_EVENT_ID_PASS_KEY, eventId);
        startActivityForResult(intent, GOOGLE_API_DELETE_CALL_KEY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.GOOGLE_API_GET_CALL_KEY) {
            if (resultCode == Activity.RESULT_OK) {
                dataList = data.getParcelableArrayListExtra(Constants.GOOGLE_API_RESULT_KEY);
                if (dataList == null || dataList.size() == 0) {
                    new AlertDialog.Builder(this).setTitle("No Tasks").setMessage("sorry, you have no tasks...")
                            .setPositiveButton("Go Back", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            }).create().show();
                } else {
                    adapter = new TasksAdapter(this, dataList, this);
                    tasksLv.setAdapter(adapter);
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {

            }
        } else if (requestCode == GOOGLE_API_DELETE_CALL_KEY) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Event Deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Some Error Occured", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showDeleteDialog(final EventModelDep item) {
        new AlertDialog.Builder(this).setTitle("Delete the following task?")
                .setMessage(item.getEventTitle() + "\n" + "Dated: " + item.getStartDate())
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        dataList.remove(item);
                        adapter = new TasksAdapter(ManipulateTasksActivity.this, dataList, ManipulateTasksActivity.this);
                        tasksLv.setAdapter(adapter);
                        eventId = item.getEventID();
                        deleteEvent();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }
}
