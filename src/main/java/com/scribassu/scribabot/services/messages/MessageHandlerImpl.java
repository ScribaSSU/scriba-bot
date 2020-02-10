package com.scribassu.scribabot.services.messages;

import com.scribassu.scribabot.commands.CommandText;
import com.scribassu.scribabot.entities.BotUser;
import com.scribassu.scribabot.entities.ScheduleDailyNotification;
import com.scribassu.scribabot.keyboard.KeyboardMap;
import com.scribassu.scribabot.keyboard.KeyboardType;
import com.scribassu.scribabot.repositories.BotUserRepository;
import com.scribassu.scribabot.repositories.ScheduleDailyNotificationRepository;
import com.scribassu.scribabot.services.CallRestService;
import com.scribassu.scribabot.services.bot.FullTimeLessonService;
import com.scribassu.scribabot.services.bot.HelpService;
import com.scribassu.scribabot.services.bot.SettingsScheduleDailyNotificationService;
import com.scribassu.scribabot.util.Constants;
import com.scribassu.scribabot.util.DepartmentConverter;
import com.scribassu.scribabot.util.Templates;
import com.scribassu.tracto.domain.EducationForm;
import com.scribassu.tracto.domain.FullTimeLesson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
    private final SettingsScheduleDailyNotificationService settingsScheduleDailyNotificationService;

    @Autowired
    public MessageHandlerImpl(CallRestService callRestService,
                              HelpService helpService,
                              FullTimeLessonService fullTimeLessonService,
                              BotUserRepository botUserRepository,
                              ScheduleDailyNotificationRepository scheduleDailyNotificationRepository,
                              SettingsScheduleDailyNotificationService settingsScheduleDailyNotificationService) {
        this.callRestService = callRestService;
        this.helpService = helpService;
        this.fullTimeLessonService = fullTimeLessonService;
        this.botUserRepository = botUserRepository;
        this.scheduleDailyNotificationRepository = scheduleDailyNotificationRepository;
        this.settingsScheduleDailyNotificationService = settingsScheduleDailyNotificationService;
    }

    @Override
    public Map<String, String> getBotMessage(String message, String userId) {
        Map<String, String> botMessage = new HashMap<>();
        message = message.toLowerCase();
        System.out.println("Get message: " + message);

        BotUser botUser = botUserRepository.findOneById(userId);

        switch(message) {
            case CommandText.HELLO:
                if(botUser == null) {
                    botUserRepository.save(new BotUser(userId));
                }
                botMessage.put(
                        Constants.KEY_MESSAGE,
                        "Привет!");
                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonActions).getJsonText());
                break;
            case CommandText.MAIN_MENU:
            case CommandText.SHORT_MAIN_MENU:
                if(botUser == null) {
                    botUserRepository.save(new BotUser(userId));
                }
                botMessage.put(
                        Constants.KEY_MESSAGE,
                        "Возврат в главное меню");
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
                if(botUser != null
                        && botUser.getEducationForm() != null
                        && EducationForm.DO.getGroupType().equalsIgnoreCase(botUser.getEducationForm())) {
                    botMessage.put(
                            Constants.KEY_MESSAGE,
                            "Выберите день, для которого хотите узнать расписание");
                    botMessage.put(
                            Constants.KEY_KEYBOARD,
                            KeyboardMap.keyboards.get(KeyboardType.ButtonFullTimeSchedule).getJsonText());
                }
                else {
                    botMessage.put(
                            Constants.KEY_MESSAGE,
                            "Извините, ваша форма обучения пока не поддерживается");
                }
                break;
            case CommandText.CHOOSE_DEPARTMENT:
                botMessage.put(
                        Constants.KEY_MESSAGE,
                        "Выберите факультет");
                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonDepartment).getJsonText());
                break;
            case CommandText.FULL_TIME:
                botUserRepository.updateEducationForm(EducationForm.DO.getGroupType(), userId);
                botMessage.put(
                        Constants.KEY_MESSAGE,
                        "Введите номер группы в формате 'г номер_группы'");
                break;
            case CommandText.EXTRAMURAL:
                botUserRepository.updateEducationForm(EducationForm.ZO.getGroupType(), userId);
                botMessage.put(
                        Constants.KEY_MESSAGE,
                        "Введите номер группы в формате 'г номер_группы'");
                break;
            case CommandText.EVENING:
                botUserRepository.updateEducationForm(EducationForm.VO.getGroupType(), userId);
                botMessage.put(
                        Constants.KEY_MESSAGE,
                        "Введите номер группы в формате 'г номер_группы'");
                break;
            case CommandText.MONDAY:
            case CommandText.TUESDAY:
            case CommandText.WEDNESDAY:
            case CommandText.THURSDAY:
            case CommandText.FRIDAY:
            case CommandText.SATURDAY:
            case CommandText.TODAY:
            case CommandText.TOMORROW:
                botMessage = fullTimeLessonService.getBotMessage(message, botUser);
                break;
            case CommandText.SETTINGS:
                botMessage.put(
                        Constants.KEY_MESSAGE,
                        "Здесь вы можете настроить некоторые функции бота");
                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonSettings).getJsonText());
                break;
            case CommandText.SET_SEND_SCHEDULE_TIME:
            case CommandText.ENABLE_SEND_SCHEDULE:
            case CommandText.DISABLE_SEND_SCHEDULE:
                botMessage = settingsScheduleDailyNotificationService.getBotMessage(message, botUser);
                break;
            case "т":
                botMessage.put(Constants.KEY_MESSAGE, "test");
                break;
        }

        if(CommandText.DEPARTMENT_PATTERN.matcher(message).matches()) {
            botUserRepository.updateDepartment(DepartmentConverter.convertToUrl(message), userId);
            botMessage.put(
                    Constants.KEY_MESSAGE,
                    "Выберите форму расписания");
            botMessage.put(
                    Constants.KEY_KEYBOARD,
                    KeyboardMap.keyboards.get(KeyboardType.ButtonGroupType).getJsonText()
            );
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
                    KeyboardMap.keyboards.get(KeyboardType.ButtonSettings).getJsonText()
            );
        }

        if(message.startsWith(CommandText.GROUP_NUMBER_INPUT)) {
            botUserRepository.updateGroupNumber(message.substring(2), userId);
            botMessage.put(
                    Constants.KEY_MESSAGE,
                    "Хотите расписание пар или сессии?");
            botMessage.put(
                    Constants.KEY_KEYBOARD,
                    KeyboardMap.keyboards.get(KeyboardType.ButtonSchedule).getJsonText()
            );
        }

        if(message.startsWith("р ")) {
            String[] params = message.split(" ");
            List<FullTimeLesson> lessons = callRestService.getFullTimeLessonsByDay(
                    params[1],
                    params[2],
                    params[3]
            );

            if(CollectionUtils.isEmpty(lessons)) {
                botMessage.put(Constants.KEY_MESSAGE, "Информация отсутствует.");
            }
            else {
                botMessage.put(Constants.KEY_MESSAGE, Templates.makeTemplate(lessons));
            }
        }

        if(botMessage.isEmpty()
                || !botMessage.containsKey(Constants.KEY_MESSAGE)
                && !botMessage.containsKey(Constants.KEY_KEYBOARD)) {
            botMessage.put(Constants.KEY_MESSAGE, "Сообщение не распознано или недостаточно данных :(");
        }

        return botMessage;
    }
}
