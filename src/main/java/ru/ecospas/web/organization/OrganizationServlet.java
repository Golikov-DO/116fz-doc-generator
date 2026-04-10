package ru.ecospas.web.organization;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ecospas.web.BaseServlet;
import ru.ecospas.web.helper.DataLoader;
import ru.ecospas.web.helper.DataLoader.OrganizationData;

import java.io.IOException;

@WebServlet("/organization")
public class OrganizationServlet extends BaseServlet {

    private DataLoader dataLoader;

    @Override
    public void init() {
        super.init();
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
            }

        } catch (Exception e) {
            getServletContext().log("Error loading Organization", e);
        }

        String requestedWith = req.getHeader("X-Requested-With");

        if ("XMLHttpRequest".equals(requestedWith)) {
            req.getRequestDispatcher("/WEB-INF/fragments/organization/organization.jsp")
                    .forward(req, resp);
        } else {
            req.setAttribute("contentPage", "/WEB-INF/pages/organization-page.jsp");
            req.getRequestDispatcher("/WEB-INF/layout.jsp").forward(req, resp);
        }
    }
}