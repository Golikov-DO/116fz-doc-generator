package com.caseo.web;

import com.caseo.app.ApplicationContext;
import com.caseo.app.InternalServices;
import com.caseo.domain.model.Organization;
import com.caseo.domain.service.ParentService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

@WebServlet(value = "", loadOnStartup = 1)
public class HomeServlet extends HttpServlet {

    private InternalServices services;

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

            List<Organization> organizations = orgService.getMany();

            req.setAttribute("organizations", organizations);
            req.setAttribute("mode", null);

            req.setAttribute("contentPage", "/WEB-INF/pages/organization-page.jsp");
            req.getRequestDispatcher("/WEB-INF/layout.jsp").forward(req, resp);

        } catch (Exception e) {
            getServletContext().log("Ошибка в HomeServlet", e);
        }
    }
}