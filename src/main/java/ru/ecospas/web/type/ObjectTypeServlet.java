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

import static ru.ecospas.web.util.RequestUtils.param;
import static ru.ecospas.web.util.RequestUtils.paramInt;

@Component
public class ObjectTypeServlet extends BaseServlet {

    private final ObjectTypeService objectTypeService;

    public ObjectTypeServlet(
            SecurityService securityService,
            OrganizationRepository organizationRepository,
            ObjectTypeService objectTypeService,
            CurrentUserService currentUserService
    ) {

        super(securityService, organizationRepository, currentUserService);
        this.objectTypeService = objectTypeService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {

        try {

            int id = paramInt(req, "id");
            String mode = param(req, "mode");

            ObjectType objectType = null;

            if (id > 0) {
                objectType = objectTypeService.load(id);
            }

            req.setAttribute("objectType", objectType);
            req.setAttribute("mode", mode);
            req.setAttribute("isView", "view".equals(mode));
            req.setAttribute("backUrl", req.getParameter("backUrl"));

            req.setAttribute(
                    "contentPage",
                    "/WEB-INF/pages/object-type-page.jsp"
            );

            req.getRequestDispatcher("/WEB-INF/layout.jsp")
                    .forward(req, resp);

        } catch (Exception e) {
            getServletContext().log("Error loading object type", e);
            throw new ServletException("Error loading object type", e);
        }
    }
}