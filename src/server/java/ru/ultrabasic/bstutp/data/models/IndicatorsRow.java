package ru.ultrabasic.bstutp.data.models;

public class IndicatorsRow {
    private String name;
    private int subId;
    private int idCompetence;

    public String getName() {
        return name;
    }

    public int getSubId() {
        return subId;
    }

    public int getIdCompetence() {
        return idCompetence;
    }

    public IndicatorsRow(String name, int subId, int idCompetence) {
        this.name = name;
        this.subId = subId;
        this.idCompetence = idCompetence;
    }
}
