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
            "Для получения расписания укажите факультет и группу, в которой на данный момент обучаетесь. Бот запоминает указанные факультет и группу до следующей их смены. В начале нового учебного года не забывайте обновлять группу!\n\n" +
            "По умолчанию рассылки расписания не подключены. Для подключения какой-либо рассылки нужно установить время рассылки, после чего она автоматически включится. В дальнейшем можно пользоваться кнопками вкл-выкл, пока не захотите сменить время.\n\n" +
            "На данный момент поддерживается только расписание очников и преподавателей. Мы работаем над другими видами расписаний, ждите.\n\n" +
            "Чтобы сбросить режим узнавания расписания преподавателей, выберите на клавиатуре или введите вручную 'Главное меню'. Также сработает ввод слова 'меню'.\n\n" +
            "Курсы магистратуры отсчитываются с первого.\n\n" +
            "Ручной ввод факультетов: биолог, географ, геолог, идпо, и-т искусств, ииимо, и-т физ кул, " +
            "ифиж, и-т химии, мехмат, фияил, книит, соцфак, фнбмт, фнп, психолог, пписо, физфак, " +
            "философ, эконом, юрфак, геолог к-ж, к-ж яблочкова";

    @Override
    public BotMessage getBotMessage(String message, BotUser botUser) {
        return new BotMessage(HELP, KeyboardType.ButtonActions);
    }
}
