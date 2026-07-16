package ru.ecospas.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.ecospas.domain.model.ReferenceTableTitle;
import ru.ecospas.domain.service.ReferenceTableTitleService;
import ru.ecospas.web.dto.request.tabletitle.SaveTableTitleRequest;
import ru.ecospas.web.dto.response.tabletitle.TableTitleListResponse;
import ru.ecospas.web.dto.response.tabletitle.TableTitleResponse;
import ru.ecospas.web.mapper.tabletitle.TableTitleResponseMapper;

import java.util.List;

@RestController
@RequestMapping("/api/table-titles")
@RequiredArgsConstructor
public class TableTitleRestController {

    private final ReferenceTableTitleService tableTitleService;
    private final TableTitleResponseMapper responseMapper;

    @GetMapping
    public List<TableTitleListResponse> getTableTitles() {
        return responseMapper.toListResponses(tableTitleService.findAll());
    }

    @GetMapping("/{id}")
    public TableTitleResponse getTableTitle(@PathVariable Integer id) {
        ReferenceTableTitle tableTitle = tableTitleService.loadRest(id);
        if (tableTitle == null) {
            throw new IllegalArgumentException(
                    "Table title not found"
            );
        }
        return responseMapper.toResponse(tableTitle);
    }

    @PostMapping
    public TableTitleResponse createTableTitle(
            @Valid
            @RequestBody
            SaveTableTitleRequest request
    ) {
        return responseMapper.toResponse(tableTitleService.create(request));
    }

    @PutMapping("/{id}")
    public TableTitleResponse updateTableTitle(
            @PathVariable Integer id,
            @Valid
            @RequestBody
            SaveTableTitleRequest request
    ) {
        ReferenceTableTitle tableTitle = tableTitleService.update(id, request);
        if (tableTitle == null) {
            throw new IllegalArgumentException("Table title not found");
        }
        return responseMapper.toResponse(tableTitle);
    }

    @DeleteMapping("/{id}")
    public void deleteTableTitle(@PathVariable Integer id) {
        tableTitleService.deleteRest(id);
    }
}