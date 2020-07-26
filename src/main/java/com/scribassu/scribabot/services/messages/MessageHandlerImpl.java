package com.scribassu.scribabot.services.messages;

import com.scribassu.scribabot.dto.BotMessage;
import com.scribassu.scribabot.dto.rest.FullTimeLessonDto;
import com.scribassu.scribabot.entities.BotUser;
import com.scribassu.scribabot.repositories.BotUserRepository;
import com.scribassu.scribabot.services.CallRestService;
import com.scribassu.scribabot.services.bot.*;
import com.scribassu.scribabot.text.Command;
import com.scribassu.scribabot.text.CommandText;
import com.scribassu.scribabot.util.BotMessageUtils;
import com.scribassu.scribabot.util.DepartmentConverter;
import com.scribassu.scribabot.util.Templates;
import com.scribassu.tracto.domain.EducationForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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

    @Autowired
    public MessageHandlerImpl(CallRestService callRestService,
                              HelpService helpService,
                              FullTimeLessonService fullTimeLessonService,
                              ExamPeriodService examPeriodService,
                              BotUserRepository botUserRepository,
                              SettingsService settingsService,
                              StudentGroupService studentGroupService,
                              TeacherService teacherService) {
        this.callRestService = callRestService;
        this.helpService = helpService;
        this.fullTimeLessonService = fullTimeLessonService;
        this.examPeriodService = examPeriodService;
        this.botUserRepository = botUserRepository;
        this.settingsService = settingsService;
        this.studentGroupService = studentGroupService;
        this.teacherService = teacherService;
    }

    @Override
    public BotMessage getBotMessage(Command command) {
        BotMessage botMessage = new BotMessage();
        String message = command.getMessage().toLowerCase();
        String payload = command.getPayload().toLowerCase();
        String userId = command.getUserId();
        System.out.println("Get message: " + message);

        BotUser botUser = botUserRepository.findOneById(userId);

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
                }
                else {
                    botUser.setPreviousUserMessage("");
                    botUserRepository.save(botUser);
                }
                botMessage = new BotMessage("Возврат в главное меню.", ButtonActions);
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
                    botMessage = new BotMessage("Извините, ваша форма обучения пока не поддерживается :(", ButtonActions);
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
                botMessage = new BotMessage("Здесь вы можете настроить некоторые функции бота.", ButtonSettings);
                break;
            case CommandText.SET_SEND_SCHEDULE_TIME_TODAY:
            case CommandText.ENABLE_SEND_SCHEDULE_TODAY:
            case CommandText.DISABLE_SEND_SCHEDULE_TODAY:
            case CommandText.SET_SEND_SCHEDULE_TIME_TOMORROW:
            case CommandText.ENABLE_SEND_SCHEDULE_TOMORROW:
            case CommandText.DISABLE_SEND_SCHEDULE_TOMORROW:
            case CommandText.ENABLE_FILTER_WEEK_TYPE:
            case CommandText.DISABLE_FILTER_WEEK_TYPE:
            case CommandText.CURRENT_USER_SETTINGS:
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

        if(CommandText.DEPARTMENT_PAYLOAD.equalsIgnoreCase(payload)) {
            botUserRepository.updateDepartment(DepartmentConverter.convertToUrl(message), userId);
            botMessage = new BotMessage("Выберите форму расписания.", ButtonGroupType);
        }

        if(CommandText.COURSE_PAYLOAD.equalsIgnoreCase(payload)) {
            botMessage = studentGroupService.getBotMessage(message, botUser);
        }

        if(CommandText.CHOOSE_STUDENT_GROUP.equalsIgnoreCase(payload)) {
            botUserRepository.updateGroupNumber(message, userId);
            botUser = botUserRepository.findOneById(userId);
            if(botUser.getPreviousUserMessage() != null && botUser.getPreviousUserMessage().equalsIgnoreCase(GREETING_WITH_CHOOSE_DEPARTMENT)) {
                botMessage = new BotMessage(THIS_IS_MAIN_MENU, ButtonActions);
                botUserRepository.updatePreviousUserMessage("", botUser.getUserId());
            }
            else {
                botMessage = new BotMessage("Хотите расписание пар или сессии?", ButtonFullTimeSchedule);
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
                botMessage = new BotMessage("Хотите расписание пар или сессии?", ButtonFullTimeSchedule);
            }
        }

        if(message.startsWith("р ")) {
            String[] params = message.split(" ");
            FullTimeLessonDto lessons = callRestService.getFullTimeLessonsByDay(
                    params[1],
                    params[2],
                    params[3]
            );

            if(CollectionUtils.isEmpty(lessons.getLessons())) {
                botMessage = new BotMessage("Информация отсутствует.", ButtonActions);
            }
            else {
                botMessage = new BotMessage(
                        Templates.makeFullTimeLessonTemplate(lessons, "", botUser.isFilterNomDenom()),
                        ButtonActions);
            }
        }

        if(payload.startsWith(CommandText.TEACHER_ID_PAYLOAD)) {
            botMessage = teacherService.getBotMessage(payload, botUser);
        }

        if(botMessage.isEmpty()) {
            botMessage = new BotMessage("Сообщение не распознано или недостаточно данных :(", ButtonActions);
        }

        return botMessage;
    }
}
