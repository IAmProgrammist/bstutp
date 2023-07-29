package ru.ultrabasic.bstutp.data.models;

public enum TaskTypes {
    ONE_IN_MANY(0, "one_in_many"),
    TEXT(1, "text"),
    MANY_IN_MANY(2, "many_in_many");

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
}
