package com.example.plannus;

public class ToDoTask {

    private String moduleName, task, status, deadLineDate, deadLineTime, plannedDate, plannedTime;

    public ToDoTask(String moduleName, String task, String status, String deadLineDate, String deadLineTime, String plannedDate, String plannedTime) {
        this.moduleName = moduleName;
        this.task = task;
        this.status = status;
        this.deadLineDate = deadLineDate;
        this.deadLineTime = deadLineTime;
        this.plannedDate = plannedDate;
        this.plannedTime = plannedTime;
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

    public String getDeadLineDate() {
        return deadLineDate;
    }

    public String getDeadLineTime() { return deadLineTime; }

    public String getPlannedDate() { return  plannedDate; }

    public String getPlannedTime() { return plannedTime; }
}
