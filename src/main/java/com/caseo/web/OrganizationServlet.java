package com.caseo.web;

import com.caseo.app.ApplicationContext;
import com.caseo.app.InternalServices;
import com.caseo.domain.model.ObjectModel;
import com.caseo.web.helper.DataLoader;
import com.caseo.web.helper.DataLoader.OrganizationData;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/organization")
public class OrganizationServlet extends HttpServlet {

    private DataLoader dataLoader;

    @Override
    public void init() {
        ApplicationContext context = (ApplicationContext) getServletContext()
                .getAttribute("appContext");
        InternalServices services = context.internalServices();
        dataLoader = new DataLoader(services);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String mode = req.getParameter("mode");
        String orgId = req.getParameter("orgId");

        if (orgId != null && !orgId.isEmpty()) {
            req.getSession().setAttribute("orgId", orgId);
        }
        if ((orgId == null || orgId.isEmpty()) && req.getSession().getAttribute("orgId") != null) {
            orgId = req.getSession().getAttribute("orgId").toString();
        }

        req.setAttribute("mode", mode);

        try {
            if (("view".equals(mode) || "edit".equals(mode)) && orgId != null && !orgId.isEmpty()) {
                int id = Integer.parseInt(orgId);

                OrganizationData data = dataLoader.loadOrganization(id);

                req.setAttribute("organization", data.org());
                req.setAttribute("address", data.addr());
                req.setAttribute("signer", data.signer());
                req.setAttribute("contacts", data.contacts());
                List<ObjectModel> objects = dataLoader.loadObjects(id);
                req.setAttribute("sidebarObjects", objects);
            }

        } catch (Exception e) {
            getServletContext().log("Ошибка при загрузке организации", e);
        }

        String requestedWith = req.getHeader("X-Requested-With");

        if ("XMLHttpRequest".equals(requestedWith)) {
            req.getRequestDispatcher("/WEB-INF/fragments/organization/organization.jsp")
                    .forward(req, resp);
        } else {
            req.setAttribute("contentPage", "/WEB-INF/pages/organization-page.jsp");
            req.getRequestDispatcher("/WEB-INF/template/layout.jsp").forward(req, resp);
        }
    }
}