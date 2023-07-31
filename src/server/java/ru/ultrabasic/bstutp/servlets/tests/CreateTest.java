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
import ru.ultrabasic.bstutp.data.models.UserTypes;
import ru.ultrabasic.bstutp.messages.errors.DatabaseError;
import ru.ultrabasic.bstutp.messages.errors.InvalidParameter;
import ru.ultrabasic.bstutp.messages.errors.JSONError;
import ru.ultrabasic.bstutp.messages.errors.SessionKeyInvalid;
import ru.ultrabasic.bstutp.messages.success.OKMessage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.stream.Collectors;

@WebServlet("/api/tests/create")
public class CreateTest extends HttpServlet {
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

            if (userInfo.userType != UserTypes.STUDENT) {
                int testId = SQLHandler.newTest(userId);

                JSONObject obj = new JSONObject();
                obj.put("testId", testId);

                resp.getWriter().println(obj);
                resp.setStatus(200);
            } else {
                new InvalidParameter().writeToResponse(resp);
            }
        } catch (SQLException e) {
            new DatabaseError().writeToResponse(resp);
        } catch (JSONException e) {
            new JSONError().writeToResponse(resp);
        }
    }
}
