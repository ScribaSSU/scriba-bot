package com.scribassu.scribabot.services.messages;

import com.scribassu.scribabot.dto.BotMessage;
import com.scribassu.scribabot.dto.Command;
import com.scribassu.scribabot.util.BotUserSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Slf4j
@Component
public class TGMessageService extends TelegramLongPollingBot {

    @Autowired
    private MessageHandler messageHandler;

    @Value("${scriba-bot.tg.name}")
    private String botUsername;

    @Value("${scriba-bot.tg.token}")
    private String botToken;

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(update);
        Command command = new Command(update.getMessage().getText(),
                "no payload",
                String.valueOf(update.getMessage().getFrom().getId()),
                BotUserSource.TG);
        try {
            BotMessage botMessage = messageHandler.getBotMessage(command);
            ReplyKeyboardMarkup k = new ReplyKeyboardMarkup();
            KeyboardRow kr = new KeyboardRow();
            kr.add(new KeyboardButton().setText("Главное меню"));
            kr.add(new KeyboardButton().setText("Настройки"));
            k.setKeyboard(List.of(kr));
            execute(new SendMessage().setChatId(update.getMessage().getChatId())
                    .setText(botMessage.getMessage())
                    .setReplyMarkup(k));
        } catch (TelegramApiException e) {
            log.error("Telegram API Exception: " + e.getMessage());
        }
        catch (Exception e) {
            log.error("Telegram Another Exception: " + e.getMessage());
        }
    }

    public String getBotUsername() {
        return botUsername;
    }

    public String getBotToken() {
        return botToken;
    }
}
