package ru.ecospas.web.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
        @NotBlank
        @Size(min = 4, max = 30)
        String login,

        @NotBlank
        @Email
        String email,

        @Size(min = 8, max = 100)
        String password  // пустая строка = не менять
) {
}