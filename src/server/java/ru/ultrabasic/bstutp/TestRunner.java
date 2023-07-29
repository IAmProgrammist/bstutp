package ru.ultrabasic.bstutp;

import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.ultrabasic.bstutp.data.SQLHandler;
import ru.ultrabasic.bstutp.sql.Task;
import ru.ultrabasic.bstutp.sql.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class TestRunner {
    private int time;
    private String sessionKey;
    private int idTest;
    private HttpServletResponse resp;

    public TestRunner(String sessionKey, int idTest, HttpServletResponse resp) {
        this.sessionKey = sessionKey;
        this.idTest = idTest;
        this.resp = resp;
    }

    public void startTest() throws SQLException, IOException, InterruptedException {
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

        eventMapWithSolution.put(sessionKey, "start_test");

        Timer runTimer = new Timer();
        runTimer.schedule(new TimerTask() {
            @Override
            public void run() {

            }
        }, test.getTime());
//        Thread.sleep(test.getTime());

    }

    static private Map<String, String> eventMapWithSolution = new HashMap<>(20);
    private void endTest() {

    }
}
