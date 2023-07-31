package ru.ultrabasic.bstutp.data.models.tasks.oneinmany;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.ultrabasic.bstutp.data.models.tasks.CorrectAnswer;
import ru.ultrabasic.bstutp.data.models.records.Indicator;

import java.util.ArrayList;
import java.util.List;

public class TaskOneInManyTeacherEdit extends TaskOneInMany implements CorrectAnswer {

    TaskOneInManyQuestion correctAnswer = null;
    List<Indicator> indicators = new ArrayList<>();

    public TaskOneInManyTeacherEdit(int id, int order, String description, int ownerId,
                                    List<TaskOneInManyQuestion> questionPull, TaskOneInManyQuestion correctAnswer, List<Indicator> indicators) {
        super(id, order, description, ownerId, questionPull);

        this.indicators = indicators;
        this.questionPull = questionPull;
        if (questionPull.contains(correctAnswer))
            this.correctAnswer = correctAnswer;
    }

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = super.getJSONObject();

        object.put("idAnswerCorrect", correctAnswer == null ? JSONObject.NULL : correctAnswer.id());
        JSONArray inds = new JSONArray();
        for (Indicator ind : indicators) {
            inds.put(ind.id());
        }
        object.put("indicators", inds);

        return object;
    }
}
