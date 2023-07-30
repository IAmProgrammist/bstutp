package ru.ultrabasic.bstutp.data.models.tasks.oneinmany;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.ultrabasic.bstutp.data.models.tasks.Task;
import ru.ultrabasic.bstutp.data.models.tasks.TaskTypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskOneInMany extends Task {
    List<TaskOneInManyQuestion> questionPull;

    public TaskOneInMany(int id, int order, String description, int ownerId, List<TaskOneInManyQuestion> questionPull) {
        super(id, order, description, ownerId, TaskTypes.ONE_IN_MANY);

        this.questionPull = questionPull;
    }

    public JSONObject getJSONObject() {
        JSONObject object = super.getJSONObject();

        JSONArray questionPullJSONArray = new JSONArray();
        for (TaskOneInManyQuestion question : this.questionPull) {
            JSONObject questionJSON = new JSONObject();
            questionJSON.put("id", question.id());
            questionJSON.put("text", question.text());

            questionPullJSONArray.put(questionJSON);
        }
        object.put("questionPull", questionPullJSONArray);

        return object;
    }
}
