package ru.ecospas.web.substance;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import ru.ecospas.app.InternalServices;
import ru.ecospas.domain.model.ObjectHazardousParamValue;
import ru.ecospas.domain.model.ReferenceHazardousParam;
import ru.ecospas.domain.model.ReferenceHazardousSubstance;
import ru.ecospas.domain.service.ChildService;
import ru.ecospas.domain.service.ParentService;
import ru.ecospas.domain.service.SecurityService;
import ru.ecospas.web.BaseServlet;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.ecospas.web.util.RequestUtils.param;
import static ru.ecospas.web.util.RequestUtils.paramInt;

@Component
public class HazardousSubstanceServlet extends BaseServlet {

    private final ParentService<ReferenceHazardousSubstance> substanceService;
    private final ParentService<ReferenceHazardousParam> paramService;
    private final ChildService<ObjectHazardousParamValue> valueService;

    public HazardousSubstanceServlet(InternalServices services,  SecurityService securityService) {
        super(services, securityService);
        this.substanceService = services.getParentService(ReferenceHazardousSubstance.class);
        this.paramService = services.getParentService(ReferenceHazardousParam.class);
        this.valueService = services.getChildService(ObjectHazardousParamValue.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {

        try {
            int id = paramInt(req, "id");
            String mode = param(req, "mode");

            ReferenceHazardousSubstance substance;
            List<ObjectHazardousParamValue> values = List.of();

            if (id > 0) {
                substance = substanceService.getOneById(id);
                values = valueService.getManyByParentId(id);
            } else {
                substance = new ReferenceHazardousSubstance();
            }

            List<ReferenceHazardousParam> params = paramService.getMany();

            Map<Integer, ObjectHazardousParamValue> valueMap =
                    values.stream().collect(Collectors.toMap(
                            paramValue -> paramValue.getParam().getId(),
                            paramValue -> paramValue
                    ));

            req.setAttribute("substance", substance);
            req.setAttribute("params", params);
            req.setAttribute("values", valueMap);
            req.setAttribute("mode", mode);
            String backUrl = req.getParameter("backUrl");

            req.setAttribute("backUrl", backUrl);
            req.setAttribute("contentPage", "/WEB-INF/pages/hazardous-substance-page.jsp");
            req.getRequestDispatcher("/WEB-INF/layout.jsp").forward(req, resp);

        } catch (Exception e) {
            getServletContext().log("Error loading hazardous substance", e);
            throw new ServletException("Error loading hazardous substance", e);
        }
    }
}