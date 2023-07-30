package ru.ultrabasic.bstutp.data.models.tests;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.ultrabasic.bstutp.data.models.tasks.Task;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    public Test(int id, Long time, String discipline, boolean isDraft, Integer idOwner, String name, List<Task> tasks, double result, boolean completed) {
        this.id = id;
        this.time = time;
        this.discipline = discipline;
        this.isDraft = isDraft;
        this.idOwner = idOwner;
        this.name = name;
        this.tasks = tasks;
        this.result = result;
        this.completed = completed;

        this.tasks = this.tasks.stream().distinct().collect(Collectors.toList());
        this.tasks.sort(Comparator.comparingInt(o -> o.order));
    }

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
