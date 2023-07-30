package ru.ultrabasic.bstutp.data.models;

import java.util.ArrayList;

public class DisciplineWithIdCompetences extends DisciplineRow {
    private ArrayList<Integer> idCompetences;

    public DisciplineWithIdCompetences(String name, ArrayList<Integer> idCompetences) {
        super(name, idCompetences);
        this.idCompetences = idCompetences;
    }

    public DisciplineWithIdCompetences(int id, String name, ArrayList<Integer> idCompetences) {
        super(id, name, idCompetences);
        this.idCompetences = idCompetences;
    }

    public ArrayList<Integer> getIdCompetences() {
        return idCompetences;
    }
}
