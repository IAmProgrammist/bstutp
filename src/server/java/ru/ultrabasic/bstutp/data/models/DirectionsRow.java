package ru.ultrabasic.bstutp.data.models;

public class DirectionsRow {
    private String espfCode;
    private String espfName;
    private String code;
    private String name;

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

    public DirectionsRow(String espfCode, String espfName, String code, String name) {
        this.espfCode = espfCode;
        this.espfName = espfName;
        this.code = code;
        this.name = name;
    }
}
