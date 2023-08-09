package ru.ultrabasic.bstutp.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.ultrabasic.bstutp.models.users.User;

public interface UserRepository extends CrudRepository<User, Long> {
    public User findByLogin(String login);
}
