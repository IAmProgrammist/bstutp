package ru.ultrabasic.bstutp.servlets.account;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import ru.ultrabasic.bstutp.Config;
import ru.ultrabasic.bstutp.data.SQLHandler;
import ru.ultrabasic.bstutp.data.models.UserTypes;
import ru.ultrabasic.bstutp.messages.errors.DatabaseError;
import ru.ultrabasic.bstutp.messages.errors.JSONError;
import ru.ultrabasic.bstutp.messages.errors.LoginUserDoesntExists;
import ru.ultrabasic.bstutp.messages.success.LoginSuccess;

import java.io.IOException;
import java.sql.SQLException;
import java.util.stream.Collectors;

@WebServlet("/api/account/login")
public class Login extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            JSONObject jsonUpdate = new JSONObject(req.getReader().lines().collect(Collectors.joining(System.lineSeparator())));
            String login = jsonUpdate.has("login") ? jsonUpdate.getString("login") : "";
            String password = jsonUpdate.has("password") ? jsonUpdate.getString("password") : "";
            boolean rememberMe = jsonUpdate.has("rememberMe") && jsonUpdate.getBoolean("rememberMe");

            Integer id;
            if ((id = SQLHandler.getUserId(login, password)) != null) {
                String sessionKey = SQLHandler.createSessionKey(id, rememberMe);

                Cookie cookie = new Cookie("sessionKey", sessionKey);
                cookie.setMaxAge(rememberMe ? (int) (Config.SESSION_KEY_EXPIRATION_YEARS * 365 * 60 * 60) : 0);
                cookie.setSecure(false);
                cookie.setPath("/");

                UserTypes userType = SQLHandler.getUserType(id);

                new LoginSuccess().writeToResponse(resp, cookie, userType);
            } else {
                new LoginUserDoesntExists().writeToResponse(resp);
            }
        } catch (SQLException e) {
            new DatabaseError().writeToResponse(resp);
        } catch (JSONException e) {
            new JSONError().writeToResponse(resp);
        }
    }
}
