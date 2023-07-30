package ru.ultrabasic.bstutp.data.models.tasks;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task task)) return false;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
