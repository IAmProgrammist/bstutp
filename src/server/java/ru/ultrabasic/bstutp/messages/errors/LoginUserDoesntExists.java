package ru.ultrabasic.bstutp.messages.errors;

import ru.ultrabasic.bstutp.messages.Message;
import ru.ultrabasic.bstutp.messages.MessageCodes;

public class LoginUserDoesntExists extends Message {
    public LoginUserDoesntExists() {
        super("user_doesnt_exists", MessageCodes.UNAUTHORIZED);
    }
}
