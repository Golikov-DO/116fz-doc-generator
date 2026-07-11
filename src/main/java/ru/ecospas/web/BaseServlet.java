package ru.ecospas.web;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ecospas.domain.model.Organization;
import ru.ecospas.domain.model.User;
import ru.ecospas.domain.repository.OrganizationRepository;
import ru.ecospas.domain.service.SecurityService;

import java.io.IOException;

public abstract class BaseServlet extends HttpServlet {

    protected final SecurityService securityService;
    protected final OrganizationRepository organizationRepository;

    protected BaseServlet(
            SecurityService securityService,
            OrganizationRepository organizationRepository) {

        this.securityService = securityService;
        this.organizationRepository = organizationRepository;
    }

    protected Organization requireAccess(
            HttpServletRequest req,
            HttpServletResponse resp,
            int orgId) throws IOException {

        Organization org = organizationRepository
                .findById(orgId)
                .orElse(null);

        if (org == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        User user = (User) req.getSession().getAttribute("user");

        if (!securityService.hasAccess(user, org)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }

        return org;
    }
}
