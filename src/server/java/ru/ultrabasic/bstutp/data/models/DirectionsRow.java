package ru.ultrabasic.bstutp.data.models;

public class DirectionsRow {
    private Integer id;
    private String espfCode;
    private String espfName;
    private String code;
    private String name;
    private LevelTypes levelType;
    private int educationalProgram;

    public DirectionsRow(int id, String espfCode, String espfName, String code, String name, LevelTypes levelType, int educationalProgram) {
        this.id = id;
        this.espfCode = espfCode;
        this.espfName = espfName;
        this.code = code;
        this.name = name;
        this.levelType = levelType;
        this.educationalProgram = educationalProgram;
    }

    public DirectionsRow(String espfCode, String espfName, String code, String name) {
        this.espfCode = espfCode;
        this.espfName = espfName;
        this.code = code;
        this.name = name;
    }

    public DirectionsRow(String espfCode, String espfName, String code, String name, LevelTypes levelType, int educationalProgram) {
        this.espfCode = espfCode;
        this.espfName = espfName;
        this.code = code;
        this.name = name;
        this.levelType = levelType;
        this.educationalProgram = educationalProgram;
    }

    public Integer getId() {
        return id;
    }

    public LevelTypes getIdLevel() {
        return levelType;
    }

    public int getIdEducationalProgram() {
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
