package ru.ultrabasic.bstutp.messages.errors;

import ru.ultrabasic.bstutp.messages.Message;
import ru.ultrabasic.bstutp.messages.MessageCodes;

public class SessionKeyInvalid extends Message {
    public SessionKeyInvalid() {
        super("session_key_invalid_or_expired", MessageCodes.FORBIDDEN);
    }
}
