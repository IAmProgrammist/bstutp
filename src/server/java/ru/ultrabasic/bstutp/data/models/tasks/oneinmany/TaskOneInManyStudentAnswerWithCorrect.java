package ru.ultrabasic.bstutp.data.models.tasks.oneinmany;

import org.json.JSONObject;
import ru.ultrabasic.bstutp.data.models.tasks.CorrectAnswer;
import ru.ultrabasic.bstutp.data.models.tasks.StudentAnswer;

import java.util.List;

public class TaskOneInManyStudentAnswerWithCorrect extends TaskOneInManyStudentAnswer implements CorrectAnswer {

    TaskOneInManyQuestion correctAnswer = null;

    public TaskOneInManyStudentAnswerWithCorrect(int id, int order, String description, int ownerId,
                                                 List<TaskOneInManyQuestion> questionPull, TaskOneInManyQuestion chosen,
                                                 Integer idReportDetailed, TaskOneInManyQuestion correctAnswer) {
        super(id, order, description, ownerId, questionPull, chosen, idReportDetailed);

        if (questionPull.contains(correctAnswer))
            this.correctAnswer = correctAnswer;
    }

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = super.getJSONObject();

        object.put("idAnswerCorrect", correctAnswer == null ? JSONObject.NULL : correctAnswer.id());

        return object;
    }
}
