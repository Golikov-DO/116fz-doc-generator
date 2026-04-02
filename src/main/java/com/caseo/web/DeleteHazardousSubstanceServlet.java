package com.caseo.web;

import com.caseo.app.ApplicationContext;
import com.caseo.app.InternalServices;
import com.caseo.domain.model.ObjectHazardousParamValue;
import com.caseo.domain.model.ReferenceHazardousSubstance;
import com.caseo.domain.service.ChildService;
import com.caseo.domain.service.ParentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

import static com.caseo.web.util.RequestUtils.paramInt;

@WebServlet("/deleteHazardousSubstance")
public class DeleteHazardousSubstanceServlet extends HttpServlet {

    private ParentService<ReferenceHazardousSubstance> substanceService;
    private ChildService<ObjectHazardousParamValue> valueService;

    @Override
    public void init() {
        ApplicationContext context = (ApplicationContext) getServletContext()
                .getAttribute("appContext");

        InternalServices services = context.internalServices();

        substanceService = services.getParentService(ReferenceHazardousSubstance.class);
        valueService = services.getChildService(ObjectHazardousParamValue.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {

        try {
            int id = paramInt(req, "id");

            if (id == 0) {
                throw new ServletException("id is required");
            }

            List<ObjectHazardousParamValue> values =
                    valueService.getManyByParentId(id);

            for (ObjectHazardousParamValue v : values) {
                valueService.deleteById(v.getId());
            }

            substanceService.deleteById(id);

            resp.sendRedirect("/objects");

        } catch (Exception e) {
            getServletContext().log("Ошибка при удалении вещества", e);
            throw new ServletException("Ошибка при удалении вещества", e);
        }
    }
}