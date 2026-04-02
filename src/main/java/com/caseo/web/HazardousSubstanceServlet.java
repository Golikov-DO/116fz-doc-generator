package com.caseo.web;

import com.caseo.app.ApplicationContext;
import com.caseo.app.InternalServices;
import com.caseo.domain.model.ObjectHazardousParamValue;
import com.caseo.domain.model.ReferenceHazardousParam;
import com.caseo.domain.model.ReferenceHazardousSubstance;
import com.caseo.domain.service.ChildService;
import com.caseo.domain.service.ParentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.caseo.web.util.RequestUtils.paramInt;

@WebServlet("/hazardousSubstance")
public class HazardousSubstanceServlet extends HttpServlet {

    private ParentService<ReferenceHazardousSubstance> substanceService;
    private ParentService<ReferenceHazardousParam> paramService;
    private ChildService<ObjectHazardousParamValue> valueService;

    @Override
    public void init() {
        ApplicationContext context = (ApplicationContext) getServletContext()
                .getAttribute("appContext");

        InternalServices services = context.internalServices();

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
            getServletContext().log("Ошибка при загрузке вещества", e);
            throw new ServletException("Ошибка при загрузке вещества", e);
        }
    }
}