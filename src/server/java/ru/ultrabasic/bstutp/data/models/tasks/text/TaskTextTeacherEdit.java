package ru.ultrabasic.bstutp.data.models.tasks.text;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.ultrabasic.bstutp.data.models.tasks.CorrectAnswer;
import ru.ultrabasic.bstutp.data.models.records.Indicator;

import java.util.ArrayList;
import java.util.List;

public class TaskTextTeacherEdit extends TaskText implements CorrectAnswer {
    String correctAnswer;
    List<Indicator> indicators = new ArrayList<>();

    public TaskTextTeacherEdit(int id, int order, String description, int ownerId,
                               String correctAnswer, List<Indicator> indicators) {
        super(id, order, description, ownerId);

        this.correctAnswer = correctAnswer;
        this.indicators = indicators;
    }

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = super.getJSONObject();
        object.put("answerCorrect", correctAnswer == null ? JSONObject.NULL : correctAnswer);

        JSONArray inds = new JSONArray();
        for (Indicator ind : indicators) {
            inds.put(ind.id());
        }
        object.put("indicators", inds);

        return object;
    }
}
