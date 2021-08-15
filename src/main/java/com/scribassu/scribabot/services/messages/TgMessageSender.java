package com.scribassu.scribabot.services.messages;

import com.scribassu.scribabot.dto.BotMessage;
import com.scribassu.scribabot.dto.Command;
import com.scribassu.scribabot.util.BotUserSource;
import com.scribassu.scribabot.util.TgKeyboardEmoji;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TgMessageSender extends TelegramLongPollingBot implements MessageSender {

    @Autowired
    private MessageHandler messageHandler;

    @Value("${scriba-bot.tg.name}")
    private String botUsername;

    @Value("${scriba-bot.tg.token}")
    private String botToken;

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(update);
        String text = update.getMessage().getText();
        text = removeEmoji(text);
        text = text.toLowerCase(Locale.ROOT);
        Command command = new Command(text,
                "no payload",
                String.valueOf(update.getMessage().getFrom().getId()),
                BotUserSource.TG);
        try {
            BotMessage botMessage = messageHandler.getBotMessage(command);
            execute(new SendMessage().setChatId(update.getMessage().getChatId())
                    .setText(botMessage.getMessage())
                    .setReplyMarkup(botMessage.getTgKeyboard()));
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

    private String removeEmoji(String text) {
        for(String emoji: TgKeyboardEmoji.TG_EMOJIS)
            text = text.replace(emoji, "");
        return text.trim();
    }

    @Override
    public void send(BotMessage botMessage, String userId) {
        try {
            execute(new SendMessage().setChatId(userId)
                    .setText(botMessage.getMessage())
                    .setReplyMarkup(botMessage.getTgKeyboard()));
        } catch (TelegramApiException e) {
            log.error("Telegram API Exception: " + e.getMessage());
        }
        catch (Exception e) {
            log.error("Telegram Another Exception: " + e.getMessage());
        }
    }
}
