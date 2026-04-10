package ru.ecospas.web.organization;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ecospas.domain.model.ObjectModel;
import ru.ecospas.domain.model.Organization;
import ru.ecospas.domain.service.ChildService;
import ru.ecospas.domain.service.ObjectDeleteService;
import ru.ecospas.domain.service.OrganizationDeleteService;
import ru.ecospas.domain.service.ParentService;
import ru.ecospas.web.BaseServlet;

import java.util.List;

import static ru.ecospas.web.util.RequestUtils.paramInt;

@WebServlet("/delete-organization")
public class DeleteOrganizationServlet extends BaseServlet {

    private ParentService<Organization> organizationService;
    private OrganizationDeleteService orgDeleteService;
    private ChildService<ObjectModel> objectService;
    private ObjectDeleteService objectDeleteService;

    @Override
    public void init() {
        super.init();
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

            if (orgId == 0) throw new ServletException("orgId is required");

            orgDeleteService.delete(orgId);

            List<ObjectModel> objects = objectService.getManyByParentId(orgId);

            for (ObjectModel obj : objects) objectDeleteService.delete(obj.getId());

            organizationService.deleteById(orgId);

            resp.sendRedirect("/");
        } catch (Exception e) {
            getServletContext().log("Error deleting Organization", e);
            throw new ServletException("Error deleting Organization", e);
        }
    }
}