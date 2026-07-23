package ru.ecospas.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.ecospas.domain.model.Role;
import ru.ecospas.domain.model.User;
import ru.ecospas.domain.repository.UserRepository;
import ru.ecospas.domain.service.UserService;
import ru.ecospas.web.dto.request.user.SaveUserRequest;
import ru.ecospas.web.mapper.user.UserRequestMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserRequestMapper userRequestMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private SaveUserRequest createRequest() {
        return new SaveUserRequest(
                "login",
                "user@test.com",
                "password123",
                Role.USER
        );
    }

    private User createUser() {
        User user = new User();
        user.setId(1);
        user.setLogin("oldLogin");
        user.setEmail("old@test.com");
        user.setPassword("oldPassword");
        user.setRole(Role.USER);
        return user;
    }

    @Test
    void shouldFindByLogin() {

        User user = createUser();

        when(repository.findByLogin("login")).thenReturn(Optional.of(user));

        User result = userService.findByLogin("login");

        assertEquals(user, result);
    }

    @Test
    void shouldReturnNullWhenLoginNotFound() {

        when(repository.findByLogin("login")).thenReturn(Optional.empty());

        assertNull(userService.findByLogin("login"));
    }

    @Test
    void shouldReturnAllUsers() {

        List<User> users = List.of(createUser(), createUser());

        when(repository.findAll()).thenReturn(users);

        List<User> result = userService.findAll();

        assertEquals(users, result);

        verify(repository).findAll();
    }

    @Test
    void shouldLoadUser() {

        User user = createUser();

        when(repository.findById(1)).thenReturn(Optional.of(user));

        User result = userService.load(1);

        assertEquals(user, result);
    }

    @Test
    void shouldReturnNullWhenUserNotFound() {

        when(repository.findById(1)).thenReturn(Optional.empty());

        assertNull(userService.load(1));
    }

    @Test
    void shouldCreateUser() {

        SaveUserRequest request = createRequest();

        when(repository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        userService.create(request);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        verify(repository).save(captor.capture());

        verify(userRequestMapper).toUser(request, captor.getValue());
    }

    @Test
    void shouldRegisterUser() {

        when(repository.findByLogin("login")).thenReturn(Optional.empty());

        when(passwordEncoder.encode("password123"))
                .thenReturn("encodedPassword");

        when(repository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        User result = userService.register(
                "login",
                "password123",
                "user@test.com"
        );

        assertEquals("login", result.getLogin());
        assertEquals("user@test.com", result.getEmail());
        assertEquals(Role.USER, result.getRole());
        assertEquals("encodedPassword", result.getPassword());

        verify(repository).save(any(User.class));
    }

    @Test
    void shouldThrowWhenLoginAlreadyExists() {

        when(repository.findByLogin("login"))
                .thenReturn(Optional.of(createUser()));

        assertThrows(
                IllegalArgumentException.class,
                () -> userService.register(
                        "login",
                        "password123",
                        "user@test.com"
                )
        );

        verify(repository, never()).save(any());
    }

    @Test
    void shouldUpdateUser() {

        SaveUserRequest request = createRequest();

        User user = createUser();

        when(repository.findById(1)).thenReturn(Optional.of(user));

        when(repository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        User result = userService.update(1, request);

        assertNotNull(result);

        verify(userRequestMapper).toUser(request, user);

        verify(repository).save(user);
    }

    @Test
    void shouldReturnNullWhenUpdatingMissingUser() {

        SaveUserRequest request = createRequest();

        when(repository.findById(1)).thenReturn(Optional.empty());

        assertNull(userService.update(1, request));

        verify(repository, never()).save(any());
    }

    @Test
    void shouldUpdateUserWithPassword() {

        User user = createUser();

        when(passwordEncoder.encode("newPassword"))
                .thenReturn("encodedPassword");

        when(repository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        User result = userService.updateUser(
                user,
                "newLogin",
                "new@test.com",
                "newPassword",
                Role.ADMIN
        );

        assertEquals("newLogin", result.getLogin());
        assertEquals("new@test.com", result.getEmail());
        assertEquals(Role.ADMIN, result.getRole());
        assertEquals("encodedPassword", result.getPassword());

        verify(repository).save(user);
    }

    @Test
    void shouldUpdateUserWithoutPassword() {

        User user = createUser();

        when(repository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        User result = userService.updateUser(
                user,
                "newLogin",
                "new@test.com",
                "",
                Role.ADMIN
        );

        assertEquals("oldPassword", result.getPassword());
        assertEquals("newLogin", result.getLogin());
        assertEquals("new@test.com", result.getEmail());
        assertEquals(Role.ADMIN, result.getRole());

        verify(passwordEncoder, never()).encode(any());

        verify(repository).save(user);
    }

    @Test
    void shouldUpdateProfileWithPassword() {

        User user = createUser();

        when(passwordEncoder.encode("newPassword"))
                .thenReturn("encodedPassword");

        when(repository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        User result = userService.updateProfile(
                user,
                "newLogin",
                "new@test.com",
                "newPassword"
        );

        assertEquals("newLogin", result.getLogin());
        assertEquals("new@test.com", result.getEmail());
        assertEquals("encodedPassword", result.getPassword());

        verify(repository).save(user);
    }

    @Test
    void shouldUpdateProfileWithoutPassword() {

        User user = createUser();

        when(repository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        User result = userService.updateProfile(
                user,
                "newLogin",
                "new@test.com",
                ""
        );

        assertEquals("oldPassword", result.getPassword());
        assertEquals("newLogin", result.getLogin());
        assertEquals("new@test.com", result.getEmail());

        verify(passwordEncoder, never()).encode(any());

        verify(repository).save(user);
    }

    @Test
    void shouldDeleteUser() {

        User user = createUser();

        when(repository.findById(1)).thenReturn(Optional.of(user));

        userService.delete(1);

        verify(repository).delete(user);
    }

    @Test
    void shouldIgnoreDeletingMissingUser() {

        when(repository.findById(1)).thenReturn(Optional.empty());

        userService.delete(1);

        verify(repository, never()).delete(any());
    }
}