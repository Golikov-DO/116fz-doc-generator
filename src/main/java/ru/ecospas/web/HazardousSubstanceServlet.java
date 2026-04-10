package ru.ecospas.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ecospas.domain.model.ObjectHazardousParamValue;
import ru.ecospas.domain.model.ReferenceHazardousParam;
import ru.ecospas.domain.model.ReferenceHazardousSubstance;
import ru.ecospas.domain.service.ChildService;
import ru.ecospas.domain.service.ParentService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.ecospas.web.util.RequestUtils.paramInt;

@WebServlet("/hazardous-substance")
public class HazardousSubstanceServlet extends BaseServlet {

    private ParentService<ReferenceHazardousSubstance> substanceService;
    private ParentService<ReferenceHazardousParam> paramService;
    private ChildService<ObjectHazardousParamValue> valueService;

    @Override
    public void init() {
        super.init();
        substanceService = services.getParentService(ReferenceHazardousSubstance.class);
        paramService = services.getParentService(ReferenceHazardousParam.class);
        valueService = services.getChildService(ObjectHazardousParamValue.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {

        try {
            int id = paramInt(req, "id");

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

            req.setAttribute("contentPage", "/WEB-INF/pages/hazardous-substance-page.jsp");
            req.getRequestDispatcher("/WEB-INF/layout.jsp").forward(req, resp);

        } catch (Exception e) {
            getServletContext().log("Error loading hazardous substance", e);
            throw new ServletException("Error loading hazardous substance", e);
        }
    }
}