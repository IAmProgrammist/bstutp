package ru.ultrabasic.bstutp.messages.success;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import ru.ultrabasic.bstutp.messages.Message;
import ru.ultrabasic.bstutp.messages.MessageCodes;

import java.io.IOException;

public class LoginSuccess extends Message {

    public LoginSuccess() {
        super("login_success", MessageCodes.OK);
    }

    public void writeToResponse(HttpServletResponse response, Cookie sessionKeyCookie) throws IOException {
        super.writeToResponse(response);

        sessionKeyCookie.setSecure(true);
        response.addCookie(sessionKeyCookie);
    }
}
