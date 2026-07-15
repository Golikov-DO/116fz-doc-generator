package ru.ecospas.web.user;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.domain.model.Role;
import ru.ecospas.domain.model.User;
import ru.ecospas.domain.service.UserAdminService;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class RegistrationService {

    private final UserAdminService userAdminService;
    private final Validator validator;

    public void register(RegistrationForm form) {
        Set<ConstraintViolation<RegistrationForm>> violations =
                validator.validate(form);

        if (!violations.isEmpty()) {
            throw new IllegalArgumentException(
                    violations.iterator().next().getMessage());
        }
        User user = new User();

        user.setLogin(form.getLogin());
        user.setEmail(form.getEmail());
        user.setPassword(form.getPassword());
        user.setRole(Role.USER);

        userAdminService.saveWithPassword(user);
    }
}