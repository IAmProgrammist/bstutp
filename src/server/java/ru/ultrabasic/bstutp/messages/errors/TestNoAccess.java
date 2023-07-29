package ru.ultrabasic.bstutp.messages.errors;

import ru.ultrabasic.bstutp.messages.Message;
import ru.ultrabasic.bstutp.messages.MessageCodes;

public class TestNoAccess extends Message {
    public TestNoAccess() {
        super("test_access_restricted", MessageCodes.FORBIDDEN);
    }
}
