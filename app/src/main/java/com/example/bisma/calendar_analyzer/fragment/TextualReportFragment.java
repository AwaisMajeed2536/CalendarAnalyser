package com.example.bisma.calendar_analyzer.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bisma.calendar_analyzer.R;
import com.example.bisma.calendar_analyzer.helpers.Constants;
import com.example.bisma.calendar_analyzer.helpers.UtilHelpers;
import com.example.bisma.calendar_analyzer.models.EventModelDep;
import com.example.bisma.calendar_analyzer.services.SaveReportService;
import com.example.bisma.calendar_analyzer.services.core.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Devprovider on 29/08/2017.
 */

public class TextualReportFragment extends Fragment {
    protected TextView resultTv;
    protected View rootView;
    protected TextView startDateTv;
    protected TextView endDateTv;
    protected TextView scheduledTasksTv;
    protected TextView scheduledHoursTv;
    protected TextView unscheduledTasksTv;
    protected TextView unscheduledHoursTv;
    private List<EventModelDep> dataList;
    boolean resultOk;
    View mainView;
    private int scheduledTasksCount, unScheduledTasksCount;
    private double scheduledHoursCount, unScheduledHoursCount;
    private String startDate, endDate;

    public static TextualReportFragment newInstance(ArrayList<EventModelDep> dataList, String startDate, String endDate) {

        Bundle args = new Bundle();
        args.putParcelableArrayList(Constants.TEXTUAL_REPORT_PASS_KEY, dataList);
        args.putString(Constants.START_DATE_PASS_KEY, startDate);
        args.putString(Constants.END_DATE_PASS_KEY, endDate);
        TextualReportFragment fragment = new TextualReportFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_textual_report, container, false);
        initView(rootView);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.pie_chart_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            Bitmap screenShot = takeScreenShot(mainView);
            Intent i = new Intent(Intent.ACTION_SEND);
            String path = saveBitmap(screenShot);
            Uri screenshotUri = Uri.parse(path);
            i.putExtra(Intent.EXTRA_STREAM, screenshotUri);
            i.setType("image/png");
            try {
                startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (ActivityNotFoundException ex) {
                Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        } else if (item.getItemId() == R.id.action_save) {
            Bitmap scrnShot = takeScreenShot(mainView);
            String userId = UtilHelpers.getUserId(getActivity());
            SaveReportService.newInstance(getActivity(), false, result).callService(getActivity(), scrnShot, userId);
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView(View rootView) {
        resultTv = rootView.findViewById(R.id.result_tv);
        mainView = rootView.findViewById(R.id.main_view);
        dataList = getArguments().getParcelableArrayList(Constants.TEXTUAL_REPORT_PASS_KEY);
        startDate = getArguments().getString(Constants.START_DATE_PASS_KEY);
        endDate = getArguments().getString(Constants.END_DATE_PASS_KEY);
        setResultText();
        startDateTv = rootView.findViewById(R.id.start_date_tv);
        endDateTv = rootView.findViewById(R.id.end_date_tv);
        scheduledTasksTv = rootView.findViewById(R.id.scheduled_tasks_tv);
        scheduledHoursTv = rootView.findViewById(R.id.scheduled_hours_tv);
        unscheduledTasksTv = rootView.findViewById(R.id.unscheduled_tasks_tv);
        unscheduledHoursTv = rootView.findViewById(R.id.unscheduled_hours_tv);
    }

    private void setResultText() {
        DecimalFormat df2 = new DecimalFormat(".##");
        analyzeDate();
        if (resultOk) {
            resultTv.setText(Constants.RESULT_OK);
            resultTv.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.holo_green_light));
        } else {
            resultTv.setText(Constants.RESULT_FAIL);
            resultTv.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.holo_red_light));
        }
        startDateTv.setText(startDate);
        endDateTv.setText(endDate);
        scheduledTasksTv.setText(df2.format(scheduledTasksCount));
        scheduledHoursTv.setText(df2.format(unScheduledTasksCount));
        unscheduledTasksTv.setText(df2.format(scheduledHoursCount));
        unscheduledHoursTv.setText(df2.format(unScheduledHoursCount));
    }

    private void analyzeDate() {
        for (EventModelDep obj : dataList) {
            if (obj.isScheduled() == 1)
                scheduledTasksCount++;
            else
                unScheduledTasksCount++;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
                Date sDate = sdf.parse(obj.getStartDate());
                Date eDate = sdf.parse(obj.getEndDate());
                long difference = eDate.getTime() - sDate.getTime();
                scheduledHoursCount += difference / (1000.0d * 60.0d * 60.0d);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        scheduledHoursCount = (8.0d * dataList.size()) - scheduledHoursCount;
        resultOk = scheduledHoursCount - scheduledHoursCount > ((dataList.size() * 8) / 2);
    }


    private String saveBitmap(Bitmap pictureBitmap) {
        String path = Environment.getExternalStorageDirectory().toString();
        OutputStream fOut;
        File file = new File(path, "result.jpg"); // the File to save , append increasing numeric counter to prevent files from getting overwritten.

        try {
            fOut = new FileOutputStream(file);
            pictureBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
            fOut.flush(); // Not really required
            fOut.close(); // do not forget to close the stream

            return MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public Bitmap takeScreenShot(View view) {
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        view.buildDrawingCache();

        if (view.getDrawingCache() == null) return null;

        Bitmap snapshot = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();

        return snapshot;
    }

    Result<String> result = new Result<String>() {
        @Override
        public void onSuccess(String data, int requestId) {
            try {
                JSONObject res = new JSONObject(data);
                if (res.getString("status").equalsIgnoreCase("successful")) {
                    Toast.makeText(getActivity(), "Report Saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Failed to save report", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(String message, int requestId) {
            Toast.makeText(getActivity(), "Failed to save report", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(Throwable throwable, int requestId) {
            Toast.makeText(getActivity(), "Failed to save report", Toast.LENGTH_SHORT).show();
        }
    };

}
