package ru.ecospas.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.ecospas.web.user.RegistrationForm;

import java.util.Objects;

public class PasswordsMatchValidator
        implements ConstraintValidator<PasswordsMatch, RegistrationForm> {

    @Override
    public boolean isValid(
            RegistrationForm form,
            ConstraintValidatorContext context) {

        if (form == null) {
            return true;
        }

        return Objects.equals(
                form.getPassword(),
                form.getConfirmPassword());
    }
}