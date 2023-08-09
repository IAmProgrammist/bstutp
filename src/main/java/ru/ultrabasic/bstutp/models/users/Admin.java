package ru.ultrabasic.bstutp.models.users;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Value;

@Entity
@DiscriminatorValue("1")
@NoArgsConstructor
public class Admin extends User {

    public Admin(Long id, String login, String password, String name, String surname, String patronymic) {
        super(id, login, password, name, surname, patronymic, Roles.ADMIN);
    }
}
