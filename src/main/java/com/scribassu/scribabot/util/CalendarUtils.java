package com.scribassu.scribabot.util;

import com.scribassu.tracto.domain.WeekType;

import java.util.Calendar;
import java.util.TimeZone;

public class CalendarUtils {

    public static Calendar getCalendar() {
        return Calendar.getInstance(TimeZone.getTimeZone("Europe/Samara"));
    }

    public static int getDayOfWeekStartsFromMonday(Calendar calendar) {
        int dayOfWeekStartsFromMonday = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if(dayOfWeekStartsFromMonday == 0) {
            dayOfWeekStartsFromMonday = 7;
        }
        if(dayOfWeekStartsFromMonday == -1) {
            dayOfWeekStartsFromMonday = 6;
        }
        return dayOfWeekStartsFromMonday;
    }
}
