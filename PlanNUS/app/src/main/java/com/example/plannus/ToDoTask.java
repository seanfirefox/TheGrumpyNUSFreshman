package com.example.plannus;

import com.google.firebase.database.PropertyName;

public class ToDoTask {

    @PropertyName("moduleName")
    private String moduleName;

    @PropertyName("task")
    private String task;

    @PropertyName("status")
    private String status;

    @PropertyName("deadLineDateTime")
    private String deadLineDateTime;

    @PropertyName("plannedDateTime")
    private String plannedDateTime;

    public ToDoTask() {

    }

    public ToDoTask(String moduleName, String task, String status, String deadLineDateTime, String plannedDateTime) {
        this.moduleName = moduleName;
        this.task = task;
        this.status = status;
        this.deadLineDateTime = deadLineDateTime;
        this.plannedDateTime = plannedDateTime;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getTask() {
        return task;
    }

    public String getStatus() {
        return status;
    }

    public String getDeadLineDateTime() {
        return deadLineDateTime;
    }

    public String getPlannedDateTime() { return  plannedDateTime; }

}
