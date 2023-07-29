package ru.ultrabasic.bstutp.messages.success;

import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import ru.ultrabasic.bstutp.data.models.UserInfo;
import ru.ultrabasic.bstutp.messages.Message;
import ru.ultrabasic.bstutp.messages.MessageCodes;

import java.io.IOException;

public class TestDataMessage extends Message {
    public TestDataMessage() {
        super("test_data", MessageCodes.OK);
    }

    public void writeToResponse(HttpServletResponse response, JSONObject testData, UserInfo userInfo) throws IOException {
        response.setStatus(code.code);

        testData.put("type", type);
        testData.put("userInfo", userInfo.getJSONObject());

        response.getWriter().println(testData);
    }
}
