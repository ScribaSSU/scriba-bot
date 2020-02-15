package com.scribassu.scribabot.services.bot;

import com.scribassu.scribabot.text.CommandText;
import com.scribassu.scribabot.entities.BotUser;
import com.scribassu.scribabot.services.CallRestService;
import com.scribassu.scribabot.util.BotMessageUtils;
import com.scribassu.scribabot.util.CalendarUtils;
import com.scribassu.tracto.domain.FullTimeLesson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FullTimeLessonService implements BotMessageService {

    private final CallRestService callRestService;

    @Autowired
    public FullTimeLessonService(CallRestService callRestService) {
        this.callRestService = callRestService;
    }

    @Override
    public Map<String, String> getBotMessage(String message, BotUser botUser) {
        Calendar calendar = CalendarUtils.getCalendar();
        List<FullTimeLesson> lessons = new ArrayList<>();
        boolean isBotUserFullTime = false;
        boolean isToday = false;
        boolean isTomorrow = false;
        boolean isYesterday = false;
        String dayNumber = "";

        switch(message) {
            case CommandText.MONDAY:
                if(BotMessageUtils.isBotUserFullTime(botUser)) {
                    lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            "1"
                    );
                    isBotUserFullTime = true;
                    dayNumber = "1";
                }
                break;
            case CommandText.TUESDAY:
                if(BotMessageUtils.isBotUserFullTime(botUser)) {
                    lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            "2"
                    );
                    isBotUserFullTime = true;
                    dayNumber = "2";
                }
                break;
            case CommandText.WEDNESDAY:
                if(BotMessageUtils.isBotUserFullTime(botUser)) {
                    lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            "3"
                    );
                    isBotUserFullTime = true;
                    dayNumber = "3";
                }
                break;
            case CommandText.THURSDAY:
                if(BotMessageUtils.isBotUserFullTime(botUser)) {
                    lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            "4"
                    );
                    isBotUserFullTime = true;
                    dayNumber = "4";
                }
                break;
            case CommandText.FRIDAY:
                if(BotMessageUtils.isBotUserFullTime(botUser)) {
                    lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            "5"
                    );
                    isBotUserFullTime = true;
                    dayNumber = "5";
                }
                break;
            case CommandText.SATURDAY:
                if(BotMessageUtils.isBotUserFullTime(botUser)) {
                    lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            "6"
                    );
                    isBotUserFullTime = true;
                    dayNumber = "6";
                }
                break;
            case CommandText.TODAY:
                String day = String.valueOf(CalendarUtils.getDayOfWeekStartsFromMonday(calendar));
                if(BotMessageUtils.isBotUserFullTime(botUser)) {
                    lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            day
                    );
                    isBotUserFullTime = true;
                    isToday = true;
                    dayNumber = day;
                }
                break;
            case CommandText.TOMORROW:
                calendar.add(Calendar.DAY_OF_WEEK, 1);
                day = String.valueOf(CalendarUtils.getDayOfWeekStartsFromMonday(calendar));
                if(BotMessageUtils.isBotUserFullTime(botUser)) {
                    lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            day
                    );
                    isBotUserFullTime = true;
                    isTomorrow = true;
                    dayNumber = day;
                }
                break;
            case CommandText.YESTERDAY:
                calendar.add(Calendar.DAY_OF_WEEK, -1);
                day = String.valueOf(CalendarUtils.getDayOfWeekStartsFromMonday(calendar));
                if(BotMessageUtils.isBotUserFullTime(botUser)) {
                    lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            day
                    );
                    isBotUserFullTime = true;
                    isYesterday = true;
                    dayNumber = day;
                }
                break;
        }

        if(isBotUserFullTime) {
            if(isToday) {
                return BotMessageUtils.getBotMessageForFullTimeLessons(lessons, CommandText.TODAY, dayNumber);
            }
            if(isTomorrow) {
                return BotMessageUtils.getBotMessageForFullTimeLessons(lessons, CommandText.TOMORROW, dayNumber);
            }
            if(isYesterday) {
                return BotMessageUtils.getBotMessageForFullTimeLessons(lessons, CommandText.YESTERDAY, dayNumber);
            }
            return BotMessageUtils.getBotMessageForFullTimeLessons(lessons, "", dayNumber);
        }
        else {
            return BotMessageUtils.getBotMessageForUnsupportedLessons();
        }
    }
}
