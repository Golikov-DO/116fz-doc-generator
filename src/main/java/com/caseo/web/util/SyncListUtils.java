package com.caseo.web.util;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class SyncListUtils {

    public static <T> void syncList(
            List<T> newList,
            List<T> oldList,
            Function<T, Integer> idGetter,
            Consumer<Integer> deleteFunc
    ) {
        if (oldList == null || oldList.isEmpty()) {
            return;
        }

        if (newList == null) {
            newList = List.of();
        }

        for (T oldItem : oldList) {
            Integer oldId = idGetter.apply(oldItem);
            if (oldId == null) continue;

            boolean stillExists = newList.stream()
                    .filter(n -> idGetter.apply(n) != null)
                    .anyMatch(n -> idGetter.apply(n).equals(oldId));

            if (!stillExists) {
                deleteFunc.accept(oldId);
            }
        }
    }
}