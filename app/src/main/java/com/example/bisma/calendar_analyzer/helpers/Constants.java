package com.example.bisma.calendar_analyzer.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.bisma.calendar_analyzer.R;

/**
 * Created by Devprovider on 16/08/2017.
 */

public class Constants {
    public static final String GOOGLE_API_RESULT_KEY = "google_api_result_key";
    public static final String GOOGLE_API_ERROR_KEY = "GOOGLE_API_ERROR_KEY";
    public static final String GOOGLE_API_EVENT_DELETE_KEY = "google_api_event_delete_key";
    public static final String GOOGLE_API_EVENT_MODEL_PASS_KEY = "google_api_event_model_key";
    public static final String GOOGLE_API_STARTDATE_PASS_KEY = "google_api_startdate_pass_key";
    public static final String GOOGLE_API_ENDDATE_PASS_KEY = "google_api_enddate_pass_key";
    public static final String GOOGLE_API_CALL_TYPE_KEY = "google_api_call_type_key";
    public static final String GOOGLE_API_EVENT_ID_PASS_KEY = "google_api_event_id_pass_key";
    public static final int GOOGLE_API_GET_CALL_KEY = 2156;
    public static final int GOOGLE_API_DELETE_CALL_KEY = 2556;
    public static final int GOOGLE_API_CREATE_CALL_KEY = 6554;
    public static final int GOOGLE_API_UPDATE_CALL_KEY = 2185;
    public static final String NO_EVENTS_FOUND = "No Events Found!";

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String EVENT_UPDATE_PASS_KEY = "event_update_pass_key";

    public static final String ERROR_SOMETHING_WENT_WRONG = "Something went wrong during the process of your request. Please try again.";
    public static final String ERROR_INTERNET_CONNECTION = "You're not connected to the Internet. Check your Internet connection and try again!";
    public static final String ERROR_TIMEOUT_REQUEST = "The connection is lost. Check your internet connection and try again";
    public static final String PIE_DATA_KEY = "pie_data_key";
    public static final String REPORT_DATA_KEY = "event_data_key";

    public static final String USER_ID_KEY = "USER_ID_KEY";
    public static final String REPORT_TYPE_PASS_KEY = "REPORT_TYPE_PASS_KEY";
    public static final String REMEMBER_USER_KEY = "REMEMBER_USER_KEY";
    public static final String USER_TYPE_KEY = "USER_TYPE_KEY";

    public static final int USER_TYPE_HOD = 0;
    public static final int USER_TYPE_FACULTY = 1;
    public static final String COLUMN_CHART_DATA_PASS_KEY = "bar_chart_pass_key";
    public static final String TEXTUAL_REPORT_PASS_KEY = "textual_report_pass_key";

    public static final String RESULT_OK = "Your time management is satisfactory!";
    public static final String RESULT_FAIL = "You should improve you time management!";
    public static final String REMINDER_ID_PASS_KEY = "REMINDER_ID_PASS_KEY";
    public static final String NOTIFICATION_DATA_PASS_KEY = "notification_data_pass_key";
    public static final String START_DATE_PASS_KEY = "START_DATE_PASS_KEY";
    public static final String END_DATE_PASS_KEY = "END_DATE_PASS_KEY";
    public static final String SERVICE_DATA_PASS_KEY = "SERVICE_DATA_PASS_KEY";

    public static String MAIN_ACTION = "com.action.main";
    public static String PLAY_ACTION = "com.action.play";
    public static String PAUSE_ACTION = "com.action.pause";
    public static String STOP_ACTION = "com.action.stop";
    public static String STARTFOREGROUND_ACTION = "com.action.startforeground";
    public static String STOPFOREGROUND_ACTION = "com.action.stopforeground";

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }

    public static Bitmap getDefaultAlbumArt(Context context) {
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            bm = BitmapFactory.decodeResource(context.getResources(),
                    R.mipmap.app_icon, options);
        } catch (Error ee) {
        } catch (Exception e) {
        }
        return bm;
    }

}
