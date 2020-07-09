package com.scribassu.scribabot.services.bot;

import com.scribassu.scribabot.entities.BotUser;
import com.scribassu.scribabot.entities.ScheduleDailyNotification;
import com.scribassu.scribabot.entities.ScheduleTomorrowNotification;
import com.scribassu.scribabot.keyboard.KeyboardMap;
import com.scribassu.scribabot.keyboard.KeyboardType;
import com.scribassu.scribabot.repositories.BotUserRepository;
import com.scribassu.scribabot.repositories.ScheduleDailyNotificationRepository;
import com.scribassu.scribabot.repositories.ScheduleTomorrowNotificationRepository;
import com.scribassu.scribabot.text.CommandText;
import com.scribassu.scribabot.text.MessageText;
import com.scribassu.scribabot.util.Constants;
import com.scribassu.scribabot.util.DepartmentConverter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SettingsService implements BotMessageService {

    private final BotUserRepository botUserRepository;
    private final ScheduleDailyNotificationRepository scheduleDailyNotificationRepository;
    private final ScheduleTomorrowNotificationRepository scheduleTomorrowNotificationRepository;

    @Autowired
    public SettingsService(BotUserRepository botUserRepository,
                           ScheduleDailyNotificationRepository scheduleDailyNotificationRepository,
                           ScheduleTomorrowNotificationRepository scheduleTomorrowNotificationRepository) {
        this.botUserRepository = botUserRepository;
        this.scheduleDailyNotificationRepository = scheduleDailyNotificationRepository;
        this.scheduleTomorrowNotificationRepository = scheduleTomorrowNotificationRepository;
    }

    @Override
    public Map<String, String> getBotMessage(String message, BotUser botUser) {
        Map<String, String> botMessage = new HashMap<>();
        String userId = botUser.getUserId();

        switch(message) {
            case CommandText.SET_SEND_SCHEDULE_TIME_TODAY:
                botMessage.put(
                        Constants.KEY_MESSAGE,
                        String.format(MessageText.CHOOSE_SCHEDULE_NOTIFICATION_TIME, "сегодня"));
                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonHours).getJsonText());
                botUser.setPreviousUserMessage(String.format(MessageText.CHOOSE_SCHEDULE_NOTIFICATION_TIME, "сегодня"));
                botUserRepository.save(botUser);
                break;
            case CommandText.ENABLE_SEND_SCHEDULE_TODAY:
                ScheduleDailyNotification scheduleDailyNotificationEn =
                        scheduleDailyNotificationRepository.findByUserId(userId);
                if(scheduleDailyNotificationEn != null && !scheduleDailyNotificationEn.isEnabled()) {
                    scheduleDailyNotificationRepository.enableScheduleDailyNotificationByUserId(userId);
                    botMessage.put(
                            Constants.KEY_MESSAGE,
                            String.format(MessageText.SCHEDULE_WILL_BE_SENT, "сегодня") +
                                    scheduleDailyNotificationEn.getHourForSend() + " ч.");
                }
                else {
                    if(scheduleDailyNotificationEn == null) {
                        botMessage.put(
                                Constants.KEY_MESSAGE,
                                "Вы еще не подключали рассылку расписания на сегодня. Подключите через '" +
                                        CommandText.SET_SEND_SCHEDULE_TIME_TODAY + "'.");
                    }
                    else {
                        botMessage.put(
                                Constants.KEY_MESSAGE,
                                String.format(MessageText.SCHEDULE_IS_ENABLED_DOUBLE, "сегодня"));
                    }
                }

                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonSettings).getJsonText());
                break;
            case CommandText.DISABLE_SEND_SCHEDULE_TODAY:
                ScheduleDailyNotification scheduleDailyNotificationDis =
                        scheduleDailyNotificationRepository.findByUserId(userId);
                if(scheduleDailyNotificationDis != null && scheduleDailyNotificationDis.isEnabled()) {
                    scheduleDailyNotificationRepository.disableScheduleDailyNotificationByUserId(userId);
                    botMessage.put(
                            Constants.KEY_MESSAGE,
                            String.format(MessageText.SCHEDULE_IS_DISABLED, "сегодня"));
                }
                else {
                    if(scheduleDailyNotificationDis == null) {
                        botMessage.put(
                                Constants.KEY_MESSAGE,
                                "Вы еще не подключали рассылку расписания на сегодня. Подключите через '" +
                                        CommandText.SET_SEND_SCHEDULE_TIME_TODAY + "'.");
                    }
                    else {
                        botMessage.put(
                                Constants.KEY_MESSAGE,
                                String.format(MessageText.SCHEDULE_IS_DISABLED_DOUBLE, "сегодня"));
                    }
                }
                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonSettings).getJsonText());
                break;
            case CommandText.SET_SEND_SCHEDULE_TIME_TOMORROW:
                botMessage.put(
                        Constants.KEY_MESSAGE,
                        String.format(MessageText.CHOOSE_SCHEDULE_NOTIFICATION_TIME, "завтра"));
                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonHours).getJsonText());
                botUser.setPreviousUserMessage(String.format(MessageText.CHOOSE_SCHEDULE_NOTIFICATION_TIME, "завтра"));
                botUserRepository.save(botUser);
                break;
            case CommandText.ENABLE_SEND_SCHEDULE_TOMORROW:
                ScheduleTomorrowNotification scheduleTomorrowNotificationEn =
                        scheduleTomorrowNotificationRepository.findByUserId(userId);
                if(scheduleTomorrowNotificationEn != null && !scheduleTomorrowNotificationEn.isEnabled()) {
                    scheduleTomorrowNotificationRepository.enableScheduleTomorrowNotificationByUserId(userId);
                    botMessage.put(
                            Constants.KEY_MESSAGE,
                            String.format(MessageText.SCHEDULE_WILL_BE_SENT, "завтра") +
                                    scheduleTomorrowNotificationEn.getHourForSend() + " ч.");
                }
                else {
                    if(scheduleTomorrowNotificationEn == null) {
                        botMessage.put(
                                Constants.KEY_MESSAGE,
                                "Вы еще не подключали рассылку расписания на завтра. Подключите через '" +
                                        CommandText.SET_SEND_SCHEDULE_TIME_TOMORROW + "'.");
                    }
                    else {
                        botMessage.put(
                                Constants.KEY_MESSAGE,
                                String.format(MessageText.SCHEDULE_IS_ENABLED_DOUBLE, "завтра"));
                    }
                }

                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonSettings).getJsonText());
                break;
            case CommandText.DISABLE_SEND_SCHEDULE_TOMORROW:
                ScheduleTomorrowNotification scheduleTomorrowNotificationDis =
                        scheduleTomorrowNotificationRepository.findByUserId(userId);
                if(scheduleTomorrowNotificationDis != null && scheduleTomorrowNotificationDis.isEnabled()) {
                    scheduleTomorrowNotificationRepository.disableScheduleTomorrowNotificationByUserId(userId);
                    botMessage.put(
                            Constants.KEY_MESSAGE,
                            String.format(MessageText.SCHEDULE_IS_DISABLED, "завтра"));
                }
                else {
                    if(scheduleTomorrowNotificationDis == null) {
                        botMessage.put(
                                Constants.KEY_MESSAGE,
                                "Вы еще не подключали рассылку расписания на завтра. Подключите через '" +
                                        CommandText.SET_SEND_SCHEDULE_TIME_TOMORROW + "'.");
                    }
                    else {
                        botMessage.put(
                                Constants.KEY_MESSAGE,
                                String.format(MessageText.SCHEDULE_IS_DISABLED_DOUBLE, "завтра"));
                    }
                }
                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonSettings).getJsonText());
                break;
            case CommandText.ENABLE_FILTER_WEEK_TYPE:
                botUser.setFilterNomDenom(true);
                botUserRepository.save(botUser);
                botMessage.put(Constants.KEY_MESSAGE, "Включена фильтрация по типу недели.");
                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonSettings).getJsonText());
                break;
            case CommandText.DISABLE_FILTER_WEEK_TYPE:
                botUser.setFilterNomDenom(false);
                botUserRepository.save(botUser);
                botMessage.put(Constants.KEY_MESSAGE, "Выключена фильтрация по типу недели.");
                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonSettings).getJsonText());
                break;
            case CommandText.CURRENT_USER_SETTINGS:
                String currentUserSettings = getStudentInfo(botUser) +
                        "\n" +
                        getScheduleNotificationStatus(botUser.getUserId()) +
                        "\n\n" +
                        "Фильтрация по типу недели: " +
                        (botUser.isFilterNomDenom() ? "вкл." : "выкл.");
                botMessage.put(Constants.KEY_MESSAGE, currentUserSettings);
                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonSettings).getJsonText());
                break;
        }

        if(CommandText.HOUR_PATTERN.matcher(message).matches()) {
            int hourForSend = Integer.parseInt(message.substring(0, message.indexOf(" ")));

            if(botUser.getPreviousUserMessage().equalsIgnoreCase(
                                String.format(MessageText.CHOOSE_SCHEDULE_NOTIFICATION_TIME, "сегодня"))) {
                ScheduleDailyNotification scheduleDailyNotification =
                        scheduleDailyNotificationRepository.findByUserId(userId);

                if(scheduleDailyNotification == null) {
                    scheduleDailyNotification = new ScheduleDailyNotification(userId, true, hourForSend);
                }
                else {
                    scheduleDailyNotification.setHourForSend(hourForSend);
                }
                scheduleDailyNotificationRepository.save(scheduleDailyNotification);
            }
            if(botUser.getPreviousUserMessage().equalsIgnoreCase(
                                String.format(MessageText.CHOOSE_SCHEDULE_NOTIFICATION_TIME, "завтра"))) {
                ScheduleTomorrowNotification scheduleTomorrowNotification =
                        scheduleTomorrowNotificationRepository.findByUserId(userId);

                if(scheduleTomorrowNotification == null) {
                    scheduleTomorrowNotification = new ScheduleTomorrowNotification(userId, true, hourForSend);
                }
                else {
                    scheduleTomorrowNotification.setHourForSend(hourForSend);
                }
                scheduleTomorrowNotificationRepository.save(scheduleTomorrowNotification);
            }
            botMessage.put(
                    Constants.KEY_MESSAGE,
                    "Теперь расписание будет приходить в " + hourForSend + " ч.");
            botMessage.put(
                    Constants.KEY_KEYBOARD,
                    KeyboardMap.keyboards.get(KeyboardType.ButtonSettings).getJsonText());
        }

        return botMessage;
    }

    private String getStudentInfo(BotUser botUser) {
        String department = firstNotEmpty(botUser.getDepartment());
        department = department.equals("не указано")
                ? department
                : DepartmentConverter.getDepartmentByUrl(department);
        return "Факультет: " + department + "\n" +
                "Форма обучения: " + firstNotEmpty(botUser.getEducationForm()) + "\n" +
                "Группа: " + firstNotEmpty(botUser.getGroupNumber()) + "\n";
    }

    private String getScheduleNotificationStatus(String userId) {
        ScheduleDailyNotification scheduleDailyNotification =
                scheduleDailyNotificationRepository.findByUserId(userId);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Рассылка расписания на сегодня: ");

        if(scheduleDailyNotification == null) {
            stringBuilder.append("не подключена.");
        }
        else {
            if(scheduleDailyNotification.isEnabled()) {
                stringBuilder.append("вкл, ");
            }
            else {
                stringBuilder.append("выкл, ");
            }

            if(scheduleDailyNotification.getHourForSend() == null) {
                stringBuilder.append("время не указано.");
            }
            else {
                stringBuilder.append(scheduleDailyNotification.getHourForSend()).append(" ч.");
            }
        }

        stringBuilder.append("\n\n");

        System.out.println("FULL_TIME_SCHEDULE TOMORROW INFO START");

        ScheduleTomorrowNotification scheduleTomorrowNotification =
                scheduleTomorrowNotificationRepository.findByUserId(userId);

        stringBuilder.append("Рассылка расписания на завтра: ");

        if(scheduleTomorrowNotification == null) {
            stringBuilder.append("не подключена.");
        }
        else {
            if(scheduleTomorrowNotification.isEnabled()) {
                stringBuilder.append("вкл, ");
            }
            else {
                stringBuilder.append("выкл, ");
            }

            if(scheduleTomorrowNotification.getHourForSend() == null) {
                stringBuilder.append("время не указано.");
            }
            else {
                stringBuilder.append(scheduleTomorrowNotification.getHourForSend()).append(" ч.");
            }
        }

        System.out.println("FULL_TIME_SCHEDULE TOMORROW INFO END");

        return stringBuilder.toString();
    }

    private String firstNotEmpty(String string) {
        return StringUtils.isEmpty(string) ? "не указано" : string;
    }
}
