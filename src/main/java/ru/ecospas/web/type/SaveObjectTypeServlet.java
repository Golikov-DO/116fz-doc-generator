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

import static ru.ecospas.web.util.RequestUtils.paramInt;

@Component
public class SaveObjectTypeServlet extends BaseServlet {

    private final ReferenceTypeService referenceTypeService;

    public SaveObjectTypeServlet(
            SecurityService securityService,
            OrganizationRepository organizationRepository,
            ReferenceTypeService referenceTypeService,
            CurrentUserService currentUserService
    ) {

        super(securityService, organizationRepository, currentUserService);
        this.referenceTypeService = referenceTypeService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {

        try {

            int id = paramInt(req, "id");

            ReferenceType referenceType;

            if (id == 0) {
                referenceType = referenceTypeService.create();
            } else {
                referenceType = referenceTypeService.load(id);
            }

            referenceTypeService.save(req, referenceType);

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