package ru.ultrabasic.bstutp.sql;

import java.util.ArrayList;

public class Task {
    private int order;
    private int taskType;
    private String description;
    private ArrayList<String> taskQuestions;

    public Task(int order, int taskType, String description, ArrayList<String> taskQuestions) {
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

    public int getTaskType() {
        return taskType;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getTaskQuestions() {
        return taskQuestions;
    }
}
