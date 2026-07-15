package ru.ecospas.web.asf;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.Asf;
import ru.ecospas.domain.repository.OrganizationRepository;
import ru.ecospas.domain.service.AsfService;
import ru.ecospas.domain.service.CurrentUserService;
import ru.ecospas.domain.service.SecurityService;
import ru.ecospas.web.BaseServlet;

import static ru.ecospas.web.util.RequestUtils.param;
import static ru.ecospas.web.util.RequestUtils.paramInt;

@Component
public class SaveAsfServlet extends BaseServlet {

    private final AsfService asfService;

    public SaveAsfServlet(
            SecurityService securityService,
            OrganizationRepository organizationRepository,
            AsfService asfService,
            CurrentUserService currentUserService
    ) {
        super(securityService, organizationRepository, currentUserService);
        this.asfService = asfService;
    }

    @Override
    protected void doPost(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws ServletException {

        try {

            int asfId = paramInt(req, "asfId");
            String returnOrgId = param(req, "returnOrgId");

            Asf asf;

            if (asfId == 0) {
                asf = asfService.create();
            } else {
                asf = asfService.load(asfId);

                if (asf == null) {
                    throw new ServletException(
                            "Asf not found with id: " + asfId
                    );
                }
            }

            asf = asfService.save(req, asf);

            if (returnOrgId != null && !returnOrgId.isEmpty()) {
                resp.sendRedirect(
                        "objects?mode=edit&orgId=" + returnOrgId
                );
            } else {
                resp.sendRedirect(
                        "asf?mode=view&asfId=" + asf.getId()
                );
            }

        } catch (Exception e) {
            getServletContext().log("Error saving ASF", e);
            throw new ServletException("Error saving ASF", e);
        }
    }
}