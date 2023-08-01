package ru.ultrabasic.bstutp.data;

import ru.ultrabasic.bstutp.Config;
import ru.ultrabasic.bstutp.data.models.*;
import ru.ultrabasic.bstutp.data.models.records.Group;
import ru.ultrabasic.bstutp.data.models.records.Indicator;
import ru.ultrabasic.bstutp.data.models.records.*;
import ru.ultrabasic.bstutp.data.models.tasks.Task;
import ru.ultrabasic.bstutp.data.models.tasks.TaskTypes;
import ru.ultrabasic.bstutp.data.models.tasks.oneinmany.*;
import ru.ultrabasic.bstutp.data.models.tasks.text.TaskText;
import ru.ultrabasic.bstutp.data.models.tasks.text.TaskTextStudentAnswer;
import ru.ultrabasic.bstutp.data.models.tasks.text.TaskTextStudentAnswerWithCorrect;
import ru.ultrabasic.bstutp.data.models.records.Discipline;
import ru.ultrabasic.bstutp.data.models.tasks.text.TaskTextTeacherEdit;
import ru.ultrabasic.bstutp.data.models.tests.Report;
import ru.ultrabasic.bstutp.data.models.tests.TeacherEditableTest;
import ru.ultrabasic.bstutp.data.models.tests.Test;
import ru.ultrabasic.bstutp.messages.errors.DatabaseError;

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


    public void delUser(int idUser) throws SQLException {
        UserTypes userType = userType(idUser);
        if (userType == UserTypes.STUDENT) {
            statementExecute("DELETE FROM students WHERE id=%d".formatted(idUser));
        } else if (userType == UserTypes.TEACHER) {
            statementExecute("DELETE FROM teacher WHERE id=%d".formatted(idUser));
        } else if (userType == UserTypes.ADMIN) {
            statementExecute("DELETE FROM admins WHERE id=%d".formatted(idUser));
        }
        statementExecute("DELETE FROM users WHERE id=%d".formatted(idUser));
    }


    public static Integer getUserId(String login, String password) throws SQLException {
        ResultSet rs = connection.createStatement()
                .executeQuery("SELECT users.id FROM users WHERE login='%s' AND password='%s';".formatted(login, password));

        if (rs.next()) {
            return rs.getInt("id");
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

    public static UserTypes userType(int userId) throws SQLException {
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
                        INNER JOIN tests ON tests_groups.id_test = tests.id\s
                        INNER JOIN tests_disciplines ON tests_disciplines.id_test = tests.id\s
                        INNER JOIN discipline ON tests_disciplines.id_discipline = discipline.id\s
                        INNER JOIN reports ON tests.id = reports.id_test\s
                        WHERE users.id='%d' AND tests.is_draft=0 AND %d > reports.completion_time;
                                """.formatted(userId, new Date().getTime())
                ));

        List<TestShort> tests = new ArrayList<>();
        while (rs.next()) {
            tests.add(new TestShort(rs.getInt(1), rs.getString(2), rs.getLong(3),
                    rs.getString(4), getScore(rs.getInt(5)), true));
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
                        INNER JOIN tests ON tests_groups.id_test = tests.id
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
                        INNER JOIN tests ON tests_groups.id_test = tests.id
                        INNER JOIN tests_disciplines ON tests_disciplines.id_test = tests.id
                        INNER JOIN discipline ON tests_disciplines.id_discipline = discipline.id
                        LEFT JOIN reports ON tests.id = reports.id_test
                        WHERE users.id='%d' AND %d < reports.completion_time AND tests.is_draft=0
                                """.formatted(userId, new Date().getTime())
                ));

        List<TestShort> tests = new ArrayList<>();

        while (rs.next())
            tests.add(new TestShort(rs.getInt(1), rs.getString(2), rs.getLong(3),
                    rs.getString(4), 0.0, false));

        return tests;
    }

    public static List<TestShort> getTeacherTestsActive(int userId) throws SQLException {
        ResultSet rs = connection.createStatement()
                .executeQuery(("""
                        SELECT tests.id, tests.name, tests.time, discipline.name FROM users\s
                        INNER JOIN teacher ON teacher.id_user = users.id\s
                        INNER JOIN tests ON tests.id_owner = users.id\s
                        INNER JOIN tests_disciplines ON tests_disciplines.id_test = tests.id\s
                        INNER JOIN discipline ON tests_disciplines.id_discipline = discipline.id\s
                        WHERE users.id='%d' AND tests.is_draft = 0;
                                """.formatted(userId)
                ));

        List<TestShort> tests = new ArrayList<>();

        while (rs.next())
            tests.add(new TestShort(rs.getInt(1), rs.getString(2), rs.getLong(3),
                    rs.getString(4), 0.0, false));

        return tests;
    }

    public static List<TestShort> getTeacherTestsDraft(int userId) throws SQLException {
        ResultSet rs = connection.createStatement()
                .executeQuery(("""
                        SELECT tests.id, tests.name, tests.time, discipline.name FROM users\s
                        INNER JOIN teacher ON teacher.id_user = users.id\s
                        INNER JOIN tests ON tests.id_owner = users.id\s
                        LEFT JOIN tests_disciplines ON tests_disciplines.id_test = tests.id\s
                        LEFT JOIN discipline ON tests_disciplines.id_discipline = discipline.id\s
                        WHERE users.id='%d' AND tests.is_draft = 1;
                                """.formatted(userId)
                ));

        List<TestShort> tests = new ArrayList<>();

        while (rs.next())
            tests.add(new TestShort(rs.getInt(1), rs.getString(2), rs.getLong(3),
                    rs.getString(4) == null ? "" : rs.getString(4), 0.0, false));

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

        searchRes = getTeacherTestsDraft(userId).stream().filter(el -> el.id == testId).findFirst();
        if (searchRes.isPresent())
            return TestState.DRAFT;

        searchRes = getTeacherTestsActive(userId).stream().filter(el -> el.id == testId).findFirst();
        if (searchRes.isPresent())
            return TestState.ACTIVE;

        return TestState.NOT_AVAILABLE;
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

    public static void finishTest(int userId, int testId) throws SQLException {
        TestShort test = getTestShort(testId);

        if (test == null)
            return;

        connection.createStatement()
                .executeUpdate(("""
                        UPDATE reports SET completion_time = %d WHERE id_student=%d AND id_test=%d
                                """.formatted(new Date().getTime(), userId, testId)));
    }


    public static void releaseTest(int testId) throws SQLException {
        connection.createStatement()
                .executeUpdate(("""
                        UPDATE tests SET is_draft = 0 WHERE tests.id=%d;
                                """.formatted(testId)));
    }

    public static void updateTestHeader(int testId, String name, Long duration, Integer idDiscipline, List<Integer> groups) throws SQLException {
        connection.createStatement()
                .executeUpdate(("""
                        UPDATE tests SET name = '%s', time = %d WHERE tests.id=%d
                                """.formatted(name, duration, testId)));

        connection.createStatement()
                .executeUpdate(("""
                        DELETE FROM tests_disciplines WHERE tests_disciplines.id_test=%d
                                """.formatted(testId)));

        if (idDiscipline != null)
            connection.createStatement()
                    .executeUpdate(("""
                            INSERT INTO tests_disciplines(id_test, id_discipline) VALUES (%d, %d)
                                    """.formatted(testId, idDiscipline)));
        else
            connection.createStatement()
                    .executeUpdate(("""
                            INSERT INTO tests_disciplines(id_test, id_discipline) VALUES (%d, NULL)
                                    """.formatted(testId)));

        connection.createStatement()
                .executeUpdate(("""
                        DELETE FROM tests_groups WHERE tests_groups.id_test=%d
                                """.formatted(testId)));

        for (Integer g : groups)
            connection.createStatement()
                    .executeUpdate(("""
                            INSERT INTO tests_groups(id_test, id_group) VALUES (%d, %d)
                                    """.formatted(testId, g)));
    }

    public static void addTextTask(int testId, int owner) throws SQLException {
        connection.createStatement()
                .executeUpdate(("""
                        INSERT INTO tasks(tasks.id_test, tasks.order, tasks.task_type, tasks.description, tasks.id_owner) VALUES (%d, 0, 1, "", %d)
                                """.formatted(testId, owner)));
        int taskId = getLastInsertId();


        connection.createStatement()
                .executeUpdate(("""
                        INSERT INTO tasks_text(id_task, answer_correct) VALUES (%d, "")
                                """.formatted(taskId)));
    }

    public static int newTest(int userId) throws SQLException {
        connection.createStatement()
                .executeUpdate(("""
                        INSERT INTO tests(tests.time, tests.is_draft, tests.id_owner, tests.name) VALUES (900, 1, %d, 'Новый тест')
                                """.formatted(userId)));
        int testId = getLastInsertId();

        return testId;
    }

    public static void updateTextTask(int taskId, String description, String answerCorrect, List<Integer> indicators) throws SQLException {
        connection.createStatement()
                .executeUpdate(("""
                        UPDATE tasks SET description = '%s' WHERE tasks.id=%d;
                                """.formatted(description, taskId)));

        connection.createStatement()
                .executeUpdate(("""
                        UPDATE tasks_text SET answer_correct = '%s' WHERE tasks_text.id_task=%d;
                                """.formatted(answerCorrect, taskId)));

        connection.createStatement()
                .executeUpdate(("""
                        DELETE FROM tasks_indicators WHERE tasks_indicators.id_task=%d;
                                """.formatted(taskId)));

        for (Integer indicator : indicators) {
            connection.createStatement()
                    .executeUpdate(("""
                            INSERT INTO tasks_indicators(id_task, id_indicator) VALUES (%d, %d)
                                    """.formatted(taskId, indicator)));
        }
    }

    public static void updateOneInManyTaskQuestion(int qId, String text) throws SQLException {
        connection.createStatement()
                .executeUpdate(("""
                        UPDATE tasks_one_in_many_questions_bank SET text = '%s' WHERE tasks_one_in_many_questions_bank.id=%d;
                                """.formatted(text, qId)));

    }

    public static void updateOneInManyTask(int taskId, String description, Integer idAnswerCorrect, List<Integer> indicators) throws SQLException {
        connection.createStatement()
                .executeUpdate(("""
                        UPDATE tasks SET description = '%s' WHERE tasks.id=%d;
                                """.formatted(description, taskId)));

        if (idAnswerCorrect != null)
            connection.createStatement()
                    .executeUpdate(("""
                            UPDATE tasks_one_in_many SET id_answer_correct = '%d' WHERE tasks_one_in_many.id_task=%d;
                                    """.formatted(idAnswerCorrect, taskId)));
        else
            connection.createStatement()
                    .executeUpdate(("""
                            UPDATE tasks_one_in_many SET id_answer_correct = NULL WHERE tasks_one_in_many.id_task=%d;
                                    """.formatted(taskId)));

        connection.createStatement()
                .executeUpdate(("""
                        DELETE FROM tasks_indicators WHERE tasks_indicators.id_task=%d;
                                """.formatted(taskId)));

        for (Integer indicator : indicators) {
            connection.createStatement()
                    .executeUpdate(("""
                            INSERT INTO tasks_indicators(id_task, id_indicator) VALUES (%d, %d)
                                    """.formatted(taskId, indicator)));
        }
    }

    public static void addOneInManyTask(int testId, int owner) throws SQLException {
        connection.createStatement()
                .executeUpdate(("""
                        INSERT INTO tasks(tasks.id_test, tasks.order, tasks.task_type, tasks.description, tasks.id_owner) VALUES (%d, 0, 0, "", %d)
                                """.formatted(testId, owner)));
        int taskId = getLastInsertId();


        connection.createStatement()
                .executeUpdate(("""
                        INSERT INTO tasks_one_in_many(id_task, id_answer_correct) VALUES (%d, NULL)
                                """.formatted(taskId)));
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

    public static boolean isTestRunningFromIdDetailedReport(int idDetailedReport, int userId) throws SQLException {
        ResultSet testsRequest = connection.createStatement()
                .executeQuery(("""
                        SELECT reports.completion_time FROM report_detailed
                        INNER JOIN reports ON reports.id = report_detailed.id_report
                        INNER JOIN users ON users.id = reports.id_student
                        WHERE report_detailed.id = %d AND reports.completion_time > %d AND users.id=%d
                                """.formatted(idDetailedReport, new Date().getTime(), userId)));

        return testsRequest.next();
    }

    public static void setAnswerText(int idDetailedReport, String answer) throws SQLException {
        if (answer == null)
            connection.createStatement()
                    .executeUpdate("""
                            UPDATE report_detailed_text SET answer = NULL WHERE id_report_detailed = '%d'
                                    """.formatted(idDetailedReport));
        else
            connection.createStatement()
                    .executeUpdate("""
                            UPDATE report_detailed_text SET answer = '%s' WHERE id_report_detailed = '%d'
                                    """.formatted(answer, idDetailedReport));
    }

    public static void setAnswerOneInMany(int idDetailedReport, Integer idAnswer) throws SQLException {
        if (idAnswer != null)
            connection.createStatement()
                    .executeUpdate("""
                            UPDATE report_detailed_one_in_many SET id_answer = '%d' WHERE id_report_detailed = '%d'
                                    """.formatted(idAnswer, idDetailedReport));
        else
            connection.createStatement()
                    .executeUpdate("""
                            UPDATE report_detailed_one_in_many SET id_answer = NULL WHERE id_report_detailed = '%d'
                                    """.formatted(idDetailedReport));
    }

    public static Report getReportWithAnswers(int idTest, int userId) throws SQLException {
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
                            report_detailed_text.answer, report_detailed_one_in_many.id_answer, report_detailed.id,
                            tasks_one_in_many.id_answer_correct, tasks_text.answer_correct, tasks_one_in_many_questions_bank.text FROM report_detailed
                            INNER JOIN tasks ON tasks.id = report_detailed.id_task
                            LEFT JOIN tasks_text ON tasks_text.id_task = tasks.id
                            LEFT JOIN tasks_one_in_many ON tasks_one_in_many.id_task = tasks.id
                            LEFT JOIN report_detailed_text ON report_detailed_text.id_report_detailed = report_detailed.id
                            LEFT JOIN report_detailed_one_in_many ON report_detailed_one_in_many.id_report_detailed = report_detailed.id
                            LEFT JOIN tasks_one_in_many_questions_bank ON tasks_one_in_many_questions_bank.id = tasks_one_in_many.id_answer_correct
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

                        tasks.add(new TaskOneInManyStudentAnswerWithCorrect(tasksRequest.getInt(2), tasksRequest.getInt(3),
                                tasksRequest.getString(4), tasksRequest.getInt(5), questions, chosen, tasksRequest.getInt(8),
                                new TaskOneInManyQuestion(tasksRequest.getInt(9), tasksRequest.getString(11))));

                        break;
                    case TEXT:
                        tasks.add(new TaskTextStudentAnswerWithCorrect(tasksRequest.getInt(2), tasksRequest.getInt(3),
                                tasksRequest.getString(4), tasksRequest.getInt(5), tasksRequest.getString(6),
                                tasksRequest.getInt(8), tasksRequest.getString(10)));
                        break;
                }
            }

            return new Report(idTest, testsRequest.getLong(2), testsRequest.getString(6),
                    testsRequest.getBoolean(3), testsRequest.getInt(4),
                    testsRequest.getString(5), tasks, getScore(testsRequest.getInt(7)),
                    false, testsRequest.getInt(7), testsRequest.getLong(8));
        }

        return null;
    }

    public static TeacherEditableTest getTestTeacherDraft(int testId, int userId) throws SQLException {
        //List<Task> tasks
        TeacherEditableTest test = null;
        ResultSet testRequest = connection.createStatement()
                .executeQuery(("""
                        SELECT tests.id, tests.time, tests.is_draft, tests.id_owner, tests.name FROM tests
                        LEFT JOIN tests_disciplines ON tests_disciplines.id_test = tests.id
                        LEFT JOIN discipline ON discipline.id = tests_disciplines.id_discipline
                        WHERE tests.id_owner='%d' AND tests.id='%d'
                                """.formatted(userId, testId)));
        if (testRequest.next()) {
            List<Discipline> disciplines = getDisciplinesNoComps();
            List<Group> allGroups = getGroups();

            Discipline discipline = null;
            List<Group> groups = new ArrayList<>();

            ResultSet groupsRequest = connection.createStatement()
                    .executeQuery(("""
                            SELECT teaching_groups.id, teaching_groups.name, teaching_groups.id_direction FROM teaching_groups
                            INNER JOIN tests_groups ON tests_groups.id_group = teaching_groups.id
                            WHERE tests_groups.id_test=%d
                                    """.formatted(testId)));

            while (groupsRequest.next())
                groups.add(new Group(groupsRequest.getInt(1), groupsRequest.getString(2), groupsRequest.getInt(3)));

            ResultSet disciplineRequest = connection.createStatement()
                    .executeQuery(("""
                            SELECT discipline.id, discipline.name FROM discipline
                            INNER JOIN tests_disciplines ON tests_disciplines.id_discipline = discipline.id
                            WHERE tests_disciplines.id_test=%d
                                    """.formatted(testId)));

            if (disciplineRequest.next()) {
                discipline = new Discipline(disciplineRequest.getInt("id"), disciplineRequest.getString("name"), null);
            }

            ResultSet tasksRequest = connection.createStatement()
                    .executeQuery(("""
                            SELECT tasks.task_type, tasks.id, tasks.order, tasks.description, tasks.id_owner,
                            tasks_one_in_many.id_answer_correct, tasks_text.answer_correct FROM tasks
                            LEFT JOIN tasks_text ON tasks_text.id_task = tasks.id
                            LEFT JOIN tasks_one_in_many ON tasks_one_in_many.id_task = tasks.id
                            WHERE tasks.id_test=%d ORDER BY tasks.order
                                    """.formatted(testId)));

            List<Task> tasks = new ArrayList<>();

            while (tasksRequest.next()) {
                List<Indicator> indicators = new ArrayList<>();
                ResultSet indicatorsRequest = connection.createStatement()
                        .executeQuery(("""
                                SELECT indicators.id, indicators.name, indicators.sub_id, indicators.id_competence FROM indicators
                                INNER JOIN tasks_indicators ON tasks_indicators.id_indicator = indicators.id
                                WHERE tasks_indicators.id_task=%d
                                        """.formatted(testId)));

                while (indicatorsRequest.next()) {
                    indicators.add(new Indicator(indicatorsRequest.getInt(1), indicatorsRequest.getInt(3),
                            indicatorsRequest.getString(2), indicatorsRequest.getInt(4)));
                }

                TaskTypes tType = TaskTypes.fromID(tasksRequest.getInt(1));

                if (tType == null)
                    continue;

                switch (tType) {
                    case ONE_IN_MANY:
                        Integer answerIdCorrect = tasksRequest.getInt(6);

                        List<TaskOneInManyQuestion> questions = new ArrayList<>();

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

                            questions.add(question);
                        }

                        tasks.add(new TaskOneInManyTeacherEdit(tasksRequest.getInt(2), tasksRequest.getInt(3),
                                tasksRequest.getString(4), tasksRequest.getInt(5), questions,
                                questions.stream().filter(el -> el.id() == answerIdCorrect).findFirst().orElseGet(() -> null), indicators));

                        break;
                    case TEXT:
                        String answerCorrect = tasksRequest.getString(7);
                        tasks.add(new TaskTextTeacherEdit(tasksRequest.getInt(2), tasksRequest.getInt(3),
                                tasksRequest.getString(4), tasksRequest.getInt(5), answerCorrect, indicators));
                        break;
                }
            }

            return new TeacherEditableTest(testRequest.getInt(1), testRequest.getLong(2), disciplines,
                    discipline, testRequest.getBoolean(3), testRequest.getInt(4), testRequest.getString(5),
                    tasks, groups, allGroups, getIndicators());
        }

        return null;
    }

    public static List<Discipline> getDisciplinesNoComps() throws SQLException {
        ResultSet disciplineRequest = connection.createStatement()
                .executeQuery(("""
                        SELECT * FROM discipline;
                                """));

        List<Discipline> list = new ArrayList<>();
        while (disciplineRequest.next()) {
            list.add(new Discipline(disciplineRequest.getInt("id"), disciplineRequest.getString("name"), null));
        }

        return list;
    }

    public static List<Group> getGroups() throws SQLException {
        ResultSet groupsRequest = connection.createStatement()
                .executeQuery(("""
                        SELECT * FROM teaching_groups;
                                """));

        List<Group> list = new ArrayList<>();
        while (groupsRequest.next()) {
            list.add(new Group(groupsRequest.getInt("id"), groupsRequest.getString("name"),
                    groupsRequest.getInt("id_direction")));
        }

        return list;
    }

    public static List<Indicator> getIndicators() throws SQLException {
        ResultSet groupsRequest = connection.createStatement()
                .executeQuery(("""
                        SELECT * FROM indicators;
                                """));

        List<Indicator> list = new ArrayList<>();
        while (groupsRequest.next()) {
            list.add(new Indicator(groupsRequest.getInt("id"), groupsRequest.getInt("sub_id"),
                    groupsRequest.getString("name"),
                    groupsRequest.getInt("id_competence")));
        }

        return list;
    }

//    public Test getTest(int idTest) throws SQLException {
//        int time = connection.createStatement().executeQuery(
//                "SELECT time FROM tests WHERE id=%d LIMIT 1;"
//                        .formatted(idTest)).getInt(1);
//
//        ResultSet tasks = connection.createStatement().executeQuery(
//                "SELECT id, order, task_type, description FROM tasks ORDERED BY order WHERE id_test=%d;"
//                        .formatted(idTest));
//
//        Test test = new Test(time);
//        ArrayList<Task> taskArray = new ArrayList<>();
//        while (tasks.next()) {
//            ArrayList<String> taskQuestions = null;
//            if (tasks.getInt("task_type") == TaskTypes.ONE_IN_MANY.id)
//                taskQuestions = getTaskQuestionsOneInMany(tasks.getInt("id"));
//
//            taskArray.add(new Task(
//                    tasks.getInt("order"),
//                    TaskTypes.fromID(tasks.getInt("task_type")),
//                    tasks.getString("description"),
//                    taskQuestions
//            ));
//        }
//
//        test.setTasks(taskArray);
//
//        return test;
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

    public static int addUpdateUser(User user) throws SQLException {
        if (user.id() == null) {
            // add
            if (user.patronymic() == null)
                statementExecute("INSERT INTO users (login, password, user_type, name, surname) VALUES ('%s', '%s', %d, '%s', '%s');"
                        .formatted(user.login(), user.password(), user.userType().id, user.name(), user.surname()));
            else
                statementExecute("INSERT INTO users (login, password, user_type, name, surname, patronymic) VALUES ('%s', '%s', %d, '%s', '%s', '%s');"
                        .formatted(user.login(), user.password(), user.userType().id, user.name(), user.surname(), user.patronymic()));


            return getLastInsertId();
        } else if (user.id() > 0) {
            // update
            if (user.patronymic() == null)
                statementExecute("UPDATE users SET login='%s', password='%s', user_type=%d, name='%s', surname='%s' WHERE id=%d);"
                        .formatted(user.login(), user.password(), user.userType().id, user.name(), user.surname(), user.id()));
            else
                statementExecute("UPDATE users SET login='%s', password='%s', name='%s', surname='%s', patronymic='%s' WHERE id=%d);"
                        .formatted(user.login(), user.password(), user.name(), user.surname(), user.patronymic(), user.id()));

            return user.id();
        } else {
            new DatabaseError();
            throw new SQLException();
        }
    }

    private static int getLastInsertId() throws SQLException {
        return getOneRowExecuteQuery("SELECT LAST_INSERT_ID();").getInt(1);
    }

    public static void removeTask(int testId, int taskId) throws SQLException {
        connection.createStatement()
                .executeUpdate(("""
                        DELETE FROM tasks WHERE tasks.id = '%d' AND tasks.id_test = '%d';
                                """.formatted(taskId, testId)));
    }

    private static ResultSet getOneRowExecuteQuery(String sql) throws SQLException {
        ResultSet rs = connection.createStatement().executeQuery(sql);
        rs.next();
        return rs;
    }

    public static void oneInManyRemoveQuestion(int taskId, int questionId) throws SQLException {
        connection.createStatement()
                .executeUpdate(("""
                        DELETE FROM tasks_one_in_many_task_questions WHERE tasks_one_in_many_task_questions.id_task = '%d' 
                        AND tasks_one_in_many_task_questions.id_question = '%d';
                                """.formatted(taskId, questionId)));
    }


    public static void oneInManyAddQuestion(int taskId) throws SQLException {
        connection.createStatement()
                .executeUpdate(("""
                        INSERT INTO tasks_one_in_many_questions_bank(text) VALUES ("");
                                """));

        int id = getLastInsertId();
        connection.createStatement()
                .executeUpdate(("""
                        INSERT INTO tasks_one_in_many_task_questions(id_task, id_question) VALUES (%d, %d);
                                """.formatted(taskId, id)));
    }

    private static void statementExecute(String sql) throws SQLException {
        connection.createStatement().execute(sql);
    }

    private static ResultSet statementExecuteQuery(String sql) throws SQLException {
        return connection.createStatement().executeQuery(sql);
    }

    public int addUpdateCompetence(Competence competence) throws SQLException {
        if (competence.id() == null) {
            // add
            statementExecute("INSERT INTO competences (name, description) VALUES ('%s', '%s');"
                    .formatted(competence.name(), competence.description()));

            return getLastInsertId();
        } else if (competence.id() > 0) {
            // update
            statementExecute("UPDATE idCompetence SET name='%s', description='%s' WHERE id=%d;"
                    .formatted(competence.name(), competence.description(), competence.id()));

            return competence.id();
        } else {
            new DatabaseError();
            throw new SQLException();
        }
    }

    public int addUpdateIndicator(Indicator indicator) throws SQLException {
        // проверка наличия компетенции по id
        if (!statementExecuteQuery("SELECT * FROM idCompetence WHERE id=%d LIMIT 1;"
                .formatted(indicator.competence())).next()) {
            new DatabaseError();
            throw new SQLException(); // TODO: 30.07.2023 сделать автодополненние sub_id
        }

        if (indicator.id() == null) {
            // add
            statementExecute("INSERT INTO indicators (name, sub_id, id_competence) VALUES ('%s', %d, %d);"
                    .formatted(indicator.name(), indicator.subId(), indicator.competence()));

            return getLastInsertId();
        } else if (indicator.id() > 0) {
            // update
            statementExecute("UPDATE indicators SET name='%s', sub_id=%d, id_competence=%d WHERE id=%d;"
                    .formatted(indicator.name(), indicator.subId(), indicator.competence(), indicator.id()));

            return indicator.id();
        } else {
            new DatabaseError();
            throw new SQLException();
        }
    }

    public int addUpdateEducationalPrograms(EducationalProgram educationalProgram) throws SQLException {
        int idEducationalProgram;
        if (educationalProgram.id() == null) {
            // add
            statementExecute("INSERT INTO educational_programs (name) VALUES ('%s');"
                    .formatted(educationalProgram.name()));

            idEducationalProgram = getLastInsertId();
        } else if (educationalProgram.id() > 0) {
            // update
            statementExecute("UPDATE educational_programs SET name='%s' WHERE id=%d;"
                    .formatted(educationalProgram.name(), educationalProgram.id()));

            idEducationalProgram = educationalProgram.id();
        } else {
            new DatabaseError();
            throw new SQLException();
        }

        // refresh id of competences
        if (educationalProgram.idCompetences() != null) {
            statementExecute("DELETE FROM educational_programs_competences WHERE id_educational_program=%d;".formatted(idEducationalProgram));
            for (int idCompetence : educationalProgram.idCompetences())
                statementExecute("INSERT INTO educational_programs_competences (id_educational_program, id_competences) VALUES (%d, %d);"
                        .formatted(idEducationalProgram, idCompetence));
        }

        return idEducationalProgram;
    }

    public int addUpdateDirection(Direction direction) throws SQLException {
        if (!statementExecuteQuery("SELECT * FROM educational_programs WHERE id=%d LIMIT 1;"
                .formatted(direction.idEducationalProgram())).next()) {
            new DatabaseError();
            throw new SQLException();
        }

        if (direction.id() == null) {
            // add
            statementExecute(
                    ("INSERT INTO directions (id_level, espf_code, espf_name, code, name, id_educational_program) " +
                            "VALUES (%d, '%s', '%s','%s','%s', %d);").formatted(
                            direction.levelType().id, direction.espfCode(), direction.espfName(),
                            direction.code(), direction.name(), direction.idEducationalProgram())
            );
            return getLastInsertId();
        } else if (direction.id() > 0) {
            // update
            statementExecute(("UPDATE directions SET id_level=%d, espf_code='%s', espf_name='%s', code='%s', name='%s', " +
                    "id_educational_program=%d WHERE id=%d;").formatted(direction.levelType().id, direction.espfCode(),
                    direction.espfName(), direction.code(), direction.name(), direction.idEducationalProgram(), direction.id()));

            return direction.id();
        } else {
            new DatabaseError();
            throw new SQLException();
        }
    }

    public int addUpdateDiscipline(Discipline discipline) throws SQLException {
        int idDiscipline;
        if (discipline.id() == null) {
            // add
            statementExecute("INSERT INTO discipline (name) VALUES ('%s');"
                    .formatted(discipline.name()));

            idDiscipline = getLastInsertId();
        } else if (discipline.id() > 0) {
            // update
            statementExecute("UPDATE discipline SET name='%s' WHERE id=%d;"
                    .formatted(discipline.name(), discipline.id()));

            idDiscipline = discipline.id();
        } else {
            new DatabaseError();
            throw new SQLException();
        }

        // refresh id of competences
        if (discipline.idCompetences() != null) {
            statementExecute("DELETE FROM discipline_competence WHERE id_discipline=%d;".formatted(idDiscipline));
            for (int idCompetence : discipline.idCompetences())
                statementExecute("INSERT INTO discipline_competence (id_discipline, id_competences) VALUES (%d, %d);"
                        .formatted(discipline.id(), idCompetence));
        }

        return idDiscipline;
    }

    public int addUpdateGroups(Group group) throws SQLException {
        if (!statementExecuteQuery("SELECT * FROM directions WHERE id=%d LIMIT 1;"
                .formatted(group.idDirection())).next()) {
            new DatabaseError();
            throw new SQLException();
        }

        if (group.id() == null) {
            // add
            statementExecute("INSERT INTO teaching_groups (name, id_direction) VALUES ('%s', %d);"
                    .formatted(group.name(), group.idDirection()));

            return getLastInsertId();
        } else if (group.id() > 0) {
            // update
            statementExecute("UPDATE teaching_groups SET name='%s', id_direction=%d WHERE id=%d;"
                    .formatted(group.name(), group.idDirection(), group.id()));

            return group.id();
        } else {
            new DatabaseError();
            throw new SQLException();
        }
    }

    public int addStudent(Student student) throws SQLException {
        if (!statementExecuteQuery("SELECT * FROM users WHERE id=%d AND user_type=%d LIMIT 1;"
                .formatted(student.idUser(), 0)).next()) {
            new DatabaseError();
            throw new SQLException();
        }

        if (!statementExecuteQuery("SELECT * FROM teaching_groups WHERE id=%d LIMIT 1;"
                .formatted(student.idGroup())).next()) {
            new DatabaseError();
            throw new SQLException();
        }

        if (student.idUser() > 0) {
            // add
            statementExecute("INSERT INTO students (id_user, id_group, report_card_id) VALUES (%d, %d, %d);"
                    .formatted(student.idUser(), student.idGroup(), student.reportCardId()));

            return getLastInsertId();
        } else {
            new DatabaseError();
            throw new SQLException();
        }
    }

    public int addTeacher(Teacher teacher) throws SQLException {
        if (!statementExecuteQuery("SELECT * FROM users WHERE id=%d AND user_type=%d LIMIT 1;"
                .formatted(teacher.idUser(), 1)).next()) {
            new DatabaseError();
            throw new SQLException();
        }

        if (!statementExecuteQuery("SELECT * FROM teacher_status WHERE id=%d LIMIT 1;"
                .formatted(teacher.idUser())).next()) {
            new DatabaseError();
            throw new SQLException();
        }

        if (teacher.idUser() > 0) {
            // add
            statementExecute("INSERT INTO teacher (id_user, id_status) VALUES (%d, %d);"
                    .formatted(teacher.idUser(), teacher.status()));

            return getLastInsertId();
        } else {
            new DatabaseError();
            throw new SQLException();
        }
    }

    public ArrayList<String> getTaskQuestionsOneInMany(int idTask) throws SQLException {
        ResultSet id_questions = statementExecuteQuery(
                "SELECT id_question FROM tasks_one_in_many_task_questions WHERE id_task=%d;"
                        .formatted(idTask));

        ArrayList<String> taskQuestions = new ArrayList<>(4);
        while (id_questions.next()) {
            ResultSet text = statementExecuteQuery(
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

    public void addDirection(Direction direction, int idLevelType, int idEducationalPrograms) throws SQLException {
        statementExecute(
                ("INSERT INTO directions (id_level, espf_code, espf_name, code, name, id_educational_program) " +
                        "VALUES (%d, '%s', '%s','%s','%s', %d);").formatted(
                        idLevelType, direction.espfCode(), direction.espfName(),
                        direction.code(), direction.name(), idEducationalPrograms)
        );
    }

    public void delDirection(int idDirection) throws SQLException {
        statementExecute("DELETE FROM directions WHERE id=%d;".formatted(idDirection));
    }

    public void delDiscipline(int idDiscipline) throws SQLException {
        statementExecute("DELETE FROM discipline WHERE id=%d;".formatted(idDiscipline));
    }

    public void delGroup(int idGroup) throws SQLException {
        statementExecute("DELETE FROM teaching_groups WHERE id=%d;".formatted(idGroup));
    }

    public void updateDirectionIdEducationalProgram(int idDirections, Direction direction, int idEducationalProgram) throws SQLException {
        statementExecute(("UPDATE directions SET espf_code='%s', espf_name='%s', code='%s', name='%s', " +
                "id_educational_program=%d WHERE id=%d;").formatted(direction.espfCode(),
                direction.espfName(), direction.code(), direction.name(), idEducationalProgram, idDirections));
    }

    public void updateDirectionIdLevelType(int idDirections, Direction direction, int idLevelType) throws SQLException {
        statementExecute(("UPDATE directions SET id_level=%d, espf_code='%s', espf_name='%s', code='%s', name='%s', " +
                "WHERE id=%d;").formatted(idLevelType, direction.espfCode(),
                direction.espfName(), direction.code(), direction.name(), idDirections));
    }

    public void updateDirection(int idDirections, Direction direction) throws SQLException {
        statementExecute(("UPDATE directions SET espf_code='%s', espf_name='%s', code='%s', name='%s', WHERE id=%d;")
                .formatted(direction.espfCode(), direction.espfName(), direction.code(), direction.name(),
                        idDirections));
    }

    public Direction getDirection(int idDirection) throws SQLException {
        ResultSet direction = statementExecuteQuery("SELECT * FROM directions WHERE id=%d LIMIT 1;"
                .formatted(idDirection));
        return new Direction(
                getLastInsertId(),
                direction.getString("espf_code"),
                direction.getString("espf_name"),
                direction.getString("code"),
                direction.getString("name"),
                LevelTypes.fromID(direction.getInt("id_level")),
                direction.getInt("id_educational_program"));
    }

    public String nameEducationalProgram(int idEducationalProgram) throws SQLException {
        return statementExecuteQuery("SELECT * FROM educational_programs WHERE id=%d LIMIT 1;"
                .formatted(idEducationalProgram)).getString("name");
    }

    public void addCompetence(Competence competence) throws SQLException {
        statementExecute("INSERT INTO competences (name, description) VALUES ('%s', '%s');"
                .formatted(competence.name(), competence.description()));
    }

    public void delCompetence(int idCompetence) throws SQLException {
        statementExecute("DELETE FROM idCompetence WHERE id=%d;".formatted(idCompetence));
    }

    public void updateCompetence(int idCompetence, Competence competence) throws SQLException {
        statementExecute("UPDATE idCompetence SET name='%s', description='%s' WHERE id=%d;"
                .formatted(competence.name(), competence.description(), idCompetence));
    }

//    public ArrayList<Integer> idTasksByIndicator(int idIndicator) {
//        statementExecuteQuery("")
//    }

    public Competence getCompetence(int idCompetence) throws SQLException {
        ResultSet competence = statementExecuteQuery("SELECT * FROM idCompetence WHERE id=%d LIMIT 1;"
                .formatted(idCompetence));
        return new Competence(
                getLastInsertId(),
                competence.getString("name"),
                competence.getString("description"));
    }

    public void delIndicator(int idIndicator) throws SQLException {
        statementExecute("DELETE FROM indicators WHERE id=%d;".formatted(idIndicator));
    }

    public Indicator getIndicator(int idIndicator) throws SQLException {
        ResultSet competence = statementExecuteQuery("SELECT * FROM indicators WHERE id=%d LIMIT 1;"
                .formatted(idIndicator));
        return new Indicator(
                getLastInsertId(),
                competence.getInt("sub_id"),
                competence.getString("name"),
                competence.getInt("id_competence"));
    }

    public ArrayList<Integer> idTasksByIndicator(int idIndicator) throws SQLException {
        ResultSet idIndicators = statementExecuteQuery(
                "SELECT id_task FROM tasks_indicators WHERE id_task=%d"
                        .formatted(idIndicator));

        ArrayList<Integer> idTasks = new ArrayList<>();
        while (idIndicators.next()) {
            idTasks.add(idIndicators.getInt("id_task"));
        }

        return idTasks;
    }

    public void addEducationalProgram(EducationalProgram educationalProgram) throws SQLException {
        statementExecute("INSERT INTO educational_programs (name) VALUES ('%s');"
                .formatted(educationalProgram.name()));
    }

    public void delEducationalProgram(int idEducationalProgram) throws SQLException {
        statementExecute("DELETE FROM educational_programs WHERE id=%d;".formatted(idEducationalProgram));
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

    public void updateEducationalProgram(int idEducationalProgram, EducationalProgram educationalProgram) throws SQLException {
        statementExecute("UPDATE educational_programs SET name='%s' WHERE id=%d;"
                .formatted(educationalProgram.name(), idEducationalProgram));
    }

    public EducationalProgram getEducationalProgram(int idEducationalProgram) throws SQLException {
        ResultSet idCompetencesResultSet = statementExecuteQuery("SELECT id_competence FROM educational_programs_competences WHERE id=%d;");
        ArrayList<Integer> idCompetences = new ArrayList<>();
        while (idCompetencesResultSet.next())
            idCompetences.add(idCompetencesResultSet.getInt("id_competence"));

        ResultSet educationalProgram = getOneRowExecuteQuery("SELECT * FROM educational_programs WHERE id=%d LIMIT 1;"
                .formatted(idEducationalProgram));
        return new EducationalProgram(getLastInsertId(), educationalProgram.getString("name"), idCompetences);
    }

    public void connectEducationalProgramsWithCompetences(int idEducationalProgram, ArrayList<Integer> idCompetences) throws SQLException {
        for (int idCompetence : idCompetences) {
            statementExecute("INSERT INTO educational_programs_competences (id_educational_program, id_competence) VALUES (%d, %d);"
                    .formatted(idEducationalProgram, idCompetence));
        }
    }
}
