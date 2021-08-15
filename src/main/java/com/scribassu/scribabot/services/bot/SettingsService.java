package com.scribassu.scribabot.services.bot;

import com.scribassu.scribabot.dto.BotMessage;
import com.scribassu.scribabot.dto.InnerBotUser;
import com.scribassu.scribabot.entities.*;
import com.scribassu.scribabot.keyboard.TgKeyboardGenerator;
import com.scribassu.scribabot.keyboard.VkKeyboardGenerator;
import com.scribassu.scribabot.repositories.*;
import com.scribassu.scribabot.text.CommandText;
import com.scribassu.scribabot.text.MessageText;
import com.scribassu.scribabot.util.BotMessageUtils;
import com.scribassu.scribabot.util.BotUserSource;
import com.scribassu.scribabot.util.DepartmentConverter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SettingsService implements BotMessageService {

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
    private final VkKeyboardGenerator vkKeyboardGenerator;
    private final TgKeyboardGenerator tgKeyboardGenerator;

    private static final String CHOOSE_SCHEDULE_NOTIFICATION_TIME_TODAY = String.format(MessageText.CHOOSE_SCHEDULE_NOTIFICATION_TIME, "сегодня");
    private static final String CHOOSE_SCHEDULE_NOTIFICATION_TIME_TOMORROW = String.format(MessageText.CHOOSE_SCHEDULE_NOTIFICATION_TIME, "завтра");
    private static final String CHOOSE_EXAM_PERIOD_NOTIFICATION_TIME_TODAY = String.format(MessageText.CHOOSE_EXAM_PERIOD_NOTIFICATION_TIME, "сегодня");
    private static final String CHOOSE_EXAM_PERIOD_NOTIFICATION_TIME_TOMORROW = String.format(MessageText.CHOOSE_EXAM_PERIOD_NOTIFICATION_TIME, "завтра");
    private static final String CHOOSE_EXAM_PERIOD_NOTIFICATION_TIME_AFTER_TOMORROW = String.format(MessageText.CHOOSE_EXAM_PERIOD_NOTIFICATION_TIME, "послезавтра");

    @Autowired
    public SettingsService(VkBotUserRepository vkBotUserRepository,
                           TgBotUserRepository tgBotUserRepository,
                           ScheduleTodayNotificationRepository scheduleTodayNotificationRepository,
                           ScheduleTomorrowNotificationRepository scheduleTomorrowNotificationRepository,
                           ExamPeriodTodayNotificationRepository examPeriodTodayNotificationRepository,
                           ExamPeriodTomorrowNotificationRepository examPeriodTomorrowNotificationRepository,
                           ExamPeriodAfterTomorrowNotificationRepository examPeriodAfterTomorrowNotificationRepository,
                           ExtramuralEventTodayNotificationRepository extramuralEventTodayNotificationRepository,
                           ExtramuralEventTomorrowNotificationRepository extramuralEventTomorrowNotificationRepository,
                           ExtramuralEventAfterTomorrowNotificationRepository extramuralEventAfterTomorrowNotificationRepository,
                           VkKeyboardGenerator vkKeyboardGenerator,
                           TgKeyboardGenerator tgKeyboardGenerator) {
        this.vkBotUserRepository = vkBotUserRepository;
        this.tgBotUserRepository = tgBotUserRepository;
        this.scheduleTodayNotificationRepository = scheduleTodayNotificationRepository;
        this.scheduleTomorrowNotificationRepository = scheduleTomorrowNotificationRepository;
        this.examPeriodTodayNotificationRepository = examPeriodTodayNotificationRepository;
        this.examPeriodTomorrowNotificationRepository = examPeriodTomorrowNotificationRepository;
        this.examPeriodAfterTomorrowNotificationRepository = examPeriodAfterTomorrowNotificationRepository;
        this.extramuralEventTodayNotificationRepository = extramuralEventTodayNotificationRepository;
        this.extramuralEventTomorrowNotificationRepository = extramuralEventTomorrowNotificationRepository;
        this.extramuralEventAfterTomorrowNotificationRepository = extramuralEventAfterTomorrowNotificationRepository;
        this.vkKeyboardGenerator = vkKeyboardGenerator;
        this.tgKeyboardGenerator = tgKeyboardGenerator;
    }

    @Override
    public BotMessage getBotMessage(String message, InnerBotUser botUser) {
        BotMessage botMessage = new BotMessage();
        String userId = botUser.getUserId();
        BotUserSource source = botUser.getSource();

        switch (message) {
            case CommandText.SEND_EXAM_PERIOD:
                botMessage = botUser.fromVk() ?
                        new BotMessage(
                            "Здесь вы можете настроить рассылку расписания сессии.",
                            vkKeyboardGenerator.settingsExamNotification(botUser))
                        : new BotMessage(
                            "Здесь вы можете настроить рассылку расписания сессии.",
                            tgKeyboardGenerator.settingsExamNotification(botUser));

                break;
            case CommandText.SEND_SCHEDULE:
                botMessage = botUser.fromVk() ? new BotMessage(
                            "Здесь вы можете настроить рассылку расписания занятий.",
                            vkKeyboardGenerator.settingsScheduleNotification(botUser))
                        : new BotMessage(
                            "Здесь вы можете настроить рассылку расписания занятий.",
                            tgKeyboardGenerator.settingsScheduleNotification(botUser));
                break;
            case CommandText.SET_SEND_SCHEDULE_TIME_TODAY:
            case CommandText.ENABLE_SEND_SCHEDULE_TODAY:
            case CommandText.DISABLE_SEND_SCHEDULE_TODAY:
                botMessage = todayNotificationsSchedule(message, botUser);
                break;
            case CommandText.SET_SEND_SCHEDULE_TIME_TOMORROW:
            case CommandText.ENABLE_SEND_SCHEDULE_TOMORROW:
            case CommandText.DISABLE_SEND_SCHEDULE_TOMORROW:
                botMessage = tomorrowNotificationsSchedule(message, botUser);
                break;
            case CommandText.SET_SEND_EXAM_PERIOD_TIME_TODAY:
            case CommandText.ENABLE_SEND_EXAM_PERIOD_TODAY:
            case CommandText.DISABLE_SEND_EXAM_PERIOD_TODAY:
                botMessage = todayNotificationsExamPeriod(message, botUser);
                break;
            case CommandText.SET_SEND_EXAM_PERIOD_TIME_TOMORROW:
            case CommandText.ENABLE_SEND_EXAM_PERIOD_TOMORROW:
            case CommandText.DISABLE_SEND_EXAM_PERIOD_TOMORROW:
                botMessage = tomorrowNotificationsExamPeriod(message, botUser);
                break;
            case CommandText.SET_SEND_EXAM_PERIOD_TIME_AFTER_TOMORROW:
            case CommandText.ENABLE_SEND_EXAM_PERIOD_AFTER_TOMORROW:
            case CommandText.DISABLE_SEND_EXAM_PERIOD_AFTER_TOMORROW:
                botMessage = afterTomorrowNotificationsExamPeriod(message, botUser);
                break;
            case CommandText.ENABLE_FILTER_WEEK_TYPE:
                botUser.setFilterNomDenom(true);
                if (botUser.fromVk()) {
                    VkBotUser vkBotUser = vkBotUserRepository.findOneById(userId);
                    vkBotUser.setFilterNomDenom(true);
                    vkBotUserRepository.save(vkBotUser);
                    botMessage = new BotMessage(
                            "Включена фильтрация по типу недели.",
                            vkKeyboardGenerator.settings(botUser));
                } else {
                    TgBotUser tgBotUser = tgBotUserRepository.findOneById(userId);
                    tgBotUser.setFilterNomDenom(true);
                    tgBotUserRepository.save(tgBotUser);
                    botMessage = new BotMessage(
                            "Включена фильтрация по типу недели.",
                            tgKeyboardGenerator.settings(botUser));
                }
                break;
            case CommandText.DISABLE_FILTER_WEEK_TYPE:
                botUser.setFilterNomDenom(false);
                if (botUser.fromVk()) {
                    VkBotUser vkBotUser = vkBotUserRepository.findOneById(userId);
                    vkBotUser.setFilterNomDenom(false);
                    vkBotUserRepository.save(vkBotUser);
                    botMessage = new BotMessage(
                            "Выключена фильтрация по типу недели.",
                            vkKeyboardGenerator.settings(botUser));
                } else {
                    TgBotUser tgBotUser = tgBotUserRepository.findOneById(userId);
                    tgBotUser.setFilterNomDenom(false);
                    tgBotUserRepository.save(tgBotUser);
                    botMessage = new BotMessage(
                            "Выключена фильтрация по типу недели.",
                            tgKeyboardGenerator.settings(botUser));
                }
                break;
            case CommandText.CURRENT_USER_SETTINGS:
                String scheduleNotificationStatus = BotMessageUtils.isBotUserFullTime(botUser) ?
                        getScheduleNotificationStatusFullTime(botUser)
                        : getScheduleNotificationStatusExtramural(botUser);
                String currentUserSettings = getStudentInfo(botUser) + "\n" +
                        scheduleNotificationStatus + "\n\n" +
                        "Фильтрация по типу недели: " + (botUser.isFilterNomDenom() ? "вкл." : "выкл.");
                if (botUser.fromVk()) {
                    botMessage = new BotMessage(currentUserSettings, vkKeyboardGenerator.settings(botUser));
                } else {
                    botMessage = new BotMessage(currentUserSettings, tgKeyboardGenerator.settings(botUser));
                }
                break;
            case CommandText.DELETE_PROFILE:
                botMessage = new BotMessage(MessageText.DELETE_CONFIRMATION);
                if(botUser.fromVk()) {
                    botMessage.setVkKeyboard(VkKeyboardGenerator.confirmDeletion);
                } else {
                    botMessage.setTgKeyboard(TgKeyboardGenerator.confirmDeletion());
                }
                break;
            case CommandText.NO:
                if (botUser.fromVk()) {
                    botMessage = new BotMessage(
                            "Спасибо, что остаётесь с нами!",
                            vkKeyboardGenerator.settings(botUser));
                } else {
                    botMessage = new BotMessage(
                            "Спасибо, что остаётесь с нами!",
                            tgKeyboardGenerator.settings(botUser));
                }
                break;
            case CommandText.YES:
                if (botUser.fromVk()) {
                    vkBotUserRepository.deleteOneById(userId);
                } else {
                    tgBotUserRepository.deleteOneById(userId);
                }
                botMessage = new BotMessage(MessageText.BYE_MESSAGE);
                break;
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
                if (BotMessageUtils.isBotUserFullTime(botUser)) {
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
                if (BotMessageUtils.isBotUserFullTime(botUser)) {
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
                if (BotMessageUtils.isBotUserFullTime(botUser)) {
                    ExamPeriodAfterTomorrowNotification examPeriodAfterTomorrowNotification =
                            examPeriodAfterTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);

                    if (examPeriodAfterTomorrowNotification == null) {
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
            final String hourMessage = "Теперь расписание будет приходить в " + hourForSend + " ч.";
            botMessage = botUser.fromVk() ?
                    new BotMessage(hourMessage, vkKeyboardGenerator.settings(botUser))
                    : new BotMessage(hourMessage, tgKeyboardGenerator.settings(botUser));
        }

        return botMessage;
    }

    private BotMessage todayNotificationsSchedule(String message, InnerBotUser botUser) {
        BotMessage botMessage = new BotMessage();
        String userId = botUser.getUserId();
        BotUserSource source = botUser.getSource();

        switch (message) {
            case CommandText.SET_SEND_SCHEDULE_TIME_TODAY:
                String formatSchedule = String.format(MessageText.CHOOSE_SCHEDULE_NOTIFICATION_TIME, "сегодня");
                if (botUser.fromVk()) {
                    botMessage = new BotMessage(formatSchedule, VkKeyboardGenerator.hours);
                    VkBotUser vkBotUser = vkBotUserRepository.findOneById(userId);
                    vkBotUser.setPreviousUserMessage(formatSchedule);
                    vkBotUserRepository.save(vkBotUser);
                } else {
                    botMessage = new BotMessage(formatSchedule, TgKeyboardGenerator.hours());
                    TgBotUser tgBotUser = tgBotUserRepository.findOneById(userId);
                    tgBotUser.setPreviousUserMessage(formatSchedule);
                    tgBotUserRepository.save(tgBotUser);
                }
                break;
            case CommandText.ENABLE_SEND_SCHEDULE_TODAY:
                ScheduleTodayNotification scheduleTodayNotificationEn =
                        scheduleTodayNotificationRepository.findByUserIdAndUserSource(userId, source);
                if (null != scheduleTodayNotificationEn) {
                    scheduleTodayNotificationEn.setEnabled(true);
                    scheduleTodayNotificationRepository.save(scheduleTodayNotificationEn);
                    String enableNotificationMessage = String.format(MessageText.SCHEDULE_WILL_BE_SENT, "сегодня") +
                            scheduleTodayNotificationEn.getHourForSend() + " ч.";
                    if(botUser.fromVk()) {
                        botMessage = new BotMessage(
                                enableNotificationMessage,
                                vkKeyboardGenerator.settingsScheduleNotification(botUser));
                    } else {
                        botMessage = new BotMessage(
                                enableNotificationMessage,
                                tgKeyboardGenerator.settingsScheduleNotification(botUser));
                    }
                } else {
                    if (botUser.fromVk()) {
                        botMessage = new BotMessage(
                                "Вы еще не подключали рассылку расписания на сегодня. Подключите через '" +
                                        CommandText.SET_SEND_SCHEDULE_TIME_TODAY + "'.",
                                vkKeyboardGenerator.settingsScheduleNotification(botUser));
                    } else {
                        botMessage = new BotMessage(
                                "Вы еще не подключали рассылку расписания на сегодня. Подключите через '" +
                                        CommandText.SET_SEND_SCHEDULE_TIME_TODAY + "'.",
                                tgKeyboardGenerator.settingsScheduleNotification(botUser));
                    }
                }
                break;
            case CommandText.DISABLE_SEND_SCHEDULE_TODAY:
                ScheduleTodayNotification scheduleTodayNotificationDis =
                        scheduleTodayNotificationRepository.findByUserIdAndUserSource(userId, source);
                if (null != scheduleTodayNotificationDis) {
                    scheduleTodayNotificationDis.setEnabled(false);
                    scheduleTodayNotificationRepository.save(scheduleTodayNotificationDis);
                    if (botUser.fromVk()) {
                        botMessage = new BotMessage(
                                String.format(MessageText.SCHEDULE_IS_DISABLED, "сегодня"),
                                vkKeyboardGenerator.settingsScheduleNotification(botUser));
                    } else {
                        botMessage = new BotMessage(
                                String.format(MessageText.SCHEDULE_IS_DISABLED, "сегодня"),
                                tgKeyboardGenerator.settingsScheduleNotification(botUser));
                    }
                } else {
                    if (botUser.fromVk()) {
                        botMessage = new BotMessage(
                                "Вы еще не подключали рассылку расписания на сегодня. Подключите через '" +
                                        CommandText.SET_SEND_SCHEDULE_TIME_TODAY + "'.",
                                vkKeyboardGenerator.settingsScheduleNotification(botUser));
                    } else {
                        botMessage = new BotMessage(
                                "Вы еще не подключали рассылку расписания на сегодня. Подключите через '" +
                                        CommandText.SET_SEND_SCHEDULE_TIME_TODAY + "'.",
                                tgKeyboardGenerator.settingsScheduleNotification(botUser));
                    }
                }
                break;
        }

        return botMessage;
    }

    private BotMessage todayNotificationsExamPeriod(String message, InnerBotUser botUser) {
        BotMessage botMessage = new BotMessage();
        String userId = botUser.getUserId();
        BotUserSource source = botUser.getSource();

        switch (message) {
            case CommandText.SET_SEND_EXAM_PERIOD_TIME_TODAY:
                String formatSchedule = String.format(MessageText.CHOOSE_EXAM_PERIOD_NOTIFICATION_TIME, "сегодня");
                if (botUser.fromVk()) {
                    botMessage = new BotMessage(formatSchedule, VkKeyboardGenerator.hours);
                    vkBotUserRepository.updatePreviousUserMessage(formatSchedule, userId);
                } else {
                    botMessage = new BotMessage(formatSchedule, TgKeyboardGenerator.hours());
                    tgBotUserRepository.updatePreviousUserMessage(formatSchedule, userId);
                }
                break;
            case CommandText.ENABLE_SEND_EXAM_PERIOD_TODAY:
                if (BotMessageUtils.isBotUserFullTime(botUser)) {
                    ExamPeriodTodayNotification examPeriodTodayNotificationEn =
                            examPeriodTodayNotificationRepository.findByUserIdAndUserSource(userId, source);
                    if (null != examPeriodTodayNotificationEn) {
                        examPeriodTodayNotificationEn.setEnabled(true);
                        examPeriodTodayNotificationRepository.save(examPeriodTodayNotificationEn);
                        if (botUser.fromVk()) {
                            botMessage = new BotMessage(
                                    String.format(MessageText.EXAM_PERIOD_WILL_BE_SENT, "сегодня") +
                                            examPeriodTodayNotificationEn.getHourForSend() + " ч.",
                                    vkKeyboardGenerator.settingsExamNotification(botUser));
                        } else {
                            botMessage = new BotMessage(
                                    String.format(MessageText.EXAM_PERIOD_WILL_BE_SENT, "сегодня") +
                                            examPeriodTodayNotificationEn.getHourForSend() + " ч.",
                                    tgKeyboardGenerator.settingsExamNotification(botUser));
                        }
                    } else {
                        if (botUser.fromVk()) {
                            botMessage = new BotMessage(
                                    "Вы еще не подключали рассылку расписания сессии на сегодня. Подключите через '" +
                                            CommandText.SET_SEND_EXAM_PERIOD_TIME_TODAY + "'.",
                                    vkKeyboardGenerator.settingsExamNotification(botUser));
                        } else {
                            botMessage = new BotMessage(
                                    "Вы еще не подключали рассылку расписания сессии на сегодня. Подключите через '" +
                                            CommandText.SET_SEND_EXAM_PERIOD_TIME_TODAY + "'.",
                                    tgKeyboardGenerator.settingsExamNotification(botUser));
                        }
                    }
                } else if (BotMessageUtils.isBotUserExtramural(botUser)) {
                    ExtramuralEventTodayNotification extramuralEventTodayNotificationEn =
                            extramuralEventTodayNotificationRepository.findByUserIdAndUserSource(userId, source);
                    if (null != extramuralEventTodayNotificationEn) {
                        extramuralEventTodayNotificationEn.setEnabled(true);
                        extramuralEventTodayNotificationRepository.save(extramuralEventTodayNotificationEn);
                        if (botUser.fromVk()) {
                            botMessage = new BotMessage(
                                    String.format(MessageText.EXAM_PERIOD_WILL_BE_SENT, "сегодня") +
                                            extramuralEventTodayNotificationEn.getHourForSend() + " ч.",
                                    vkKeyboardGenerator.settingsExamNotification(botUser));
                        } else {
                            botMessage = new BotMessage(
                                    String.format(MessageText.EXAM_PERIOD_WILL_BE_SENT, "сегодня") +
                                            extramuralEventTodayNotificationEn.getHourForSend() + " ч.",
                                    tgKeyboardGenerator.settingsExamNotification(botUser));
                        }
                    } else {
                        if (botUser.fromVk()) {
                            botMessage = new BotMessage(
                                    "Вы еще не подключали рассылку расписания сессии на сегодня. Подключите через '" +
                                            CommandText.SET_SEND_EXAM_PERIOD_TIME_TODAY + "'.",
                                    vkKeyboardGenerator.settingsExamNotification(botUser));
                        } else {
                            botMessage = new BotMessage(
                                    "Вы еще не подключали рассылку расписания сессии на сегодня. Подключите через '" +
                                            CommandText.SET_SEND_EXAM_PERIOD_TIME_TODAY + "'.",
                                    tgKeyboardGenerator.settingsExamNotification(botUser));
                        }
                    }
                }
                break;
            case CommandText.DISABLE_SEND_EXAM_PERIOD_TODAY:
                if (BotMessageUtils.isBotUserFullTime(botUser)) {
                    ExamPeriodTodayNotification examPeriodTodayNotificationDis =
                            examPeriodTodayNotificationRepository.findByUserIdAndUserSource(userId, source);
                    if (null != examPeriodTodayNotificationDis) {
                        examPeriodTodayNotificationDis.setEnabled(false);
                        examPeriodTodayNotificationRepository.save(examPeriodTodayNotificationDis);
                        if (botUser.fromVk()) {
                            botMessage = new BotMessage(
                                    String.format(MessageText.EXAM_PERIOD_IS_DISABLED, "сегодня"),
                                    vkKeyboardGenerator.settingsExamNotification(botUser));
                        } else {
                            botMessage = new BotMessage(
                                    String.format(MessageText.EXAM_PERIOD_IS_DISABLED, "сегодня"),
                                    tgKeyboardGenerator.settingsExamNotification(botUser));
                        }
                    } else {
                        if (botUser.fromVk()) {
                            botMessage = new BotMessage(
                                    "Вы еще не подключали рассылку расписания сессии на сегодня. Подключите через '" +
                                            CommandText.SET_SEND_EXAM_PERIOD_TIME_TODAY + "'.",
                                    vkKeyboardGenerator.settingsExamNotification(botUser));
                        } else {
                            botMessage = new BotMessage(
                                    "Вы еще не подключали рассылку расписания сессии на сегодня. Подключите через '" +
                                            CommandText.SET_SEND_EXAM_PERIOD_TIME_TODAY + "'.",
                                    tgKeyboardGenerator.settingsExamNotification(botUser));
                        }
                    }
                } else if (BotMessageUtils.isBotUserExtramural(botUser)) {
                    ExtramuralEventTodayNotification extramuralEventTodayNotificationDis =
                            extramuralEventTodayNotificationRepository.findByUserIdAndUserSource(userId, source);
                    if (null != extramuralEventTodayNotificationDis) {
                        extramuralEventTodayNotificationDis.setEnabled(false);
                        extramuralEventTodayNotificationRepository.save(extramuralEventTodayNotificationDis);
                        if (botUser.fromVk()) {
                            botMessage = new BotMessage(
                                    String.format(MessageText.EXAM_PERIOD_IS_DISABLED, "сегодня"),
                                    vkKeyboardGenerator.settingsExamNotification(botUser));
                        } else {
                            botMessage = new BotMessage(
                                    String.format(MessageText.EXAM_PERIOD_IS_DISABLED, "сегодня"),
                                    tgKeyboardGenerator.settingsExamNotification(botUser));
                        }
                    } else {
                        if (botUser.fromVk()) {
                            botMessage = new BotMessage(
                                    "Вы еще не подключали рассылку расписания сессии на сегодня. Подключите через '" +
                                            CommandText.SET_SEND_EXAM_PERIOD_TIME_TODAY + "'.",
                                    vkKeyboardGenerator.settingsExamNotification(botUser));
                        } else {
                            botMessage = new BotMessage(
                                    "Вы еще не подключали рассылку расписания сессии на сегодня. Подключите через '" +
                                            CommandText.SET_SEND_EXAM_PERIOD_TIME_TODAY + "'.",
                                    tgKeyboardGenerator.settingsExamNotification(botUser));
                        }
                    }
                }
                break;
        }

        return botMessage;
    }

    private BotMessage tomorrowNotificationsSchedule(String message, InnerBotUser botUser) {
        BotMessage botMessage = new BotMessage();
        String userId = botUser.getUserId();
        BotUserSource source = botUser.getSource();

        switch (message) {
            case CommandText.SET_SEND_SCHEDULE_TIME_TOMORROW:
                String formatSchedule = String.format(MessageText.CHOOSE_SCHEDULE_NOTIFICATION_TIME, "завтра");
                if (botUser.fromVk()) {
                    botMessage = new BotMessage(formatSchedule, VkKeyboardGenerator.hours);
                    vkBotUserRepository.updatePreviousUserMessage(formatSchedule, userId);
                } else {
                    botMessage = new BotMessage(formatSchedule, TgKeyboardGenerator.hours());
                    tgBotUserRepository.updatePreviousUserMessage(formatSchedule, userId);
                }
                break;
            case CommandText.ENABLE_SEND_SCHEDULE_TOMORROW:
                ScheduleTomorrowNotification scheduleTomorrowNotificationEn =
                        scheduleTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);
                if (scheduleTomorrowNotificationEn != null && !scheduleTomorrowNotificationEn.isEnabled()) {
                    scheduleTomorrowNotificationEn.setEnabled(true);
                    scheduleTomorrowNotificationRepository.save(scheduleTomorrowNotificationEn);
                    botMessage = new BotMessage(
                            String.format(MessageText.SCHEDULE_WILL_BE_SENT, "завтра") +
                                    scheduleTomorrowNotificationEn.getHourForSend() + " ч.",
                            vkKeyboardGenerator.settingsScheduleNotification(botUser));
                } else {
                    if (null == scheduleTomorrowNotificationEn) {
                        if (botUser.fromVk()) {
                            botMessage = new BotMessage(
                                    "Вы еще не подключали рассылку расписания на завтра. Подключите через '" +
                                            CommandText.SET_SEND_SCHEDULE_TIME_TOMORROW + "'.",
                                    vkKeyboardGenerator.settingsScheduleNotification(botUser));
                        } else {
                            botMessage = new BotMessage(
                                    "Вы еще не подключали рассылку расписания на завтра. Подключите через '" +
                                            CommandText.SET_SEND_SCHEDULE_TIME_TOMORROW + "'.",
                                    tgKeyboardGenerator.settingsScheduleNotification(botUser));
                        }
                    } else {
                        if (botUser.fromVk()) {
                            botMessage = new BotMessage(
                                    String.format(MessageText.SCHEDULE_IS_ENABLED_DOUBLE, "завтра"),
                                    vkKeyboardGenerator.settingsScheduleNotification(botUser));
                        } else {
                            botMessage = new BotMessage(
                                    String.format(MessageText.SCHEDULE_IS_ENABLED_DOUBLE, "завтра"),
                                    tgKeyboardGenerator.settingsScheduleNotification(botUser));
                        }
                    }
                }
                break;
            case CommandText.DISABLE_SEND_SCHEDULE_TOMORROW:
                ScheduleTomorrowNotification scheduleTomorrowNotificationDis =
                        scheduleTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);
                if (null != scheduleTomorrowNotificationDis) {
                    scheduleTomorrowNotificationDis.setEnabled(false);
                    scheduleTomorrowNotificationRepository.save(scheduleTomorrowNotificationDis);
                    if (botUser.fromVk()) {
                        botMessage = new BotMessage(
                                String.format(MessageText.SCHEDULE_IS_DISABLED, "завтра"),
                                vkKeyboardGenerator.settingsScheduleNotification(botUser));
                    } else {
                        botMessage = new BotMessage(
                                String.format(MessageText.SCHEDULE_IS_DISABLED, "завтра"),
                                tgKeyboardGenerator.settingsScheduleNotification(botUser));
                    }
                } else {
                    if (botUser.fromVk()) {
                        botMessage = new BotMessage(
                                "Вы еще не подключали рассылку расписания на завтра. Подключите через '" +
                                        CommandText.SET_SEND_SCHEDULE_TIME_TOMORROW + "'.",
                                vkKeyboardGenerator.settingsScheduleNotification(botUser));
                    } else {
                        botMessage = new BotMessage(
                                "Вы еще не подключали рассылку расписания на завтра. Подключите через '" +
                                        CommandText.SET_SEND_SCHEDULE_TIME_TOMORROW + "'.",
                                tgKeyboardGenerator.settingsScheduleNotification(botUser));
                    }
                }
                break;
        }

        return botMessage;
    }

    private BotMessage tomorrowNotificationsExamPeriod(String message, InnerBotUser botUser) {
        BotMessage botMessage = new BotMessage();
        String userId = botUser.getUserId();
        BotUserSource source = botUser.getSource();

        switch (message) {
            case CommandText.SET_SEND_EXAM_PERIOD_TIME_TOMORROW:
                String formatSchedule = String.format(MessageText.CHOOSE_EXAM_PERIOD_NOTIFICATION_TIME, "завтра");
                if (botUser.fromVk()) {
                    botMessage = new BotMessage(formatSchedule, VkKeyboardGenerator.hours);
                    vkBotUserRepository.updatePreviousUserMessage(formatSchedule, userId);
                } else {
                    botMessage = new BotMessage(formatSchedule, TgKeyboardGenerator.hours());
                    tgBotUserRepository.updatePreviousUserMessage(formatSchedule, userId);
                }
                break;
            case CommandText.ENABLE_SEND_EXAM_PERIOD_TOMORROW:
                if (BotMessageUtils.isBotUserFullTime(botUser)) {
                    ExamPeriodTomorrowNotification examPeriodTomorrowNotificationEn =
                            examPeriodTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);
                    if (null != examPeriodTomorrowNotificationEn) {
                        examPeriodTomorrowNotificationEn.setEnabled(true);
                        examPeriodTomorrowNotificationRepository.save(examPeriodTomorrowNotificationEn);
                        if (botUser.fromVk()) {
                            botMessage = new BotMessage(
                                    String.format(MessageText.EXAM_PERIOD_WILL_BE_SENT, "завтра") +
                                            examPeriodTomorrowNotificationEn.getHourForSend() + " ч.",
                                    vkKeyboardGenerator.settingsExamNotification(botUser));
                        } else {
                            botMessage = new BotMessage(
                                    String.format(MessageText.EXAM_PERIOD_WILL_BE_SENT, "завтра") +
                                            examPeriodTomorrowNotificationEn.getHourForSend() + " ч.",
                                    tgKeyboardGenerator.settingsExamNotification(botUser));
                        }
                    } else {
                        if (botUser.fromVk()) {
                            botMessage = new BotMessage(
                                    "Вы еще не подключали рассылку расписания сессии на завтра. Подключите через '" +
                                            CommandText.SET_SEND_EXAM_PERIOD_TIME_TOMORROW + "'.",
                                    vkKeyboardGenerator.settingsExamNotification(botUser));
                        } else {
                            botMessage = new BotMessage(
                                    "Вы еще не подключали рассылку расписания сессии на завтра. Подключите через '" +
                                            CommandText.SET_SEND_EXAM_PERIOD_TIME_TOMORROW + "'.",
                                    tgKeyboardGenerator.settingsExamNotification(botUser));
                        }
                    }
                } else if (BotMessageUtils.isBotUserExtramural(botUser)) {
                    ExtramuralEventTomorrowNotification extramuralEventTomorrowNotificationEn =
                            extramuralEventTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);
                    if (null != extramuralEventTomorrowNotificationEn) {
                        extramuralEventTomorrowNotificationEn.setEnabled(true);
                        extramuralEventTomorrowNotificationRepository.save(extramuralEventTomorrowNotificationEn);
                        if (botUser.fromVk()) {
                            botMessage = new BotMessage(
                                    String.format(MessageText.EXAM_PERIOD_WILL_BE_SENT, "завтра") +
                                            extramuralEventTomorrowNotificationEn.getHourForSend() + " ч.",
                                    vkKeyboardGenerator.settingsExamNotification(botUser));
                        } else {
                            botMessage = new BotMessage(
                                    String.format(MessageText.EXAM_PERIOD_WILL_BE_SENT, "завтра") +
                                            extramuralEventTomorrowNotificationEn.getHourForSend() + " ч.",
                                    tgKeyboardGenerator.settingsExamNotification(botUser));
                        }
                    } else {
                        if (botUser.fromVk()) {
                            botMessage = new BotMessage(
                                    "Вы еще не подключали рассылку расписания сессии на завтра. Подключите через '" +
                                            CommandText.SET_SEND_EXAM_PERIOD_TIME_TOMORROW + "'.",
                                    vkKeyboardGenerator.settingsExamNotification(botUser));
                        } else {
                            botMessage = new BotMessage(
                                    "Вы еще не подключали рассылку расписания сессии на завтра. Подключите через '" +
                                            CommandText.SET_SEND_EXAM_PERIOD_TIME_TOMORROW + "'.",
                                    tgKeyboardGenerator.settingsExamNotification(botUser));
                        }
                    }
                }
                break;
            case CommandText.DISABLE_SEND_EXAM_PERIOD_TOMORROW:
                if (BotMessageUtils.isBotUserFullTime(botUser)) {
                    ExamPeriodTomorrowNotification examPeriodTomorrowNotificationDis =
                            examPeriodTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);
                    if (null != examPeriodTomorrowNotificationDis) {
                        examPeriodTomorrowNotificationDis.setEnabled(false);
                        examPeriodTomorrowNotificationRepository.save(examPeriodTomorrowNotificationDis);
                        if (botUser.fromVk()) {
                            botMessage = new BotMessage(
                                    String.format(MessageText.EXAM_PERIOD_IS_DISABLED, "завтра"),
                                    vkKeyboardGenerator.settingsExamNotification(botUser));
                        } else {
                            botMessage = new BotMessage(
                                    String.format(MessageText.EXAM_PERIOD_IS_DISABLED, "завтра"),
                                    tgKeyboardGenerator.settingsExamNotification(botUser));
                        }
                    } else {
                        if (botUser.fromVk()) {
                            botMessage = new BotMessage(
                                    "Вы еще не подключали рассылку расписания сессии на завтра. Подключите через '" +
                                            CommandText.SET_SEND_EXAM_PERIOD_TIME_TOMORROW + "'.",
                                    vkKeyboardGenerator.settingsExamNotification(botUser));
                        } else {
                            botMessage = new BotMessage(
                                    "Вы еще не подключали рассылку расписания сессии на завтра. Подключите через '" +
                                            CommandText.SET_SEND_EXAM_PERIOD_TIME_TOMORROW + "'.",
                                    tgKeyboardGenerator.settingsExamNotification(botUser));
                        }
                    }
                } else if (BotMessageUtils.isBotUserExtramural(botUser)) {
                    ExtramuralEventTomorrowNotification extramuralEventTomorrowNotificationDis =
                            extramuralEventTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);
                    if (null != extramuralEventTomorrowNotificationDis) {
                        extramuralEventTomorrowNotificationDis.setEnabled(false);
                        extramuralEventTomorrowNotificationRepository.save(extramuralEventTomorrowNotificationDis);
                        if (botUser.fromVk()) {
                            botMessage = new BotMessage(
                                    String.format(MessageText.EXAM_PERIOD_IS_DISABLED, "завтра"),
                                    vkKeyboardGenerator.settingsExamNotification(botUser));
                        } else {
                            botMessage = new BotMessage(
                                    String.format(MessageText.EXAM_PERIOD_IS_DISABLED, "завтра"),
                                    tgKeyboardGenerator.settingsExamNotification(botUser));
                        }
                    } else {
                        if (botUser.fromVk()) {
                            botMessage = new BotMessage(
                                    "Вы еще не подключали рассылку расписания сессии на завтра. Подключите через '" +
                                            CommandText.SET_SEND_EXAM_PERIOD_TIME_TOMORROW + "'.",
                                    vkKeyboardGenerator.settingsExamNotification(botUser));
                        } else {
                            botMessage = new BotMessage(
                                    "Вы еще не подключали рассылку расписания сессии на завтра. Подключите через '" +
                                            CommandText.SET_SEND_EXAM_PERIOD_TIME_TOMORROW + "'.",
                                    tgKeyboardGenerator.settingsExamNotification(botUser));
                        }
                    }
                }
                break;
        }

        return botMessage;
    }

    private BotMessage afterTomorrowNotificationsExamPeriod(String message, InnerBotUser botUser) {
        BotMessage botMessage = new BotMessage();
        String userId = botUser.getUserId();
        BotUserSource source = botUser.getSource();

        switch (message) {
            case CommandText.SET_SEND_EXAM_PERIOD_TIME_AFTER_TOMORROW:
                String formatSchedule = String.format(MessageText.CHOOSE_EXAM_PERIOD_NOTIFICATION_TIME, "послезавтра");
                if (botUser.fromVk()) {
                    botMessage = new BotMessage(formatSchedule, VkKeyboardGenerator.hours);
                    vkBotUserRepository.updatePreviousUserMessage(formatSchedule, userId);
                } else {
                    botMessage = new BotMessage(formatSchedule, TgKeyboardGenerator.hours());
                    tgBotUserRepository.updatePreviousUserMessage(formatSchedule, userId);
                }
                break;
            case CommandText.ENABLE_SEND_EXAM_PERIOD_AFTER_TOMORROW:
                if (BotMessageUtils.isBotUserFullTime(botUser)) {
                    ExamPeriodAfterTomorrowNotification examPeriodAfterTomorrowNotificationEn =
                            examPeriodAfterTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);
                    if (null != examPeriodAfterTomorrowNotificationEn) {
                        examPeriodAfterTomorrowNotificationEn.setEnabled(true);
                        examPeriodAfterTomorrowNotificationRepository.save(examPeriodAfterTomorrowNotificationEn);
                        if (botUser.fromVk()) {
                            botMessage = new BotMessage(
                                    String.format(MessageText.EXAM_PERIOD_WILL_BE_SENT, "послезавтра") +
                                            examPeriodAfterTomorrowNotificationEn.getHourForSend() + " ч.",
                                    vkKeyboardGenerator.settingsExamNotification(botUser));
                        } else {
                            botMessage = new BotMessage(
                                    String.format(MessageText.EXAM_PERIOD_WILL_BE_SENT, "послезавтра") +
                                            examPeriodAfterTomorrowNotificationEn.getHourForSend() + " ч.",
                                    tgKeyboardGenerator.settingsExamNotification(botUser));
                        }
                    } else {
                        if (botUser.fromVk()) {
                            botMessage = new BotMessage(
                                    "Вы еще не подключали рассылку расписания сессии на послезавтра. Подключите через '" +
                                            CommandText.SET_SEND_EXAM_PERIOD_TIME_AFTER_TOMORROW + "'.",
                                    vkKeyboardGenerator.settingsExamNotification(botUser));
                        } else {
                            botMessage = new BotMessage(
                                    "Вы еще не подключали рассылку расписания сессии на послезавтра. Подключите через '" +
                                            CommandText.SET_SEND_EXAM_PERIOD_TIME_AFTER_TOMORROW + "'.",
                                    tgKeyboardGenerator.settingsExamNotification(botUser));
                        }
                    }
                } else if (BotMessageUtils.isBotUserExtramural(botUser)) {
                    ExtramuralEventAfterTomorrowNotification extramuralEventAfterTomorrowNotificationEn =
                            extramuralEventAfterTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);
                    if (null != extramuralEventAfterTomorrowNotificationEn) {
                        extramuralEventAfterTomorrowNotificationEn.setEnabled(true);
                        extramuralEventAfterTomorrowNotificationRepository.save(extramuralEventAfterTomorrowNotificationEn);
                        if (botUser.fromVk()) {
                            botMessage = new BotMessage(
                                    String.format(MessageText.EXAM_PERIOD_WILL_BE_SENT, "послезавтра") +
                                            extramuralEventAfterTomorrowNotificationEn.getHourForSend() + " ч.",
                                    vkKeyboardGenerator.settingsExamNotification(botUser));
                        } else {
                            botMessage = new BotMessage(
                                    String.format(MessageText.EXAM_PERIOD_WILL_BE_SENT, "послезавтра") +
                                            extramuralEventAfterTomorrowNotificationEn.getHourForSend() + " ч.",
                                    tgKeyboardGenerator.settingsExamNotification(botUser));
                        }
                    } else {
                        if (botUser.fromVk()) {
                            botMessage = new BotMessage(
                                    "Вы еще не подключали рассылку расписания сессии на послезавтра. Подключите через '" +
                                            CommandText.SET_SEND_EXAM_PERIOD_TIME_AFTER_TOMORROW + "'.",
                                    vkKeyboardGenerator.settingsExamNotification(botUser));
                        } else {
                            botMessage = new BotMessage(
                                    "Вы еще не подключали рассылку расписания сессии на послезавтра. Подключите через '" +
                                            CommandText.SET_SEND_EXAM_PERIOD_TIME_AFTER_TOMORROW + "'.",
                                    tgKeyboardGenerator.settingsExamNotification(botUser));
                        }
                    }
                }
                break;
            case CommandText.DISABLE_SEND_EXAM_PERIOD_AFTER_TOMORROW:
                if (BotMessageUtils.isBotUserFullTime(botUser)) {
                    ExamPeriodAfterTomorrowNotification examPeriodAfterTomorrowNotificationDis =
                            examPeriodAfterTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);
                    if (null != examPeriodAfterTomorrowNotificationDis) {
                        examPeriodAfterTomorrowNotificationDis.setEnabled(false);
                        examPeriodAfterTomorrowNotificationRepository.save(examPeriodAfterTomorrowNotificationDis);
                        if (botUser.fromVk()) {
                            botMessage = new BotMessage(
                                    String.format(MessageText.EXAM_PERIOD_IS_DISABLED, "послезавтра"),
                                    vkKeyboardGenerator.settingsExamNotification(botUser));
                        } else {
                            botMessage = new BotMessage(
                                    String.format(MessageText.EXAM_PERIOD_IS_DISABLED, "послезавтра"),
                                    tgKeyboardGenerator.settingsExamNotification(botUser));
                        }
                    } else {
                        if (botUser.fromVk()) {
                            botMessage = new BotMessage(
                                    "Вы еще не подключали рассылку расписания сессии на послезавтра. Подключите через '" +
                                            CommandText.SET_SEND_EXAM_PERIOD_TIME_AFTER_TOMORROW + "'.",
                                    vkKeyboardGenerator.settingsExamNotification(botUser));
                        } else {
                            botMessage = new BotMessage(
                                    "Вы еще не подключали рассылку расписания сессии на послезавтра. Подключите через '" +
                                            CommandText.SET_SEND_EXAM_PERIOD_TIME_AFTER_TOMORROW + "'.",
                                    tgKeyboardGenerator.settingsExamNotification(botUser));
                        }
                    }
                } else if (BotMessageUtils.isBotUserExtramural(botUser)) {
                    ExtramuralEventAfterTomorrowNotification extramuralEventAfterTomorrowNotificationDis =
                            extramuralEventAfterTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);
                    if (null != extramuralEventAfterTomorrowNotificationDis) {
                        extramuralEventAfterTomorrowNotificationDis.setEnabled(false);
                        extramuralEventAfterTomorrowNotificationRepository.save(extramuralEventAfterTomorrowNotificationDis);
                        if (botUser.fromVk()) {
                            botMessage = new BotMessage(
                                    String.format(MessageText.EXAM_PERIOD_IS_DISABLED, "послезавтра"),
                                    vkKeyboardGenerator.settingsExamNotification(botUser));
                        } else {
                            botMessage = new BotMessage(
                                    String.format(MessageText.EXAM_PERIOD_IS_DISABLED, "послезавтра"),
                                    tgKeyboardGenerator.settingsExamNotification(botUser));
                        }
                    } else {
                        if (botUser.fromVk()) {
                            botMessage = new BotMessage(
                                    "Вы еще не подключали рассылку расписания сессии на послезавтра. Подключите через '" +
                                            CommandText.SET_SEND_EXAM_PERIOD_TIME_AFTER_TOMORROW + "'.",
                                    vkKeyboardGenerator.settingsExamNotification(botUser));
                        } else {
                            botMessage = new BotMessage(
                                    "Вы еще не подключали рассылку расписания сессии на послезавтра. Подключите через '" +
                                            CommandText.SET_SEND_EXAM_PERIOD_TIME_AFTER_TOMORROW + "'.",
                                    tgKeyboardGenerator.settingsExamNotification(botUser));
                        }
                    }
                }
                break;
        }

        return botMessage;
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

    private String getScheduleNotificationStatusFullTime(InnerBotUser botUser) {
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
                stringBuilder.append(scheduleTodayNotification.getHourForSend()).append(" ч.");
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
                stringBuilder.append(scheduleTomorrowNotification.getHourForSend()).append(" ч.");
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
                stringBuilder.append(examPeriodTodayNotification.getHourForSend()).append(" ч.");
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
                stringBuilder.append(examPeriodTomorrowNotification.getHourForSend()).append(" ч.");
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
                stringBuilder.append(examPeriodAfterTomorrowNotification.getHourForSend()).append(" ч.");
            }
        }

        return stringBuilder.toString();
    }

    private String getScheduleNotificationStatusExtramural(InnerBotUser botUser) {
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
                stringBuilder.append(extramuralEventTodayNotification.getHourForSend()).append(" ч.");
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
                stringBuilder.append(extramuralEventTomorrowNotification.getHourForSend()).append(" ч.");
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
                stringBuilder.append(extramuralEventAfterTomorrowNotification.getHourForSend()).append(" ч.");
            }
        }

        return stringBuilder.toString();
    }

    private String firstNotEmpty(String string) {
        return StringUtils.isEmpty(string) ? "не указано" : string;
    }
}
