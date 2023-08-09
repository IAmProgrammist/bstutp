package ru.ultrabasic.bstutp.models.hierarchy;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class CompetenceGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String name;
    @OneToMany
    @JoinColumn(name = "competence_group_id", nullable = false)
    Set<Competence> competences;
}
