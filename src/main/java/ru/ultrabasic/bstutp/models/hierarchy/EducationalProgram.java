package ru.ultrabasic.bstutp.models.hierarchy;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class EducationalProgram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToMany
    @JoinTable
    Set<Competence> competences;
    @Column(nullable = false)
    String name;
}
