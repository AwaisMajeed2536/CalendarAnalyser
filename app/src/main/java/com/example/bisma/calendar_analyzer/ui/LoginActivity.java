package com.example.bisma.calendar_analyzer.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bisma.calendar_analyzer.R;
import com.example.bisma.calendar_analyzer.helpers.UtilHelpers;
import com.example.bisma.calendar_analyzer.services.LoginService;
import com.example.bisma.calendar_analyzer.services.core.Result;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button loginBtn;
    private EditText emailET, passwordET;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_NETWORK_STATE}, 1);
        }
        initView();
        if (UtilHelpers.isUserLoggedIn(this)) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }

    @Override
    public void onClick(View view) {
        String userId = emailET.getText().toString();
        int userType = -1;
        if (userId.toLowerCase().startsWith("s")) {
            userType = 0;
        } else if (userId.toLowerCase().startsWith("f")) {
            userType = 1;
        } else if (userId.toLowerCase().startsWith("h")) {
            userType = 2;
        }
        LoginService.newInstance(this, false, result).callService(userId, passwordET.getText().toString(), userType);

    }

    private void initView() {
        progressDialog = new ProgressDialog(this);
        emailET = (EditText) findViewById(R.id.email);
        passwordET = (EditText) findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(this);
    }

    Result<String> result = new Result<String>() {
        @Override
        public void onSuccess(String data, int requestId) {
            try {
                JSONObject response = new JSONObject(data);
                if (response.getString("foundResult").equals("true")) {
                    UtilHelpers.createLoginSession(LoginActivity.this, response.getString("userId"),
                            response.getInt("userType"));
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
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

        }
    };
}