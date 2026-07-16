package ru.ecospas.web.mapper.user;

import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.User;
import ru.ecospas.web.dto.request.user.SaveUserRequest;

@Component
public class UserRequestMapper {

    public void toUser(
            SaveUserRequest request,
            User user
    ) {

        user.setLogin(request.login());

        user.setEmail(request.email());

        user.setPassword(request.password());

        user.setRole(request.role());
    }
}