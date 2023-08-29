package com.scribassu.scribabot.services;

import com.scribassu.scribabot.entities.notifications.*;
import com.scribassu.scribabot.entities.users.TgBotUser;
import com.scribassu.scribabot.entities.users.VkBotUser;
import com.scribassu.scribabot.model.Command;
import com.scribassu.scribabot.model.BotUser;
import com.scribassu.scribabot.model.RegisteredUserResult;
import com.scribassu.scribabot.repositories.notifications.*;
import com.scribassu.scribabot.repositories.users.TgBotUserRepository;
import com.scribassu.scribabot.repositories.users.VkBotUserRepository;
import com.scribassu.scribabot.model.BotUserSource;
import com.scribassu.scribabot.util.DepartmentConverter;
import com.scribassu.tracto.domain.EducationForm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.scribassu.scribabot.text.MessageText.GREETING_WITH_CHOOSE_DEPARTMENT;

@Service
@Slf4j
@Data
public class BotUserService {

    @Value("${scheduled.default-hour-for-send}")
    private int defaultHourForSend;

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
        BotUser botUser;
        VkBotUser vkBotUser;
        TgBotUser tgBotUser;
        if (BotUserSource.VK.equals(source)) {
            vkBotUser = vkBotUserRepository.findOneById(userId);
            if (null == vkBotUser) {
                botUser = new BotUser(source, userId);
            } else {
                botUser = new BotUser(vkBotUser);
                registered = true;
            }
        } else {
            tgBotUser = tgBotUserRepository.findOneById(userId);
            if (null == tgBotUser) {
                botUser = new BotUser(source, userId);
            } else {
                botUser = new BotUser(tgBotUser);
                registered = true;
            }
        }
        return new RegisteredUserResult(registered, botUser);
    }

    public void registerUser(BotUser botUser) {
        var userId = botUser.getUserId();
        var source = botUser.getSource();
        if (botUser.fromVk()) {
            var vkBotUser = new VkBotUser(userId);
            vkBotUser = vkBotUserRepository.save(vkBotUser);
            vkBotUserRepository.updatePreviousUserMessage(GREETING_WITH_CHOOSE_DEPARTMENT, vkBotUser.getUserId());
        } else {
            var tgBotUser = new TgBotUser(userId);
            tgBotUser = tgBotUserRepository.save(tgBotUser);
            tgBotUserRepository.updatePreviousUserMessage(GREETING_WITH_CHOOSE_DEPARTMENT, tgBotUser.getUserId());
        }

        scheduleTodayNotificationRepository.save(new ScheduleTodayNotification(userId, source, false, defaultHourForSend));
        scheduleTomorrowNotificationRepository.save(new ScheduleTomorrowNotification(userId, source, false, defaultHourForSend));
        examPeriodTodayNotificationRepository.save(new ExamPeriodTodayNotification(userId, source, false, defaultHourForSend));
        examPeriodTomorrowNotificationRepository.save(new ExamPeriodTomorrowNotification(userId, source, false, defaultHourForSend));
        examPeriodAfterTomorrowNotificationRepository.save(new ExamPeriodAfterTomorrowNotification(userId, source, false, defaultHourForSend));
        extramuralEventTodayNotificationRepository.save(new ExtramuralEventTodayNotification(userId, source, false, defaultHourForSend));
        extramuralEventTomorrowNotificationRepository.save(new ExtramuralEventTomorrowNotification(userId, source, false, defaultHourForSend));
        extramuralEventAfterTomorrowNotificationRepository.save(new ExtramuralEventAfterTomorrowNotification(userId, source, false, defaultHourForSend));
    }

    public void resetPreviousUserMessage(BotUser botUser) {
        updatePreviousUserMessage("", botUser);
    }

    public void updatePreviousUserMessage(String message, BotUser botUser) {
        if (botUser.fromVk()) {
            vkBotUserRepository.updatePreviousUserMessage(message, botUser.getUserId());
        } else {
            tgBotUserRepository.updatePreviousUserMessage(message, botUser.getUserId());
        }
    }

    public void updateEducationForm(EducationForm educationForm, BotUser botUser) {
        if (botUser.fromVk()) {
            vkBotUserRepository.updateEducationForm(educationForm.getGroupType(), botUser.getUserId());
        } else {
            tgBotUserRepository.updateEducationForm(educationForm.getGroupType(), botUser.getUserId());
        }
    }

    public void updateDepartment(String message, BotUser botUser) {
        if (botUser.fromVk()) {
            vkBotUserRepository.updateDepartment(DepartmentConverter.convertToUrl(message), botUser.getUserId());
        } else {
            tgBotUserRepository.updateDepartment(DepartmentConverter.convertToUrl(message), botUser.getUserId());
        }
    }

    public void updateGroupNumber(String message, BotUser botUser) {
        if (botUser.fromVk()) {
            vkBotUserRepository.updateGroupNumber(message, botUser.getUserId());
        } else {
            tgBotUserRepository.updateGroupNumber(message, botUser.getUserId());
        }
    }

    public void setFilterNomDenom(boolean filter, BotUser botUser) {
        if (botUser.fromVk()) {
            VkBotUser vkBotUser = vkBotUserRepository.findOneById(botUser.getUserId());
            vkBotUser.setFilterNomDenom(filter);
            vkBotUserRepository.save(vkBotUser);
        } else {
            TgBotUser tgBotUser = tgBotUserRepository.findOneById(botUser.getUserId());
            tgBotUser.setFilterNomDenom(true);
            tgBotUserRepository.save(tgBotUser);
        }
    }

    public void setSentKeyboard(boolean flag, BotUser botUser) {
        if (botUser.fromVk()) {
            VkBotUser vkBotUser = vkBotUserRepository.findOneById(botUser.getUserId());
            vkBotUser.setSentKeyboard(flag);
            vkBotUserRepository.save(vkBotUser);
        } else {
            TgBotUser tgBotUser = tgBotUserRepository.findOneById(botUser.getUserId());
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

    public void setSilentEmptyDays(boolean flag, BotUser botUser) {
        if (botUser.fromVk()) {
            VkBotUser vkBotUser = vkBotUserRepository.findOneById(botUser.getUserId());
            vkBotUser.setSilentEmptyDays(flag);
            vkBotUserRepository.save(vkBotUser);
        } else {
            TgBotUser tgBotUser = tgBotUserRepository.findOneById(botUser.getUserId());
            tgBotUser.setSilentEmptyDays(flag);
            tgBotUserRepository.save(tgBotUser);
        }
    }

    public void delete(BotUser botUser) {
        var userId = botUser.getUserId();
        var botUserSource = botUser.getSource();

        if (botUser.fromVk()) {
            vkBotUserRepository.deleteOneById(userId);
        } else {
            tgBotUserRepository.deleteOneById(userId);
        }
        extramuralEventTodayNotificationRepository.deleteByUserIdAndUserSource(userId, botUserSource);
        extramuralEventTomorrowNotificationRepository.deleteByUserIdAndUserSource(userId, botUserSource);
        extramuralEventAfterTomorrowNotificationRepository.deleteByUserIdAndUserSource(userId, botUserSource);
        examPeriodTodayNotificationRepository.deleteByUserIdAndUserSource(userId, botUserSource);
        examPeriodTomorrowNotificationRepository.deleteByUserIdAndUserSource(userId, botUserSource);
        examPeriodAfterTomorrowNotificationRepository.deleteByUserIdAndUserSource(userId, botUserSource);
        scheduleTodayNotificationRepository.deleteByUserIdAndUserSource(userId, botUserSource);
        scheduleTomorrowNotificationRepository.deleteByUserIdAndUserSource(userId, botUserSource);
    }
}
