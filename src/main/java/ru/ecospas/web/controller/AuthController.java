package ru.ecospas.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.ecospas.domain.service.RegistrationService;
import ru.ecospas.infrastructure.security.UserPrincipal;
import ru.ecospas.web.dto.request.auth.RegisterRequest;
import ru.ecospas.web.dto.response.auth.AuthMessageResponse;
import ru.ecospas.web.dto.response.auth.CurrentUserResponse;
import ru.ecospas.web.dto.response.auth.LoginAvailabilityResponse;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegistrationService registrationService;

    @PostMapping("/register")
    public AuthMessageResponse register(@RequestBody RegisterRequest request) {
        registrationService.register(request);
        return new AuthMessageResponse("User registered successfully");
    }

    @GetMapping("/check-login")
    public LoginAvailabilityResponse checkLogin(@RequestParam String login) {
        return new LoginAvailabilityResponse(registrationService.isLoginAvailable(login));
    }

    @GetMapping("/me")
    public CurrentUserResponse me() {
        Authentication authentication = SecurityContextHolder
                        .getContext()
                        .getAuthentication();
        if (authentication == null
                || !(authentication.getPrincipal() instanceof UserPrincipal principal)) {
            return null;
        }
        return new CurrentUserResponse(
                principal.getUser().getId(),
                principal.getUsername(),
                principal.getUser().getEmail(),
                principal.getUser().getRole().name()
        );
    }
}