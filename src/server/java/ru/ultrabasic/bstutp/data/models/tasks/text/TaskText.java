package ru.ultrabasic.bstutp.data.models.tasks.text;

import org.json.JSONObject;
import ru.ultrabasic.bstutp.data.models.tasks.Task;
import ru.ultrabasic.bstutp.data.models.tasks.TaskTypes;

public class TaskText extends Task {

    public TaskText(int id, int order, String description, int ownerId) {
        super(id, order, description, ownerId, TaskTypes.TEXT);
    }
}
