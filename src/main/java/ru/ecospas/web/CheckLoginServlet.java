package ru.ecospas.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ecospas.domain.service.UserAdminService;

import java.io.IOException;

@WebServlet("/check-login")
public class CheckLoginServlet extends HttpServlet {

    private final UserAdminService service = new UserAdminService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String login = req.getParameter("login");

        boolean exists = service.findByLogin(login) != null;

        resp.setContentType("text/plain");
        resp.getWriter().write(exists ? "taken" : "free");
    }
}