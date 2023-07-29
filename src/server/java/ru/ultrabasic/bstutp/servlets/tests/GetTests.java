package ru.ultrabasic.bstutp.servlets.tests;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import ru.ultrabasic.bstutp.Config;
import ru.ultrabasic.bstutp.data.SQLHandler;
import ru.ultrabasic.bstutp.data.models.TestShort;
import ru.ultrabasic.bstutp.data.models.UserTypes;
import ru.ultrabasic.bstutp.messages.errors.DatabaseError;
import ru.ultrabasic.bstutp.messages.errors.JSONError;
import ru.ultrabasic.bstutp.messages.errors.SessionKeyInvalid;
import ru.ultrabasic.bstutp.messages.success.StudentsTestsListActive;
import ru.ultrabasic.bstutp.messages.success.StudentsTestsListCompleted;
import ru.ultrabasic.bstutp.messages.success.TeacherTestsListDraft;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/tests/list")
public class GetTests extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Integer userId = null;
            for (Cookie cookie : req.getCookies())
                if (cookie.getName().equals("sessionKey"))
                    userId = SQLHandler.getUserIdBySessionKey(cookie.getValue());

            if (userId == null) {
                new SessionKeyInvalid().writeToResponse(resp);
                return;
            }

            UserTypes userTypes = SQLHandler.getUserType(userId);

            if (userTypes == null)
                throw new SQLException();

            String taskType = req.getParameter("task_type");
            Integer page;
            try {
                page = Integer.parseInt(req.getParameter("page_num"));
            } catch (Exception e) {
                page = 0;
            }

            if (userTypes == UserTypes.STUDENT) {

                if ("results".equals(taskType)) {
                    new StudentsTestsListCompleted()
                            .writeToResponse(resp, getTasksByPage(SQLHandler.getStudentsTasksCompleted(userId), page));
                } else {
                    new StudentsTestsListActive()
                            .writeToResponse(resp, getTasksByPage(SQLHandler.getStudentsTasksActive(userId), page));
                }
            } else {
                if ("draft".equals(taskType)) {
                    new TeacherTestsListDraft()
                            .writeToResponse(resp, getTasksByPage(SQLHandler.getTeacherTasksDraft(userId), page));
                } else if (userTypes == UserTypes.ADMIN && "admin".equals(taskType)) {
                    // TODO: вернуть значения для админки
                } else {
                    new TeacherTestsListDraft()
                            .writeToResponse(resp, getTasksByPage(SQLHandler.getTeacherTasksActive(userId), page));
                }
            }
        } catch (SQLException e) {
            new DatabaseError().writeToResponse(resp);
        } catch (JSONException e) {
            new JSONError().writeToResponse(resp);
        }
    }

    public JSONArray getTasksByPage(List<TestShort> taskList, Integer page) {
        int pages = 1 + (taskList.size() - 1) / Config.TESTS_IN_PAGE;

        page = Math.max(0, Math.min(pages - 1, page));

        JSONArray tasks = new JSONArray();
        for (int i = page * Config.TESTS_IN_PAGE; i < Math.min(page * Config.TESTS_IN_PAGE + Config.TESTS_IN_PAGE,
                taskList.size()); i++)
            tasks.put(taskList.get(i).getJSONObject());

        return tasks;
    }
}
