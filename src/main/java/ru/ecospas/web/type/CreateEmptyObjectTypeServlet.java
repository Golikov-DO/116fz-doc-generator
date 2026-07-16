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

import java.nio.charset.StandardCharsets;

@Component
public class CreateEmptyObjectTypeServlet extends BaseServlet {

    private final ReferenceTypeService referenceTypeService;

    public CreateEmptyObjectTypeServlet(
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

            ReferenceType referenceType = referenceTypeService.createEmpty();

            String backUrl = req.getParameter("backUrl");

            String redirect =
                    "/object-type?id=" + referenceType.getId() + "&mode=edit";

            if (backUrl != null && !backUrl.isEmpty()) {
                redirect += "&backUrl=" +
                        java.net.URLEncoder.encode(
                                backUrl,
                                StandardCharsets.UTF_8
                        );
            }

            resp.sendRedirect(redirect);

        } catch (Exception e) {
            getServletContext().log("Error creating Object Type", e);
            throw new ServletException("Error creating Object Type", e);
        }
    }
}