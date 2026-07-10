package ru.ecospas.domain.repository;

import ru.ecospas.domain.model.User;

import java.util.Optional;

public interface UserRepository extends BaseRepository<User> {

    Optional<User> findByLogin(String login);
}