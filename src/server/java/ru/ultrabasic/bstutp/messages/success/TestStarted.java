package ru.ultrabasic.bstutp.messages.success;

import ru.ultrabasic.bstutp.messages.Message;
import ru.ultrabasic.bstutp.messages.MessageCodes;

public class TestStarted extends Message {
    public TestStarted() {
        super("test_started", MessageCodes.OK);
    }
}
