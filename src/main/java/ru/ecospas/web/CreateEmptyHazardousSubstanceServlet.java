package ru.ecospas.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ecospas.domain.model.ReferenceHazardousSubstance;
import ru.ecospas.domain.service.ParentService;

@WebServlet("/createEmptyHazardousSubstance")
public class CreateEmptyHazardousSubstanceServlet extends BaseServlet {

    private ParentService<ReferenceHazardousSubstance> hazardousSubstanceService;

    @Override
    public void init() {
        super.init();
        hazardousSubstanceService = services.getParentService(ReferenceHazardousSubstance.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            ReferenceHazardousSubstance substance = new ReferenceHazardousSubstance();
            substance.setName("");
            hazardousSubstanceService.save(substance);

            resp.sendRedirect("/hazardous-substance?id=" + substance.getId());
        } catch (Exception e) {
            getServletContext().log("Error creating Hazardous Substance", e);
            throw new ServletException("Error creating Hazardous Substance", e);
        }
    }
}