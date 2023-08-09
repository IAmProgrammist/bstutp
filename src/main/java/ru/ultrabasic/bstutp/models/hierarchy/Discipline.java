package ru.ultrabasic.bstutp.models.hierarchy;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Discipline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false, unique = true)
    String name;
    @ManyToMany
    @JoinTable(name = "discipline_competence",
    joinColumns = {@JoinColumn(name = "discipline_id")},
    inverseJoinColumns = {@JoinColumn(name = "competence_id")})
    Set<Competence> competences;
}
