package ru.ecospas.web.user;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ecospas.domain.model.Role;
import ru.ecospas.domain.model.User;
import ru.ecospas.domain.service.UserAdminService;

import java.io.IOException;

@SuppressWarnings("unused") // Managed via dynamic registration in ServletAutoRegistration
public class SaveUserServlet extends HttpServlet {

    private final UserAdminService service = new UserAdminService();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String idStr = req.getParameter("id");

        User user;

        if (idStr != null && !idStr.isEmpty()) user = service.findById(Integer.parseInt(idStr));
        else user = new User();

        String login = req.getParameter("login");

        User existing = service.findByLogin(login);

        if (existing != null && (user.getId() == null || !existing.getId().equals(user.getId()))) {
            resp.sendRedirect("/?error=login_taken");
            return;
        }

        user.setLogin(req.getParameter("login"));
        user.setPassword(req.getParameter("password"));
        String roleParam = req.getParameter("role");

        if (roleParam != null) user.setRole(Role.valueOf(roleParam));

        try {
            service.save(user);
        } catch (Exception e) {
            resp.sendRedirect("/?error=login_taken");
        }

        String source = req.getParameter("source");

        if ("register".equals(source)) {
            resp.sendRedirect("/?login=true");
        } else {
            resp.sendRedirect("/user?mode=view&id=" + user.getId());
        }
    }
}