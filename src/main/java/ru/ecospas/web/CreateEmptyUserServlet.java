package ru.ecospas.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ecospas.domain.model.Role;
import ru.ecospas.domain.model.User;
import ru.ecospas.domain.service.UserAdminService;

import java.io.IOException;

@WebServlet("/create-empty-user")
public class CreateEmptyUserServlet extends HttpServlet {

    private final UserAdminService service = new UserAdminService();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        User user = new User();
        user.setLogin("");
        user.setPassword("");
        user.setRole(Role.USER);
        service.save(user);

        resp.sendRedirect("user?mode=edit&id=" + user.getId());
    }
}