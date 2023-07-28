package ru.ultrabasic.bstutp.messages;

import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;

public abstract class Message {
    public final String type;
    public final MessageCodes code;

    protected Message(String type, MessageCodes code) {
        this.type = type;
        this.code = code;
    }

    public void writeToResponse(HttpServletResponse response) throws IOException {
        response.setStatus(code.code);

        JSONObject body = new JSONObject();
        body.put("type", type);

        response.getWriter().println(body);
    }
}
