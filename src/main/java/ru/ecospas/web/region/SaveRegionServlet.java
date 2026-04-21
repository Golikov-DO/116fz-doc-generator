package ru.ecospas.web.region;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ecospas.domain.model.ObjectRegionalAuthorities;
import ru.ecospas.domain.model.ReferenceCity;
import ru.ecospas.domain.service.ChildService;
import ru.ecospas.domain.service.ParentService;
import ru.ecospas.web.BaseServlet;
import ru.ecospas.web.helper.RegionalAuthoritiesSaveHelper;
import ru.ecospas.web.util.SyncListUtils;

import java.util.List;

import static ru.ecospas.web.util.RequestUtils.paramInt;

@SuppressWarnings("unused") // Managed via dynamic registration in ServletAutoRegistration
public class SaveRegionServlet extends BaseServlet {

    private ParentService<ReferenceCity> cityService;
    private ChildService<ObjectRegionalAuthorities> authoritiesService;
    private RegionalAuthoritiesSaveHelper saveHelper;

    @Override
    public void init() {
        super.init();

        cityService = services.getParentService(ReferenceCity.class);
        authoritiesService = services.getChildService(ObjectRegionalAuthorities.class);

        saveHelper = new RegionalAuthoritiesSaveHelper();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {

        try {
            int cityId = paramInt(req, "cityId");

            ReferenceCity city;
            if (cityId > 0) city = cityService.getOneById(cityId);
            else city = new ReferenceCity();

            saveHelper.mapCity(req, city);
            cityService.save(city);

            List<ObjectRegionalAuthorities> list = saveHelper.mapAuthorities(req);

            List<ObjectRegionalAuthorities> oldDbList = authoritiesService.getManyByParentId(city.getId());

            SyncListUtils.syncList(
                    list,
                    oldDbList,
                    ObjectRegionalAuthorities::getId,
                    authoritiesService::deleteById
            );

            for (ObjectRegionalAuthorities e : list) {
                e.setObjectCity(city);
                authoritiesService.save(e);
            }

            // Build redirect URL
            String backUrl = req.getParameter("backUrl");

            if (backUrl != null && !backUrl.isEmpty()) {
                resp.sendRedirect(backUrl);
                return;
            }

            // fallback
            resp.sendRedirect("/objects?mode=edit");

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}