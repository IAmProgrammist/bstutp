package ru.ultrabasic.bstutp.data.models;

public enum UserTypes {
    STUDENT(0, "student"),
    TEACHER(1, "teacher"),
    ADMIN(2, "admin");

    public final Integer id;
    public final String type;
    UserTypes(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public static UserTypes fromID(int id) {
        for (UserTypes userType : UserTypes.values())
            if (userType.id.equals(id))
                return userType;

        return null;
    }
}
