package ru.ultrabasic.bstutp.data.models;

import java.util.ArrayList;

public class DisciplineRow {
    private Integer id;
    private String name;


    public DisciplineRow(String name, ArrayList<Integer> idCompetences) {
        this.name = name;
    }

    public DisciplineRow(Integer id, String name, ArrayList<Integer> idCompetences) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
