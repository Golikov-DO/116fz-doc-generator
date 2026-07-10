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
import ru.ecospas.web.BaseServlet;

import java.nio.charset.StandardCharsets;

@Component
public class CreateEmptyRegionServlet extends BaseServlet {

    private final ParentService<ReferenceCity> cityService;
    private final ChildService<ObjectRegionalAuthorities> service;

    public CreateEmptyRegionServlet(InternalServices services) {
        super(services);
        this.cityService = services.getParentService(ReferenceCity.class);
        this.service = services.getChildService(ObjectRegionalAuthorities.class);
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