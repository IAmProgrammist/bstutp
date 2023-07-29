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

            if (userTypes == UserTypes.STUDENT) {
                String taskType = req.getParameter("task_type");
                int page;
                try {
                    page = Integer.parseInt(req.getParameter("page_num"));
                } catch (Exception e) {
                    page = 0;
                }

                if ("results".equals(taskType)) {
                    List<TestShort> taskList = SQLHandler.getStudentsTasksCompleted(userId);
                    int pages = 1 + (taskList.size() - 1) / Config.TESTS_IN_PAGE;

                    page = Math.max(0, Math.min(pages - 1, page));

                    JSONArray tasks = new JSONArray();
                    for (int i = page * Config.TESTS_IN_PAGE; i < Math.min(page * Config.TESTS_IN_PAGE + Config.TESTS_IN_PAGE,
                            taskList.size()); i++)
                        tasks.put(taskList.get(i).getJSONObject());

                    new StudentsTestsListCompleted().writeToResponse(resp, tasks);
                } else {
                    List<TestShort> taskList = SQLHandler.getStudentsTasksActive(userId);
                    int pages = 1 + (taskList.size() - 1) / Config.TESTS_IN_PAGE;

                    page = Math.max(0, Math.min(pages - 1, page));

                    JSONArray tasks = new JSONArray();
                    for (int i = page * Config.TESTS_IN_PAGE; i < Math.min(page * Config.TESTS_IN_PAGE + Config.TESTS_IN_PAGE,
                            taskList.size()); i++)
                        tasks.put(taskList.get(i).getJSONObject());

                    new StudentsTestsListActive().writeToResponse(resp, tasks);
                }
            } else {


                if (userTypes == UserTypes.ADMIN) {

                }
            }
        } catch (SQLException e) {
            new DatabaseError().writeToResponse(resp);
        } catch (JSONException e) {
            new JSONError().writeToResponse(resp);
        }
    }
}
