package com.example.bisma.calendar_analyzer.models;

public class task {
    private String taskName;
    private String tasDkate;
    private String taskTime;

    public task(String taskName, String tasDkate, String taskTime) {
        this.taskName = taskName;
        this.tasDkate = tasDkate;
        this.taskTime = taskTime;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTasDkate() {
        return tasDkate;
    }

    public void setTasDkate(String tasDkate) {
        this.tasDkate = tasDkate;
    }

    public String getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(String taskTime) {
        this.taskTime = taskTime;
    }
}

