package com.scribassu.scribabot.services.bot_message;

import com.scribassu.scribabot.generators.InnerKeyboardGenerator;
import com.scribassu.scribabot.model.BotMessage;
import com.scribassu.scribabot.model.BotUser;
import com.scribassu.scribabot.properties.ProjectProperties;
import com.scribassu.scribabot.services.BotMessageService;
import com.scribassu.scribabot.text.CommandText;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@Data
public class HelpService implements BotMessageService {

    private final InnerKeyboardGenerator innerKeyboardGenerator;
    private final ProjectProperties projectProperties;

    @Value("${scheduled.default-hour-for-send}")
    private int defaultHourForSend;

    private final String HELP_VK = "Чем смогу, помогу. Справка периодически пополняется.\n\n" +
            "'меню' - возврат в главное меню.\n\n" +
            "Все команды, указанные на клавиатурах, можно вводить и просто так, причем в любом регистре.\n\n" +
            "Для получения расписания укажите факультет и группу, в которой на данный момент обучаетесь. Я запоминаю указанные факультет и группу до следующей их смены. В начале нового учебного года не забывайте обновлять группу!\n\n" +
            "По умолчанию рассылки расписания выставлены на %s часов и выключены. Можно установить свое время рассылки, после чего она автоматически включится. В дальнейшем можно пользоваться кнопками вкл-выкл, пока не захотите сменить время.\n\n" +
            "На данный момент поддерживается расписание очников (в т. ч. для колледжей), заочников и преподавателей.\n\n" +
            "Чтобы сбросить режим узнавания расписания преподавателей, выберите на клавиатуре или введите вручную 'Главное меню' или 'Выйти из расписания преподавателя'. Также сработает ввод слова 'меню'.\n\n" +
            "Курсы магистратуры отсчитываются с первого.\n\n" +
            "Ручной ввод факультетов: биолог, географ, геолог, идпо, и-т искусств, ииимо, и-т физики, и-т физ кул, " +
            "ифиж, и-т химии, мехмат, фияил, книит, соцфак, ффмимт, психолог, пписо, " +
            "философ, эконом, юрфак, геолог к-ж, к-ж яблочкова\n\n" +
            "Версия бота: %s\n\n" +
            "Статья со списком команд для ручного ввода (если ваш клиент ВК не поддерживает клавиатуры ботов или вы вам удобнее вводить команды вручную): vk.com/@scriba-komandy-bota\n\n" +
            "Поддержать проект: vk.com/scriba?w=app6471849_-187867513\n\n" +
            "Обратная связь: vk.com/topic-187867513_41144487\n\n" +
            "Советую подписаться на группу, чтобы быть в курсе обновлений.\n\n" +
            "Желаю продуктивной учебы!";

    private final String HELP_TG = "Чем смогу, помогу. Справка периодически пополняется.\n\n" +
            "'меню' - возврат в главное меню.\n\n" +
            "Все команды, указанные на клавиатурах, можно вводить и просто так, причем в любом регистре. Эмодзи не обязательны :)\n\n" +
            "Для получения расписания укажите факультет и группу, в которой на данный момент обучаетесь. Я запоминаю указанные факультет и группу до следующей их смены. В начале нового учебного года не забывайте обновлять группу!\n\n" +
            "По умолчанию рассылки расписания выставлены на %s часов и выключены. Можно установить свое время рассылки, после чего она автоматически включится. В дальнейшем можно пользоваться кнопками вкл-выкл, пока не захотите сменить время.\n\n" +
            "На данный момент поддерживается расписание очников (в т. ч. для колледжей), заочников и преподавателей.\n\n" +
            "Чтобы сбросить режим узнавания расписания преподавателей, выберите на клавиатуре или введите вручную 'Главное меню' или 'Выйти из расписания преподавателя'. Также сработает ввод слова 'меню'.\n\n" +
            "Курсы магистратуры отсчитываются с первого.\n\n" +
            "Ручной ввод факультетов: биолог, географ, геолог, идпо, и-т искусств, ииимо, и-т физ кул, " +
            "ифиж, и-т химии, мехмат, фияил, книит, соцфак, ффмимт, психолог, пписо, физфак, " +
            "философ, эконом, юрфак, геолог к-ж, к-ж яблочкова\n\n" +
            "Версия бота: %s\n\n" +
            "Статья со списком команд для ручного ввода: telegra.ph/Komandy-bota-dlya-ruchnogo-vvoda-07-21\n\n" +
            "Поддержать проект: vk.com/scriba?w=app6471849_-187867513\n\n" +
            "Обратная связь: @Dzeru\n\n" +
            "Советую подписаться на новостной канал t.me/scribanews, чтобы быть в курсе обновлений.\n\n" +
            "Желаю продуктивной учебы!";

    @Override
    public boolean shouldAccept(String message, BotUser botUser) {
        return message.equals(CommandText.HELP);
    }

    @Override
    public CompletableFuture<BotMessage> getBotMessage(String message, BotUser botUser) {
        var help = botUser.fromVk() ? String.format(HELP_VK, defaultHourForSend, projectProperties.getVersion()) : String.format(HELP_TG, defaultHourForSend, projectProperties.getVersion());
        return CompletableFuture.completedFuture(new BotMessage(help, innerKeyboardGenerator.mainMenu(botUser), botUser));
    }
}
