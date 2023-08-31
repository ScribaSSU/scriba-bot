package com.scribassu.scribabot.mapper;

import com.scribassu.scribabot.dto.vk.VkMessageDto;
import com.scribassu.scribabot.dto.vkkeyboard.*;
import com.scribassu.scribabot.model.BotUserSource;
import com.scribassu.scribabot.model.Command;
import com.scribassu.scribabot.model.keyboard.Keyboard;
import com.scribassu.scribabot.model.keyboard.KeyboardButton;
import com.scribassu.scribabot.model.keyboard.KeyboardEmoji;
import com.scribassu.scribabot.text.CommandText;
import com.scribassu.scribabot.util.Constants;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Collectors;

import static com.scribassu.scribabot.util.Constants.DEFAULT_PAYLOAD;

@Component
@Data
public class HttpMapper {

    public Command toCommand(VkMessageDto incomingMessage) {
        var messageObject = incomingMessage.getObject();

        var message = removeEmoji(incomingMessage.getObject().getText()).toLowerCase(Locale.ROOT);

        var attachments = messageObject.getAttachments();

        if (null != attachments && !attachments.isEmpty()) {
           for (var attachment : attachments) {
               if (attachment.getType().equals(Constants.KEY_STICKER)) {
                   message = CommandText.STICKER_WAS_SENT_TO_BOT;
                   break;
               }
           }
        }

        return new Command(message, String.valueOf(incomingMessage.getObject().getPeerId()), BotUserSource.VK);
    }

    public Command toCommand(Update update) {
        return new Command(removeEmoji(update.getMessage().getText()).toLowerCase(Locale.ROOT),
                String.valueOf(update.getMessage().getChatId()),
                BotUserSource.TG);
    }

    public VkKeyboard toVkKeyboard(Keyboard keyboard) {
        return new VkKeyboard(keyboard.getButtons()
                .stream()
                .map(buttonRow ->
                        buttonRow
                                .stream()
                                .map(this::toVkKeyboardButton)
                                .collect(Collectors.toList()))
                .collect(Collectors.toList()),
                false);
    }

    public ReplyKeyboardMarkup toTgKeyboard(Keyboard keyboard) {
        var replyKeyboardMarkup = new ReplyKeyboardMarkup();
        var rows = new ArrayList<KeyboardRow>();
        var buttons = keyboard.getButtons();
        for (var i = 0; i < buttons.size(); i++) {
            rows.add(new KeyboardRow());
            for (KeyboardButton button : buttons.get(i)) {
                rows.get(i).add(toTgKeyboardButton(button));
            }
        }
        replyKeyboardMarkup.setKeyboard(rows);
        return replyKeyboardMarkup;
    }

    public VkKeyboardButton toVkKeyboardButton(KeyboardButton keyboardButton) {
        return new VkKeyboardButton(
                new VkKeyboardButtonActionText(
                        keyboardButton.getText(),
                        DEFAULT_PAYLOAD,
                        VkKeyboardButtonActionType.TEXT
                ), VkKeyboardButtonColor.PRIMARY);
    }

    public org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton toTgKeyboardButton(KeyboardButton keyboardButton) {
        return new org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton(keyboardButton.getText());
    }

    private String removeEmoji(String text) {
        for (String emoji : KeyboardEmoji.KEYBOARD_EMOJIS)
            text = text.replace(emoji, "");
        return text.trim();
    }
}
