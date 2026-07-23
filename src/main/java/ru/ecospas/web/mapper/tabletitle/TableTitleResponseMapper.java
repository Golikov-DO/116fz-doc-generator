package ru.ecospas.web.mapper.tabletitle;

import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.ReferenceTableTitle;
import ru.ecospas.web.dto.response.tabletitle.TableTitleListResponse;
import ru.ecospas.web.dto.response.tabletitle.TableTitleResponse;

import java.util.List;

@Component
public class TableTitleResponseMapper {

    public TableTitleResponse toResponse(ReferenceTableTitle tableTitle) {
        if (tableTitle == null) {
            return null;
        }
        return new TableTitleResponse(
                tableTitle.getId(),
                tableTitle.getTableTextLinc(),
                tableTitle.getTableTextName()
        );
    }

    public List<TableTitleResponse> toResponses(List<ReferenceTableTitle> list) {
        return list.stream().map(this::toResponse).toList();
    }

    public TableTitleListResponse toListResponse(ReferenceTableTitle tableTitle) {
        if (tableTitle == null) {
            return null;
        }
        return new TableTitleListResponse(
                tableTitle.getId(),
                tableTitle.getTableTextLinc(),
                tableTitle.getTableTextName()
        );
    }

    public List<TableTitleListResponse> toListResponses(List<ReferenceTableTitle> list) {
        return list.stream().map(this::toListResponse).toList();
    }
}