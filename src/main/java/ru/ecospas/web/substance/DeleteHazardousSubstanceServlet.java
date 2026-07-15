package ru.ecospas.web.substance;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import ru.ecospas.domain.repository.OrganizationRepository;
import ru.ecospas.domain.service.CurrentUserService;
import ru.ecospas.domain.service.HazardousSubstanceService;
import ru.ecospas.domain.service.SecurityService;
import ru.ecospas.web.BaseServlet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static ru.ecospas.web.util.RequestUtils.paramInt;

@Component
public class DeleteHazardousSubstanceServlet extends BaseServlet {

    private final HazardousSubstanceService hazardousSubstanceService;

    public DeleteHazardousSubstanceServlet(
            SecurityService securityService,
            OrganizationRepository organizationRepository,
            HazardousSubstanceService hazardousSubstanceService,
            CurrentUserService currentUserService
    ) {

        super(securityService, organizationRepository, currentUserService);
        this.hazardousSubstanceService = hazardousSubstanceService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (!currentUserService.isAdmin()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {

            int id = paramInt(req, "id");

            if (id == 0) {
                throw new ServletException("id is required");
            }

            hazardousSubstanceService.delete(id);

            String backUrl = req.getParameter("backUrl");

            if (backUrl != null && !backUrl.isEmpty()) {
                resp.sendRedirect(
                        java.net.URLDecoder.decode(backUrl, StandardCharsets.UTF_8)
                );
                return;
            }

            resp.sendRedirect("/objects?mode=edit");

        } catch (Exception e) {
            getServletContext().log("Error removing hazardous substance", e);
            throw new ServletException("Error removing hazardous substance", e);
        }
    }
}