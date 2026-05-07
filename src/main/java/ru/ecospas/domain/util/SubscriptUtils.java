package ru.ecospas.domain.util;

public class SubscriptUtils {

    private SubscriptUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    private static final String[] SUB = {
            "₀","₁","₂","₃","₄","₅","₆","₇","₈","₉"
    };

    public static String toSubscript(int number) {
        String numStr = String.valueOf(number);
        StringBuilder result = new StringBuilder();

        for (char c : numStr.toCharArray()) {
            if (Character.isDigit(c)) {
                result.append(SUB[c - '0']);
            }
        }

        return result.toString();
    }
}