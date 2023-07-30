package ru.ultrabasic.bstutp.data.models;

public enum TestState {
    AVAILABLE("available"),
    NOT_AVAILABLE("not_available"),
    DRAFT("draft"),
    ACTIVE("active"),
    RUNNING("running"),
    COMPLETED("completed");

    public final String type;
    TestState(String type) {
        this.type = type;
    }
}
