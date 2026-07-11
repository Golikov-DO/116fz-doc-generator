package ru.ecospas.web.plan;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import ru.ecospas.domain.repository.OrganizationRepository;
import ru.ecospas.domain.service.PlanService;
import ru.ecospas.domain.service.SecurityService;
import ru.ecospas.web.BaseServlet;

import java.io.IOException;

import static ru.ecospas.web.util.RequestUtils.paramInt;

@Component
public class DownloadPlanServlet extends BaseServlet {

    private final PlanService planService;

    public DownloadPlanServlet(
            SecurityService securityService,
            OrganizationRepository organizationRepository,
            PlanService planService
    ) {
        super(securityService, organizationRepository);
        this.planService = planService;
    }

    @Override
    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws IOException, ServletException {

        try {

            int objectId = paramInt(req, "objectId");

            planService.download(
                    objectId,
                    resp
            );

        } catch (Exception e) {
            getServletContext().log("Download Word document error", e);
            throw new ServletException("Download Word document error", e);
        }
    }
}