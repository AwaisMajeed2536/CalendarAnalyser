package com.example.bisma.calendar_analyzer.helpers;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.bisma.calendar_analyzer.interfaces.DatePickerCallback;
import com.example.bisma.calendar_analyzer.ui.CreateEventActivity;

import java.io.ByteArrayOutputStream;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class UtilHelpers {

    private static ProgressDialog waitDialog = null;

    public static void showAlertDialog(Context context, String title, @Nullable String message) {
        AlertDialog dialog = new AlertDialog.Builder(context).setTitle(title)
                .setMessage("" + message).setCancelable(true)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    public static void showWaitDialog(Context context, String title, String message) {
        try {
            if (waitDialog != null) {
                if (!waitDialog.isShowing()) {
                    waitDialog = new ProgressDialog(context);
                    waitDialog.setTitle(title);
                    waitDialog.setMessage(message);
                    waitDialog.setIndeterminate(true);
                    waitDialog.setCancelable(false);
                    waitDialog.show();
                }
            } else {
                waitDialog = new ProgressDialog(context);
                waitDialog.setTitle(title);
                waitDialog.setMessage(message);
                waitDialog.setIndeterminate(true);
                waitDialog.setCancelable(false);
                waitDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dismissWaitDialog() {
        try {
            if (waitDialog != null && waitDialog.isShowing()) {
                waitDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public static String getMonthName(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11) {
            month = months[num];
        }
        return month;
    }


    public static void showDateTimePicker(final Context context, final int type, final DatePickerCallback callback) {
        final Calendar date;
        final Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(new Date());
        date = Calendar.getInstance();
        new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, (monthOfYear + 1), dayOfMonth);
                new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        date.set(Calendar.SECOND, 1);
                        callback.onDateSelected(type, date);
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

    public static void createLoginSession(Context context, String userId, int userType) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(Constants.USER_ID_KEY, userId).apply();
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(Constants.USER_TYPE_KEY, userType).apply();
    }

    public static void shouldRememberUser(Context context, boolean flag) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(Constants.REMEMBER_USER_KEY, flag).apply();
    }

    public static boolean isUserLoggedIn(Context context) {
        if (PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.USER_ID_KEY, "").equalsIgnoreCase("")) {
            return false;
        }
        return true;
    }

    public static boolean isRememberedUser(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(Constants.REMEMBER_USER_KEY, false);
    }

    public static void endLoginSession(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().apply();
    }

    public static String getUserId(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.USER_ID_KEY, "");
    }

    public static int getUserType(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(Constants.USER_TYPE_KEY, -1);
    }


    public static String getDateInFormat(Calendar dateTime, boolean addOne) {
        return dateTime.get(Calendar.YEAR) + "-" + (addOne ? String.format("%02d", (dateTime.get(Calendar.MONTH) + 1)) : String.format("%02d", dateTime.get(Calendar.MONTH)))
                + "-" + String.format("%02d", dateTime.get(Calendar.DAY_OF_MONTH)) + " "
                + String.format("%02d", dateTime.get(Calendar.HOUR_OF_DAY)) + ":"
                + String.format("%02d", dateTime.get(Calendar.MINUTE)) + ":"
                + String.format("%02d", dateTime.get(Calendar.SECOND));
    }

    @NonNull
    public static Calendar getCalendarFromString(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
            Date dt = sdf.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dt);
            return calendar;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Calendar.getInstance();
    }

    public static int getIdFromDate(String date) {
        return (int) getDateInFormat(date).getTimeInMillis();
    }

    public static Calendar getDateInFormat(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
        Date dt = null;
        try {
            dt = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        return calendar;
    }
}

