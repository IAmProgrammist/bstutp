package ru.ultrabasic.bstutp.data.models.tests;

import org.json.JSONObject;
import ru.ultrabasic.bstutp.data.models.tasks.Task;

import java.util.List;

public class Report extends Test {
    public Integer reportId;
    public Long completionTime;
    public Report(int id, Long time, String discipline, boolean isDraft, Integer idOwner, String name, List<Task> tasks,
                  double result, boolean completed, Integer reportId, Long completionTime) {
        super(id, time, discipline, isDraft, idOwner, name, tasks, result, completed);

        this.reportId = reportId;
        this.completionTime = completionTime;
    }

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = super.getJSONObject();

        object.put("reportId", reportId);
        object.put("completionTime", completionTime);

        return object;
    }
}
