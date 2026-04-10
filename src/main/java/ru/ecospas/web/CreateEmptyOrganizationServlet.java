package ru.ecospas.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ecospas.domain.model.Organization;
import ru.ecospas.domain.model.User;
import ru.ecospas.domain.service.ParentService;

@WebServlet("/create-empty-organization")
public class CreateEmptyOrganizationServlet extends BaseServlet {

    private ParentService<Organization> orgService;

    @Override
    public void init() {
        super.init();
        orgService = services.getParentService(Organization.class);
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
            getServletContext().log("Error creating Organization", e);
            throw new ServletException(e);
        }
    }
}