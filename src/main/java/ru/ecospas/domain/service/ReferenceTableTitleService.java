package ru.ecospas.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.domain.model.ReferenceTableTitle;
import ru.ecospas.domain.repository.ReferenceTableTitleRepository;
import ru.ecospas.web.dto.request.tabletitle.SaveTableTitleRequest;
import ru.ecospas.web.mapper.tabletitle.TableTitleRequestMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReferenceTableTitleService {

    private final ReferenceTableTitleRepository repository;
    private final TableTitleRequestMapper requestMapper;

    public List<ReferenceTableTitle> findAll() {
        return repository.findAll();
    }

    public ReferenceTableTitle loadRest(Integer id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional
    public ReferenceTableTitle create(SaveTableTitleRequest request) {
        ReferenceTableTitle tableTitle = new ReferenceTableTitle();
        return save(request, tableTitle);
    }

    @Transactional
    public ReferenceTableTitle save(SaveTableTitleRequest request,ReferenceTableTitle tableTitle) {
        requestMapper.toTableTitle(request, tableTitle);
        return repository.save(tableTitle);
    }

    @Transactional
    public ReferenceTableTitle update(Integer id, SaveTableTitleRequest request) {
        ReferenceTableTitle tableTitle = loadRest(id);
        if (tableTitle == null) {
            return null;
        }
        return save(request, tableTitle);
    }

    @Transactional
    public void deleteRest(Integer id) {
        ReferenceTableTitle tableTitle = loadRest(id);
        if (tableTitle == null) {
            return;
        }
        repository.delete(tableTitle);
    }
}