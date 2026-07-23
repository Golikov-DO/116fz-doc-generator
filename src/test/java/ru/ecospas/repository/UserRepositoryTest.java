package ru.ecospas.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.config.AbstractIntegrationTest;
import ru.ecospas.domain.model.Role;
import ru.ecospas.domain.model.User;
import ru.ecospas.domain.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
class UserRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveUser() {

        User saved = userRepository.save(createUser());

        assertNotNull(saved.getId());
        assertEquals(Role.USER, saved.getRole());
    }

    @Test
    void shouldFindUserById() {

        User user = userRepository.save(createUser());

        User found = userRepository.findById(user.getId())
                .orElseThrow();

        assertEquals(user.getId(), found.getId());
        assertEquals(user.getLogin(), found.getLogin());
    }

    @Test
    void shouldUpdateUser() {

        User user = userRepository.save(createUser());

        user.setEmail("updated@test.com");

        userRepository.save(user);

        User updated = userRepository.findById(user.getId())
                .orElseThrow();

        assertEquals("updated@test.com", updated.getEmail());
    }

    @Test
    void shouldDeleteUser() {

        User user = userRepository.save(createUser());

        Integer id = user.getId();

        userRepository.delete(user);

        assertTrue(userRepository.findById(id).isEmpty());
    }

    @Test
    void shouldFindUserByLogin() {

        User user = userRepository.save(createUser());

        Optional<User> found = userRepository.findByLogin(user.getLogin());

        assertTrue(found.isPresent());
        assertEquals(user.getId(), found.get().getId());
    }

    @Test
    void shouldReturnEmptyWhenLoginNotExists() {

        Optional<User> found = userRepository.findByLogin("unknown_login");

        assertTrue(found.isEmpty());
    }

    private User createUser() {

        String suffix = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8);

        User user = new User();
        user.setLogin("user" + suffix);
        user.setEmail("user" + suffix + "@test.com");
        user.setPassword("password");
        user.setRole(Role.USER);

        return user;
    }
}