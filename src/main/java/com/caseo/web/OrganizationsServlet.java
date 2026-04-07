package com.caseo.web;

import com.caseo.app.ApplicationContext;
import com.caseo.app.InternalServices;
import com.caseo.domain.model.Organization;
import com.caseo.domain.model.User;
import com.caseo.domain.service.OrganizationSecurityService;
import com.caseo.domain.service.ParentService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

@WebServlet("/home")
public class OrganizationsServlet extends HttpServlet {

    private InternalServices services;
    private final OrganizationSecurityService securityService = new OrganizationSecurityService();

    @Override
    public void init() {
        ApplicationContext context = (ApplicationContext) getServletContext()
                .getAttribute("appContext");
        services = context.internalServices();
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            ParentService<Organization> orgService =
                    services.getParentService(Organization.class);

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
            getServletContext().log("Ошибка в HomeServlet", e);
        }
    }
}