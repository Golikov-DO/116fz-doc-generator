package ru.ecospas.web.mapper.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.User;
import ru.ecospas.web.dto.request.user.SaveUserRequest;

@Component
@RequiredArgsConstructor
public class UserRequestMapper {

    private final PasswordEncoder passwordEncoder;

    public void toUser(SaveUserRequest request, User user) {
        user.setLogin(request.login());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(request.role());
    }
}