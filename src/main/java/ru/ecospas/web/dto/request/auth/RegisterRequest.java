package ru.ecospas.web.dto.request.auth;

public record RegisterRequest(

        String login,
        String email,
        String password,
        String confirmPassword

) {
}