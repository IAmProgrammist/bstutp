package ru.ultrabasic.bstutp.data.models;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.ultrabasic.bstutp.data.models.tasks.Task;

import java.util.List;

@AllArgsConstructor
public class Test {
    public int id;
    public Long time;
    public String discipline;
    public boolean isDraft;
    public Integer idOwner;
    public String name;
    public List<Task> tasks;
    public double result;
    public boolean completed;
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("name", name);
        object.put("duration", time);
        object.put("discipline", discipline);
        object.put("result", result);
        object.put("completed", completed);

        JSONArray tasksJSON = new JSONArray();
        for (Task t : tasks)
            tasksJSON.put(t.getJSONObject());

        object.put("tasks", tasksJSON);

        return object;
    }
}
