package ru.ecospas.web.user;

import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.domain.model.Role;
import ru.ecospas.domain.model.User;
import ru.ecospas.domain.service.UserAdminService;
import ru.ecospas.web.dto.request.auth.RegisterRequest;

@Service
@RequiredArgsConstructor
@Transactional
public class RegistrationOldService {

    private final UserAdminService userAdminService;
    private final Validator validator;

    @Transactional
    public void register(RegisterRequest request) {

        if (!request.password().equals(request.confirmPassword())) {
            throw new IllegalArgumentException("Пароли не совпадают");
        }

        if (userAdminService.findByLogin(request.login()) != null) {
            throw new IllegalArgumentException("Логин уже существует");
        }

        User user = new User();

        user.setLogin(request.login());
        user.setEmail(request.email());
        user.setPassword(request.password());
        user.setRole(Role.USER);

        userAdminService.saveWithPassword(user);
    }

    @Transactional(readOnly = true)
    public boolean isLoginAvailable(String login) {
        return userAdminService.findByLogin(login) == null;
    }
}