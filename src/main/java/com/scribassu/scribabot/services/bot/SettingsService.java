package com.scribassu.scribabot.services.bot;

import com.scribassu.scribabot.dto.BotMessage;
import com.scribassu.scribabot.entities.*;
import com.scribassu.scribabot.keyboard.KeyboardGenerator;
import com.scribassu.scribabot.repositories.*;
import com.scribassu.scribabot.text.CommandText;
import com.scribassu.scribabot.text.MessageText;
import com.scribassu.scribabot.util.BotMessageUtils;
import com.scribassu.scribabot.util.DepartmentConverter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.scribassu.scribabot.keyboard.KeyboardType.*;

@Service
public class SettingsService implements BotMessageService {

    private final BotUserRepository botUserRepository;
    private final ScheduleTodayNotificationRepository scheduleTodayNotificationRepository;
    private final ScheduleTomorrowNotificationRepository scheduleTomorrowNotificationRepository;
    private final ExamPeriodTodayNotificationRepository examPeriodTodayNotificationRepository;
    private final ExamPeriodTomorrowNotificationRepository examPeriodTomorrowNotificationRepository;
    private final ExamPeriodAfterTomorrowNotificationRepository examPeriodAfterTomorrowNotificationRepository;
    private final ExtramuralEventTodayNotificationRepository extramuralEventTodayNotificationRepository;
    private final ExtramuralEventTomorrowNotificationRepository extramuralEventTomorrowNotificationRepository;
    private final ExtramuralEventAfterTomorrowNotificationRepository extramuralEventAfterTomorrowNotificationRepository;
    private final KeyboardGenerator keyboardGenerator;

    @Autowired
    public SettingsService(BotUserRepository botUserRepository,
                           ScheduleTodayNotificationRepository scheduleTodayNotificationRepository,
                           ScheduleTomorrowNotificationRepository scheduleTomorrowNotificationRepository,
                           ExamPeriodTodayNotificationRepository examPeriodTodayNotificationRepository,
                           ExamPeriodTomorrowNotificationRepository examPeriodTomorrowNotificationRepository,
                           ExamPeriodAfterTomorrowNotificationRepository examPeriodAfterTomorrowNotificationRepository,
                           ExtramuralEventTodayNotificationRepository extramuralEventTodayNotificationRepository,
                           ExtramuralEventTomorrowNotificationRepository extramuralEventTomorrowNotificationRepository,
                           ExtramuralEventAfterTomorrowNotificationRepository extramuralEventAfterTomorrowNotificationRepository,
                           KeyboardGenerator keyboardGenerator) {
        this.botUserRepository = botUserRepository;
        this.scheduleTodayNotificationRepository = scheduleTodayNotificationRepository;
        this.scheduleTomorrowNotificationRepository = scheduleTomorrowNotificationRepository;
        this.examPeriodTodayNotificationRepository = examPeriodTodayNotificationRepository;
        this.examPeriodTomorrowNotificationRepository = examPeriodTomorrowNotificationRepository;
        this.examPeriodAfterTomorrowNotificationRepository = examPeriodAfterTomorrowNotificationRepository;
        this.extramuralEventTodayNotificationRepository = extramuralEventTodayNotificationRepository;
        this.extramuralEventTomorrowNotificationRepository = extramuralEventTomorrowNotificationRepository;
        this.extramuralEventAfterTomorrowNotificationRepository = extramuralEventAfterTomorrowNotificationRepository;
        this.keyboardGenerator = keyboardGenerator;
    }

    @Override
    public BotMessage getBotMessage(String message, BotUser botUser) {
        BotMessage botMessage = new BotMessage();
        String userId = botUser.getUserId();

        switch(message) {
            case CommandText.SEND_EXAM_PERIOD:
                botMessage = new BotMessage(
                        "Здесь вы можете настроить рассылку расписания сессии.",
                        keyboardGenerator.buildSettingsExamNotification(botUser));
                break;
            case CommandText.SEND_SCHEDULE:
                botMessage = new BotMessage(
                        "Здесь вы можете настроить рассылку расписания занятий.",
                        keyboardGenerator.buildSettingsScheduleNotification(botUser));
                break;
            case CommandText.SET_SEND_SCHEDULE_TIME_TODAY:
                String formatSchedule = String.format(MessageText.CHOOSE_SCHEDULE_NOTIFICATION_TIME, "сегодня");
                botMessage = new BotMessage(formatSchedule, ButtonHours);
                botUser.setPreviousUserMessage(formatSchedule);
                botUserRepository.save(botUser);
                break;
            case CommandText.ENABLE_SEND_SCHEDULE_TODAY:
                ScheduleTodayNotification scheduleTodayNotificationEn =
                        scheduleTodayNotificationRepository.findByUserId(userId);
                if(scheduleTodayNotificationEn != null && !scheduleTodayNotificationEn.isEnabled()) {
                    scheduleTodayNotificationEn.setEnabled(true);
                    scheduleTodayNotificationRepository.save(scheduleTodayNotificationEn);
                    botMessage = new BotMessage(String.format(MessageText.SCHEDULE_WILL_BE_SENT, "сегодня") +
                            scheduleTodayNotificationEn.getHourForSend() + " ч.",
                            keyboardGenerator.buildSettingsScheduleNotification(botUser));
                }
                else {
                    if(scheduleTodayNotificationEn == null) {
                        botMessage = new BotMessage(
                                "Вы еще не подключали рассылку расписания на сегодня. Подключите через '" +
                                CommandText.SET_SEND_SCHEDULE_TIME_TODAY + "'.",
                                keyboardGenerator.buildSettingsScheduleNotification(botUser));
                    }
                    else {
                        botMessage = new BotMessage(
                                String.format(MessageText.SCHEDULE_IS_ENABLED_DOUBLE, "сегодня"),
                                keyboardGenerator.buildSettingsScheduleNotification(botUser));
                    }
                }
                break;
            case CommandText.DISABLE_SEND_SCHEDULE_TODAY:
                ScheduleTodayNotification scheduleTodayNotificationDis =
                        scheduleTodayNotificationRepository.findByUserId(userId);
                if(scheduleTodayNotificationDis != null && scheduleTodayNotificationDis.isEnabled()) {
                    scheduleTodayNotificationDis.setEnabled(false);
                    scheduleTodayNotificationRepository.save(scheduleTodayNotificationDis);
                    botMessage = new BotMessage(
                            String.format(MessageText.SCHEDULE_IS_DISABLED, "сегодня"),
                            keyboardGenerator.buildSettingsScheduleNotification(botUser));
                }
                else {
                    if(scheduleTodayNotificationDis == null) {
                        botMessage = new BotMessage(
                                "Вы еще не подключали рассылку расписания на сегодня. Подключите через '" +
                                CommandText.SET_SEND_SCHEDULE_TIME_TODAY + "'.",
                                keyboardGenerator.buildSettingsScheduleNotification(botUser));
                    }
                    else {
                        botMessage = new BotMessage(
                                String.format(MessageText.SCHEDULE_IS_DISABLED_DOUBLE, "сегодня"),
                                keyboardGenerator.buildSettingsScheduleNotification(botUser));
                    }
                }
                break;
            case CommandText.SET_SEND_SCHEDULE_TIME_TOMORROW:
                formatSchedule = String.format(MessageText.CHOOSE_SCHEDULE_NOTIFICATION_TIME, "завтра");
                botMessage = new BotMessage(formatSchedule, ButtonHours);
                botUser.setPreviousUserMessage(formatSchedule);
                botUserRepository.save(botUser);
                break;
            case CommandText.ENABLE_SEND_SCHEDULE_TOMORROW:
                ScheduleTomorrowNotification scheduleTomorrowNotificationEn =
                        scheduleTomorrowNotificationRepository.findByUserId(userId);
                if(scheduleTomorrowNotificationEn != null && !scheduleTomorrowNotificationEn.isEnabled()) {
                    scheduleTomorrowNotificationEn.setEnabled(true);
                    scheduleTomorrowNotificationRepository.save(scheduleTomorrowNotificationEn);
                    botMessage = new BotMessage(
                            String.format(MessageText.SCHEDULE_WILL_BE_SENT, "завтра") +
                            scheduleTomorrowNotificationEn.getHourForSend() + " ч.",
                            keyboardGenerator.buildSettingsScheduleNotification(botUser));
                }
                else {
                    if(scheduleTomorrowNotificationEn == null) {
                        botMessage = new BotMessage(
                                "Вы еще не подключали рассылку расписания на завтра. Подключите через '" +
                                        CommandText.SET_SEND_SCHEDULE_TIME_TOMORROW + "'.",
                                keyboardGenerator.buildSettingsScheduleNotification(botUser));
                    }
                    else {
                        botMessage = new BotMessage(
                                String.format(MessageText.SCHEDULE_IS_ENABLED_DOUBLE, "завтра"),
                                keyboardGenerator.buildSettingsScheduleNotification(botUser));
                    }
                }
                break;
            case CommandText.DISABLE_SEND_SCHEDULE_TOMORROW:
                ScheduleTomorrowNotification scheduleTomorrowNotificationDis =
                        scheduleTomorrowNotificationRepository.findByUserId(userId);
                if(scheduleTomorrowNotificationDis != null && scheduleTomorrowNotificationDis.isEnabled()) {
                    scheduleTomorrowNotificationDis.setEnabled(false);
                    scheduleTomorrowNotificationRepository.save(scheduleTomorrowNotificationDis);
                    botMessage = new BotMessage(
                            String.format(MessageText.SCHEDULE_IS_DISABLED, "завтра"),
                            keyboardGenerator.buildSettingsScheduleNotification(botUser));
                }
                else {
                    if(scheduleTomorrowNotificationDis == null) {
                        botMessage = new BotMessage(
                                "Вы еще не подключали рассылку расписания на завтра. Подключите через '" +
                                CommandText.SET_SEND_SCHEDULE_TIME_TOMORROW + "'.",
                                keyboardGenerator.buildSettingsScheduleNotification(botUser));
                    }
                    else {
                        botMessage = new BotMessage(
                                String.format(MessageText.SCHEDULE_IS_DISABLED_DOUBLE, "завтра"),
                                keyboardGenerator.buildSettingsScheduleNotification(botUser));
                    }
                }
                break;
            case CommandText.SET_SEND_EXAM_PERIOD_TIME_TODAY:
                formatSchedule = String.format(MessageText.CHOOSE_EXAM_PERIOD_NOTIFICATION_TIME, "сегодня");
                botMessage = new BotMessage(formatSchedule, ButtonHours);
                botUser.setPreviousUserMessage(formatSchedule);
                botUserRepository.save(botUser);
                break;
            case CommandText.ENABLE_SEND_EXAM_PERIOD_TODAY:
                if(BotMessageUtils.isBotUserFullTime(botUser)) {
                    ExamPeriodTodayNotification examPeriodTodayNotificationEn =
                            examPeriodTodayNotificationRepository.findByUserId(userId);
                    if(examPeriodTodayNotificationEn != null && !examPeriodTodayNotificationEn.isEnabled()) {
                        examPeriodTodayNotificationEn.setEnabled(true);
                        examPeriodTodayNotificationRepository.save(examPeriodTodayNotificationEn);
                        botMessage = new BotMessage(
                                String.format(MessageText.EXAM_PERIOD_WILL_BE_SENT, "сегодня") +
                                        examPeriodTodayNotificationEn.getHourForSend() + " ч.",
                                keyboardGenerator.buildSettingsExamNotification(botUser));
                    }
                    else {
                        if(null == examPeriodTodayNotificationEn) {
                            botMessage = new BotMessage(
                                    "Вы еще не подключали рассылку расписания сессии на сегодня. Подключите через '" +
                                            CommandText.SET_SEND_EXAM_PERIOD_TIME_TODAY + "'.",
                                    keyboardGenerator.buildSettingsExamNotification(botUser));
                        }
                        else {
                            botMessage = new BotMessage(
                                    String.format(MessageText.EXAM_PERIOD_IS_ENABLED_DOUBLE, "сегодня"),
                                    keyboardGenerator.buildSettingsExamNotification(botUser));
                        }
                    }
                }
                else if(BotMessageUtils.isBotUserExtramural(botUser)) {
                    ExtramuralEventTodayNotification extramuralEventTodayNotificationEn =
                            extramuralEventTodayNotificationRepository.findByUserId(userId);
                    if(null != extramuralEventTodayNotificationEn && !extramuralEventTodayNotificationEn.isEnabled()) {
                        extramuralEventTodayNotificationEn.setEnabled(true);
                        extramuralEventTodayNotificationRepository.save(extramuralEventTodayNotificationEn);
                        botMessage = new BotMessage(
                                String.format(MessageText.EXAM_PERIOD_WILL_BE_SENT, "сегодня") +
                                        extramuralEventTodayNotificationEn.getHourForSend() + " ч.",
                                keyboardGenerator.buildSettingsExamNotification(botUser));
                    }
                    else {
                        if(null == extramuralEventTodayNotificationEn) {
                            botMessage = new BotMessage(
                                    "Вы еще не подключали рассылку расписания сессии на сегодня. Подключите через '" +
                                            CommandText.SET_SEND_EXAM_PERIOD_TIME_TODAY + "'.",
                                    keyboardGenerator.buildSettingsExamNotification(botUser));
                        }
                        else {
                            botMessage = new BotMessage(
                                    String.format(MessageText.EXAM_PERIOD_IS_ENABLED_DOUBLE, "сегодня"),
                                    keyboardGenerator.buildSettingsExamNotification(botUser));
                        }
                    }
                }
                break;
            case CommandText.DISABLE_SEND_EXAM_PERIOD_TODAY:
                if(BotMessageUtils.isBotUserFullTime(botUser)) {
                    ExamPeriodTodayNotification examPeriodTodayNotificationDis =
                            examPeriodTodayNotificationRepository.findByUserId(userId);
                    if(null != examPeriodTodayNotificationDis && examPeriodTodayNotificationDis.isEnabled()) {
                        examPeriodTodayNotificationDis.setEnabled(false);
                        examPeriodTodayNotificationRepository.save(examPeriodTodayNotificationDis);
                        botMessage = new BotMessage(
                                String.format(MessageText.EXAM_PERIOD_IS_DISABLED, "сегодня"),
                                keyboardGenerator.buildSettingsExamNotification(botUser));
                    }
                    else {
                        if(null == examPeriodTodayNotificationDis) {
                            botMessage = new BotMessage(
                                    "Вы еще не подключали рассылку расписания сессии на сегодня. Подключите через '" +
                                            CommandText.SET_SEND_EXAM_PERIOD_TIME_TODAY + "'.",
                                    keyboardGenerator.buildSettingsExamNotification(botUser));
                        }
                        else {
                            botMessage = new BotMessage(
                                    String.format(MessageText.EXAM_PERIOD_IS_DISABLED_DOUBLE, "сегодня"),
                                    keyboardGenerator.buildSettingsExamNotification(botUser));
                        }
                    }
                }
                else if(BotMessageUtils.isBotUserExtramural(botUser)) {
                    ExtramuralEventTodayNotification extramuralEventTodayNotificationDis =
                            extramuralEventTodayNotificationRepository.findByUserId(userId);
                    if(null != extramuralEventTodayNotificationDis && extramuralEventTodayNotificationDis.isEnabled()) {
                        extramuralEventTodayNotificationDis.setEnabled(false);
                        extramuralEventTodayNotificationRepository.save(extramuralEventTodayNotificationDis);
                        botMessage = new BotMessage(
                                String.format(MessageText.EXAM_PERIOD_IS_DISABLED, "сегодня"),
                                keyboardGenerator.buildSettingsExamNotification(botUser));
                    }
                    else {
                        if(null == extramuralEventTodayNotificationDis) {
                            botMessage = new BotMessage(
                                    "Вы еще не подключали рассылку расписания сессии на сегодня. Подключите через '" +
                                            CommandText.SET_SEND_EXAM_PERIOD_TIME_TODAY + "'.",
                                    keyboardGenerator.buildSettingsExamNotification(botUser));
                        }
                        else {
                            botMessage = new BotMessage(
                                    String.format(MessageText.EXAM_PERIOD_IS_DISABLED_DOUBLE, "сегодня"),
                                    keyboardGenerator.buildSettingsExamNotification(botUser));
                        }
                    }
                }
                break;
            case CommandText.SET_SEND_EXAM_PERIOD_TIME_TOMORROW:
                formatSchedule = String.format(MessageText.CHOOSE_EXAM_PERIOD_NOTIFICATION_TIME, "завтра");
                botMessage = new BotMessage(formatSchedule, ButtonHours);
                botUser.setPreviousUserMessage(formatSchedule);
                botUserRepository.save(botUser);
                break;
            case CommandText.ENABLE_SEND_EXAM_PERIOD_TOMORROW:
                if(BotMessageUtils.isBotUserFullTime(botUser)) {
                    ExamPeriodTomorrowNotification examPeriodTomorrowNotificationEn =
                            examPeriodTomorrowNotificationRepository.findByUserId(userId);
                    if(null != examPeriodTomorrowNotificationEn && !examPeriodTomorrowNotificationEn.isEnabled()) {
                        examPeriodTomorrowNotificationEn.setEnabled(true);
                        examPeriodTomorrowNotificationRepository.save(examPeriodTomorrowNotificationEn);
                        botMessage = new BotMessage(
                                String.format(MessageText.EXAM_PERIOD_WILL_BE_SENT, "завтра") +
                                        examPeriodTomorrowNotificationEn.getHourForSend() + " ч.",
                                keyboardGenerator.buildSettingsExamNotification(botUser));
                    }
                    else {
                        if(null == examPeriodTomorrowNotificationEn) {
                            botMessage = new BotMessage(
                                    "Вы еще не подключали рассылку расписания сессии на завтра. Подключите через '" +
                                            CommandText.SET_SEND_EXAM_PERIOD_TIME_TOMORROW + "'.",
                                    keyboardGenerator.buildSettingsExamNotification(botUser));
                        }
                        else {
                            botMessage = new BotMessage(
                                    String.format(MessageText.EXAM_PERIOD_IS_ENABLED_DOUBLE, "завтра"),
                                    keyboardGenerator.buildSettingsExamNotification(botUser));
                        }
                    }
                }
                else if(BotMessageUtils.isBotUserExtramural(botUser)) {
                    ExtramuralEventTomorrowNotification extramuralEventTomorrowNotificationEn =
                            extramuralEventTomorrowNotificationRepository.findByUserId(userId);
                    if(null != extramuralEventTomorrowNotificationEn && !extramuralEventTomorrowNotificationEn.isEnabled()) {
                        extramuralEventTomorrowNotificationEn.setEnabled(true);
                        extramuralEventTomorrowNotificationRepository.save(extramuralEventTomorrowNotificationEn);
                        botMessage = new BotMessage(
                                String.format(MessageText.EXAM_PERIOD_WILL_BE_SENT, "завтра") +
                                        extramuralEventTomorrowNotificationEn.getHourForSend() + " ч.",
                                keyboardGenerator.buildSettingsExamNotification(botUser));
                    }
                    else {
                        if(null == extramuralEventTomorrowNotificationEn) {
                            botMessage = new BotMessage(
                                    "Вы еще не подключали рассылку расписания сессии на завтра. Подключите через '" +
                                            CommandText.SET_SEND_EXAM_PERIOD_TIME_TOMORROW + "'.",
                                    keyboardGenerator.buildSettingsExamNotification(botUser));
                        }
                        else {
                            botMessage = new BotMessage(
                                    String.format(MessageText.EXAM_PERIOD_IS_ENABLED_DOUBLE, "завтра"),
                                    keyboardGenerator.buildSettingsExamNotification(botUser));
                        }
                    }
                }
                break;
            case CommandText.DISABLE_SEND_EXAM_PERIOD_TOMORROW:
                if(BotMessageUtils.isBotUserFullTime(botUser)) {
                    ExamPeriodTomorrowNotification examPeriodTomorrowNotificationDis =
                            examPeriodTomorrowNotificationRepository.findByUserId(userId);
                    if(null != examPeriodTomorrowNotificationDis && examPeriodTomorrowNotificationDis.isEnabled()) {
                        examPeriodTomorrowNotificationDis.setEnabled(false);
                        examPeriodTomorrowNotificationRepository.save(examPeriodTomorrowNotificationDis);
                        botMessage = new BotMessage(
                                String.format(MessageText.EXAM_PERIOD_IS_DISABLED, "завтра"),
                                keyboardGenerator.buildSettingsExamNotification(botUser));
                    }
                    else {
                        if(null == examPeriodTomorrowNotificationDis) {
                            botMessage = new BotMessage(
                                    "Вы еще не подключали рассылку расписания сессии на завтра. Подключите через '" +
                                            CommandText.SET_SEND_EXAM_PERIOD_TIME_TOMORROW + "'.",
                                    keyboardGenerator.buildSettingsExamNotification(botUser));
                        }
                        else {
                            botMessage = new BotMessage(
                                    String.format(MessageText.EXAM_PERIOD_IS_DISABLED_DOUBLE, "завтра"),
                                    keyboardGenerator.buildSettingsExamNotification(botUser));
                        }
                    }
                }
                else if(BotMessageUtils.isBotUserExtramural(botUser)) {
                    ExtramuralEventTomorrowNotification extramuralEventTomorrowNotificationDis =
                            extramuralEventTomorrowNotificationRepository.findByUserId(userId);
                    if(null != extramuralEventTomorrowNotificationDis && extramuralEventTomorrowNotificationDis.isEnabled()) {
                        extramuralEventTomorrowNotificationDis.setEnabled(false);
                        extramuralEventTomorrowNotificationRepository.save(extramuralEventTomorrowNotificationDis);
                        botMessage = new BotMessage(
                                String.format(MessageText.EXAM_PERIOD_IS_DISABLED, "завтра"),
                                keyboardGenerator.buildSettingsExamNotification(botUser));
                    }
                    else {
                        if(null == extramuralEventTomorrowNotificationDis) {
                            botMessage = new BotMessage(
                                    "Вы еще не подключали рассылку расписания сессии на завтра. Подключите через '" +
                                            CommandText.SET_SEND_EXAM_PERIOD_TIME_TOMORROW + "'.",
                                    keyboardGenerator.buildSettingsExamNotification(botUser));
                        }
                        else {
                            botMessage = new BotMessage(
                                    String.format(MessageText.EXAM_PERIOD_IS_DISABLED_DOUBLE, "завтра"),
                                    keyboardGenerator.buildSettingsExamNotification(botUser));
                        }
                    }
                }
                break;
            case CommandText.SET_SEND_EXAM_PERIOD_TIME_AFTER_TOMORROW:
                formatSchedule = String.format(MessageText.CHOOSE_EXAM_PERIOD_NOTIFICATION_TIME, "послезавтра");
                botMessage = new BotMessage(formatSchedule, ButtonHours);
                botUser.setPreviousUserMessage(formatSchedule);
                botUserRepository.save(botUser);
                break;
            case CommandText.ENABLE_SEND_EXAM_PERIOD_AFTER_TOMORROW:
                if(BotMessageUtils.isBotUserFullTime(botUser)) {
                    ExamPeriodAfterTomorrowNotification examPeriodAfterTomorrowNotificationEn =
                            examPeriodAfterTomorrowNotificationRepository.findByUserId(userId);
                    if(null != examPeriodAfterTomorrowNotificationEn && !examPeriodAfterTomorrowNotificationEn.isEnabled()) {
                        examPeriodAfterTomorrowNotificationEn.setEnabled(true);
                        examPeriodAfterTomorrowNotificationRepository.save(examPeriodAfterTomorrowNotificationEn);
                        botMessage = new BotMessage(
                                String.format(MessageText.EXAM_PERIOD_WILL_BE_SENT, "послезавтра") +
                                        examPeriodAfterTomorrowNotificationEn.getHourForSend() + " ч.",
                                keyboardGenerator.buildSettingsExamNotification(botUser));
                    }
                    else {
                        if(null == examPeriodAfterTomorrowNotificationEn) {
                            botMessage = new BotMessage(
                                    "Вы еще не подключали рассылку расписания сессии на послезавтра. Подключите через '" +
                                            CommandText.SET_SEND_EXAM_PERIOD_TIME_TOMORROW + "'.",
                                    keyboardGenerator.buildSettingsExamNotification(botUser));
                        }
                        else {
                            botMessage = new BotMessage(
                                    String.format(MessageText.EXAM_PERIOD_IS_ENABLED_DOUBLE, "послезавтра"),
                                    keyboardGenerator.buildSettingsExamNotification(botUser));
                        }
                    }
                }
                else if(BotMessageUtils.isBotUserExtramural(botUser)) {
                    ExtramuralEventAfterTomorrowNotification extramuralEventAfterTomorrowNotificationEn =
                            extramuralEventAfterTomorrowNotificationRepository.findByUserId(userId);
                    if(null != extramuralEventAfterTomorrowNotificationEn && !extramuralEventAfterTomorrowNotificationEn.isEnabled()) {
                        extramuralEventAfterTomorrowNotificationEn.setEnabled(true);
                        extramuralEventAfterTomorrowNotificationRepository.save(extramuralEventAfterTomorrowNotificationEn);
                        botMessage = new BotMessage(
                                String.format(MessageText.EXAM_PERIOD_WILL_BE_SENT, "послезавтра") +
                                        extramuralEventAfterTomorrowNotificationEn.getHourForSend() + " ч.",
                                keyboardGenerator.buildSettingsExamNotification(botUser));
                    }
                    else {
                        if(null == extramuralEventAfterTomorrowNotificationEn) {
                            botMessage = new BotMessage(
                                    "Вы еще не подключали рассылку расписания сессии на послезавтра. Подключите через '" +
                                            CommandText.SET_SEND_EXAM_PERIOD_TIME_TOMORROW + "'.",
                                    keyboardGenerator.buildSettingsExamNotification(botUser));
                        }
                        else {
                            botMessage = new BotMessage(
                                    String.format(MessageText.EXAM_PERIOD_IS_ENABLED_DOUBLE, "послезавтра"),
                                    keyboardGenerator.buildSettingsExamNotification(botUser));
                        }
                    }
                }
                break;
            case CommandText.DISABLE_SEND_EXAM_PERIOD_AFTER_TOMORROW:
                if(BotMessageUtils.isBotUserFullTime(botUser)) {
                    ExamPeriodAfterTomorrowNotification examPeriodAfterTomorrowNotificationDis =
                            examPeriodAfterTomorrowNotificationRepository.findByUserId(userId);
                    if(null != examPeriodAfterTomorrowNotificationDis && examPeriodAfterTomorrowNotificationDis.isEnabled()) {
                        examPeriodAfterTomorrowNotificationDis.setEnabled(false);
                        examPeriodAfterTomorrowNotificationRepository.save(examPeriodAfterTomorrowNotificationDis);
                        botMessage = new BotMessage(
                                String.format(MessageText.EXAM_PERIOD_IS_DISABLED, "послезавтра"),
                                keyboardGenerator.buildSettingsExamNotification(botUser));
                    }
                    else {
                        if(null == examPeriodAfterTomorrowNotificationDis) {
                            botMessage = new BotMessage(
                                    "Вы еще не подключали рассылку расписания сессии на послезавтра. Подключите через '" +
                                            CommandText.SET_SEND_EXAM_PERIOD_TIME_TOMORROW + "'.",
                                    keyboardGenerator.buildSettingsExamNotification(botUser));
                        }
                        else {
                            botMessage = new BotMessage(
                                    String.format(MessageText.EXAM_PERIOD_IS_DISABLED_DOUBLE, "послезавтра"),
                                    keyboardGenerator.buildSettingsExamNotification(botUser));
                        }
                    }
                }
                else if(BotMessageUtils.isBotUserExtramural(botUser)) {
                    ExtramuralEventAfterTomorrowNotification extramuralEventAfterTomorrowNotificationDis =
                            extramuralEventAfterTomorrowNotificationRepository.findByUserId(userId);
                    if(null != extramuralEventAfterTomorrowNotificationDis && extramuralEventAfterTomorrowNotificationDis.isEnabled()) {
                        extramuralEventAfterTomorrowNotificationDis.setEnabled(false);
                        extramuralEventAfterTomorrowNotificationRepository.save(extramuralEventAfterTomorrowNotificationDis);
                        botMessage = new BotMessage(
                                String.format(MessageText.EXAM_PERIOD_IS_DISABLED, "послезавтра"),
                                keyboardGenerator.buildSettingsExamNotification(botUser));
                    }
                    else {
                        if(null == extramuralEventAfterTomorrowNotificationDis) {
                            botMessage = new BotMessage(
                                    "Вы еще не подключали рассылку расписания сессии на послезавтра. Подключите через '" +
                                            CommandText.SET_SEND_EXAM_PERIOD_TIME_TOMORROW + "'.",
                                    keyboardGenerator.buildSettingsExamNotification(botUser));
                        }
                        else {
                            botMessage = new BotMessage(
                                    String.format(MessageText.EXAM_PERIOD_IS_DISABLED_DOUBLE, "послезавтра"),
                                    keyboardGenerator.buildSettingsExamNotification(botUser));
                        }
                    }
                }
                break;
            case CommandText.ENABLE_FILTER_WEEK_TYPE:
                botUser.setFilterNomDenom(true);
                botUserRepository.save(botUser);
                botMessage = new BotMessage(
                        "Включена фильтрация по типу недели.",
                        keyboardGenerator.buildSettings(botUser));
                break;
            case CommandText.DISABLE_FILTER_WEEK_TYPE:
                botUser.setFilterNomDenom(false);
                botUserRepository.save(botUser);
                botMessage = new BotMessage(
                            "Выключена фильтрация по типу недели.",
                        keyboardGenerator.buildSettings(botUser));
                break;
            case CommandText.CURRENT_USER_SETTINGS:
                String currentUserSettings = getStudentInfo(botUser) +
                        "\n" +
                        getScheduleNotificationStatus(botUser.getUserId()) +
                        "\n\n" +
                        "Фильтрация по типу недели: " +
                        (botUser.isFilterNomDenom() ? "вкл." : "выкл.");
                botMessage = new BotMessage(currentUserSettings, keyboardGenerator.buildSettings(botUser));
                break;
            case CommandText.DELETE_PROFILE:
                botMessage = new BotMessage(MessageText.DELETE_CONFIRMATION, ButtonConfirmDeletion);
                break;
            case CommandText.NO:
                botMessage = new BotMessage(
                        "Спасибо, что остаётесь с нами!",
                        keyboardGenerator.buildSettings(botUser));
                break;
            case CommandText.YES:
                botUserRepository.deleteOneById(userId);
                botMessage = new BotMessage(MessageText.BYE_MESSAGE);
                break;
        }

        if(CommandText.HOUR_PATTERN.matcher(message).matches()) {
            int hourForSend = Integer.parseInt(message.substring(0, message.indexOf(" ")));

            if(botUser.getPreviousUserMessage().equalsIgnoreCase(
                                String.format(MessageText.CHOOSE_SCHEDULE_NOTIFICATION_TIME, "сегодня"))) {
                ScheduleTodayNotification scheduleTodayNotification =
                        scheduleTodayNotificationRepository.findByUserId(userId);

                if(scheduleTodayNotification == null) {
                    scheduleTodayNotification = new ScheduleTodayNotification(userId, true, hourForSend);
                }
                else {
                    scheduleTodayNotification.setHourForSend(hourForSend);
                }
                scheduleTodayNotificationRepository.save(scheduleTodayNotification);
            }
            if(botUser.getPreviousUserMessage().equalsIgnoreCase(
                                String.format(MessageText.CHOOSE_SCHEDULE_NOTIFICATION_TIME, "завтра"))) {
                ScheduleTomorrowNotification scheduleTomorrowNotification =
                        scheduleTomorrowNotificationRepository.findByUserId(userId);

                if(scheduleTomorrowNotification == null) {
                    scheduleTomorrowNotification = new ScheduleTomorrowNotification(userId, true, hourForSend);
                }
                else {
                    scheduleTomorrowNotification.setHourForSend(hourForSend);
                }
                scheduleTomorrowNotificationRepository.save(scheduleTomorrowNotification);
            }
            if(botUser.getPreviousUserMessage().equalsIgnoreCase(
                    String.format(MessageText.CHOOSE_EXAM_PERIOD_NOTIFICATION_TIME, "сегодня"))) {
                ExamPeriodTodayNotification examPeriodTodayNotification =
                        examPeriodTodayNotificationRepository.findByUserId(userId);

                if(examPeriodTodayNotification == null) {
                    examPeriodTodayNotification = new ExamPeriodTodayNotification(userId, true, hourForSend);
                }
                else {
                    examPeriodTodayNotification.setHourForSend(hourForSend);
                }
                examPeriodTodayNotificationRepository.save(examPeriodTodayNotification);
            }
            if(botUser.getPreviousUserMessage().equalsIgnoreCase(
                    String.format(MessageText.CHOOSE_EXAM_PERIOD_NOTIFICATION_TIME, "завтра"))) {
                ExamPeriodTomorrowNotification examPeriodTomorrowNotification =
                        examPeriodTomorrowNotificationRepository.findByUserId(userId);

                if(examPeriodTomorrowNotification == null) {
                    examPeriodTomorrowNotification = new ExamPeriodTomorrowNotification(userId, true, hourForSend);
                }
                else {
                    examPeriodTomorrowNotification.setHourForSend(hourForSend);
                }
                examPeriodTomorrowNotificationRepository.save(examPeriodTomorrowNotification);
            }
            if(botUser.getPreviousUserMessage().equalsIgnoreCase(
                    String.format(MessageText.CHOOSE_EXAM_PERIOD_NOTIFICATION_TIME, "послезавтра"))) {
                ExamPeriodAfterTomorrowNotification examPeriodAfterTomorrowNotification =
                        examPeriodAfterTomorrowNotificationRepository.findByUserId(userId);

                if(examPeriodAfterTomorrowNotification == null) {
                    examPeriodAfterTomorrowNotification = new ExamPeriodAfterTomorrowNotification(userId, true, hourForSend);
                }
                else {
                    examPeriodAfterTomorrowNotification.setHourForSend(hourForSend);
                }
                examPeriodAfterTomorrowNotificationRepository.save(examPeriodAfterTomorrowNotification);
            }
            botMessage = new BotMessage(
                    "Теперь расписание будет приходить в " + hourForSend + " ч.",
                    keyboardGenerator.buildSettings(botUser));
        }

        return botMessage;
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

    private String getScheduleNotificationStatus(String userId) {
        ScheduleTodayNotification scheduleTodayNotification =
                scheduleTodayNotificationRepository.findByUserId(userId);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Рассылка расписания на сегодня: ");

        if(scheduleTodayNotification == null) {
            stringBuilder.append("не подключена.");
        }
        else {
            if(scheduleTodayNotification.isEnabled()) {
                stringBuilder.append("вкл, ");
            }
            else {
                stringBuilder.append("выкл, ");
            }

            if(scheduleTodayNotification.getHourForSend() == null) {
                stringBuilder.append("время не указано.");
            }
            else {
                stringBuilder.append(scheduleTodayNotification.getHourForSend()).append(" ч.");
            }
        }

        stringBuilder.append("\n\n");

        ScheduleTomorrowNotification scheduleTomorrowNotification =
                scheduleTomorrowNotificationRepository.findByUserId(userId);

        stringBuilder.append("Рассылка расписания на завтра: ");

        if(scheduleTomorrowNotification == null) {
            stringBuilder.append("не подключена.");
        }
        else {
            if(scheduleTomorrowNotification.isEnabled()) {
                stringBuilder.append("вкл, ");
            }
            else {
                stringBuilder.append("выкл, ");
            }

            if(scheduleTomorrowNotification.getHourForSend() == null) {
                stringBuilder.append("время не указано.");
            }
            else {
                stringBuilder.append(scheduleTomorrowNotification.getHourForSend()).append(" ч.");
            }
        }

        stringBuilder.append("\n\n");

        ExamPeriodTodayNotification examPeriodTodayNotification =
                examPeriodTodayNotificationRepository.findByUserId(userId);
        stringBuilder.append("Рассылка расписания сессии на сегодня: ");

        if(examPeriodTodayNotification == null) {
            stringBuilder.append("не подключена.");
        }
        else {
            if(examPeriodTodayNotification.isEnabled()) {
                stringBuilder.append("вкл, ");
            }
            else {
                stringBuilder.append("выкл, ");
            }

            if(examPeriodTodayNotification.getHourForSend() == null) {
                stringBuilder.append("время не указано.");
            }
            else {
                stringBuilder.append(examPeriodTodayNotification.getHourForSend()).append(" ч.");
            }
        }

        stringBuilder.append("\n\n");

        ExamPeriodTomorrowNotification examPeriodTomorrowNotification =
                examPeriodTomorrowNotificationRepository.findByUserId(userId);
        stringBuilder.append("Рассылка расписания сессии на завтра: ");

        if(examPeriodTomorrowNotification == null) {
            stringBuilder.append("не подключена.");
        }
        else {
            if(examPeriodTomorrowNotification.isEnabled()) {
                stringBuilder.append("вкл, ");
            }
            else {
                stringBuilder.append("выкл, ");
            }

            if(examPeriodTomorrowNotification.getHourForSend() == null) {
                stringBuilder.append("время не указано.");
            }
            else {
                stringBuilder.append(examPeriodTomorrowNotification.getHourForSend()).append(" ч.");
            }
        }

        stringBuilder.append("\n\n");

        ExamPeriodAfterTomorrowNotification examPeriodAfterTomorrowNotification =
                examPeriodAfterTomorrowNotificationRepository.findByUserId(userId);
        stringBuilder.append("Рассылка расписания сессии на послезавтра: ");

        if(examPeriodAfterTomorrowNotification == null) {
            stringBuilder.append("не подключена.");
        }
        else {
            if(examPeriodAfterTomorrowNotification.isEnabled()) {
                stringBuilder.append("вкл, ");
            }
            else {
                stringBuilder.append("выкл, ");
            }

            if(examPeriodAfterTomorrowNotification.getHourForSend() == null) {
                stringBuilder.append("время не указано.");
            }
            else {
                stringBuilder.append(examPeriodAfterTomorrowNotification.getHourForSend()).append(" ч.");
            }
        }

        return stringBuilder.toString();
    }

    private String firstNotEmpty(String string) {
        return StringUtils.isEmpty(string) ? "не указано" : string;
    }
}
