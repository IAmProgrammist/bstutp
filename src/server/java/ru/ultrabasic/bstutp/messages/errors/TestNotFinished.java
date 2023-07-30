package ru.ultrabasic.bstutp.messages.errors;

import ru.ultrabasic.bstutp.messages.Message;
import ru.ultrabasic.bstutp.messages.MessageCodes;

public class TestNotFinished extends Message {
    public TestNotFinished() {
        super("test_not_finished", MessageCodes.FORBIDDEN);
    }
}
