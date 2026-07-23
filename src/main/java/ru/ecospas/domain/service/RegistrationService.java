package ru.ecospas.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.domain.model.Role;
import ru.ecospas.domain.model.User;
import ru.ecospas.domain.repository.UserRepository;
import ru.ecospas.web.dto.request.auth.RegisterRequest;

@Service
@RequiredArgsConstructor
@Transactional
public class RegistrationService {

    private final UserRepository userRepository; // ← напрямую репозиторий
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(RegisterRequest request) {
        if (userRepository.findByLogin(request.login()).isPresent()) {
            throw new IllegalArgumentException("Логин уже существует");
        }

        User user = new User();
        user.setLogin(request.login());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public boolean isLoginAvailable(String login) {
        return userRepository.findByLogin(login).isEmpty();
    }
}