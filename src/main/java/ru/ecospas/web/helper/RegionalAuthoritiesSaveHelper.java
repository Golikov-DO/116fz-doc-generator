package ru.ecospas.web.helper;

import jakarta.servlet.http.HttpServletRequest;
import ru.ecospas.domain.model.ObjectRegionalAuthorities;
import ru.ecospas.domain.model.ReferenceCity;
import ru.ecospas.web.util.MapListUtils;
import ru.ecospas.web.util.RequestIndexContext;

import java.util.List;

import static ru.ecospas.web.util.RequestUtils.param;

public class RegionalAuthoritiesSaveHelper {

    public void mapCity(HttpServletRequest req, ReferenceCity city) {
        city.setGeoRelief(param(req, "geo_relief"));
        city.setGeoGeology(param(req, "geo_geology"));
        city.setClimatDesc(param(req, "climat_desc"));
        city.setHydroDesc(param(req, "hydro_desc"));
        city.setInfraTransport(param(req, "infra_transport"));
        city.setInfraEngineering(param(req, "infra_engineering"));
        city.setInfraOrganizations(param(req, "infra_organizations"));
        city.setNearbyTowns(param(req, "nearby_towns"));
        city.setMassPeoplePlaces(param(req, "mass_people_places"));
        city.setAdminStatus(param(req, "admin_status"));
        city.setDistCenters(param(req, "dist_centers"));
        city.setCityName(param(req, "city_name"));
    }

    public void mapAuthority(RequestIndexContext ctx, ObjectRegionalAuthorities authorities) {
        int i = ctx.index;

        authorities.setName(param(ctx.req, "ra_name[]", i));
        authorities.setDepartment(param(ctx.req, "ra_department[]", i));
        authorities.setPhoneNumber(param(ctx.req, "ra_phone[]", i));
        authorities.setAddress(param(ctx.req, "ra_address[]", i));
    }

    public List<ObjectRegionalAuthorities> mapAuthorities(HttpServletRequest req) {
        return MapListUtils.mapList(
                req,
                "ra_id[]",
                ObjectRegionalAuthorities::new,
                this::mapAuthority
        );
    }
}