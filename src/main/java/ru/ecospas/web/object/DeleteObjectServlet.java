package ru.ecospas.web.object;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import ru.ecospas.domain.repository.ObjectModelRepository;
import ru.ecospas.domain.repository.OrganizationRepository;
import ru.ecospas.domain.service.CurrentUserService;
import ru.ecospas.domain.service.ObjectDeleteService;
import ru.ecospas.domain.service.SecurityService;
import ru.ecospas.web.BaseServlet;

import static ru.ecospas.web.util.RequestUtils.paramInt;

@Component
public class DeleteObjectServlet extends BaseServlet {

    private final ObjectDeleteService deleteService;
    private final ObjectModelRepository objectRepository;

    public DeleteObjectServlet(
            SecurityService securityService,
            OrganizationRepository organizationRepository,
            ObjectModelRepository objectRepository,
            ObjectDeleteService deleteService,
            CurrentUserService currentUserService) {

        super(securityService, organizationRepository, currentUserService);

        this.objectRepository = objectRepository;
        this.deleteService = deleteService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            int objectId = paramInt(req, "objectId");

            if (objectId == 0) throw new ServletException("objectId is required");

            var object = objectRepository.findById(objectId).orElse(null);

            if (object == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            if (requireAccess(resp, object.getOrganization().getId()) == null) return;

            String returnUrl = req.getParameter("returnUrl");

            deleteService.delete(objectId);

            if (returnUrl != null && !returnUrl.isEmpty()) {
                resp.sendRedirect(returnUrl);
            } else {
                resp.sendRedirect("objects"); // fallback
            }
        } catch (Exception e) {
            getServletContext().log("Ошибка удаления объекта", e);
            throw new ServletException("Ошибка удаления объекта", e);
        }
    }
}