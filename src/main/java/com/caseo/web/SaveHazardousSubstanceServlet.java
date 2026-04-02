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

import static com.caseo.web.util.RequestUtils.param;
import static com.caseo.web.util.RequestUtils.paramInt;

@WebServlet("/saveHazardousSubstance")
public class SaveHazardousSubstanceServlet extends HttpServlet {

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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {

        try {
            int id = paramInt(req, "substanceId");

            ReferenceHazardousSubstance substance;

            if (id == 0) {
                substance = new ReferenceHazardousSubstance();
            } else {
                substance = substanceService.getOneById(id);
            }

            substance.setName(param(req, "name"));
            substance.setNameGen(param(req, "nameGen"));
            substanceService.save(substance);

            List<ReferenceHazardousParam> params = paramService.getMany();

            List<ObjectHazardousParamValue> existing =
                    valueService.getManyByParentId(substance.getId());

            for (ReferenceHazardousParam param : params) {

                String value = req.getParameter("value_" + param.getId());
                String source = req.getParameter("source_" + param.getId());

                ObjectHazardousParamValue entity = existing.stream()
                        .filter(v -> v.getParam().getId().equals(param.getId()))
                        .findFirst()
                        .orElse(null);

                if (entity == null) {
                    entity = new ObjectHazardousParamValue();
                    entity.setParam(param);
                    entity.setSubstance(substance);
                }

                entity.setValueText(value);
                entity.setSourceInfo(source);

                valueService.save(entity);
            }

            resp.sendRedirect("hazardousSubstance?id=" + substance.getId());

        } catch (Exception e) {
            getServletContext().log("Ошибка при сохранении вещества", e);
            throw new ServletException("Ошибка при сохранении вещества", e);
        }
    }
}