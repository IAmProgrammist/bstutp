package ru.ultrabasic.bstutp.data.models.tasks.text;

import org.json.JSONObject;
import ru.ultrabasic.bstutp.data.models.tasks.StudentAnswer;

public class TaskTextStudentAnswer extends TaskText implements StudentAnswer {
    String answer;
    Integer idReportDetailed;

    public TaskTextStudentAnswer(int id, int order, String description, int ownerId, String answer, Integer idReportDetailed) {
        super(id, order, description, ownerId);

        this.answer = answer;
        this.idReportDetailed = idReportDetailed;
    }

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = super.getJSONObject();
        object.put("answer", answer == null ? JSONObject.NULL : answer);
        object.put("idReportDetailed", idReportDetailed);

        return object;
    }
}
