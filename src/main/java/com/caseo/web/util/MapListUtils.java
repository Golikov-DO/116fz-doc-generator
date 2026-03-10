package com.caseo.web.util;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class MapListUtils {

    public static <T> List<T> mapList(
            HttpServletRequest req,
            int currentObjectIndex,
            String indexField,
            String idField,
            Supplier<T> creator,
            BiConsumer<RequestIndexContext, T> mapper
    ) {

        List<T> list = new ArrayList<>();

        String[] index = req.getParameterValues(indexField);
        String[] ids = req.getParameterValues(idField);

        if (index == null) return list;

        for (int i = 0; i < index.length; i++) {

            if (index[i] != null && Integer.parseInt(index[i]) == currentObjectIndex) {

                T entity = creator.get();

                if (ids != null && i < ids.length && ids[i] != null && !ids[i].isEmpty()) {
                    try {
                        entity.getClass()
                                .getMethod("setId", Integer.class)
                                .invoke(entity, Integer.parseInt(ids[i]));
                    } catch (Exception ignored) {}
                }

                mapper.accept(new RequestIndexContext(req, i), entity);

                list.add(entity);
            }
        }

        return list;
    }

    public static <T> List<T> mapList(
            HttpServletRequest req,
            String idField,
            Supplier<T> creator,
            BiConsumer<RequestIndexContext, T> mapper
    ) {

        List<T> list = new ArrayList<>();

        String[] ids = req.getParameterValues(idField);

        int length = 0;

        if (ids != null) {
            length = ids.length;
        } else {
            // Если id нет (новые строки) пытаемся определить длину по mapper полям
            length = req.getParameterMap().values().stream()
                    .findFirst()
                    .map(arr -> arr.length)
                    .orElse(0);
        }

        for (int i = 0; i < length; i++) {

            T entity = creator.get();

            if (ids != null && i < ids.length && ids[i] != null && !ids[i].isEmpty()) {
                try {
                    entity.getClass()
                            .getMethod("setId", Integer.class)
                            .invoke(entity, Integer.parseInt(ids[i]));
                } catch (Exception ignored) {}
            }

            mapper.accept(new RequestIndexContext(req, i), entity);

            list.add(entity);
        }

        return list;
    }
}