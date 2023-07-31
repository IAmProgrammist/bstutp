package ru.ultrabasic.bstutp.data.models.tests;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.ultrabasic.bstutp.data.models.records.Discipline;
import ru.ultrabasic.bstutp.data.models.records.Group;
import ru.ultrabasic.bstutp.data.models.records.Indicator;
import ru.ultrabasic.bstutp.data.models.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class TeacherEditableTest extends Test {

    public Discipline discipline = null;
    public List<Discipline> disciplines;
    public List<Group> allGroups;
    public List<Group> groups = new ArrayList<>();
    public List<Indicator> indicators = new ArrayList<>();

    public TeacherEditableTest(int id, Long time, List<Discipline> disciplines, Discipline discipline, boolean isDraft,
                               Integer idOwner, String name, List<Task> tasks, List<Group> groups, List<Group> allGroups,
                               List<Indicator> indicators) {
        super(id, time, null, isDraft, idOwner, name, tasks, 0.0, false);

        this.disciplines = disciplines;
        if (disciplines.contains(discipline))
            this.discipline = discipline;

        this.allGroups = allGroups;
        for (Group g : groups)
            if (this.allGroups.contains(g))
                this.groups.add(g);

        this.indicators = indicators;
    }

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("name", name);
        object.put("duration", time);

        object.put("idDiscipline", discipline == null ? JSONObject.NULL : discipline.id());
        object.put("discipline", discipline == null ? JSONObject.NULL : discipline.name());

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
            gAll.put(gJSON);
        }
        object.put("groupsAll", gAll);

        object.put("result", result);
        object.put("completed", completed);

        JSONArray tasksJSON = new JSONArray();
        for (Task t : tasks)
            tasksJSON.put(t.getJSONObject());

        JSONArray inds = new JSONArray();
        for (Indicator ind : indicators) {
            JSONObject indJSON = new JSONObject();
            indJSON.put("id", ind.id());
            indJSON.put("subId", ind.subId());
            indJSON.put("name", ind.name());
            indJSON.put("competenceId", ind.competence());
            inds.put(indJSON);
        }
        object.put("indicators", inds);

        object.put("tasks", tasksJSON);

        return object;
    }
}
