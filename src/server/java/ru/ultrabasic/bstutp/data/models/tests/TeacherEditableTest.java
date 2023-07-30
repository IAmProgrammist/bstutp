package ru.ultrabasic.bstutp.data.models.tests;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.ultrabasic.bstutp.data.models.records.Discipline;
import ru.ultrabasic.bstutp.data.models.records.Group;
import ru.ultrabasic.bstutp.data.models.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class TeacherEditableTest extends Test {

    public Discipline discipline = null;
    public List<Discipline> disciplines;
    public List<Group> allGroups;
    public List<Group> groups = new ArrayList<>();

    public TeacherEditableTest(int id, Long time, List<Discipline> disciplines, Discipline discipline, boolean isDraft,
                               Integer idOwner, String name, List<Task> tasks, List<Group> groups, List<Group> allGroups) {
        super(id, time, null, isDraft, idOwner, name, tasks, 0.0, false);

        this.disciplines = disciplines;
        if (disciplines.contains(discipline))
            this.discipline = discipline;

        this.allGroups = allGroups;
        for (Group g : groups)
            if (this.allGroups.contains(g))
                this.groups.add(g);
    }

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("name", name);
        object.put("duration", time);

        if (discipline != null) {
            object.put("idDiscipline", discipline.id());
            object.put("discipline", discipline.name());
        }
        JSONArray ds = new JSONArray();
        for (Discipline d : this.disciplines) {
            JSONObject dJSON = new JSONObject();
            dJSON.put("id", d.id());
            dJSON.put("name", d.name());

            ds.put(dJSON);
        }

        object.put("disciplines", ds);

        JSONArray gSelected = new JSONArray();
        for (Group g : groups) {
            gSelected.put(g.id());
        }
        object.put("groups", gSelected);

        JSONArray gAll = new JSONArray();
        for (Group g : allGroups) {
            JSONObject gJSON = new JSONObject();
            gJSON.put("id", g.id());
            gJSON.put("name", g.name());
            gSelected.put(gJSON);
        }
        object.put("groupsAll", gAll);

        object.put("result", result);
        object.put("completed", completed);

        JSONArray tasksJSON = new JSONArray();
        for (Task t : tasks)
            tasksJSON.put(t.getJSONObject());

        object.put("tasks", tasksJSON);

        return object;
    }
}
