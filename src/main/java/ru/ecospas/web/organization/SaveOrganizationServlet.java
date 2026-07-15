package ru.ecospas.web.organization;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.Organization;
import ru.ecospas.domain.model.User;
import ru.ecospas.domain.repository.OrganizationRepository;
import ru.ecospas.domain.service.CurrentUserService;
import ru.ecospas.domain.service.OrganizationService;
import ru.ecospas.domain.service.SecurityService;
import ru.ecospas.web.BaseServlet;

import static ru.ecospas.web.util.RequestUtils.paramInt;

@Component
public class SaveOrganizationServlet extends BaseServlet {

    private final OrganizationService organizationService;

    public SaveOrganizationServlet(
            SecurityService securityService,
            OrganizationRepository organizationRepository,
            OrganizationService organizationService,
            CurrentUserService currentUserService) {
        super(securityService, organizationRepository, currentUserService);
        this.organizationService = organizationService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            int orgId = paramInt(req, "orgId");
            User currentUser = currentUserService.currentUser();

            if (currentUser == null) {
                resp.sendRedirect("/");
                return;
            }

            Organization org;

            if (orgId > 0) {

                org = requireAccess(resp, orgId);

                if (org == null) {
                    return;
                }

            } else {

                org = organizationService.create(currentUser);
            }

            org = organizationService.save(req, org);

            resp.sendRedirect("/organization?mode=view&orgId=" + org.getId());
        } catch (Exception e) {
            getServletContext().log("Error saving Organization", e);
            throw new ServletException("Error saving Organization", e);
        }
    }
}