package ru.ultrabasic.bstutp.messages.success;

import ru.ultrabasic.bstutp.messages.Message;
import ru.ultrabasic.bstutp.messages.MessageCodes;

public class OKMessage extends Message {
    public OKMessage() {
        super("ok", MessageCodes.OK);
    }
}
