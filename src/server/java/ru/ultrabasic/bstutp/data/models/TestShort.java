package ru.ultrabasic.bstutp.data.models;

import org.json.JSONObject;

public class TestShort {
    public int id;
    public String name;
    public long duration;
    public String discipline;
    public Double result;
    public boolean completed;

    public TestShort(int id, String name, long duration, String discipline, Double result, boolean completed) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.discipline = discipline;
        this.result = result;
        this.completed = completed;
    }

    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("name", name);
        object.put("duration", duration);
        object.put("discipline", discipline);
        object.put("result", result);
        object.put("completed", completed);

        return object;
    }
}
