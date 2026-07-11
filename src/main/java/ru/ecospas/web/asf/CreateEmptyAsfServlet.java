package ru.ecospas.web.asf;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.Asf;
import ru.ecospas.domain.repository.AsfRepository;
import ru.ecospas.domain.repository.OrganizationRepository;
import ru.ecospas.domain.service.AsfService;
import ru.ecospas.domain.service.SecurityService;
import ru.ecospas.web.BaseServlet;

import static ru.ecospas.web.util.RequestUtils.param;

@Component
public class CreateEmptyAsfServlet extends BaseServlet {

    private final AsfRepository asfRepository;
    private final AsfService asfService;

    public CreateEmptyAsfServlet(
            SecurityService securityService,
            OrganizationRepository organizationRepository,
            AsfRepository asfRepository,
            AsfService asfService) {
        super(securityService, organizationRepository);
        this.asfRepository = asfRepository;
        this.asfService = asfService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            String returnOrgId = param(req, "returnOrgId");
            Asf asf = asfRepository.save(asfService.create());

            resp.sendRedirect("asf?mode=edit&asfId=" + asf.getId() + "&returnOrgId=" + returnOrgId);
        } catch (Exception e) {
            getServletContext().log("Error creating empty ASF record", e);
            throw new ServletException("Error creating empty ASF record", e);
        }
    }
}