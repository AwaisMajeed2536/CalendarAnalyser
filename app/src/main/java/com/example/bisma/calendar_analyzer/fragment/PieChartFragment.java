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
import android.widget.Toast;

import com.example.bisma.calendar_analyzer.R;
import com.example.bisma.calendar_analyzer.helpers.Constants;
import com.example.bisma.calendar_analyzer.helpers.UtilHelpers;
import com.example.bisma.calendar_analyzer.models.PieDataModel;
import com.example.bisma.calendar_analyzer.services.SaveReportService;
import com.example.bisma.calendar_analyzer.services.core.Result;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


//import android.text.Layout;

public class PieChartFragment extends Fragment {

    protected PieChart mChart;
    View mainView;
    private List<PieDataModel> recievedData = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public static PieChartFragment newInstance(ArrayList<PieDataModel> reportData) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(Constants.PIE_DATA_KEY, reportData);
        PieChartFragment fragment = new PieChartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.pie_chart, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        mChart = rootView.findViewById(R.id.pie_graphview);
        mainView = rootView.findViewById(R.id.pie_chart);
        recievedData = getArguments().getParcelableArrayList(Constants.PIE_DATA_KEY);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PieDataSet dataSet = retrievePieData(recievedData);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);
        mChart.setData(data);
        Legend legend = mChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        mChart.invalidate();
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


    private PieDataSet retrievePieData(List<PieDataModel> recievedData) {
        List<PieEntry> entries = new ArrayList<>();
        for (PieDataModel obj : recievedData) {
            entries.add(new PieEntry(obj.getOccurences(), obj.getItem()));
        }
        return new PieDataSet(entries, "Evaluation Results");
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

        }

        @Override
        public void onError(Throwable throwable, int requestId) {
            Toast.makeText(getActivity(), "Report Saved", Toast.LENGTH_SHORT).show();
        }
    };
}
