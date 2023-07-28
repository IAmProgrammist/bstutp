package ru.ultrabasic.bstutp.sql;

import java.util.ArrayList;

public class Test {
    private int time;
    private ArrayList<Task> tasks;

    public Test(int time, ArrayList<Task> tasks) {
        this.time = time;
        this.tasks = tasks;
    }

    public Test(int time) {
        this.time = time;
    }

    public Test() {
    }

    public int getTime() {
        return time;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }
}
