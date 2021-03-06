package com.example.bisma.calendar_analyzer.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bisma.calendar_analyzer.R;
import com.example.bisma.calendar_analyzer.helpers.Constants;
import com.example.bisma.calendar_analyzer.helpers.UtilHelpers;
import com.example.bisma.calendar_analyzer.models.EventModelDep;
import com.example.bisma.calendar_analyzer.services.SaveReportService;
import com.example.bisma.calendar_analyzer.services.core.Result;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Devprovider on 29/08/2017.
 */

public class ColumnChartFragment extends Fragment {
    protected BarChart barChart;
    protected ListView titlesLv;
    private List<EventModelDep> dataList = new ArrayList<>();
    View mainView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public static ColumnChartFragment newInstance(ArrayList<EventModelDep> dataList) {

        Bundle args = new Bundle();
        args.putParcelableArrayList(Constants.COLUMN_CHART_DATA_PASS_KEY, dataList);
        ColumnChartFragment fragment = new ColumnChartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_column_chart, container, false);
        initView(view);
        return view;
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

    private void initView(View rootView) {
        barChart = rootView.findViewById(R.id.bar_chart);
        titlesLv = rootView.findViewById(R.id.titles_lv);
        mainView = rootView.findViewById(R.id.main_view);
        dataList = getArguments().getParcelableArrayList(Constants.COLUMN_CHART_DATA_PASS_KEY);
        BarData barData = new BarData(analyzeData());
        barData.setBarWidth(0.9f); // set custom bar width
        barChart.setData(barData);
        barChart.setFitBars(true); // make the x-axis fit exactly all bars
        barChart.invalidate(); // refresh
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_list_item,
                getTaskTitles());
        titlesLv.setAdapter(adapter);
    }

    private BarDataSet analyzeData() {
        List<BarEntry> entries = new ArrayList<>();
        float i = 1f;
        for (EventModelDep obj : dataList) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
                Date sDate = sdf.parse(obj.getStartDate());
                Date eDate = sdf.parse(obj.getEndDate());
                long difference = eDate.getTime() - sDate.getTime();
                long result = TimeUnit.MILLISECONDS.toMinutes(difference);
                entries.add(new BarEntry(i++, result));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        BarDataSet dataSet = new BarDataSet(entries, "Tasks");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        return dataSet;
    }

    private ArrayList<String> getTaskTitles() {
        ArrayList<String> returner = new ArrayList<>();
        int i = 1;
        for (EventModelDep obj : dataList) {
            returner.add(i++ + ". " +obj.getEventTitle());
        }
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
