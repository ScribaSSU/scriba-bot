package com.scribassu.scribabot.services.scheduled;

import com.scribassu.scribabot.entities.notifications.ScheduleNotification;
import com.scribassu.scribabot.entities.users.TgBotUser;
import com.scribassu.scribabot.entities.users.VkBotUser;
import com.scribassu.scribabot.generators.BotMessageGenerator;
import com.scribassu.scribabot.message_handlers.TgMessageSender;
import com.scribassu.scribabot.message_handlers.VkMessageSender;
import com.scribassu.scribabot.model.BotMessage;
import com.scribassu.scribabot.model.BotUser;
import com.scribassu.scribabot.repositories.notifications.ScheduleNotificationRepository;
import com.scribassu.scribabot.repositories.users.TgBotUserRepository;
import com.scribassu.scribabot.repositories.users.VkBotUserRepository;
import com.scribassu.scribabot.services.CallRestService;
import com.scribassu.scribabot.text.CommandText;
import com.scribassu.scribabot.util.CalendarUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Calendar;
import java.util.List;

import static com.scribassu.scribabot.entities.notifications.NotificationType.*;
import static com.scribassu.scribabot.text.MessageText.NO_EXAMS;
import static com.scribassu.scribabot.text.MessageText.NO_LESSONS;

@Service
@Slf4j
@Data
public class TodayNotificationService {

    private final VkMessageSender vkMessageSender;
    private final TgMessageSender tgMessageSender;
    private final CallRestService callRestService;
    private final BotMessageGenerator botMessageGenerator;
    private final VkBotUserRepository vkBotUserRepository;
    private final TgBotUserRepository tgBotUserRepository;
    private final ScheduleNotificationRepository scheduleNotificationRepository;

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
        var scheduleTodayNotifications = scheduleNotificationRepository.findByHourForSendAndEnabled(hourOfDay, FULL_TIME_TODAY);

        if (!CollectionUtils.isEmpty(scheduleTodayNotifications)) {
            log.info("Send today full time schedule for hour {}", hourOfDay);
            final String dayNumber = String.valueOf(CalendarUtils.getDayOfWeekStartsFromMonday(calendar));
            for (var notification : scheduleTodayNotifications) {
                BotUser botUser;
                if (notification.fromVk()) {
                    VkBotUser vkBotUser = vkBotUserRepository.findOneById(notification.getUserId());
                    if (null != vkBotUser) {
                        botUser = new BotUser(vkBotUser);
                    } else {
                        continue;
                    }
                } else {
                    TgBotUser tgBotUser = tgBotUserRepository.findOneById(notification.getUserId());
                    if (null != tgBotUser) {
                        botUser = new BotUser(tgBotUser);
                    } else {
                        continue;
                    }
                }
                if (BotUser.isBotUserFullTime(botUser)) {
                    var lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            dayNumber
                    );
                    BotMessage botMessage = botMessageGenerator.getBotMessageForFullTimeLessons(lessons, CommandText.TODAY, botUser);
                    botMessage.setBotUser(botUser);
                    if (!(botUser.isSilentEmptyDays() && botMessage.getMessage().contains(NO_LESSONS))) {
                        if (botUser.fromVk()) {
                            vkMessageSender.send(botMessage);
                        } else {
                            tgMessageSender.send(botMessage);
                        }
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
        var examPeriodTodayNotifications = scheduleNotificationRepository.findByHourForSendAndEnabled(hourOfDay, EXAM_PERIOD_TODAY);

        if (!CollectionUtils.isEmpty(examPeriodTodayNotifications)) {
            log.info("Send today exam period schedule for hour {}", hourOfDay);
            for (var notification : examPeriodTodayNotifications) {
                BotUser botUser;
                if (notification.fromVk()) {
                    VkBotUser vkBotUser = vkBotUserRepository.findOneById(notification.getUserId());
                    if (null != vkBotUser) {
                        botUser = new BotUser(vkBotUser);
                    } else {
                        continue;
                    }
                } else {
                    TgBotUser tgBotUser = tgBotUserRepository.findOneById(notification.getUserId());
                    if (null != tgBotUser) {
                        botUser = new BotUser(tgBotUser);
                    } else {
                        continue;
                    }
                }
                if (BotUser.isBotUserFullTime(botUser)) {
                    var examPeriodEventDto = callRestService.getFullTimeExamPeriodEventByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            month,
                            day
                    );
                    BotMessage botMessage;
                    botMessage = botMessageGenerator.getBotMessageForFullTimeExamPeriod(examPeriodEventDto, CommandText.TODAY, botUser);
                    botMessage.setBotUser(botUser);
                    if (!(botUser.isSilentEmptyDays() && botMessage.getMessage().contains(NO_EXAMS))) {
                        if (botUser.fromVk()) {
                            vkMessageSender.send(botMessage);
                        } else {
                            tgMessageSender.send(botMessage);
                        }
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
        var extramuralEventTodayNotifications = scheduleNotificationRepository.findByHourForSendAndEnabled(hourOfDay, EXTRAMURAL_EVENT_TODAY);

        if (!CollectionUtils.isEmpty(extramuralEventTodayNotifications)) {
            log.info("Send today extramural schedule for hour {}", hourOfDay);
            for (var notification : extramuralEventTodayNotifications) {
                BotUser botUser;
                if (notification.fromVk()) {
                    VkBotUser vkBotUser = vkBotUserRepository.findOneById(notification.getUserId());
                    if (null != vkBotUser) {
                        botUser = new BotUser(vkBotUser);
                    } else {
                        continue;
                    }
                } else {
                    TgBotUser tgBotUser = tgBotUserRepository.findOneById(notification.getUserId());
                    if (null != tgBotUser) {
                        botUser = new BotUser(tgBotUser);
                    } else {
                        continue;
                    }
                }
                if (BotUser.isBotUserExtramural(botUser)) {
                    var extramuralDto = callRestService.getExtramuralEventsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            month,
                            day
                    );
                    BotMessage botMessage;
                    botMessage = botMessageGenerator.getBotMessageForExtramuralEvent(extramuralDto, CommandText.TODAY, botUser);
                    botMessage.setBotUser(botUser);
                    if (!(botUser.isSilentEmptyDays() && botMessage.getMessage().contains(NO_EXAMS))) {
                        if (botUser.fromVk()) {
                            vkMessageSender.send(botMessage);
                        } else {
                            tgMessageSender.send(botMessage);
                        }
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
