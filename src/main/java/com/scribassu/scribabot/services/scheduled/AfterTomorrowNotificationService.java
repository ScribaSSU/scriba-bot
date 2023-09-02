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
import com.scribassu.tracto.dto.ExamPeriodEventListDto;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Calendar;

import static com.scribassu.scribabot.entities.notifications.NotificationType.EXAM_PERIOD_AFTER_TOMORROW;
import static com.scribassu.scribabot.entities.notifications.NotificationType.EXTRAMURAL_EVENT_AFTER_TOMORROW;
import static com.scribassu.scribabot.text.MessageText.NO_EXAMS;

@Service
@Slf4j
@Data
public class AfterTomorrowNotificationService {

    private final VkMessageSender vkMessageSender;
    private final TgMessageSender tgMessageSender;
    private final CallRestService callRestService;
    private final BotMessageGenerator botMessageGenerator;
    private final VkBotUserRepository vkBotUserRepository;
    private final TgBotUserRepository tgBotUserRepository;
    private final ScheduleNotificationRepository scheduleNotificationRepository;
    @Scheduled(cron = "${scheduled.schedule-after-tomorrow-notification-service.cron}")
    public void sendSchedule() throws Exception {
        sendExamPeriodSchedule();
        sendExtramuralSchedule();
    }

    private void sendExamPeriodSchedule() throws Exception {
        Calendar calendar = CalendarUtils.getCalendar();
        calendar.add(Calendar.DAY_OF_MONTH, 2);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.DAY_OF_MONTH, -2);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        log.info("Start to send after tomorrow exam period schedule for hour {}", hourOfDay);
        var examPeriodAfterTomorrowNotifications =
                scheduleNotificationRepository.findByHourForSendAndEnabled(hourOfDay, EXAM_PERIOD_AFTER_TOMORROW);

        if (!CollectionUtils.isEmpty(examPeriodAfterTomorrowNotifications)) {
            log.info("Send after tomorrow exam period schedule for hour {}", hourOfDay);
            for (var notification : examPeriodAfterTomorrowNotifications) {
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
                    ExamPeriodEventListDto examPeriodEventDto = callRestService.getFullTimeExamPeriodEventByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            month,
                            day
                    );
                    BotMessage botMessage;
                    botMessage = botMessageGenerator.getBotMessageForFullTimeExamPeriod(examPeriodEventDto, CommandText.AFTER_TOMORROW, botUser);
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
            log.info("No need to send after tomorrow exam period schedule for hour {}", hourOfDay);
        }
        log.info("Finish sending after tomorrow exam period schedule for hour {}", hourOfDay);
    }

    private void sendExtramuralSchedule() throws Exception {
        Calendar calendar = CalendarUtils.getCalendar();
        calendar.add(Calendar.DAY_OF_MONTH, 2);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.DAY_OF_MONTH, -2);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        log.info("Start to send after tomorrow extramural schedule for hour {}", hourOfDay);
        var extramuralEventAfterTomorrowNotifications =
                scheduleNotificationRepository.findByHourForSendAndEnabled(hourOfDay, EXTRAMURAL_EVENT_AFTER_TOMORROW);

        if (!CollectionUtils.isEmpty(extramuralEventAfterTomorrowNotifications)) {
            log.info("Send after tomorrow extramural schedule for hour {}", hourOfDay);
            for (var notification : extramuralEventAfterTomorrowNotifications) {
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
                    botMessage = botMessageGenerator.getBotMessageForExtramuralEvent(extramuralDto, CommandText.AFTER_TOMORROW, botUser);
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
            log.info("No need to send after tomorrow extramural schedule for hour {}", hourOfDay);
        }
        log.info("Finish sending after tomorrow extramural schedule for hour {}", hourOfDay);
    }
}
