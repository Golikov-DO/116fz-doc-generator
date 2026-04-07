package com.caseo.web;

import com.caseo.app.ApplicationContext;
import com.caseo.domain.model.Organization;
import com.caseo.domain.model.User;
import com.caseo.domain.service.ParentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/createEmptyOrganization")
public class CreateEmptyOrganizationServlet extends HttpServlet {

    private ParentService<Organization> orgService;

    @Override
    public void init() {
        ApplicationContext context =
                (ApplicationContext) getServletContext().getAttribute("appContext");

        orgService = context.internalServices().getParentService(Organization.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {

        try {
            Organization org = new Organization();
            orgService.save(org);
            User currentUser = (User) req.getSession().getAttribute("user");
            org.setUser(currentUser);
            resp.sendRedirect("organization?mode=edit&orgId=" + org.getId());

        } catch (Exception e) {
            getServletContext().log("Ошибка создания организации", e);
            throw new ServletException(e);
        }
    }
}