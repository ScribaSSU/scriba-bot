package com.scribassu.scribabot.services.bot;

import com.scribassu.scribabot.entities.BotUser;
import com.scribassu.scribabot.entities.ScheduleDailyNotification;
import com.scribassu.scribabot.entities.ScheduleTomorrowNotification;
import com.scribassu.scribabot.keyboard.KeyboardMap;
import com.scribassu.scribabot.keyboard.KeyboardType;
import com.scribassu.scribabot.repositories.ScheduleDailyNotificationRepository;
import com.scribassu.scribabot.repositories.ScheduleTomorrowNotificationRepository;
import com.scribassu.scribabot.text.CommandText;
import com.scribassu.scribabot.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SettingsService implements BotMessageService {

    private final ScheduleDailyNotificationRepository scheduleDailyNotificationRepository;
    private final ScheduleTomorrowNotificationRepository scheduleTomorrowNotificationRepository;

    @Autowired
    public SettingsService(ScheduleDailyNotificationRepository scheduleDailyNotificationRepository,
                           ScheduleTomorrowNotificationRepository scheduleTomorrowNotificationRepository) {
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
                        "Выберите удобное время для рассылки расписания на сегодня.");
                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonHours).getJsonText());
                break;
            case CommandText.ENABLE_SEND_SCHEDULE_TODAY:
                ScheduleDailyNotification scheduleDailyNotificationEn =
                        scheduleDailyNotificationRepository.findByUserId(userId);
                if(scheduleDailyNotificationEn != null && !scheduleDailyNotificationEn.isEnabled()) {
                    scheduleDailyNotificationRepository.enableScheduleDailyNotificationByUserId(userId);
                    botMessage.put(
                            Constants.KEY_MESSAGE,
                            "Теперь расписание на сегодня будет приходить в " +
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
                                "Рассылка расписания на сегодня уже включена.");
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
                            "Рассылка расписания на сегодня отключена.");
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
                                "Рассылка расписания на сегодня уже выключена.");
                    }
                }
                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonSettings).getJsonText());
                break;
            case CommandText.SET_SEND_SCHEDULE_TIME_TOMORROW:
                botMessage.put(
                        Constants.KEY_MESSAGE,
                        "Выберите удобное время для рассылки расписания на завтра.");
                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonHours).getJsonText());
                break;
            case CommandText.ENABLE_SEND_SCHEDULE_TOMORROW:
                ScheduleTomorrowNotification scheduleTomorrowNotificationEn =
                        scheduleTomorrowNotificationRepository.findByUserId(userId);
                if(scheduleTomorrowNotificationEn != null && !scheduleTomorrowNotificationEn.isEnabled()) {
                    scheduleTomorrowNotificationRepository.enableScheduleTomorrowNotificationByUserId(userId);
                    botMessage.put(
                            Constants.KEY_MESSAGE,
                            "Теперь расписание на завтра будет приходить в " +
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
                                "Рассылка расписания на завтра уже включена.");
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
                            "Рассылка расписания на завтра отключена.");
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
                                "Рассылка расписания на завтра уже выключена.");
                    }
                }
                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonSettings).getJsonText());
                break;
            case CommandText.CURRENT_USER_SETTINGS:
                String currentUserSettings = getStudentInfo(botUser) +
                        "\n" +
                        getScheduleNotificationStatus(botUser.getUserId());
                botMessage.put(Constants.KEY_MESSAGE, currentUserSettings);
                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonSettings).getJsonText());
                break;
        }

        return botMessage;
    }

    private String getStudentInfo(BotUser botUser) {
        return "Факультет: " + firstNotEmpty(botUser.getDepartment()) + "\n" +
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

        System.out.println("SCHEDULE TOMORROW INFO START");

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

        System.out.println("SCHEDULE TOMORROW INFO END");

        return stringBuilder.toString();
    }

    private String firstNotEmpty(String string) {
        return StringUtils.isEmpty(string) ? "не указано" : string;
    }
}
