package com.example.bisma.calendar_analyzer.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class EventModelDep implements Parcelable{
    private int eventID;
    private String eventTitle;
    private String description;
    private String startDateTime;
    private String endDateTime;
    private int isScheduled = 1;
    private boolean completed = false;

    public EventModelDep() {
        this.eventID = 0;
        this.eventTitle = "";
        this.description = "";
        this.startDateTime = "";
        this.endDateTime = "";
        isScheduled = 1;
        completed = false;
    }

    protected EventModelDep(Parcel in) {
        eventID = in.readInt();
        eventTitle = in.readString();
        description = in.readString();
        startDateTime = in.readString();
        endDateTime = in.readString();
        isScheduled = in.readInt();
        completed = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(eventID);
        dest.writeString(eventTitle);
        dest.writeString(description);
        dest.writeString(startDateTime);
        dest.writeString(endDateTime);
        dest.writeInt(isScheduled);
        dest.writeByte((byte) (completed ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EventModelDep> CREATOR = new Creator<EventModelDep>() {
        @Override
        public EventModelDep createFromParcel(Parcel in) {
            return new EventModelDep(in);
        }

        @Override
        public EventModelDep[] newArray(int size) {
            return new EventModelDep[size];
        }
    };

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int isScheduled() {
        return isScheduled;
    }

    public void setScheduled(int scheduled) {
        isScheduled = scheduled;
    }

    public EventModelDep(int eventID, String eventTitle, String description, String startDateTime, String endDateTime) {
        this.eventID = eventID;
        this.eventTitle = eventTitle;
        this.description = description;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.isScheduled = 1;
        completed = false;
    }

    public EventModelDep(int eventID, String eventTitle, String description, String startDateTime, String endDateTime, boolean completed) {
        this.eventID = eventID;
        this.eventTitle = eventTitle;
        this.description = description;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.isScheduled = 1;
        this.completed = completed;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDateTime;
    }

    public void setStartDate(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getEndDate() {
        return endDateTime;
    }

    public void setEndDate(String endDateTime) {
        this.endDateTime = endDateTime;
    }
}
