package com.scribassu.scribabot.services.bot;

import com.scribassu.scribabot.entities.BotUser;
import com.scribassu.scribabot.keyboard.KeyboardMap;
import com.scribassu.scribabot.keyboard.KeyboardType;
import com.scribassu.scribabot.util.Constants;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class HelpService implements BotMessageService {

    private static final String HELP = "Чем смогу, помогу. Справка периодически пополняется.\n\n" +
            "'меню' - возврат в главное меню.\n\n" +
            "Все команды, указанные на клавиатурах, можно вводить и просто так, причем в любом регистре.\n\n" +
            "Для получения расписания укажите факультет и группу, в которой на данный момент обучаетесь. В начале нового учебного года не забывайте обновлять группу!\n\n" +
            "На данный момент поддерживается только расписание очников. Мы работаем над другими видами расписаний, ждите.";

    @Override
    public Map<String, String> getBotMessage(String message, BotUser botUser) {
        Map<String, String> botMessage = new HashMap<>();
        botMessage.put(
                Constants.KEY_MESSAGE,
                HELP);
        botMessage.put(
                Constants.KEY_KEYBOARD,
                KeyboardMap.keyboards.get(KeyboardType.ButtonActions).getJsonText());
        return botMessage;
    }
}
