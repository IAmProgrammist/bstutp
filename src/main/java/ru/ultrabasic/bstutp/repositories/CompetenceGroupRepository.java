package ru.ultrabasic.bstutp.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.ultrabasic.bstutp.models.hierarchy.Competence;
import ru.ultrabasic.bstutp.models.hierarchy.CompetenceGroup;

public interface CompetenceGroupRepository extends CrudRepository<CompetenceGroup, Long> {
}
