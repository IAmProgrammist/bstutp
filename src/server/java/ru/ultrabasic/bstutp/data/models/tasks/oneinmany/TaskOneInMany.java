package ru.ultrabasic.bstutp.data.models.tasks.oneinmany;

import org.json.JSONObject;
import ru.ultrabasic.bstutp.data.models.tasks.Task;
import ru.ultrabasic.bstutp.data.models.tasks.TaskTypes;

import java.util.ArrayList;
import java.util.List;

public class TaskOneInMany extends Task {
    List<TaskOneInManyQuestion> questionPull;

    public TaskOneInMany(int id, int order, String description, int ownerId, List<TaskOneInManyQuestion> questionPull) {
        super(id, order, description, ownerId, TaskTypes.ONE_IN_MANY);

        this.questionPull = questionPull;
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
}
