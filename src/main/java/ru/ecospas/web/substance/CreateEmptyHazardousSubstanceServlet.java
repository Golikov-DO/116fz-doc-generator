package ru.ecospas.web.substance;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.ReferenceHazardousSubstance;
import ru.ecospas.domain.repository.OrganizationRepository;
import ru.ecospas.domain.service.CurrentUserService;
import ru.ecospas.domain.service.HazardousSubstanceService;
import ru.ecospas.domain.service.SecurityService;
import ru.ecospas.web.BaseServlet;

import java.nio.charset.StandardCharsets;

@Component
public class CreateEmptyHazardousSubstanceServlet extends BaseServlet {

    private final HazardousSubstanceService hazardousSubstanceService;

    public CreateEmptyHazardousSubstanceServlet(
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
            throws ServletException {

        try {

            ReferenceHazardousSubstance substance =
                    hazardousSubstanceService.createEmpty();

            String backUrl = req.getParameter("backUrl");

            String redirect =
                    "/hazardous-substance?id=" + substance.getId() + "&mode=edit";

            if (backUrl != null && !backUrl.isEmpty()) {
                redirect += "&backUrl=" +
                        java.net.URLEncoder.encode(
                                backUrl,
                                StandardCharsets.UTF_8
                        );
            }

            resp.sendRedirect(redirect);

        } catch (Exception e) {
            getServletContext().log("Error creating Hazardous Substance", e);
            throw new ServletException("Error creating Hazardous Substance", e);
        }
    }
}