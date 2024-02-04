package com.scribassu.scribabot.services.bot_message;

import com.scribassu.scribabot.generators.BotMessageGenerator;
import com.scribassu.scribabot.generators.InnerKeyboardGenerator;
import com.scribassu.scribabot.model.BotMessage;
import com.scribassu.scribabot.model.BotUser;
import com.scribassu.scribabot.services.BotMessageService;
import com.scribassu.scribabot.services.BotUserService;
import com.scribassu.scribabot.services.CallRestService;
import com.scribassu.scribabot.text.CommandText;
import com.scribassu.scribabot.util.CalendarUtils;
import com.scribassu.scribabot.util.Constants;
import com.scribassu.tracto.dto.TeacherDto;
import com.scribassu.tracto.dto.TeacherFullTimeLessonListDto;
import com.scribassu.tracto.dto.TeacherListDto;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import static com.scribassu.scribabot.text.CommandText.TEACHER_PREFIX;
import static com.scribassu.scribabot.text.MessageText.*;

@Service
@Slf4j
@Data
public class TeacherService implements BotMessageService {

    private final CallRestService callRestService;
    private final BotUserService botUserService;
    private final InnerKeyboardGenerator innerKeyboardGenerator;
    private final BotMessageGenerator botMessageGenerator;

    @Override
    public boolean shouldAccept(String message, BotUser botUser) {
        return userWantsTeacherSchedule(message)
                || userExitsTeacherSchedule(message)
                || userSendsTeacherPrompt(botUser)
                || userChoosesTeacher(botUser)
                || botUser.wantTeacherSchedule();
    }

    // todo CommandText.TEACHER_SCHEDULE_FOR_EXTRAMURAL
    @Override
    public CompletableFuture<BotMessage> getBotMessage(String message, BotUser botUser) {
        // Предложение ввести ФИО преподавателя
        if (userWantsTeacherSchedule(message)) {
            botUserService.updatePreviousUserMessage(CommandText.TEACHER_SCHEDULE, botUser);
            return CompletableFuture.completedFuture(new BotMessage(TEACHER_NAME_PROMPT, innerKeyboardGenerator.mainMenu(botUser), botUser));
        }

        if (userExitsTeacherSchedule(message)) {
            botUserService.resetPreviousUserMessage(botUser);
            return CompletableFuture.completedFuture(new BotMessage(RETURN_MAIN_MENU, innerKeyboardGenerator.mainMenu(botUser), botUser));
        }

        if (userSendsTeacherPrompt(botUser)) {
            return tryToSendTeacherList(message, botUser);
        }

        if (userChoosesTeacher(botUser)) {
            return tryToSaveUsersTeacherChoice(message, botUser);
        }

        return getFullTimeSchedule(message, botUser);
    }

    private String retrieveTeacherId(String message) {
        var withoutPrefix  = message.replace(TEACHER_PREFIX.toLowerCase(Locale.ROOT), "").trim();
        return withoutPrefix.substring(0, withoutPrefix.indexOf(" ") + 1);
    }

    private boolean userSendsTeacherPrompt(BotUser botUser) {
        return null != botUser.getPreviousUserMessage()
                && botUser.getPreviousUserMessage().equalsIgnoreCase(CommandText.TEACHER_SCHEDULE);
    }

    private CompletableFuture<BotMessage> tryToSendTeacherList(String message, BotUser botUser) {
        TeacherListDto teacherListDto = callRestService.getTeachersByWord(message);

        if (null == teacherListDto.getTeachers()) {
            botUserService.resetPreviousUserMessage(botUser);
            return CompletableFuture.completedFuture(new BotMessage(CANNOT_GET_TEACHERS, innerKeyboardGenerator.mainMenu(botUser), botUser));
        }

        if (teacherListDto.getTeachers().isEmpty()) {
            botUserService.resetPreviousUserMessage(botUser);
            return CompletableFuture.completedFuture(new BotMessage(EMPTY_TEACHER_LIST, innerKeyboardGenerator.mainMenu(botUser), botUser));
        }

        List<TeacherDto> teachers = teacherListDto.getTeachers();

        if (teachers.size() > Constants.MAX_VK_KEYBOARD_SIZE_FOR_LISTS) {
            botUserService.resetPreviousUserMessage(botUser);
            return CompletableFuture.completedFuture(new BotMessage(TOO_LONG_TEACHER_LIST, innerKeyboardGenerator.mainMenu(botUser), botUser));
        }

        try {
            String teacherList = makeStringTeacherList(teachers);
            botUserService.updatePreviousUserMessage(CHOOSE_TEACHER_TO_GET_SCHEDULE, botUser);
            return CompletableFuture.completedFuture(new BotMessage(teacherList, innerKeyboardGenerator.teachers(teachers), botUser));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(new BotMessage(CANNOT_GET_TEACHERS, innerKeyboardGenerator.mainMenu(botUser), botUser));
        }
    }
    
    private boolean userChoosesTeacher(BotUser botUser) {
        return null != botUser.getPreviousUserMessage()
                && botUser.getPreviousUserMessage().equals(CHOOSE_TEACHER_TO_GET_SCHEDULE);
    }
    
    private CompletableFuture<BotMessage> tryToSaveUsersTeacherChoice(String message, BotUser botUser) {
        try {
            var teacherId = retrieveTeacherId(message);
            botUserService.updatePreviousUserMessage(TEACHER_PREFIX + " " + teacherId, botUser);
            return CompletableFuture.completedFuture(new BotMessage(
                    CHOOSE_DAY_FOR_TEACHER_SCHEDULE,
                    innerKeyboardGenerator.teacherSchedule(botUser), botUser));
        } catch (StringIndexOutOfBoundsException e) {
            return CompletableFuture.completedFuture(new BotMessage(CANNOT_GET_TEACHERS, innerKeyboardGenerator.mainMenu(botUser), botUser));
        }
    }

    private boolean userWantsTeacherSchedule(String message) {
        return message.equals(CommandText.TEACHER_SCHEDULE);
    }

    private boolean userExitsTeacherSchedule(String message) {
        return message.equals(CommandText.EXIT_TEACHER_SCHEDULE_MODE);
    }

    private String makeStringTeacherList(List<TeacherDto> teachers) {
        StringBuilder sb = new StringBuilder();
        sb.append(CHOOSE_TEACHER_TO_GET_SCHEDULE).append("\n\n");
        for (var teacher : teachers) {
            sb
                    .append(TEACHER_PREFIX).append(" ")
                    .append(teacher.getId()).append(" ")
                    .append(teacher.getSurname()).append(" ")
                    .append(teacher.getName()).append(" ")
                    .append(teacher.getPatronymic()).append("\n");
        }
        return sb.toString();
    }

    private CompletableFuture<BotMessage> getFullTimeSchedule(String message, BotUser botUser) {
        var calendar = CalendarUtils.getCalendar();
        var lessons = new TeacherFullTimeLessonListDto();
        var isToday = false;
        var isTomorrow = false;
        var isYesterday = false;
        var teacherId = botUser.getPreviousUserMessage().split(" ")[1];
        var day = "1";

        switch (message) {
            case CommandText.MONDAY:
                day = "1";
                break;
            case CommandText.TUESDAY:
                day = "2";
                break;
            case CommandText.WEDNESDAY:
                day = "3";
                break;
            case CommandText.THURSDAY:
                day = "4";
                break;
            case CommandText.FRIDAY:
                day = "5";
                break;
            case CommandText.SATURDAY:
                day = "6";
                break;
            case CommandText.TODAY:
                day = String.valueOf(CalendarUtils.getDayOfWeekStartsFromMonday(calendar));
                isToday = true;
                break;
            case CommandText.TOMORROW:
                calendar.add(Calendar.DAY_OF_WEEK, 1);
                day = String.valueOf(CalendarUtils.getDayOfWeekStartsFromMonday(calendar));
                isTomorrow = true;
                break;
            case CommandText.YESTERDAY:
                calendar.add(Calendar.DAY_OF_WEEK, -1);
                day = String.valueOf(CalendarUtils.getDayOfWeekStartsFromMonday(calendar));
                isYesterday = true;
                break;
        }

        lessons = callRestService.getTeacherLessonsByDay(teacherId, day);

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
}
