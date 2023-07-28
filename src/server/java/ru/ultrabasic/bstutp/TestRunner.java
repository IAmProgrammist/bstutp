package ru.ultrabasic.bstutp;

import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.ultrabasic.bstutp.sql.SQLHandler;
import ru.ultrabasic.bstutp.sql.Task;
import ru.ultrabasic.bstutp.sql.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.TimerTask;

public class TestRunner extends TimerTask {
    private int time;
    private String sessionKey;
    private int idTest;
    private HttpServletResponse resp;

    public TestRunner(String sessionKey, int idTest, HttpServletResponse resp) {
        this.sessionKey = sessionKey;
        this.idTest = idTest;
        this.resp = resp;
    }

    private void startTest() throws SQLException, IOException, InterruptedException {
        SQLHandler sqlHandler = new SQLHandler();
        Test test = sqlHandler.getTest(idTest);

        // создание json объекта
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("time", test.getTime());
        JSONArray jsonTasks = new JSONArray();
        JSONObject jsonObjectSub = new JSONObject();
        for (Task task : test.getTasks()) {
            jsonObjectSub.put("taskType", task.getTaskType());
            jsonObjectSub.put("description", task.getDescription());
            if (task.getTaskType() == 0) {
                JSONArray jsonTaskQuestions = new JSONArray();
                for (String taskQuestion : task.getTaskQuestions())
                    jsonTaskQuestions.put(taskQuestion);
                jsonObjectSub.put("taskQuestions", jsonTaskQuestions);
            }
            jsonTasks.put(jsonObjectSub);
        }

        // отправка json объекта с текстом теста
        resp.setContentType("application/json");
        resp.getWriter().print(jsonObject);

        Thread.sleep(test.getTime());

        Thread.

    }

    @Override
    public void run() {
        try {


        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
