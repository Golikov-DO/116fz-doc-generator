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

        for (T oldItem : oldList) {

            Integer oldId = idGetter.apply(oldItem);

            boolean stillExists = newList.stream()
                    .anyMatch(n -> idGetter.apply(n) != null && idGetter.apply(n).equals(oldId));

            if (!stillExists) {
                deleteFunc.accept(oldId);
            }
        }
    }
}