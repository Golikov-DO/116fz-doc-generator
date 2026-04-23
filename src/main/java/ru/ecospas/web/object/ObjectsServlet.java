package ru.ecospas.web.object;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ecospas.domain.model.ObjectModel;
import ru.ecospas.web.BaseServlet;
import ru.ecospas.web.helper.DataLoader;

import java.util.List;

@SuppressWarnings("unused")
public class ObjectsServlet extends BaseServlet {

    private DataLoader dataLoader;

    @Override
    public void init() {
        super.init();
        dataLoader = new DataLoader(services);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

        String orgId = req.getParameter("orgId");

        try {
            if (orgId != null && !orgId.isEmpty()) {

                int id = Integer.parseInt(orgId);

                if (requireAccess(req, resp, id) == null) return;

                List<ObjectModel> objects = dataLoader.loadObjects(id);

                req.setAttribute("objects", objects);
                req.setAttribute("orgId", orgId);
            }

            req.setAttribute("mode", null);

            req.setAttribute("contentPage", "/WEB-INF/pages/objects-page.jsp");
            req.getRequestDispatcher("/WEB-INF/layout.jsp").forward(req, resp);

        } catch (Exception e) {
            getServletContext().log("Ошибка в ObjectsServlet", e);
        }
    }
}