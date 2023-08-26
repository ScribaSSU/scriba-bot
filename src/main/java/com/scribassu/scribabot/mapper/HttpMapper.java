package com.scribassu.scribabot.mapper;

import com.scribassu.scribabot.model.Command;
import com.scribassu.scribabot.model.inner_keyboard.InnerKeyboard;
import com.scribassu.scribabot.model.inner_keyboard.InnerKeyboardButton;
import com.scribassu.scribabot.model.inner_keyboard.KeyboardEmoji;
import com.scribassu.scribabot.dto.vk.VkMessageDto;
import com.scribassu.scribabot.dto.vkkeyboard.*;
import com.scribassu.scribabot.text.CommandText;
import com.scribassu.scribabot.util.BotUserSource;
import com.scribassu.scribabot.util.Constants;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import static com.scribassu.scribabot.util.Constants.DEFAULT_PAYLOAD;

@Component
@Data
public class HttpMapper {

    public Command toCommand(VkMessageDto incomingMessage) {
        var messageObject = incomingMessage.getObject();
        var payload = messageObject.getPayload();
        if (payload.startsWith(Constants.PAYLOAD_START) && payload.endsWith(Constants.PAYLOAD_END)) {
            payload = payload.substring(Constants.PAYLOAD_START.length());
            payload = payload.substring(0, payload.indexOf(Constants.PAYLOAD_END)).toLowerCase(Locale.ROOT);
        }

        var message = removeEmoji(incomingMessage.getObject().getText()).toLowerCase(Locale.ROOT);

        var attachments = messageObject.getAttachments();

        if (!attachments.isEmpty()) {
           for (var attachment : attachments) {
               if (attachment.getType().equals(Constants.KEY_STICKER)) {
                   message = CommandText.STICKER_WAS_SENT_TO_BOT;
                   break;
               }
           }
        }

        return new Command(message, payload.toLowerCase(Locale.ROOT), String.valueOf(incomingMessage.getObject().getPeerId()), BotUserSource.VK);
    }

    public Command toCommand(Update update) {
        return new Command(removeEmoji(update.getMessage().getText()).toLowerCase(Locale.ROOT),
                "",
                String.valueOf(update.getMessage().getFrom().getId()),
                BotUserSource.TG);
    }

    public VkKeyboard toVkKeyboard(InnerKeyboard innerKeyboard) {
        return new VkKeyboard(innerKeyboard.getButtons()
                .stream()
                .map(buttonRow ->
                        buttonRow
                                .stream()
                                .map(this::toVkKeyboardButton)
                                .collect(Collectors.toList()))
                .collect(Collectors.toList()),
                false);
    }

    public ReplyKeyboardMarkup toTgKeyboard(InnerKeyboard innerKeyboard) {
        var replyKeyboardMarkup = new ReplyKeyboardMarkup();
        var rows = new ArrayList<KeyboardRow>();
        var buttons = innerKeyboard.getButtons();
        for (var i = 0; i < buttons.size(); i++) {
            rows.add(new KeyboardRow());
            for (InnerKeyboardButton button : buttons.get(i)) {
                rows.get(i).add(toTgKeyboardButton(button));
            }
        }
        replyKeyboardMarkup.setKeyboard(rows);
        return replyKeyboardMarkup;
    }

    public VkKeyboardButton toVkKeyboardButton(InnerKeyboardButton innerKeyboardButton) {
        return new VkKeyboardButton(
                new VkKeyboardButtonActionText(
                        innerKeyboardButton.getText(),
                        innerKeyboardButton.getPayload().orElse(DEFAULT_PAYLOAD),
                        VkKeyboardButtonActionType.TEXT
                ), VkKeyboardButtonColor.PRIMARY);
    }

    public KeyboardButton toTgKeyboardButton(InnerKeyboardButton innerKeyboardButton) {
        return new KeyboardButton(innerKeyboardButton.getText());
    }

    private String removeEmoji(String text) {
        for (String emoji : KeyboardEmoji.KEYBOARD_EMOJIS)
            text = text.replace(emoji, "");
        return text.trim();
    }
}
