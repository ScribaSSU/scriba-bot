package com.scribassu.scribabot.services.bot;

import com.scribassu.scribabot.dto.BotMessage;
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
    public BotMessage getBotMessage(String message, BotUser botUser) {
        return new BotMessage(HELP, KeyboardType.ButtonActions);
    }
}
