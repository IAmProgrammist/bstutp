package ru.ultrabasic.bstutp.data;

import ru.ultrabasic.bstutp.Config;
import ru.ultrabasic.bstutp.data.models.*;
import ru.ultrabasic.bstutp.data.models.TestShort;
import ru.ultrabasic.bstutp.data.models.UserInfo;
import ru.ultrabasic.bstutp.data.models.UserTypes;
import ru.ultrabasic.bstutp.data.models.DirectionsRow;
import ru.ultrabasic.bstutp.data.models.tasks.Task;
import ru.ultrabasic.bstutp.data.models.Test;
import ru.ultrabasic.bstutp.data.models.tasks.TaskTypes;
import ru.ultrabasic.bstutp.data.models.tasks.oneinmany.TaskOneInMany;
import ru.ultrabasic.bstutp.data.models.tasks.oneinmany.TaskOneInManyQuestion;
import ru.ultrabasic.bstutp.data.models.tasks.oneinmany.TaskOneInManyStudentAnswer;
import ru.ultrabasic.bstutp.data.models.tasks.text.TaskText;
import ru.ultrabasic.bstutp.data.models.tasks.text.TaskTextStudentAnswer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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
                .executeQuery("SELECT users.id FROM users WHERE login='%s' AND password='%s';".formatted(login, password));

        if (rs.next()) {
            return rs.getInt("id");
        }

        return null;
    }

    public static String createSessionKey(Integer userId, boolean rememberMe) throws SQLException {
        String sessionKey = UUID.randomUUID().toString();

        connection.createStatement()
                .executeUpdate("INSERT INTO session_keys(id, id_user, expiration) VALUES ('%s', %d, %d);"
                        .formatted(sessionKey, userId,
                                new Date().getTime() + Config.SESSION_KEY_EXPIRATION_YEARS * 365 * 24 * 60 * 60 * 1000));

        return sessionKey;
    }

    public static Integer getUserIdBySessionKey(String sessionKey) throws SQLException {
        ResultSet rs = connection.createStatement()
                .executeQuery("SELECT id_user, expiration FROM session_keys WHERE id='%s';"
                        .formatted(sessionKey));

        if (rs.next()) {
            Integer userId = rs.getInt("id_user");
            long expiration = rs.getLong("expiration");

            if (expiration < new Date().getTime() + Config.SESSION_KEY_EXPIRATION_YEARS * 365 * 24 * 60 * 60 * 1000) {
                // TODO: добавить ивент на очищение истечённых sessionKey в MySQL
                connection.createStatement()
                        .executeUpdate("UPDATE session_keys SET expiration = %d WHERE id='%s';"
                                .formatted(
                                        new Date().getTime() + Config.SESSION_KEY_EXPIRATION_YEARS * 365 * 24 * 60 * 60 * 1000, sessionKey));

                return userId;
            } else {
                connection.createStatement()
                        .executeUpdate("DELETE FROM session_keys WHERE id='%s';"
                                .formatted(sessionKey));
            }
        }

        return null;
    }

    public static UserTypes getUserType(int userId) throws SQLException {
        ResultSet rs = connection.createStatement()
                .executeQuery(("SELECT user_types.id FROM users INNER JOIN user_types ON user_types.id = users.user_type " +
                        "WHERE users.id='%d';").formatted(userId));

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
                        WHERE users.id='%d' AND tests.is_draft=0 AND %d > reports.completion_time;
                                """.formatted(userId, new Date().getTime())
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
                        SELECT tests.id, tests.name, tests.time, discipline.name FROM users
                        INNER JOIN students ON users.id = students.id_user
                        INNER JOIN teaching_groups ON teaching_groups.id = students.id_group
                        INNER JOIN tests_groups ON tests_groups.id_group = students.id_group
                        INNER JOIN tests ON tests_groups.id_group = tests.id
                        INNER JOIN tests_disciplines ON tests_disciplines.id_test = tests.id
                        INNER JOIN discipline ON tests_disciplines.id_discipline = discipline.id
                        LEFT JOIN reports ON tests.id = reports.id_test
                        WHERE users.id='%d' AND (reports.id IS NULL OR %d < reports.completion_time) AND tests.is_draft=0
                                """.formatted(userId, new Date().getTime())
                ));

        List<TestShort> tests = new ArrayList<>();

        while (rs.next())
            tests.add(new TestShort(rs.getInt(1), rs.getString(2), rs.getLong(3),
                    rs.getString(4), 0.0, false));

        return tests;
    }

    public static List<TestShort> getStudentsTasksRunning(int userId) throws SQLException {
        ResultSet rs = connection.createStatement()
                .executeQuery(("""
                        SELECT tests.id, tests.name, tests.time, discipline.name FROM users
                        INNER JOIN students ON users.id = students.id_user
                        INNER JOIN teaching_groups ON teaching_groups.id = students.id_group
                        INNER JOIN tests_groups ON tests_groups.id_group = students.id_group
                        INNER JOIN tests ON tests_groups.id_group = tests.id
                        INNER JOIN tests_disciplines ON tests_disciplines.id_test = tests.id
                        INNER JOIN discipline ON tests_disciplines.id_discipline = discipline.id
                        INNER JOIN reports ON tests.id = reports.id_test
                        WHERE users.id='%d' AND %d < reports.completion_time AND tests.is_draft=0
                                """.formatted(userId, new Date().getTime())
                ));

        List<TestShort> tests = new ArrayList<>();

        while (rs.next())
            tests.add(new TestShort(rs.getInt(1), rs.getString(2), rs.getLong(3),
                    rs.getString(4), 0.0, false));

        return tests;
    }

    public static List<TestShort> getTeacherTasksActive(int userId) throws SQLException {
        ResultSet rs = connection.createStatement()
                .executeQuery(("""
                        SELECT tests.id, tests.name, tests.time, discipline.name FROM users\s
                        INNER JOIN teacher ON teacher.id_user = users.id\s
                        INNER JOIN tests ON tests.id_owner = users.id\s
                        INNER JOIN tests_disciplines ON tests_disciplines.id_test = tests.id\s
                        INNER JOIN discipline ON tests_disciplines.id_discipline = discipline.id\s
                        WHERE users.id='%d' AND tests.is_draft = 0
                                """.formatted(userId)
                ));

        List<TestShort> tests = new ArrayList<>();

        while (rs.next())
            tests.add(new TestShort(rs.getInt(1), rs.getString(2), rs.getLong(3),
                    rs.getString(4), 0.0, false));

        return tests;
    }

    public static List<TestShort> getTeacherTasksDraft(int userId) throws SQLException {
        ResultSet rs = connection.createStatement()
                .executeQuery(("""
                        SELECT tests.id, tests.name, tests.time, discipline.name FROM users\s
                        INNER JOIN teacher ON teacher.id_user = users.id\s
                        INNER JOIN tests ON tests.id_owner = users.id\s
                        INNER JOIN tests_disciplines ON tests_disciplines.id_test = tests.id\s
                        INNER JOIN discipline ON tests_disciplines.id_discipline = discipline.id\s
                        WHERE users.id='%d' AND tests.is_draft = 1
                                """.formatted(userId)
                ));

        List<TestShort> tests = new ArrayList<>();

        while (rs.next())
            tests.add(new TestShort(rs.getInt(1), rs.getString(2), rs.getLong(3),
                    rs.getString(4), 0.0, false));

        return tests;
    }

    public static TestState getState(int userId, int testId) throws SQLException {
        Optional<TestShort> searchRes = getStudentsTasksRunning(userId).stream().filter(el -> el.id == testId).findFirst();
        if (searchRes.isPresent())
            return TestState.RUNNING;

        searchRes = getStudentsTasksActive(userId).stream().filter(el -> el.id == testId).findFirst();
        if (searchRes.isPresent())
            return TestState.AVAILABLE;

        searchRes = getStudentsTasksCompleted(userId).stream().filter(el -> el.id == testId).findFirst();
        if (searchRes.isPresent())
            return TestState.COMPLETED;

        return null;
    }

    public static TestShort getTestShort(int testId) throws SQLException {
        ResultSet rs = connection.createStatement()
                .executeQuery(("""
                        SELECT tests.id, tests.name, tests.time, discipline.name FROM tests\s
                        INNER JOIN tests_disciplines ON tests_disciplines.id_test = tests.id\s
                        INNER JOIN discipline ON tests_disciplines.id_discipline = discipline.id\s
                        WHERE tests.id='%d'
                                """.formatted(testId)
                ));

        if (rs.next())
            return new TestShort(rs.getInt(1), rs.getString(2), rs.getLong(3),
                    rs.getString(4), 0.0, false);

        return null;
    }

    public static boolean startTest(int userId, int testId) throws SQLException {
        TestShort test = getTestShort(testId);

        if (test == null)
            return false;

        connection.createStatement()
                .executeUpdate(("""
                        INSERT INTO reports(id_student, id_test, completion_time) VALUES (%d, %d, %d)
                                """.formatted(userId, testId, test.duration * 1000 + new Date().getTime())));

        int reportId = getLastInsertId();
        Test questionsOnlyTasks = getTestQuestionsOnly(testId);

        for (Task task : questionsOnlyTasks.tasks) {
            connection.createStatement()
                    .executeUpdate(("""
                            INSERT INTO report_detailed(id_report, id_task) VALUES (%d, %d)
                                    """.formatted(reportId, task.id)));
            int reportDetailedId = getLastInsertId();
            if (task.taskType == TaskTypes.ONE_IN_MANY)
                connection.createStatement()
                        .executeUpdate(("""
                                INSERT INTO report_detailed_one_in_many(id_report_detailed, id_answer) VALUES (%d, NULL)
                                        """.formatted(reportDetailedId)));
            else if (task.taskType == TaskTypes.TEXT)
                connection.createStatement()
                        .executeUpdate(("""
                                INSERT INTO report_detailed_text(id_report_detailed, answer) VALUES (%d, NULL)
                                        """.formatted(reportDetailedId)));
        }

        return true;
    }

    public static Test getTestQuestionsOnly(int idTest) throws SQLException {
        ResultSet testsRequest = connection.createStatement()
                .executeQuery(("""
                        SELECT tests.id, tests.time, tests.is_draft, tests.id_owner, tests.name, discipline.name FROM tests
                        INNER JOIN tests_disciplines ON tests_disciplines.id_test = tests.id
                        INNER JOIN discipline ON discipline.id = tests_disciplines.id_discipline
                        WHERE tests.id='%d'
                                """.formatted(idTest)));

        if (testsRequest.next()) {
            ResultSet tasksRequest = connection.createStatement()
                    .executeQuery(("""
                            SELECT tasks.task_type, tasks.id, tasks.order, tasks.description, tasks.id_owner FROM tests
                            INNER JOIN tasks ON tasks.id_test = tests.id
                            LEFT JOIN tasks_text ON tasks_text.id_task = tasks.id
                            LEFT JOIN tasks_one_in_many ON tasks_one_in_many.id_task = tasks.id
                            WHERE tests.id='%d' ORDER BY tasks.order
                                    """.formatted(idTest)));

            List<Task> tasks = new ArrayList<>();

            while (tasksRequest.next()) {
                TaskTypes tType = TaskTypes.fromID(tasksRequest.getInt(1));

                if (tType == null)
                    continue;

                switch (tType) {
                    case ONE_IN_MANY:
                        List<TaskOneInManyQuestion> questions = new ArrayList<>();

                        ResultSet oneInManyQuestions = connection.createStatement()
                                .executeQuery(("""
                                            SELECT tasks_one_in_many_questions_bank.id, tasks_one_in_many_questions_bank.text FROM tasks
                                            INNER JOIN tasks_one_in_many_task_questions ON tasks_one_in_many_task_questions.id_task = tasks.id
                                            INNER JOIN tasks_one_in_many_questions_bank ON tasks_one_in_many_questions_bank.id = tasks_one_in_many_task_questions.id_question
                                            WHERE tasks.id='%d'
                                        """.formatted(tasksRequest.getInt(2))));

                        while (oneInManyQuestions.next())
                            questions.add(new TaskOneInManyQuestion(oneInManyQuestions.getInt(1),
                                    oneInManyQuestions.getString(2)));

                        tasks.add(new TaskOneInMany(tasksRequest.getInt(2), tasksRequest.getInt(3),
                                tasksRequest.getString(4), tasksRequest.getInt(5), questions));

                        break;
                    case TEXT:
                        tasks.add(new TaskText(tasksRequest.getInt(2), tasksRequest.getInt(3),
                                tasksRequest.getString(4), tasksRequest.getInt(5)));
                        break;
                }
            }

            return new Test(idTest, testsRequest.getLong(2), testsRequest.getString(6),
                    testsRequest.getBoolean(3), testsRequest.getInt(4),
                    testsRequest.getString(5), tasks, 0.0, false);
        }

        return null;
    }

    public static Report getReportNoAnswers(int idTest, int userId) throws SQLException {
        ResultSet testsRequest = connection.createStatement()
                .executeQuery(("""
                        SELECT tests.id, tests.time, tests.is_draft, tests.id_owner, tests.name, discipline.name, reports.id, reports.completion_time FROM tests
                        INNER JOIN tests_disciplines ON tests_disciplines.id_test = tests.id
                        INNER JOIN discipline ON discipline.id = tests_disciplines.id_discipline
                        INNER JOIN reports ON reports.id_test = tests.id
                        WHERE tests.id='%d' AND reports.id_student='%d'
                                """.formatted(idTest, userId)));

        if (testsRequest.next()) {
            ResultSet tasksRequest = connection.createStatement()
                    .executeQuery(("""
                            SELECT tasks.task_type, tasks.id, tasks.order, tasks.description, tasks.id_owner,
                            report_detailed_text.answer, report_detailed_one_in_many.id_answer, report_detailed.id FROM report_detailed
                            INNER JOIN tasks ON tasks.id = report_detailed.id_task
                            LEFT JOIN tasks_text ON tasks_text.id_task = tasks.id
                            LEFT JOIN tasks_one_in_many ON tasks_one_in_many.id_task = tasks.id
                            LEFT JOIN report_detailed_text ON report_detailed_text.id_report_detailed = report_detailed.id
                            LEFT JOIN report_detailed_one_in_many ON report_detailed_one_in_many.id_report_detailed = report_detailed.id
                            WHERE report_detailed.id_report='%d' ORDER BY tasks.order
                                    """.formatted(testsRequest.getInt(7))));

            List<Task> tasks = new ArrayList<>();

            while (tasksRequest.next()) {
                TaskTypes tType = TaskTypes.fromID(tasksRequest.getInt(1));

                if (tType == null)
                    continue;

                switch (tType) {
                    case ONE_IN_MANY:
                        List<TaskOneInManyQuestion> questions = new ArrayList<>();
                        TaskOneInManyQuestion chosen = null;

                        ResultSet oneInManyQuestions = connection.createStatement()
                                .executeQuery(("""
                                            SELECT tasks_one_in_many_questions_bank.id, tasks_one_in_many_questions_bank.text FROM tasks
                                            INNER JOIN tasks_one_in_many_task_questions ON tasks_one_in_many_task_questions.id_task = tasks.id
                                            INNER JOIN tasks_one_in_many_questions_bank ON tasks_one_in_many_questions_bank.id = tasks_one_in_many_task_questions.id_question
                                            WHERE tasks.id='%d'
                                        """.formatted(tasksRequest.getInt(2))));

                        while (oneInManyQuestions.next()) {
                            TaskOneInManyQuestion question = new TaskOneInManyQuestion(oneInManyQuestions.getInt(1),
                                    oneInManyQuestions.getString(2));

                            if (tasksRequest.getInt(7) == question.id())
                                chosen = question;

                            questions.add(question);
                        }

                        tasks.add(new TaskOneInManyStudentAnswer(tasksRequest.getInt(2), tasksRequest.getInt(3),
                                tasksRequest.getString(4), tasksRequest.getInt(5), questions, chosen, tasksRequest.getInt(8)));

                        break;
                    case TEXT:
                        tasks.add(new TaskTextStudentAnswer(tasksRequest.getInt(2), tasksRequest.getInt(3),
                                tasksRequest.getString(4), tasksRequest.getInt(5), tasksRequest.getString(6),
                                tasksRequest.getInt(8)));
                        break;
                }
            }

            return new Report(idTest, testsRequest.getLong(2), testsRequest.getString(6),
                    testsRequest.getBoolean(3), testsRequest.getInt(4),
                    testsRequest.getString(5), tasks, 0.0, false, testsRequest.getInt(7), testsRequest.getLong(8));
        }

        return null;
    }

    public ArrayList<String> getTaskQuestionsOneInMany(int idTask) throws SQLException {
        ResultSet id_questions = connection.createStatement().executeQuery(
                "SELECT id_question FROM tasks_one_in_many_task_questions WHERE id_task=%d;"
                        .formatted(idTask));

        ArrayList<String> taskQuestions = new ArrayList<>(4);
        while (id_questions.next()) {
            ResultSet text = connection.createStatement().executeQuery(
                    "SELECT text FROM tasks_one_in_many_questions_bank WHERE id=%d LIMIT 1;"
                            .formatted(id_questions.getInt(1)));
            text.next();
            taskQuestions.add(text.getString(1));
        }

        return taskQuestions;
    }

    public String getAnswerOneInMany(int idTask) throws SQLException {
        ResultSet id_answer_correct = connection.createStatement().executeQuery(
                "SELECT id_answer_correct FROM tasks_one_in_many WHERE id=%d LIMIT 1;"
                        .formatted(idTask));
        id_answer_correct.next();

        ResultSet answer_correct = connection.createStatement().executeQuery(
                "SELECT text FROM tasks_one_in_many_questions_bank WHERE id=%d LIMIT 1;"
                        .formatted(id_answer_correct.getInt(1)));
        answer_correct.next();

        return answer_correct.getString(1);
    }

    public String getAnswerText(int idTask) throws SQLException {
        ResultSet answer_correct = connection.createStatement().executeQuery(
                "SELECT answer_correct FROM tasks_text WHERE id=%d LIMIT 1;"
                        .formatted(idTask));
        answer_correct.next();

        return answer_correct.getString(1);
    }

    private static int getLastInsertId() throws SQLException {
        return getOneRowExecuteQuery("SELECT LAST_INSERT_ID();").getInt(1);
    }

    private static ResultSet getOneRowExecuteQuery(String sql) throws SQLException {
        ResultSet rs = connection.createStatement().executeQuery(sql);
        rs.next();

        return rs;
    }

    private void statementExecute(String sql) throws SQLException {
        connection.createStatement().execute(sql);
    }

    private ResultSet statementExecuteQuery(String sql) throws SQLException {
        return connection.createStatement().executeQuery(sql);
    }

    public void addDirection(DirectionsRow direction, int idLevelType, int idEducationalPrograms) throws SQLException {
        statementExecute(
                ("INSERT INTO directions (id_level, espf_code, espf_name, code, name, id_educational_program) " +
                        "VALUES (%d, '%s', '%s','%s','%s', %d);").formatted(
                        idLevelType, direction.getEspfCode(), direction.getEspfName(),
                        direction.getCode(), direction.getName(), idEducationalPrograms)
        );
    }

    public void delDirection(int id_direction) throws SQLException {
        statementExecute("DELETE FROM directions WHERE id=%d;".formatted(id_direction));
    }

    public void updateDirectionAll(int idDirections, DirectionsRow direction, int idLevelType, int idEducationalPrograms) throws SQLException {
        statementExecute(("UPDATE directions SET id_level=%d, espf_code='%s', espf_name='%s', code='%s', name='%s', " +
                "id_educational_program=%d WHERE id=%d;").formatted(idLevelType, direction.getEspfCode(),
                direction.getEspfName(), direction.getCode(), direction.getName(), idEducationalPrograms, idDirections));
    }

    public void updateDirectionIdEducationalProgram(int idDirections, DirectionsRow direction, int idEducationalProgram) throws SQLException {
        statementExecute(("UPDATE directions SET espf_code='%s', espf_name='%s', code='%s', name='%s', " +
                "id_educational_program=%d WHERE id=%d;").formatted(direction.getEspfCode(),
                direction.getEspfName(), direction.getCode(), direction.getName(), idEducationalProgram, idDirections));
    }

    public void updateDirectionIdLevelType(int idDirections, DirectionsRow direction, int idLevelType) throws SQLException {
        statementExecute(("UPDATE directions SET id_level=%d, espf_code='%s', espf_name='%s', code='%s', name='%s', " +
                "WHERE id=%d;").formatted(idLevelType, direction.getEspfCode(),
                direction.getEspfName(), direction.getCode(), direction.getName(), idDirections));
    }

    public void updateDirection(int idDirections, DirectionsRow direction) throws SQLException {
        statementExecute(("UPDATE directions SET espf_code='%s', espf_name='%s', code='%s', name='%s', WHERE id=%d;")
                .formatted(direction.getEspfCode(), direction.getEspfName(), direction.getCode(), direction.getName(),
                        idDirections));
    }

    public DirectionsRow getDirection(int idDirection) throws SQLException {
        ResultSet direction = statementExecuteQuery("SELECT * FROM directions WHERE id=%d LIMIT 1;"
                .formatted(idDirection));
        return new DirectionsRow(
                direction.getString("espf_code"),
                direction.getString("espf_name"),
                direction.getString("code"),
                direction.getString("name"),
                direction.getInt("id_level"),
                direction.getInt("id_educational_program"));
    }

    public String getNameEducationalProgram(int idEducationalProgram) throws SQLException {
        return statementExecuteQuery("SELECT * FROM educational_programs WHERE id=%d LIMIT 1;"
                .formatted(idEducationalProgram)).getString("name");
    }

    public String getLevel(int idLevel) throws SQLException {
        return statementExecuteQuery("SELECT * FROM levels WHERE id=%d LIMIT 1;"
                .formatted(idLevel)).getString("type");
    }

//    public void addTeacher(int login, int password) throws SQLException {
//        connection.createStatement().execute(
//                "INSERT INTO users (login, password, user_type) VALUES ('%s', '%s', 1)"
//                        .formatted(login, password)
//        );
//
//        int id_new_user = getLastInsertId();
//
//        connection.createStatement().execute(
//                "INSERT INTO users (login, password, user_type) VALUES ('%s', '%s', 1)"
//                        .formatted(login, password)
//        );
//    }

    public static UserInfo getUserInfo(int userId) throws SQLException {
        ResultSet rs = connection.createStatement()
                .executeQuery(("""
                        SELECT name, surname, patronymic, user_type FROM users WHERE id=%d
                                """.formatted(userId)
                ));

        if (rs.next())
            return new UserInfo(userId, rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4));

        return null;
    }
}
