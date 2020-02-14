package com.scribassu.scribabot.services.bot;

import com.scribassu.scribabot.commands.CommandText;
import com.scribassu.scribabot.entities.BotUser;
import com.scribassu.scribabot.services.CallRestService;
import com.scribassu.scribabot.util.BotMessageUtils;
import com.scribassu.scribabot.util.CalendarUtils;
import com.scribassu.scribabot.util.CalendarUtils;
import com.scribassu.tracto.domain.EducationForm;
import com.scribassu.tracto.domain.FullTimeLesson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

        switch(message) {
            case CommandText.MONDAY:
                if(BotMessageUtils.isBotUserFullTime(botUser)) {
                    lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            "1"
                    );
                    isBotUserFullTime = true;
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
                }
                break;
            case CommandText.TODAY:
                if(BotMessageUtils.isBotUserFullTime(botUser)) {
                    lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            String.valueOf(CalendarUtils.getDayOfWeekStartsFromMonday(calendar))
                    );
                    isBotUserFullTime = true;
                    isToday = true;
                }
                break;
            case CommandText.TOMORROW:
                calendar.add(Calendar.DAY_OF_WEEK, 1);
                if(BotMessageUtils.isBotUserFullTime(botUser)) {
                    lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            String.valueOf(CalendarUtils.getDayOfWeekStartsFromMonday(calendar))
                    );
                    isBotUserFullTime = true;
                    isTomorrow = true;
                }
                break;
            case CommandText.YESTERDAY:
                calendar.add(Calendar.DAY_OF_WEEK, -1);
                if(BotMessageUtils.isBotUserFullTime(botUser)) {
                    lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            String.valueOf(CalendarUtils.getDayOfWeekStartsFromMonday(calendar))
                    );
                    isBotUserFullTime = true;
                    isYesterday = true;
                }
                break;

        }

        if(isBotUserFullTime) {
            if(isToday) {
                return BotMessageUtils.getBotMessageForFullTimeLessons(lessons, CommandText.TODAY);
            }
            if(isTomorrow) {
                return BotMessageUtils.getBotMessageForFullTimeLessons(lessons, CommandText.TOMORROW);
            }
            if(isYesterday) {
                return BotMessageUtils.getBotMessageForFullTimeLessons(lessons, CommandText.YESTERDAY);
            }
            return BotMessageUtils.getBotMessageForFullTimeLessons(lessons, "");
        }
        else {
            return BotMessageUtils.getBotMessageForUnsupportedLessons();
        }
    }
}
