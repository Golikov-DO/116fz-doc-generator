package ru.ecospas.web.dto.response.user;

import ru.ecospas.domain.model.Role;

public record UserListResponse(

        Integer id,
        String login,
        Role role

) {
}