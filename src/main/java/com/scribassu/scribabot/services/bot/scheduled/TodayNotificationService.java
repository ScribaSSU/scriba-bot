package com.scribassu.scribabot.services.bot.scheduled;

import com.scribassu.scribabot.dto.BotMessage;
import com.scribassu.scribabot.dto.InnerBotUser;
import com.scribassu.scribabot.dto.rest.ExamPeriodEventDto;
import com.scribassu.scribabot.dto.rest.ExtramuralDto;
import com.scribassu.scribabot.dto.rest.FullTimeLessonDto;
import com.scribassu.scribabot.entities.*;
import com.scribassu.scribabot.repositories.*;
import com.scribassu.scribabot.services.CallRestService;
import com.scribassu.scribabot.services.messages.TgMessageSender;
import com.scribassu.scribabot.services.messages.VkMessageSender;
import com.scribassu.scribabot.text.CommandText;
import com.scribassu.scribabot.util.BotMessageUtils;
import com.scribassu.scribabot.util.CalendarUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Calendar;
import java.util.List;

@Slf4j
@Service
public class TodayNotificationService {

    private final VkMessageSender vkMessageSender;
    private final TgMessageSender tgMessageSender;
    private final CallRestService callRestService;
    private final VkBotUserRepository vkBotUserRepository;
    private final TgBotUserRepository tgBotUserRepository;
    private final ScheduleTodayNotificationRepository scheduleTodayNotificationRepository;
    private final ExamPeriodTodayNotificationRepository examPeriodTodayNotificationRepository;
    private final ExtramuralEventTodayNotificationRepository extramuralEventTodayNotificationRepository;

    @Autowired
    public TodayNotificationService(VkMessageSender vkMessageSender,
                                    TgMessageSender tgMessageSender,
                                    CallRestService callRestService,
                                    VkBotUserRepository vkBotUserRepository,
                                    TgBotUserRepository tgBotUserRepository,
                                    ScheduleTodayNotificationRepository scheduleTodayNotificationRepository,
                                    ExamPeriodTodayNotificationRepository examPeriodTodayNotificationRepository,
                                    ExtramuralEventTodayNotificationRepository extramuralEventTodayNotificationRepository) {
        this.vkMessageSender = vkMessageSender;
        this.tgMessageSender = tgMessageSender;
        this.callRestService = callRestService;
        this.vkBotUserRepository = vkBotUserRepository;
        this.tgBotUserRepository = tgBotUserRepository;
        this.scheduleTodayNotificationRepository = scheduleTodayNotificationRepository;
        this.examPeriodTodayNotificationRepository = examPeriodTodayNotificationRepository;
        this.extramuralEventTodayNotificationRepository = extramuralEventTodayNotificationRepository;
    }

    @Scheduled(cron = "${scheduled.schedule-today-notification-service.cron}")
    public void sendSchedule() throws Exception {
        sendFullTimeSchedule();
        sendExamPeriodSchedule();
        sendExtramuralSchedule();
    }

    private void sendFullTimeSchedule() throws Exception {
        Calendar calendar = CalendarUtils.getCalendar();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        log.info("Start to send today full time schedule for hour {}", hourOfDay);
        List<ScheduleTodayNotification> scheduleTodayNotifications =
                scheduleTodayNotificationRepository.findByHourForSendAndEnabled(hourOfDay);

        if (!CollectionUtils.isEmpty(scheduleTodayNotifications)) {
            log.info("Send today full time schedule for hour {}", hourOfDay);
            final String dayNumber = String.valueOf(CalendarUtils.getDayOfWeekStartsFromMonday(calendar));
            for (ScheduleTodayNotification notification : scheduleTodayNotifications) {
                InnerBotUser botUser;
                if (notification.fromVk()) {
                    VkBotUser vkBotUser = vkBotUserRepository.findOneById(notification.getUserId());
                    botUser = new InnerBotUser(vkBotUser);
                } else {
                    TgBotUser tgBotUser = tgBotUserRepository.findOneById(notification.getUserId());
                    botUser = new InnerBotUser(tgBotUser);
                }
                if (BotMessageUtils.isBotUserFullTime(botUser)) {
                    FullTimeLessonDto lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            dayNumber
                    );
                    BotMessage botMessage = BotMessageUtils.getBotMessageForFullTimeLessons(lessons, CommandText.TODAY, botUser.isFilterNomDenom(), botUser);
                    if (botUser.fromVk()) {
                        vkMessageSender.send(botMessage, botUser.getUserId());
                    } else {
                        tgMessageSender.send(botMessage, botUser.getUserId());
                    }
                    Thread.sleep(51); //20 messages per second
                }
            }
        } else {
            log.info("No need to send today full time schedule for hour {}", hourOfDay);
        }
        log.info("Finish sending today full time schedule for hour {}", hourOfDay);
    }

    private void sendExamPeriodSchedule() throws Exception {
        Calendar calendar = CalendarUtils.getCalendar();
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        log.info("Start to send today exam period schedule for hour {}", hourOfDay);
        List<ExamPeriodTodayNotification> examPeriodTodayNotifications =
                examPeriodTodayNotificationRepository.findByHourForSendAndEnabled(hourOfDay);

        if (!CollectionUtils.isEmpty(examPeriodTodayNotifications)) {
            log.info("Send today exam period schedule for hour {}", hourOfDay);
            for (ExamPeriodTodayNotification notification : examPeriodTodayNotifications) {
                InnerBotUser botUser;
                if (notification.fromVk()) {
                    VkBotUser vkBotUser = vkBotUserRepository.findOneById(notification.getUserId());
                    botUser = new InnerBotUser(vkBotUser);
                } else {
                    TgBotUser tgBotUser = tgBotUserRepository.findOneById(notification.getUserId());
                    botUser = new InnerBotUser(tgBotUser);
                }
                if (BotMessageUtils.isBotUserFullTime(botUser)) {
                    ExamPeriodEventDto examPeriodEventDto = callRestService.getFullTimeExamPeriodEventByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            month,
                            day
                    );
                    BotMessage botMessage;
                    botMessage = BotMessageUtils.getBotMessageForFullTimeExamPeriod(examPeriodEventDto, CommandText.TODAY, botUser);
                    if (botUser.fromVk()) {
                        vkMessageSender.send(botMessage, botUser.getUserId());
                    } else {
                        tgMessageSender.send(botMessage, botUser.getUserId());
                    }
                    Thread.sleep(51); //20 messages per second
                }
            }
        } else {
            log.info("No need to send today exam period schedule for hour {}", hourOfDay);
        }
        log.info("Finish sending today exam period schedule for hour {}", hourOfDay);
    }

    private void sendExtramuralSchedule() throws Exception {
        Calendar calendar = CalendarUtils.getCalendar();
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        log.info("Start to send today extramural schedule for hour {}", hourOfDay);
        List<ExtramuralEventTodayNotification> extramuralEventTodayNotifications =
                extramuralEventTodayNotificationRepository.findByHourForSendAndEnabled(hourOfDay);

        if (!CollectionUtils.isEmpty(extramuralEventTodayNotifications)) {
            log.info("Send today extramural schedule for hour {}", hourOfDay);
            for (ExtramuralEventTodayNotification notification : extramuralEventTodayNotifications) {
                InnerBotUser botUser;
                if (notification.fromVk()) {
                    VkBotUser vkBotUser = vkBotUserRepository.findOneById(notification.getUserId());
                    botUser = new InnerBotUser(vkBotUser);
                } else {
                    TgBotUser tgBotUser = tgBotUserRepository.findOneById(notification.getUserId());
                    botUser = new InnerBotUser(tgBotUser);
                }
                if (BotMessageUtils.isBotUserExtramural(botUser)) {
                    ExtramuralDto extramuralDto = callRestService.getExtramuralEventsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            month,
                            day
                    );
                    BotMessage botMessage;
                    botMessage = BotMessageUtils.getBotMessageForExtramuralEvent(extramuralDto, CommandText.TODAY, botUser);
                    if (botUser.fromVk()) {
                        vkMessageSender.send(botMessage, botUser.getUserId());
                    } else {
                        tgMessageSender.send(botMessage, botUser.getUserId());
                    }
                    Thread.sleep(51); //20 messages per second
                }
            }
        } else {
            log.info("No need to send today extramural schedule for hour {}", hourOfDay);
        }
        log.info("Finish sending today extramural schedule for hour {}", hourOfDay);
    }
}
