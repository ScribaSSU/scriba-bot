package com.scribassu.scribabot.util;

import com.scribassu.tracto.dto.WeekType;

import java.util.Calendar;
import java.util.TimeZone;

// todo jodatime?
public class CalendarUtils {

    public static Calendar getCalendar() {
        return Calendar.getInstance(TimeZone.getTimeZone("Europe/Samara"));
    }

    public static int getDayOfWeekStartsFromMonday(Calendar calendar) {
        int dayOfWeekStartsFromMonday = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeekStartsFromMonday == 0) {
            dayOfWeekStartsFromMonday = 7;
        }
        if (dayOfWeekStartsFromMonday == -1) {
            dayOfWeekStartsFromMonday = 6;
        }
        return dayOfWeekStartsFromMonday;
    }

    public static WeekType getWeekType(Calendar calendar, boolean departmentShift) {
        return calendar.get(Calendar.WEEK_OF_YEAR) % 2 == 0 && departmentShift ? WeekType.NOM : WeekType.DENOM;
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
