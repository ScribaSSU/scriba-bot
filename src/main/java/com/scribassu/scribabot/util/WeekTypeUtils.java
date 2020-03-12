package com.scribassu.scribabot.util;

import com.scribassu.tracto.domain.WeekType;

import java.util.Calendar;

public class WeekTypeUtils {

    public static byte SHIFT_WEEK_TYPE = 0;

    public static WeekType getWeekType(Calendar calendar) {
        return (calendar.get(Calendar.WEEK_OF_YEAR) + SHIFT_WEEK_TYPE) % 2 == 0 ? WeekType.NOM : WeekType.DENOM;
    }

    public static String weekTypeToLongString(WeekType weekType) {
        if(weekType.equals(WeekType.NOM)) {
            return "числитель";
        }
        if(weekType.equals(WeekType.DENOM)) {
            return "знаменатель";
        }
        return "";
    }
}
