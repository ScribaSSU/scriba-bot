package com.scribassu.scribabot.services.bot.scheduled;

import com.scribassu.scribabot.dto.BotMessage;
import com.scribassu.scribabot.dto.rest.ExamPeriodEventDto;
import com.scribassu.scribabot.dto.rest.ExtramuralDto;
import com.scribassu.scribabot.dto.rest.FullTimeLessonDto;
import com.scribassu.scribabot.entities.ExamPeriodDailyNotification;
import com.scribassu.scribabot.entities.ExtramuralEventDailyNotification;
import com.scribassu.scribabot.repositories.ExamPeriodDailyNotificationRepository;
import com.scribassu.scribabot.repositories.ExtramuralEventDailyNotificationRepository;
import com.scribassu.scribabot.text.CommandText;
import com.scribassu.scribabot.entities.BotUser;
import com.scribassu.scribabot.entities.ScheduleDailyNotification;
import com.scribassu.scribabot.repositories.BotUserRepository;
import com.scribassu.scribabot.repositories.ScheduleDailyNotificationRepository;
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
public class DailyNotificationService {

    private final MessageSender messageSender;
    private final CallRestService callRestService;
    private final BotUserRepository botUserRepository;
    private final ScheduleDailyNotificationRepository scheduleDailyNotificationRepository;
    private final ExamPeriodDailyNotificationRepository examPeriodDailyNotificationRepository;
    private final ExtramuralEventDailyNotificationRepository extramuralEventDailyNotificationRepository;

    @Autowired
    public DailyNotificationService(MessageSender messageSender,
                                    CallRestService callRestService,
                                    BotUserRepository botUserRepository,
                                    ScheduleDailyNotificationRepository scheduleDailyNotificationRepository,
                                    ExamPeriodDailyNotificationRepository examPeriodDailyNotificationRepository,
                                    ExtramuralEventDailyNotificationRepository extramuralEventDailyNotificationRepository) {
        this.messageSender = messageSender;
        this.callRestService = callRestService;
        this.botUserRepository = botUserRepository;
        this.scheduleDailyNotificationRepository = scheduleDailyNotificationRepository;
        this.examPeriodDailyNotificationRepository = examPeriodDailyNotificationRepository;
        this.extramuralEventDailyNotificationRepository = extramuralEventDailyNotificationRepository;
    }

    @Scheduled(cron = "${scheduled.schedule-daily-notification-service.cron}")
    public void sendSchedule() throws Exception {
        sendFullTimeSchedule();
        sendExamPeriodSchedule();
        sendExtramuralSchedule();
    }

    private void sendFullTimeSchedule() throws Exception {
        Calendar calendar = CalendarUtils.getCalendar();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        log.info("Start to send full time schedule for hour {}", hourOfDay);
        List<ScheduleDailyNotification> scheduleDailyNotifications =
                scheduleDailyNotificationRepository.findByHourForSendAndEnabled(hourOfDay);

        if(!CollectionUtils.isEmpty(scheduleDailyNotifications)) {
            log.info("Send full time schedule for hour {}", hourOfDay);
            final String dayNumber = String.valueOf(CalendarUtils.getDayOfWeekStartsFromMonday(calendar));
            for(ScheduleDailyNotification notification : scheduleDailyNotifications) {
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
        List<ExamPeriodDailyNotification> examPeriodDailyNotifications =
                examPeriodDailyNotificationRepository.findByHourForSendAndEnabled(hourOfDay);

        if(!CollectionUtils.isEmpty(examPeriodDailyNotifications)) {
            log.info("Send exam period schedule for hour {}", hourOfDay);
            for(ExamPeriodDailyNotification notification : examPeriodDailyNotifications) {
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
        List<ExtramuralEventDailyNotification> extramuralEventDailyNotifications =
                extramuralEventDailyNotificationRepository.findByHourForSendAndEnabled(hourOfDay);

        if (!CollectionUtils.isEmpty(extramuralEventDailyNotifications)) {
            log.info("Send extramural schedule for hour {}", hourOfDay);
            for (ExtramuralEventDailyNotification notification : extramuralEventDailyNotifications) {
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
