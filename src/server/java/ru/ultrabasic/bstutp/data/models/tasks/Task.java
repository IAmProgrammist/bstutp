package ru.ultrabasic.bstutp.data.models.tasks;

import org.json.JSONObject;

import java.util.ArrayList;

public abstract class Task {
    public int id;
    public int order;
    public String description;
    public int ownerId;
    public TaskTypes taskType;

    public Task(int id, int order, String description, int ownerId, TaskTypes taskType) {
        this.id = id;
        this.order = order;
        this.description = description;
        this.ownerId = ownerId;
        this.taskType = taskType;
    }

    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();

        object.put("id", id);
        object.put("order", order);
        object.put("description", description);
        object.put("ownerId", ownerId);
        object.put("taskType", taskType.type);

        return object;
    }
}
