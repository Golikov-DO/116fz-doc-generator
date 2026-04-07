package com.caseo.web;

import com.caseo.domain.model.Role;
import com.caseo.domain.model.User;
import com.caseo.domain.service.UserAdminService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/saveUser")
public class SaveUserServlet extends HttpServlet {

    private final UserAdminService service = new UserAdminService();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String idStr = req.getParameter("id");

        User user;

        if (idStr != null && !idStr.isEmpty()) {
            user = service.findById(Integer.parseInt(idStr));
        } else {
            user = new User();
        }

        user.setLogin(req.getParameter("login"));
        user.setPassword(req.getParameter("password"));
        user.setRole(Role.valueOf(req.getParameter("role")));

        service.save(user);

        resp.sendRedirect("user?mode=view&id=" + user.getId());
    }
}