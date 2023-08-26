package com.scribassu.scribabot.model;

import com.scribassu.scribabot.entities.users.TgBotUser;
import com.scribassu.scribabot.entities.users.VkBotUser;
import com.scribassu.scribabot.util.BotUserSource;
import com.scribassu.scribabot.util.Constants;
import com.scribassu.tracto.domain.EducationForm;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InnerBotUser {

    private BotUserSource source;
    private String userId;
    private String department;
    private String groupNumber;
    private String educationForm;
    private String previousUserMessage;
    private boolean filterNomDenom;
    private boolean silentEmptyDays;
    private boolean sentKeyboard;

    public InnerBotUser() {
        this.sentKeyboard = true;
        this.silentEmptyDays = false;
        this.filterNomDenom = false;
    }

    public InnerBotUser(BotUserSource source, String userId) {
        this.source = source;
        this.userId = userId;
        this.sentKeyboard = true;
        this.silentEmptyDays = false;
        this.filterNomDenom = false;
    }

    public InnerBotUser(VkBotUser vkBotUser) {
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

    public InnerBotUser(TgBotUser tgBotUser) {
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

    public boolean registered() {
        return null != this.userId && !this.userId.isEmpty();
    }

    public boolean notRegistered() {
        return !registered();
    }

    public boolean wantTeacherSchedule() {
        return this.previousUserMessage.startsWith(Constants.TEACHER_ID);
    }

    public static boolean isBotUserFullTime(InnerBotUser botUser) {
        return botUser != null
                && botUser.getEducationForm() != null
                && EducationForm.DO.getGroupType().equalsIgnoreCase(botUser.getEducationForm())
                && !botUser.getGroupNumber().isBlank();
    }

    public static boolean isBotUserExtramural(InnerBotUser botUser) {
        return botUser != null
                && botUser.getEducationForm() != null
                && EducationForm.ZO.getGroupType().equalsIgnoreCase(botUser.getEducationForm())
                && !botUser.getGroupNumber().isBlank();
    }
}
