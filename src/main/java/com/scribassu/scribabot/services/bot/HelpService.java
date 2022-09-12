package com.scribassu.scribabot.services.bot;

import com.scribassu.scribabot.dto.BotMessage;
import com.scribassu.scribabot.dto.InnerBotUser;
import com.scribassu.scribabot.generators.TgKeyboardGenerator;
import com.scribassu.scribabot.generators.VkKeyboardGenerator;
import org.springframework.stereotype.Service;

@Service
public class HelpService implements BotMessageService {

    private static final String HELP_VK = "Чем смогу, помогу. Справка периодически пополняется.\n\n" +
            "'меню' - возврат в главное меню.\n\n" +
            "Все команды, указанные на клавиатурах, можно вводить и просто так, причем в любом регистре.\n\n" +
            "Для получения расписания укажите факультет и группу, в которой на данный момент обучаетесь. Я запоминаю указанные факультет и группу до следующей их смены. В начале нового учебного года не забывайте обновлять группу!\n\n" +
            "По умолчанию рассылки расписания не подключены. Для подключения какой-либо рассылки нужно установить время рассылки, после чего она автоматически включится. В дальнейшем можно пользоваться кнопками вкл-выкл, пока не захотите сменить время.\n\n" +
            "На данный момент поддерживается расписание очников (в т. ч. для колледжей), заочников и преподавателей.\n\n" +
            "Чтобы сбросить режим узнавания расписания преподавателей, выберите на клавиатуре или введите вручную 'Главное меню'. Также сработает ввод слова 'меню'.\n\n" +
            "Курсы магистратуры отсчитываются с первого.\n\n" +
            "Ручной ввод факультетов: биолог, географ, геолог, идпо, и-т искусств, ииимо, и-т физики, и-т физ кул, " +
            "ифиж, и-т химии, мехмат, фияил, книит, соцфак, фнбмт, фнп, психолог, пписо, " +
            "философ, эконом, юрфак, геолог к-ж, к-ж яблочкова\n\n" +
            "Статья со списком команд для ручного ввода (если ваш клиент ВК не поддерживает клавиатуры ботов или вы вам удобнее вводить команды вручную): vk.com/@scriba-komandy-bota\n\n" +
            "Поддержать проект: vk.com/scriba?w=app6471849_-187867513\n\n" +
            "Обратная связь: vk.com/topic-187867513_41144487\n\n" +
            "Советую подписаться на группу, чтобы быть в курсе обновлений.\n\n" +
            "Желаю продуктивной учебы!";

    private static final String HELP_TG = "Чем смогу, помогу. Справка периодически пополняется.\n\n" +
            "'меню' - возврат в главное меню.\n\n" +
            "Все команды, указанные на клавиатурах, можно вводить и просто так, причем в любом регистре.\n\n" +
            "Для получения расписания укажите факультет и группу, в которой на данный момент обучаетесь. Я запоминаю указанные факультет и группу до следующей их смены. В начале нового учебного года не забывайте обновлять группу!\n\n" +
            "По умолчанию рассылки расписания не подключены. Для подключения какой-либо рассылки нужно установить время рассылки, после чего она автоматически включится. В дальнейшем можно пользоваться кнопками вкл-выкл, пока не захотите сменить время.\n\n" +
            "На данный момент поддерживается расписание очников (в т. ч. для колледжей), заочников и преподавателей.\n\n" +
            "Чтобы сбросить режим узнавания расписания преподавателей, выберите на клавиатуре или введите вручную 'Главное меню'. Также сработает ввод слова 'меню'.\n\n" +
            "Курсы магистратуры отсчитываются с первого.\n\n" +
            "Ручной ввод факультетов: биолог, географ, геолог, идпо, и-т искусств, ииимо, и-т физ кул, " +
            "ифиж, и-т химии, мехмат, фияил, книит, соцфак, фнбмт, фнп, психолог, пписо, физфак, " +
            "философ, эконом, юрфак, геолог к-ж, к-ж яблочкова\n\n" +
            "Статья со списком команд для ручного ввода: telegra.ph/Komandy-bota-dlya-ruchnogo-vvoda-07-21\n\n" +
            "Поддержать проект: vk.com/scriba?w=app6471849_-187867513\n\n" +
            "Обратная связь: @Dzeru\n\n" +
            "Советую подписаться на новостной канал t.me/scribanews, чтобы быть в курсе обновлений.\n\n" +
            "Желаю продуктивной учебы!";

    @Override
    public BotMessage getBotMessage(String message, InnerBotUser botUser) {
        return botUser.fromVk() ?
                new BotMessage(HELP_VK, VkKeyboardGenerator.mainMenu)
                : new BotMessage(HELP_TG, TgKeyboardGenerator.mainMenu());
    }
}
