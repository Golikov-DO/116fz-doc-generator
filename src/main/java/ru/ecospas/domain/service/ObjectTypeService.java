package ru.ecospas.domain.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.domain.model.ObjectType;
import ru.ecospas.domain.repository.ObjectTypeRepository;

import static ru.ecospas.web.util.RequestUtils.param;

@Service
@RequiredArgsConstructor
@Transactional
public class ObjectTypeService {

    private final ObjectTypeRepository objectTypeRepository;

    public ObjectType load(Integer id) {
        return objectTypeRepository.findById(id).orElse(null);
    }

    public ObjectType create() {

        ObjectType objectType = new ObjectType();
        objectType.setType("");
        objectType.setTypeDefinition("");

        return objectType;
    }

    public ObjectType save(
            HttpServletRequest req,
            ObjectType objectType
    ) {

        objectType.setType(
                param(req, "type")
        );

        objectType.setTypeDefinition(
                param(req, "object_type_definitions")
        );

        return objectTypeRepository.save(objectType);
    }

    public ObjectType createEmpty() {

        return objectTypeRepository.save(
                create()
        );
    }

    public void delete(Integer id) {
        objectTypeRepository.deleteById(id);
    }
}