package com.scribassu.scribabot.services.bot.scheduled;

import com.scribassu.scribabot.dto.BotMessage;
import com.scribassu.scribabot.dto.rest.ExamPeriodEventDto;
import com.scribassu.scribabot.dto.rest.ExtramuralDto;
import com.scribassu.scribabot.dto.rest.FullTimeLessonDto;
import com.scribassu.scribabot.entities.ExamPeriodTodayNotification;
import com.scribassu.scribabot.entities.ExtramuralEventTodayNotification;
import com.scribassu.scribabot.repositories.ExamPeriodTodayNotificationRepository;
import com.scribassu.scribabot.repositories.ExtramuralEventTodayNotificationRepository;
import com.scribassu.scribabot.text.CommandText;
import com.scribassu.scribabot.entities.BotUser;
import com.scribassu.scribabot.entities.ScheduleTodayNotification;
import com.scribassu.scribabot.repositories.BotUserRepository;
import com.scribassu.scribabot.repositories.ScheduleTodayNotificationRepository;
import com.scribassu.scribabot.services.CallRestService;
import com.scribassu.scribabot.services.messages.MessageSender;
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

    private final MessageSender messageSender;
    private final CallRestService callRestService;
    private final BotUserRepository botUserRepository;
    private final ScheduleTodayNotificationRepository scheduleTodayNotificationRepository;
    private final ExamPeriodTodayNotificationRepository examPeriodTodayNotificationRepository;
    private final ExtramuralEventTodayNotificationRepository extramuralEventTodayNotificationRepository;

    @Autowired
    public TodayNotificationService(MessageSender messageSender,
                                    CallRestService callRestService,
                                    BotUserRepository botUserRepository,
                                    ScheduleTodayNotificationRepository scheduleTodayNotificationRepository,
                                    ExamPeriodTodayNotificationRepository examPeriodTodayNotificationRepository,
                                    ExtramuralEventTodayNotificationRepository extramuralEventTodayNotificationRepository) {
        this.messageSender = messageSender;
        this.callRestService = callRestService;
        this.botUserRepository = botUserRepository;
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
        log.info("Start to send full time schedule for hour {}", hourOfDay);
        List<ScheduleTodayNotification> scheduleTodayNotifications =
                scheduleTodayNotificationRepository.findByHourForSendAndEnabled(hourOfDay);

        if(!CollectionUtils.isEmpty(scheduleTodayNotifications)) {
            log.info("Send full time schedule for hour {}", hourOfDay);
            final String dayNumber = String.valueOf(CalendarUtils.getDayOfWeekStartsFromMonday(calendar));
            for(ScheduleTodayNotification notification : scheduleTodayNotifications) {
                BotUser botUser = botUserRepository.findOneById(notification.getUserId());
                if(BotMessageUtils.isBotUserFullTime(botUser)) {
                    FullTimeLessonDto lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            dayNumber
                    );
                    BotMessage botMessage = BotMessageUtils.getBotMessageForFullTimeLessons(lessons, CommandText.TODAY, botUser.isFilterNomDenom());
                    messageSender.send(botMessage, botUser.getUserId());
                    Thread.sleep(51); //20 messages per second
                }
            }
        }
        else {
            log.info("No need to send full time schedule for hour {}", hourOfDay);
        }
        log.info("Finish sending full time schedule for hour {}", hourOfDay);
    }

    private void sendExamPeriodSchedule() throws Exception {
        Calendar calendar = CalendarUtils.getCalendar();
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        log.info("Start to send exam period schedule for hour {}", hourOfDay);
        List<ExamPeriodTodayNotification> examPeriodTodayNotifications =
                examPeriodTodayNotificationRepository.findByHourForSendAndEnabled(hourOfDay);

        if(!CollectionUtils.isEmpty(examPeriodTodayNotifications)) {
            log.info("Send exam period schedule for hour {}", hourOfDay);
            for(ExamPeriodTodayNotification notification : examPeriodTodayNotifications) {
                BotUser botUser = botUserRepository.findOneById(notification.getUserId());
                if(BotMessageUtils.isBotUserFullTime(botUser)) {
                    ExamPeriodEventDto examPeriodEventDto = callRestService.getFullTimeExamPeriodEventByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            month,
                            day
                    );
                    BotMessage botMessage;
                    botMessage = BotMessageUtils.getBotMessageForFullTimeExamPeriod(examPeriodEventDto, CommandText.TODAY);
                    messageSender.send(botMessage, botUser.getUserId());
                    Thread.sleep(51); //20 messages per second
                }
            }
        }
        else {
            log.info("No need to send exam period schedule for hour {}", hourOfDay);
        }
        log.info("Finish sending exam period schedule for hour {}", hourOfDay);
    }

    private void sendExtramuralSchedule() throws Exception {
        Calendar calendar = CalendarUtils.getCalendar();
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        log.info("Start to send extramural schedule for hour {}", hourOfDay);
        List<ExtramuralEventTodayNotification> extramuralEventTodayNotifications =
                extramuralEventTodayNotificationRepository.findByHourForSendAndEnabled(hourOfDay);

        if (!CollectionUtils.isEmpty(extramuralEventTodayNotifications)) {
            log.info("Send extramural schedule for hour {}", hourOfDay);
            for (ExtramuralEventTodayNotification notification : extramuralEventTodayNotifications) {
                BotUser botUser = botUserRepository.findOneById(notification.getUserId());
                if (BotMessageUtils.isBotUserExtramural(botUser)) {
                    ExtramuralDto extramuralDto = callRestService.getExtramuralEventsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            month,
                            day
                    );
                    BotMessage botMessage;
                    botMessage = BotMessageUtils.getBotMessageForExtramuralEvent(extramuralDto, CommandText.TODAY);
                    messageSender.send(botMessage, botUser.getUserId());
                    Thread.sleep(51); //20 messages per second
                }
            }
        } else {
            log.info("No need to send extramural schedule for hour {}", hourOfDay);
        }
        log.info("Finish sending extramural schedule for hour {}", hourOfDay);
    }
}
