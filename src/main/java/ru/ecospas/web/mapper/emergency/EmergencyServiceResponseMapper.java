package ru.ecospas.web.mapper.emergency;

import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.ReferenceEmergencyServices;
import ru.ecospas.web.dto.response.emergency.EmergencyServiceListResponse;
import ru.ecospas.web.dto.response.emergency.EmergencyServiceResponse;

import java.util.List;

@Component
public class EmergencyServiceResponseMapper {

    public EmergencyServiceResponse toResponse(ReferenceEmergencyServices service) {
        if (service == null) {
            return null;
        }
        return new EmergencyServiceResponse(
                service.getId(),
                service.getServiceName(),
                service.getPositionContact(),
                service.getPhone(),
                service.getAddress()
        );
    }

    public List<EmergencyServiceResponse> toResponses(List<ReferenceEmergencyServices> services) {
        return services.stream().map(this::toResponse).toList();
    }

    public EmergencyServiceListResponse toListResponse(ReferenceEmergencyServices service) {
        if (service == null) {
            return null;
        }
        return new EmergencyServiceListResponse(service.getId(), service.getServiceName());
    }

    public List<EmergencyServiceListResponse> toListResponses(
            List<ReferenceEmergencyServices> services
    ) {
        return services.stream().map(this::toListResponse).toList();
    }
}