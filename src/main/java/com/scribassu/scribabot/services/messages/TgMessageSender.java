package com.scribassu.scribabot.services.messages;

import com.scribassu.scribabot.dto.BotMessage;
import com.scribassu.scribabot.dto.Command;
import com.scribassu.scribabot.generators.TgKeyboardGenerator;
import com.scribassu.scribabot.util.BotUserSource;
import com.scribassu.scribabot.util.TgKeyboardEmoji;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Locale;

@Slf4j
@Component
public class TgMessageSender extends TelegramLongPollingBot implements MessageSender {

    @Autowired
    private MessageHandler messageHandler;

    @Value("${scriba-bot.tg.name}")
    private String botUsername;

    @Value("${scriba-bot.tg.token}")
    private String botToken;

    private static final Integer TG_LENGTH = 4096;
    private static final ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();

    @Override
    public void onUpdateReceived(Update update) {
        String text = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        text = removeEmoji(text);
        text = text.toLowerCase(Locale.ROOT);
        Command command = new Command(text,
                "no payload",
                String.valueOf(update.getMessage().getFrom().getId()),
                BotUserSource.TG);
        log.info("TG Message: " + command);
        try {
            BotMessage botMessage = messageHandler.getBotMessage(command);

            int startIndex = 0;
            String message;
            int lastSpaceIndex = 0;
            while (startIndex < botMessage.getMessage().length()) {
                if (botMessage.getMessage().length() - startIndex <= TG_LENGTH) {
                    message = botMessage.getMessage().substring(startIndex);
                } else {
                    lastSpaceIndex = botMessage.getMessage().lastIndexOf(' ', startIndex + TG_LENGTH);
                    message = botMessage.getMessage().substring(startIndex, lastSpaceIndex);
                }

                if (botMessage.getBotUser().isSentKeyboard()) {
                    if (botMessage.hasTgKeyboard()) {
                        execute(new SendMessage().setChatId(chatId)
                                .setText(message)
                                .setReplyMarkup(botMessage.getTgKeyboard()));
                    } else {
                        execute(new SendMessage().setChatId(chatId)
                                .setText(message)
                                .setReplyMarkup(TgKeyboardGenerator.mainMenu()));
                    }
                } else {
                    execute(new SendMessage().setChatId(chatId).setText(message).setReplyMarkup(replyKeyboardRemove));
                }

                if (lastSpaceIndex > 0) {
                    startIndex = lastSpaceIndex + 1;
                    lastSpaceIndex = 0;
                } else {
                    startIndex += TG_LENGTH;
                }
            }
        } catch (TelegramApiException e) {
            log.error("Telegram API Exception: " + e.getMessage());
        } catch (Exception e) {
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
        for (String emoji : TgKeyboardEmoji.TG_EMOJIS)
            text = text.replace(emoji, "");
        return text.trim();
    }

    @Override
    public void send(BotMessage botMessage, String userId) {
        try {
            int startIndex = 0;
            String message;
            int lastSpaceIndex = 0;
            while (startIndex < botMessage.getMessage().length()) {
                if (botMessage.getMessage().length() - startIndex <= TG_LENGTH) {
                    message = botMessage.getMessage().substring(startIndex);
                } else {
                    lastSpaceIndex = botMessage.getMessage().lastIndexOf(' ', startIndex + TG_LENGTH);
                    message = botMessage.getMessage().substring(startIndex, lastSpaceIndex);
                }

                if (botMessage.getBotUser().isSentKeyboard()) {
                    if (botMessage.hasTgKeyboard()) {
                        execute(new SendMessage().setChatId(userId)
                                .setText(message)
                                .setReplyMarkup(botMessage.getTgKeyboard()));
                    } else {
                        execute(new SendMessage().setChatId(userId)
                                .setText(message)
                                .setReplyMarkup(TgKeyboardGenerator.mainMenu()));
                    }
                } else {
                    execute(new SendMessage().setChatId(userId).setText(message).setReplyMarkup(replyKeyboardRemove));
                }

                if (lastSpaceIndex > 0) {
                    startIndex = lastSpaceIndex + 1;
                    lastSpaceIndex = 0;
                } else {
                    startIndex += TG_LENGTH;
                }
            }
        } catch (TelegramApiException e) {
            log.error("Telegram API Exception: " + e.getMessage());
        } catch (Exception e) {
            log.error("Telegram Another Exception: " + e.getMessage());
        }
    }
}
