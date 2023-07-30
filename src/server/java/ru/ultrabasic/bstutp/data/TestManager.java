package ru.ultrabasic.bstutp.data;

import org.json.JSONObject;
import ru.ultrabasic.bstutp.data.models.*;

import java.sql.SQLException;

public class TestManager {
    public static JSONObject whassup(int userId, int testId) throws SQLException {
        TestState state = SQLHandler.getState(userId, testId);

        JSONObject testInfo = new JSONObject();

        switch (state) {
            case AVAILABLE:
                TestShort testShortAvailable = SQLHandler.getTestShort(testId);
                testInfo.put("test", testShortAvailable.getJSONObject());
                break;
            case RUNNING:
                Report questionsOnlyTest = SQLHandler.getReportNoAnswers(testId, userId);
                testInfo.put("test", questionsOnlyTest.getJSONObject());
                break;
            case COMPLETED:
                Report fullTest = SQLHandler.getReportWithAnswers(testId, userId);
                testInfo.put("test", fullTest.getJSONObject());
                break;
            case DRAFT:
                // Teacher func
                break;
            case EDITABLE:
                // Edit for teacher
                break;
            case NOT_AVAILABLE:
                break;
        }

        testInfo.put("state", state.type);

        return testInfo;
    }

    public static boolean startTest(int userId, int testId) throws SQLException {
        TestState state = SQLHandler.getState(userId, testId);
        if (state != TestState.AVAILABLE)
            return false;

        return SQLHandler.startTest(userId, testId);
    }
}
