package ru.ultrabasic.bstutp.data.models;

import org.json.JSONObject;

public class UserInfo {
    public int userId;
    public String name;
    public String surname;
    public String patronymic;
    public UserTypes userType;

    public UserInfo(int userId, String name, String surname, String patronymic, int userType) {
        this.userId = userId;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.userType = UserTypes.fromID(userType);
    }

    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("name", name);
        object.put("surname", surname);
        object.put("patronymic", patronymic);
        object.put("userType", userType.type);

        return object;
    }
}
