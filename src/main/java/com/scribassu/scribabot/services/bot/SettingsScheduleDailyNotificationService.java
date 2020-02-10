package com.scribassu.scribabot.services.bot;

import com.scribassu.scribabot.commands.CommandText;
import com.scribassu.scribabot.entities.BotUser;
import com.scribassu.scribabot.entities.ScheduleDailyNotification;
import com.scribassu.scribabot.keyboard.KeyboardMap;
import com.scribassu.scribabot.keyboard.KeyboardType;
import com.scribassu.scribabot.repositories.ScheduleDailyNotificationRepository;
import com.scribassu.scribabot.util.Constants;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SettingsScheduleDailyNotificationService implements BotMessageService {

    private final ScheduleDailyNotificationRepository scheduleDailyNotificationRepository;

    public SettingsScheduleDailyNotificationService(ScheduleDailyNotificationRepository scheduleDailyNotificationRepository) {
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
                        "Выберите удобное время для рассылки расписания");
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
                    botMessage.put(
                            Constants.KEY_MESSAGE,
                            "Вы не подключали рассылку расписания или она уже включена");
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
                            "Рассылка расписания отключена");
                }
                else {
                    botMessage.put(
                            Constants.KEY_MESSAGE,
                            "Вы не подключали рассылку расписания или она уже выключена");
                }

                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonSettings).getJsonText());
                break;
        }

        return botMessage;
    }
}
