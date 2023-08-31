package com.scribassu.scribabot.model;

import com.scribassu.scribabot.entities.users.TgBotUser;
import com.scribassu.scribabot.entities.users.VkBotUser;
import com.scribassu.scribabot.text.CommandText;
import com.scribassu.tracto.dto.EducationForm;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BotUser {

    private BotUserSource source;
    private String userId;
    private String department;
    private String groupNumber;
    private String educationForm;
    private String previousUserMessage;
    private boolean filterNomDenom;
    private boolean silentEmptyDays;
    private boolean sentKeyboard;

    public BotUser() {
        this.sentKeyboard = true;
        this.silentEmptyDays = false;
        this.filterNomDenom = false;
    }

    public BotUser(BotUserSource source, String userId) {
        this.source = source;
        this.userId = userId;
        this.sentKeyboard = true;
        this.silentEmptyDays = false;
        this.filterNomDenom = false;
    }

    public BotUser(VkBotUser vkBotUser) {
        this.source = BotUserSource.VK;
        this.userId = vkBotUser.getUserId();
        this.department = vkBotUser.getDepartment();
        this.educationForm = vkBotUser.getEducationForm();
        this.groupNumber = vkBotUser.getGroupNumber();
        this.previousUserMessage = vkBotUser.getPreviousUserMessage();
        this.filterNomDenom = vkBotUser.isFilterNomDenom();
        this.silentEmptyDays = vkBotUser.isSilentEmptyDays();
        this.sentKeyboard = vkBotUser.isSentKeyboard();
    }

    public BotUser(TgBotUser tgBotUser) {
        this.source = BotUserSource.TG;
        this.userId = tgBotUser.getUserId();
        this.department = tgBotUser.getDepartment();
        this.educationForm = tgBotUser.getEducationForm();
        this.groupNumber = tgBotUser.getGroupNumber();
        this.previousUserMessage = tgBotUser.getPreviousUserMessage();
        this.filterNomDenom = tgBotUser.isFilterNomDenom();
        this.silentEmptyDays = tgBotUser.isSilentEmptyDays();
        this.sentKeyboard = tgBotUser.isSentKeyboard();
    }

    public boolean fromVk() {
        return BotUserSource.VK.equals(this.source);
    }

    public boolean wantTeacherSchedule() {
        return null != this.previousUserMessage && this.previousUserMessage.startsWith(CommandText.TEACHER_PREFIX);
    }

    public static boolean isBotUserFullTime(BotUser botUser) {
        return botUser != null
                && botUser.getEducationForm() != null
                && EducationForm.DO.getGroupType().equalsIgnoreCase(botUser.getEducationForm())
                && !botUser.getGroupNumber().isBlank();
    }

    public static boolean isBotUserExtramural(BotUser botUser) {
        return botUser != null
                && botUser.getEducationForm() != null
                && EducationForm.ZO.getGroupType().equalsIgnoreCase(botUser.getEducationForm())
                && !botUser.getGroupNumber().isBlank();
    }
}
