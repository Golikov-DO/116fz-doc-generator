package com.caseo.domain.util;

public final class RussianPlural {

    private RussianPlural() {}

    public static String technologicalBlock(int count) {

        if (count % 10 == 1 && count % 100 != 11) {
            return count + " технологический блок";
        }

        if (count % 10 >= 2 && count % 10 <= 4 &&
                (count % 100 < 10 || count % 100 >= 20)) {
            return count + " технологических блока";
        }

        return count + " технологических блоков";
    }
}
