package ru.ecospas.web.organization;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.Organization;
import ru.ecospas.domain.model.User;
import ru.ecospas.domain.repository.OrganizationRepository;
import ru.ecospas.domain.service.CurrentUserService;
import ru.ecospas.domain.service.SecurityService;
import ru.ecospas.web.BaseServlet;

import java.util.List;

@Component
public class OrganizationsServlet extends BaseServlet {

    private final CurrentUserService currentUserService;

    public OrganizationsServlet(
            SecurityService securityService,
            OrganizationRepository organizationRepository,
            CurrentUserService currentUserService
    ) {
        super(securityService, organizationRepository, currentUserService);
        this.currentUserService = currentUserService;
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            User user = currentUserService.currentUser();

            if (user == null) {
                resp.sendRedirect("/");
                return;
            }

            List<Organization> organizations =
                    securityService.getOrganizationsForUser(user);

            req.setAttribute("organizations", organizations);
            req.setAttribute("mode", null);

            req.setAttribute("contentPage", "/WEB-INF/pages/organization-page.jsp");
            req.getRequestDispatcher("/WEB-INF/layout.jsp").forward(req, resp);

        } catch (Exception e) {
            getServletContext().log("Error at HomeServlet", e);
        }
    }
}