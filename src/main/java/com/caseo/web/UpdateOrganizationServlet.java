package com.caseo.web;

import com.caseo.app.ApplicationContext;
import com.caseo.app.InternalServices;
import com.caseo.domain.model.*;
import com.caseo.domain.service.*;
import com.caseo.web.helper.OrganizationSaveHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/updateOrganization")
public class UpdateOrganizationServlet extends HttpServlet {

    private OrganizationService organizationService;
    private OrganizationSaveHelper saveHelper;


    @Override
    public void init() {
        ApplicationContext context = (ApplicationContext) getServletContext()
                .getAttribute("appContext");
        InternalServices services = context.internalServices();

        organizationService = services.organizationService();
        OrganizationAddressService organizationAddressService = services.organizationAddressService();
        OrganizationSignerService organizationSignerService = services.organizationSignerService();
        OrganizationContactService organizationContactService = services.organizationContactService();
        saveHelper = new OrganizationSaveHelper(
                organizationAddressService,
                organizationSignerService,
                organizationContactService
        );
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            // Обновляем существующую организацию
            int savedOrgId = updateOrganization(req);

            resp.sendRedirect("portal?mode=edit&orgId=" + savedOrgId);

        } catch (Exception e) {
            getServletContext().log("Ошибка при обновлении организации", e);
            req.setAttribute("error", "Ошибка: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/pages/organization-form.jsp").forward(req, resp);
        }
    }

    private int updateOrganization(HttpServletRequest req) throws Exception {
        String orgIdParam = req.getParameter("orgId");
        int orgId = Integer.parseInt(orgIdParam);

        Organization org = new Organization(
                orgId,
                req.getParameter("organization_full_name"),
                req.getParameter("organization_short_name"),
                req.getParameter("organization_type_activity"),
                Boolean.parseBoolean(req.getParameter("opo_single_territory"))
        );

        Organization savedOrg = organizationService.save(org);
        int savedOrgId = savedOrg.organizationId();

        saveHelper.saveRelatedEntities(req, savedOrgId);
        return savedOrgId;
    }
}