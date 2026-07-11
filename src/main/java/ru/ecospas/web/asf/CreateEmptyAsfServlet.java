package ru.ecospas.web.asf;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import ru.ecospas.app.InternalServices;
import ru.ecospas.domain.model.Asf;
import ru.ecospas.domain.service.ParentService;
import ru.ecospas.domain.service.SecurityService;
import ru.ecospas.web.BaseServlet;

import static ru.ecospas.web.util.RequestUtils.param;

@Component
public class CreateEmptyAsfServlet extends BaseServlet {

    private final ParentService<Asf> asfService;

    public CreateEmptyAsfServlet(InternalServices services, SecurityService securityService) {
        super(services, securityService);
        this.asfService = services.getParentService(Asf.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            String returnOrgId = param(req, "returnOrgId");
            Asf asf = new Asf();
            asfService.save(asf);

            resp.sendRedirect("asf?mode=edit&asfId=" + asf.getId() + "&returnOrgId=" + returnOrgId);
        } catch (Exception e) {
            getServletContext().log("Error creating empty ASF record", e);
            throw new ServletException("Error creating empty ASF record", e);
        }
    }
}