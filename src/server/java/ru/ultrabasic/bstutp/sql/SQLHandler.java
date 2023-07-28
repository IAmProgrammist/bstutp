package ru.ultrabasic.bstutp.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class SQLHandler {
    static Connection connection;

    static {
        //TODO: вынести в .properties
        String url = "jdbc:mysql://localhost:3306/";
        String username = "root";
        String password = "root";

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Test getTest(int idTest) throws SQLException {
        ResultSet time = connection.createStatement().executeQuery(
                "SELECT time FROM main_db.tests WHERE id=%d LIMIT 1;"
                        .formatted(idTest));
        time.next();


        ResultSet tasks = connection.createStatement().executeQuery(
                "SELECT id, order, task_type, description FROM main_db.tasks WHERE id_test=%d;"
                        .formatted(idTest));

        Test test = new Test(time.getInt(1));
        ArrayList<Task> taskArray = new ArrayList<>();
        while (tasks.next()) {
            ArrayList<String> taskQuestions = null;
            if (tasks.getInt("task_type") == 0)
                taskQuestions = getTaskQuestionsOneInMany(tasks.getInt("id"));

            taskArray.add(new Task(
                    tasks.getInt("order"),
                    tasks.getInt("task_type"),
                    tasks.getString("description"),
                    taskQuestions
                    ));
        }

        test.setTasks(taskArray);

        return test;
    }



    public ArrayList<String> getTaskQuestionsOneInMany(int idTask) throws SQLException {
        ResultSet id_questions = connection.createStatement().executeQuery(
                "SELECT id_question FROM main_db.tasks_one_in_many_task_questions WHERE id_task=%d;"
                        .formatted(idTask));

        ArrayList<String> taskQuestions = new ArrayList<>(4);
        while (id_questions.next()) {
            ResultSet text = connection.createStatement().executeQuery(
                    "SELECT text FROM main_db.tasks_one_in_many_questions_bank WHERE id=%d LIMIT 1;"
                            .formatted(id_questions.getInt(1)));
            text.next();
            taskQuestions.add(text.getString(1));
        }

        return taskQuestions;
    }

    public String getAnswerOneInMany(int idTask) throws SQLException {
        ResultSet id_answer_correct = connection.createStatement().executeQuery(
                "SELECT id_answer_correct FROM main_db.tasks_one_in_many WHERE id=%d;"
                        .formatted(idTask));
        id_answer_correct.next();

        ResultSet answer_correct = connection.createStatement().executeQuery(
                "SELECT text FROM main_db.tasks_one_in_many_questions_bank WHERE id=%d LIMIT 1;"
                        .formatted(id_answer_correct.getInt(1)));
        answer_correct.next();

        return answer_correct.getString(1);
    }

    public String getAnswerText(int idTask) throws SQLException {
        ResultSet answer_correct = connection.createStatement().executeQuery(
                "SELECT answer_correct FROM main_db.tasks_text WHERE id=%d LIMIT 1;"
                        .formatted(idTask));
        answer_correct.next();

        return answer_correct.getString(1);
    }


}

