package com.example.bisma.calendar_analyzer.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.example.bisma.calendar_analyzer.interfaces.DownloadCallback;
import com.example.bisma.calendar_analyzer.models.DBReportModel;
import com.example.bisma.calendar_analyzer.models.DownloadReportsModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Devprovider on 24/08/2017.
 */

public class DownloadImagesAsynctask extends AsyncTask<Void, Void, List<DownloadReportsModel>> {

    private Context context;
    private List<DBReportModel> imageLinksList;
    private DownloadCallback callback;

    public DownloadImagesAsynctask(Context context, List<DBReportModel> imageLinksList, DownloadCallback callback) {
        this.context = context;
        this.imageLinksList = imageLinksList;
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        UtilHelpers.showWaitDialog(context, "Preparing Reports Data", "please wait");
    }

    @Override
    protected List<DownloadReportsModel> doInBackground(Void... params) {
        List<DownloadReportsModel> returner = new ArrayList<>();
        for (DBReportModel obj : imageLinksList) {
            File file = new File(context.getCacheDir(), String.valueOf(new Date().getTime()));
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            BitmapHelper.downloadFile(obj.getReportUrl(), file);
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            returner.add(new DownloadReportsModel(obj.getReportId(), bitmap, obj.getUserId(), obj.getDateTime()));

        }
        return returner;
    }

    @Override
    protected void onPostExecute(List<DownloadReportsModel> bitmaps) {
        super.onPostExecute(bitmaps);
        UtilHelpers.dismissWaitDialog();
        callback.onDownloadComplete(bitmaps);
    }
}
