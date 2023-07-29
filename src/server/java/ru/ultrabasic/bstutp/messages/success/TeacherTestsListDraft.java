package ru.ultrabasic.bstutp.messages.success;

import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.ultrabasic.bstutp.data.models.UserInfo;
import ru.ultrabasic.bstutp.data.models.UserTypes;
import ru.ultrabasic.bstutp.messages.Message;
import ru.ultrabasic.bstutp.messages.MessageCodes;

import java.io.IOException;

public class TeacherTestsListDraft extends Message {
    public TeacherTestsListDraft() {
        super("tests_draft_teacher", MessageCodes.OK);
    }

    public void writeToResponse(HttpServletResponse response, JSONObject activeTests, UserInfo userInfo) throws IOException {
        response.setStatus(code.code);

        JSONObject body = new JSONObject();
        body.put("type", type);
        body.put("userInfo", userInfo.getJSONObject());
        body.put("tests", activeTests);

        response.getWriter().println(body);
    }
}
