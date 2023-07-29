package ru.ultrabasic.bstutp.messages.errors;

import ru.ultrabasic.bstutp.messages.Message;
import ru.ultrabasic.bstutp.messages.MessageCodes;

public class InvalidParameter extends Message {
    public InvalidParameter() {
        super("invalid_parameter", MessageCodes.UNPROCESSABLE_ENTITY);
    }
}
