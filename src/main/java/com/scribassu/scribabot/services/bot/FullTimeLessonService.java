package com.scribassu.scribabot.services.bot;

import com.scribassu.scribabot.dto.BotMessage;
import com.scribassu.scribabot.dto.InnerBotUser;
import com.scribassu.scribabot.dto.rest.FullTimeLessonDto;
import com.scribassu.scribabot.dto.rest.TeacherFullTimeLessonDto;
import com.scribassu.scribabot.entities.BotUser;
import com.scribassu.scribabot.services.CallRestService;
import com.scribassu.scribabot.text.CommandText;
import com.scribassu.scribabot.util.BotMessageUtils;
import com.scribassu.scribabot.util.CalendarUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class FullTimeLessonService implements BotMessageService {

    private final CallRestService callRestService;

    @Autowired
    public FullTimeLessonService(CallRestService callRestService) {
        this.callRestService = callRestService;
    }

    @Override
    public BotMessage getBotMessage(String message, InnerBotUser botUser) {
        if (botUser.wantTeacherSchedule()) {
            return getTeacherBotMessage(message, botUser);
        } else {
            return getStudentBotMessage(message, botUser);
        }
    }

    private BotMessage getTeacherBotMessage(String message, InnerBotUser botUser) {
        Calendar calendar = CalendarUtils.getCalendar();
        TeacherFullTimeLessonDto lessons = new TeacherFullTimeLessonDto();
        boolean isToday = false;
        boolean isTomorrow = false;
        boolean isYesterday = false;
        String teacherId = botUser.getPreviousUserMessage().split(" ")[1];

        switch (message) {
            case CommandText.MONDAY:
                lessons = callRestService.getTeacherLessonsByDay(teacherId, "1");
                break;
            case CommandText.TUESDAY:
                lessons = callRestService.getTeacherLessonsByDay(teacherId, "2");
                break;
            case CommandText.WEDNESDAY:
                lessons = callRestService.getTeacherLessonsByDay(teacherId, "3");
                break;
            case CommandText.THURSDAY:
                lessons = callRestService.getTeacherLessonsByDay(teacherId, "4");
                break;
            case CommandText.FRIDAY:
                lessons = callRestService.getTeacherLessonsByDay(teacherId, "5");
                break;
            case CommandText.SATURDAY:
                lessons = callRestService.getTeacherLessonsByDay(teacherId, "6");
                break;
            case CommandText.TODAY:
                String day = String.valueOf(CalendarUtils.getDayOfWeekStartsFromMonday(calendar));
                lessons = callRestService.getTeacherLessonsByDay(teacherId, day);
                isToday = true;
                break;
            case CommandText.TOMORROW:
                calendar.add(Calendar.DAY_OF_WEEK, 1);
                day = String.valueOf(CalendarUtils.getDayOfWeekStartsFromMonday(calendar));
                lessons = callRestService.getTeacherLessonsByDay(teacherId, day);
                isTomorrow = true;
                break;
            case CommandText.YESTERDAY:
                calendar.add(Calendar.DAY_OF_WEEK, -1);
                day = String.valueOf(CalendarUtils.getDayOfWeekStartsFromMonday(calendar));
                lessons = callRestService.getTeacherLessonsByDay(teacherId, day);
                isYesterday = true;
                break;
        }

        if (isToday) {
            return BotMessageUtils.getBotMessageForTeacherFullTimeLessons(lessons, CommandText.TODAY, botUser.isFilterNomDenom(), botUser);
        }
        if (isTomorrow) {
            return BotMessageUtils.getBotMessageForTeacherFullTimeLessons(lessons, CommandText.TOMORROW, botUser.isFilterNomDenom(), botUser);
        }
        if (isYesterday) {
            return BotMessageUtils.getBotMessageForTeacherFullTimeLessons(lessons, CommandText.YESTERDAY, botUser.isFilterNomDenom(), botUser);
        }
        return BotMessageUtils.getBotMessageForTeacherFullTimeLessons(lessons, "", botUser.isFilterNomDenom(), botUser);
    }

    private BotMessage getStudentBotMessage(String message, InnerBotUser botUser) {
        Calendar calendar = CalendarUtils.getCalendar();
        FullTimeLessonDto lessons = new FullTimeLessonDto();
        boolean isBotUserFullTime = false;
        boolean isToday = false;
        boolean isTomorrow = false;
        boolean isYesterday = false;

        switch (message) {
            case CommandText.MONDAY:
                if (BotMessageUtils.isBotUserFullTime(botUser)) {
                    lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            "1"
                    );
                    isBotUserFullTime = true;
                }
                break;
            case CommandText.TUESDAY:
                if (BotMessageUtils.isBotUserFullTime(botUser)) {
                    lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            "2"
                    );
                    isBotUserFullTime = true;
                }
                break;
            case CommandText.WEDNESDAY:
                if (BotMessageUtils.isBotUserFullTime(botUser)) {
                    lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            "3"
                    );
                    isBotUserFullTime = true;
                }
                break;
            case CommandText.THURSDAY:
                if (BotMessageUtils.isBotUserFullTime(botUser)) {
                    lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            "4"
                    );
                    isBotUserFullTime = true;
                }
                break;
            case CommandText.FRIDAY:
                if (BotMessageUtils.isBotUserFullTime(botUser)) {
                    lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            "5"
                    );
                    isBotUserFullTime = true;
                }
                break;
            case CommandText.SATURDAY:
                if (BotMessageUtils.isBotUserFullTime(botUser)) {
                    lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            "6"
                    );
                    isBotUserFullTime = true;
                }
                break;
            case CommandText.TODAY:
                String day = String.valueOf(CalendarUtils.getDayOfWeekStartsFromMonday(calendar));
                if (BotMessageUtils.isBotUserFullTime(botUser)) {
                    lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            day
                    );
                    isBotUserFullTime = true;
                    isToday = true;
                }
                break;
            case CommandText.TOMORROW:
                calendar.add(Calendar.DAY_OF_WEEK, 1);
                day = String.valueOf(CalendarUtils.getDayOfWeekStartsFromMonday(calendar));
                if (BotMessageUtils.isBotUserFullTime(botUser)) {
                    lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            day
                    );
                    isBotUserFullTime = true;
                    isTomorrow = true;
                }
                break;
            case CommandText.YESTERDAY:
                calendar.add(Calendar.DAY_OF_WEEK, -1);
                day = String.valueOf(CalendarUtils.getDayOfWeekStartsFromMonday(calendar));
                if (BotMessageUtils.isBotUserFullTime(botUser)) {
                    lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            day
                    );
                    isBotUserFullTime = true;
                    isYesterday = true;
                }
                break;
        }

        if (isBotUserFullTime) {
            if (isToday) {
                return BotMessageUtils.getBotMessageForFullTimeLessons(lessons, CommandText.TODAY, botUser.isFilterNomDenom(), botUser);
            }
            if (isTomorrow) {
                return BotMessageUtils.getBotMessageForFullTimeLessons(lessons, CommandText.TOMORROW, botUser.isFilterNomDenom(), botUser);
            }
            if (isYesterday) {
                return BotMessageUtils.getBotMessageForFullTimeLessons(lessons, CommandText.YESTERDAY, botUser.isFilterNomDenom(), botUser);
            }
            return BotMessageUtils.getBotMessageForFullTimeLessons(lessons, "", botUser.isFilterNomDenom(), botUser);
        } else {
            return BotMessageUtils.getBotMessageForUnsupportedLessons(botUser);
        }
    }
}
