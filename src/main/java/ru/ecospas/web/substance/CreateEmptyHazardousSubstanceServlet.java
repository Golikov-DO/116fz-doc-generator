package ru.ecospas.web.substance;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import ru.ecospas.app.InternalServices;
import ru.ecospas.domain.model.ReferenceHazardousSubstance;
import ru.ecospas.domain.service.ParentService;
import ru.ecospas.web.BaseServlet;

import java.nio.charset.StandardCharsets;

@Component
public class CreateEmptyHazardousSubstanceServlet extends BaseServlet {

    private final ParentService<ReferenceHazardousSubstance> hazardousSubstanceService;

    public CreateEmptyHazardousSubstanceServlet(InternalServices services) {
        super(services);
        this.hazardousSubstanceService = services.getParentService(ReferenceHazardousSubstance.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            ReferenceHazardousSubstance substance = new ReferenceHazardousSubstance();
            substance.setName("");
            hazardousSubstanceService.save(substance);

            // Get return
            String backUrl = req.getParameter("backUrl");

            String redirectUrl = "/hazardous-substance?id=" + substance.getId() + "&mode=edit";

            if (backUrl != null && !backUrl.isEmpty()) {
                redirectUrl += "&backUrl=" + java.net.URLEncoder.encode(backUrl, StandardCharsets.UTF_8);
            }

            resp.sendRedirect(redirectUrl);

        } catch (Exception e) {
            getServletContext().log("Error creating Hazardous Substance", e);
            throw new ServletException("Error creating Hazardous Substance", e);
        }
    }
}