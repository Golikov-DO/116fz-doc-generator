package ru.ecospas.web.dto.response.auth;

public record CurrentUserResponse(

        Integer id,
        String login,
        String email,
        String role

) {
}