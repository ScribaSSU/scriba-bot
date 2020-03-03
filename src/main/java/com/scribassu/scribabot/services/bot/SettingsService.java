package com.scribassu.scribabot.services.bot;

import com.scribassu.scribabot.entities.BotUser;
import com.scribassu.scribabot.entities.ScheduleDailyNotification;
import com.scribassu.scribabot.keyboard.KeyboardMap;
import com.scribassu.scribabot.keyboard.KeyboardType;
import com.scribassu.scribabot.repositories.ScheduleDailyNotificationRepository;
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

    @Autowired
    public SettingsService(ScheduleDailyNotificationRepository scheduleDailyNotificationRepository) {
        this.scheduleDailyNotificationRepository = scheduleDailyNotificationRepository;
    }

    @Override
    public Map<String, String> getBotMessage(String message, BotUser botUser) {
        Map<String, String> botMessage = new HashMap<>();
        String userId = botUser.getUserId();

        switch(message) {
            case CommandText.SET_SEND_SCHEDULE_TIME:
                botMessage.put(
                        Constants.KEY_MESSAGE,
                        "Выберите удобное время для рассылки расписания.");
                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonHours).getJsonText());
                break;
            case CommandText.ENABLE_SEND_SCHEDULE:
                ScheduleDailyNotification scheduleDailyNotificationEn = scheduleDailyNotificationRepository.findByUserId(userId);
                if(scheduleDailyNotificationEn != null && !scheduleDailyNotificationEn.isEnabled()) {
                    scheduleDailyNotificationRepository.enableScheduleDailyNotificationByUserId(userId);
                    botMessage.put(
                            Constants.KEY_MESSAGE,
                            "Теперь расписание будет приходить в " + scheduleDailyNotificationEn.getHourForSend() + " ч.");
                }
                else {
                    if(scheduleDailyNotificationEn == null) {
                        botMessage.put(
                                Constants.KEY_MESSAGE,
                                "Вы еще не подключали рассылку расписания. Подключите через '" + CommandText.SET_SEND_SCHEDULE_TIME + "'.");
                    }
                    else {
                        botMessage.put(
                                Constants.KEY_MESSAGE,
                                "Рассылка расписания уже включена.");
                    }
                }

                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonSettings).getJsonText());
                break;
            case CommandText.DISABLE_SEND_SCHEDULE:
                ScheduleDailyNotification scheduleDailyNotificationDis = scheduleDailyNotificationRepository.findByUserId(userId);
                if(scheduleDailyNotificationDis != null && scheduleDailyNotificationDis.isEnabled()) {
                    scheduleDailyNotificationRepository.disableScheduleDailyNotificationByUserId(userId);
                    botMessage.put(
                            Constants.KEY_MESSAGE,
                            "Рассылка расписания отключена.");
                }
                else {
                    if(scheduleDailyNotificationDis == null) {
                        botMessage.put(
                                Constants.KEY_MESSAGE,
                                "Вы еще не подключали рассылку расписания. Подключите через '" + CommandText.SET_SEND_SCHEDULE_TIME + "'.");
                    }
                    else {
                        botMessage.put(
                                Constants.KEY_MESSAGE,
                                "Рассылка расписания уже выключена.");
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
        ScheduleDailyNotification scheduleDailyNotification = scheduleDailyNotificationRepository.findByUserId(userId);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Рассылка расписания: ");

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
        return stringBuilder.toString();
    }

    private String firstNotEmpty(String string) {
        return StringUtils.isEmpty(string) ? "не указано" : string;
    }
}
