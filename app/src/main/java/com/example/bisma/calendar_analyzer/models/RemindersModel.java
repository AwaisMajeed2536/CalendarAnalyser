package com.example.bisma.calendar_analyzer.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Devprovider on 15/08/2017.
 */

public class RemindersModel implements Parcelable{
    private int id;
    private String title, text, startDateTime, endDateTime;

    public RemindersModel() {
    }

    public RemindersModel(int id, String title, String text, String startDateTime, String endDateTime) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    protected RemindersModel(Parcel in) {
        id = in.readInt();
        title = in.readString();
        text = in.readString();
        startDateTime = in.readString();
        endDateTime = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(text);
        dest.writeString(startDateTime);
        dest.writeString(endDateTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RemindersModel> CREATOR = new Creator<RemindersModel>() {
        @Override
        public RemindersModel createFromParcel(Parcel in) {
            return new RemindersModel(in);
        }

        @Override
        public RemindersModel[] newArray(int size) {
            return new RemindersModel[size];
        }
    };

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }
}
