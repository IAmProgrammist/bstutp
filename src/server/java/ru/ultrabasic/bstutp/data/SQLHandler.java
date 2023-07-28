package ru.ultrabasic.bstutp.data;

import ru.ultrabasic.bstutp.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

public class SQLHandler {
    static Connection connection;

    static {
        //TODO: вынести в .properties
        String url = Config.SQL_URL;
        String username = Config.SQL_NAME;
        String password = Config.SQL_PASSWORD;

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }

    public static Integer getUserId(String login, String password) throws SQLException {
        ResultSet rs = connection.createStatement()
                .executeQuery("SELECT users.id FROM users WHERE login='%s' AND password='%s'".formatted(login, password));

        if (rs.next()) {
            return rs.getInt("id");
        }

        return null;
    }

    public static String createSessionKey(Integer userId, boolean rememberMe) throws SQLException {
        String sessionKey = UUID.randomUUID().toString();

        ResultSet rs = connection.createStatement()
                .executeQuery("INSERT INTO session_keys(id, id_user, expiration) VALUES ('%s', %d, %d)"
                        .formatted(sessionKey, userId,
                        new Date().getTime() + (rememberMe ? Config.SESSION_KEY_EXPIRATION_YEARS * 365 * 24 * 60 * 60 : 0)));

        return sessionKey;
    }
}
