package ru.ultrabasic.bstutp.data.models.records;

import ru.ultrabasic.bstutp.data.models.UserTypes;

public record User(Integer id, String login, String password, UserTypes userType, String name, String surname, String patronymic) {}
