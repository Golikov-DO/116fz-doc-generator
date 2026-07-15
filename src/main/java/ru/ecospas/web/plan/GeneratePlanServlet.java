package ru.ecospas.web.plan;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import ru.ecospas.domain.repository.OrganizationRepository;
import ru.ecospas.domain.service.CurrentUserService;
import ru.ecospas.domain.service.PlanService;
import ru.ecospas.domain.service.SecurityService;
import ru.ecospas.web.BaseServlet;

import java.io.IOException;

import static ru.ecospas.web.util.RequestUtils.paramInt;

@Component
public class GeneratePlanServlet extends BaseServlet {

    private final PlanService planService;

    public GeneratePlanServlet(
            SecurityService securityService,
            OrganizationRepository organizationRepository,
            PlanService planService,
            CurrentUserService currentUserService
    ) {
        super(securityService, organizationRepository, currentUserService);
        this.planService = planService;
    }

    @Override
    protected void doPost(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws IOException, ServletException {

        try {

            int objectId = paramInt(req, "objectId");

            planService.generate(
                    getServletContext(),
                    req,
                    objectId
            );

            resp.setContentType("text/plain;charset=UTF-8");
            resp.getWriter().println("План успешно разработан");

        } catch (Exception e) {
            getServletContext().log("Generation Word document error", e);
            throw new ServletException("Generation Word document error", e);
        }
    }
}