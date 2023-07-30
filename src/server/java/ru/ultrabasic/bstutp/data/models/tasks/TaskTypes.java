package ru.ultrabasic.bstutp.data.models.tasks;

import ru.ultrabasic.bstutp.data.models.UserTypes;

public enum TaskTypes {
    ONE_IN_MANY(0, "one_in_many"),
    TEXT(1, "text");

    public final Integer id;
    public final String type;
    TaskTypes(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public static TaskTypes fromID(int id) {
        for (TaskTypes taskType : TaskTypes.values())
            if (taskType.id.equals(id))
                return taskType;

        return null;
    }

    public static TaskTypes fromType(String type) {
        for (TaskTypes taskType : TaskTypes.values())
            if (taskType.type.equals(type))
                return taskType;

        return null;
    }
}
