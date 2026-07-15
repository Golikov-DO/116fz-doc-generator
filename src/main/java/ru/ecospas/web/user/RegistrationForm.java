package ru.ecospas.web.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import ru.ecospas.validation.PasswordsMatch;

@Getter
@Setter
@PasswordsMatch
public class RegistrationForm {

    @NotBlank(message = "Введите логин")
    @Size(min = 4, max = 30)
    private String login;

    @NotBlank(message = "Введите email")
    @Email(message = "Некорректный email")
    private String email;

    @NotBlank(message = "Введите пароль")
    @Size(min = 8, message = "Пароль должен содержать минимум 8 символов")
    private String password;

    @NotBlank(message = "Повторите пароль")
    private String confirmPassword;
}