package com.example.bisma.calendar_analyzer.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.bisma.calendar_analyzer.R;
import com.example.bisma.calendar_analyzer.helpers.Constants;
import com.example.bisma.calendar_analyzer.helpers.DownloadImagesAsynctask;
import com.example.bisma.calendar_analyzer.helpers.UtilHelpers;
import com.example.bisma.calendar_analyzer.interfaces.AllReportsCallback;
import com.example.bisma.calendar_analyzer.interfaces.DownloadCallback;
import com.example.bisma.calendar_analyzer.models.DBReportModel;
import com.example.bisma.calendar_analyzer.models.DownloadReportsModel;
import com.example.bisma.calendar_analyzer.services.GetAllReportsService;

import java.util.ArrayList;
import java.util.List;

import okhttp3.internal.Util;

public class ReportsHistoryActivity extends AppCompatActivity implements AllReportsCallback, DownloadCallback, AdapterView.OnItemClickListener {

    protected ListView reportsLv;
    protected ImageView imageHolder;
    private ArrayAdapter<String> adapter;
    private List<DownloadReportsModel> dataList = new ArrayList<>();
    private boolean imageDisplayed = false;
    private int reportType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_reports_history);
        initView();
    }

    private void initView() {
        reportsLv = findViewById(R.id.reports_lv);
        reportType = getIntent().getIntExtra(Constants.REPORT_TYPE_PASS_KEY, 0);
        GetAllReportsService.newInstance(this, false, this).callService();
        imageHolder = findViewById(R.id.image_holder);
    }

    @Override
    public void onLinksDownloaded(List<DBReportModel> reports) {
        if (reportType == 1) {
            new DownloadImagesAsynctask(this, filterList(reports), this).execute();
        } else {
            new DownloadImagesAsynctask(this, reports, this).execute();
        }

    }

    @Override
    public void onDownloadComplete(List<DownloadReportsModel> images) {
        dataList = images;
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getDates(images));
        reportsLv.setAdapter(adapter);
        reportsLv.setOnItemClickListener(this);
    }

    private ArrayList<String> getDates(List<DownloadReportsModel> images) {
        ArrayList<String> returner = new ArrayList<>();
        for (DownloadReportsModel obj : images) {
            returner.add(obj.getDateTime());
        }
        return returner;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DownloadReportsModel clickedObj = dataList.get(position);
        imageHolder.setVisibility(View.VISIBLE);
        reportsLv.setVisibility(View.INVISIBLE);
        imageHolder.setImageBitmap(clickedObj.getReportImage());
        imageDisplayed = true;
    }

    @Override
    public void onBackPressed() {
        if (imageDisplayed) {
            imageHolder.setVisibility(View.GONE);
            reportsLv.setVisibility(View.VISIBLE);
            imageDisplayed = false;
        } else {
            super.onBackPressed();
        }
    }

    private List<DBReportModel> filterList(List<DBReportModel> list) {
        List<DBReportModel> toReturn = new ArrayList<>();
        String userID = UtilHelpers.getUserId(this);
        for (DBReportModel obj : list) {
            if (obj.getUserId().equalsIgnoreCase(userID)) {
                toReturn.add(obj);
            }
        }
        return toReturn;
    }
}
