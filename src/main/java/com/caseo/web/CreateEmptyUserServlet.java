package com.caseo.web;

import com.caseo.domain.model.Role;
import com.caseo.domain.model.User;
import com.caseo.domain.service.UserAdminService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/createEmptyUser")
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