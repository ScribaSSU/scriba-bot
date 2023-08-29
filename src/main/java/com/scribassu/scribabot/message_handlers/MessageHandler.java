package com.scribassu.scribabot.message_handlers;

import com.scribassu.scribabot.dto.rest.FullTimeLessonDto;
import com.scribassu.scribabot.entities.unrecognized_messages.UnrecognizedMessage;
import com.scribassu.scribabot.generators.BotMessageGenerator;
import com.scribassu.scribabot.generators.InnerKeyboardGenerator;
import com.scribassu.scribabot.model.*;
import com.scribassu.scribabot.repositories.UnrecognizedMessageRepository;
import com.scribassu.scribabot.services.BotUserService;
import com.scribassu.scribabot.services.CallRestService;
import com.scribassu.scribabot.services.bot_message.*;
import com.scribassu.scribabot.text.CommandText;
import com.scribassu.scribabot.text.MessageText;
import com.scribassu.scribabot.util.Constants;
import com.scribassu.tracto.domain.EducationForm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import static com.scribassu.scribabot.text.MessageText.*;

// 434
// 367
// 260
@Service
@Slf4j
@Data
public class MessageHandler {

    private final CallRestService callRestService;
    private final HelpService helpService;
    private final GreetingsService greetingsService;
    private final FullTimeLessonService fullTimeLessonService;
    private final ExamPeriodService examPeriodService;
    private final ExtramuralEventService extramuralEventService;
    private final SettingsService settingsService;
    private final StudentGroupService studentGroupService;
    private final TeacherService teacherService;

    private final BotUserService botUserService;
    private final UnrecognizedMessageRepository unrecognizedMessageRepository;
    private final InnerKeyboardGenerator innerKeyboardGenerator;
    private final BotMessageGenerator botMessageGenerator;

    @Value("#{'${scriba-bot.vk.mentioned-names}'.split(',')}")
    private List<String> mentionedNames;
    
    @Async
    public CompletableFuture<BotMessage> getBotMessage(Command command) {
        String message = command.getMessage().trim();
        String payload = command.getPayload().toLowerCase();
        String userId = command.getUserId();
        BotUserSource botUserSource = command.getBotUserSource();
        RegisteredUserResult registeredUserResult = botUserService.isBotUserRegistered(command);
        BotUser botUser = registeredUserResult.getBotUser();
        boolean registered = registeredUserResult.isRegistered();

        var botMessage = new BotMessage(
                MessageText.DEFAULT_MESSAGE,
                innerKeyboardGenerator.mainMenu(), botUser);

        boolean isMentioned = false;

        for (String mentionedName : mentionedNames) {
            if (message.contains(mentionedName.toLowerCase(Locale.ROOT))) {
                // 1 for ] char
                message = message.substring(message.indexOf(mentionedName) + mentionedName.length() + 1);
                isMentioned = true;
                break;
            }
        }

        long userIdLong = Long.parseLong(userId);

        // It is mandatory to mention bot name in chats
        if (!isMentioned && userIdLong > Constants.PEER_ID_SHIFT) {
            return CompletableFuture.completedFuture(new BotMessage(DO_NOT_SEND, botUser));
        }

        if (!registered && !message.equals(CommandText.HELLO)
                && !message.equals(CommandText.MAIN_MENU)
                && !message.equals(CommandText.SHORT_MAIN_MENU)
                && !message.equals(CommandText.TG_START)) {
            botMessage = new BotMessage(GREETING_WITH_CHOOSE_DEPARTMENT, botUser);
            botUser.setUserId(userId); //DON'T SAVE! It is only for unrecognized messages check
            return CompletableFuture.completedFuture(botMessage);
        }

        if (registered
                && null != botUser.getPreviousUserMessage()
                && (botUser.getPreviousUserMessage().equalsIgnoreCase(CommandText.TEACHER_SCHEDULE)
                || botUser.getPreviousUserMessage().startsWith(Constants.TEACHER_ID)
                || botUser.getPreviousUserMessage().equalsIgnoreCase(CHOOSE_TEACHER_TO_GET_SCHEDULE))) {
            return teacherService.getBotMessage(message, botUser);
        }

        switch (message) {
            case CommandText.STICKER_WAS_SENT_TO_BOT:
            case CommandText.TG_START:
            case CommandText.HELLO:
            case CommandText.MAIN_MENU:
            case CommandText.SHORT_MAIN_MENU:
            case CommandText.THANKS:
                return greetingsService.getBotMessage(command);
            case CommandText.HELP:
                return helpService.getBotMessage(message, botUser);
            case CommandText.TEACHER_SCHEDULE:
                return teacherService.getBotMessage(message, botUser);
            case CommandText.STUDENTS_SCHEDULE:
                if (BotUser.isBotUserFullTime(botUser)) {
                    return CompletableFuture.completedFuture(new BotMessage(CHOOSE_WANTED_SCHEDULE, innerKeyboardGenerator.fullTimeSchedule(), botUser));
                } else if (BotUser.isBotUserExtramural(botUser)) {
                    return CompletableFuture.completedFuture(new BotMessage(CHOOSE_WANTED_SCHEDULE, innerKeyboardGenerator.extramuralSchedule(), botUser));
                } else {
                    return CompletableFuture.completedFuture(new BotMessage(CANNOT_GET_SCHEDULE_GROUP_NOT_SET, innerKeyboardGenerator.departments(), botUser));
                }
            case CommandText.CHOOSE_DEPARTMENT:
                return CompletableFuture.completedFuture(new BotMessage(CHOOSE_DEPARTMENT, innerKeyboardGenerator.departments(), botUser));
            case CommandText.FULL_TIME:
                botUserService.updateEducationForm(EducationForm.DO, botUser);
                return CompletableFuture.completedFuture(new BotMessage(CHOOSE_COURSE, innerKeyboardGenerator.courses(), botUser));
            case CommandText.EXTRAMURAL:
                botUserService.updateEducationForm(EducationForm.ZO, botUser);
                return CompletableFuture.completedFuture(new BotMessage(CHOOSE_COURSE, innerKeyboardGenerator.courses(), botUser));
            case CommandText.EVENING:
                botUserService.updateEducationForm(EducationForm.VO, botUser);
                return CompletableFuture.completedFuture(new BotMessage(CHOOSE_COURSE, innerKeyboardGenerator.courses(), botUser));
            case CommandText.MONDAY:
            case CommandText.TUESDAY:
            case CommandText.WEDNESDAY:
            case CommandText.THURSDAY:
            case CommandText.FRIDAY:
            case CommandText.SATURDAY:
                return fullTimeLessonService.getBotMessage(message, botUser);
            case CommandText.TODAY:
            case CommandText.TOMORROW:
            case CommandText.YESTERDAY:
                if (BotUser.isBotUserFullTime(botUser)) {
                    return fullTimeLessonService.getBotMessage(message, botUser);
                } else {
                    return extramuralEventService.getBotMessage(message, botUser);
                }
            case CommandText.ALL_LESSONS:
                if (BotUser.isBotUserExtramural(botUser)) {
                    return extramuralEventService.getBotMessage(message, botUser);
                } else {
                    return fullTimeLessonService.getBotMessage(message, botUser);
                }
            case CommandText.TEACHER_SCHEDULE_FOR_EXTRAMURAL:
                if (botUser.wantTeacherSchedule()) {
                    return teacherService.getBotMessage(message, botUser);
                } else {
                    return CompletableFuture.completedFuture(new BotMessage(RETURN_MAIN_MENU, innerKeyboardGenerator.mainMenu(), botUser));
                }
            case CommandText.SETTINGS:
                return CompletableFuture.completedFuture(new BotMessage(SETTINGS_MENU, innerKeyboardGenerator.settings(botUser), botUser));
            case CommandText.EXAMS:
                return examPeriodService.getBotMessage(message, botUser);
        }

        if (isSettingsCommand(command)) {
            return settingsService.getBotMessage(message, botUser);
        }

        // todo здесь тестируем выход из режима учителя
        if (CommandText.DEPARTMENT_PAYLOAD.equalsIgnoreCase(payload)
                || CommandText.DEPARTMENT_PATTERN.matcher(message).matches()) {
            botUserService.updateDepartment(message, botUser);
            return CompletableFuture.completedFuture(new BotMessage(MessageText.CHOOSE_EDUCATION_FORM, innerKeyboardGenerator.educationForms(), botUser));
        }

        if (isStudentGroupCommand(command)) {
            return studentGroupService.getBotMessage(message, botUser);
        }

        if (CommandText.CHOOSE_STUDENT_GROUP.equalsIgnoreCase(payload)) {
            botUserService.updateGroupNumber(message, botUser);
            if (null != botUser.getPreviousUserMessage()
                    && botUser.getPreviousUserMessage().equalsIgnoreCase(GREETING_WITH_CHOOSE_DEPARTMENT)) {
                botUserService.resetPreviousUserMessage(botUser);
                return CompletableFuture.completedFuture(new BotMessage(THIS_IS_MAIN_MENU, innerKeyboardGenerator.mainMenu(), botUser));

            } else {
                if (BotUser.isBotUserFullTime(botUser)) {
                    return CompletableFuture.completedFuture(new BotMessage(MessageText.FINISH_SET_GROUP, innerKeyboardGenerator.fullTimeSchedule(), botUser));
                }
                if (BotUser.isBotUserExtramural(botUser)) {
                    return CompletableFuture.completedFuture(new BotMessage(FINISH_SET_GROUP, innerKeyboardGenerator.extramuralSchedule(), botUser));
                }
            }
        }

        if (message.startsWith(CommandText.GROUP_NUMBER_INPUT)) {
            botUserService.updateGroupNumber(message.substring(2), botUser);

            if (null != botUser.getPreviousUserMessage()
                    && botUser.getPreviousUserMessage().equalsIgnoreCase(GREETING_WITH_CHOOSE_DEPARTMENT)) {
                botUserService.resetPreviousUserMessage(botUser);
                return CompletableFuture.completedFuture(new BotMessage(THIS_IS_MAIN_MENU, innerKeyboardGenerator.mainMenu(), botUser));
            } else {
                return CompletableFuture.completedFuture(new BotMessage(MessageText.FINISH_SET_GROUP, innerKeyboardGenerator.fullTimeSchedule(), botUser));
            }
        }

        if (message.startsWith("р ")) {
            String[] params = message.split(" ");
            FullTimeLessonDto lessons = callRestService.getFullTimeLessonsByDay(params[1], params[2], params[3]);
            return CompletableFuture.completedFuture(botMessageGenerator.getBotMessageForFullTimeLessons(lessons, "", botUser));
        }

        if (payload.startsWith(CommandText.TEACHER_ID_PAYLOAD)) {
            return teacherService.getBotMessage(payload, botUser);
        }

        if (botMessage.isDefault()) {
            unrecognizedMessageRepository.save(new UnrecognizedMessage(command, userId, botUserSource));
        }

        botMessage.setBotUser(botUser);
        return CompletableFuture.completedFuture(botMessage);
    }

    private boolean isStudentGroupCommand(Command command) {
        return CommandText.COURSE_PAYLOAD.equalsIgnoreCase(command.getPayload())
                || CommandText.COURSE_PATTERN.matcher(command.getMessage()).matches();
    }

    private boolean isSettingsCommand(Command command) {
        var message = command.getMessage();

        if (CommandText.HOUR_PATTERN.matcher(message).matches()) {
            return true;
        }

        switch (message) {
            case CommandText.SET_SEND_SCHEDULE_TIME_TODAY:
            case CommandText.ENABLE_SEND_SCHEDULE_TODAY:
            case CommandText.DISABLE_SEND_SCHEDULE_TODAY:
            case CommandText.SET_SEND_SCHEDULE_TIME_TOMORROW:
            case CommandText.ENABLE_SEND_SCHEDULE_TOMORROW:
            case CommandText.DISABLE_SEND_SCHEDULE_TOMORROW:
            case CommandText.SET_SEND_EXAM_PERIOD_TIME_TODAY:
            case CommandText.ENABLE_SEND_EXAM_PERIOD_TODAY:
            case CommandText.DISABLE_SEND_EXAM_PERIOD_TODAY:
            case CommandText.SET_SEND_EXAM_PERIOD_TIME_TOMORROW:
            case CommandText.ENABLE_SEND_EXAM_PERIOD_TOMORROW:
            case CommandText.DISABLE_SEND_EXAM_PERIOD_TOMORROW:
            case CommandText.SET_SEND_EXAM_PERIOD_TIME_AFTER_TOMORROW:
            case CommandText.ENABLE_SEND_EXAM_PERIOD_AFTER_TOMORROW:
            case CommandText.DISABLE_SEND_EXAM_PERIOD_AFTER_TOMORROW:
            case CommandText.SEND_EXAM_PERIOD:
            case CommandText.SEND_SCHEDULE:
            case CommandText.ENABLE_FILTER_WEEK_TYPE:
            case CommandText.DISABLE_FILTER_WEEK_TYPE:
            case CommandText.ENABLE_SEND_EMPTY_SCHEDULE_NOTIFICATION:
            case CommandText.DISABLE_SEND_EMPTY_SCHEDULE_NOTIFICATION:
            case CommandText.CURRENT_USER_SETTINGS:
            case CommandText.ENABLE_SEND_KEYBOARD:
            case CommandText.DISABLE_SEND_KEYBOARD:
            case CommandText.DELETE_PROFILE:
            case CommandText.YES:
            case CommandText.NO:
                return true;
            default: return false;
        }
    }
}
