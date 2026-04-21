package ru.ecospas.web.region;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ecospas.domain.model.ObjectRegionalAuthorities;
import ru.ecospas.domain.model.ReferenceCity;
import ru.ecospas.domain.service.ChildService;
import ru.ecospas.domain.service.ParentService;
import ru.ecospas.web.BaseServlet;

import java.nio.charset.StandardCharsets;

@SuppressWarnings("unused") // Managed via dynamic registration in ServletAutoRegistration
public class CreateEmptyRegionServlet extends BaseServlet {

    private ParentService<ReferenceCity> cityService;
    private ChildService<ObjectRegionalAuthorities> service;

    @Override
    public void init() {
        super.init();
        cityService = services.getParentService(ReferenceCity.class);
        service = services.getChildService(ObjectRegionalAuthorities.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {

        try {
            ReferenceCity referenceCity = new ReferenceCity();
            referenceCity.setCityName("");
            cityService.save(referenceCity);

            int cityId = referenceCity.getId();
            ReferenceCity city = cityService.getOneById(cityId);

            // создаём 4 записи сразу
            for (int i = 0; i < 4; i++) {
                ObjectRegionalAuthorities authorities = new ObjectRegionalAuthorities();
                authorities.setObjectCity(city);
                authorities.setName("");
                service.save(authorities);
            }

            String backUrl = req.getParameter("backUrl");

            String redirect = "/region?cityId=" + cityId + "&mode=edit";

            if (backUrl != null) {
                redirect += "&backUrl=" + java.net.URLEncoder.encode(backUrl, StandardCharsets.UTF_8);
            }

            resp.sendRedirect(redirect);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}