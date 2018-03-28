package com.example.bisma.calendar_analyzer.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bisma.calendar_analyzer.R;
import com.example.bisma.calendar_analyzer.db.source.TasksSource;
import com.example.bisma.calendar_analyzer.helpers.Constants;
import com.example.bisma.calendar_analyzer.helpers.UtilHelpers;
import com.example.bisma.calendar_analyzer.interfaces.DatePickerCallback;
import com.example.bisma.calendar_analyzer.models.EventModelDep;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class CreateEventActivity extends AppCompatActivity implements View.OnClickListener, DatePickerCallback {

    protected String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};
    protected EditText eventTitleEt;
    protected EditText descriptionEt;
    protected TextView startDateTv;
    protected TextView endDateTv;
    protected Button createEventBtn;
    private EventModelDep event;
    private Calendar startDateToPass;
    private Calendar endDateToPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_create_event);
        initView();

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.create_event_btn) {
            List<EventModelDep> events = TasksSource.newInstance().getByDate(UtilHelpers.getDateInFormat(startDateToPass, true));
            for (EventModelDep event : events) {
                if (!timeIsNotColliding(event)) {
                    Toast.makeText(this, "Time is colliding with another task!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            event = new EventModelDep();
            event.setEventID(UtilHelpers.getIdFromDate(UtilHelpers.getDateInFormat(startDateToPass, true)));
            event.setEventTitle(eventTitleEt.getText().toString());
            event.setDescription(descriptionEt.getText().toString());
            event.setStartDate(UtilHelpers.getDateInFormat(startDateToPass, false));
            event.setEndDate(UtilHelpers.getDateInFormat(endDateToPass, false));
            event.setScheduled(0);
            TasksSource.newInstance().insertOrUpdate(event);
            if (getIntent().getExtras() != null) {
                Intent intent = new Intent(this, GoogleApi.class);
                intent.putExtra(Constants.GOOGLE_API_CALL_TYPE_KEY, 3);
                intent.putExtra(Constants.GOOGLE_API_EVENT_MODEL_PASS_KEY, event);
                startActivityForResult(intent, Constants.GOOGLE_API_UPDATE_CALL_KEY);
            }

            Toast.makeText(this, "Event Created", Toast.LENGTH_SHORT).show();
            finish();
        } else if (view.getId() == R.id.startDate_tv) {
            UtilHelpers.showDateTimePicker(this, 0, this);
        } else if (view.getId() == R.id.endDate_tv) {
            UtilHelpers.showDateTimePicker(this, 1, this);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.GOOGLE_API_UPDATE_CALL_KEY) {
            if (resultCode == RESULT_OK && data != null) {
                Toast.makeText(this, "Event Updated", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void initView() {
        eventTitleEt = findViewById(R.id.eventTitle_et);
        descriptionEt = findViewById(R.id.description_et);
        startDateTv = findViewById(R.id.startDate_tv);
        startDateTv.setOnClickListener(this);
        endDateTv = findViewById(R.id.endDate_tv);
        endDateTv.setOnClickListener(this);
        createEventBtn = findViewById(R.id.create_event_btn);
        createEventBtn.setOnClickListener(CreateEventActivity.this);
        loadEditView();
    }

    private void loadEditView() {
        if (getIntent().getExtras() != null) {
            EventModelDep model = getIntent().getParcelableExtra(Constants.EVENT_UPDATE_PASS_KEY);
            eventTitleEt.setText(model.getEventTitle());
            descriptionEt.setText(model.getDescription());
            setStartDate(model.getStartDate());
            setEndDate(model.getEndDate());
        }
    }

    private void setStartDate(String date) {
        startDateTv.setText(date);
    }

    private void setEndDate(String date) {
        endDateTv.setText(date);
    }

    @Override
    public void onDateSelected(int type, Calendar calendar) {
        String date = UtilHelpers.getDateInFormat(calendar, true);
        if (type == 0) {
            startDateToPass = calendar;
            setStartDate(date);
        } else {
            endDateToPass = calendar;
            setEndDate(date);
        }
    }

    private boolean timeIsNotColliding(EventModelDep event) {
        boolean flag = false;
        Calendar oldTaskStartDate = UtilHelpers.getCalendarFromString(event.getStartDate());
        Calendar oldTaskEndDate = UtilHelpers.getCalendarFromString(event.getEndDate());
        if (startDateToPass.before(oldTaskStartDate) && endDateToPass.before(oldTaskStartDate))
            flag = true;
        if (startDateToPass.after(oldTaskStartDate) && endDateToPass.after(oldTaskEndDate))
            flag = true;
        return flag;
    }
}
