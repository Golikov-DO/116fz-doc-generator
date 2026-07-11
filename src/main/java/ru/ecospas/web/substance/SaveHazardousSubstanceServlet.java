package ru.ecospas.web.substance;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.ReferenceHazardousSubstance;
import ru.ecospas.domain.repository.OrganizationRepository;
import ru.ecospas.domain.service.HazardousSubstanceService;
import ru.ecospas.domain.service.SecurityService;
import ru.ecospas.web.BaseServlet;

import static ru.ecospas.web.util.RequestUtils.paramInt;

@Component
public class SaveHazardousSubstanceServlet extends BaseServlet {

    private final HazardousSubstanceService hazardousSubstanceService;

    public SaveHazardousSubstanceServlet(
            SecurityService securityService,
            OrganizationRepository organizationRepository,
            HazardousSubstanceService hazardousSubstanceService) {

        super(securityService, organizationRepository);
        this.hazardousSubstanceService = hazardousSubstanceService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {

        try {

            int id = paramInt(req, "substanceId");

            ReferenceHazardousSubstance substance =
                    id == 0
                            ? hazardousSubstanceService.create()
                            : hazardousSubstanceService.load(id);

            hazardousSubstanceService.save(req, substance);

            String backUrl = req.getParameter("backUrl");

            if (backUrl != null && !backUrl.isEmpty()) {
                resp.sendRedirect(backUrl);
                return;
            }

            resp.sendRedirect("/objects?mode=edit");

        } catch (Exception e) {
            getServletContext().log("Error saving Hazardous Substance", e);
            throw new ServletException("Error saving Hazardous Substance", e);
        }
    }
}