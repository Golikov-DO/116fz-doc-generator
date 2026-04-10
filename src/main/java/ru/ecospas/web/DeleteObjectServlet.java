package ru.ecospas.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ecospas.domain.service.ObjectDeleteService;

import static ru.ecospas.web.util.RequestUtils.paramInt;

@WebServlet("/delete-object")
public class DeleteObjectServlet extends BaseServlet {

    private ObjectDeleteService deleteService;

    @Override
    public void init() {
        super.init();
        deleteService = new ObjectDeleteService(services);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            int objectId = paramInt(req, "objectId");

            if (objectId == 0) throw new ServletException("objectId is required");

            String returnUrl = req.getParameter("returnUrl");

            deleteService.delete(objectId);

            if (returnUrl != null && !returnUrl.isEmpty()) {
                resp.sendRedirect(returnUrl);
            } else {
                resp.sendRedirect("objects"); // fallback
            }
        } catch (Exception e) {
            getServletContext().log("Ошибка удаления объекта", e);
            throw new ServletException("Ошибка удаления объекта", e);
        }
    }
}