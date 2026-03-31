package com.caseo.web;

import com.caseo.app.ApplicationContext;
import com.caseo.app.InternalServices;
import com.caseo.domain.model.*;
import com.caseo.domain.service.ObjectDeleteService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.caseo.web.util.RequestUtils.paramInt;

@WebServlet("/deleteObject")
public class DeleteObjectServlet extends HttpServlet {

    private ObjectDeleteService deleteService;

    @Override
    public void init() {
        ApplicationContext context = (ApplicationContext) getServletContext()
                .getAttribute("appContext");

        InternalServices services = context.internalServices();

        deleteService = new ObjectDeleteService(services);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {

        try {
            int objectId = paramInt(req, "objectId");

            if (objectId == 0) {
                throw new ServletException("objectId is required");
            }

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