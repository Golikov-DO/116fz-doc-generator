package ru.ecospas.web.type;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.ObjectType;
import ru.ecospas.domain.repository.OrganizationRepository;
import ru.ecospas.domain.service.CurrentUserService;
import ru.ecospas.domain.service.ObjectTypeService;
import ru.ecospas.domain.service.SecurityService;
import ru.ecospas.web.BaseServlet;

import static ru.ecospas.web.util.RequestUtils.paramInt;

@Component
public class SaveObjectTypeServlet extends BaseServlet {

    private final ObjectTypeService objectTypeService;

    public SaveObjectTypeServlet(
            SecurityService securityService,
            OrganizationRepository organizationRepository,
            ObjectTypeService objectTypeService,
            CurrentUserService currentUserService
    ) {

        super(securityService, organizationRepository, currentUserService);
        this.objectTypeService = objectTypeService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {

        try {

            int id = paramInt(req, "id");

            ObjectType objectType;

            if (id == 0) {
                objectType = objectTypeService.create();
            } else {
                objectType = objectTypeService.load(id);
            }

            objectTypeService.save(req, objectType);

            String backUrl = req.getParameter("backUrl");

            if (backUrl != null && !backUrl.isEmpty()) {
                resp.sendRedirect(backUrl);
                return;
            }

            resp.sendRedirect("/objects?mode=edit");

        } catch (Exception e) {
            getServletContext().log("Error saving Object Type", e);
            throw new ServletException("Error saving Object Type", e);
        }
    }
}