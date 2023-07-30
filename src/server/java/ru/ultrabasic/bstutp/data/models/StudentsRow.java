package ru.ultrabasic.bstutp.data.models;

public class StudentsRow {
    private Integer idUser;
    private Integer idGroup;
    private Integer reportCardId;

    public Integer getIdUser() {
        return idUser;
    }

    public Integer getIdGroup() {
        return idGroup;
    }

    public Integer getReportCardId() {
        return reportCardId;
    }

    public StudentsRow(Integer idUser, Integer idGroup, Integer reportCardId) {
        this.idUser = idUser;
        this.idGroup = idGroup;
        this.reportCardId = reportCardId;
    }
}
