package com.example.bisma.calendar_analyzer.models;

import android.os.Parcel;
import android.os.Parcelable;



public class PieDataModel implements Parcelable{
    private String item;
    private float occurences;

    public PieDataModel() {
    }

    public PieDataModel(String item, float occurences) {
        this.item = item;
        this.occurences = occurences;
    }

    protected PieDataModel(Parcel in) {
        item = in.readString();
        occurences = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(item);
        dest.writeFloat(occurences);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PieDataModel> CREATOR = new Creator<PieDataModel>() {
        @Override
        public PieDataModel createFromParcel(Parcel in) {
            return new PieDataModel(in);
        }

        @Override
        public PieDataModel[] newArray(int size) {
            return new PieDataModel[size];
        }
    };

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public float getOccurences() {
        return occurences;
    }

    public void setOccurences(float occurences) {
        this.occurences = occurences;
    }


}

