package ru.ecospas.web.user;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ecospas.domain.service.UserAdminService;

import java.io.IOException;

@SuppressWarnings("unused") // Managed via dynamic registration in ServletAutoRegistration
public class DeleteUserServlet extends HttpServlet {

    private final UserAdminService adminService = new UserAdminService();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));

        adminService.delete(id);

        resp.sendRedirect("/users");
    }
}