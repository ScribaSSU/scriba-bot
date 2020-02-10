package com.scribassu.scribabot.util;

import java.util.HashMap;
import java.util.Map;

public class DepartmentConverter {

    private static Map<String, String> converterMap = new HashMap<>();

    public static String convertToUrl(String department) {
        return converterMap.get(department);
    }

    public static void add(String department, String url) {
        converterMap.put(department, url);
    }
}
