package ru.ultrabasic.bstutp.data;

import org.json.JSONObject;
import ru.ultrabasic.bstutp.data.models.TestShort;
import ru.ultrabasic.bstutp.data.models.TestState;
import ru.ultrabasic.bstutp.data.models.UserTypes;

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
                // Get full test
                break;
            case COMPLETED:
                // Get report
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
