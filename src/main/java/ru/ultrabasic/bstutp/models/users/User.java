package ru.ultrabasic.bstutp.models.users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type",
        discriminatorType = DiscriminatorType.INTEGER)
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    public enum Roles {
        ADMIN("ADMIN"),
        STUDENT("STUDENT"),
        TEACHER("TEACHER");

        Roles(String role) {
            this.role = role;
        }

        String role;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, unique = true)
    private String login;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String surname;
    private String patronymic;
    @Transient
    private Roles role;
}
