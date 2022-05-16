package com.example.plannus;

public class ToDoTask {

    private String moduleName, task, status, deadLine;

    public ToDoTask(String moduleName, String task, String status, String deadLine) {
        this.moduleName = moduleName;
        this.task = task;
        this.status = status;
        this.deadLine = deadLine;
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

    public String getDeadLine() {
        return deadLine;
    }
}
