package ru.ecospas.web.region;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.Role;
import ru.ecospas.domain.model.User;
import ru.ecospas.domain.repository.OrganizationRepository;
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
            RegionalAuthoritiesService regionalAuthoritiesService
    ) {
        super(securityService, organizationRepository);
        this.regionalAuthoritiesService = regionalAuthoritiesService;
    }

    @Override
    protected void doPost(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("user");

        if (user == null || user.getRole() != Role.ADMIN) {
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
                        java.net.URLDecoder.decode(
                                backUrl,
                                StandardCharsets.UTF_8
                        )
                );
                return;
            }

            resp.sendRedirect("/objects?mode=edit");

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}