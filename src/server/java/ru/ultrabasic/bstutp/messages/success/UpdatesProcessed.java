package ru.ultrabasic.bstutp.messages.success;

import ru.ultrabasic.bstutp.messages.Message;
import ru.ultrabasic.bstutp.messages.MessageCodes;

public class UpdatesProcessed extends Message {
    public UpdatesProcessed() {
        super("updates_processed", MessageCodes.OK);
    }
}
