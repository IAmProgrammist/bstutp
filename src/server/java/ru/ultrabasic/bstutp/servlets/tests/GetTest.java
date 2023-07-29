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
import ru.ultrabasic.bstutp.data.models.TestState;
import ru.ultrabasic.bstutp.data.models.UserInfo;
import ru.ultrabasic.bstutp.data.models.UserTypes;
import ru.ultrabasic.bstutp.messages.errors.*;
import ru.ultrabasic.bstutp.messages.success.*;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/api/tests/test")
public class GetTest extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

            Integer testId = Integer.valueOf(req.getParameter("id_test"));
            JSONObject data = TestManager.whassup(userId, testId);

            if (data.getString("state").equals(TestState.NOT_AVAILABLE.type)) {
                new TestNoAccess().writeToResponse(resp);
            }

            new TestDataMessage().writeToResponse(resp, data, userInfo);
        } catch (SQLException e) {
            new DatabaseError().writeToResponse(resp);
        } catch (JSONException e) {
            new JSONError().writeToResponse(resp);
        } catch (NumberFormatException e) {
            new InvalidParameter().writeToResponse(resp);
        }
    }
}
