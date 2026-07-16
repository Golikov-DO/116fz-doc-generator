package ru.ecospas.web.type;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.ReferenceType;
import ru.ecospas.domain.repository.OrganizationRepository;
import ru.ecospas.domain.service.CurrentUserService;
import ru.ecospas.domain.service.ReferenceTypeService;
import ru.ecospas.domain.service.SecurityService;
import ru.ecospas.web.BaseServlet;

import static ru.ecospas.web.util.RequestUtils.param;
import static ru.ecospas.web.util.RequestUtils.paramInt;

@Component
public class ObjectTypeServlet extends BaseServlet {

    private final ReferenceTypeService referenceTypeService;

    public ObjectTypeServlet(
            SecurityService securityService,
            OrganizationRepository organizationRepository,
            ReferenceTypeService referenceTypeService,
            CurrentUserService currentUserService
    ) {

        super(securityService, organizationRepository, currentUserService);
        this.referenceTypeService = referenceTypeService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {

        try {

            int id = paramInt(req, "id");
            String mode = param(req, "mode");

            ReferenceType referenceType = null;

            if (id > 0) {
                referenceType = referenceTypeService.load(id);
            }

            req.setAttribute("referenceType", referenceType);
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