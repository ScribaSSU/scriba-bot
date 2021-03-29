package com.scribassu.scribabot.keyboard;

import com.scribassu.scribabot.dto.BotMessage;
import com.scribassu.scribabot.entities.BotUser;
import com.scribassu.scribabot.repositories.*;
import org.springframework.stereotype.Component;

@Component
public class KeyboardFormatter {

    private final ExamPeriodAfterTomorrowNotificationRepository examPeriodAfterTomorrowNotificationRepository;
    private final ExamPeriodTomorrowNotificationRepository examPeriodTomorrowNotificationRepository;
    private final ExamPeriodDailyNotificationRepository examPeriodDailyNotificationRepository;
    private final ScheduleDailyNotificationRepository scheduleDailyNotificationRepository;
    private final ScheduleTomorrowNotificationRepository scheduleTomorrowNotificationRepository;

    public KeyboardFormatter(ExamPeriodAfterTomorrowNotificationRepository examPeriodAfterTomorrowNotificationRepository,
                             ExamPeriodTomorrowNotificationRepository examPeriodTomorrowNotificationRepository,
                             ExamPeriodDailyNotificationRepository examPeriodDailyNotificationRepository,
                             ScheduleDailyNotificationRepository scheduleDailyNotificationRepository,
                             ScheduleTomorrowNotificationRepository scheduleTomorrowNotificationRepository,
                             BotUserRepository botUserRepository) {
        this.examPeriodAfterTomorrowNotificationRepository = examPeriodAfterTomorrowNotificationRepository;
        this.examPeriodTomorrowNotificationRepository = examPeriodTomorrowNotificationRepository;
        this.examPeriodDailyNotificationRepository = examPeriodDailyNotificationRepository;
        this.scheduleDailyNotificationRepository = scheduleDailyNotificationRepository;
        this.scheduleTomorrowNotificationRepository = scheduleTomorrowNotificationRepository;
    }

    public BotMessage formatSettings(BotMessage botMessage, BotUser botUser) {
        return FormatUtils.addWeekFilter(botMessage, botUser);
    }

    public BotMessage formatSettingsEmptyLesson(BotMessage botMessage, BotUser botUser) {
        return FormatUtils.addEmptyLessonsFilter(botMessage, botUser);
    }

    public BotMessage formatSettingsScheduleNotif(BotMessage botMessage, BotUser botUser) {
        if(null == botUser) {
            botMessage = FormatUtils.addScheduleNotifs(botMessage, null, null);
        }
        else {
            final String userId = botUser.getUserId();
            botMessage = FormatUtils.addScheduleNotifs(
                    botMessage,
                    scheduleDailyNotificationRepository.findByUserId(userId),
                    scheduleTomorrowNotificationRepository.findByUserId(userId));
        }
        return botMessage;
    }

    public BotMessage formatSettingsExamNotif(BotMessage botMessage, BotUser botUser) {
        if(null == botUser) {
            botMessage = FormatUtils.addExamNotifs(botMessage, null, null, null);
        }
        else {
            final String userId = botUser.getUserId();
            botMessage = FormatUtils.addExamNotifs(
                    botMessage,
                    examPeriodDailyNotificationRepository.findByUserId(userId),
                    examPeriodTomorrowNotificationRepository.findByUserId(userId),
                    examPeriodAfterTomorrowNotificationRepository.findByUserId(userId));
        }
        return botMessage;
    }
}
