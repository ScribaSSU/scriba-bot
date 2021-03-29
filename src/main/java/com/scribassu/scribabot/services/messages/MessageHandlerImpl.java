package com.scribassu.scribabot.services.messages;

import com.scribassu.scribabot.dto.BotMessage;
import com.scribassu.scribabot.dto.Command;
import com.scribassu.scribabot.dto.rest.FullTimeLessonDto;
import com.scribassu.scribabot.entities.BotUser;
import com.scribassu.scribabot.entities.UnrecognizedMessage;
import com.scribassu.scribabot.keyboard.KeyboardFormatter;
import com.scribassu.scribabot.repositories.BotUserRepository;
import com.scribassu.scribabot.repositories.UnrecognizedMessageRepository;
import com.scribassu.scribabot.services.CallRestService;
import com.scribassu.scribabot.services.bot.*;
import com.scribassu.scribabot.text.CommandText;
import com.scribassu.scribabot.text.MessageText;
import com.scribassu.scribabot.util.BotMessageUtils;
import com.scribassu.scribabot.util.Constants;
import com.scribassu.scribabot.util.DepartmentConverter;
import com.scribassu.tracto.domain.EducationForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.scribassu.scribabot.keyboard.KeyboardType.*;
import static com.scribassu.scribabot.text.MessageText.*;

@Service
public class MessageHandlerImpl implements MessageHandler {

    private final CallRestService callRestService;
    private final HelpService helpService;
    private final FullTimeLessonService fullTimeLessonService;
    private final ExamPeriodService examPeriodService;
    private final BotUserRepository botUserRepository;
    private final SettingsService settingsService;
    private final StudentGroupService studentGroupService;
    private final TeacherService teacherService;
    private final UnrecognizedMessageRepository unrecognizedMessageRepository;
    private final KeyboardFormatter keyboardFormatter;

    @Value("#{'${scriba-bot.mentioned-names}'.split(',')}")
    private List<String> mentionedNames;

    @Autowired
    public MessageHandlerImpl(CallRestService callRestService,
                              HelpService helpService,
                              FullTimeLessonService fullTimeLessonService,
                              ExamPeriodService examPeriodService,
                              BotUserRepository botUserRepository,
                              SettingsService settingsService,
                              StudentGroupService studentGroupService,
                              TeacherService teacherService,
                              UnrecognizedMessageRepository unrecognizedMessageRepository,
                              KeyboardFormatter keyboardFormatter) {
        this.callRestService = callRestService;
        this.helpService = helpService;
        this.fullTimeLessonService = fullTimeLessonService;
        this.examPeriodService = examPeriodService;
        this.botUserRepository = botUserRepository;
        this.settingsService = settingsService;
        this.studentGroupService = studentGroupService;
        this.teacherService = teacherService;
        this.unrecognizedMessageRepository = unrecognizedMessageRepository;
        this.keyboardFormatter = keyboardFormatter;
    }

    @Override
    public BotMessage getBotMessage(Command command) {
        BotMessage botMessage = new BotMessage(MessageText.DEFAULT_MESSAGE, ButtonActions);
        String message = command.getMessage(); // NO LOWER CASE HERE!
        String payload = command.getPayload().toLowerCase();
        String userId = command.getUserId();
        BotUser botUser = botUserRepository.findOneById(userId);
        boolean isMentioned = false;

        for(String mentionedName : mentionedNames) {
            if(message.contains(mentionedName)) {
                // 1 for ] char
                message = message.substring(message.indexOf(mentionedName) + mentionedName.length() + 1);
                isMentioned = true;
                break;
            }
        }
        message = message.trim().toLowerCase();

        long userIdLong = Long.parseLong(userId);

        // It is mandatory to mention bot name in chats
        if(!isMentioned && userIdLong > Constants.PEER_ID_SHIFT) {
            return new BotMessage(DO_NOT_SEND);
        }

        if(null == botUser && !message.equals(CommandText.HELLO)
                && !message.equals(CommandText.MAIN_MENU)
                && !message.equals(CommandText.SHORT_MAIN_MENU)) {
            botMessage = new BotMessage("Для начала работы с ботом напишите 'Привет', чтобы настроить факультет и группу.");
            botUser = new BotUser();
            botUser.setUserId(userId); //DON'T SAVE! It is only for unrecognized messages check
            return botMessage;
        }

        if(null != botUser
                && null != botUser.getPreviousUserMessage()
                && botUser.getPreviousUserMessage().equalsIgnoreCase(CommandText.TEACHER_SCHEDULE)) {
            botMessage = teacherService.getBotMessage(message, botUser);
        }

        switch(message) {
            case CommandText.HELLO:
                if(botUser == null) {
                    botUser = new BotUser(userId);
                    botUser.setFilterNomDenom(false);
                    botUser.setFilterLessonNotif(true);
                    botUser = botUserRepository.save(botUser);
                    botMessage = new BotMessage(GREETING_WITH_CHOOSE_DEPARTMENT, ButtonDepartment);
                    botUserRepository.updatePreviousUserMessage(GREETING_WITH_CHOOSE_DEPARTMENT, botUser.getUserId());
                }
                else {
                    botMessage = new BotMessage("Привет!", ButtonActions);
                }
                break;
            case CommandText.MAIN_MENU:
            case CommandText.SHORT_MAIN_MENU:
                if(botUser == null) {
                    botUser = new BotUser(userId);
                    botUser.setFilterNomDenom(false);
                    botUser = botUserRepository.save(botUser);
                    botMessage = new BotMessage(GREETING_WITH_CHOOSE_DEPARTMENT, ButtonDepartment);
                    botUserRepository.updatePreviousUserMessage(GREETING_WITH_CHOOSE_DEPARTMENT, botUser.getUserId());
                }
                else {
                    botUser.setPreviousUserMessage("");
                    botUserRepository.save(botUser);
                    botMessage = new BotMessage("Возврат в главное меню.", ButtonActions);
                }
                break;
            case CommandText.HELP:
                botMessage = helpService.getBotMessage(message, botUser);
                break;
            case CommandText.THANKS:
                botMessage = new BotMessage("Пожалуйста :)", ButtonActions);
                break;
            case CommandText.TEACHER_SCHEDULE:
                botMessage = teacherService.getBotMessage(message, botUser);
                break;
            case CommandText.FULL_TIME_SCHEDULE:
                if(BotMessageUtils.isBotUserFullTime(botUser)) {
                    botMessage = new BotMessage("Выберите, для чего хотите узнать расписание.", ButtonFullTimeSchedule);
                }
                else if(botUser != null
                    && botUser.getDepartment() == null
                    && botUser.getEducationForm() == null
                    && botUser.getGroupNumber() == null) {
                    botMessage = new BotMessage(CHOOSE_DEPARTMENT, ButtonDepartment);
                }
                else {
                    botMessage = new BotMessage(MessageText.CANNOT_GET_SCHEDULE_GROUP_NOT_SET, ButtonDepartment);
                }
                break;
            case CommandText.CHOOSE_DEPARTMENT:
                botMessage = new BotMessage(CHOOSE_DEPARTMENT, ButtonDepartment);
                break;
            case CommandText.FULL_TIME:
                botUserRepository.updateEducationForm(EducationForm.DO.getGroupType(), userId);
                botMessage = new BotMessage(CHOOSE_COURSE, ButtonCourse);
                break;
            case CommandText.EXTRAMURAL:
                botUserRepository.updateEducationForm(EducationForm.ZO.getGroupType(), userId);
                botMessage = new BotMessage(CHOOSE_COURSE, ButtonCourse);
                break;
            case CommandText.EVENING:
                botUserRepository.updateEducationForm(EducationForm.VO.getGroupType(), userId);
                botMessage = new BotMessage(CHOOSE_COURSE, ButtonCourse);
                break;
            case CommandText.MONDAY:
            case CommandText.TUESDAY:
            case CommandText.WEDNESDAY:
            case CommandText.THURSDAY:
            case CommandText.FRIDAY:
            case CommandText.SATURDAY:
            case CommandText.TODAY:
            case CommandText.TOMORROW:
            case CommandText.YESTERDAY:
                botMessage = fullTimeLessonService.getBotMessage(message, botUser);
                break;
            case CommandText.SETTINGS:
                botMessage = new BotMessage("Здесь вы можете настроить бота под себя.", ButtonSettings);
                botMessage = keyboardFormatter.formatSettings(botMessage, botUser);
                break;
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
            case CommandText.CURRENT_USER_SETTINGS:
            case CommandText.DELETE_PROFILE:
            case CommandText.YES:
            case CommandText.NO:
                botMessage = settingsService.getBotMessage(message, botUser);
                break;
            case CommandText.EXAMS:
                botMessage = examPeriodService.getBotMessage(message, botUser);
                break;
            case "т":
                botMessage = new BotMessage("test");
                break;
        }

        if(CommandText.HOUR_PATTERN.matcher(message).matches()) {
            botMessage = settingsService.getBotMessage(message, botUser);
        }

        if(CommandText.DEPARTMENT_PAYLOAD.equalsIgnoreCase(payload)
            || CommandText.DEPARTMENT_PATTERN.matcher(message).matches()) {
            botUserRepository.updateDepartment(DepartmentConverter.convertToUrl(message), userId);
            botMessage = new BotMessage(MessageText.CHOOSE_EDUCATION_FORM, ButtonGroupType);
        }

        if(CommandText.COURSE_PAYLOAD.equalsIgnoreCase(payload)) {
            botMessage = studentGroupService.getBotMessage(message, botUser);
        }

        if(CommandText.CHOOSE_STUDENT_GROUP.equalsIgnoreCase(payload)) {
            botUserRepository.updateGroupNumber(message, userId);
            botUser = botUserRepository.findOneById(userId);
            if(null != botUser.getPreviousUserMessage() && botUser.getPreviousUserMessage().equalsIgnoreCase(GREETING_WITH_CHOOSE_DEPARTMENT)) {
                botMessage = new BotMessage(THIS_IS_MAIN_MENU, ButtonActions);
                botUserRepository.updatePreviousUserMessage("", botUser.getUserId());
            }
            else {
                botMessage = new BotMessage(MessageText.FINISH_SET_GROUP, ButtonFullTimeSchedule);
            }
        }

        if(message.startsWith(CommandText.GROUP_NUMBER_INPUT)) {
            botUserRepository.updateGroupNumber(message.substring(2), userId);
            botUser = botUserRepository.findOneById(userId);
            if(botUser.getPreviousUserMessage().equalsIgnoreCase(GREETING_WITH_CHOOSE_DEPARTMENT)) {
                botMessage = new BotMessage(THIS_IS_MAIN_MENU, ButtonActions);
                botUserRepository.updatePreviousUserMessage("", botUser.getUserId());
            }
            else {
                botMessage = new BotMessage(MessageText.FINISH_SET_GROUP, ButtonFullTimeSchedule);
            }
        }

        if(message.startsWith("р ")) {
            String[] params = message.split(" ");
            FullTimeLessonDto lessons = callRestService.getFullTimeLessonsByDay(params[1], params[2], params[3]);
            botMessage = BotMessageUtils.getBotMessageForFullTimeLessons(lessons, "", botUser.isFilterNomDenom());
        }

        if(payload.startsWith(CommandText.TEACHER_ID_PAYLOAD)) {
            botMessage = teacherService.getBotMessage(payload, botUser);
        }

        if(CommandText.COURSE_PATTERN.matcher(message).matches()) {
            botMessage = studentGroupService.getBotMessage(message, botUser);
        }

        if(botMessage.isDefault()) {
            unrecognizedMessageRepository.save(new UnrecognizedMessage(command, botUser));
        }

        return botMessage;
    }
}
