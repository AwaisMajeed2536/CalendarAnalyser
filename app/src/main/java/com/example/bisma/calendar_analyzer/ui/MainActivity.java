package com.example.bisma.calendar_analyzer.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import android.widget.EditText;
import android.widget.TextView;

import com.example.bisma.calendar_analyzer.R;
import com.example.bisma.calendar_analyzer.adapters.TodaysTasksAdapter;
import com.example.bisma.calendar_analyzer.db.source.TasksSource;
import com.example.bisma.calendar_analyzer.helpers.BottomNavigationHelper;
import com.example.bisma.calendar_analyzer.helpers.Constants;
import com.example.bisma.calendar_analyzer.helpers.UtilHelpers;
import com.example.bisma.calendar_analyzer.models.EventModelDep;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements BottomNavigationHelper.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationHelper.with(this).setCallBack(this);
        startActivityForResult(new Intent(this, GoogleApi.class), Constants.GOOGLE_API_GET_CALL_KEY);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_logout:
                UtilHelpers.endLoginSession(this);
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.action_today_tasks:
                startActivity(new Intent(this, TodaysTasksActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
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
                fragment =  new CalendarView();
                break;
            case 1:
                fragment =  new Analyzer();
                break;
            case 2:
                fragment =  new History();
                break;
            case 3:
                fragment =  new History();
                break;
            default:
                fragment =  new CalendarView();
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
    }
}
