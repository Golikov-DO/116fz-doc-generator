package ru.ecospas.web.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ru.ecospas.domain.model.Role;

public record UpdateUserRequest(
        @NotBlank
        @Size(min = 4, max = 30)
        String login,

        @NotBlank
        @Email
        String email,

        @Size(min = 8, max = 100)
        String password,

        Role role
) {
}