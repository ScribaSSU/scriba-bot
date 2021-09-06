package com.scribassu.scribabot.dto;

import com.scribassu.scribabot.dto.vkkeyboard.VkKeyboard;
import com.scribassu.scribabot.generators.TgKeyboardGenerator;
import com.scribassu.scribabot.generators.VkKeyboardGenerator;
import com.scribassu.scribabot.text.MessageText;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Data
@NoArgsConstructor
public class BotMessage {

    private String message;

    private VkKeyboard vkKeyboard;

    private ReplyKeyboardMarkup tgKeyboard;

    private InnerBotUser botUser;

    public BotMessage(String message) {
        this.message = message;
    }

    public BotMessage(String message, VkKeyboard vkKeyboard) {
        this.message = message;
        this.vkKeyboard = vkKeyboard;
    }

    public BotMessage(String message, ReplyKeyboardMarkup tgKeyboard) {
        this.message = message;
        this.tgKeyboard = tgKeyboard;
    }

    public BotMessage(String message, VkKeyboard vkKeyboard, ReplyKeyboardMarkup tgKeyboard) {
        this.message = message;
        this.vkKeyboard = vkKeyboard;
        this.tgKeyboard = tgKeyboard;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setBotUser(InnerBotUser botUser) {
        this.botUser = botUser;
    }

    public InnerBotUser getBotUser() {
        return botUser;
    }

    public boolean isEmpty() {
        return StringUtils.isEmpty(message);
    }

    public boolean isDefault() {
        return message.equalsIgnoreCase(MessageText.DEFAULT_MESSAGE)
                && (
                VkKeyboardGenerator.mainMenu.equals(vkKeyboard) || TgKeyboardGenerator.mainMenu().equals(tgKeyboard)
        );
    }

    public boolean hasVkKeyboard() {
        return null != this.vkKeyboard;
    }

    public boolean hasTgKeyboard() {
        return null != this.tgKeyboard;
    }
}

