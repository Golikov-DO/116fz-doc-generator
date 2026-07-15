package ru.ecospas.web.region;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import ru.ecospas.domain.repository.OrganizationRepository;
import ru.ecospas.domain.service.CurrentUserService;
import ru.ecospas.domain.service.RegionalAuthoritiesService;
import ru.ecospas.domain.service.SecurityService;
import ru.ecospas.web.BaseServlet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static ru.ecospas.web.util.RequestUtils.paramInt;

@Component
public class DeleteRegionServlet extends BaseServlet {

    private final RegionalAuthoritiesService regionalAuthoritiesService;

    public DeleteRegionServlet(
            SecurityService securityService,
            OrganizationRepository organizationRepository,
            RegionalAuthoritiesService regionalAuthoritiesService,
            CurrentUserService currentUserService
    ) {
        super(securityService, organizationRepository, currentUserService);
        this.regionalAuthoritiesService = regionalAuthoritiesService;
    }

    @Override
    protected void doPost(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws ServletException, IOException {

        if (!currentUserService.isAdmin()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {

            int cityId = paramInt(req, "cityId");

            if (cityId == 0) {
                throw new ServletException("cityId is required");
            }

            regionalAuthoritiesService.delete(cityId);

            String backUrl = req.getParameter("backUrl");

            if (backUrl != null) {
                resp.sendRedirect(
                        java.net.URLDecoder.decode(backUrl, StandardCharsets.UTF_8)
                );
                return;
            }

            resp.sendRedirect("/objects?mode=edit");

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}