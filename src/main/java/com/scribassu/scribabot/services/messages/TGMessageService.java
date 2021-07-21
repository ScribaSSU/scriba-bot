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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class TGMessageService extends TelegramLongPollingBot {

    @Autowired
    private MessageHandler messageHandler;

    @Value("${scriba-bot.tg.name}")
    private String botUsername;

    @Value("${scriba-bot.tg.token}")
    private String botToken;

    private static final String TG_MESSAGE_WITHOUT_EMOJI_REGEX = "^[А-Яа-я\\-\\.\\sA-Za-z\\d].*$";

    private static ReplyKeyboardMarkup menu() {
        ReplyKeyboardMarkup r = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        for(int i = 0; i < 4; i++)
            rows.add(new KeyboardRow());
        //rows.get(0).add(new KeyboardButton("\uD83D\uDDD3 Расписание студентов \uD83D\uDDD3"));
        //rows.get(1).add(new KeyboardButton("\uD83C\uDF93 Расписание преподавателей \uD83C\uDF93"));
        rows.get(0).add(new KeyboardButton("\uD83C\uDF1A Расписание студентов \uD83C\uDF1A"));
        rows.get(1).add(new KeyboardButton("\uD83C\uDF1D Расписание преподавателей \uD83C\uDF1D"));
        rows.get(2).add(new KeyboardButton("⚙ Настройки ⚙"));
        rows.get(3).add(new KeyboardButton("❓ Справка ❓"));
        r.setKeyboard(rows);
        return r;
    }

    private static ReplyKeyboardMarkup settings() {
        ReplyKeyboardMarkup r = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        for(int i = 0; i < 2; i++)
            rows.add(new KeyboardRow());
        rows.get(0).add(new KeyboardButton("✅ включить ✅"));
        rows.get(1).add(new KeyboardButton("❌ выключить ❌"));
        r.setKeyboard(rows);
        return r;
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(update);
        String text = update.getMessage().getText();
        if(!text.matches(TG_MESSAGE_WITHOUT_EMOJI_REGEX)) {
            text = removeEmoji(update.getMessage().getText());
        }
        text = text.toLowerCase(Locale.ROOT);
        Command command = new Command(text,
                "no payload",
                String.valueOf(update.getMessage().getFrom().getId()),
                BotUserSource.TG);
        try {
            BotMessage botMessage = messageHandler.getBotMessage(command);
            execute(new SendMessage().setChatId(update.getMessage().getChatId())
                    .setText(botMessage.getMessage())
                    .setReplyMarkup(settings()));
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
        text = text.substring(text.indexOf(" ") + 1);
        return text.substring(0, text.lastIndexOf(" "));
    }
}
