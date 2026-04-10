package ru.ecospas.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ecospas.domain.model.ObjectHazardousParamValue;
import ru.ecospas.domain.model.ReferenceHazardousSubstance;
import ru.ecospas.domain.service.ChildService;
import ru.ecospas.domain.service.ParentService;

import java.util.List;

import static ru.ecospas.web.util.RequestUtils.paramInt;

@WebServlet("/delete-hazardous-substance")
public class DeleteHazardousSubstanceServlet extends BaseServlet {

    private ParentService<ReferenceHazardousSubstance> substanceService;
    private ChildService<ObjectHazardousParamValue> valueService;

    @Override
    public void init() {
        super.init();
        substanceService = services.getParentService(ReferenceHazardousSubstance.class);
        valueService = services.getChildService(ObjectHazardousParamValue.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            int id = paramInt(req, "id");

            if (id == 0) throw new ServletException("id is required");

            List<ObjectHazardousParamValue> values = valueService.getManyByParentId(id);

            for (ObjectHazardousParamValue v : values) valueService.deleteById(v.getId());

            substanceService.deleteById(id);

            resp.sendRedirect("/objects");
        } catch (Exception e) {
            getServletContext().log("Error removing hazardous substance", e);
            throw new ServletException("Error removing hazardous substance", e);
        }
    }
}