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

import java.nio.charset.StandardCharsets;

@Component
public class CreateEmptyObjectTypeServlet extends BaseServlet {

    private final ObjectTypeService objectTypeService;

    public CreateEmptyObjectTypeServlet(
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

            ObjectType objectType = objectTypeService.createEmpty();

            String backUrl = req.getParameter("backUrl");

            String redirect =
                    "/object-type?id=" + objectType.getId() + "&mode=edit";

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