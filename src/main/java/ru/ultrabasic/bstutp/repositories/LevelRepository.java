package ru.ultrabasic.bstutp.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.ultrabasic.bstutp.models.hierarchy.Level;

public interface LevelRepository extends CrudRepository<Level, Long> {
}
