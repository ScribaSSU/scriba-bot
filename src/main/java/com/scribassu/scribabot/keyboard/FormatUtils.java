package com.scribassu.scribabot.keyboard;

import com.scribassu.scribabot.dto.BotMessage;
import com.scribassu.scribabot.entities.*;

import static com.scribassu.scribabot.keyboard.KeyboardType.*;

public class FormatUtils {

    public static BotMessage addWeekFilter(BotMessage botMessage, BotUser botUser) {
        if(null == botUser) {
            botMessage.formatKeyboard(ReplacedConstants.WEEK_FILTER, PartButtonEnableWeekFilter);
        }
        else if(botUser.isFilterNomDenom()) {
            botMessage.formatKeyboard(ReplacedConstants.WEEK_FILTER, PartButtonDisableWeekFilter);
        }
        else {
            botMessage.formatKeyboard(ReplacedConstants.WEEK_FILTER, PartButtonEnableWeekFilter);
        }
        return botMessage;
    }

    public static BotMessage addEmptyLessonsFilter(BotMessage botMessage, BotUser botUser) {
        if(null == botUser) {
            botMessage.formatKeyboard(ReplacedConstants.EMPTY_LESSONS_FILTER, PartButtonEnableEmptyLessonsFilter);
        }
        else if(botUser.isFilterLessonNotif()) {
            botMessage.formatKeyboard(ReplacedConstants.EMPTY_LESSONS_FILTER, PartButtonEnableEmptyLessonsFilter);
        }
        else {
            botMessage.formatKeyboard(ReplacedConstants.EMPTY_LESSONS_FILTER, PartButtonDisableEmptyLessonsFilter);
        }
        return botMessage;
    }

    public static BotMessage addScheduleNotifs(BotMessage botMessage,
                                               ScheduleDailyNotification scheduleDailyNotification,
                                               ScheduleTomorrowNotification scheduleTomorrowNotification) {
        botMessage = addScheduleNotifDaily(botMessage, scheduleDailyNotification);
        botMessage = addScheduleNotifTomorrow(botMessage, scheduleTomorrowNotification);
        return botMessage;
    }

    public static BotMessage addExamNotifs(BotMessage botMessage,
                                           ExamPeriodDailyNotification examPeriodDailyNotification,
                                           ExamPeriodTomorrowNotification examPeriodTomorrowNotification,
                                           ExamPeriodAfterTomorrowNotification examPeriodAfterTomorrowNotification) {
        botMessage = addExamNotifDaily(botMessage, examPeriodDailyNotification);
        botMessage = addExamNotifTomorrow(botMessage, examPeriodTomorrowNotification);
        botMessage = addExamNotifAfterTomorrow(botMessage, examPeriodAfterTomorrowNotification);
        return botMessage;
    }

    public static BotMessage addExamNotifDaily(BotMessage botMessage,
                                               ExamPeriodDailyNotification examPeriodDailyNotification) {
        if(null == examPeriodDailyNotification) {
            botMessage.formatKeyboard(ReplacedConstants.EXAM_NOTIF_DAILY, PartButtonEnableExamNotificationDaily);
        }
        else if(examPeriodDailyNotification.isEnabled()) {
            botMessage.formatKeyboard(ReplacedConstants.EXAM_NOTIF_DAILY, PartButtonDisableExamNotificationDaily);
        }
        else {
            botMessage.formatKeyboard(ReplacedConstants.EXAM_NOTIF_DAILY, PartButtonEnableExamNotificationDaily);
        }
        return botMessage;
    }

    public static BotMessage addExamNotifTomorrow(BotMessage botMessage,
                                                  ExamPeriodTomorrowNotification examPeriodTomorrowNotification) {
        if(null == examPeriodTomorrowNotification) {
            botMessage.formatKeyboard(ReplacedConstants.EXAM_NOTIF_TOMORROW, PartButtonEnableExamNotificationTomorrow);
        }
        else if(examPeriodTomorrowNotification.isEnabled()) {
            botMessage.formatKeyboard(ReplacedConstants.EXAM_NOTIF_TOMORROW, PartButtonDisableExamNotificationTomorrow);
        }
        else {
            botMessage.formatKeyboard(ReplacedConstants.EXAM_NOTIF_TOMORROW, PartButtonEnableExamNotificationTomorrow);
        }
        return botMessage;
    }

    public static BotMessage addExamNotifAfterTomorrow(BotMessage botMessage,
                                                       ExamPeriodAfterTomorrowNotification examPeriodAfterTomorrowNotification) {
        if(null == examPeriodAfterTomorrowNotification) {
            botMessage.formatKeyboard(ReplacedConstants.EXAM_NOTIF_AFTER_TOMORROW, PartButtonEnableExamNotificationAfterTomorrow);
        }
        else if(examPeriodAfterTomorrowNotification.isEnabled()) {
            botMessage.formatKeyboard(ReplacedConstants.EXAM_NOTIF_AFTER_TOMORROW, PartButtonDisableExamNotificationAfterTomorrow);
        }
        else {
            botMessage.formatKeyboard(ReplacedConstants.EXAM_NOTIF_AFTER_TOMORROW, PartButtonEnableExamNotificationAfterTomorrow);
        }
        return botMessage;
    }

    public static BotMessage addScheduleNotifDaily(BotMessage botMessage,
                                                   ScheduleDailyNotification scheduleDailyNotification) {
        if(null == scheduleDailyNotification) {
            botMessage.formatKeyboard(ReplacedConstants.SCHEDULE_NOTIF_DAILY, PartButtonEnableScheduleNotificationDaily);
        }
        else if(scheduleDailyNotification.isEnabled()) {
            botMessage.formatKeyboard(ReplacedConstants.SCHEDULE_NOTIF_DAILY, PartButtonDisableScheduleNotificationDaily);
        }
        else {
            botMessage.formatKeyboard(ReplacedConstants.SCHEDULE_NOTIF_DAILY, PartButtonEnableScheduleNotificationDaily);
        }
        return botMessage;
    }

    public static BotMessage addScheduleNotifTomorrow(BotMessage botMessage,
                                                      ScheduleTomorrowNotification scheduleTomorrowNotification) {
        if(null == scheduleTomorrowNotification) {
            botMessage.formatKeyboard(ReplacedConstants.SCHEDULE_NOTIF_TOMORROW, PartButtonEnableScheduleNotificationTomorrow);
        }
        else if(scheduleTomorrowNotification.isEnabled()) {
            botMessage.formatKeyboard(ReplacedConstants.SCHEDULE_NOTIF_TOMORROW, PartButtonDisableScheduleNotificationTomorrow);
        }
        else {
            botMessage.formatKeyboard(ReplacedConstants.SCHEDULE_NOTIF_TOMORROW, PartButtonEnableScheduleNotificationTomorrow);
        }
        return botMessage;
    }
}
