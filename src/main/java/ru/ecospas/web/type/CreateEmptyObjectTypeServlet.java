package ru.ecospas.web.type;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import ru.ecospas.app.InternalServices;
import ru.ecospas.domain.model.ObjectType;
import ru.ecospas.domain.service.ParentService;
import ru.ecospas.domain.service.SecurityService;
import ru.ecospas.web.BaseServlet;

import java.nio.charset.StandardCharsets;

@Component
public class CreateEmptyObjectTypeServlet extends BaseServlet {

    private final ParentService<ObjectType> objectTypeService;

    public CreateEmptyObjectTypeServlet(InternalServices services, SecurityService securityService) {
        super(services, securityService);
        this.objectTypeService = services.getParentService(ObjectType.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            ObjectType objectType = new ObjectType();
            objectType.setType("");
            objectType.setTypeDefinition("");
            objectTypeService.save(objectType);

            // Get return
            String backUrl = req.getParameter("backUrl");

            String redirectUrl = "/object-type?id=" + objectType.getId() + "&mode=edit";

            if (backUrl != null && !backUrl.isEmpty()) {
                redirectUrl += "&backUrl=" + java.net.URLEncoder.encode(backUrl, StandardCharsets.UTF_8);
            }

            resp.sendRedirect(redirectUrl);
        } catch (Exception e) {
            getServletContext().log("Error creating Object Type", e);
            throw new ServletException("Error creating Object Type", e);
        }
    }
}