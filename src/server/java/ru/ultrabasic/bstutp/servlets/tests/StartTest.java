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
import ru.ultrabasic.bstutp.data.TestManager;
import ru.ultrabasic.bstutp.data.models.UserInfo;
import ru.ultrabasic.bstutp.messages.errors.DatabaseError;
import ru.ultrabasic.bstutp.messages.errors.JSONError;
import ru.ultrabasic.bstutp.messages.errors.SessionKeyInvalid;
import ru.ultrabasic.bstutp.messages.errors.TestNotStarted;
import ru.ultrabasic.bstutp.messages.success.TestStarted;

import java.io.IOException;
import java.sql.SQLException;
import java.util.stream.Collectors;

@WebServlet("/api/tests/start")
public class StartTest extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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

            if (TestManager.startTest(userId, testId))
                new TestStarted().writeToResponse(resp);
            else
                new TestNotStarted().writeToResponse(resp);
        } catch (SQLException e) {
            new DatabaseError().writeToResponse(resp);
        } catch (JSONException e) {
            new JSONError().writeToResponse(resp);
        }
    }
}
