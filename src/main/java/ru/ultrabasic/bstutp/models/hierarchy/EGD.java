package ru.ultrabasic.bstutp.models.hierarchy;

import jakarta.persistence.*;

@Entity
public class EGD {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false, unique = true)
    String name;
}
