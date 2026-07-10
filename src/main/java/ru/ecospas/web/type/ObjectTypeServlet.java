package ru.ecospas.web.type;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import ru.ecospas.app.InternalServices;
import ru.ecospas.domain.model.ObjectType;
import ru.ecospas.domain.service.ParentService;
import ru.ecospas.web.BaseServlet;

import static ru.ecospas.web.util.RequestUtils.param;
import static ru.ecospas.web.util.RequestUtils.paramInt;

@Component
public class ObjectTypeServlet extends BaseServlet {

    private final ParentService<ObjectType> objectTypeService;

    public ObjectTypeServlet(InternalServices services) {
        super(services);
        this.objectTypeService = services.getParentService(ObjectType.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {

        try {
            int id = paramInt(req, "id");
            String mode = param(req, "mode");

            ObjectType objectType = null;

            if (id > 0) {
                objectType = objectTypeService.getOneById(id);
            }

            boolean isView = "view".equals(mode);
            
            req.setAttribute("objectType", objectType);
            req.setAttribute("mode", mode);
            req.setAttribute("isView", isView);
            String backUrl = req.getParameter("backUrl");

            req.setAttribute("backUrl", backUrl);
            req.setAttribute("contentPage", "/WEB-INF/pages/object-type-page.jsp");
            req.getRequestDispatcher("/WEB-INF/layout.jsp").forward(req, resp);

        } catch (Exception e) {
            getServletContext().log("Error loading object type", e);
            throw new ServletException("Error loading object type", e);
        }
    }
}