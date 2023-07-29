package ru.ultrabasic.bstutp.data;

import ru.ultrabasic.bstutp.Config;
import ru.ultrabasic.bstutp.data.models.TestShort;
import ru.ultrabasic.bstutp.data.models.UserTypes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

        connection.createStatement()
                .executeUpdate("INSERT INTO session_keys(id, id_user, expiration) VALUES ('%s', %d, %d)"
                        .formatted(sessionKey, userId,
                                new Date().getTime() + Config.SESSION_KEY_EXPIRATION_YEARS * 365 * 24 * 60 * 60 * 1000));

        return sessionKey;
    }

    public static Integer getUserIdBySessionKey(String sessionKey) throws SQLException {
        ResultSet rs = connection.createStatement()
                .executeQuery("SELECT id_user, expiration FROM session_keys WHERE id='%s'"
                        .formatted(sessionKey));

        if (rs.next()) {
            Integer userId = rs.getInt("id_user");
            long expiration = rs.getLong("expiration");

            if (expiration < new Date().getTime() + Config.SESSION_KEY_EXPIRATION_YEARS * 365 * 24 * 60 * 60 * 1000) {
                // TODO: добавить ивент на очищение истечённых sessionKey в MySQL
                connection.createStatement()
                        .executeUpdate("UPDATE session_keys SET expiration = %d WHERE id='%s'"
                                .formatted(
                                        new Date().getTime() + Config.SESSION_KEY_EXPIRATION_YEARS * 365 * 24 * 60 * 60 * 1000, sessionKey));

                return userId;
            } else {
                connection.createStatement()
                        .executeUpdate("DELETE FROM session_keys WHERE id='%s'"
                                .formatted(sessionKey));
            }
        }

        return null;
    }

    public static UserTypes getUserType(int userId) throws SQLException {
        ResultSet rs = connection.createStatement()
                .executeQuery(("SELECT user_types.id FROM users INNER JOIN user_types ON user_types.id = users.user_type " +
                        "WHERE users.id='%d'").formatted(userId));

        if (rs.next())
            return UserTypes.fromID(rs.getInt("id"));

        return null;
    }

    public static double getScore(int reportId) throws SQLException {
        ResultSet rs = connection.createStatement()
                .executeQuery(("""
                        SELECT tasks.task_type, report_detailed_text.answer, report_detailed_one_in_many.id_answer, tasks_one_in_many.id_answer_correct, tasks_text.answer_correct FROM reports
                        INNER JOIN report_detailed ON report_detailed.id_report = reports.id
                        INNER JOIN tasks ON report_detailed.id_task = tasks.id
                        LEFT OUTER JOIN report_detailed_text ON report_detailed_text.id_report_detailed = report_detailed.id
                        LEFT OUTER JOIN report_detailed_one_in_many ON report_detailed_one_in_many.id_report_detailed = report_detailed.id
                        LEFT OUTER JOIN tasks_one_in_many ON tasks_one_in_many.id_task=tasks.id
                        LEFT OUTER JOIN tasks_text ON tasks_text.id_task=tasks.id
                        WHERE reports.id='%d';
                        """.formatted(reportId)
                ));

        double sum = 0;
        double size = 0;

        while (rs.next()) {
            if (rs.getInt(1) == 0) {
                Integer guess = rs.getInt(3);
                Integer ans = rs.getInt(4);
                if (ans.equals(guess))
                    sum++;
            } else if (rs.getInt(1) == 1) {
                String guess = rs.getString(2);
                String ans = rs.getString(5);
                if (ans.equals(guess))
                    sum++;
            }

            size++;
        }

        return size == 0 ? 0 : sum / size;
    }

    public static List<TestShort> getStudentsTasksCompleted(int userId) throws SQLException {
        ResultSet rs = connection.createStatement()
                .executeQuery(("""
                        SELECT tests.id, tests.name, tests.time, discipline.name, reports.id FROM users\s
                        INNER JOIN students ON users.id = students.id_user\s
                        INNER JOIN teaching_groups ON teaching_groups.id = students.id_group\s
                        INNER JOIN tests_groups ON tests_groups.id_group = students.id_group\s
                        INNER JOIN tests ON tests_groups.id_group = tests.id\s
                        INNER JOIN tests_disciplines ON tests_disciplines.id_test = tests.id\s
                        INNER JOIN discipline ON tests_disciplines.id_discipline = discipline.id\s
                        INNER JOIN reports ON tests.id = reports.id_test\s
                        WHERE users.id='%d'
                                """.formatted(userId)
                ));

        List<TestShort> tests = new ArrayList<>();
        while (rs.next()) {
            tests.add(new TestShort(rs.getInt(1), rs.getString(2), rs.getLong(3),
                    rs.getString(4), getScore(5), true));
        }

        return tests;
    }

    public static List<TestShort> getStudentsTasksActive(int userId) throws SQLException {
        ResultSet rs = connection.createStatement()
                .executeQuery(("""
                        SELECT tests.id, tests.name, tests.time, discipline.name, reports.id FROM users\s
                        INNER JOIN students ON users.id = students.id_user\s
                        INNER JOIN teaching_groups ON teaching_groups.id = students.id_group\s
                        INNER JOIN tests_groups ON tests_groups.id_group = students.id_group\s
                        INNER JOIN tests ON tests_groups.id_group = tests.id\s
                        INNER JOIN tests_disciplines ON tests_disciplines.id_test = tests.id\s
                        INNER JOIN discipline ON tests_disciplines.id_discipline = discipline.id\s
                        INNER JOIN reports ON tests.id = reports.id_test\s
                        WHERE users.id='%d' AND reports.id IS NULL'
                                """.formatted(userId)
                ));

        List<TestShort> tests = new ArrayList<>();

        while (rs.next())
            tests.add(new TestShort(rs.getInt(1), rs.getString(2), rs.getLong(3),
                    rs.getString(4), 0.0, false));

        return tests;
    }
}
