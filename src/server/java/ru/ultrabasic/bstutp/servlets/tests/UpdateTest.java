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
import ru.ultrabasic.bstutp.data.models.tasks.TaskTypes;
import ru.ultrabasic.bstutp.messages.errors.DatabaseError;
import ru.ultrabasic.bstutp.messages.errors.JSONError;
import ru.ultrabasic.bstutp.messages.errors.SessionKeyInvalid;
import ru.ultrabasic.bstutp.messages.errors.TestNotStarted;
import ru.ultrabasic.bstutp.messages.success.TestStarted;
import ru.ultrabasic.bstutp.messages.success.UpdatesProcessed;

import java.io.IOException;
import java.sql.SQLException;
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

            JSONArray updatedTasks = jsonStart.getJSONArray("tasks");
            for (int i = 0; i < updatedTasks.length(); i++) {
                JSONObject update = updatedTasks.getJSONObject(i);

                TaskTypes taskType = TaskTypes.fromType(update.getString("taskType"));
                int idDetailedReport = update.getInt("idReportDetailed");

                if (SQLHandler.isTestRunningFromIdDetailedReport(idDetailedReport)) {
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
        } catch (SQLException e) {
            new DatabaseError().writeToResponse(resp);
        } catch (JSONException e) {
            new JSONError().writeToResponse(resp);
        }
    }
}
