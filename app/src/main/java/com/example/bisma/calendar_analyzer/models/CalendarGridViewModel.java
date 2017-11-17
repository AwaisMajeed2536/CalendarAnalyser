package com.example.bisma.calendar_analyzer.models;

/**
 * Created by Devprovider on 17/08/2017.
 */

public class CalendarGridViewModel {
    private int day;
    private String task;
    private String dayName;
    private String monthName;
    private int year;

    public CalendarGridViewModel() {
    }

    public CalendarGridViewModel(int day, String task, String dayName, String monthName, int year) {
        this.day = day;
        this.task = task;
        this.dayName = dayName;
        this.monthName = monthName;
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
