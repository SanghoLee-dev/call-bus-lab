package com.callbus.codingtest.utils;

public class StringSplitUtils {
    public static String[] splitStringByCharacter(final String any) {
        final int length = any.toCharArray().length;
        String[] stringArray = new String[length];

        for (int i = 0; i < length; i++) {
            final String substring = any.substring(i, i + 1);
            stringArray[i] = substring;
        }

        return stringArray;
    }
}
