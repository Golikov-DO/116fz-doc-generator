package com.caseo.word.util;

public class LayoutUtil {
    public static int calcLines(String text, int limit) {
        if (text == null || text.isEmpty()) return 1;
        String[] lines = text.split("\n");
        int total = 0;
        for (String line : lines) {
            total += Math.max(1, (int) Math.ceil((double) line.length() / limit));
        }
        return total;
    }

    public static int rootSection(String s) {
        int dot = s.indexOf('.');
        return Integer.parseInt(dot == -1 ? s : s.substring(0, dot));
    }
}
