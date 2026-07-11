package ru.ecospas.web.region;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import ru.ecospas.app.InternalServices;
import ru.ecospas.domain.model.ObjectRegionalAuthorities;
import ru.ecospas.domain.model.ReferenceCity;
import ru.ecospas.domain.service.ChildService;
import ru.ecospas.domain.service.ParentService;
import ru.ecospas.domain.service.SecurityService;
import ru.ecospas.web.BaseServlet;

import java.util.List;

import static ru.ecospas.web.util.RequestUtils.paramInt;

@Component
public class RegionServlet extends BaseServlet {

    private final ParentService<ReferenceCity> cityService;
    private final ChildService<ObjectRegionalAuthorities> authoritiesService;

    public RegionServlet(InternalServices services, SecurityService securityService) {
        super(services, securityService);
        this.cityService = services.getParentService(ReferenceCity.class);
        this.authoritiesService = services.getChildService(ObjectRegionalAuthorities.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {

        try {
            int cityId = paramInt(req, "cityId");
            String backUrl = req.getParameter("backUrl");

            ReferenceCity city = cityService.getOneById(cityId);
            List<ObjectRegionalAuthorities> list = authoritiesService.getManyByParentId(cityId);

            req.setAttribute("city", city);
            req.setAttribute("authorities", list);
            req.setAttribute("backUrl", backUrl);

            req.setAttribute("contentPage", "/WEB-INF/pages/region-page.jsp");
            req.getRequestDispatcher("/WEB-INF/layout.jsp").forward(req, resp);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}