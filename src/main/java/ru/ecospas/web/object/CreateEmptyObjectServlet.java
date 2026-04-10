package ru.ecospas.web.object;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ecospas.domain.model.ObjectModel;
import ru.ecospas.domain.model.Organization;
import ru.ecospas.domain.service.ChildService;
import ru.ecospas.domain.service.ParentService;
import ru.ecospas.web.BaseServlet;

import static ru.ecospas.web.util.RequestUtils.paramInt;

@SuppressWarnings("unused") // Managed via dynamic registration in ServletAutoRegistration
public class CreateEmptyObjectServlet extends BaseServlet {

    private ChildService<ObjectModel> objectService;
    private ParentService<Organization> orgService;

    @Override
    public void init() {
        super.init();
        objectService = services.getChildService(ObjectModel.class);
        orgService = services.getParentService(Organization.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {
        try {
            int orgId = paramInt(req, "orgId");
            if (orgId == 0) throw new ServletException("orgId is required");

            Organization organization = orgService.getOneById(orgId);

            if (organization == null) throw new ServletException("Organization not found: " + orgId);

            ObjectModel object = new ObjectModel();
            object.setOrganization(organization);
            objectService.save(object);

            resp.sendRedirect("objects?mode=edit&orgId=" + orgId + "&id=" + object.getId());
        } catch (Exception e) {
            getServletContext().log("Error creating object", e);
            throw new ServletException("Error object create", e);
        }
    }
}