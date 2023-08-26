package com.scribassu.scribabot.util;

import com.scribassu.tracto.domain.WeekType;

import java.util.Calendar;

public class WeekTypeUtils {

    public static WeekType getWeekType(Calendar calendar, int departmentShift) {
        return (calendar.get(Calendar.WEEK_OF_YEAR) + departmentShift) % 2 == 0 ? WeekType.NOM : WeekType.DENOM;
    }

    public static String weekTypeToLongString(WeekType weekType) {
        if (weekType.equals(WeekType.NOM)) {
            return "числитель";
        }
        if (weekType.equals(WeekType.DENOM)) {
            return "знаменатель";
        }
        return "";
    }
}
