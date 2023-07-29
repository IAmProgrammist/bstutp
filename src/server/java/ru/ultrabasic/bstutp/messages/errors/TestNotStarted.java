package ru.ultrabasic.bstutp.messages.errors;

import ru.ultrabasic.bstutp.messages.Message;
import ru.ultrabasic.bstutp.messages.MessageCodes;

public class TestNotStarted extends Message {
    public TestNotStarted() {
        super("test_not_started", MessageCodes.FORBIDDEN);
    }
}
