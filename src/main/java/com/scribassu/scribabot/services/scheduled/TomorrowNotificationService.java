package com.scribassu.scribabot.services.scheduled;

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

import static com.scribassu.scribabot.entities.notifications.NotificationType.*;
import static com.scribassu.scribabot.text.MessageText.NO_EXAMS;
import static com.scribassu.scribabot.text.MessageText.NO_LESSONS;

@Service
@Slf4j
@Data
public class TomorrowNotificationService {

    private final VkMessageSender vkMessageSender;
    private final TgMessageSender tgMessageSender;
    private final CallRestService callRestService;
    private final BotMessageGenerator botMessageGenerator;
    private final VkBotUserRepository vkBotUserRepository;
    private final TgBotUserRepository tgBotUserRepository;
    private final ScheduleNotificationRepository scheduleNotificationRepository;

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
        var scheduleTomorrowNotifications =
                scheduleNotificationRepository.findByHourForSendAndEnabled(hourOfDay, FULL_TIME_TOMORROW);

        if (!CollectionUtils.isEmpty(scheduleTomorrowNotifications)) {
            log.info("Send tomorrow full time schedule for hour {}", hourOfDay);
            int dayNumberInt = CalendarUtils.getDayOfWeekStartsFromMonday(calendar);
            if (dayNumberInt == 7) {
                dayNumberInt = 1;
            } else {
                dayNumberInt++;
            }
            final String dayNumber = String.valueOf(dayNumberInt);
            for (var notification : scheduleTomorrowNotifications) {
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
                    BotMessage botMessage = botMessageGenerator.getBotMessageForFullTimeLessons(lessons, CommandText.TOMORROW, botUser);
                    botMessage.setBotUser(botUser);
                    if (!(botUser.isSilentEmptyDays() && botMessage.getMessage().contains(NO_LESSONS))) {
                        if (botUser.fromVk()) {
                            vkMessageSender.send(botMessage);
                        } else {
                            tgMessageSender.send(botMessage);
                        }
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
        var examPeriodTomorrowNotifications =
                scheduleNotificationRepository.findByHourForSendAndEnabled(hourOfDay, EXAM_PERIOD_TOMORROW);

        if (!CollectionUtils.isEmpty(examPeriodTomorrowNotifications)) {
            log.info("Send tomorrow exam period schedule for hour {}", hourOfDay);
            for (var notification : examPeriodTomorrowNotifications) {
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
                    botMessage = botMessageGenerator.getBotMessageForFullTimeExamPeriod(examPeriodEventDto, CommandText.TOMORROW, botUser);
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
        var extramuralEventTomorrowNotifications =
                scheduleNotificationRepository.findByHourForSendAndEnabled(hourOfDay, EXTRAMURAL_EVENT_TOMORROW);

        if (!CollectionUtils.isEmpty(extramuralEventTomorrowNotifications)) {
            log.info("Send tomorrow extramural schedule for hour {}", hourOfDay);
            for (var notification : extramuralEventTomorrowNotifications) {
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
                    BotMessage botMessage = botMessageGenerator.getBotMessageForExtramuralEvent(extramuralDto, CommandText.TOMORROW, botUser);
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
            log.info("No need to send tomorrow extramural schedule for hour {}", hourOfDay);
        }
        log.info("Finish sending tomorrow extramural schedule for hour {}", hourOfDay);
    }
}
