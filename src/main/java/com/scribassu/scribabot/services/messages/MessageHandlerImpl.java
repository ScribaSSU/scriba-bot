package com.scribassu.scribabot.services.messages;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scribassu.scribabot.dto.FullTimeLessonDto;
import com.scribassu.scribabot.dto.GroupNumbersDto;
import com.scribassu.scribabot.dto.vkkeyboard.*;
import com.scribassu.scribabot.text.Command;
import com.scribassu.scribabot.text.CommandText;
import com.scribassu.scribabot.entities.BotUser;
import com.scribassu.scribabot.entities.ScheduleDailyNotification;
import com.scribassu.scribabot.keyboard.KeyboardMap;
import com.scribassu.scribabot.keyboard.KeyboardType;
import com.scribassu.scribabot.repositories.BotUserRepository;
import com.scribassu.scribabot.repositories.ScheduleDailyNotificationRepository;
import com.scribassu.scribabot.services.CallRestService;
import com.scribassu.scribabot.services.bot.FullTimeLessonService;
import com.scribassu.scribabot.services.bot.HelpService;
import com.scribassu.scribabot.services.bot.SettingsService;
import com.scribassu.scribabot.text.MessageText;
import com.scribassu.scribabot.util.BotMessageUtils;
import com.scribassu.scribabot.util.Constants;
import com.scribassu.scribabot.util.DepartmentConverter;
import com.scribassu.scribabot.util.Templates;
import com.scribassu.tracto.domain.EducationForm;
import com.scribassu.tracto.domain.FullTimeLesson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MessageHandlerImpl implements MessageHandler {

    private final CallRestService callRestService;
    private final HelpService helpService;
    private final FullTimeLessonService fullTimeLessonService;
    private final BotUserRepository botUserRepository;
    private final ScheduleDailyNotificationRepository scheduleDailyNotificationRepository;
    private final SettingsService settingsService;

    @Autowired
    public MessageHandlerImpl(CallRestService callRestService,
                              HelpService helpService,
                              FullTimeLessonService fullTimeLessonService,
                              BotUserRepository botUserRepository,
                              ScheduleDailyNotificationRepository scheduleDailyNotificationRepository,
                              SettingsService settingsService) {
        this.callRestService = callRestService;
        this.helpService = helpService;
        this.fullTimeLessonService = fullTimeLessonService;
        this.botUserRepository = botUserRepository;
        this.scheduleDailyNotificationRepository = scheduleDailyNotificationRepository;
        this.settingsService = settingsService;
    }

    @Override
    public Map<String, String> getBotMessage(Command command) {
        Map<String, String> botMessage = new HashMap<>();
        String message = command.getMessage().toLowerCase();
        String payload = command.getPayload().toLowerCase();
        String userId = command.getUserId();
        System.out.println("Get message: " + message);

        BotUser botUser = botUserRepository.findOneById(userId);

        switch(message) {
            case CommandText.HELLO:
                if(botUser == null) {
                    botUser = botUserRepository.save(new BotUser(userId));
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
                    botUserRepository.save(new BotUser(userId));
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
            case CommandText.SCHEDULE:
                botMessage.put(
                        Constants.KEY_MESSAGE,
                        "Хотите расписание пар или сессии?");
                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonSchedule).getJsonText());
                break;
            case CommandText.LESSONS:
                if(BotMessageUtils.isBotUserFullTime(botUser)) {
                    botMessage.put(
                            Constants.KEY_MESSAGE,
                            "Выберите день, для которого хотите узнать расписание.");
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
                botMessage.put(Constants.KEY_MESSAGE, "Выберите курс.");
                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonCourse).getJsonText());
                break;
            case CommandText.EXTRAMURAL:
                botUserRepository.updateEducationForm(EducationForm.ZO.getGroupType(), userId);
                botMessage.put(Constants.KEY_MESSAGE, "Выберите курс.");
                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonCourse).getJsonText());
                break;
            case CommandText.EVENING:
                botUserRepository.updateEducationForm(EducationForm.VO.getGroupType(), userId);
                botMessage.put(Constants.KEY_MESSAGE, "Выберите курс.");
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
            case CommandText.SET_SEND_SCHEDULE_TIME:
            case CommandText.ENABLE_SEND_SCHEDULE:
            case CommandText.DISABLE_SEND_SCHEDULE:
            case CommandText.CURRENT_USER_SETTINGS:
                botMessage = settingsService.getBotMessage(message, botUser);
                break;
            case "т":
                botMessage.put(Constants.KEY_MESSAGE, "test");
                break;
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

        if(CommandText.COURSE_PATTERN.matcher(message).matches()) {
            String course = message.substring(0, 1);
            if(botUser != null
                    && !StringUtils.isEmpty(botUser.getDepartment())
                    && !StringUtils.isEmpty(botUser.getEducationForm())) {
                GroupNumbersDto groupNumbersDto =
                        callRestService.getGroupNumbersByDepartmentUrlAndEducationFormAndCourse(
                                botUser.getDepartment(),
                                EducationForm.fromGroupType(botUser.getEducationForm()).toString(),
                                course
                        );
                if(CollectionUtils.isEmpty(groupNumbersDto.getGroupNumbers())) {
                    botMessage.put(
                            Constants.KEY_MESSAGE,
                            "Группы не найдены.");
                    botMessage.put(
                            Constants.KEY_KEYBOARD,
                            KeyboardMap.keyboards.get(KeyboardType.ButtonCourse).getJsonText());
                }
                else {
                    List<String> groupNumbers = groupNumbersDto.getGroupNumbers();
                    List<List<VkKeyboardButton>> vkKeyboardButtons = new ArrayList<>();

                    int i = 0;
                    int row = 0;
                    vkKeyboardButtons.add(new ArrayList<>());

                    while(i < groupNumbers.size()) {
                        if(i % 5 == 4) {
                            row++;
                            vkKeyboardButtons.add(new ArrayList<>());
                        }
                        vkKeyboardButtons.get(row).add(
                                new VkKeyboardButton(
                                        new VkKeyboardButtonActionText(
                                                groupNumbers.get(i),
                                                CommandText.CHOOSE_STUDENT_GROUP,
                                                VkKeyboardButtonActionType.TEXT
                                        ), VkKeyboardButtonColor.PRIMARY)
                        );
                        i++;
                    }

                    VkKeyboard vkKeyboard = new VkKeyboard(vkKeyboardButtons, true);
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        botMessage.put(Constants.KEY_KEYBOARD, objectMapper.writeValueAsString(vkKeyboard));
                        botMessage.put(Constants.KEY_MESSAGE, "Выберите группу.");
                    }
                    catch(Exception e) {
                        botMessage.put(Constants.KEY_MESSAGE, "Не удалось загрузить список групп.");
                    }
                }
            }
        }

        if(CommandText.CHOOSE_STUDENT_GROUP.equalsIgnoreCase(payload)) {
            botUser.setGroupNumber(message);
            botUserRepository.save(botUser);
            botMessage.put(Constants.KEY_MESSAGE, "Вы выбрали группу " + message);
            botMessage.put(Constants.KEY_KEYBOARD, KeyboardMap.keyboards.get(KeyboardType.ButtonSchedule).getJsonText());
        }

        if(CommandText.HOUR_PATTERN.matcher(message).matches()) {
            ScheduleDailyNotification scheduleDailyNotification = scheduleDailyNotificationRepository.findByUserId(userId);
            int hourForSend = Integer.parseInt(message.substring(0, message.indexOf(" ")));
            if(scheduleDailyNotification == null) {
                scheduleDailyNotification = new ScheduleDailyNotification(userId, true, hourForSend);
            }
            else {
                scheduleDailyNotification.setHourForSend(hourForSend);
            }
            scheduleDailyNotificationRepository.save(scheduleDailyNotification);
            botMessage.put(
                    Constants.KEY_MESSAGE,
                    "Теперь расписание будет приходить в " + scheduleDailyNotification.getHourForSend() + " ч.");
            botMessage.put(
                    Constants.KEY_KEYBOARD,
                    KeyboardMap.keyboards.get(KeyboardType.ButtonSettings).getJsonText());
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
                        KeyboardMap.keyboards.get(KeyboardType.ButtonSchedule).getJsonText());
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
                botMessage.put(Constants.KEY_MESSAGE, Templates.makeTemplate(lessons, ""));
                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonActions).getJsonText());
            }
        }

        if(message.equalsIgnoreCase("testkb")) {
            botMessage.put(Constants.KEY_MESSAGE, "Тестим кастомную клаву");
            try {
                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        TestKeyboardService.getTestKeyboard());
            }catch(Exception e) {}
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
}
