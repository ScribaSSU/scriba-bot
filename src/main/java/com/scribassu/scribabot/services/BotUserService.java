package com.scribassu.scribabot.services;

import com.scribassu.scribabot.entities.notifications.NotificationType;
import com.scribassu.scribabot.entities.notifications.ScheduleNotification;
import com.scribassu.scribabot.entities.users.TgBotUser;
import com.scribassu.scribabot.entities.users.VkBotUser;
import com.scribassu.scribabot.model.BotUser;
import com.scribassu.scribabot.model.BotUserSource;
import com.scribassu.scribabot.model.Command;
import com.scribassu.scribabot.model.RegisteredUserResult;
import com.scribassu.scribabot.repositories.notifications.ScheduleNotificationRepository;
import com.scribassu.scribabot.repositories.users.TgBotUserRepository;
import com.scribassu.scribabot.repositories.users.VkBotUserRepository;
import com.scribassu.scribabot.util.DepartmentConverter;
import com.scribassu.tracto.dto.EducationForm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.scribassu.scribabot.entities.notifications.NotificationType.*;
import static com.scribassu.scribabot.text.MessageText.GREETING_WITH_CHOOSE_DEPARTMENT;

@Service
@Slf4j
@Data
public class BotUserService {

    @Value("${scheduled.default-hour-for-send}")
    private int defaultHourForSend;

    private final VkBotUserRepository vkBotUserRepository;
    private final TgBotUserRepository tgBotUserRepository;

    private final ScheduleNotificationRepository scheduleNotificationRepository;

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

        saveScheduleNotification(userId, source, FULL_TIME_TODAY);
        saveScheduleNotification(userId, source, FULL_TIME_TOMORROW);

        saveScheduleNotification(userId, source, EXAM_PERIOD_TODAY);
        saveScheduleNotification(userId, source, EXAM_PERIOD_TOMORROW);
        saveScheduleNotification(userId, source, EXAM_PERIOD_AFTER_TOMORROW);

        saveScheduleNotification(userId, source, EXTRAMURAL_EVENT_TODAY);
        saveScheduleNotification(userId, source, EXTRAMURAL_EVENT_TOMORROW);
        saveScheduleNotification(userId, source, EXTRAMURAL_EVENT_AFTER_TOMORROW);
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
        botUser.setPreviousUserMessage(message);
    }

    public void updateEducationForm(EducationForm educationForm, BotUser botUser) {
        var educationFormType = educationForm.getGroupType();
        if (botUser.fromVk()) {
            vkBotUserRepository.updateEducationForm(educationFormType, botUser.getUserId());
        } else {
            tgBotUserRepository.updateEducationForm(educationFormType, botUser.getUserId());
        }
        botUser.setEducationForm(educationFormType);
    }

    public void updateDepartment(String message, BotUser botUser) {
        var department = DepartmentConverter.convertToUrl(message);
        if (botUser.fromVk()) {
            vkBotUserRepository.updateDepartment(department, botUser.getUserId());
        } else {
            tgBotUserRepository.updateDepartment(department, botUser.getUserId());
        }
        botUser.setDepartment(department);
    }

    public void updateGroupNumber(String message, BotUser botUser) {
        if (botUser.fromVk()) {
            vkBotUserRepository.updateGroupNumber(message, botUser.getUserId());
        } else {
            tgBotUserRepository.updateGroupNumber(message, botUser.getUserId());
        }
        botUser.setGroupNumber(message);
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
        botUser.setFilterNomDenom(filter);
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
        botUser.setSentKeyboard(flag);
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
        botUser.setSilentEmptyDays(flag);
    }

    public void delete(BotUser botUser) {
        var userId = botUser.getUserId();
        var botUserSource = botUser.getSource();

        if (botUser.fromVk()) {
            vkBotUserRepository.deleteOneById(userId);
        } else {
            tgBotUserRepository.deleteOneById(userId);
        }

        deleteScheduleNotification(userId, botUserSource, FULL_TIME_TODAY);
        deleteScheduleNotification(userId, botUserSource, FULL_TIME_TOMORROW);

        deleteScheduleNotification(userId, botUserSource, EXAM_PERIOD_TODAY);
        deleteScheduleNotification(userId, botUserSource, EXAM_PERIOD_TOMORROW);
        deleteScheduleNotification(userId, botUserSource, EXAM_PERIOD_AFTER_TOMORROW);

        deleteScheduleNotification(userId, botUserSource, EXTRAMURAL_EVENT_TODAY);
        deleteScheduleNotification(userId, botUserSource, EXTRAMURAL_EVENT_TOMORROW);
        deleteScheduleNotification(userId, botUserSource, EXTRAMURAL_EVENT_AFTER_TOMORROW);
    }

    private void saveScheduleNotification(String userId, BotUserSource source, NotificationType notificationType) {
        if (scheduleNotificationRepository.findByUserIdAndUserSource(userId, source, notificationType).isEmpty()) {
            scheduleNotificationRepository.save(new ScheduleNotification(userId, source, false, defaultHourForSend, notificationType));
        }
    }

    private void deleteScheduleNotification(String userId, BotUserSource source, NotificationType notificationType) {
        scheduleNotificationRepository.deleteByUserIdAndUserSource(userId, source, notificationType);
    }
}
