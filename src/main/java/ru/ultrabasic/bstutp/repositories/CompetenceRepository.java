package ru.ultrabasic.bstutp.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.ultrabasic.bstutp.models.hierarchy.Competence;

public interface CompetenceRepository extends CrudRepository<Competence, Long> {
}
