package com.scribassu.scribabot.services.bot;

import com.scribassu.scribabot.entities.users.TgBotUser;
import com.scribassu.scribabot.entities.users.VkBotUser;
import com.scribassu.scribabot.model.Command;
import com.scribassu.scribabot.model.InnerBotUser;
import com.scribassu.scribabot.model.RegisteredUserResult;
import com.scribassu.scribabot.repositories.notifications.*;
import com.scribassu.scribabot.repositories.users.TgBotUserRepository;
import com.scribassu.scribabot.repositories.users.VkBotUserRepository;
import com.scribassu.scribabot.util.BotUserSource;
import com.scribassu.scribabot.util.DepartmentConverter;
import com.scribassu.tracto.domain.EducationForm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.scribassu.scribabot.text.MessageText.GREETING_WITH_CHOOSE_DEPARTMENT;

@Service
@Slf4j
@Data
public class BotUserService {

    private final VkBotUserRepository vkBotUserRepository;
    private final TgBotUserRepository tgBotUserRepository;

    private final ScheduleTodayNotificationRepository scheduleTodayNotificationRepository;
    private final ScheduleTomorrowNotificationRepository scheduleTomorrowNotificationRepository;
    private final ExamPeriodTodayNotificationRepository examPeriodTodayNotificationRepository;
    private final ExamPeriodTomorrowNotificationRepository examPeriodTomorrowNotificationRepository;
    private final ExamPeriodAfterTomorrowNotificationRepository examPeriodAfterTomorrowNotificationRepository;
    private final ExtramuralEventTodayNotificationRepository extramuralEventTodayNotificationRepository;
    private final ExtramuralEventTomorrowNotificationRepository extramuralEventTomorrowNotificationRepository;
    private final ExtramuralEventAfterTomorrowNotificationRepository extramuralEventAfterTomorrowNotificationRepository;

    public RegisteredUserResult isBotUserRegistered(Command command) {
        String userId = command.getUserId();
        BotUserSource source = command.getBotUserSource();

        boolean registered = false;
        InnerBotUser botUser;
        VkBotUser vkBotUser;
        TgBotUser tgBotUser;
        if (BotUserSource.VK.equals(source)) {
            vkBotUser = vkBotUserRepository.findOneById(userId);
            if (null == vkBotUser) {
                botUser = new InnerBotUser(source, userId);
            } else {
                botUser = new InnerBotUser(vkBotUser);
                registered = true;
            }
        } else {
            tgBotUser = tgBotUserRepository.findOneById(userId);
            if (null == tgBotUser) {
                botUser = new InnerBotUser(source, userId);
            } else {
                botUser = new InnerBotUser(tgBotUser);
                registered = true;
            }
        }
        return new RegisteredUserResult(registered, botUser);
    }

    // todo add notifications insert
    public void registerUser(InnerBotUser innerBotUser) {
        var userId = innerBotUser.getUserId();
        if (innerBotUser.fromVk()) {
            var vkBotUser = new VkBotUser(userId);
            vkBotUser = vkBotUserRepository.save(vkBotUser);
            vkBotUserRepository.updatePreviousUserMessage(GREETING_WITH_CHOOSE_DEPARTMENT, vkBotUser.getUserId());
        } else {
            var tgBotUser = new TgBotUser(userId);
            tgBotUser = tgBotUserRepository.save(tgBotUser);
            tgBotUserRepository.updatePreviousUserMessage(GREETING_WITH_CHOOSE_DEPARTMENT, tgBotUser.getUserId());
        }
    }

    public void resetPreviousUserMessage(InnerBotUser innerBotUser) {
        updatePreviousUserMessage("", innerBotUser);
    }

    public void updatePreviousUserMessage(String message, InnerBotUser innerBotUser) {
        if (innerBotUser.fromVk()) {
            vkBotUserRepository.updatePreviousUserMessage(message, innerBotUser.getUserId());
        } else {
            tgBotUserRepository.updatePreviousUserMessage(message, innerBotUser.getUserId());
        }
    }

    public void updateEducationForm(EducationForm educationForm, InnerBotUser innerBotUser) {
        if (innerBotUser.fromVk()) {
            vkBotUserRepository.updateEducationForm(educationForm.getGroupType(), innerBotUser.getUserId());
        } else {
            tgBotUserRepository.updateEducationForm(educationForm.getGroupType(), innerBotUser.getUserId());
        }
    }

    public void updateDepartment(String message, InnerBotUser innerBotUser) {
        if (innerBotUser.fromVk()) {
            vkBotUserRepository.updateDepartment(DepartmentConverter.convertToUrl(message), innerBotUser.getUserId());
        } else {
            tgBotUserRepository.updateDepartment(DepartmentConverter.convertToUrl(message), innerBotUser.getUserId());
        }
    }

    public void updateGroupNumber(String message, InnerBotUser innerBotUser) {
        if (innerBotUser.fromVk()) {
            vkBotUserRepository.updateGroupNumber(message, innerBotUser.getUserId());
        } else {
            tgBotUserRepository.updateGroupNumber(message, innerBotUser.getUserId());
        }
    }

    public void setFilterNomDenom(boolean filter, InnerBotUser innerBotUser) {
        if (innerBotUser.fromVk()) {
            VkBotUser vkBotUser = vkBotUserRepository.findOneById(innerBotUser.getUserId());
            vkBotUser.setFilterNomDenom(filter);
            vkBotUserRepository.save(vkBotUser);
        } else {
            TgBotUser tgBotUser = tgBotUserRepository.findOneById(innerBotUser.getUserId());
            tgBotUser.setFilterNomDenom(true);
            tgBotUserRepository.save(tgBotUser);
        }
    }

    public void setSentKeyboard(boolean flag, InnerBotUser innerBotUser) {
        if (innerBotUser.fromVk()) {
            VkBotUser vkBotUser = vkBotUserRepository.findOneById(innerBotUser.getUserId());
            vkBotUser.setSentKeyboard(flag);
            vkBotUserRepository.save(vkBotUser);
        } else {
            TgBotUser tgBotUser = tgBotUserRepository.findOneById(innerBotUser.getUserId());
            tgBotUser.setSentKeyboard(flag);
            tgBotUserRepository.save(tgBotUser);
        }
    }

    public boolean isSentKeyboard(String userId, BotUserSource botUserSource) {
        if (BotUserSource.VK.equals(botUserSource)) {
            return vkBotUserRepository.findOneById(userId).isSentKeyboard();
        } else {
            return tgBotUserRepository.findOneById(userId).isSentKeyboard();
        }
    }

    public void setSilentEmptyDays(boolean flag, InnerBotUser innerBotUser) {
        if (innerBotUser.fromVk()) {
            VkBotUser vkBotUser = vkBotUserRepository.findOneById(innerBotUser.getUserId());
            vkBotUser.setSilentEmptyDays(flag);
            vkBotUserRepository.save(vkBotUser);
        } else {
            TgBotUser tgBotUser = tgBotUserRepository.findOneById(innerBotUser.getUserId());
            tgBotUser.setSilentEmptyDays(flag);
            tgBotUserRepository.save(tgBotUser);
        }
    }

    public void delete(InnerBotUser innerBotUser) {
        var userId = innerBotUser.getUserId();
        var botUserSource = innerBotUser.getSource();

        if (innerBotUser.fromVk()) {
            vkBotUserRepository.deleteOneById(userId);
        } else {
            tgBotUserRepository.deleteOneById(userId);
        }
        if (InnerBotUser.isBotUserExtramural(innerBotUser)) {
            extramuralEventTodayNotificationRepository.deleteByUserIdAndUserSource(userId, botUserSource);
            extramuralEventTomorrowNotificationRepository.deleteByUserIdAndUserSource(userId, botUserSource);
            extramuralEventAfterTomorrowNotificationRepository.deleteByUserIdAndUserSource(userId, botUserSource);
        } else {
            examPeriodTodayNotificationRepository.deleteByUserIdAndUserSource(userId, botUserSource);
            examPeriodTomorrowNotificationRepository.deleteByUserIdAndUserSource(userId, botUserSource);
            examPeriodAfterTomorrowNotificationRepository.deleteByUserIdAndUserSource(userId, botUserSource);
            scheduleTodayNotificationRepository.deleteByUserIdAndUserSource(userId, botUserSource);
            scheduleTomorrowNotificationRepository.deleteByUserIdAndUserSource(userId, botUserSource);
        }
    }
}
