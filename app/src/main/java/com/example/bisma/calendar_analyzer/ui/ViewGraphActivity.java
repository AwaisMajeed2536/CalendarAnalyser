package com.example.bisma.calendar_analyzer.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.bisma.calendar_analyzer.R;
import com.example.bisma.calendar_analyzer.fragment.BarChartFragment;
import com.example.bisma.calendar_analyzer.fragment.PieChartFragment;
import com.example.bisma.calendar_analyzer.fragment.ScheduleBarChartFragment;
import com.example.bisma.calendar_analyzer.fragment.TextualReportFragment;
import com.example.bisma.calendar_analyzer.helpers.Constants;
import com.example.bisma.calendar_analyzer.models.EventModelDep;
import com.example.bisma.calendar_analyzer.models.PieDataModel;

import java.util.ArrayList;


public class ViewGraphActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private ArrayList<PieDataModel> pieData = new ArrayList<>();
    private ArrayList<EventModelDep> reportData = new ArrayList<>();
    private String passedStartDate;
    private String passedEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_graph);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        navigation.setSelected(true);
        getDataFromIntents();
        addFragment(PieChartFragment.newInstance(pieData));
    }

    private void getDataFromIntents() {
        pieData = getIntent().getParcelableArrayListExtra(Constants.PIE_DATA_KEY);
        reportData = getIntent().getParcelableArrayListExtra(Constants.REPORT_DATA_KEY);
        passedStartDate = getIntent().getStringExtra(Constants.START_DATE_PASS_KEY);
        passedEndDate = getIntent().getStringExtra(Constants.END_DATE_PASS_KEY);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;
        switch (item.getItemId()) {
            case R.id.pie_chart:
                selectedFragment = PieChartFragment.newInstance(pieData);
                break;
            case R.id.column_chart:
                selectedFragment = BarChartFragment.newInstance(reportData);
                break;
            case R.id.textual_report:
                selectedFragment = TextualReportFragment.newInstance(reportData);
                break;
            case R.id.bar_graph:
                selectedFragment = ScheduleBarChartFragment.newInstance(passedStartDate, passedEndDate);
                break;
        }
        if (selectedFragment == null)
            return true;
        addFragment(selectedFragment);
        return true;
    }

    private void addFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.commit();
    }
}
