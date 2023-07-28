package ru.ultrabasic.bstutp.messages.errors;

import ru.ultrabasic.bstutp.messages.Message;
import ru.ultrabasic.bstutp.messages.MessageCodes;

public class DatabaseError extends Message {
    public DatabaseError() {
        super("database_error", MessageCodes.INTERNAL_SERVER_ERROR);
    }
}
