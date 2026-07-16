package ru.ecospas.web.region;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.CityRegionalAuthorities;
import ru.ecospas.domain.model.ReferenceCity;
import ru.ecospas.domain.repository.OrganizationRepository;
import ru.ecospas.domain.service.CurrentUserService;
import ru.ecospas.domain.service.RegionalAuthoritiesService;
import ru.ecospas.domain.service.SecurityService;
import ru.ecospas.web.BaseServlet;

import java.util.List;

import static ru.ecospas.web.util.RequestUtils.paramInt;

@Component
public class RegionServlet extends BaseServlet {

    private final RegionalAuthoritiesService regionalAuthoritiesService;

    public RegionServlet(
            SecurityService securityService,
            OrganizationRepository organizationRepository,
            RegionalAuthoritiesService regionalAuthoritiesService,
            CurrentUserService currentUserService
    ) {
        super(securityService, organizationRepository, currentUserService);
        this.regionalAuthoritiesService = regionalAuthoritiesService;
    }

    @Override
    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws ServletException {

        try {

            int cityId = paramInt(req, "cityId");
            String backUrl = req.getParameter("backUrl");

            ReferenceCity city =
                    regionalAuthoritiesService.load(cityId);

            List<CityRegionalAuthorities> authorities =
                    regionalAuthoritiesService.findAuthorities(cityId);

            req.setAttribute("city", city);
            req.setAttribute("authorities", authorities);
            req.setAttribute("backUrl", backUrl);

            req.setAttribute(
                    "contentPage",
                    "/WEB-INF/pages/region-page.jsp"
            );

            req.getRequestDispatcher("/WEB-INF/layout.jsp")
                    .forward(req, resp);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}