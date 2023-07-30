package ru.ultrabasic.bstutp.data.models;

public class UsersRow {
    private Integer id;

    public Integer getId() {
        return id;
    }

    public UsersRow(int id, String login, String password, UserTypes userType, String name, String surname, String patronymic) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.userType = userType;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
    }

    private String login;
    private String password;
    private UserTypes userType;
    private String name;
    private String surname;
    private String patronymic;

    public UsersRow(String login, String password, UserTypes userType, String name, String surname, String patronymic) {
        this.login = login;
        this.password = password;
        this.userType = userType;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
    }

    public UsersRow(String login, String password, UserTypes userType, String name, String surname) {
        this.login = login;
        this.password = password;
        this.userType = userType;
        this.name = name;
        this.surname = surname;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public UserTypes getUserType() {
        return userType;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPatronymic() {
        return patronymic;
    }
}
