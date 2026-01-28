package com.caseo.word;

import com.caseo.domain.model.NumberedItem;

import java.util.List;

public class WordListBuilder {

    public static String build(
            List<? extends NumberedItem> items,
            ListFormat format
    ) {
        if (items == null || items.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        for (NumberedItem item : items) {
            sb.append(formatNumber(item.getNum(), format))
                    .append(item.getName())
                    .append("\n");
        }

        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    private static String formatNumber(int num, ListFormat format) {
        return switch (format) {
            case DOT -> num + "). ";
            case NO_SIGN -> "№ " + num + " ";
        };
    }
}
