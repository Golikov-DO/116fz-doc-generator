package ru.ecospas.web.mapper.user;

import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.User;
import ru.ecospas.web.dto.response.user.UserListResponse;
import ru.ecospas.web.dto.response.user.UserResponse;

import java.util.List;

@Component
public class UserResponseMapper {

    public UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }
        return new UserResponse(
                user.getId(),
                user.getLogin(),
                user.getEmail(),
                user.getRole()
        );
    }

    public List<UserResponse> toResponses(List<User> users) {
        return users.stream().map(this::toResponse).toList();
    }

    public UserListResponse toListResponse(User user) {
        if (user == null) {
            return null;
        }
        return new UserListResponse(user.getId(), user.getLogin(), user.getRole());
    }

    public List<UserListResponse> toListResponses(List<User> users) {
        return users.stream().map(this::toListResponse).toList();
    }
}