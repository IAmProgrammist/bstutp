package ru.ultrabasic.bstutp.messages.errors;

import ru.ultrabasic.bstutp.messages.Message;
import ru.ultrabasic.bstutp.messages.MessageCodes;

public class JSONError extends Message {
    public JSONError() {
        super("json_parse_exception", MessageCodes.UNPROCESSABLE_ENTITY);
    }
}
