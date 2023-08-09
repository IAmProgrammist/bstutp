package ru.ultrabasic.bstutp.models.users;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Value;

@Entity
@DiscriminatorValue("2")
@NoArgsConstructor
public class Student extends User {
    //Group group;
    @Column(unique = true)
    Long recordId;

    public Student(Long id, String login, String password, String name, String surname, String patronymic, Long recordId) {
        super(id, login, password, name, surname, patronymic, Roles.STUDENT);

        this.recordId = recordId;
    }
}
