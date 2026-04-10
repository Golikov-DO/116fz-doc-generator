package ru.ecospas.web.asf;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ecospas.domain.model.Asf;
import ru.ecospas.domain.service.ParentService;
import ru.ecospas.web.BaseServlet;

import static ru.ecospas.web.util.RequestUtils.param;

@WebServlet("/create-empty-asf")
public class CreateEmptyAsfServlet extends BaseServlet {

    private ParentService<Asf> asfService;

    @Override
    public void init() {
        super.init();
        asfService = services.getParentService(Asf.class);
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