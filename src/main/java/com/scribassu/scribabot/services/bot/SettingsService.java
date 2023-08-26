package com.scribassu.scribabot.services.bot;

import com.scribassu.scribabot.entities.notifications.*;
import com.scribassu.scribabot.generators.InnerKeyboardGenerator;
import com.scribassu.scribabot.model.BotMessage;
import com.scribassu.scribabot.model.InnerBotUser;
import com.scribassu.scribabot.repositories.notifications.*;
import com.scribassu.scribabot.text.CommandText;
import com.scribassu.scribabot.util.BotUserSource;
import com.scribassu.scribabot.util.DepartmentConverter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

import static com.scribassu.scribabot.text.MessageText.*;

// 1127
// 1038
// 984
// 734
@Service
@Slf4j
@Data
public class SettingsService implements BotMessageService {
    private final BotUserService botUserService;
    private final ScheduleTodayNotificationRepository scheduleTodayNotificationRepository;
    private final ScheduleTomorrowNotificationRepository scheduleTomorrowNotificationRepository;
    private final ExamPeriodTodayNotificationRepository examPeriodTodayNotificationRepository;
    private final ExamPeriodTomorrowNotificationRepository examPeriodTomorrowNotificationRepository;
    private final ExamPeriodAfterTomorrowNotificationRepository examPeriodAfterTomorrowNotificationRepository;
    private final ExtramuralEventTodayNotificationRepository extramuralEventTodayNotificationRepository;
    private final ExtramuralEventTomorrowNotificationRepository extramuralEventTomorrowNotificationRepository;
    private final ExtramuralEventAfterTomorrowNotificationRepository extramuralEventAfterTomorrowNotificationRepository;
    private final InnerKeyboardGenerator innerKeyboardGenerator;

    @Override
    public CompletableFuture<BotMessage> getBotMessage(String message, InnerBotUser botUser) {
        BotMessage botMessage = new BotMessage();
        String userId = botUser.getUserId();
        BotUserSource source = botUser.getSource();

        switch (message) {
            case CommandText.SEND_EXAM_PERIOD:
                return CompletableFuture.completedFuture(new BotMessage(HERE_EXAM_PERIOD_NOTIFICATION, innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
            case CommandText.SEND_SCHEDULE:
                return CompletableFuture.completedFuture(new BotMessage(HERE_SCHEDULE_NOTIFICATION, innerKeyboardGenerator.settingsScheduleNotification(botUser), botUser));
            case CommandText.SET_SEND_SCHEDULE_TIME_TODAY:
            case CommandText.ENABLE_SEND_SCHEDULE_TODAY:
            case CommandText.DISABLE_SEND_SCHEDULE_TODAY:
                return todayNotificationsSchedule(message, botUser);
            case CommandText.SET_SEND_SCHEDULE_TIME_TOMORROW:
            case CommandText.ENABLE_SEND_SCHEDULE_TOMORROW:
            case CommandText.DISABLE_SEND_SCHEDULE_TOMORROW:
                return tomorrowNotificationsSchedule(message, botUser);
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
                String scheduleNotificationSettings = InnerBotUser.isBotUserFullTime(botUser) ?
                        getScheduleNotificationSettingsFullTime(botUser)
                        : getScheduleNotificationSettingsExtramural(botUser);
                String weekTypeFilterSettings = InnerBotUser.isBotUserFullTime(botUser) ?
                        "\n\n" + "Фильтрация по типу недели: " + (botUser.isFilterNomDenom() ? "вкл." : "выкл.") : "";
                String silentDaysSettings = "\n\n" + "Рассылка, когда пар нет: " + (botUser.isSilentEmptyDays() ? "не присылается" : "присылается");
                String sentKeyboardSettings = "\n\n" + (botUser.isSentKeyboard() ? "Клавиатура бота: присылается" : "Клавиатура бота: не присылается");
                String currentUserSettings = getStudentInfo(botUser) + "\n" + scheduleNotificationSettings + weekTypeFilterSettings + silentDaysSettings + sentKeyboardSettings;
                return CompletableFuture.completedFuture(new BotMessage(currentUserSettings, innerKeyboardGenerator.settings(botUser), botUser));
            case CommandText.DELETE_PROFILE:
                return CompletableFuture.completedFuture(new BotMessage(DELETE_CONFIRMATION, innerKeyboardGenerator.confirmDeletion(), botUser));
            case CommandText.NO:
                return CompletableFuture.completedFuture(new BotMessage(NOT_BYE_MESSAGE, innerKeyboardGenerator.settings(botUser), botUser));
            case CommandText.YES:
                botUserService.delete(botUser);
                return CompletableFuture.completedFuture(new BotMessage(BYE_MESSAGE, botUser));
        }

        if (CommandText.HOUR_PATTERN.matcher(message).matches()) {
            int hourForSend = Integer.parseInt(message.substring(0, message.indexOf(" ")));

            if (botUser.getPreviousUserMessage().equalsIgnoreCase(CHOOSE_SCHEDULE_NOTIFICATION_TIME_TODAY)) {
                ScheduleTodayNotification scheduleTodayNotification =
                        scheduleTodayNotificationRepository.findByUserIdAndUserSource(userId, source);

                if (null == scheduleTodayNotification) {
                    scheduleTodayNotification = new ScheduleTodayNotification(userId, source, true, hourForSend);
                } else {
                    scheduleTodayNotification.setHourForSend(hourForSend);
                }
                scheduleTodayNotificationRepository.save(scheduleTodayNotification);
            }
            if (botUser.getPreviousUserMessage().equalsIgnoreCase(CHOOSE_SCHEDULE_NOTIFICATION_TIME_TOMORROW)) {
                ScheduleTomorrowNotification scheduleTomorrowNotification =
                        scheduleTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);

                if (null == scheduleTomorrowNotification) {
                    scheduleTomorrowNotification = new ScheduleTomorrowNotification(userId, source, true, hourForSend);
                } else {
                    scheduleTomorrowNotification.setHourForSend(hourForSend);
                }
                scheduleTomorrowNotificationRepository.save(scheduleTomorrowNotification);
            }
            if (botUser.getPreviousUserMessage().equalsIgnoreCase(CHOOSE_EXAM_PERIOD_NOTIFICATION_TIME_TODAY)) {
                if (InnerBotUser.isBotUserFullTime(botUser)) {
                    ExamPeriodTodayNotification examPeriodTodayNotification =
                            examPeriodTodayNotificationRepository.findByUserIdAndUserSource(userId, source);

                    if (null == examPeriodTodayNotification) {
                        examPeriodTodayNotification = new ExamPeriodTodayNotification(userId, source, true, hourForSend);
                    } else {
                        examPeriodTodayNotification.setHourForSend(hourForSend);
                    }
                    examPeriodTodayNotificationRepository.save(examPeriodTodayNotification);
                } else {
                    ExtramuralEventTodayNotification extramuralEventTodayNotification =
                            extramuralEventTodayNotificationRepository.findByUserIdAndUserSource(userId, source);

                    if (null == extramuralEventTodayNotification) {
                        extramuralEventTodayNotification = new ExtramuralEventTodayNotification(userId, source, true, hourForSend);
                    } else {
                        extramuralEventTodayNotification.setHourForSend(hourForSend);
                    }
                    extramuralEventTodayNotificationRepository.save(extramuralEventTodayNotification);
                }
            }
            if (botUser.getPreviousUserMessage().equalsIgnoreCase(CHOOSE_EXAM_PERIOD_NOTIFICATION_TIME_TOMORROW)) {
                if (InnerBotUser.isBotUserFullTime(botUser)) {
                    ExamPeriodTomorrowNotification examPeriodTomorrowNotification =
                            examPeriodTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);

                    if (null == examPeriodTomorrowNotification) {
                        examPeriodTomorrowNotification = new ExamPeriodTomorrowNotification(userId, source, true, hourForSend);
                    } else {
                        examPeriodTomorrowNotification.setHourForSend(hourForSend);
                    }
                    examPeriodTomorrowNotificationRepository.save(examPeriodTomorrowNotification);
                } else {
                    ExtramuralEventTomorrowNotification extramuralEventTomorrowNotification =
                            extramuralEventTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);

                    if (null == extramuralEventTomorrowNotification) {
                        extramuralEventTomorrowNotification = new ExtramuralEventTomorrowNotification(userId, source, true, hourForSend);
                    } else {
                        extramuralEventTomorrowNotification.setHourForSend(hourForSend);
                    }
                    extramuralEventTomorrowNotificationRepository.save(extramuralEventTomorrowNotification);
                }
            }
            if (botUser.getPreviousUserMessage().equalsIgnoreCase(CHOOSE_EXAM_PERIOD_NOTIFICATION_TIME_AFTER_TOMORROW)) {
                if (InnerBotUser.isBotUserFullTime(botUser)) {
                    ExamPeriodAfterTomorrowNotification examPeriodAfterTomorrowNotification =
                            examPeriodAfterTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);

                    if (null == examPeriodAfterTomorrowNotification) {
                        examPeriodAfterTomorrowNotification = new ExamPeriodAfterTomorrowNotification(userId, source, true, hourForSend);
                    } else {
                        examPeriodAfterTomorrowNotification.setHourForSend(hourForSend);
                    }
                    examPeriodAfterTomorrowNotificationRepository.save(examPeriodAfterTomorrowNotification);
                } else {
                    ExtramuralEventAfterTomorrowNotification extramuralEventAfterTomorrowNotification =
                            extramuralEventAfterTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);

                    if (null == extramuralEventAfterTomorrowNotification) {
                        extramuralEventAfterTomorrowNotification = new ExtramuralEventAfterTomorrowNotification(userId, source, true, hourForSend);
                    } else {
                        extramuralEventAfterTomorrowNotification.setHourForSend(hourForSend);
                    }
                    extramuralEventAfterTomorrowNotificationRepository.save(extramuralEventAfterTomorrowNotification);
                }
            }
            final String hourMessage = SCHEDULE_WILL_BE_SENT_ABSTRACT + hourForSend + H_DOT;
            return CompletableFuture.completedFuture(new BotMessage(hourMessage, innerKeyboardGenerator.settings(botUser), botUser));
        }

        return CompletableFuture.completedFuture(botMessage);
    }

    private CompletableFuture<BotMessage> todayNotificationsSchedule(String message, InnerBotUser botUser) {
        BotMessage botMessage = new BotMessage();
        String userId = botUser.getUserId();
        BotUserSource source = botUser.getSource();

        switch (message) {
            case CommandText.SET_SEND_SCHEDULE_TIME_TODAY:
                String formatSchedule = String.format(CHOOSE_SCHEDULE_NOTIFICATION_TIME, TODAY);
                botUserService.updatePreviousUserMessage(formatSchedule, botUser);
                return CompletableFuture.completedFuture(new BotMessage(formatSchedule, innerKeyboardGenerator.hours(), botUser));
            case CommandText.ENABLE_SEND_SCHEDULE_TODAY:
                ScheduleTodayNotification scheduleTodayNotificationEn =
                        scheduleTodayNotificationRepository.findByUserIdAndUserSource(userId, source);
                if (null != scheduleTodayNotificationEn) {
                    scheduleTodayNotificationEn.setEnabled(true);
                    scheduleTodayNotificationRepository.save(scheduleTodayNotificationEn);
                    String enableNotificationMessage = String.format(SCHEDULE_WILL_BE_SENT, TODAY) +
                            scheduleTodayNotificationEn.getHourForSend() + H_DOT;
                    return CompletableFuture.completedFuture(new BotMessage(
                            enableNotificationMessage,
                            innerKeyboardGenerator.settingsScheduleNotification(botUser), botUser));
                } else {
                    return CompletableFuture.completedFuture(new BotMessage(
                            NOT_ENABLE_SCHEDULE_NOTIFICATION_TODAY_FRMTD,
                            innerKeyboardGenerator.settingsScheduleNotification(botUser), botUser));
                }
            case CommandText.DISABLE_SEND_SCHEDULE_TODAY:
                ScheduleTodayNotification scheduleTodayNotificationDis =
                        scheduleTodayNotificationRepository.findByUserIdAndUserSource(userId, source);
                if (null != scheduleTodayNotificationDis) {
                    scheduleTodayNotificationDis.setEnabled(false);
                    scheduleTodayNotificationRepository.save(scheduleTodayNotificationDis);
                    return CompletableFuture.completedFuture(new BotMessage(
                            String.format(SCHEDULE_IS_DISABLED, TODAY),
                            innerKeyboardGenerator.settingsScheduleNotification(botUser), botUser));
                } else {
                    return CompletableFuture.completedFuture(new BotMessage(
                            NOT_ENABLE_SCHEDULE_NOTIFICATION_TODAY_FRMTD,
                            innerKeyboardGenerator.settingsScheduleNotification(botUser), botUser));
                }
        }

        return CompletableFuture.completedFuture(botMessage);
    }

    private CompletableFuture<BotMessage> todayNotificationsExamPeriod(String message, InnerBotUser botUser) {
        BotMessage botMessage = new BotMessage();
        String userId = botUser.getUserId();
        BotUserSource source = botUser.getSource();

        switch (message) {
            case CommandText.SET_SEND_EXAM_PERIOD_TIME_TODAY:
                String formatSchedule = String.format(CHOOSE_EXAM_PERIOD_NOTIFICATION_TIME, TODAY);
                botUserService.updatePreviousUserMessage(formatSchedule, botUser);
                return CompletableFuture.completedFuture(new BotMessage(formatSchedule, innerKeyboardGenerator.hours(), botUser));
            case CommandText.ENABLE_SEND_EXAM_PERIOD_TODAY:
                if (InnerBotUser.isBotUserFullTime(botUser)) {
                    ExamPeriodTodayNotification examPeriodTodayNotificationEn =
                            examPeriodTodayNotificationRepository.findByUserIdAndUserSource(userId, source);
                    if (null != examPeriodTodayNotificationEn) {
                        examPeriodTodayNotificationEn.setEnabled(true);
                        examPeriodTodayNotificationRepository.save(examPeriodTodayNotificationEn);
                        return CompletableFuture.completedFuture(new BotMessage(
                                String.format(EXAM_PERIOD_WILL_BE_SENT, TODAY) +
                                        examPeriodTodayNotificationEn.getHourForSend() + H_DOT,
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    } else {
                        return CompletableFuture.completedFuture(new BotMessage(
                                NOT_ENABLE_EXAM_PERIOD_NOTIFICATION_TODAY_FRMTD,
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    }
                } else if (InnerBotUser.isBotUserExtramural(botUser)) {
                    ExtramuralEventTodayNotification extramuralEventTodayNotificationEn =
                            extramuralEventTodayNotificationRepository.findByUserIdAndUserSource(userId, source);
                    if (null != extramuralEventTodayNotificationEn) {
                        extramuralEventTodayNotificationEn.setEnabled(true);
                        extramuralEventTodayNotificationRepository.save(extramuralEventTodayNotificationEn);
                        return CompletableFuture.completedFuture(new BotMessage(
                                String.format(EXAM_PERIOD_WILL_BE_SENT, TODAY) +
                                        extramuralEventTodayNotificationEn.getHourForSend() + H_DOT,
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    } else {
                        return CompletableFuture.completedFuture(new BotMessage(
                                NOT_ENABLE_EXAM_PERIOD_NOTIFICATION_TODAY_FRMTD,
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    }
                }
                break;
            case CommandText.DISABLE_SEND_EXAM_PERIOD_TODAY:
                if (InnerBotUser.isBotUserFullTime(botUser)) {
                    ExamPeriodTodayNotification examPeriodTodayNotificationDis =
                            examPeriodTodayNotificationRepository.findByUserIdAndUserSource(userId, source);
                    if (null != examPeriodTodayNotificationDis) {
                        examPeriodTodayNotificationDis.setEnabled(false);
                        examPeriodTodayNotificationRepository.save(examPeriodTodayNotificationDis);
                        return CompletableFuture.completedFuture(new BotMessage(
                                String.format(EXAM_PERIOD_IS_DISABLED, TODAY),
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    } else {
                        return CompletableFuture.completedFuture(new BotMessage(
                                NOT_ENABLE_EXAM_PERIOD_NOTIFICATION_TODAY_FRMTD,
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    }
                } else if (InnerBotUser.isBotUserExtramural(botUser)) {
                    ExtramuralEventTodayNotification extramuralEventTodayNotificationDis =
                            extramuralEventTodayNotificationRepository.findByUserIdAndUserSource(userId, source);
                    if (null != extramuralEventTodayNotificationDis) {
                        extramuralEventTodayNotificationDis.setEnabled(false);
                        extramuralEventTodayNotificationRepository.save(extramuralEventTodayNotificationDis);
                        return CompletableFuture.completedFuture(new BotMessage(
                                String.format(EXAM_PERIOD_IS_DISABLED, TODAY),
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    } else {
                        return CompletableFuture.completedFuture(new BotMessage(
                                NOT_ENABLE_EXAM_PERIOD_NOTIFICATION_TODAY_FRMTD,
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    }
                }
        }

        return CompletableFuture.completedFuture(botMessage);
    }

    private CompletableFuture<BotMessage> tomorrowNotificationsSchedule(String message, InnerBotUser botUser) {
        BotMessage botMessage = new BotMessage();
        String userId = botUser.getUserId();
        BotUserSource source = botUser.getSource();

        switch (message) {
            case CommandText.SET_SEND_SCHEDULE_TIME_TOMORROW:
                String formatSchedule = String.format(CHOOSE_SCHEDULE_NOTIFICATION_TIME, TOMORROW);
                botUserService.updatePreviousUserMessage(formatSchedule, botUser);
                return CompletableFuture.completedFuture(new BotMessage(formatSchedule, innerKeyboardGenerator.hours(), botUser));
            case CommandText.ENABLE_SEND_SCHEDULE_TOMORROW:
                ScheduleTomorrowNotification scheduleTomorrowNotificationEn =
                        scheduleTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);
                if (null != scheduleTomorrowNotificationEn) {
                    scheduleTomorrowNotificationEn.setEnabled(true);
                    scheduleTomorrowNotificationRepository.save(scheduleTomorrowNotificationEn);
                    String enableNotificationMessage = String.format(SCHEDULE_WILL_BE_SENT, TOMORROW) +
                            scheduleTomorrowNotificationEn.getHourForSend() + H_DOT;
                    return CompletableFuture.completedFuture(new BotMessage(
                            enableNotificationMessage,
                            innerKeyboardGenerator.settingsScheduleNotification(botUser), botUser));
                } else {
                    return CompletableFuture.completedFuture(new BotMessage(
                            NOT_ENABLE_SCHEDULE_NOTIFICATION_TOMORROW_FRMTD,
                            innerKeyboardGenerator.settingsScheduleNotification(botUser), botUser));
                }
            case CommandText.DISABLE_SEND_SCHEDULE_TOMORROW:
                ScheduleTomorrowNotification scheduleTomorrowNotificationDis =
                        scheduleTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);
                if (null != scheduleTomorrowNotificationDis) {
                    scheduleTomorrowNotificationDis.setEnabled(false);
                    scheduleTomorrowNotificationRepository.save(scheduleTomorrowNotificationDis);
                    return CompletableFuture.completedFuture(new BotMessage(
                            String.format(SCHEDULE_IS_DISABLED, TOMORROW),
                            innerKeyboardGenerator.settingsScheduleNotification(botUser), botUser));
                } else {
                    return CompletableFuture.completedFuture(new BotMessage(
                            NOT_ENABLE_SCHEDULE_NOTIFICATION_TOMORROW_FRMTD,
                            innerKeyboardGenerator.settingsScheduleNotification(botUser), botUser));
                }
        }

        return CompletableFuture.completedFuture(botMessage);
    }

    private CompletableFuture<BotMessage> tomorrowNotificationsExamPeriod(String message, InnerBotUser botUser) {
        var botMessage = new BotMessage();
        String userId = botUser.getUserId();
        BotUserSource source = botUser.getSource();

        switch (message) {
            case CommandText.SET_SEND_EXAM_PERIOD_TIME_TOMORROW:
                String formatSchedule = String.format(CHOOSE_EXAM_PERIOD_NOTIFICATION_TIME, TOMORROW);
                botUserService.updatePreviousUserMessage(formatSchedule, botUser);
                return CompletableFuture.completedFuture(new BotMessage(formatSchedule, innerKeyboardGenerator.hours(), botUser));
            case CommandText.ENABLE_SEND_EXAM_PERIOD_TOMORROW:
                if (InnerBotUser.isBotUserFullTime(botUser)) {
                    ExamPeriodTomorrowNotification examPeriodTomorrowNotificationEn =
                            examPeriodTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);
                    if (null != examPeriodTomorrowNotificationEn) {
                        examPeriodTomorrowNotificationEn.setEnabled(true);
                        examPeriodTomorrowNotificationRepository.save(examPeriodTomorrowNotificationEn);
                        return CompletableFuture.completedFuture(new BotMessage(
                                String.format(EXAM_PERIOD_WILL_BE_SENT, TOMORROW) +
                                        examPeriodTomorrowNotificationEn.getHourForSend() + H_DOT,
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    } else {
                        return CompletableFuture.completedFuture(new BotMessage(
                                NOT_ENABLE_EXAM_PERIOD_NOTIFICATION_TOMORROW_FRMTD,
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    }
                } else if (InnerBotUser.isBotUserExtramural(botUser)) {
                    ExtramuralEventTomorrowNotification extramuralEventTomorrowNotificationEn =
                            extramuralEventTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);
                    if (null != extramuralEventTomorrowNotificationEn) {
                        extramuralEventTomorrowNotificationEn.setEnabled(true);
                        extramuralEventTomorrowNotificationRepository.save(extramuralEventTomorrowNotificationEn);
                        return CompletableFuture.completedFuture(new BotMessage(
                                String.format(EXAM_PERIOD_WILL_BE_SENT, TOMORROW) +
                                        extramuralEventTomorrowNotificationEn.getHourForSend() + H_DOT,
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    } else {
                        return CompletableFuture.completedFuture(new BotMessage(
                                NOT_ENABLE_EXAM_PERIOD_NOTIFICATION_TOMORROW_FRMTD,
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    }
                }
                break;
            case CommandText.DISABLE_SEND_EXAM_PERIOD_TOMORROW:
                if (InnerBotUser.isBotUserFullTime(botUser)) {
                    ExamPeriodTomorrowNotification examPeriodTomorrowNotificationDis =
                            examPeriodTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);
                    if (null != examPeriodTomorrowNotificationDis) {
                        examPeriodTomorrowNotificationDis.setEnabled(false);
                        examPeriodTomorrowNotificationRepository.save(examPeriodTomorrowNotificationDis);
                        return CompletableFuture.completedFuture(new BotMessage(
                                String.format(EXAM_PERIOD_IS_DISABLED, TOMORROW),
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    } else {
                        return CompletableFuture.completedFuture(new BotMessage(
                                NOT_ENABLE_EXAM_PERIOD_NOTIFICATION_TOMORROW_FRMTD,
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    }
                } else if (InnerBotUser.isBotUserExtramural(botUser)) {
                    ExtramuralEventTomorrowNotification extramuralEventTomorrowNotificationDis =
                            extramuralEventTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);
                    if (null != extramuralEventTomorrowNotificationDis) {
                        extramuralEventTomorrowNotificationDis.setEnabled(false);
                        extramuralEventTomorrowNotificationRepository.save(extramuralEventTomorrowNotificationDis);
                        return CompletableFuture.completedFuture(new BotMessage(
                                String.format(EXAM_PERIOD_IS_DISABLED, TOMORROW),
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    } else {
                        return CompletableFuture.completedFuture(new BotMessage(
                                NOT_ENABLE_EXAM_PERIOD_NOTIFICATION_TOMORROW_FRMTD,
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    }
                }
        }

        return CompletableFuture.completedFuture(botMessage);
    }

    private CompletableFuture<BotMessage> afterTomorrowNotificationsExamPeriod(String message, InnerBotUser botUser) {
        BotMessage botMessage = new BotMessage();
        String userId = botUser.getUserId();
        BotUserSource source = botUser.getSource();

        switch (message) {
            case CommandText.SET_SEND_EXAM_PERIOD_TIME_AFTER_TOMORROW:
                String formatSchedule = String.format(CHOOSE_EXAM_PERIOD_NOTIFICATION_TIME, AFTER_TOMORROW);
                botUserService.updatePreviousUserMessage(formatSchedule, botUser);
                return CompletableFuture.completedFuture(new BotMessage(formatSchedule, innerKeyboardGenerator.hours(), botUser));
            case CommandText.ENABLE_SEND_EXAM_PERIOD_AFTER_TOMORROW:
                if (InnerBotUser.isBotUserFullTime(botUser)) {
                    ExamPeriodAfterTomorrowNotification examPeriodAfterTomorrowNotificationEn =
                            examPeriodAfterTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);
                    if (null != examPeriodAfterTomorrowNotificationEn) {
                        examPeriodAfterTomorrowNotificationEn.setEnabled(true);
                        examPeriodAfterTomorrowNotificationRepository.save(examPeriodAfterTomorrowNotificationEn);
                        return CompletableFuture.completedFuture(new BotMessage(
                                String.format(EXAM_PERIOD_WILL_BE_SENT, AFTER_TOMORROW) +
                                        examPeriodAfterTomorrowNotificationEn.getHourForSend() + H_DOT,
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    } else {
                        return CompletableFuture.completedFuture(new BotMessage(
                                NOT_ENABLE_EXAM_PERIOD_NOTIFICATION_AFTER_TOMORROW_FRMTD,
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    }
                } else if (InnerBotUser.isBotUserExtramural(botUser)) {
                    ExtramuralEventAfterTomorrowNotification extramuralEventAfterTomorrowNotificationEn =
                            extramuralEventAfterTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);
                    if (null != extramuralEventAfterTomorrowNotificationEn) {
                        extramuralEventAfterTomorrowNotificationEn.setEnabled(true);
                        extramuralEventAfterTomorrowNotificationRepository.save(extramuralEventAfterTomorrowNotificationEn);
                        return CompletableFuture.completedFuture(new BotMessage(
                                String.format(EXAM_PERIOD_WILL_BE_SENT, AFTER_TOMORROW) +
                                        extramuralEventAfterTomorrowNotificationEn.getHourForSend() + H_DOT,
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    } else {
                        return CompletableFuture.completedFuture(new BotMessage(
                                NOT_ENABLE_EXAM_PERIOD_NOTIFICATION_AFTER_TOMORROW_FRMTD,
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    }
                }
                break;
            case CommandText.DISABLE_SEND_EXAM_PERIOD_AFTER_TOMORROW:
                if (InnerBotUser.isBotUserFullTime(botUser)) {
                    ExamPeriodAfterTomorrowNotification examPeriodAfterTomorrowNotificationDis =
                            examPeriodAfterTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);
                    if (null != examPeriodAfterTomorrowNotificationDis) {
                        examPeriodAfterTomorrowNotificationDis.setEnabled(false);
                        examPeriodAfterTomorrowNotificationRepository.save(examPeriodAfterTomorrowNotificationDis);
                        return CompletableFuture.completedFuture(new BotMessage(
                                String.format(EXAM_PERIOD_IS_DISABLED, AFTER_TOMORROW),
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    } else {
                        return CompletableFuture.completedFuture(new BotMessage(
                                NOT_ENABLE_EXAM_PERIOD_NOTIFICATION_AFTER_TOMORROW_FRMTD,
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    }
                } else if (InnerBotUser.isBotUserExtramural(botUser)) {
                    ExtramuralEventAfterTomorrowNotification extramuralEventAfterTomorrowNotificationDis =
                            extramuralEventAfterTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);
                    if (null != extramuralEventAfterTomorrowNotificationDis) {
                        extramuralEventAfterTomorrowNotificationDis.setEnabled(false);
                        extramuralEventAfterTomorrowNotificationRepository.save(extramuralEventAfterTomorrowNotificationDis);
                        return CompletableFuture.completedFuture(new BotMessage(
                                String.format(EXAM_PERIOD_IS_DISABLED, AFTER_TOMORROW),
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    } else {
                        return CompletableFuture.completedFuture(new BotMessage(
                                NOT_ENABLE_EXAM_PERIOD_NOTIFICATION_AFTER_TOMORROW_FRMTD,
                                innerKeyboardGenerator.settingsExamNotification(botUser), botUser));
                    }
                }
        }
        return CompletableFuture.completedFuture(botMessage);
    }


    private String getStudentInfo(InnerBotUser botUser) {
        String department = firstNotEmpty(botUser.getDepartment());
        department = department.equals("не указано")
                ? department
                : DepartmentConverter.getDepartmentByUrl(department);
        return "Факультет: " + department + "\n" +
                "Форма обучения: " + firstNotEmpty(botUser.getEducationForm()) + "\n" +
                "Группа: " + firstNotEmpty(botUser.getGroupNumber()) + "\n";
    }

    private String getScheduleNotificationSettingsFullTime(InnerBotUser botUser) {
        String userId = botUser.getUserId();
        BotUserSource source = botUser.getSource();

        ScheduleTodayNotification scheduleTodayNotification =
                scheduleTodayNotificationRepository.findByUserIdAndUserSource(userId, source);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Рассылка расписания на сегодня: ");

        if (null == scheduleTodayNotification) {
            stringBuilder.append("не подключена.");
        } else {
            if (scheduleTodayNotification.isEnabled()) {
                stringBuilder.append("вкл, ");
            } else {
                stringBuilder.append("выкл, ");
            }

            if (null == scheduleTodayNotification.getHourForSend()) {
                stringBuilder.append("время не указано.");
            } else {
                stringBuilder.append(scheduleTodayNotification.getHourForSend()).append(H_DOT);
            }
        }

        stringBuilder.append("\n\n");

        ScheduleTomorrowNotification scheduleTomorrowNotification =
                scheduleTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);

        stringBuilder.append("Рассылка расписания на завтра: ");

        if (null == scheduleTomorrowNotification) {
            stringBuilder.append("не подключена.");
        } else {
            if (scheduleTomorrowNotification.isEnabled()) {
                stringBuilder.append("вкл, ");
            } else {
                stringBuilder.append("выкл, ");
            }

            if (null == scheduleTomorrowNotification.getHourForSend()) {
                stringBuilder.append("время не указано.");
            } else {
                stringBuilder.append(scheduleTomorrowNotification.getHourForSend()).append(H_DOT);
            }
        }

        stringBuilder.append("\n\n");

        ExamPeriodTodayNotification examPeriodTodayNotification =
                examPeriodTodayNotificationRepository.findByUserIdAndUserSource(userId, source);
        stringBuilder.append("Рассылка расписания сессии на сегодня: ");

        if (null == examPeriodTodayNotification) {
            stringBuilder.append("не подключена.");
        } else {
            if (examPeriodTodayNotification.isEnabled()) {
                stringBuilder.append("вкл, ");
            } else {
                stringBuilder.append("выкл, ");
            }

            if (null == examPeriodTodayNotification.getHourForSend()) {
                stringBuilder.append("время не указано.");
            } else {
                stringBuilder.append(examPeriodTodayNotification.getHourForSend()).append(H_DOT);
            }
        }

        stringBuilder.append("\n\n");

        ExamPeriodTomorrowNotification examPeriodTomorrowNotification =
                examPeriodTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);
        stringBuilder.append("Рассылка расписания сессии на завтра: ");

        if (null == examPeriodTomorrowNotification) {
            stringBuilder.append("не подключена.");
        } else {
            if (examPeriodTomorrowNotification.isEnabled()) {
                stringBuilder.append("вкл, ");
            } else {
                stringBuilder.append("выкл, ");
            }

            if (null == examPeriodTomorrowNotification.getHourForSend()) {
                stringBuilder.append("время не указано.");
            } else {
                stringBuilder.append(examPeriodTomorrowNotification.getHourForSend()).append(H_DOT);
            }
        }

        stringBuilder.append("\n\n");

        ExamPeriodAfterTomorrowNotification examPeriodAfterTomorrowNotification =
                examPeriodAfterTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);
        stringBuilder.append("Рассылка расписания сессии на послезавтра: ");

        if (null == examPeriodAfterTomorrowNotification) {
            stringBuilder.append("не подключена.");
        } else {
            if (examPeriodAfterTomorrowNotification.isEnabled()) {
                stringBuilder.append("вкл, ");
            } else {
                stringBuilder.append("выкл, ");
            }

            if (null == examPeriodAfterTomorrowNotification.getHourForSend()) {
                stringBuilder.append("время не указано.");
            } else {
                stringBuilder.append(examPeriodAfterTomorrowNotification.getHourForSend()).append(H_DOT);
            }
        }

        return stringBuilder.toString();
    }

    private String getScheduleNotificationSettingsExtramural(InnerBotUser botUser) {
        String userId = botUser.getUserId();
        BotUserSource source = botUser.getSource();

        StringBuilder stringBuilder = new StringBuilder();

        ExtramuralEventTodayNotification extramuralEventTodayNotification =
                extramuralEventTodayNotificationRepository.findByUserIdAndUserSource(userId, source);
        stringBuilder.append("Рассылка расписания сессии на сегодня: ");

        if (null == extramuralEventTodayNotification) {
            stringBuilder.append("не подключена.");
        } else {
            if (extramuralEventTodayNotification.isEnabled()) {
                stringBuilder.append("вкл, ");
            } else {
                stringBuilder.append("выкл, ");
            }

            if (null == extramuralEventTodayNotification.getHourForSend()) {
                stringBuilder.append("время не указано.");
            } else {
                stringBuilder.append(extramuralEventTodayNotification.getHourForSend()).append(H_DOT);
            }
        }

        stringBuilder.append("\n\n");

        ExtramuralEventTomorrowNotification extramuralEventTomorrowNotification =
                extramuralEventTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);
        stringBuilder.append("Рассылка расписания сессии на завтра: ");

        if (null == extramuralEventTomorrowNotification) {
            stringBuilder.append("не подключена.");
        } else {
            if (extramuralEventTomorrowNotification.isEnabled()) {
                stringBuilder.append("вкл, ");
            } else {
                stringBuilder.append("выкл, ");
            }

            if (null == extramuralEventTomorrowNotification.getHourForSend()) {
                stringBuilder.append("время не указано.");
            } else {
                stringBuilder.append(extramuralEventTomorrowNotification.getHourForSend()).append(H_DOT);
            }
        }

        stringBuilder.append("\n\n");

        ExtramuralEventAfterTomorrowNotification extramuralEventAfterTomorrowNotification =
                extramuralEventAfterTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);
        stringBuilder.append("Рассылка расписания сессии на послезавтра: ");

        if (null == extramuralEventAfterTomorrowNotification) {
            stringBuilder.append("не подключена.");
        } else {
            if (extramuralEventAfterTomorrowNotification.isEnabled()) {
                stringBuilder.append("вкл, ");
            } else {
                stringBuilder.append("выкл, ");
            }

            if (null == extramuralEventAfterTomorrowNotification.getHourForSend()) {
                stringBuilder.append("время не указано.");
            } else {
                stringBuilder.append(extramuralEventAfterTomorrowNotification.getHourForSend()).append(H_DOT);
            }
        }

        return stringBuilder.toString();
    }

    private String firstNotEmpty(String string) {
        return StringUtils.isEmpty(string) ? "не указано" : string;
    }
}
