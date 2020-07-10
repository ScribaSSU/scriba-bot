package com.scribassu.scribabot.services.messages;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scribassu.scribabot.dto.FullTimeLessonDto;
import com.scribassu.scribabot.dto.TeacherListDto;
import com.scribassu.scribabot.dto.vkkeyboard.*;
import com.scribassu.scribabot.entities.BotUser;
import com.scribassu.scribabot.keyboard.KeyboardMap;
import com.scribassu.scribabot.keyboard.KeyboardType;
import com.scribassu.scribabot.repositories.BotUserRepository;
import com.scribassu.scribabot.services.CallRestService;
import com.scribassu.scribabot.services.bot.*;
import com.scribassu.scribabot.text.Command;
import com.scribassu.scribabot.text.CommandText;
import com.scribassu.scribabot.text.MessageText;
import com.scribassu.scribabot.util.BotMessageUtils;
import com.scribassu.scribabot.util.Constants;
import com.scribassu.scribabot.util.DepartmentConverter;
import com.scribassu.scribabot.util.Templates;
import com.scribassu.tracto.domain.EducationForm;
import com.scribassu.tracto.domain.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MessageHandlerImpl implements MessageHandler {

    private final CallRestService callRestService;
    private final HelpService helpService;
    private final FullTimeLessonService fullTimeLessonService;
    private final ExamPeriodService examPeriodService;
    private final BotUserRepository botUserRepository;
    private final SettingsService settingsService;
    private final StudentGroupService studentGroupService;

    @Autowired
    public MessageHandlerImpl(CallRestService callRestService,
                              HelpService helpService,
                              FullTimeLessonService fullTimeLessonService,
                              ExamPeriodService examPeriodService,
                              BotUserRepository botUserRepository,
                              SettingsService settingsService,
                              StudentGroupService studentGroupService) {
        this.callRestService = callRestService;
        this.helpService = helpService;
        this.fullTimeLessonService = fullTimeLessonService;
        this.examPeriodService = examPeriodService;
        this.botUserRepository = botUserRepository;
        this.settingsService = settingsService;
        this.studentGroupService = studentGroupService;
    }

    @Override
    public Map<String, String> getBotMessage(Command command) {
        Map<String, String> botMessage = new HashMap<>();
        String message = command.getMessage().toLowerCase();
        String payload = command.getPayload().toLowerCase();
        String userId = command.getUserId();
        System.out.println("Get message: " + message);

        BotUser botUser = botUserRepository.findOneById(userId);

        if(null != botUser
                && null != botUser.getPreviousUserMessage()
                && botUser.getPreviousUserMessage().equalsIgnoreCase(CommandText.TEACHER_SCHEDULE)) {
            TeacherListDto teacherListDto = callRestService.getTeachersByWord(message);
            if(null != teacherListDto.getTeachers()) {
                List<Teacher> teachers = teacherListDto.getTeachers();
                if(teachers.size() > Constants.MAX_VK_KEYBOARD_SIZE) {
                    botMessage.put(Constants.KEY_MESSAGE,
                            "Искомый список преподавателей слишком большой для клавиатуры VK. " +
                                    "Попробуйте запросить точнее.");
                    botMessage.put(
                            Constants.KEY_KEYBOARD,
                            KeyboardMap.keyboards.get(KeyboardType.ButtonActions).getJsonText());
                }
                else {
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        botMessage.put(Constants.KEY_MESSAGE, "Выберите, для какого преподавателя хотите узнать расписание.");
                        botMessage.put(
                                Constants.KEY_KEYBOARD,
                                objectMapper.writeValueAsString(buildVkKeyboardFromTeachers(teachers)));
                    }
                    catch(Exception e) {
                        botMessage.put(Constants.KEY_MESSAGE, "Не удалось получить список преподавателей.");
                        botMessage.put(
                                Constants.KEY_KEYBOARD,
                                KeyboardMap.keyboards.get(KeyboardType.ButtonActions).getJsonText());
                    }
                }
            }
            else {
                botMessage.put(Constants.KEY_MESSAGE, "Не удалось получить список преподавателей.");
                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonActions).getJsonText());
            }
        }

        switch(message) {
            case CommandText.HELLO:
                if(botUser == null) {
                    botUser = new BotUser(userId);
                    botUser.setFilterNomDenom(false);
                    botUser = botUserRepository.save(botUser);
                    botMessage.put(
                            Constants.KEY_MESSAGE,
                            MessageText.GREETING_WITH_CHOOSE_DEPARTMENT);
                    botMessage.put(
                            Constants.KEY_KEYBOARD,
                            KeyboardMap.keyboards.get(KeyboardType.ButtonDepartment).getJsonText());
                    botUserRepository.updatePreviousUserMessage(MessageText.GREETING_WITH_CHOOSE_DEPARTMENT, botUser.getUserId());
                }
                else {
                    botMessage.put(
                            Constants.KEY_KEYBOARD,
                            KeyboardMap.keyboards.get(KeyboardType.ButtonActions).getJsonText());
                    botMessage.put(
                            Constants.KEY_MESSAGE,
                            "Привет!");
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
                botMessage.put(
                        Constants.KEY_MESSAGE,
                        "Возврат в главное меню.");
                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonActions).getJsonText());
                break;
            case CommandText.HELP:
                botMessage = helpService.getBotMessage(message, botUser);
                break;
            case CommandText.THANKS:
                botMessage.put(
                        Constants.KEY_MESSAGE,
                        "Пожалуйста :)");
                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonActions).getJsonText());
                break;
            case CommandText.TEACHER_SCHEDULE:
                botMessage.put(Constants.KEY_MESSAGE, "Введите полностью или частично что-либо из ФИО преподавателя. " +
                        "Например, по запросу 'Ива' найдутся и 'Иванова', и 'Иван', и 'Иванович'. " +
                        "По запросу 'Иванов Ев' найдется 'Иванов Евгений', но не 'Иванова Евгения'.");
                botUser.setPreviousUserMessage(CommandText.TEACHER_SCHEDULE);
                botUserRepository.save(botUser);
                break;
            case CommandText.FULL_TIME_SCHEDULE:
                if(BotMessageUtils.isBotUserFullTime(botUser)) {
                    botMessage.put(
                            Constants.KEY_MESSAGE,
                            "Выберите, для чего хотите узнать расписание.");
                    botMessage.put(
                            Constants.KEY_KEYBOARD,
                            KeyboardMap.keyboards.get(KeyboardType.ButtonFullTimeSchedule).getJsonText());
                }
                else if(botUser != null
                    && botUser.getDepartment() == null
                    && botUser.getEducationForm() == null
                    && botUser.getGroupNumber() == null) {
                    botMessage.put(Constants.KEY_MESSAGE, MessageText.CHOOSE_DEPARTMENT);
                    botMessage.put(
                            Constants.KEY_KEYBOARD,
                            KeyboardMap.keyboards.get(KeyboardType.ButtonDepartment).getJsonText());
                }
                else {
                    botMessage.put(
                            Constants.KEY_MESSAGE,
                            "Извините, ваша форма обучения пока не поддерживается :(");
                }
                break;
            case CommandText.CHOOSE_DEPARTMENT:
                botMessage.put(Constants.KEY_MESSAGE, MessageText.CHOOSE_DEPARTMENT);
                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonDepartment).getJsonText());
                break;
            case CommandText.FULL_TIME:
                botUserRepository.updateEducationForm(EducationForm.DO.getGroupType(), userId);
                botMessage.put(Constants.KEY_MESSAGE, "Выберите курс. Если учитесь в колледже, выберите 'Другое'.");
                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonCourse).getJsonText());
                break;
            case CommandText.EXTRAMURAL:
                botUserRepository.updateEducationForm(EducationForm.ZO.getGroupType(), userId);
                botMessage.put(Constants.KEY_MESSAGE, "Выберите курс. Если учитесь в колледже, выберите 'Другое'.");
                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonCourse).getJsonText());
                break;
            case CommandText.EVENING:
                botUserRepository.updateEducationForm(EducationForm.VO.getGroupType(), userId);
                botMessage.put(Constants.KEY_MESSAGE, "Выберите курс. Если учитесь в колледже, выберите 'Другое'.");
                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonCourse).getJsonText());
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
                botMessage.put(
                        Constants.KEY_MESSAGE,
                        "Здесь вы можете настроить некоторые функции бота.");
                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonSettings).getJsonText());
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
                botMessage.put(Constants.KEY_MESSAGE, "test");
                break;
        }

        if(CommandText.HOUR_PATTERN.matcher(message).matches()) {
            botMessage = settingsService.getBotMessage(message, botUser);
        }

        if(CommandText.DEPARTMENT_PATTERN.matcher(message).matches()) {
            botUserRepository.updateDepartment(DepartmentConverter.convertToUrl(message), userId);
            botMessage.put(
                    Constants.KEY_MESSAGE,
                    "Выберите форму расписания.");
            botMessage.put(
                    Constants.KEY_KEYBOARD,
                    KeyboardMap.keyboards.get(KeyboardType.ButtonGroupType).getJsonText());
        }

        if(CommandText.COURSE_PAYLOAD.equalsIgnoreCase(payload)) {
            botMessage = studentGroupService.getBotMessage(message, botUser);
        }

        if(CommandText.CHOOSE_STUDENT_GROUP.equalsIgnoreCase(payload)) {
            botUserRepository.updateGroupNumber(message, userId);
            botUser = botUserRepository.findOneById(userId);
            if(botUser.getPreviousUserMessage() != null && botUser.getPreviousUserMessage().equalsIgnoreCase(MessageText.GREETING_WITH_CHOOSE_DEPARTMENT)) {
                botMessage.put(
                        Constants.KEY_MESSAGE,
                        "Это главное меню бота. Отсюда вы можете узнать расписание, задать настройки и не только.");
                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonActions).getJsonText());
                botUserRepository.updatePreviousUserMessage("", botUser.getUserId());
            }
            else {
                botMessage.put(
                        Constants.KEY_MESSAGE,
                        "Хотите расписание пар или сессии?");
                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonFullTimeSchedule).getJsonText());
            }
        }

        if(message.startsWith(CommandText.GROUP_NUMBER_INPUT)) {
            botUserRepository.updateGroupNumber(message.substring(2), userId);
            botUser = botUserRepository.findOneById(userId);
            if(botUser.getPreviousUserMessage().equalsIgnoreCase(MessageText.GREETING_WITH_CHOOSE_DEPARTMENT)) {
                botMessage.put(
                        Constants.KEY_MESSAGE,
                        "Это главное меню бота. Отсюда вы можете узнать расписание, задать настройки и не только.");
                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonActions).getJsonText());
                botUserRepository.updatePreviousUserMessage("", botUser.getUserId());
            }
            else {
                botMessage.put(
                        Constants.KEY_MESSAGE,
                        "Хотите расписание пар или сессии?");
                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonFullTimeSchedule).getJsonText());
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
                botMessage.put(Constants.KEY_MESSAGE, "Информация отсутствует.");
                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonActions).getJsonText());
            }
            else {
                botMessage.put(Constants.KEY_MESSAGE, Templates.makeFullTimeLessonTemplate(lessons, "", botUser.isFilterNomDenom()));
                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonActions).getJsonText());
            }
        }

        if(payload.startsWith(CommandText.TEACHER_ID_PAYLOAD)) {
            botUser.setPreviousUserMessage(payload);
            botUserRepository.save(botUser);
            botMessage.put(
                    Constants.KEY_MESSAGE,
                    "Выберите, для чего хотите узнать расписание преподавателя.");
            botMessage.put(
                    Constants.KEY_KEYBOARD,
                    KeyboardMap.keyboards.get(KeyboardType.ButtonFullTimeSchedule).getJsonText());
        }

        if(botMessage.isEmpty()
                || !botMessage.containsKey(Constants.KEY_MESSAGE)
                && !botMessage.containsKey(Constants.KEY_KEYBOARD)) {
            botMessage.put(Constants.KEY_MESSAGE, "Сообщение не распознано или недостаточно данных :(");
            botMessage.put(
                    Constants.KEY_KEYBOARD,
                    KeyboardMap.keyboards.get(KeyboardType.ButtonActions).getJsonText());
        }

        return botMessage;
    }

    private VkKeyboard buildVkKeyboardFromTeachers(List<Teacher> teachers) {
        List<List<VkKeyboardButton>> vkKeyboardButtons = new ArrayList<>();

        int i = 0;
        int row = 0;
        vkKeyboardButtons.add(new ArrayList<>());

        while(i < teachers.size()) {
            if(i % 5 == 4) {
                row++;
                vkKeyboardButtons.add(new ArrayList<>());
            }
            vkKeyboardButtons.get(row).add(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    teachers.get(i).getSurname() + " " + teachers.get(i).getName() + " " + teachers.get(i).getPatronymic(),
                                    String.format(Constants.PAYLOAD, CommandText.TEACHER_ID_PAYLOAD + " " + teachers.get(i).getId()),
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            );
            i++;
        }

        vkKeyboardButtons.add(new ArrayList<>());
        vkKeyboardButtons.get(vkKeyboardButtons.size() - 1).add(
                new VkKeyboardButton(
                        new VkKeyboardButtonActionText(
                                "Главное меню",
                                "",
                                VkKeyboardButtonActionType.TEXT
                        ), VkKeyboardButtonColor.POSITIVE)
        );

        return new VkKeyboard(vkKeyboardButtons, true);
    }

}
