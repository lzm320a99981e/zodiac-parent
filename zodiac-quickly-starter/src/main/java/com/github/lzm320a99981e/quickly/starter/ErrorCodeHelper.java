package com.github.lzm320a99981e.quickly.starter;

public class ErrorCodeHelper {
    public static String parseErrorCode(Enum e) {
        final String[] items = e.name().split("_");
        return items[items.length - 1];
    }
}
