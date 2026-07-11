package ru.ecospas.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ecospas.app.InternalServices;
import ru.ecospas.domain.model.Organization;
import ru.ecospas.domain.model.User;
import ru.ecospas.domain.service.SecurityService;

import java.io.IOException;

public abstract class BaseServlet extends HttpServlet {

    protected final InternalServices services;
    protected final SecurityService securityService;

    protected BaseServlet(InternalServices services, SecurityService securityService) {
        this.services = services;
        this.securityService = securityService;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        super.service(req, resp);
    }

    protected Organization requireAccess(HttpServletRequest req, HttpServletResponse resp, int orgId)
            throws IOException {

        var org = services.getParentService(Organization.class).getOneById(orgId);

        var user = (User) req.getSession().getAttribute("user");

        if (!securityService.hasAccess(user, org)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }

        return org;
    }
}
