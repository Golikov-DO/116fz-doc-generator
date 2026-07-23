package ru.ecospas.web.mapper.type;

import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.ReferenceType;
import ru.ecospas.web.dto.response.type.TypeListResponse;
import ru.ecospas.web.dto.response.type.TypeResponse;

import java.util.List;

@Component
public class TypeResponseMapper {

    public TypeResponse toResponse(ReferenceType type) {
        if (type == null) {
            return null;
        }
        return new TypeResponse(
                type.getId(),
                type.getType(),
                type.getTypeDefinition()
        );
    }

    public List<TypeResponse> toResponses(List<ReferenceType> list) {
        return list.stream().map(this::toResponse).toList();
    }

    public TypeListResponse toListResponse(ReferenceType type) {
        if (type == null) {
            return null;
        }
        return new TypeListResponse(type.getId(), type.getType());
    }

    public List<TypeListResponse> toListResponses(List<ReferenceType> list) {
        return list.stream().map(this::toListResponse).toList();
    }
}