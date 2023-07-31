package ru.ultrabasic.bstutp.servlets.tests;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.ultrabasic.bstutp.data.SQLHandler;
import ru.ultrabasic.bstutp.data.TestManager;
import ru.ultrabasic.bstutp.data.models.TestState;
import ru.ultrabasic.bstutp.data.models.UserInfo;
import ru.ultrabasic.bstutp.data.models.UserTypes;
import ru.ultrabasic.bstutp.data.models.tasks.TaskTypes;
import ru.ultrabasic.bstutp.messages.errors.DatabaseError;
import ru.ultrabasic.bstutp.messages.errors.JSONError;
import ru.ultrabasic.bstutp.messages.errors.SessionKeyInvalid;
import ru.ultrabasic.bstutp.messages.errors.TestNotStarted;
import ru.ultrabasic.bstutp.messages.success.TestStarted;
import ru.ultrabasic.bstutp.messages.success.UpdatesProcessed;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/api/tests/update")
public class UpdateTest extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
            JSONObject jsonStart = new JSONObject(req.getReader().lines().collect(Collectors.joining(System.lineSeparator())));

            if (userInfo.userType == UserTypes.STUDENT) {
                JSONArray updatedTasks = jsonStart.getJSONArray("tasks");
                for (int i = 0; i < updatedTasks.length(); i++) {
                    JSONObject update = updatedTasks.getJSONObject(i);

                    TaskTypes taskType = TaskTypes.fromType(update.getString("taskType"));
                    int idDetailedReport = update.getInt("idReportDetailed");

                    if (SQLHandler.isTestRunningFromIdDetailedReport(idDetailedReport, userId)) {
                        switch (taskType) {
                            case ONE_IN_MANY:
                                Integer idAnswer = update.get("idAnswer").equals(JSONObject.NULL) ? null : update.getInt("idAnswer");
                                SQLHandler.setAnswerOneInMany(idDetailedReport, idAnswer);
                                break;
                            case TEXT:
                                String answer = update.get("answer").equals(JSONObject.NULL) ? null : update.getString("answer");
                                SQLHandler.setAnswerText(idDetailedReport, answer);
                                break;
                        }
                    }
                }

                new UpdatesProcessed().writeToResponse(resp);
            } else if (SQLHandler.getState(userId, jsonStart.getInt("idTest")) == TestState.DRAFT) {
                int testId = jsonStart.getInt("idTest");
                String name = jsonStart.getString("name");
                Long duration = jsonStart.getLong("duration");
                Integer idDiscipline = jsonStart.get("idDiscipline") == JSONObject.NULL ? null : jsonStart.getInt("idDiscipline");
                List<Integer> groups = new ArrayList<>();
                JSONArray gArray = jsonStart.getJSONArray("groups");
                for (int i = 0; i < gArray.length(); i++) {
                    groups.add(gArray.getInt(i));
                }

                SQLHandler.updateTestHeader(testId, name, duration, idDiscipline, groups);

                JSONArray tasks = jsonStart.getJSONArray("tasks");
                for (int i = 0; i < tasks.length(); i++) {
                    JSONObject task = tasks.getJSONObject(i);
                    JSONArray iArray = task.getJSONArray("indicators");
                    List<Integer> indicators = new ArrayList<>();
                    for (int j = 0; j < iArray.length(); j++) {
                        indicators.add(gArray.getInt(j));
                    }

                    if (task.getString("taskType").equals("text")) {
                        SQLHandler.updateTextTask(task.getInt("taskId"),
                                task.getString("description"), task.get("answerCorrect") == JSONObject.NULL ? "" : task.getString("answerCorrect"), indicators);
                    } else if (task.getString("taskType").equals("one_in_many")) {
                        JSONArray bank = task.getJSONArray("questionPull");
                        for (int j = 0; j < bank.length(); j++) {
                            JSONObject q = bank.getJSONObject(j);
                            SQLHandler.updateOneInManyTaskQuestion(q.getInt("id"), q.getString("text"));
                        }
                        SQLHandler.updateOneInManyTask(task.getInt("taskId"),
                                task.getString("description"), task.get("idAnswerCorrect") == JSONObject.NULL ? null : task.getInt("idAnswerCorrect"), indicators);
                    }
                }

                new UpdatesProcessed().writeToResponse(resp);
            }
        } catch (SQLException e) {
            new DatabaseError().writeToResponse(resp);
        } catch (JSONException e) {
            new JSONError().writeToResponse(resp);
        }
    }
}
