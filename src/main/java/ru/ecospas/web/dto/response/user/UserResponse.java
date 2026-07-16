package ru.ecospas.web.dto.response.user;

import ru.ecospas.domain.model.Role;

public record UserResponse(

        Integer id,

        String login,

        String email,

        Role role
) {
}