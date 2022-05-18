package com.example.plannus;

import com.google.firebase.database.PropertyName;

public class ToDoTask {

    @PropertyName("moduleName")
    private String moduleName;

    @PropertyName("task")
    private String task;

    @PropertyName("status")
    private String status;

    @PropertyName("deadLineDate")
    private String deadLineDate;

    @PropertyName("deadLineTime")
    private String deadLineTime;

    @PropertyName("plannedDate")
    private String plannedDate;

    @PropertyName("plannedTime")
    private String plannedTime;

    public ToDoTask() {

    }

    public ToDoTask(String moduleName, String task, String status, String deadLineDate, String deadLineTime, String plannedDate, String plannedTime) {
        this.moduleName = moduleName;
        this.task = task;
        this.status = status;
        this.deadLineDate = deadLineDate;
        this.deadLineTime = deadLineTime;
        this.plannedDate = plannedDate;
        this.plannedTime = plannedTime;
    }

    @PropertyName("moduleName")
    public String getModuleName() {
        return moduleName;
    }

    @PropertyName("task")
    public String getTask() {
        return task;
    }

    @PropertyName("status")
    public String getStatus() {
        return status;
    }

    @PropertyName("deadLineDate")
    public String getDeadLineDate() {
        return deadLineDate;
    }

    @PropertyName("deadLineTime")
    public String getDeadLineTime() { return deadLineTime; }

    @PropertyName("plannedDate")
    public String getPlannedDate() { return  plannedDate; }

    @PropertyName("plannedTime")
    public String getPlannedTime() { return plannedTime; }
}
