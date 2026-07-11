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

import static ru.ecospas.web.util.RequestUtils.param;
import static ru.ecospas.web.util.RequestUtils.paramInt;

@Component
public class SaveObjectTypeServlet extends BaseServlet {

    private final ParentService<ObjectType> objectTypeService;

    public SaveObjectTypeServlet(InternalServices services, SecurityService securityService) {
        super(services, securityService);
        this.objectTypeService = services.getParentService(ObjectType.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {

        try {
            int id = paramInt(req, "id");

            ObjectType objectType;

            if (id == 0) {
                objectType = new ObjectType();
            } else {
                objectType = objectTypeService.getOneById(id);
            }

            objectType.setType(param(req, "type"));
            objectType.setTypeDefinition(param(req, "object_type_definitions"));
            
            objectTypeService.save(objectType);

            // Build redirect URL
            String backUrl = req.getParameter("backUrl");

            if (backUrl != null && !backUrl.isEmpty()) {
                resp.sendRedirect(backUrl);
                return;
            }

            // fallback
            resp.sendRedirect("/objects?mode=edit");

        } catch (Exception e) {
            getServletContext().log("Error saving Object Type", e);
            throw new ServletException("Error saving Object Type", e);
        }
    }
}