package com.github.lzm320a99981e.cloud.commons.util;

public class ErrorCodeUtils {
    public static String parseErrorCode(Enum e) {
        final String[] items = e.name().split("_");
        return items[items.length - 1];
    }
}
