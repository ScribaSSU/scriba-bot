package com.scribassu.scribabot.message_handlers;

import com.scribassu.scribabot.generators.InnerKeyboardGenerator;
import com.scribassu.scribabot.mapper.HttpMapper;
import com.scribassu.scribabot.model.BotMessage;
import com.scribassu.scribabot.model.Command;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@Slf4j
public class TgMessageSender extends TelegramLongPollingBot implements MessageSender {

    private final MessageHandler messageHandler;
    private final InnerKeyboardGenerator innerKeyboardGenerator;
    private final HttpMapper httpMapper;

    public TgMessageSender(MessageHandler messageHandler,
                           InnerKeyboardGenerator innerKeyboardGenerator,
                           HttpMapper httpMapper) {
        super();
        this.messageHandler = messageHandler;
        this.innerKeyboardGenerator = innerKeyboardGenerator;
        this.httpMapper = httpMapper;
    }

    @Value("${scriba-bot.tg.name}")
    private String botUsername;

    @Value("${scriba-bot.tg.token}")
    private String botToken;

    private static final Integer TG_LENGTH = 4096;
    private static final ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();

    @Override
    public void onUpdateReceived(Update update) {
        var chatId = update.getMessage().getChatId();
        Command command = httpMapper.toCommand(update);
        log.info("TG Message: {}", command);
        try {
            BotMessage botMessage = messageHandler.getBotMessage(command).get();

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
                    if (botMessage.hasKeyboard()) {
                        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), message);
                        sendMessage.setReplyMarkup(httpMapper.toTgKeyboard(botMessage.getInnerKeyboard().get()));
                        execute(sendMessage);
                    } else {
                        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), message);
                        sendMessage.setReplyMarkup(httpMapper.toTgKeyboard(innerKeyboardGenerator.mainMenu(botMessage.getBotUser())));
                        execute(sendMessage);
                    }
                } else {
                    SendMessage sendMessage = new SendMessage(String.valueOf(chatId), message);
                    sendMessage.setReplyMarkup(replyKeyboardRemove);
                    execute(sendMessage);
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



    @Override
    public void send(BotMessage botMessage) {
        try {
            int startIndex = 0;
            String message;
            int lastSpaceIndex = 0;
            var userId = botMessage.getBotUser().getUserId();
            while (startIndex < botMessage.getMessage().length()) {
                if (botMessage.getMessage().length() - startIndex <= TG_LENGTH) {
                    message = botMessage.getMessage().substring(startIndex);
                } else {
                    lastSpaceIndex = botMessage.getMessage().lastIndexOf(' ', startIndex + TG_LENGTH);
                    message = botMessage.getMessage().substring(startIndex, lastSpaceIndex);
                }

                if (botMessage.getBotUser().isSentKeyboard()) {
                    if (botMessage.hasKeyboard()) {
                        SendMessage sendMessage = new SendMessage(String.valueOf(userId), message);
                        sendMessage.setReplyMarkup(httpMapper.toTgKeyboard(botMessage.getInnerKeyboard().get()));
                        execute(sendMessage);
                    } else {
                        SendMessage sendMessage = new SendMessage(String.valueOf(userId), message);
                        sendMessage.setReplyMarkup(httpMapper.toTgKeyboard(innerKeyboardGenerator.mainMenu(botMessage.getBotUser())));
                        execute(sendMessage);
                    }
                } else {
                    SendMessage sendMessage = new SendMessage(String.valueOf(userId), message);
                    sendMessage.setReplyMarkup(replyKeyboardRemove);
                    execute(sendMessage);
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
