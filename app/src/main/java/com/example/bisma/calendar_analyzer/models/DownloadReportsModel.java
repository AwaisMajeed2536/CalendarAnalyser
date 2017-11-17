package com.example.bisma.calendar_analyzer.models;

import android.graphics.Bitmap;

/**
 * Created by Devprovider on 28/08/2017.
 */

public class DownloadReportsModel {
    private int reportId;
    private Bitmap reportImage;
    private String userId, dateTime;

    public DownloadReportsModel() {
    }

    public DownloadReportsModel(int reportId, Bitmap reportImage, String userId, String dateTime) {
        this.reportId = reportId;
        this.reportImage = reportImage;
        this.userId = userId;
        this.dateTime = dateTime;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public Bitmap getReportImage() {
        return reportImage;
    }

    public void setReportImage(Bitmap reportImage) {
        this.reportImage = reportImage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
