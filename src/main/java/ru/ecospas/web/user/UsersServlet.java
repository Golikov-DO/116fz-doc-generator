package ru.ecospas.web.user;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.User;
import ru.ecospas.domain.service.UserAdminService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UsersServlet extends HttpServlet {

    private final UserAdminService service;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

        List<User> users = service.getAllUsers();

        req.setAttribute("users", users);
        req.setAttribute("mode", null);

        req.setAttribute("contentPage", "/WEB-INF/pages/users-page.jsp");

        try {
            req.getRequestDispatcher("/WEB-INF/layout.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}