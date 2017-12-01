package com.example.bisma.calendar_analyzer.ui;


import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import com.example.bisma.calendar_analyzer.R;
import com.example.bisma.calendar_analyzer.helpers.UtilHelpers;
import com.example.bisma.calendar_analyzer.models.EventModelDep;
import com.example.bisma.calendar_analyzer.helpers.Constants;

import static com.example.bisma.calendar_analyzer.helpers.Constants.GOOGLE_API_RESULT_KEY;
import static com.example.bisma.calendar_analyzer.helpers.Constants.GOOGLE_API_ERROR_KEY;

public class GoogleApi extends Activity
        implements EasyPermissions.PermissionCallbacks {

    private GoogleAccountCredential mCredential;
    ArrayList<EventModelDep> dataList = new ArrayList<>();


    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    private DateTime fromDate;
    private DateTime toDate;

    private static final String PREF_ACCOUNT_NAME = "accountName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_google_api);
        if (getIntent().getExtras() != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
                Date dt = sdf.parse(getIntent().getExtras().getString(Constants.GOOGLE_API_STARTDATE_PASS_KEY));
                fromDate = new DateTime(dt.getTime());
                dt = sdf.parse(getIntent().getExtras().getString(Constants.GOOGLE_API_ENDDATE_PASS_KEY));
                toDate = new DateTime(dt.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), CalendarScopes.all())
                .setBackOff(new ExponentialBackOff());
        getResultsFromApi();

    }

    private void getResultsFromApi() {
        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (!isDeviceOnline()) {
            UtilHelpers.showAlertDialog(this, "Error", "No network connection available.");
        } else {
            int callType = getIntent().getIntExtra(Constants.GOOGLE_API_CALL_TYPE_KEY, 0);
            if (callType == 0) {
                new GetEventsAsynctask(mCredential).execute();
            } else if (callType == 1) {
                String eventId = getIntent().getStringExtra(Constants.GOOGLE_API_EVENT_ID_PASS_KEY);
                new DeleteEventAsyntask(mCredential, eventId).execute();
            } else if (callType == 2) {
                EventModelDep event = getIntent().getParcelableExtra(Constants.GOOGLE_API_EVENT_MODEL_PASS_KEY);
                new CreateEventAsyntask(mCredential, event).execute();
            } else if (callType == 3) {
                EventModelDep event = getIntent().getParcelableExtra(Constants.GOOGLE_API_EVENT_MODEL_PASS_KEY);
                new UpdateEventAsyntask(mCredential, event).execute();
            }
        }
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                this, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    UtilHelpers.showAlertDialog(this, "Error",
                            "This app requires Google Play Services. Please install " +
                                    "Google Play Services on your device and relaunch this app.");
                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
    }

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                GoogleApi.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    private class GetEventsAsynctask extends AsyncTask<Void, Void, ArrayList<EventModelDep>> {
        private Calendar mService = null;
        private Exception mLastError = null;

        GetEventsAsynctask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new Calendar.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Google Calendar API")
                    .build();
        }

        @Override
        protected ArrayList<EventModelDep> doInBackground(Void... params) {
            try {
                return getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        private ArrayList<EventModelDep> getDataFromApi() throws IOException {
            ArrayList<EventModelDep> dList = new ArrayList<>();
            Events events;
            if (fromDate != null && toDate != null) {
                events = mService.events().list("primary")
                        .setTimeMin(fromDate)
                        .setTimeMax(toDate)
                        .setOrderBy("startTime")
                        .setSingleEvents(true)
                        .execute();
            } else {
                events = mService.events().list("primary")
                        .setOrderBy("startTime")
                        .setSingleEvents(true)
                        .execute();
            }
            List<Event> eventDataList = events.getItems();
            for (Event event : eventDataList) {
                EventModelDep model = new EventModelDep(UtilHelpers.getIdFromDate(getDateInFormat(event.getStart().toString())),
                        event.getSummary(), "" + event.getDescription(), getDateInFormat(event.getStart().toString()),
                        getDateInFormat(event.getEnd().toString()));
                dList.add(model);
            }
            return dList;
        }

        @Override
        protected void onPreExecute() {
            UtilHelpers.showWaitDialog(GoogleApi.this, "Getting Evets", "please wait...");
        }

        @Override
        protected void onPostExecute(ArrayList<EventModelDep> output) {
            UtilHelpers.dismissWaitDialog();
            Intent intent = new Intent();
            if (output == null || output.size() == 0) {
                intent.putExtra(GOOGLE_API_ERROR_KEY, Constants.NO_EVENTS_FOUND);
                setResult(Activity.RESULT_OK, intent);
            } else {
                intent.putExtra(GOOGLE_API_RESULT_KEY, output);
                setResult(Activity.RESULT_OK, intent);
            }
            finish();
        }

        @Override
        protected void onCancelled() {
            UtilHelpers.dismissWaitDialog();
            Intent intent = new Intent();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            GoogleApi.REQUEST_AUTHORIZATION);
                } else {
                    intent.putExtra(Constants.GOOGLE_API_ERROR_KEY, mLastError.getMessage());
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            } else {
                intent.putExtra(Constants.GOOGLE_API_ERROR_KEY, "Error, Request cancelled.");
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
    }

    private class DeleteEventAsyntask extends AsyncTask<Void, Void, Void> {
        private Calendar mService = null;
        private Exception mLastError = null;
        private String eventId;


        DeleteEventAsyntask(GoogleAccountCredential credential, String eventId) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new Calendar.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Google Calendar API")
                    .build();
            this.eventId = eventId;
        }


        @Override
        protected void onPreExecute() {
            //txt.setText("");
            UtilHelpers.showWaitDialog(GoogleApi.this, "Deleting Event", "please wait...");
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                mService.events().delete("primary", eventId).execute();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            UtilHelpers.dismissWaitDialog();
            Intent intent = new Intent();
            intent.putExtra(Constants.GOOGLE_API_EVENT_DELETE_KEY, "Event Deleted");
            setResult(RESULT_OK, intent);
            finish();
        }

        @Override
        protected void onCancelled() {
            UtilHelpers.dismissWaitDialog();
            Intent intent = new Intent();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            GoogleApi.REQUEST_AUTHORIZATION);
                } else {
                    intent.putExtra(Constants.GOOGLE_API_ERROR_KEY, mLastError.getMessage());
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            } else {
                intent.putExtra(Constants.GOOGLE_API_ERROR_KEY, "Error, Request cancelled.");
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
    }

    private class CreateEventAsyntask extends AsyncTask<Void, Void, Void> {
        private Calendar mService = null;
        private Exception mLastError = null;
        private EventModelDep passedEvent;


        CreateEventAsyntask(GoogleAccountCredential credential, EventModelDep passedEvent) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new Calendar.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Google Calendar API")
                    .build();
            this.passedEvent = passedEvent;
        }


        @Override
        protected void onPreExecute() {
            //txt.setText("");
            UtilHelpers.showWaitDialog(GoogleApi.this, "Creating Event", "please wait...");
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Event event = new Event();
                event.setSummary(passedEvent.getEventTitle());
                event.setDescription(passedEvent.getDescription());
                EventDateTime startDateTime = new EventDateTime();
                SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
                Date dt = sdf.parse(passedEvent.getStartDate());
                startDateTime.setDateTime(new DateTime(dt));
                EventDateTime endDateTime = new EventDateTime();
                dt = sdf.parse(passedEvent.getEndDate());
                endDateTime.setDateTime(new DateTime(dt));
                event.setStart(startDateTime);
                event.setEnd(endDateTime);
                mService.events().insert("primary", event).execute();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            UtilHelpers.dismissWaitDialog();
            Intent intent = new Intent();
            intent.putExtra(Constants.GOOGLE_API_EVENT_MODEL_PASS_KEY, "Event Created");
            setResult(RESULT_OK, intent);
            finish();
        }

        @Override
        protected void onCancelled() {
            UtilHelpers.dismissWaitDialog();
            Intent intent = new Intent();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            GoogleApi.REQUEST_AUTHORIZATION);
                } else {
                    intent.putExtra(Constants.GOOGLE_API_ERROR_KEY, mLastError.getMessage());
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            } else {
                intent.putExtra(Constants.GOOGLE_API_ERROR_KEY, "Error, Request cancelled.");
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
    }

    private class UpdateEventAsyntask extends AsyncTask<Void, Void, Void> {
        private Calendar mService = null;
        private Exception mLastError = null;
        private EventModelDep passedEvent;


        UpdateEventAsyntask(GoogleAccountCredential credential, EventModelDep passedEvent) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new Calendar.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Google Calendar API")
                    .build();
            this.passedEvent = passedEvent;
        }


        @Override
        protected void onPreExecute() {
            //txt.setText("");
            UtilHelpers.showWaitDialog(GoogleApi.this, "Creating Event", "please wait...");
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Event event = new Event();
                event.setId(String.valueOf(passedEvent.getEventID()));
                event.setSummary(passedEvent.getEventTitle());
                event.setDescription(passedEvent.getDescription());
                EventDateTime startDateTime = new EventDateTime();
                startDateTime.setDateTime(new DateTime(passedEvent.getStartDate()));
                EventDateTime endDateTime = new EventDateTime();
                endDateTime.setDateTime(new DateTime(passedEvent.getEndDate()));
                event.setStart(startDateTime);
                event.setEnd(endDateTime);
                mService.events().update("primary", String.valueOf(passedEvent.getEventID()), event).execute();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            UtilHelpers.dismissWaitDialog();
            Intent intent = new Intent();
            intent.putExtra(Constants.GOOGLE_API_EVENT_MODEL_PASS_KEY, "Event Created");
            setResult(RESULT_OK, intent);
            finish();
        }

        @Override
        protected void onCancelled() {
            UtilHelpers.dismissWaitDialog();
            Intent intent = new Intent();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            GoogleApi.REQUEST_AUTHORIZATION);
                } else {
                    intent.putExtra(Constants.GOOGLE_API_ERROR_KEY, mLastError.getMessage());
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            } else {
                intent.putExtra(Constants.GOOGLE_API_ERROR_KEY, "Error, Request cancelled.");
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
    }

    private String getDateInFormat(String dateAndTime) {
        dateAndTime = dateAndTime.split("\"")[3];
        String dat[] = dateAndTime.split("T");
        return dat[0] + " " + (dat[1].split("\\+")[0]).split("\\.")[0];
    }
}
