package ru.ecospas.domain.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.domain.model.ReferenceType;
import ru.ecospas.domain.repository.ReferenceTypeRepository;
import ru.ecospas.web.dto.request.type.SaveTypeRequest;
import ru.ecospas.web.mapper.type.TypeRequestMapper;

import java.util.List;

import static ru.ecospas.web.util.RequestUtils.param;

@Service
@RequiredArgsConstructor
@Transactional
public class ReferenceTypeService {

    private final ReferenceTypeRepository referenceTypeRepository;
    private final TypeRequestMapper requestMapper;

    public ReferenceType load(Integer id) {
        return referenceTypeRepository.findById(id).orElse(null);
    }

    public ReferenceType create() {

        ReferenceType referenceType = new ReferenceType();
        referenceType.setType("");
        referenceType.setTypeDefinition("");

        return referenceType;
    }

    public ReferenceType save(
            HttpServletRequest req,
            ReferenceType referenceType
    ) {

        referenceType.setType(
                param(req, "type")
        );

        referenceType.setTypeDefinition(
                param(req, "object_type_definitions")
        );

        return referenceTypeRepository.save(referenceType);
    }

    public ReferenceType createEmpty() {

        return referenceTypeRepository.save(
                create()
        );
    }

    public void delete(Integer id) {
        referenceTypeRepository.deleteById(id);
    }

    //REST
    @Transactional(readOnly = true)
    public ReferenceType loadRest(Integer id) {
        return referenceTypeRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<ReferenceType> findAll() {
        return referenceTypeRepository.findAll();
    }

    @Transactional
    public ReferenceType create(SaveTypeRequest request) {
        ReferenceType type = new ReferenceType();
        return save(request, type);
    }

    @Transactional
    public ReferenceType save(SaveTypeRequest request, ReferenceType type) {
        requestMapper.toType(request, type);
        return referenceTypeRepository.save(type);
    }

    @Transactional
    public ReferenceType update(Integer id, SaveTypeRequest request) {
        ReferenceType type = loadRest(id);
        if (type == null) {
            return null;
        }
        return save(request, type);
    }

    @Transactional
    public void deleteRest(Integer id) {
        ReferenceType type = loadRest(id);
        if (type == null) {
            return;
        }
        referenceTypeRepository.delete(type);
    }
}