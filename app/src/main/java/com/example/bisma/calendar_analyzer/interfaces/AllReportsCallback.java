package com.example.bisma.calendar_analyzer.interfaces;

import com.example.bisma.calendar_analyzer.models.DBReportModel;

import java.util.List;

/**
 * Created by Devprovider on 28/08/2017.
 */

public interface AllReportsCallback {
    void onLinksDownloaded(List<DBReportModel> reports);
}
