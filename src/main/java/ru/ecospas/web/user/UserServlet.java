package ru.ecospas.web.user;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ecospas.domain.model.User;
import ru.ecospas.domain.service.UserAdminService;

@WebServlet("/user")
public class UserServlet extends HttpServlet {

    private final UserAdminService service = new UserAdminService();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

        String mode = req.getParameter("mode");

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