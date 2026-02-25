package com.caseo.web;

import com.caseo.app.ApplicationContext;
import com.caseo.app.InternalServices;
import com.caseo.domain.model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/organization")
public class OrganizationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String mode = req.getParameter("mode"); // view, edit, create
        String orgId = req.getParameter("orgId");

        req.setAttribute("mode", mode);

        try {
            ApplicationContext context = (ApplicationContext) getServletContext()
                    .getAttribute("appContext");
            InternalServices services = context.internalServices();

            // Для view и edit загружаем данные
            if (("view".equals(mode) || "edit".equals(mode)) && orgId != null && !orgId.isEmpty()) {
                int id = Integer.parseInt(orgId);

                Organization org = services.organizationService().getById(id);
                OrganizationAddress addr = services.organizationAddressService()
                        .getByOrganizationId(id);
                OrganizationSigner signer = services.organizationSignerService()
                        .getByOrganizationId(id);
                List<OrganizationContact> contacts = services.organizationContactService()
                        .getByOrganizationId(id);

                req.setAttribute("organization", org);
                req.setAttribute("address", addr);
                req.setAttribute("signer", signer);
                req.setAttribute("contacts", contacts);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Определяем, откуда пришел запрос
        String requestedWith = req.getHeader("X-Requested-With");

        if ("XMLHttpRequest".equals(requestedWith)) {
            // AJAX запрос из агрегатора - отдаем только фрагмент
            req.getRequestDispatcher("/WEB-INF/fragments/organization/organization.jsp")
                    .forward(req, resp);
        } else {
            // Прямой запрос - отдаем полную страницу
            req.getRequestDispatcher("/WEB-INF/pages/organization-page.jsp")
                    .forward(req, resp);
        }
    }
}