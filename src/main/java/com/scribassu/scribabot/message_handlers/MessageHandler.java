package com.scribassu.scribabot.message_handlers;

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
import com.scribassu.tracto.dto.EducationForm;
import com.scribassu.tracto.dto.FullTimeLessonListDto;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import static com.scribassu.scribabot.text.CommandText.HELLO;
import static com.scribassu.scribabot.text.MessageText.*;

// 434
// 367
// 260
// 222
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
        String userId = command.getUserId();
        BotUserSource botUserSource = command.getBotUserSource();
        RegisteredUserResult registeredUserResult = botUserService.isBotUserRegistered(command);
        BotUser botUser = registeredUserResult.getBotUser();

        var botMessage = new BotMessage(
                MessageText.DEFAULT_MESSAGE,
                innerKeyboardGenerator.mainMenu(botUser), botUser);

        boolean isMentioned = false;

        // vk only
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

        // todo to remove?
//        if (!registered && !message.equals(CommandText.HELLO)
//                && !message.equals(CommandText.MAIN_MENU)
//                && !message.equals(CommandText.SHORT_MAIN_MENU)
//                && !message.equals(CommandText.TG_START)) {
//            botMessage = new BotMessage(GREETING_WITH_CHOOSE_DEPARTMENT, botUser);
//            botUser.setUserId(userId); //DON'T SAVE! It is only for unrecognized messages check
//            return CompletableFuture.completedFuture(botMessage);
//        }
        if (!registeredUserResult.isRegistered()) {
            message = HELLO;
            command = new Command(message, userId, botUserSource);
        }

        if (greetingsService.shouldAccept(message, botUser)) {
            return greetingsService.getBotMessage(command);
        }

        if (settingsService.shouldAccept(message, botUser)) {
            return settingsService.getBotMessage(message, botUser);
        }

        if (helpService.shouldAccept(message, botUser)) {
            return helpService.getBotMessage(message, botUser);
        }

        if (teacherService.shouldAccept(message, botUser)) {
            return teacherService.getBotMessage(message, botUser);
        }

        if (studentGroupService.shouldAccept(message, botUser)) {
            return studentGroupService.getBotMessage(message, botUser);
        }

        if (fullTimeLessonService.shouldAccept(message, botUser)) {
            return fullTimeLessonService.getBotMessage(message, botUser);
        }

        if (extramuralEventService.shouldAccept(message, botUser)) {
            return extramuralEventService.getBotMessage(message, botUser);
        }

        switch (message) {
            case CommandText.STUDENTS_SCHEDULE:
                if (BotUser.isBotUserFullTime(botUser)) {
                    return CompletableFuture.completedFuture(new BotMessage(CHOOSE_WANTED_SCHEDULE, innerKeyboardGenerator.fullTimeSchedule(botUser), botUser));
                } else if (BotUser.isBotUserExtramural(botUser)) {
                    return CompletableFuture.completedFuture(new BotMessage(CHOOSE_WANTED_SCHEDULE, innerKeyboardGenerator.extramuralSchedule(botUser), botUser));
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
            case CommandText.SETTINGS:
                return CompletableFuture.completedFuture(new BotMessage(SETTINGS_MENU, innerKeyboardGenerator.settings(botUser), botUser));
            case CommandText.EXAMS:
                return examPeriodService.getBotMessage(message, botUser);
        }


        if (CommandText.DEPARTMENT_PATTERN.matcher(message).matches()) {
            botUserService.updateDepartment(message, botUser);
            return CompletableFuture.completedFuture(new BotMessage(MessageText.CHOOSE_EDUCATION_FORM, innerKeyboardGenerator.educationForms(), botUser));
        }

        if (message.startsWith("р ")) {
            String[] params = message.split(" ");
            FullTimeLessonListDto lessons = callRestService.getFullTimeLessonsByDay(params[1], params[2], params[3]);
            return CompletableFuture.completedFuture(botMessageGenerator.getBotMessageForFullTimeLessons(lessons, "", botUser));
        }

        if (botMessage.isDefault()) {
            unrecognizedMessageRepository.save(new UnrecognizedMessage(command, userId, botUserSource));
        }

        botMessage.setBotUser(botUser);
        return CompletableFuture.completedFuture(botMessage);
    }
}
