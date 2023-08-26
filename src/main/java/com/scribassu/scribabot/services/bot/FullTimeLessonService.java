package com.scribassu.scribabot.services.bot;

import com.scribassu.scribabot.dto.rest.FullTimeLessonDto;
import com.scribassu.scribabot.dto.rest.TeacherFullTimeLessonDto;
import com.scribassu.scribabot.generators.BotMessageGenerator;
import com.scribassu.scribabot.model.BotMessage;
import com.scribassu.scribabot.model.InnerBotUser;
import com.scribassu.scribabot.services.CallRestService;
import com.scribassu.scribabot.text.CommandText;
import com.scribassu.scribabot.util.CalendarUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@Data
public class FullTimeLessonService implements BotMessageService {

    private final CallRestService callRestService;
    private final BotMessageGenerator botMessageGenerator;

    @Override
    public CompletableFuture<BotMessage> getBotMessage(String message, InnerBotUser botUser) {
        if (botUser.wantTeacherSchedule()) {
            return getTeacherBotMessage(message, botUser);
        } else {
            return getStudentBotMessage(message, botUser);
        }
    }

    private CompletableFuture<BotMessage> getTeacherBotMessage(String message, InnerBotUser botUser) {
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
// todo why isnomdenom with botuser
        if (isToday) {
            return CompletableFuture.completedFuture(botMessageGenerator.getBotMessageForTeacherFullTimeLessons(lessons, CommandText.TODAY, botUser));
        }
        if (isTomorrow) {
            return CompletableFuture.completedFuture(botMessageGenerator.getBotMessageForTeacherFullTimeLessons(lessons, CommandText.TOMORROW, botUser));
        }
        if (isYesterday) {
            return CompletableFuture.completedFuture(botMessageGenerator.getBotMessageForTeacherFullTimeLessons(lessons, CommandText.YESTERDAY, botUser));
        }
        return CompletableFuture.completedFuture(botMessageGenerator.getBotMessageForTeacherFullTimeLessons(lessons, "", botUser));
    }

    private CompletableFuture<BotMessage> getStudentBotMessage(String message, InnerBotUser botUser) {
        Calendar calendar = CalendarUtils.getCalendar();
        FullTimeLessonDto lessons = new FullTimeLessonDto();
        boolean isBotUserFullTime = false;
        boolean isToday = false;
        boolean isTomorrow = false;
        boolean isYesterday = false;
        boolean isAll = false;

        switch (message) {
            case CommandText.ALL_LESSONS:
                if (InnerBotUser.isBotUserFullTime(botUser)) {
                    lessons = callRestService.getFullTimeLessonsByGroup(
                            botUser.getDepartment(),
                            botUser.getGroupNumber()
                    );
                    isBotUserFullTime = true;
                    isAll = true;
                }
                break;
            case CommandText.MONDAY:
                if (InnerBotUser.isBotUserFullTime(botUser)) {
                    lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            "1"
                    );
                    isBotUserFullTime = true;
                }
                break;
            case CommandText.TUESDAY:
                if (InnerBotUser.isBotUserFullTime(botUser)) {
                    lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            "2"
                    );
                    isBotUserFullTime = true;
                }
                break;
            case CommandText.WEDNESDAY:
                if (InnerBotUser.isBotUserFullTime(botUser)) {
                    lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            "3"
                    );
                    isBotUserFullTime = true;
                }
                break;
            case CommandText.THURSDAY:
                if (InnerBotUser.isBotUserFullTime(botUser)) {
                    lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            "4"
                    );
                    isBotUserFullTime = true;
                }
                break;
            case CommandText.FRIDAY:
                if (InnerBotUser.isBotUserFullTime(botUser)) {
                    lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            "5"
                    );
                    isBotUserFullTime = true;
                }
                break;
            case CommandText.SATURDAY:
                if (InnerBotUser.isBotUserFullTime(botUser)) {
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
                if (InnerBotUser.isBotUserFullTime(botUser)) {
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
                if (InnerBotUser.isBotUserFullTime(botUser)) {
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
                if (InnerBotUser.isBotUserFullTime(botUser)) {
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
                return CompletableFuture.completedFuture(botMessageGenerator.getBotMessageForFullTimeLessons(lessons, CommandText.TODAY, botUser));
            }
            if (isTomorrow) {
                return CompletableFuture.completedFuture(botMessageGenerator.getBotMessageForFullTimeLessons(lessons, CommandText.TOMORROW, botUser));
            }
            if (isYesterday) {
                return CompletableFuture.completedFuture(botMessageGenerator.getBotMessageForFullTimeLessons(lessons, CommandText.YESTERDAY, botUser));
            }
            if (isAll) {
                return CompletableFuture.completedFuture(botMessageGenerator.getBotMessageForFullTimeLessonsAll(lessons, botUser));
            }
            return CompletableFuture.completedFuture(botMessageGenerator.getBotMessageForFullTimeLessons(lessons, "", botUser));
        } else {
            return CompletableFuture.completedFuture(botMessageGenerator.getBotMessageForUnsupportedLessons(botUser));
        }
    }
}
