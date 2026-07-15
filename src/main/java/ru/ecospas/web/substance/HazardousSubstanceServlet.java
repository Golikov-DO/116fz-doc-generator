package ru.ecospas.web.substance;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.ObjectHazardousParamValue;
import ru.ecospas.domain.model.ReferenceHazardousParam;
import ru.ecospas.domain.model.ReferenceHazardousSubstance;
import ru.ecospas.domain.repository.OrganizationRepository;
import ru.ecospas.domain.service.CurrentUserService;
import ru.ecospas.domain.service.HazardousSubstanceService;
import ru.ecospas.domain.service.SecurityService;
import ru.ecospas.web.BaseServlet;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.ecospas.web.util.RequestUtils.param;
import static ru.ecospas.web.util.RequestUtils.paramInt;

@Component
public class HazardousSubstanceServlet extends BaseServlet {

    private final HazardousSubstanceService hazardousSubstanceService;

    public HazardousSubstanceServlet(
            SecurityService securityService,
            OrganizationRepository organizationRepository,
            HazardousSubstanceService hazardousSubstanceService,
            CurrentUserService currentUserService
    ) {

        super(securityService, organizationRepository, currentUserService);
        this.hazardousSubstanceService = hazardousSubstanceService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {

        try {

            int id = paramInt(req, "id");
            String mode = param(req, "mode");

            ReferenceHazardousSubstance substance =
                    id > 0
                            ? hazardousSubstanceService.load(id)
                            : hazardousSubstanceService.create();

            List<ReferenceHazardousParam> params =
                    hazardousSubstanceService.loadParams();

            List<ObjectHazardousParamValue> values =
                    id > 0
                            ? hazardousSubstanceService.loadValues(id)
                            : List.of();

            Map<Integer, ObjectHazardousParamValue> valueMap =
                    values.stream()
                            .collect(Collectors.toMap(
                                    value -> value.getParam().getId(),
                                    value -> value
                            ));

            req.setAttribute("substance", substance);
            req.setAttribute("params", params);
            req.setAttribute("values", valueMap);
            req.setAttribute("mode", mode);
            req.setAttribute("backUrl", req.getParameter("backUrl"));

            req.setAttribute(
                    "contentPage",
                    "/WEB-INF/pages/hazardous-substance-page.jsp"
            );

            req.getRequestDispatcher("/WEB-INF/layout.jsp")
                    .forward(req, resp);

        } catch (Exception e) {
            getServletContext().log("Error loading hazardous substance", e);
            throw new ServletException("Error loading hazardous substance", e);
        }
    }
}