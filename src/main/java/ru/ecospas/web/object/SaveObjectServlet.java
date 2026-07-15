package ru.ecospas.web.object;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.ObjectModel;
import ru.ecospas.domain.model.Organization;
import ru.ecospas.domain.repository.OrganizationRepository;
import ru.ecospas.domain.service.CurrentUserService;
import ru.ecospas.domain.service.ObjectService;
import ru.ecospas.domain.service.SecurityService;
import ru.ecospas.web.BaseServlet;
import ru.ecospas.web.helper.ObjectSaveHelper;

import static ru.ecospas.web.util.RequestUtils.paramInt;

@Component
public class SaveObjectServlet extends BaseServlet {

    private final ObjectService objectService;
    private final ObjectSaveHelper saveHelper;

    public SaveObjectServlet(
            SecurityService securityService,
            OrganizationRepository organizationRepository,
            ObjectService objectService,
            ObjectSaveHelper saveHelper,
            CurrentUserService currentUserService) {

        super(securityService, organizationRepository, currentUserService);

        this.objectService = objectService;
        this.saveHelper = saveHelper;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {

        try {

            int objectId = paramInt(req, "objectId");

            Organization organization;
            ObjectModel object;
            int orgId;

            if (objectId > 0) {

                object = objectService.load(objectId);

                if (object == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }

                organization = requireAccess(resp, object.getOrganization().getId()
                );

                if (organization == null) {
                    return;
                }

                orgId = organization.getId();

            } else {

                orgId = paramInt(req, "orgId");

                organization = requireAccess(resp, orgId);

                if (organization == null) {
                    return;
                }

                object = objectService.create(organization);
            }

            object = objectService.save(req, saveHelper, object);

            resp.sendRedirect(
                    "object?mode=view&orgId=" +
                            orgId +
                            "&id=" +
                            object.getId()
            );

        } catch (Exception e) {

            getServletContext().log("Error saving Object", e);

            throw new ServletException(e);
        }
    }
}