package com.example.bisma.calendar_analyzer.services;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.bisma.calendar_analyzer.helpers.UtilHelpers;
import com.example.bisma.calendar_analyzer.services.core.BaseService;
import com.example.bisma.calendar_analyzer.services.core.Result;
import com.example.bisma.calendar_analyzer.services.core.RetrofitClient;
import com.example.bisma.calendar_analyzer.services.core.UserClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;



public class SaveReportService extends BaseService {
    private SaveReportService(Context context, boolean runInBackground, Result<String> result) {
        super(context, runInBackground, result);
    }

    private SaveReportService(Context context, boolean runInBackground, int requestId, Result<String> result) {
        super(context, runInBackground, requestId, result);
    }

    public static SaveReportService newInstance(Context context, boolean runInBackground, int requestId, Result<String> result){
        return new SaveReportService(context, runInBackground, requestId, result);
    }

    public static SaveReportService newInstance(Context context, boolean runInBackground, Result<String> result){
        return new SaveReportService(context, runInBackground, result);
    }

    public void callService(Context context, Bitmap arr, String userId){
        showDialog();
        FileOutputStream fos = null;
        File file = new File(context.getCacheDir(), String.valueOf(new Date().getTime()));
        try {
            file.createNewFile();

            android.graphics.Bitmap bitmap = arr;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, Bitmap.DENSITY_NONE, bos);
            byte[] bitmapdata = bos.toByteArray();

            //write the bytes in file
            fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception ignored) {
                }
            }
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String dateTime = UtilHelpers.getDateInFormat(calendar, false);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        RetrofitClient.getRetrofit().create(UserClient.class).saveReport(filePart, userId, dateTime).enqueue(this);
    }
}

