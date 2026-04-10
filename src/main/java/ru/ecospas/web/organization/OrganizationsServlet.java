package ru.ecospas.web.organization;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ecospas.domain.model.Organization;
import ru.ecospas.domain.model.User;
import ru.ecospas.domain.service.OrganizationSecurityService;
import ru.ecospas.web.BaseServlet;

import java.util.List;

@WebServlet("/home")
public class OrganizationsServlet extends BaseServlet {

    private final OrganizationSecurityService securityService = new OrganizationSecurityService();

    @Override
    public void init() {
        super.init();
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            User user = (User) req.getSession().getAttribute("user");
            Boolean guest = (Boolean) req.getSession().getAttribute("guest");

            List<Organization> organizations;

            if (guest != null && guest) {
                organizations = List.of();
            } else if (user != null) {
                organizations = securityService.getOrganizationsForUser(user);
            } else {
                resp.sendRedirect("/");
                return;
            }

            req.setAttribute("organizations", organizations);
            req.setAttribute("mode", null);

            req.setAttribute("contentPage", "/WEB-INF/pages/organization-page.jsp");
            req.getRequestDispatcher("/WEB-INF/layout.jsp").forward(req, resp);

        } catch (Exception e) {
            getServletContext().log("Error at HomeServlet", e);
        }
    }
}