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
import ru.ultrabasic.bstutp.Config;
import ru.ultrabasic.bstutp.data.SQLHandler;
import ru.ultrabasic.bstutp.data.models.TestShort;
import ru.ultrabasic.bstutp.data.models.UserInfo;
import ru.ultrabasic.bstutp.data.models.UserTypes;
import ru.ultrabasic.bstutp.messages.errors.DatabaseError;
import ru.ultrabasic.bstutp.messages.errors.JSONError;
import ru.ultrabasic.bstutp.messages.errors.SessionKeyInvalid;
import ru.ultrabasic.bstutp.messages.success.StudentsTestsListActive;
import ru.ultrabasic.bstutp.messages.success.StudentsTestsListCompleted;
import ru.ultrabasic.bstutp.messages.success.TeacherTestsListActive;
import ru.ultrabasic.bstutp.messages.success.TeacherTestsListDraft;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/tests/list")
public class GetTests extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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

            String taskType = req.getParameter("task_type");
            Integer page;
            try {
                page = Integer.parseInt(req.getParameter("page_num"));
            } catch (Exception e) {
                page = 0;
            }

            if (userInfo.userType == UserTypes.STUDENT) {

                if ("completed".equals(taskType)) {
                    new StudentsTestsListCompleted()
                            .writeToResponse(resp, getTasksByPage(SQLHandler.getStudentsTasksCompleted(userId), page), userInfo);
                } else {
                    new StudentsTestsListActive()
                            .writeToResponse(resp, getTasksByPage(SQLHandler.getStudentsTasksActive(userId), page), userInfo);
                }
            } else {
                if ("draft".equals(taskType)) {
                    new TeacherTestsListDraft()
                            .writeToResponse(resp, getTasksByPage(SQLHandler.getTeacherTestsDraft(userId), page), userInfo);
                } else if (userInfo.userType == UserTypes.ADMIN && "admin".equals(taskType)) {
                    // TODO: вернуть значения для админки
                } else {
                    new TeacherTestsListActive()
                            .writeToResponse(resp, getTasksByPage(SQLHandler.getTeacherTestsActive(userId), page), userInfo);
                }
            }
        } catch (SQLException e) {
            new DatabaseError().writeToResponse(resp);
        } catch (JSONException e) {
            new JSONError().writeToResponse(resp);
        }
    }

    public JSONObject getTasksByPage(List<TestShort> taskList, Integer page) {
        JSONObject tests = new JSONObject();
        int pages = 1 + (taskList.size() - 1) / Config.TESTS_IN_PAGE;

        page = Math.max(0, Math.min(pages - 1, page));

        tests.put("currentPage", page);
        tests.put("totalPages", pages);

        JSONArray tasks = new JSONArray();
        for (int i = page * Config.TESTS_IN_PAGE; i < Math.min(page * Config.TESTS_IN_PAGE + Config.TESTS_IN_PAGE,
                taskList.size()); i++)
            tasks.put(taskList.get(i).getJSONObject());

        tests.put("data", tasks);

        return tests;
    }
}
