package ru.ultrabasic.bstutp.data.models;

import ru.ultrabasic.bstutp.data.models.tasks.TaskTypes;

public class TasksRow {
    private Integer id;

    public Integer getId() {
        return id;
    }

    public TasksRow(int id, int idTest, int order, TaskTypes taskType, String description, int idOwner) {
        this.id = id;
        this.idTest = idTest;
        this.order = order;
        this.taskType = taskType;
        this.description = description;
        this.idOwner = idOwner;
    }

    private int idTest;
    private int order;
    private TaskTypes taskType;
    private String description;
    private int idOwner;

    public TasksRow(int idTest, int order, TaskTypes taskType, String description, int idOwner) {
        this.idTest = idTest;
        this.order = order;
        this.taskType = taskType;
        this.description = description;
        this.idOwner = idOwner;
    }

    public int getIdTest() {
        return idTest;
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

    public int getIdOwner() {
        return idOwner;
    }
}
