package ru.ultrabasic.bstutp.data.models;

public enum LevelTypes {
    UNDERGRADUATE(0, "undergraduate"),
    MAGISTRACY(1, "magistracy"),
    SPECIALTY(2, "specialty");

    public final Integer id;
    public final String type;

    LevelTypes(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public static LevelTypes fromID(int id) {
        for (LevelTypes levelType : LevelTypes.values())
            if (levelType.id.equals(id))
                return levelType;

        return null;
    }
}
