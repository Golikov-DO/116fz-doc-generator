package ru.ecospas.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ecospas.app.ApplicationContext;
import ru.ecospas.app.InternalServices;
import ru.ecospas.domain.model.Organization;
import ru.ecospas.domain.model.User;
import ru.ecospas.domain.service.SecurityService;
import ru.ecospas.infrastructure.db.HibernateUtil;

import java.io.IOException;

public abstract class BaseServlet extends HttpServlet {
    protected InternalServices services;

    @Override
    public void init() {
        ApplicationContext context = (ApplicationContext) getServletContext()
                .getAttribute("appContext");
        this.services = context.internalServices();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (!HibernateUtil.isDbAvailable()) {
            req.getRequestDispatcher("/WEB-INF/views/db-error.jsp")
                    .forward(req, resp);
            return;
        }

        super.service(req, resp);
    }

    protected Organization requireAccess(HttpServletRequest req, HttpServletResponse resp, int orgId)
            throws IOException {

        var org = services.getParentService(Organization.class).getOneById(orgId);

        var user = (User) req.getSession().getAttribute("user");

        var securityService = new SecurityService();

        if (!securityService.hasAccess(user, org)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }

        return org;
    }
}
