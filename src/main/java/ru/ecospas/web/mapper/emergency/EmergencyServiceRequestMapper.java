package ru.ecospas.web.mapper.emergency;

import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.ReferenceEmergencyServices;
import ru.ecospas.web.dto.request.emergency.SaveEmergencyServiceRequest;

@Component
public class EmergencyServiceRequestMapper {

    public void toEmergencyService(
            SaveEmergencyServiceRequest request,
            ReferenceEmergencyServices service
    ) {
        service.setServiceName(request.serviceName());
        service.setPositionContact(request.positionContact());
        service.setPhone(request.phone());
        service.setAddress(request.address());
    }
}