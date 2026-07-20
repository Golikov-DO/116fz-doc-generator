package ru.ecospas.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.domain.model.Role;
import ru.ecospas.domain.model.User;
import ru.ecospas.domain.repository.UserRepository;
import ru.ecospas.web.dto.request.user.SaveUserRequest;
import ru.ecospas.web.mapper.user.UserRequestMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository repository;
    private final UserRequestMapper userRequestMapper;
    private final PasswordEncoder passwordEncoder;

    public User findByLogin(String login) {
        return repository.findByLogin(login)
                .orElse(null);
    }

    public List<User> findAll() {
        return repository.findAll();
    }
    public User load(Integer id) {
        return repository.findById(id)
                .orElse(null);
    }

    @Transactional
    public User create(SaveUserRequest request) {
        User user = new User();
        return save(request, user);
    }

    @Transactional
    public User register(String login, String password, String email) {
        if (repository.findByLogin(login).isPresent()) {
            throw new IllegalArgumentException("Логин уже занят");
        }
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        user.setEmail(email);
        user.setRole(Role.USER);

        return repository.save(user);
    }

    @Transactional
    public User save(SaveUserRequest request, User user) {
        userRequestMapper.toUser(request, user);
        return repository.save(user);
    }

    @Transactional
    public User save(User user) {
        return repository.save(user);
    }

    @Transactional
    public User update(Integer id, SaveUserRequest request) {
        User user = load(id);
        if (user == null) {
            return null;
        }
        return save(request, user);
    }

    @Transactional
    public User updateUser(User user, String login, String email, String password, Role role) {
        user.setLogin(login);
        user.setEmail(email);
        user.setRole(role);
        if (password != null && !password.isBlank()) {
            user.setPassword(passwordEncoder.encode(password));
        }
        return repository.save(user);
    }

    @Transactional
    public void delete(Integer id) {
        User user = load(id);
        if (user == null) {
            return;
        }
        repository.delete(user);
    }

    @Transactional
    public User updateProfile(User user, String login, String email, String password) {
        user.setLogin(login);
        user.setEmail(email);
        if (password != null && !password.isBlank()) {
            user.setPassword(passwordEncoder.encode(password));
        }
        return repository.save(user);
    }
}