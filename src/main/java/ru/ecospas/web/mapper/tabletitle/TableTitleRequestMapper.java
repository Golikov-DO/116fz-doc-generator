package ru.ecospas.web.mapper.tabletitle;

import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.ReferenceTableTitle;
import ru.ecospas.web.dto.request.tabletitle.SaveTableTitleRequest;

@Component
public class TableTitleRequestMapper {

    public void toTableTitle(SaveTableTitleRequest request, ReferenceTableTitle tableTitle) {
        tableTitle.setTableTextLinc(request.tableTextLinc());
        tableTitle.setTableTextName(request.tableTextName());
    }
}