package ru.ultrabasic.bstutp.data.models;

import java.util.ArrayList;

public class EducationalProgramsWithIdCompetences extends EducationalProgramsRow {
    private ArrayList<Integer> idCompetences;

    public EducationalProgramsWithIdCompetences(int id, String name, ArrayList<Integer> idCompetences) {
        super(id, name);
        this.idCompetences = idCompetences;
    }

    public EducationalProgramsWithIdCompetences(String name, ArrayList<Integer> idCompetences) {
        super(name);
        this.idCompetences = idCompetences;
    }

    public ArrayList<Integer> getIdCompetences() {
        return idCompetences;
    }
}
