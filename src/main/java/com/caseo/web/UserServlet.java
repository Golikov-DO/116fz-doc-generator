package com.caseo.web;

import com.caseo.domain.model.User;
import com.caseo.domain.service.UserAdminService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/user")
public class UserServlet extends HttpServlet {

    private final UserAdminService service = new UserAdminService();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

        String mode = req.getParameter("mode");

        // 🔥 ЕСЛИ mode НЕТ → это список
        if (mode == null || mode.isEmpty()) {

            var users = service.getAllUsers();

            req.setAttribute("users", users);
            req.setAttribute("mode", null);
            req.setAttribute("contentPage", "/WEB-INF/pages/users-page.jsp");

            try {
                req.getRequestDispatcher("/WEB-INF/layout.jsp").forward(req, resp);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return;
        }

        // 🔥 ДАЛЬШЕ твоя старая логика
        String idStr = req.getParameter("id");

        User user = null;

        if (idStr != null && !idStr.isEmpty()) {
            int id = Integer.parseInt(idStr);
            user = service.findById(id);
        }

        req.setAttribute("user", user);
        req.setAttribute("mode", mode);
        req.setAttribute("contentPage", "/WEB-INF/pages/users-page.jsp");

        try {
            req.getRequestDispatcher("/WEB-INF/layout.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}