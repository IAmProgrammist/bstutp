package ru.ultrabasic.bstutp.data.models.tasks.oneinmany;

import org.json.JSONObject;
import ru.ultrabasic.bstutp.data.models.tasks.StudentAnswer;
import ru.ultrabasic.bstutp.data.models.tasks.Task;

import java.util.List;

public class TaskOneInManyStudentAnswer extends TaskOneInMany implements StudentAnswer {

    TaskOneInManyQuestion chosen = null;
    Integer idReportDetailed;

    public TaskOneInManyStudentAnswer(int id, int order, String description, int ownerId,
                                      List<TaskOneInManyQuestion> questionPull, TaskOneInManyQuestion chosen, Integer idReportDetailed) {
        super(id, order, description, ownerId, questionPull);

        this.idReportDetailed = idReportDetailed;

        if (questionPull.contains(chosen))
            this.chosen = chosen;
    }

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = super.getJSONObject();
        object.put("idAnswer", chosen == null ? JSONObject.NULL : chosen.id());
        object.put("idReportDetailed", idReportDetailed);

        return object;
    }
}
