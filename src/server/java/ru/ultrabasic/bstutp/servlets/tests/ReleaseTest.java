package ru.ultrabasic.bstutp.servlets.tests;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import ru.ultrabasic.bstutp.data.SQLHandler;
import ru.ultrabasic.bstutp.data.models.TestState;
import ru.ultrabasic.bstutp.data.models.UserInfo;
import ru.ultrabasic.bstutp.messages.errors.DatabaseError;
import ru.ultrabasic.bstutp.messages.errors.JSONError;
import ru.ultrabasic.bstutp.messages.errors.SessionKeyInvalid;
import ru.ultrabasic.bstutp.messages.success.OKMessage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.stream.Collectors;

@WebServlet("/api/tests/release")
public class ReleaseTest extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Integer userId = null;
            for (Cookie cookie : req.getCookies())
                if (cookie.getName().equals("sessionKey"))
                    userId = SQLHandler.getUserIdBySessionKey(cookie.getValue());

            if (userId == null) {
                new SessionKeyInvalid().writeToResponse(resp);
                return;
            }

            UserInfo userInfo = SQLHandler.getUserInfo(userId);

            if (userInfo == null || userInfo.userType == null)
                throw new SQLException();

            JSONObject jsonStart = new JSONObject(req.getReader().lines().collect(Collectors.joining(System.lineSeparator())));
            int testId = jsonStart.getInt("idTest");
            String testType = jsonStart.getString("testType");

            if (SQLHandler.getState(userId, testId) == TestState.DRAFT)
                if (testType.equals("text")) {
                    SQLHandler.addTextTask(testId, userId);
                } else if (testType.equals("one_in_many")) {
                    SQLHandler.addOneInManyTask(testId, userId);
                }

            new OKMessage().writeToResponse(resp);
        } catch (SQLException e) {
            new DatabaseError().writeToResponse(resp);
        } catch (JSONException e) {
            new JSONError().writeToResponse(resp);
        }
    }
}
