package ru.ecospas.web.auth;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ecospas.domain.model.User;
import ru.ecospas.domain.service.UserAdminService;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserAdminService service = new UserAdminService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String login = req.getParameter("login");
        String password = req.getParameter("password");

        User user = service.findByLogin(login);

        if (user != null && password != null && password.equals(user.getPassword())) {

            req.getSession().setAttribute("user", user);
            resp.sendRedirect("/home");

        } else {
            resp.sendRedirect("/?login=true&error=1");
        }
    }
}