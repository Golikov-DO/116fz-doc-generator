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
import ru.ecospas.web.BaseServlet;

import java.util.List;

import static ru.ecospas.web.util.RequestUtils.param;
import static ru.ecospas.web.util.RequestUtils.paramInt;

@Component
public class SaveHazardousSubstanceServlet extends BaseServlet {

    private final ParentService<ReferenceHazardousSubstance> substanceService;
    private final ParentService<ReferenceHazardousParam> paramService;
    private final ChildService<ObjectHazardousParamValue> valueService;

    public SaveHazardousSubstanceServlet(InternalServices services) {
        super(services);
        this.substanceService = services.getParentService(ReferenceHazardousSubstance.class);
        this.paramService = services.getParentService(ReferenceHazardousParam.class);
        this.valueService = services.getChildService(ObjectHazardousParamValue.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {

        try {
            int id = paramInt(req, "substanceId");

            ReferenceHazardousSubstance substance;

            if (id == 0) substance = new ReferenceHazardousSubstance();
            else substance = substanceService.getOneById(id);

            substance.setName(param(req, "name"));
            substance.setNameGen(param(req, "nameGen"));
            substanceService.save(substance);

            List<ReferenceHazardousParam> params = paramService.getMany();

            List<ObjectHazardousParamValue> existing = valueService.getManyByParentId(substance.getId());

            for (ReferenceHazardousParam param : params) {

                String value = req.getParameter("value_" + param.getId());
                String source = req.getParameter("source_" + param.getId());

                ObjectHazardousParamValue entity = existing.stream()
                        .filter(paramValue -> paramValue.getParam().getId().equals(param.getId()))
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

            // Build redirect URL
            String backUrl = req.getParameter("backUrl");

            if (backUrl != null && !backUrl.isEmpty()) {
                resp.sendRedirect(backUrl);
                return;
            }

            // fallback
            resp.sendRedirect("/objects?mode=edit");

        } catch (Exception e) {
            getServletContext().log("Error saving Hazardous Substance", e);
            throw new ServletException("Error saving Hazardous Substance", e);
        }
    }
}