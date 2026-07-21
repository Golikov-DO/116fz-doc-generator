package ru.ecospas.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.ecospas.domain.model.User;
import ru.ecospas.domain.service.CurrentUserService;
import ru.ecospas.domain.service.UserService;
import ru.ecospas.web.dto.request.user.SaveUserRequest;
import ru.ecospas.web.dto.request.user.UpdateProfileRequest;
import ru.ecospas.web.dto.request.user.UpdateUserRequest;
import ru.ecospas.web.dto.response.user.UserListResponse;
import ru.ecospas.web.dto.response.user.UserResponse;
import ru.ecospas.web.mapper.user.UserResponseMapper;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;
    private final CurrentUserService currentUserService;
    private final UserResponseMapper responseMapper;

    @GetMapping
    public List<UserListResponse> getUsers() {
        return responseMapper.toListResponses(userService.findAll());
    }

    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable Integer id) {
        User user = userService.load(id);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        return responseMapper.toResponse(user);
    }

    @PostMapping
    public UserResponse createUser(@Valid @RequestBody SaveUserRequest request) {
        User user = userService.create(request);
        return responseMapper.toResponse(user);
    }

    @PutMapping("/{id}")
    public UserResponse updateUser(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateUserRequest request
    ) {
        User user = userService.load(id);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        return responseMapper.toResponse(
                userService.updateUser(user, request.login(), request.email(),
                        request.password(), request.role())
        );
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userService.delete(id);
    }

    @GetMapping("/me")
    public UserResponse getCurrentUser() {
        User user = currentUserService.requireCurrentUser();
        return responseMapper.toResponse(user);
    }

    @PutMapping("/me")
    public UserResponse updateCurrentUser(@Valid @RequestBody UpdateProfileRequest request) {
        User user = currentUserService.requireCurrentUser();
        User updated = userService.updateProfile(user, request.login(), request.email(),
                request.password());
        return responseMapper.toResponse(updated);
    }
}