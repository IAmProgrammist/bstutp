package ru.ultrabasic.bstutp.data.models;

import java.util.ArrayList;

public class Task {
    private int order;
    private TaskTypes taskType;
    private String description;
    private ArrayList<String> taskQuestions;

    public Task(int order, TaskTypes taskType, String description, ArrayList<String> taskQuestions) {
        this.order = order;
        this.taskType = taskType;
        this.description = description;
        this.taskQuestions = taskQuestions;
    }

    public Task() {
    }

    public int getOrder() {
        return order;
    }

    public TaskTypes getTaskType() {
        return taskType;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getTaskQuestions() {
        return taskQuestions;
    }
}