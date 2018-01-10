package com.example.bisma.calendar_analyzer.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.bisma.calendar_analyzer.R;
import com.example.bisma.calendar_analyzer.db.source.TasksSource;
import com.example.bisma.calendar_analyzer.fragment.AnalyzerFragment;
import com.example.bisma.calendar_analyzer.fragment.CalendarViewPagerFragment;
import com.example.bisma.calendar_analyzer.fragment.HistoryFragment;
import com.example.bisma.calendar_analyzer.fragment.TodaysTasksFragment;
import com.example.bisma.calendar_analyzer.helpers.BottomNavigationHelper;
import com.example.bisma.calendar_analyzer.helpers.Constants;
import com.example.bisma.calendar_analyzer.helpers.UtilHelpers;
import com.example.bisma.calendar_analyzer.models.EventModelDep;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomNavigationHelper.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivityForResult(new Intent(this, GoogleApi.class), Constants.GOOGLE_API_GET_CALL_KEY);
        BottomNavigationHelper.with(this).setCallBack(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            showLogoutDialog();
        } else if (item.getItemId() == R.id.action_crud) {
            startActivity(new Intent(this, ManipulateTasksActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this).setTitle("Sign out")
                .setMessage("Are you sure you want to sign out?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        UtilHelpers.endLoginSession(MainActivity.this);
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create().show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onSelectItem(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new CalendarViewPagerFragment();
                break;
            case 1:
                fragment = new AnalyzerFragment();
                break;
            case 2:
                fragment = new HistoryFragment();
                break;
            case 3:
                fragment = new TodaysTasksFragment();
                break;
            default:
                fragment = new CalendarViewPagerFragment();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder, fragment).commit();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.GOOGLE_API_GET_CALL_KEY) {
            if (resultCode == Activity.RESULT_OK) {
                ArrayList<EventModelDep> taskList = data.getParcelableArrayListExtra(Constants.GOOGLE_API_RESULT_KEY);
                if (taskList == null || taskList.size() == 0) {
                    UtilHelpers.showAlertDialog(this, "No Events", getString(R.string.no_events_for_this_month));
                } else {
                    TasksSource.newInstance().insertOrUpdate(taskList);
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
        onSelectItem(0);
    }
}
