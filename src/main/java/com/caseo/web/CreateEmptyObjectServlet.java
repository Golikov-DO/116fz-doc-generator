package com.caseo.web;

import com.caseo.app.ApplicationContext;
import com.caseo.app.InternalServices;
import com.caseo.domain.model.ObjectModel;
import com.caseo.domain.model.Organization;
import com.caseo.domain.service.ChildService;
import com.caseo.domain.service.ParentService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import static com.caseo.web.util.RequestUtils.paramInt;

@WebServlet("/createEmptyObject")
public class CreateEmptyObjectServlet extends HttpServlet {

    private ChildService<ObjectModel> objectService;
    private ParentService<Organization> orgService;

    @Override
    public void init() {

        ApplicationContext context =
                (ApplicationContext) getServletContext().getAttribute("appContext");

        InternalServices services = context.internalServices();

        objectService = services.getChildService(ObjectModel.class);
        orgService = services.getParentService(Organization.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {

        try {

            int orgId = paramInt(req, "orgId");

            if (orgId == 0) {
                throw new ServletException("orgId is required");
            }

            Organization organization = orgService.getOneById(orgId);

            if (organization == null) {
                throw new ServletException("Organization not found: " + orgId);
            }

            // создаём пустой объект
            ObjectModel object = new ObjectModel();
            object.setOrganization(organization);

            objectService.save(object);

            resp.sendRedirect(
                    "objects?mode=edit&orgId=" + orgId + "&id=" + object.getId()
            );

        } catch (Exception e) {
            getServletContext().log("Ошибка создания объекта", e);
            throw new ServletException("Ошибка создания объекта", e);
        }
    }
}