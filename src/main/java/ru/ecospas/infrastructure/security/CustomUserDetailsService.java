package ru.ecospas.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.ecospas.domain.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String login)
            throws UsernameNotFoundException {

        return repository.findByLogin(login)
                .map(UserPrincipal::new)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Пользователь '%s' не найден".formatted(login)));
    }
}