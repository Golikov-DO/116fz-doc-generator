package ru.ecospas.web.user;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ecospas.domain.service.UserAdminService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class DeleteUserServlet extends HttpServlet {

    private final UserAdminService adminService;

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));

        adminService.delete(id);

        resp.sendRedirect("/users");
    }
}