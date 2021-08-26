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
public class TomorrowNotificationService {

    private final VkMessageSender vkMessageSender;
    private final TgMessageSender tgMessageSender;
    private final CallRestService callRestService;
    private final VkBotUserRepository vkBotUserRepository;
    private final TgBotUserRepository tgBotUserRepository;
    private final ScheduleTomorrowNotificationRepository scheduleTomorrowNotificationRepository;
    private final ExamPeriodTomorrowNotificationRepository examPeriodTomorrowNotificationRepository;
    private final ExtramuralEventTomorrowNotificationRepository extramuralEventTomorrowNotificationRepository;

    @Autowired
    public TomorrowNotificationService(VkMessageSender vkMessageSender,
                                       TgMessageSender tgMessageSender,
                                       CallRestService callRestService,
                                       VkBotUserRepository vkBotUserRepository,
                                       TgBotUserRepository tgBotUserRepository,
                                       ScheduleTomorrowNotificationRepository scheduleTomorrowNotificationRepository,
                                       ExamPeriodTomorrowNotificationRepository examPeriodTomorrowNotificationRepository,
                                       ExtramuralEventTomorrowNotificationRepository extramuralEventTomorrowNotificationRepository) {
        this.vkMessageSender = vkMessageSender;
        this.tgMessageSender = tgMessageSender;
        this.callRestService = callRestService;
        this.vkBotUserRepository = vkBotUserRepository;
        this.tgBotUserRepository = tgBotUserRepository;
        this.scheduleTomorrowNotificationRepository = scheduleTomorrowNotificationRepository;
        this.examPeriodTomorrowNotificationRepository = examPeriodTomorrowNotificationRepository;
        this.extramuralEventTomorrowNotificationRepository = extramuralEventTomorrowNotificationRepository;
    }

    @Scheduled(cron = "${scheduled.schedule-tomorrow-notification-service.cron}")
    public void sendSchedule() throws Exception {
        sendFullTimeSchedule();
        sendExamPeriodSchedule();
        sendExtramuralSchedule();
    }

    private void sendFullTimeSchedule() throws Exception {
        Calendar calendar = CalendarUtils.getCalendar();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        log.info("Start to send tomorrow full time schedule for hour {}", hourOfDay);
        List<ScheduleTomorrowNotification> scheduleTomorrowNotifications =
                scheduleTomorrowNotificationRepository.findByHourForSendAndEnabled(hourOfDay);

        if (!CollectionUtils.isEmpty(scheduleTomorrowNotifications)) {
            log.info("Send tomorrow full time schedule for hour {}", hourOfDay);
            int dayNumberInt = CalendarUtils.getDayOfWeekStartsFromMonday(calendar);
            if (dayNumberInt == 7) {
                dayNumberInt = 1;
            } else {
                dayNumberInt++;
            }
            final String dayNumber = String.valueOf(dayNumberInt);
            for (ScheduleTomorrowNotification notification : scheduleTomorrowNotifications) {
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
                    BotMessage botMessage = BotMessageUtils.getBotMessageForFullTimeLessons(lessons, CommandText.TOMORROW, botUser.isFilterNomDenom(), botUser);
                    if (botUser.fromVk()) {
                        vkMessageSender.send(botMessage, botUser.getUserId());
                    } else {
                        tgMessageSender.send(botMessage, botUser.getUserId());
                    }
                }
            }
        } else {
            log.info("No need to send tomorrow full time schedule for hour {}", hourOfDay);
        }
        log.info("Finish sending tomorrow full time schedule for hour {}", hourOfDay);
    }

    private void sendExamPeriodSchedule() throws Exception {
        Calendar calendar = CalendarUtils.getCalendar();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        log.info("Start to send tomorrow exam period schedule for hour {}", hourOfDay);
        List<ExamPeriodTomorrowNotification> examPeriodTomorrowNotifications =
                examPeriodTomorrowNotificationRepository.findByHourForSendAndEnabled(hourOfDay);

        if (!CollectionUtils.isEmpty(examPeriodTomorrowNotifications)) {
            log.info("Send tomorrow exam period schedule for hour {}", hourOfDay);
            for (ExamPeriodTomorrowNotification notification : examPeriodTomorrowNotifications) {
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
                    botMessage = BotMessageUtils.getBotMessageForFullTimeExamPeriod(examPeriodEventDto, CommandText.TOMORROW, botUser);
                    if (botUser.fromVk()) {
                        vkMessageSender.send(botMessage, botUser.getUserId());
                    } else {
                        tgMessageSender.send(botMessage, botUser.getUserId());
                    }
                    Thread.sleep(51); //20 messages per second
                }
            }
        } else {
            log.info("No need to send tomorrow exam period schedule for hour {}", hourOfDay);
        }
        log.info("Finish sending tomorrow exam period schedule for hour {}", hourOfDay);
    }

    private void sendExtramuralSchedule() throws Exception {
        Calendar calendar = CalendarUtils.getCalendar();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        log.info("Start to send tomorrow extramural schedule for hour {}", hourOfDay);
        List<ExtramuralEventTomorrowNotification> extramuralEventTomorrowNotifications =
                extramuralEventTomorrowNotificationRepository.findByHourForSendAndEnabled(hourOfDay);

        if (!CollectionUtils.isEmpty(extramuralEventTomorrowNotifications)) {
            log.info("Send tomorrow extramural schedule for hour {}", hourOfDay);
            for (ExtramuralEventTomorrowNotification notification : extramuralEventTomorrowNotifications) {
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
                    BotMessage botMessage = BotMessageUtils.getBotMessageForExtramuralEvent(extramuralDto, CommandText.TOMORROW, botUser);
                    if (botUser.fromVk()) {
                        vkMessageSender.send(botMessage, botUser.getUserId());
                    } else {
                        tgMessageSender.send(botMessage, botUser.getUserId());
                    }
                    Thread.sleep(51); //20 messages per second
                }
            }
        } else {
            log.info("No need to send tomorrow extramural schedule for hour {}", hourOfDay);
        }
        log.info("Finish sending tomorrow extramural schedule for hour {}", hourOfDay);
    }
}
