package com.scribassu.scribabot.dto;

import com.scribassu.scribabot.entities.BotUser;
import com.scribassu.scribabot.entities.TgBotUser;
import com.scribassu.scribabot.entities.VkBotUser;
import com.scribassu.scribabot.text.CommandText;
import com.scribassu.scribabot.util.BotUserSource;
import javassist.runtime.Inner;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;

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
    }

    public InnerBotUser(TgBotUser tgBotUser) {
        this.source = BotUserSource.TG;
        this.userId = tgBotUser.getUserId();
        this.department = tgBotUser.getDepartment();
        this.educationForm = tgBotUser.getEducationForm();
        this.groupNumber = tgBotUser.getGroupNumber();
        this.previousUserMessage = tgBotUser.getPreviousUserMessage();
        this.filterNomDenom = tgBotUser.isFilterNomDenom();
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
        return this.previousUserMessage.startsWith(CommandText.TEACHER_ID_PAYLOAD);
    }
}
