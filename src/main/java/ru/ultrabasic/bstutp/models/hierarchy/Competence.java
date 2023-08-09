package ru.ultrabasic.bstutp.models.hierarchy;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Competence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    Long code;
    @Column(nullable = false)
    String name;
    @OneToMany
    @JoinColumn(name = "competence_id", nullable = false)
    Set<Indicator> indicators;
    @ManyToOne
    @JoinColumn(name = "level_id", nullable = false)
    Level level;
}
