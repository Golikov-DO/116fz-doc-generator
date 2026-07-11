package ru.ecospas.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.domain.model.User;
import ru.ecospas.domain.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserAdminService {

    private final UserRepository repository;

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public void save(User user) {
        repository.save(user);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public User findById(int id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public User findByLogin(String login) {
        return repository.findByLogin(login).orElse(null);
    }

    @Transactional(readOnly = true)
    public User login(String login, String password) {

        User user = findByLogin(login);

        if (user != null && password.equals(user.getPassword())) {
            return user;
        }

        return null;
    }
}