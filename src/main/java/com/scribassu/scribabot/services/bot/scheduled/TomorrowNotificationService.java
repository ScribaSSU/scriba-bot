package com.scribassu.scribabot.services.bot.scheduled;

import com.scribassu.scribabot.dto.BotMessage;
import com.scribassu.scribabot.dto.Command;
import com.scribassu.scribabot.dto.rest.ExamPeriodEventDto;
import com.scribassu.scribabot.dto.rest.FullTimeLessonDto;
import com.scribassu.scribabot.entities.BotUser;
import com.scribassu.scribabot.entities.ExamPeriodDailyNotification;
import com.scribassu.scribabot.entities.ExamPeriodTomorrowNotification;
import com.scribassu.scribabot.entities.ScheduleTomorrowNotification;
import com.scribassu.scribabot.repositories.BotUserRepository;
import com.scribassu.scribabot.repositories.ExamPeriodTomorrowNotificationRepository;
import com.scribassu.scribabot.repositories.ScheduleTomorrowNotificationRepository;
import com.scribassu.scribabot.services.CallRestService;
import com.scribassu.scribabot.services.messages.MessageSender;
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

    private final MessageSender messageSender;
    private final CallRestService callRestService;
    private final BotUserRepository botUserRepository;
    private final ScheduleTomorrowNotificationRepository scheduleTomorrowNotificationRepository;
    private final ExamPeriodTomorrowNotificationRepository examPeriodTomorrowNotificationRepository;

    @Autowired
    public TomorrowNotificationService(MessageSender messageSender,
                                       CallRestService callRestService,
                                       BotUserRepository botUserRepository,
                                       ScheduleTomorrowNotificationRepository scheduleTomorrowNotificationRepository,
                                       ExamPeriodTomorrowNotificationRepository examPeriodTomorrowNotificationRepository) {
        this.messageSender = messageSender;
        this.callRestService = callRestService;
        this.botUserRepository = botUserRepository;
        this.scheduleTomorrowNotificationRepository = scheduleTomorrowNotificationRepository;
        this.examPeriodTomorrowNotificationRepository = examPeriodTomorrowNotificationRepository;
    }

    @Scheduled(cron = "${scheduled.schedule-tomorrow-notification-service.cron}")
    public void sendSchedule() throws Exception {
        sendFullTimeSchedule();
        sendExamPeriodSchedule();
    }

    private void sendFullTimeSchedule() throws Exception {
        Calendar calendar = CalendarUtils.getCalendar();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        log.info("Start to send tomorrow full time schedule for hour {}", hourOfDay);
        List<ScheduleTomorrowNotification> scheduleTomorrowNotifications =
                scheduleTomorrowNotificationRepository.findByHourForSendAndEnabled(hourOfDay);

        if(!CollectionUtils.isEmpty(scheduleTomorrowNotifications)) {
            log.info("Send tomorrow full time schedule for hour {}", hourOfDay);
            int dayNumberInt = CalendarUtils.getDayOfWeekStartsFromMonday(calendar);
            if(dayNumberInt == 7) {
                dayNumberInt = 1;
            }
            else {
                dayNumberInt++;
            }
            final String dayNumber = String.valueOf(dayNumberInt);
            for(ScheduleTomorrowNotification notification : scheduleTomorrowNotifications) {
                BotUser botUser = botUserRepository.findOneById(notification.getUserId());
                if(BotMessageUtils.isBotUserFullTime(botUser)) {
                    FullTimeLessonDto lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            dayNumber
                    );
                    BotMessage botMessage = BotMessageUtils.getBotMessageForFullTimeLessons(lessons, CommandText.TOMORROW, botUser.isFilterNomDenom());
                    messageSender.send(botMessage, botUser.getUserId());
                }
            }
        }
        else {
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

        if(!CollectionUtils.isEmpty(examPeriodTomorrowNotifications)) {
            log.info("Send tomorrow exam period schedule for hour {}", hourOfDay);
            for(ExamPeriodTomorrowNotification notification : examPeriodTomorrowNotifications) {
                BotUser botUser = botUserRepository.findOneById(notification.getUserId());
                if(BotMessageUtils.isBotUserFullTime(botUser)) {
                    ExamPeriodEventDto examPeriodEventDto = callRestService.getFullTimeExamPeriodEventByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            month,
                            day
                    );
                    BotMessage botMessage;
                    if(examPeriodEventDto.getExamPeriodEvents().isEmpty()) {
                        botMessage = BotMessageUtils.getBotMessageForEmptyFullTimeExamPeriod();
                    }
                    else {
                        botMessage = BotMessageUtils.getBotMessageForFullTimeExamPeriod(examPeriodEventDto, CommandText.TOMORROW);
                    }
                    messageSender.send(botMessage, botUser.getUserId());
                    Thread.sleep(51); //20 messages per second
                }
            }
        }
        else {
            log.info("No need to send tomorrow exam period schedule for hour {}", hourOfDay);
        }
        log.info("Finish sending tomorrow exam period schedule for hour {}", hourOfDay);
    }
}
