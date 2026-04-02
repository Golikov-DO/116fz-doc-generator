package com.caseo.web;

import com.caseo.app.ApplicationContext;
import com.caseo.domain.model.ReferenceHazardousSubstance;
import com.caseo.domain.service.ParentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/createEmptyHazardousSubstance")
public class CreateEmptyHazardousSubstanceServlet extends HttpServlet {

    private ParentService<ReferenceHazardousSubstance> service;

    @Override
    public void init() {
        ApplicationContext context = (ApplicationContext) getServletContext()
                .getAttribute("appContext");

        service = context.internalServices()
                .getParentService(ReferenceHazardousSubstance.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {

        try {
            ReferenceHazardousSubstance s = new ReferenceHazardousSubstance();
            s.setName("");

            service.save(s);

            resp.sendRedirect("hazardousSubstance?id=" + s.getId());

        } catch (Exception e) {
            getServletContext().log("Ошибка при создании вещества", e);
            throw new ServletException("Ошибка при создании вещества", e);
        }
    }
}