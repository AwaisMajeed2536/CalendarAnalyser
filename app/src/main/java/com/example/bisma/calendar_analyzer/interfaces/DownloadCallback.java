package com.example.bisma.calendar_analyzer.interfaces;

import android.graphics.Bitmap;

import com.example.bisma.calendar_analyzer.models.DownloadReportsModel;

import java.util.List;

/**
 * Created by Devprovider on 24/08/2017.
 */

public interface DownloadCallback {
    void onDownloadComplete(List<DownloadReportsModel> images);
}
