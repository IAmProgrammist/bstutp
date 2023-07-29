package ru.ultrabasic.bstutp.data.models;

public class DirectionsRow {
    private String espfCode;
    private String espfName;
    private String code;
    private String name;
    private int idLevel;
    private int educationalProgram;

    public DirectionsRow(String espfCode, String espfName, String code, String name) {
        this.espfCode = espfCode;
        this.espfName = espfName;
        this.code = code;
        this.name = name;
    }

    public DirectionsRow(String espfCode, String espfName, String code, String name, int idLevel, int educationalProgram) {
        this.espfCode = espfCode;
        this.espfName = espfName;
        this.code = code;
        this.name = name;
        this.idLevel = idLevel;
        this.educationalProgram = educationalProgram;
    }

    public int getIdLevel() {
        return idLevel;
    }

    public int getEducationalProgram() {
        return educationalProgram;
    }

    public String getEspfCode() {
        return espfCode;
    }

    public String getEspfName() {
        return espfName;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
