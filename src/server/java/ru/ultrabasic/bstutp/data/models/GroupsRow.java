package ru.ultrabasic.bstutp.data.models;

public class GroupsRow {
    private Integer id;
    private String name;
    private Integer idDirection;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getIdDirection() {
        return idDirection;
    }

    public GroupsRow(String name, Integer idDirection) {
        this.name = name;
        this.idDirection = idDirection;
    }

    public GroupsRow(Integer id, String name, Integer idDirection) {
        this.id = id;
        this.name = name;
        this.idDirection = idDirection;
    }
}
