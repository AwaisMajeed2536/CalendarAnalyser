package com.example.bisma.calendar_analyzer.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bisma.calendar_analyzer.R;
import com.example.bisma.calendar_analyzer.db.source.TasksSource;
import com.example.bisma.calendar_analyzer.helpers.Constants;
import com.example.bisma.calendar_analyzer.helpers.UtilHelpers;
import com.example.bisma.calendar_analyzer.models.EventModelDep;
import com.example.bisma.calendar_analyzer.services.SaveReportService;
import com.example.bisma.calendar_analyzer.services.core.Result;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ScheduleBarChartFragment extends Fragment {

    public static final int[] COLORS = {
            Color.rgb(193, 37, 82), Color.rgb(255, 102, 0)};
    private View mainView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public static ScheduleBarChartFragment newInstance(String startDate, String endDate) {

        Bundle args = new Bundle();
        args.putString(Constants.START_DATE_PASS_KEY, startDate);
        args.putString(Constants.END_DATE_PASS_KEY, endDate);
        ScheduleBarChartFragment fragment = new ScheduleBarChartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_schedule_bar_chart, container, false);
        mainView = rootView.findViewById(R.id.main_view);
        createGraph(rootView);
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
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        } else if (item.getItemId() == R.id.action_save) {
            Bitmap scrnShot = takeScreenShot(mainView);
            String userId = UtilHelpers.getUserId(getActivity());
            SaveReportService.newInstance(getActivity(), false, result).callService(getActivity(), scrnShot, userId);
        }
        return super.onOptionsItemSelected(item);
    }

    void createGraph(View view) {
        List<EventModelDep> data = new ArrayList<>();
        if (getActivity().getIntent().getExtras() == null) {
            data = TasksSource.newInstance().getTodayEvents();
        } else {
            Intent intent = getActivity().getIntent();
            data = TasksSource.newInstance().getInRange(intent.getStringExtra(Constants.START_DATE_PASS_KEY),
                    intent.getStringExtra(Constants.END_DATE_PASS_KEY));
        }
        BarChart barChart = view.findViewById(R.id.bar_chart);
        List<BarEntry> entries = new ArrayList<>();
        long scheduledValue = 0l;
        long unScheduledValue = 0l;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isScheduled() == 1) {
                scheduledValue++;
            } else {
                unScheduledValue++;
            }
        }
        entries.add(new BarEntry(1f, scheduledValue));
        entries.add(new BarEntry(2f, unScheduledValue));
        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setColors(COLORS);
        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.9f); // set custom bar width
        barChart.setData(barData);
        barChart.setFitBars(true); // make the x-axis fit exactly all bars
        barChart.invalidate(); // refresh
    }

    private ArrayList<String> getTaskTitles() {
        ArrayList<String> returner = new ArrayList<>();
        returner.add("Scheduled");
        returner.add("UnScheduled");
        return returner;
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
