package ru.ecospas.web.organization;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import ru.ecospas.domain.repository.OrganizationRepository;
import ru.ecospas.domain.service.OrganizationDeleteService;
import ru.ecospas.domain.service.SecurityService;
import ru.ecospas.web.BaseServlet;

import static ru.ecospas.web.util.RequestUtils.paramInt;

@Component
public class DeleteOrganizationServlet extends BaseServlet {

    private final OrganizationDeleteService orgDeleteService;


    public DeleteOrganizationServlet(
            SecurityService securityService,
            OrganizationRepository organizationRepository,
            OrganizationDeleteService orgDeleteService) {
        super(securityService, organizationRepository);

        this.orgDeleteService = orgDeleteService;

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {

        try {

            int orgId = paramInt(req, "orgId");

            if (orgId == 0) {
                throw new ServletException("orgId is required");
            }

            if (requireAccess(req, resp, orgId) == null) {
                return;
            }

            orgDeleteService.delete(orgId);

            resp.sendRedirect("/");

        } catch (Exception e) {

            getServletContext().log("Error deleting Organization", e);

            throw new ServletException("Error deleting Organization", e);
        }
    }
}