package com.scribassu.scribabot.util;

import java.util.HashMap;
import java.util.Map;

public class DepartmentConverter {

    private static Map<String, String> converterMap = new HashMap<>();
    private static Map<String, String> invertedConverterMap = new HashMap<>();

    public static String convertToUrl(String department) {
        return converterMap.get(department);
    }

    public static void add(String department, String url) {
        converterMap.put(department, url);
        invertedConverterMap.put(url, department);
    }

    public static String getDepartmentByUrl(String url) {
        return invertedConverterMap.get(url);
    }

    public static boolean isCollege(String url) {
        return url.equals("cre") || url.equals("kgl");
    }
}
