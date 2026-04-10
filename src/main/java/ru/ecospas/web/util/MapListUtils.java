package ru.ecospas.web.util;

import jakarta.servlet.http.HttpServletRequest;
import ru.ecospas.domain.model.BaseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class MapListUtils {

    public static <T extends BaseEntity> List<T> mapList(
            HttpServletRequest req,
            String idField,
            Supplier<T> creator,
            BiConsumer<RequestIndexContext, T> mapper
    ) {
        List<T> list = new ArrayList<>();
        String[] ids = req.getParameterValues(idField);

        int length = (ids != null) ? ids.length :
                req.getParameterMap().values().stream()
                        .findFirst().map(arr -> arr.length).orElse(0);

        for (int i = 0; i < length; i++) {
            T entity = creator.get();

            // Прямая установка ID без рефлексии!
            if (ids != null && ids[i] != null && !ids[i].isEmpty()) {
                try {
                    entity.setId(Integer.parseInt(ids[i]));
                } catch (NumberFormatException ignored) {}
            }

            mapper.accept(new RequestIndexContext(req, i), entity);
            list.add(entity);
        }
        return list;
    }
}