package com.example.bisma.calendar_analyzer.services;

import android.content.Context;

import com.example.bisma.calendar_analyzer.helpers.UtilHelpers;
import com.example.bisma.calendar_analyzer.interfaces.AllReportsCallback;
import com.example.bisma.calendar_analyzer.models.DBReportModel;
import com.example.bisma.calendar_analyzer.services.core.BaseService;
import com.example.bisma.calendar_analyzer.services.core.Result;
import com.example.bisma.calendar_analyzer.services.core.RetrofitClient;
import com.example.bisma.calendar_analyzer.services.core.UserClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GetAllReportsService implements Callback<String> {

    private AllReportsCallback result;
    private boolean runInBackground;
    private Context context;

    private GetAllReportsService(Context context, boolean runInBackground, AllReportsCallback result) {
        this.result= result;
        this.runInBackground = runInBackground;
        this.context = context;
    }

    public static GetAllReportsService newInstance(Context context, boolean runInBackground, AllReportsCallback result){
        return new GetAllReportsService(context, runInBackground, result);
    }

    public void callService(){
        UtilHelpers.showWaitDialog(context, "Getting Reports", "please wait...");
        RetrofitClient.getRetrofit().create(UserClient.class).getAllReports().enqueue(this);
    }

    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        UtilHelpers.dismissWaitDialog();
        List<DBReportModel> returner = new ArrayList<>();
        try {
            JSONArray res = new JSONArray(response.body());
            for(int i=0; i<res.length(); i++){
                JSONObject obj = new JSONObject((new JSONArray(response.body())).get(i).toString());
                returner.add(new DBReportModel(obj.getInt("reportId"), obj.getString("userId"),
                        obj.getString("reportUrl"), obj.getString("dateTime"), obj.getString("userName")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        result.onLinksDownloaded(returner);
    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {

    }
}

