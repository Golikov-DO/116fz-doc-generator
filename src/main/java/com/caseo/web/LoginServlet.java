package com.caseo.web;

import com.caseo.domain.model.User;
import com.caseo.domain.service.UserAdminService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UserAdminService service = new UserAdminService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

        try {
            req.setAttribute("contentPage", "/WEB-INF/pages/login.jsp");
            req.getRequestDispatcher("/WEB-INF/layout.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String login = req.getParameter("login");
        String password = req.getParameter("password");

        User user = service.findByLogin(login);

        if (user != null && user.getPassword().equals(password)) {

            req.getSession().setAttribute("user", user);
            resp.sendRedirect("/home");

        } else {
            resp.sendRedirect("/login");
        }
    }
}