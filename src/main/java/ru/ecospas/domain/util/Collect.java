package ru.ecospas.domain.util;

import java.util.Set;

public class Collect {

    private Collect() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void collect(String value, Set<Integer> target) {
        if (value == null || value.isBlank()) return;

        for (String s : value.split(",")) {
            try {
                target.add(Integer.parseInt(s.trim()));
            } catch (Exception ignored) {}
        }
    }
}
