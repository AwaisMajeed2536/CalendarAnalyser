package com.example.bisma.calendar_analyzer.models;

/**
 * Created by Devprovider on 28/08/2017.
 */

public class DBReportModel {
    int reportId;
    String userId, reportUrl, dateTime, userName;

    public DBReportModel() {
    }

    public DBReportModel(int reportId, String userId, String reportUrl, String dateTime, String userName) {
        this.reportId = reportId;
        this.userId = userId;
        this.reportUrl = reportUrl;
        this.dateTime = dateTime;
        this.userName = userName;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReportUrl() {
        return reportUrl;
    }

    public void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
