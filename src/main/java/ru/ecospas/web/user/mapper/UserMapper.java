package ru.ecospas.web.user.mapper;

import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.Role;
import ru.ecospas.domain.model.User;
import ru.ecospas.web.user.RegistrationForm;

@Component
public class UserMapper {

    public User toEntity(RegistrationForm form) {

        User user = new User();

        user.setLogin(form.getLogin());
        user.setEmail(form.getEmail());
        user.setPassword(form.getPassword());
        user.setRole(Role.USER);

        return user;
    }
}