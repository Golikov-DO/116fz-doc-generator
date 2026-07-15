package ru.ecospas.web.region;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.ReferenceCity;
import ru.ecospas.domain.service.CurrentUserService;
import ru.ecospas.domain.service.RegionalAuthoritiesService;
import ru.ecospas.domain.service.SecurityService;
import ru.ecospas.domain.repository.OrganizationRepository;
import ru.ecospas.web.BaseServlet;

import static ru.ecospas.web.util.RequestUtils.paramInt;

@Component
public class SaveRegionServlet extends BaseServlet {

    private final RegionalAuthoritiesService regionalAuthoritiesService;

    public SaveRegionServlet(
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
    ) throws ServletException {

        try {

            int cityId = paramInt(req, "cityId");

            ReferenceCity city;

            if (cityId > 0) {
                city = regionalAuthoritiesService.load(cityId);
            } else {
                city = regionalAuthoritiesService.create();
            }

            city = regionalAuthoritiesService.save(req, city);

            String backUrl = req.getParameter("backUrl");

            if (backUrl != null && !backUrl.isEmpty()) {
                resp.sendRedirect(backUrl);
                return;
            }

            resp.sendRedirect("/objects?mode=edit");

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}