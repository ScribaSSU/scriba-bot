package com.scribassu.scribabot.services.bot;

import com.scribassu.scribabot.dto.BotMessage;
import com.scribassu.scribabot.entities.*;
import com.scribassu.scribabot.keyboard.KeyboardFormatter;
import com.scribassu.scribabot.repositories.*;
import com.scribassu.scribabot.text.CommandText;
import com.scribassu.scribabot.text.MessageText;
import com.scribassu.scribabot.util.DepartmentConverter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.scribassu.scribabot.keyboard.KeyboardType.*;

@Service
public class SettingsService implements BotMessageService {

    private final BotUserRepository botUserRepository;
    private final ScheduleDailyNotificationRepository scheduleDailyNotificationRepository;
    private final ScheduleTomorrowNotificationRepository scheduleTomorrowNotificationRepository;
    private final ExamPeriodDailyNotificationRepository examPeriodDailyNotificationRepository;
    private final ExamPeriodTomorrowNotificationRepository examPeriodTomorrowNotificationRepository;
    private final ExamPeriodAfterTomorrowNotificationRepository examPeriodAfterTomorrowNotificationRepository;
    private final KeyboardFormatter keyboardFormatter;

    @Autowired
    public SettingsService(BotUserRepository botUserRepository,
                           ScheduleDailyNotificationRepository scheduleDailyNotificationRepository,
                           ScheduleTomorrowNotificationRepository scheduleTomorrowNotificationRepository,
                           ExamPeriodDailyNotificationRepository examPeriodDailyNotificationRepository,
                           ExamPeriodTomorrowNotificationRepository examPeriodTomorrowNotificationRepository,
                           ExamPeriodAfterTomorrowNotificationRepository examPeriodAfterTomorrowNotificationRepository,
                           KeyboardFormatter keyboardFormatter) {
        this.botUserRepository = botUserRepository;
        this.scheduleDailyNotificationRepository = scheduleDailyNotificationRepository;
        this.scheduleTomorrowNotificationRepository = scheduleTomorrowNotificationRepository;
        this.examPeriodDailyNotificationRepository = examPeriodDailyNotificationRepository;
        this.examPeriodTomorrowNotificationRepository = examPeriodTomorrowNotificationRepository;
        this.examPeriodAfterTomorrowNotificationRepository = examPeriodAfterTomorrowNotificationRepository;
        this.keyboardFormatter = keyboardFormatter;
    }

    @Override
    public BotMessage getBotMessage(String message, BotUser botUser) {
        BotMessage botMessage = new BotMessage();
        String userId = botUser.getUserId();

        switch(message) {
            case CommandText.SEND_EXAM_PERIOD:
                botMessage = new BotMessage("Здесь вы можете настроить рассылку расписания сессии.", ButtonSettingsExamPeriodNotifications);
                botMessage = keyboardFormatter.formatSettingsExamNotif(botMessage, botUser);
                break;
            case CommandText.SEND_SCHEDULE:
                botMessage = new BotMessage("Здесь вы можете настроить рассылку расписания занятий.", ButtonSettingsScheduleNotifications);
                botMessage = keyboardFormatter.formatSettingsScheduleNotif(botMessage, botUser);
                break;
            case CommandText.SET_SEND_SCHEDULE_TIME_TODAY:
                String formatSchedule = String.format(MessageText.CHOOSE_SCHEDULE_NOTIFICATION_TIME, "сегодня");
                botMessage = new BotMessage(formatSchedule, ButtonHours);
                botUser.setPreviousUserMessage(formatSchedule);
                botUserRepository.save(botUser);
                break;
            case CommandText.ENABLE_SEND_SCHEDULE_TODAY:
                ScheduleDailyNotification scheduleDailyNotificationEn =
                        scheduleDailyNotificationRepository.findByUserId(userId);
                if(scheduleDailyNotificationEn != null && !scheduleDailyNotificationEn.isEnabled()) {
                    scheduleDailyNotificationEn.setEnabled(true);
                    scheduleDailyNotificationRepository.save(scheduleDailyNotificationEn);
                    botMessage = new BotMessage(String.format(MessageText.SCHEDULE_WILL_BE_SENT, "сегодня") +
                            scheduleDailyNotificationEn.getHourForSend() + " ч.");
                    botMessage.setKeyboard(ButtonSettingsScheduleNotifications);
                    botMessage = keyboardFormatter.formatSettingsScheduleNotif(botMessage, botUser);
                }
                else {
                    if(scheduleDailyNotificationEn == null) {
                        botMessage = new BotMessage(
                                "Вы еще не подключали рассылку расписания на сегодня. Подключите через '" +
                                CommandText.SET_SEND_SCHEDULE_TIME_TODAY + "'.");
                        botMessage.setKeyboard(ButtonSettingsScheduleNotifications);
                        botMessage = keyboardFormatter.formatSettingsScheduleNotif(botMessage, botUser);
                    }
                    else {
                        botMessage = new BotMessage(String.format(MessageText.SCHEDULE_IS_ENABLED_DOUBLE, "сегодня"));
                        botMessage.setKeyboard(ButtonSettingsScheduleNotifications);
                        botMessage = keyboardFormatter.formatSettingsScheduleNotif(botMessage, botUser);
                    }
                }
                break;
            case CommandText.DISABLE_SEND_SCHEDULE_TODAY:
                ScheduleDailyNotification scheduleDailyNotificationDis =
                        scheduleDailyNotificationRepository.findByUserId(userId);
                if(scheduleDailyNotificationDis != null && scheduleDailyNotificationDis.isEnabled()) {
                    scheduleDailyNotificationDis.setEnabled(false);
                    scheduleDailyNotificationRepository.save(scheduleDailyNotificationDis);
                    botMessage = new BotMessage(String.format(MessageText.SCHEDULE_IS_DISABLED, "сегодня"));
                    botMessage.setKeyboard(ButtonSettingsScheduleNotifications);
                    botMessage = keyboardFormatter.formatSettingsScheduleNotif(botMessage, botUser);
                }
                else {
                    if(scheduleDailyNotificationDis == null) {
                        botMessage = new BotMessage(
                                "Вы еще не подключали рассылку расписания на сегодня. Подключите через '" +
                                CommandText.SET_SEND_SCHEDULE_TIME_TODAY + "'.");
                        botMessage.setKeyboard(ButtonSettingsScheduleNotifications);
                        botMessage = keyboardFormatter.formatSettingsScheduleNotif(botMessage, botUser);
                    }
                    else {
                        botMessage = new BotMessage(String.format(MessageText.SCHEDULE_IS_DISABLED_DOUBLE, "сегодня"));
                        botMessage.setKeyboard(ButtonSettingsScheduleNotifications);
                        botMessage = keyboardFormatter.formatSettingsScheduleNotif(botMessage, botUser);
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
                    botMessage = new BotMessage(String.format(MessageText.SCHEDULE_WILL_BE_SENT, "завтра") +
                            scheduleTomorrowNotificationEn.getHourForSend() + " ч.");
                    botMessage.setKeyboard(ButtonSettingsScheduleNotifications);
                    botMessage = keyboardFormatter.formatSettingsScheduleNotif(botMessage, botUser);
                }
                else {
                    if(scheduleTomorrowNotificationEn == null) {
                        botMessage = new BotMessage(
                                "Вы еще не подключали рассылку расписания на завтра. Подключите через '" +
                                        CommandText.SET_SEND_SCHEDULE_TIME_TOMORROW + "'."
                        );
                        botMessage.setKeyboard(ButtonSettingsScheduleNotifications);
                        botMessage = keyboardFormatter.formatSettingsScheduleNotif(botMessage, botUser);
                    }
                    else {
                        botMessage = new BotMessage(String.format(MessageText.SCHEDULE_IS_ENABLED_DOUBLE, "завтра"));
                        botMessage.setKeyboard(ButtonSettingsScheduleNotifications);
                        botMessage = keyboardFormatter.formatSettingsScheduleNotif(botMessage, botUser);
                    }
                }
                break;
            case CommandText.DISABLE_SEND_SCHEDULE_TOMORROW:
                ScheduleTomorrowNotification scheduleTomorrowNotificationDis =
                        scheduleTomorrowNotificationRepository.findByUserId(userId);
                if(scheduleTomorrowNotificationDis != null && scheduleTomorrowNotificationDis.isEnabled()) {
                    scheduleTomorrowNotificationDis.setEnabled(false);
                    scheduleTomorrowNotificationRepository.save(scheduleTomorrowNotificationDis);
                    botMessage = new BotMessage(String.format(MessageText.SCHEDULE_IS_DISABLED, "завтра"));
                    botMessage.setKeyboard(ButtonSettingsScheduleNotifications);
                    botMessage = keyboardFormatter.formatSettingsScheduleNotif(botMessage, botUser);
                }
                else {
                    if(scheduleTomorrowNotificationDis == null) {
                        botMessage = new BotMessage(
                                "Вы еще не подключали рассылку расписания на завтра. Подключите через '" +
                                CommandText.SET_SEND_SCHEDULE_TIME_TOMORROW + "'.");
                        botMessage.setKeyboard(ButtonSettingsScheduleNotifications);
                        botMessage = keyboardFormatter.formatSettingsScheduleNotif(botMessage, botUser);
                    }
                    else {
                        botMessage = new BotMessage(String.format(MessageText.SCHEDULE_IS_DISABLED_DOUBLE, "завтра"));
                        botMessage.setKeyboard(ButtonSettingsScheduleNotifications);
                        botMessage = keyboardFormatter.formatSettingsScheduleNotif(botMessage, botUser);
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
                ExamPeriodDailyNotification examPeriodDailyNotificationEn =
                        examPeriodDailyNotificationRepository.findByUserId(userId);
                if(examPeriodDailyNotificationEn != null && !examPeriodDailyNotificationEn.isEnabled()) {
                    examPeriodDailyNotificationEn.setEnabled(true);
                    examPeriodDailyNotificationRepository.save(examPeriodDailyNotificationEn);
                    botMessage = new BotMessage(String.format(MessageText.EXAM_PERIOD_WILL_BE_SENT, "сегодня") +
                            examPeriodDailyNotificationEn.getHourForSend() + " ч.");
                    botMessage.setKeyboard(ButtonSettingsExamPeriodNotifications);
                    botMessage = keyboardFormatter.formatSettingsExamNotif(botMessage, botUser);
                }
                else {
                    if(examPeriodDailyNotificationEn == null) {
                        botMessage = new BotMessage(
                                "Вы еще не подключали рассылку расписания сессии на сегодня. Подключите через '" +
                                        CommandText.SET_SEND_EXAM_PERIOD_TIME_TODAY + "'.");
                        botMessage.setKeyboard(ButtonSettingsExamPeriodNotifications);
                        botMessage = keyboardFormatter.formatSettingsExamNotif(botMessage, botUser);
                    }
                    else {
                        botMessage = new BotMessage(String.format(MessageText.EXAM_PERIOD_IS_ENABLED_DOUBLE, "сегодня"));
                        botMessage.setKeyboard(ButtonSettingsExamPeriodNotifications);
                        botMessage = keyboardFormatter.formatSettingsExamNotif(botMessage, botUser);
                    }
                }
                break;
            case CommandText.DISABLE_SEND_EXAM_PERIOD_TODAY:
                ExamPeriodDailyNotification examPeriodDailyNotificationDis =
                        examPeriodDailyNotificationRepository.findByUserId(userId);
                if(examPeriodDailyNotificationDis != null && examPeriodDailyNotificationDis.isEnabled()) {
                    examPeriodDailyNotificationDis.setEnabled(false);
                    examPeriodDailyNotificationRepository.save(examPeriodDailyNotificationDis);
                    botMessage = new BotMessage(String.format(MessageText.EXAM_PERIOD_IS_DISABLED, "сегодня"));
                    botMessage.setKeyboard(ButtonSettingsExamPeriodNotifications);
                    botMessage = keyboardFormatter.formatSettingsExamNotif(botMessage, botUser);
                }
                else {
                    if(examPeriodDailyNotificationDis == null) {
                        botMessage = new BotMessage(
                                "Вы еще не подключали рассылку расписания сессии на сегодня. Подключите через '" +
                                        CommandText.SET_SEND_EXAM_PERIOD_TIME_TODAY + "'.");
                        botMessage.setKeyboard(ButtonSettingsExamPeriodNotifications);
                        botMessage = keyboardFormatter.formatSettingsExamNotif(botMessage, botUser);
                    }
                    else {
                        botMessage = new BotMessage(String.format(MessageText.EXAM_PERIOD_IS_DISABLED_DOUBLE, "сегодня"));
                        botMessage.setKeyboard(ButtonSettingsExamPeriodNotifications);
                        botMessage = keyboardFormatter.formatSettingsExamNotif(botMessage, botUser);
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
                ExamPeriodTomorrowNotification examPeriodTomorrowNotificationEn =
                        examPeriodTomorrowNotificationRepository.findByUserId(userId);
                if(examPeriodTomorrowNotificationEn != null && !examPeriodTomorrowNotificationEn.isEnabled()) {
                    examPeriodTomorrowNotificationEn.setEnabled(true);
                    examPeriodTomorrowNotificationRepository.save(examPeriodTomorrowNotificationEn);
                    botMessage = new BotMessage(String.format(MessageText.EXAM_PERIOD_WILL_BE_SENT, "завтра") +
                            examPeriodTomorrowNotificationEn.getHourForSend() + " ч.");
                    botMessage.setKeyboard(ButtonSettingsExamPeriodNotifications);
                    botMessage = keyboardFormatter.formatSettingsExamNotif(botMessage, botUser);
                }
                else {
                    if(examPeriodTomorrowNotificationEn == null) {
                        botMessage = new BotMessage(
                                "Вы еще не подключали рассылку расписания сессии на завтра. Подключите через '" +
                                        CommandText.SET_SEND_EXAM_PERIOD_TIME_TOMORROW + "'.");
                        botMessage.setKeyboard(ButtonSettingsExamPeriodNotifications);
                        botMessage = keyboardFormatter.formatSettingsExamNotif(botMessage, botUser);
                    }
                    else {
                        botMessage = new BotMessage(String.format(MessageText.EXAM_PERIOD_IS_ENABLED_DOUBLE, "завтра"));
                        botMessage.setKeyboard(ButtonSettingsExamPeriodNotifications);
                        botMessage = keyboardFormatter.formatSettingsExamNotif(botMessage, botUser);
                    }
                }
                break;
            case CommandText.DISABLE_SEND_EXAM_PERIOD_TOMORROW:
                ExamPeriodTomorrowNotification examPeriodTomorrowNotificationDis =
                        examPeriodTomorrowNotificationRepository.findByUserId(userId);
                if(examPeriodTomorrowNotificationDis != null && examPeriodTomorrowNotificationDis.isEnabled()) {
                    examPeriodTomorrowNotificationDis.setEnabled(false);
                    examPeriodTomorrowNotificationRepository.save(examPeriodTomorrowNotificationDis);
                    botMessage = new BotMessage(String.format(MessageText.EXAM_PERIOD_IS_DISABLED, "завтра"));
                    botMessage.setKeyboard(ButtonSettingsExamPeriodNotifications);
                    botMessage = keyboardFormatter.formatSettingsExamNotif(botMessage, botUser);
                }
                else {
                    if(examPeriodTomorrowNotificationDis == null) {
                        botMessage = new BotMessage(
                                "Вы еще не подключали рассылку расписания сессии на завтра. Подключите через '" +
                                        CommandText.SET_SEND_EXAM_PERIOD_TIME_TOMORROW + "'.");
                        botMessage.setKeyboard(ButtonSettingsExamPeriodNotifications);
                        botMessage = keyboardFormatter.formatSettingsExamNotif(botMessage, botUser);
                    }
                    else {
                        botMessage = new BotMessage(String.format(MessageText.EXAM_PERIOD_IS_DISABLED_DOUBLE, "завтра"));
                        botMessage.setKeyboard(ButtonSettingsExamPeriodNotifications);
                        botMessage = keyboardFormatter.formatSettingsExamNotif(botMessage, botUser);
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
                ExamPeriodAfterTomorrowNotification examPeriodAfterTomorrowNotificationEn =
                        examPeriodAfterTomorrowNotificationRepository.findByUserId(userId);
                if(examPeriodAfterTomorrowNotificationEn != null && !examPeriodAfterTomorrowNotificationEn.isEnabled()) {
                    examPeriodAfterTomorrowNotificationEn.setEnabled(true);
                    examPeriodAfterTomorrowNotificationRepository.save(examPeriodAfterTomorrowNotificationEn);
                    botMessage = new BotMessage(String.format(MessageText.EXAM_PERIOD_WILL_BE_SENT, "послезавтра") +
                            examPeriodAfterTomorrowNotificationEn.getHourForSend() + " ч.");
                    botMessage.setKeyboard(ButtonSettingsExamPeriodNotifications);
                    botMessage = keyboardFormatter.formatSettingsExamNotif(botMessage, botUser);
                }
                else {
                    if(examPeriodAfterTomorrowNotificationEn == null) {
                        botMessage = new BotMessage(
                                "Вы еще не подключали рассылку расписания сессии на послезавтра. Подключите через '" +
                                        CommandText.SET_SEND_EXAM_PERIOD_TIME_TOMORROW + "'.");
                        botMessage.setKeyboard(ButtonSettingsExamPeriodNotifications);
                        botMessage = keyboardFormatter.formatSettingsExamNotif(botMessage, botUser);
                    }
                    else {
                        botMessage = new BotMessage(String.format(MessageText.EXAM_PERIOD_IS_ENABLED_DOUBLE, "послезавтра"));
                        botMessage.setKeyboard(ButtonSettingsExamPeriodNotifications);
                        botMessage = keyboardFormatter.formatSettingsExamNotif(botMessage, botUser);
                    }
                }
                break;
            case CommandText.DISABLE_SEND_EXAM_PERIOD_AFTER_TOMORROW:
                ExamPeriodAfterTomorrowNotification examPeriodAfterTomorrowNotificationDis =
                        examPeriodAfterTomorrowNotificationRepository.findByUserId(userId);
                if(examPeriodAfterTomorrowNotificationDis != null && examPeriodAfterTomorrowNotificationDis.isEnabled()) {
                    examPeriodAfterTomorrowNotificationDis.setEnabled(false);
                    examPeriodAfterTomorrowNotificationRepository.save(examPeriodAfterTomorrowNotificationDis);
                    botMessage = new BotMessage(String.format(MessageText.EXAM_PERIOD_IS_DISABLED, "послезавтра"));
                    botMessage.setKeyboard(ButtonSettingsExamPeriodNotifications);
                    botMessage = keyboardFormatter.formatSettingsExamNotif(botMessage, botUser);
                }
                else {
                    if(examPeriodAfterTomorrowNotificationDis == null) {
                        botMessage = new BotMessage(
                                "Вы еще не подключали рассылку расписания сессии на послезавтра. Подключите через '" +
                                        CommandText.SET_SEND_EXAM_PERIOD_TIME_TOMORROW + "'.");
                        botMessage.setKeyboard(ButtonSettingsExamPeriodNotifications);
                        botMessage = keyboardFormatter.formatSettingsExamNotif(botMessage, botUser);
                    }
                    else {
                        botMessage = new BotMessage(String.format(MessageText.EXAM_PERIOD_IS_DISABLED_DOUBLE, "послезавтра"));
                        botMessage.setKeyboard(ButtonSettingsExamPeriodNotifications);
                        botMessage = keyboardFormatter.formatSettingsExamNotif(botMessage, botUser);
                    }
                }
                break;
            case CommandText.ENABLE_FILTER_WEEK_TYPE:
                botUser.setFilterNomDenom(true);
                botUserRepository.save(botUser);
                botMessage = new BotMessage("Включена фильтрация по типу недели.", ButtonSettings);
                botMessage = keyboardFormatter.formatSettings(botMessage, botUser);
                break;
            case CommandText.DISABLE_FILTER_WEEK_TYPE:
                botUser.setFilterNomDenom(false);
                botUserRepository.save(botUser);
                botMessage = new BotMessage("Выключена фильтрация по типу недели.", ButtonSettings);
                botMessage = keyboardFormatter.formatSettings(botMessage, botUser);
                break;
            case CommandText.CURRENT_USER_SETTINGS:
                String currentUserSettings = getStudentInfo(botUser) +
                        "\n" +
                        getScheduleNotificationStatus(botUser.getUserId()) +
                        "\n\n" +
                        "Фильтрация по типу недели: " +
                        (botUser.isFilterNomDenom() ? "вкл." : "выкл.");
                botMessage = new BotMessage(currentUserSettings, ButtonSettings);
                botMessage = keyboardFormatter.formatSettings(botMessage, botUser);
                break;
            case CommandText.DELETE_PROFILE:
                botMessage = new BotMessage(MessageText.DELETE_CONFIRRMATION, ButtonConfirmDeletion);
                botMessage = keyboardFormatter.formatSettings(botMessage, botUser);
                break;
            case CommandText.NO:
                botMessage = new BotMessage("Спасибо, что остаётесь с нами!", ButtonSettings);
                botMessage = keyboardFormatter.formatSettings(botMessage, botUser);
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
                ScheduleDailyNotification scheduleDailyNotification =
                        scheduleDailyNotificationRepository.findByUserId(userId);

                if(scheduleDailyNotification == null) {
                    scheduleDailyNotification = new ScheduleDailyNotification(userId, true, hourForSend);
                }
                else {
                    scheduleDailyNotification.setHourForSend(hourForSend);
                }
                scheduleDailyNotificationRepository.save(scheduleDailyNotification);
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
                ExamPeriodDailyNotification examPeriodDailyNotification =
                        examPeriodDailyNotificationRepository.findByUserId(userId);

                if(examPeriodDailyNotification == null) {
                    examPeriodDailyNotification = new ExamPeriodDailyNotification(userId, true, hourForSend);
                }
                else {
                    examPeriodDailyNotification.setHourForSend(hourForSend);
                }
                examPeriodDailyNotificationRepository.save(examPeriodDailyNotification);
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
            botMessage = new BotMessage("Теперь расписание будет приходить в " + hourForSend + " ч.", ButtonSettings);
            botMessage = keyboardFormatter.formatSettings(botMessage, botUser);
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
        ScheduleDailyNotification scheduleDailyNotification =
                scheduleDailyNotificationRepository.findByUserId(userId);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Рассылка расписания на сегодня: ");

        if(scheduleDailyNotification == null) {
            stringBuilder.append("не подключена.");
        }
        else {
            if(scheduleDailyNotification.isEnabled()) {
                stringBuilder.append("вкл, ");
            }
            else {
                stringBuilder.append("выкл, ");
            }

            if(scheduleDailyNotification.getHourForSend() == null) {
                stringBuilder.append("время не указано.");
            }
            else {
                stringBuilder.append(scheduleDailyNotification.getHourForSend()).append(" ч.");
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

        ExamPeriodDailyNotification examPeriodDailyNotification =
                examPeriodDailyNotificationRepository.findByUserId(userId);
        stringBuilder.append("Рассылка расписания сессии на сегодня: ");

        if(examPeriodDailyNotification == null) {
            stringBuilder.append("не подключена.");
        }
        else {
            if(examPeriodDailyNotification.isEnabled()) {
                stringBuilder.append("вкл, ");
            }
            else {
                stringBuilder.append("выкл, ");
            }

            if(examPeriodDailyNotification.getHourForSend() == null) {
                stringBuilder.append("время не указано.");
            }
            else {
                stringBuilder.append(examPeriodDailyNotification.getHourForSend()).append(" ч.");
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
