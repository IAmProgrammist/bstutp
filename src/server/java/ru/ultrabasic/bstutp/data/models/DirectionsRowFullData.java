package ru.ultrabasic.bstutp.data.models;

public class DirectionsRowFullData extends DirectionsRow{
    private int idLevel;
    private String educationalProgram;

    public int getIdLevel() {
        return idLevel;
    }

    public String getEducationalProgram() {
        return educationalProgram;
    }

    public DirectionsRowFullData(String espfCode, String espfName, String code, String name,
                                 String educationalProgram, int idLevel) {
        super(espfCode, espfName, code, name);
        this.educationalProgram = educationalProgram;
        this.idLevel = idLevel;
    }
}
