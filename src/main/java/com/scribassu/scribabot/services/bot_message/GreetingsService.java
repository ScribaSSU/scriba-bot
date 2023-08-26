package com.scribassu.scribabot.services.bot_message;

import com.scribassu.scribabot.generators.InnerKeyboardGenerator;
import com.scribassu.scribabot.model.BotMessage;
import com.scribassu.scribabot.model.Command;
import com.scribassu.scribabot.services.BotUserService;
import com.scribassu.scribabot.text.CommandText;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

import static com.scribassu.scribabot.text.MessageText.*;

@Service
@Slf4j
@Data
public class GreetingsService {

    private final InnerKeyboardGenerator innerKeyboardGenerator;
    private final BotUserService botUserService;

    public CompletableFuture<BotMessage> getBotMessage(Command command) {
        var registeredUserResult = botUserService.isBotUserRegistered(command);
        var botUser = registeredUserResult.getBotUser();
        var registered = registeredUserResult.isRegistered();
        var message = command.getMessage();

        switch (message) {
            case CommandText.STICKER_WAS_SENT_TO_BOT:
            case CommandText.TG_START:
            case CommandText.HELLO:
                if (!registered) {
                    botUserService.registerUser(botUser);
                    return CompletableFuture.completedFuture(new BotMessage(GREETING_WITH_CHOOSE_DEPARTMENT, innerKeyboardGenerator.departments(), botUser));
                } else {
                    return CompletableFuture.completedFuture(new BotMessage(GREETING, innerKeyboardGenerator.mainMenu(), botUser));
                }
            case CommandText.MAIN_MENU:
            case CommandText.SHORT_MAIN_MENU:
                if (!registered) {
                    botUserService.registerUser(botUser);
                    return CompletableFuture.completedFuture(new BotMessage(GREETING_WITH_CHOOSE_DEPARTMENT, innerKeyboardGenerator.departments(), botUser));
                } else {
                    botUserService.updatePreviousUserMessage("", botUser);
                    return CompletableFuture.completedFuture(new BotMessage(RETURN_MAIN_MENU, innerKeyboardGenerator.mainMenu(), botUser));
                }
            case CommandText.THANKS:
                return CompletableFuture.completedFuture(new BotMessage(YOU_ARE_WELCOME, innerKeyboardGenerator.mainMenu(), botUser));
        }
        return CompletableFuture.completedFuture(new BotMessage(RETURN_MAIN_MENU, innerKeyboardGenerator.mainMenu(), botUser));
    }
}
