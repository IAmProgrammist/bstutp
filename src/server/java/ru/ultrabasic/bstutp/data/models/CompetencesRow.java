package ru.ultrabasic.bstutp.data.models;

public class CompetencesRow {
    private String name;
    private String description;

    public CompetencesRow(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
