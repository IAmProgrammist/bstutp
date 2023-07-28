package ru.ultrabasic.bstutp.messages;

public enum MessageCodes {
    OK(200),
    INTERNAL_SERVER_ERROR(500),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    UNPROCESSABLE_ENTITY(422);

    public int code;

    MessageCodes(int code) {
        this.code = code;
    }
}
