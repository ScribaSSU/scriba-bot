package com.scribassu.scribabot.dto;

import com.scribassu.scribabot.entities.TgBotUser;
import com.scribassu.scribabot.entities.VkBotUser;
import com.scribassu.scribabot.util.BotUserSource;
import com.scribassu.scribabot.util.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InnerBotUser {

    private BotUserSource source;
    private String userId;
    private String department;
    private String groupNumber;
    private String educationForm;
    private String previousUserMessage;
    private boolean filterNomDenom;
    private boolean silentEmptyDays;

    public InnerBotUser(BotUserSource source) {
        this.source = source;
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
}
