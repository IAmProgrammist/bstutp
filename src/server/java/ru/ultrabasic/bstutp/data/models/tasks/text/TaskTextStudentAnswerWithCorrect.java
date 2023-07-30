package ru.ultrabasic.bstutp.data.models.tasks.text;

import org.json.JSONObject;
import ru.ultrabasic.bstutp.data.models.tasks.CorrectAnswer;

public class TaskTextStudentAnswerWithCorrect extends TaskTextStudentAnswer implements CorrectAnswer {
    String correctAnswer;

    public TaskTextStudentAnswerWithCorrect(int id, int order, String description, int ownerId, String answer, Integer idReportDetailed, String correctAnswer) {
        super(id, order, description, ownerId, answer, idReportDetailed);

        this.correctAnswer = correctAnswer;
    }

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = super.getJSONObject();
        object.put("answerCorrect", correctAnswer == null ? JSONObject.NULL : correctAnswer);
        return object;
    }
}
