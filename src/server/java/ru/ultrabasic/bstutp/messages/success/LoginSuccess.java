package ru.ultrabasic.bstutp.messages.success;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import ru.ultrabasic.bstutp.data.models.UserTypes;
import ru.ultrabasic.bstutp.messages.Message;
import ru.ultrabasic.bstutp.messages.MessageCodes;

import java.io.IOException;

public class LoginSuccess extends Message {

    public LoginSuccess() {
        super("login_success", MessageCodes.OK);
    }

    public void writeToResponse(HttpServletResponse response, UserTypes userType) throws IOException {
        response.setStatus(code.code);

        JSONObject body = new JSONObject();
        body.put("type", type);
        body.put("user_type", userType.type);

        response.getWriter().println(body);
    }
}
