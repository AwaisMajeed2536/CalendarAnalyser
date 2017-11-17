package com.example.bisma.calendar_analyzer.services;


import android.content.Context;

import com.example.bisma.calendar_analyzer.services.core.BaseService;
import com.example.bisma.calendar_analyzer.services.core.Result;
import com.example.bisma.calendar_analyzer.services.core.RetrofitClient;
import com.example.bisma.calendar_analyzer.services.core.UserClient;

public class LoginService extends BaseService {
    private LoginService(Context context, boolean runInBackground, Result<String> result) {
        super(context, runInBackground, result);
    }

    private LoginService(Context context, boolean runInBackground, int requestId, Result<String> result) {
        super(context, runInBackground, requestId, result);
    }

    public static LoginService newInstance(Context context, boolean runInBackground, int requestId, Result<String> result){
        return new LoginService(context, runInBackground, requestId, result);
    }

    public static LoginService newInstance(Context context, boolean runInBackground, Result<String> result){
        return new LoginService(context, runInBackground, result);
    }

    public void callService(String userId, String password,int userType){
        RetrofitClient.getRetrofit().create(UserClient.class).login(userId, password, userType).enqueue(this);
    }
}