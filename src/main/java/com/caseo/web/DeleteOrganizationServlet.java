package com.caseo.web;

import com.caseo.app.ApplicationContext;
import com.caseo.app.InternalServices;
import com.caseo.domain.model.*;
import com.caseo.domain.service.ChildService;
import com.caseo.domain.service.ObjectDeleteService;
import com.caseo.domain.service.OrganizationDeleteService;
import com.caseo.domain.service.ParentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

import static com.caseo.web.util.RequestUtils.paramInt;

@WebServlet("/deleteOrganization")
public class DeleteOrganizationServlet extends HttpServlet {

    private ParentService<Organization> organizationService;
    private OrganizationDeleteService orgDeleteService;
    private ChildService<ObjectModel> objectService;
    private ObjectDeleteService objectDeleteService;

    @Override
    public void init() {
        ApplicationContext context = (ApplicationContext) getServletContext()
                .getAttribute("appContext");

        InternalServices services = context.internalServices();
        this.organizationService = services.getParentService(Organization.class);
        this.objectService = services.getChildService(ObjectModel.class);
        orgDeleteService = new OrganizationDeleteService(services);
        objectDeleteService = new ObjectDeleteService(services);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {

        try {
            int orgId = paramInt(req, "orgId");

            if (orgId == 0) {
                throw new ServletException("orgId is required");
            }

            orgDeleteService.delete(orgId);

            //ОБЪЕКТЫ
            List<ObjectModel> objects = objectService.getManyByParentId(orgId);

            for (ObjectModel obj : objects) {
                objectDeleteService.delete(obj.getId());
            }

            //САМА ОРГАНИЗАЦИЯ (В КОНЦЕ)
            organizationService.deleteById(orgId);

            resp.sendRedirect("/");

        } catch (Exception e) {
            getServletContext().log("Ошибка при удалении организации", e);
            throw new ServletException("Ошибка при удалении организации", e);
        }
    }
}