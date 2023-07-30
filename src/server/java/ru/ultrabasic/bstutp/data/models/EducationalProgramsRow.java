package ru.ultrabasic.bstutp.data.models;

public class EducationalProgramsRow {
    private Integer id;
    private String name;

    public EducationalProgramsRow(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public EducationalProgramsRow(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
