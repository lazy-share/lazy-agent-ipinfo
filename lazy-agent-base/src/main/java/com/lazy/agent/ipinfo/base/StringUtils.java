package com.lazy.agent.ipinfo.base;

public class StringUtils {

    public static boolean isBlank(String s) {
        return s == null || "".equals(s);
    }

    public static boolean isNotBlank(String s) {
        return !isBlank(s);
    }
}
