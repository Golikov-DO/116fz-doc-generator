package ru.ecospas.web.mapper.type;

import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.ReferenceType;
import ru.ecospas.web.dto.request.type.SaveTypeRequest;

@Component
public class TypeRequestMapper {

    public void toType(SaveTypeRequest request, ReferenceType type) {
        type.setType(request.type());
        type.setTypeDefinition(request.typeDefinition());
    }
}