package com.scribassu.scribabot;

import com.scribassu.scribabot.util.CalendarUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles(profiles = {"dev"})
public class CalendarUtilsTests {

    @Test
    public void testNomDenom() {
        var calendar = CalendarUtils.getCalendar();
        var weekTypeShiftTrue = CalendarUtils.getWeekType(calendar, true);
        var weekTypeShiftFalse = CalendarUtils.getWeekType(calendar, false);

        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        var weekTypeShiftTrueNextWeek = CalendarUtils.getWeekType(calendar, true);
        var weekTypeShiftFalseNextWeek = CalendarUtils.getWeekType(calendar, false);

        assertEquals(weekTypeShiftTrue, weekTypeShiftFalseNextWeek);
        assertEquals(weekTypeShiftFalse, weekTypeShiftTrueNextWeek);


    }
}