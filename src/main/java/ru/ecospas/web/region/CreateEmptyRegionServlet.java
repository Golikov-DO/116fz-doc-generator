package ru.ecospas.web.region;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.ReferenceCity;
import ru.ecospas.domain.repository.OrganizationRepository;
import ru.ecospas.domain.service.RegionalAuthoritiesService;
import ru.ecospas.domain.service.SecurityService;
import ru.ecospas.web.BaseServlet;

import java.nio.charset.StandardCharsets;

@Component
public class CreateEmptyRegionServlet extends BaseServlet {

    private final RegionalAuthoritiesService regionalAuthoritiesService;

    public CreateEmptyRegionServlet(
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
    ) throws ServletException {

        try {

            ReferenceCity city =
                    regionalAuthoritiesService.createEmpty();

            String backUrl = req.getParameter("backUrl");

            String redirect =
                    "/region?cityId=" + city.getId() + "&mode=edit";

            if (backUrl != null) {
                redirect += "&backUrl="
                        + java.net.URLEncoder.encode(
                                backUrl,
                                StandardCharsets.UTF_8
                        );
            }

            resp.sendRedirect(redirect);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}