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
                                    List<TaskOneInManyQuestion> questionPull, List<Indicator> indicators) {
        super(id, order, description, ownerId, questionPull);

        this.indicators = indicators;
    }

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = super.getJSONObject();

        object.put("idAnswerCorrect", correctAnswer == null ? JSONObject.NULL : correctAnswer.id());
        JSONArray inds = new JSONArray();
        for (Indicator ind : indicators) {
            JSONObject indJSON = new JSONObject();
            indJSON.put("id", ind.id());
            indJSON.put("subId", ind.subId());
            indJSON.put("name", ind.name());
            inds.put(indJSON);
        }
        object.put("indicators", inds);

        return object;
    }
}
