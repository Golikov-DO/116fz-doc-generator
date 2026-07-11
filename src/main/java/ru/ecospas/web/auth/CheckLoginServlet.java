package ru.ecospas.web.auth;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ecospas.domain.service.UserAdminService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CheckLoginServlet extends HttpServlet {

    private final UserAdminService service;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String login = req.getParameter("login");

        boolean exists = service.findByLogin(login) != null;

        resp.setContentType("text/plain");
        resp.getWriter().write(exists ? "taken" : "free");
    }
}