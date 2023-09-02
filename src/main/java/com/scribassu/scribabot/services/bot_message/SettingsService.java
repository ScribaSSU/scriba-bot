package com.scribassu.scribabot.services.bot_message;

import com.scribassu.scribabot.entities.notifications.NotificationType;
import com.scribassu.scribabot.entities.notifications.ScheduleNotification;
import com.scribassu.scribabot.generators.InnerKeyboardGenerator;
import com.scribassu.scribabot.model.BotMessage;
import com.scribassu.scribabot.model.BotUser;
import com.scribassu.scribabot.model.BotUserSource;
import com.scribassu.scribabot.repositories.notifications.ScheduleNotificationRepository;
import com.scribassu.scribabot.services.BotMessageService;
import com.scribassu.scribabot.services.BotUserService;
import com.scribassu.scribabot.text.CommandText;
import com.scribassu.scribabot.util.DepartmentConverter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.scribassu.scribabot.entities.notifications.NotificationType.*;
import static com.scribassu.scribabot.text.MessageText.*;

// 1127
// 1038
// 984
// 734
// 595
// todo notification logic
@Service
@Slf4j
@Data
public class SettingsService implements BotMessageService {
    private final BotUserService botUserService;
    private final ScheduleNotificationRepository scheduleNotificationRepository;
    private final InnerKeyboardGenerator innerKeyboardGenerator;

    @Override
    public boolean shouldAccept(String message, BotUser botUser) {
        if (CommandText.HOUR_PATTERN.matcher(message).matches()) {
            return true;
        }

        switch (message) {
            case CommandText.SET_SEND_SCHEDULE_TIME_TODAY:
            case CommandText.ENABLE_SEND_SCHEDULE_TODAY:
            case CommandText.DISABLE_SEND_SCHEDULE_TODAY:
            case CommandText.SET_SEND_SCHEDULE_TIME_TOMORROW:
            case CommandText.ENABLE_SEND_SCHEDULE_TOMORROW:
            case CommandText.DISABLE_SEND_SCHEDULE_TOMORROW:
            case CommandText.SET_SEND_EXAM_PERIOD_TIME_TODAY:
            case CommandText.ENABLE_SEND_EXAM_PERIOD_TODAY:
            case CommandText.DISABLE_SEND_EXAM_PERIOD_TODAY:
            case CommandText.SET_SEND_EXAM_PERIOD_TIME_TOMORROW:
            case CommandText.ENABLE_SEND_EXAM_PERIOD_TOMORROW:
            case CommandText.DISABLE_SEND_EXAM_PERIOD_TOMORROW:
            case CommandText.SET_SEND_EXAM_PERIOD_TIME_AFTER_TOMORROW:
            case CommandText.ENABLE_SEND_EXAM_PERIOD_AFTER_TOMORROW:
            case CommandText.DISABLE_SEND_EXAM_PERIOD_AFTER_TOMORROW:
            case CommandText.SEND_EXAM_PERIOD:
            case CommandText.SEND_SCHEDULE:
            case CommandText.ENABLE_FILTER_WEEK_TYPE:
            case CommandText.DISABLE_FILTER_WEEK_TYPE:
            case CommandText.ENABLE_SEND_EMPTY_SCHEDULE_NOTIFICATION:
            case CommandText.DISABLE_SEND_EMPTY_SCHEDULE_NOTIFICATION:
            case CommandText.CURRENT_USER_SETTINGS:
            case CommandText.ENABLE_SEND_KEYBOARD:
            case CommandText.DISABLE_SEND_KEYBOARD:
            case CommandText.DELETE_PROFILE:
            case CommandText.YES_DELETE_PROFILE:
            case CommandText.NO_DELETE_PROFILE:
                return true;
            default: return false;
        }
    }

    @Override
    public CompletableFuture<BotMessage> getBotMessage(String message, BotUser botUser) {
        BotMessage botMessage = new BotMessage();

        if (forFullTimeScheduleTodayNotification(message)) {
            return processFullTimeScheduleTodayNotification(message, botUser);
        }

        if (forFullTimeScheduleTomorrowNotification(message)) {
            return processFullTimeScheduleTomorrowNotification(message, botUser);
        }

        switch (message) {
            case CommandText.SEND_EXAM_PERIOD:
                return CompletableFuture.completedFuture(new BotMessage(HERE_EXAM_PERIOD_NOTIFICATION, innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
            case CommandText.SEND_SCHEDULE:
                return CompletableFuture.completedFuture(new BotMessage(HERE_SCHEDULE_NOTIFICATION, innerKeyboardGenerator.settingsScheduleNotification(botUser), botUser));
            case CommandText.SET_SEND_EXAM_PERIOD_TIME_TODAY:
            case CommandText.ENABLE_SEND_EXAM_PERIOD_TODAY:
            case CommandText.DISABLE_SEND_EXAM_PERIOD_TODAY:
                return todayNotificationsExamPeriod(message, botUser);
            case CommandText.SET_SEND_EXAM_PERIOD_TIME_TOMORROW:
            case CommandText.ENABLE_SEND_EXAM_PERIOD_TOMORROW:
            case CommandText.DISABLE_SEND_EXAM_PERIOD_TOMORROW:
                return tomorrowNotificationsExamPeriod(message, botUser);
            case CommandText.SET_SEND_EXAM_PERIOD_TIME_AFTER_TOMORROW:
            case CommandText.ENABLE_SEND_EXAM_PERIOD_AFTER_TOMORROW:
            case CommandText.DISABLE_SEND_EXAM_PERIOD_AFTER_TOMORROW:
                return afterTomorrowNotificationsExamPeriod(message, botUser);
            case CommandText.ENABLE_FILTER_WEEK_TYPE:
                botUserService.setFilterNomDenom(true, botUser);
                return CompletableFuture.completedFuture(new BotMessage(ENABLE_FILTER_WEEK_TYPE, innerKeyboardGenerator.settings(botUser), botUser));
            case CommandText.DISABLE_FILTER_WEEK_TYPE:
                botUserService.setFilterNomDenom(false, botUser);
                return CompletableFuture.completedFuture(new BotMessage(DISABLE_FILTER_WEEK_TYPE, innerKeyboardGenerator.settings(botUser), botUser));
            case CommandText.ENABLE_SEND_KEYBOARD:
                botUserService.setSentKeyboard(true, botUser);
                return CompletableFuture.completedFuture(new BotMessage(ENABLE_SEND_KEYBOARD, innerKeyboardGenerator.settings(botUser), botUser));
            case CommandText.DISABLE_SEND_KEYBOARD:
                botUserService.setSentKeyboard(false, botUser);
                return CompletableFuture.completedFuture(new BotMessage(DISABLE_SEND_KEYBOARD, innerKeyboardGenerator.settings(botUser), botUser));
            case CommandText.ENABLE_SEND_EMPTY_SCHEDULE_NOTIFICATION:
                botUserService.setSilentEmptyDays(false, botUser);
                return CompletableFuture.completedFuture(new BotMessage(ENABLE_SEND_EMPTY_SCHEDULE_NOTIFICATION, innerKeyboardGenerator.settings(botUser), botUser));
            case CommandText.DISABLE_SEND_EMPTY_SCHEDULE_NOTIFICATION:
                botUserService.setSilentEmptyDays(true, botUser);
                return CompletableFuture.completedFuture(new BotMessage(DISABLE_SEND_EMPTY_SCHEDULE_NOTIFICATION, innerKeyboardGenerator.settings(botUser), botUser));
            case CommandText.CURRENT_USER_SETTINGS:
                return getCurrentUserSettings(botUser);
            case CommandText.DELETE_PROFILE:
                return CompletableFuture.completedFuture(new BotMessage(DELETE_CONFIRMATION, innerKeyboardGenerator.confirmDeletion(), botUser));
            case CommandText.NO_DELETE_PROFILE:
                return CompletableFuture.completedFuture(new BotMessage(NOT_BYE_MESSAGE, innerKeyboardGenerator.settings(botUser), botUser));
            case CommandText.YES_DELETE_PROFILE:
                botUserService.delete(botUser);
                return CompletableFuture.completedFuture(new BotMessage(BYE_MESSAGE, botUser));
        }

        if (CommandText.HOUR_PATTERN.matcher(message).matches()) {
            var hourForSend = Integer.parseInt(message.substring(0, message.indexOf(" ")));
            var previousMessage = botUser.getPreviousUserMessage();
            var notificationType = FULL_TIME_TODAY;

            if (previousMessage.equalsIgnoreCase(CHOOSE_SCHEDULE_NOTIFICATION_TIME_TODAY)) {
                notificationType = FULL_TIME_TODAY;
            }

            if (botUser.getPreviousUserMessage().equalsIgnoreCase(CHOOSE_SCHEDULE_NOTIFICATION_TIME_TOMORROW)) {
                notificationType = FULL_TIME_TOMORROW;
            }
            if (botUser.getPreviousUserMessage().equalsIgnoreCase(CHOOSE_EXAM_PERIOD_NOTIFICATION_TIME_TODAY)) {
                if (BotUser.isBotUserFullTime(botUser)) {
                    notificationType = EXAM_PERIOD_TODAY;
                } else {
                    notificationType = EXTRAMURAL_EVENT_TODAY;
                }
            }
            if (botUser.getPreviousUserMessage().equalsIgnoreCase(CHOOSE_EXAM_PERIOD_NOTIFICATION_TIME_TOMORROW)) {
                if (BotUser.isBotUserFullTime(botUser)) {
                    notificationType = EXAM_PERIOD_TOMORROW;
                } else {
                    notificationType = EXTRAMURAL_EVENT_TOMORROW;
                }
            }
            if (botUser.getPreviousUserMessage().equalsIgnoreCase(CHOOSE_EXAM_PERIOD_NOTIFICATION_TIME_AFTER_TOMORROW)) {
                if (BotUser.isBotUserFullTime(botUser)) {
                    notificationType = EXAM_PERIOD_AFTER_TOMORROW;
                } else {
                    notificationType = EXTRAMURAL_EVENT_AFTER_TOMORROW;
                }
            }
            upsertScheduleNotificationHourForSend(hourForSend, botUser, notificationType);
            final String hourMessage = SCHEDULE_WILL_BE_SENT_ABSTRACT + hourForSend + H_DOT;
            return CompletableFuture.completedFuture(new BotMessage(hourMessage, innerKeyboardGenerator.settings(botUser), botUser));
        }

        return CompletableFuture.completedFuture(botMessage);
    }

    private CompletableFuture<BotMessage> processFullTimeScheduleTodayNotification(String message, BotUser botUser) {
        BotMessage botMessage = new BotMessage();

        switch (message) {
            case CommandText.SET_SEND_SCHEDULE_TIME_TODAY:
                String formatSchedule = String.format(CHOOSE_SCHEDULE_NOTIFICATION_TIME, TODAY);
                botUserService.updatePreviousUserMessage(formatSchedule, botUser);
                return CompletableFuture.completedFuture(new BotMessage(formatSchedule, innerKeyboardGenerator.hours(), botUser));
            case CommandText.ENABLE_SEND_SCHEDULE_TODAY:
                var scheduleTodayNotificationOptionalEn = enableScheduleNotification(botUser, FULL_TIME_TODAY);
                if (scheduleTodayNotificationOptionalEn.isEmpty()) {
                    return CompletableFuture.completedFuture(new BotMessage(
                            NOT_ENABLE_SCHEDULE_NOTIFICATION_TODAY_FRMTD,
                            innerKeyboardGenerator.settingsScheduleNotification(botUser), botUser));
                } else {
                    return CompletableFuture.completedFuture(new BotMessage(
                            String.format(SCHEDULE_WILL_BE_SENT, TODAY) +
                                    scheduleTodayNotificationOptionalEn.get().getHourForSend() + H_DOT,
                            innerKeyboardGenerator.settingsScheduleNotification(botUser), botUser));
                }
            case CommandText.DISABLE_SEND_SCHEDULE_TODAY:
                var scheduleTodayNotificationOptionalDis = disableScheduleNotification(botUser, FULL_TIME_TODAY);
                if (scheduleTodayNotificationOptionalDis.isEmpty()) {
                    return CompletableFuture.completedFuture(new BotMessage(
                            NOT_ENABLE_SCHEDULE_NOTIFICATION_TODAY_FRMTD,
                            innerKeyboardGenerator.settingsScheduleNotification(botUser), botUser));
                } else {
                    return CompletableFuture.completedFuture(new BotMessage(
                            String.format(SCHEDULE_IS_DISABLED, TODAY),
                            innerKeyboardGenerator.settingsScheduleNotification(botUser), botUser));
                }
        }

        return CompletableFuture.completedFuture(botMessage);
    }

    private CompletableFuture<BotMessage> processFullTimeScheduleTomorrowNotification(String message, BotUser botUser) {
        BotMessage botMessage = new BotMessage();

        switch (message) {
            case CommandText.SET_SEND_SCHEDULE_TIME_TOMORROW:
                String formatSchedule = String.format(CHOOSE_SCHEDULE_NOTIFICATION_TIME, TOMORROW);
                botUserService.updatePreviousUserMessage(formatSchedule, botUser);
                return CompletableFuture.completedFuture(new BotMessage(formatSchedule, innerKeyboardGenerator.hours(), botUser));
            case CommandText.ENABLE_SEND_SCHEDULE_TOMORROW:
                var scheduleTomorrowNotificationOptionalEn = enableScheduleNotification(botUser, FULL_TIME_TOMORROW);
                if (scheduleTomorrowNotificationOptionalEn.isEmpty()) {
                    return CompletableFuture.completedFuture(new BotMessage(
                            NOT_ENABLE_SCHEDULE_NOTIFICATION_TOMORROW_FRMTD,
                            innerKeyboardGenerator.settingsScheduleNotification(botUser), botUser));
                } else {
                    return CompletableFuture.completedFuture(new BotMessage(
                            String.format(SCHEDULE_WILL_BE_SENT, TOMORROW) +
                                    scheduleTomorrowNotificationOptionalEn.get().getHourForSend() + H_DOT,
                            innerKeyboardGenerator.settingsScheduleNotification(botUser), botUser));
                }
            case CommandText.DISABLE_SEND_SCHEDULE_TOMORROW:
                var scheduleTomorrowNotificationOptionalDis = enableScheduleNotification(botUser, FULL_TIME_TOMORROW);
                if (scheduleTomorrowNotificationOptionalDis.isEmpty()) {
                    return CompletableFuture.completedFuture(new BotMessage(
                            NOT_ENABLE_SCHEDULE_NOTIFICATION_TOMORROW_FRMTD,
                            innerKeyboardGenerator.settingsScheduleNotification(botUser), botUser));
                } else {
                    return CompletableFuture.completedFuture(new BotMessage(
                            String.format(SCHEDULE_IS_DISABLED, TOMORROW),
                            innerKeyboardGenerator.settingsScheduleNotification(botUser), botUser));
                }
        }

        return CompletableFuture.completedFuture(botMessage);
    }

    private CompletableFuture<BotMessage> todayNotificationsExamPeriod(String message, BotUser botUser) {
        BotMessage botMessage = new BotMessage();
        String userId = botUser.getUserId();
        BotUserSource source = botUser.getSource();

        switch (message) {
            case CommandText.SET_SEND_EXAM_PERIOD_TIME_TODAY:
                String formatSchedule = String.format(CHOOSE_EXAM_PERIOD_NOTIFICATION_TIME, TODAY);
                botUserService.updatePreviousUserMessage(formatSchedule, botUser);
                return CompletableFuture.completedFuture(new BotMessage(formatSchedule, innerKeyboardGenerator.hours(), botUser));
            case CommandText.ENABLE_SEND_EXAM_PERIOD_TODAY:
                if (BotUser.isBotUserFullTime(botUser)) {
                    var enabledScheduleNotificationOptional = enableScheduleNotification(botUser, EXAM_PERIOD_TODAY);
                    if (enabledScheduleNotificationOptional.isEmpty()) {
                        return CompletableFuture.completedFuture(new BotMessage(
                                NOT_ENABLE_EXAM_PERIOD_NOTIFICATION_TODAY_FRMTD,
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    } else {
                        return CompletableFuture.completedFuture(new BotMessage(
                                String.format(EXAM_PERIOD_WILL_BE_SENT, TODAY) +
                                        enabledScheduleNotificationOptional.get().getHourForSend() + H_DOT,
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    }
                } else if (BotUser.isBotUserExtramural(botUser)) {
                    var extramuralEventTodayNotificationOptional = enableScheduleNotification(botUser, EXTRAMURAL_EVENT_TODAY);
                    if (extramuralEventTodayNotificationOptional.isEmpty()) {
                        return CompletableFuture.completedFuture(new BotMessage(
                                NOT_ENABLE_EXAM_PERIOD_NOTIFICATION_TODAY_FRMTD,
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    } else {
                        return CompletableFuture.completedFuture(new BotMessage(
                                String.format(EXAM_PERIOD_WILL_BE_SENT, TODAY) +
                                        extramuralEventTodayNotificationOptional.get().getHourForSend() + H_DOT,
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    }
                }
                break;
            case CommandText.DISABLE_SEND_EXAM_PERIOD_TODAY:
                if (BotUser.isBotUserFullTime(botUser)) {
                    var examPeriodTodayNotificationOptional =
                            scheduleNotificationRepository.findByUserIdAndUserSource(userId, source, EXAM_PERIOD_TODAY);
                    if (examPeriodTodayNotificationOptional.isEmpty()) {
                        return CompletableFuture.completedFuture(new BotMessage(
                                String.format(EXAM_PERIOD_IS_DISABLED, TODAY),
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    } else {
                        var examPeriodTodayNotificationDis = examPeriodTodayNotificationOptional.get();
                        examPeriodTodayNotificationDis.setEnabled(false);
                        scheduleNotificationRepository.save(examPeriodTodayNotificationDis);
                        return CompletableFuture.completedFuture(new BotMessage(
                                String.format(EXAM_PERIOD_IS_DISABLED, TODAY),
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    }
                } else if (BotUser.isBotUserExtramural(botUser)) {
                    var extramuralEventTodayNotificationOptional =
                            scheduleNotificationRepository.findByUserIdAndUserSource(userId, source, EXTRAMURAL_EVENT_TODAY);
                    if (extramuralEventTodayNotificationOptional.isEmpty()) {
                        return CompletableFuture.completedFuture(new BotMessage(
                                NOT_ENABLE_EXAM_PERIOD_NOTIFICATION_TODAY_FRMTD,
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    } else {
                        var extramuralEventTodayNotificationDis = extramuralEventTodayNotificationOptional.get();
                        extramuralEventTodayNotificationDis.setEnabled(false);
                        scheduleNotificationRepository.save(extramuralEventTodayNotificationDis);
                        return CompletableFuture.completedFuture(new BotMessage(
                                String.format(EXAM_PERIOD_IS_DISABLED, TODAY),
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    }
                }
        }

        return CompletableFuture.completedFuture(botMessage);
    }

    private CompletableFuture<BotMessage> tomorrowNotificationsExamPeriod(String message, BotUser botUser) {
        BotMessage botMessage = new BotMessage();

        switch (message) {
            case CommandText.SET_SEND_EXAM_PERIOD_TIME_TOMORROW:
                String formatSchedule = String.format(CHOOSE_EXAM_PERIOD_NOTIFICATION_TIME, TOMORROW);
                botUserService.updatePreviousUserMessage(formatSchedule, botUser);
                return CompletableFuture.completedFuture(new BotMessage(formatSchedule, innerKeyboardGenerator.hours(), botUser));
            case CommandText.ENABLE_SEND_EXAM_PERIOD_TOMORROW:
                if (BotUser.isBotUserFullTime(botUser)) {
                    var examPeriodTomorrowNotificationOptional = enableScheduleNotification(botUser, EXAM_PERIOD_TOMORROW);
                    if (examPeriodTomorrowNotificationOptional.isEmpty()) {
                        return CompletableFuture.completedFuture(new BotMessage(
                                NOT_ENABLE_EXAM_PERIOD_NOTIFICATION_TOMORROW_FRMTD,
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    } else {
                        return CompletableFuture.completedFuture(new BotMessage(
                                String.format(EXAM_PERIOD_WILL_BE_SENT, TOMORROW) +
                                        examPeriodTomorrowNotificationOptional.get().getHourForSend() + H_DOT,
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    }
                } else if (BotUser.isBotUserExtramural(botUser)) {
                    var extramuralEventTomorrowNotificationOptional = enableScheduleNotification(botUser, EXTRAMURAL_EVENT_TOMORROW);
                    if (extramuralEventTomorrowNotificationOptional.isEmpty()) {
                        return CompletableFuture.completedFuture(new BotMessage(
                                NOT_ENABLE_EXAM_PERIOD_NOTIFICATION_TOMORROW_FRMTD,
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    } else {
                        return CompletableFuture.completedFuture(new BotMessage(
                                String.format(EXAM_PERIOD_WILL_BE_SENT, TOMORROW) +
                                        extramuralEventTomorrowNotificationOptional.get().getHourForSend() + H_DOT,
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    }
                }
                break;
            case CommandText.DISABLE_SEND_EXAM_PERIOD_TOMORROW:
                if (BotUser.isBotUserFullTime(botUser)) {
                    var examPeriodTomorrowNotificationOptional = disableScheduleNotification(botUser, EXAM_PERIOD_TOMORROW);
                    if (examPeriodTomorrowNotificationOptional.isEmpty()) {
                        return CompletableFuture.completedFuture(new BotMessage(
                                NOT_ENABLE_EXAM_PERIOD_NOTIFICATION_TOMORROW_FRMTD,
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    } else {
                        return CompletableFuture.completedFuture(new BotMessage(
                                String.format(EXAM_PERIOD_IS_DISABLED, TOMORROW),
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    }
                } else if (BotUser.isBotUserExtramural(botUser)) {
                    var extramuralEventTomorrowNotificationOptional = disableScheduleNotification(botUser, EXTRAMURAL_EVENT_TOMORROW);
                    if (extramuralEventTomorrowNotificationOptional.isEmpty()) {
                        return CompletableFuture.completedFuture(new BotMessage(
                                NOT_ENABLE_EXAM_PERIOD_NOTIFICATION_TOMORROW_FRMTD,
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    } else {
                        return CompletableFuture.completedFuture(new BotMessage(
                                String.format(EXAM_PERIOD_IS_DISABLED, TOMORROW),
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    }
                }
        }
        return CompletableFuture.completedFuture(botMessage);
    }


    private CompletableFuture<BotMessage> afterTomorrowNotificationsExamPeriod(String message, BotUser botUser) {
        BotMessage botMessage = new BotMessage();

        switch (message) {
            case CommandText.SET_SEND_EXAM_PERIOD_TIME_AFTER_TOMORROW:
                String formatSchedule = String.format(CHOOSE_EXAM_PERIOD_NOTIFICATION_TIME, AFTER_TOMORROW);
                botUserService.updatePreviousUserMessage(formatSchedule, botUser);
                return CompletableFuture.completedFuture(new BotMessage(formatSchedule, innerKeyboardGenerator.hours(), botUser));
            case CommandText.ENABLE_SEND_EXAM_PERIOD_AFTER_TOMORROW:
                if (BotUser.isBotUserFullTime(botUser)) {
                    var examPeriodAfterTomorrowNotificationOptional = enableScheduleNotification(botUser, EXAM_PERIOD_AFTER_TOMORROW);
                    if (examPeriodAfterTomorrowNotificationOptional.isEmpty()) {
                        return CompletableFuture.completedFuture(new BotMessage(
                                NOT_ENABLE_EXAM_PERIOD_NOTIFICATION_AFTER_TOMORROW_FRMTD,
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    } else {
                        return CompletableFuture.completedFuture(new BotMessage(
                                String.format(EXAM_PERIOD_WILL_BE_SENT, AFTER_TOMORROW) +
                                        examPeriodAfterTomorrowNotificationOptional.get().getHourForSend() + H_DOT,
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    }
                } else if (BotUser.isBotUserExtramural(botUser)) {
                    var extramuralEventAfterTomorrowNotificationOptional = enableScheduleNotification(botUser, EXTRAMURAL_EVENT_AFTER_TOMORROW);
                    if (extramuralEventAfterTomorrowNotificationOptional.isEmpty()) {
                        return CompletableFuture.completedFuture(new BotMessage(
                                NOT_ENABLE_EXAM_PERIOD_NOTIFICATION_AFTER_TOMORROW_FRMTD,
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    } else {
                        return CompletableFuture.completedFuture(new BotMessage(
                                String.format(EXAM_PERIOD_WILL_BE_SENT, AFTER_TOMORROW) +
                                        extramuralEventAfterTomorrowNotificationOptional.get().getHourForSend() + H_DOT,
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    }
                }
                break;
            case CommandText.DISABLE_SEND_EXAM_PERIOD_AFTER_TOMORROW:
                if (BotUser.isBotUserFullTime(botUser)) {
                    var examPeriodAfterTomorrowNotificationOptional = disableScheduleNotification(botUser, EXAM_PERIOD_AFTER_TOMORROW);
                    if (examPeriodAfterTomorrowNotificationOptional.isEmpty()) {
                        return CompletableFuture.completedFuture(new BotMessage(
                                NOT_ENABLE_EXAM_PERIOD_NOTIFICATION_AFTER_TOMORROW_FRMTD,
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    } else {
                        return CompletableFuture.completedFuture(new BotMessage(
                                String.format(EXAM_PERIOD_IS_DISABLED, AFTER_TOMORROW),
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    }
                } else if (BotUser.isBotUserExtramural(botUser)) {
                    var extramuralEventAfterTomorrowNotificationOptional = disableScheduleNotification(botUser, EXTRAMURAL_EVENT_AFTER_TOMORROW);
                    if (extramuralEventAfterTomorrowNotificationOptional.isEmpty()) {
                        return CompletableFuture.completedFuture(new BotMessage(
                                NOT_ENABLE_EXAM_PERIOD_NOTIFICATION_AFTER_TOMORROW_FRMTD,
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    } else {
                        return CompletableFuture.completedFuture(new BotMessage(
                                String.format(EXAM_PERIOD_IS_DISABLED, AFTER_TOMORROW),
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    }
                }
        }
        return CompletableFuture.completedFuture(botMessage);
    }

    private CompletableFuture<BotMessage> getCurrentUserSettings(BotUser botUser) {
        String scheduleNotificationSettings = BotUser.isBotUserFullTime(botUser) ?
                getScheduleNotificationSettingsFullTime(botUser)
                : getScheduleNotificationSettingsExtramural(botUser);
        String weekTypeFilterSettings = BotUser.isBotUserFullTime(botUser) ?
                "\n\n" + "Фильтрация по типу недели: " + (botUser.isFilterNomDenom() ? "вкл." : "выкл.") : "";
        String silentDaysSettings = "\n\n" + "Рассылка, когда пар нет: " + (botUser.isSilentEmptyDays() ? "не присылается" : "присылается");
        String sentKeyboardSettings = "\n\n" + (botUser.isSentKeyboard() ? "Клавиатура бота: присылается" : "Клавиатура бота: не присылается");
        String currentUserSettings = getStudentInfo(botUser) + "\n" + scheduleNotificationSettings + weekTypeFilterSettings + silentDaysSettings + sentKeyboardSettings;

        return CompletableFuture.completedFuture(new BotMessage(currentUserSettings, innerKeyboardGenerator.settings(botUser), botUser));

    }


    private String getStudentInfo(BotUser botUser) {
        String department = firstNotEmpty(botUser.getDepartment());
        department = department.equals("не указано")
                ? department
                : DepartmentConverter.getDepartmentByUrl(department);
        return "Факультет: " + department + "\n" +
                "Форма обучения: " + firstNotEmpty(botUser.getEducationForm()) + "\n" +
                "Группа: " + firstNotEmpty(botUser.getGroupNumber()) + "\n";
    }

    private String getScheduleNotificationSettingsFullTime(BotUser botUser) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Рассылка расписания на сегодня: ");
        formatScheduleNotificationsSettings(botUser, FULL_TIME_TODAY, stringBuilder);

        stringBuilder.append("\n\n");

        stringBuilder.append("Рассылка расписания на завтра: ");
        formatScheduleNotificationsSettings(botUser, FULL_TIME_TOMORROW, stringBuilder);

        stringBuilder.append("\n\n");

        stringBuilder.append("Рассылка расписания сессии на сегодня: ");
        formatScheduleNotificationsSettings(botUser, EXAM_PERIOD_TODAY, stringBuilder);

        stringBuilder.append("\n\n");

        stringBuilder.append("Рассылка расписания сессии на завтра: ");
        formatScheduleNotificationsSettings(botUser, EXAM_PERIOD_TOMORROW, stringBuilder);

        stringBuilder.append("\n\n");

        stringBuilder.append("Рассылка расписания сессии на послезавтра: ");
        formatScheduleNotificationsSettings(botUser, EXAM_PERIOD_AFTER_TOMORROW, stringBuilder);

        return stringBuilder.toString();
    }

    private String getScheduleNotificationSettingsExtramural(BotUser botUser) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Рассылка расписания сессии на сегодня: ");
        formatScheduleNotificationsSettings(botUser, EXTRAMURAL_EVENT_TODAY, stringBuilder);

        stringBuilder.append("\n\n");

        stringBuilder.append("Рассылка расписания сессии на завтра: ");
        formatScheduleNotificationsSettings(botUser, EXTRAMURAL_EVENT_TOMORROW, stringBuilder);

        stringBuilder.append("\n\n");

        stringBuilder.append("Рассылка расписания сессии на послезавтра: ");
        formatScheduleNotificationsSettings(botUser, EXTRAMURAL_EVENT_AFTER_TOMORROW, stringBuilder);

        return stringBuilder.toString();
    }

    private void formatScheduleNotificationsSettings(BotUser botUser, NotificationType notificationType, StringBuilder stringBuilder) {
        var scheduleNotificationOptional =
                scheduleNotificationRepository.findByUserIdAndUserSource(botUser.getUserId(), botUser.getSource(), notificationType);
        if (scheduleNotificationOptional.isEmpty()) {
            stringBuilder.append("не подключена.");
        } else {
            var scheduleNotification = scheduleNotificationOptional.get();
            if (scheduleNotification.isEnabled()) {
                stringBuilder.append("вкл, ");
            } else {
                stringBuilder.append("выкл, ");
            }

            if (null == scheduleNotification.getHourForSend()) {
                stringBuilder.append("время не указано.");
            } else {
                stringBuilder.append(scheduleNotification.getHourForSend()).append(H_DOT);
            }
        }
    }

    private String firstNotEmpty(String string) {
        return StringUtils.isEmpty(string) ? "не указано" : string;
    }

    private boolean forFullTimeScheduleTodayNotification(String message) {
        return message.equals(CommandText.SET_SEND_SCHEDULE_TIME_TODAY)
                || message.equals(CommandText.ENABLE_SEND_SCHEDULE_TODAY)
                || message.equals(CommandText.DISABLE_SEND_SCHEDULE_TODAY);
    }

    private boolean forFullTimeScheduleTomorrowNotification(String message) {
        return message.equals(CommandText.SET_SEND_SCHEDULE_TIME_TOMORROW)
                || message.equals(CommandText.ENABLE_SEND_SCHEDULE_TOMORROW)
                || message.equals(CommandText.DISABLE_SEND_SCHEDULE_TOMORROW);
    }

    private Optional<ScheduleNotification> enableScheduleNotification(BotUser botUser, NotificationType notificationType) {
        return setScheduleNotificationStatus(botUser, notificationType, true);
    }

    private Optional<ScheduleNotification> disableScheduleNotification(BotUser botUser, NotificationType notificationType) {
        return setScheduleNotificationStatus(botUser, notificationType, false);
    }

    private Optional<ScheduleNotification> setScheduleNotificationStatus(BotUser botUser, NotificationType notificationType, boolean isEnabled) {
        var scheduleNotificationOptional =
                scheduleNotificationRepository.findByUserIdAndUserSource(botUser.getUserId(), botUser.getSource(), notificationType);
        if (scheduleNotificationOptional.isEmpty()) {
            return Optional.empty();
        } else {
            var scheduleNotification = scheduleNotificationOptional.get();
            scheduleNotification.setEnabled(isEnabled);
            scheduleNotification = scheduleNotificationRepository.save(scheduleNotification);
            return Optional.of(scheduleNotification);
        }
    }

    private void upsertScheduleNotificationHourForSend(int hourForSend, BotUser botUser, NotificationType notificationType) {
        var userId = botUser.getUserId();
        var source = botUser.getSource();
        var scheduleTodayNotificationOptional =
                scheduleNotificationRepository.findByUserIdAndUserSource(userId, source, notificationType);

        if (scheduleTodayNotificationOptional.isEmpty()) {
            scheduleNotificationRepository.save(new ScheduleNotification(userId, source, true, hourForSend, notificationType));
        } else {
            var scheduleTodayNotification = scheduleTodayNotificationOptional.get();
            scheduleTodayNotification.setHourForSend(hourForSend);
            scheduleNotificationRepository.save(scheduleTodayNotification);
        }
    }
}
