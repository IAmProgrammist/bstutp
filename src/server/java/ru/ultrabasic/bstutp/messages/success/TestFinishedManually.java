package ru.ultrabasic.bstutp.messages.success;

import ru.ultrabasic.bstutp.messages.Message;
import ru.ultrabasic.bstutp.messages.MessageCodes;

public class TestFinishedManually extends Message {
    public TestFinishedManually() {
        super("test_finished_manually", MessageCodes.OK);
    }
}
