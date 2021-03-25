package com.scribassu.scribabot.services.bot.scheduled;

import com.scribassu.scribabot.dto.BotMessage;
import com.scribassu.scribabot.dto.rest.ExamPeriodEventDto;
import com.scribassu.scribabot.dto.rest.ExtramuralDto;
import com.scribassu.scribabot.entities.BotUser;
import com.scribassu.scribabot.entities.ExamPeriodAfterTomorrowNotification;
import com.scribassu.scribabot.entities.ExtramuralEventAfterTomorrowNotification;
import com.scribassu.scribabot.repositories.BotUserRepository;
import com.scribassu.scribabot.repositories.ExamPeriodAfterTomorrowNotificationRepository;
import com.scribassu.scribabot.repositories.ExtramuralEventAfterTomorrowNotificationRepository;
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
public class AfterTomorrowNotificationService {

    private final MessageSender messageSender;
    private final CallRestService callRestService;
    private final BotUserRepository botUserRepository;
    private final ExamPeriodAfterTomorrowNotificationRepository examPeriodAfterTomorrowNotificationRepository;
    private final ExtramuralEventAfterTomorrowNotificationRepository extramuralEventAfterTomorrowNotificationRepository;

    @Autowired
    public AfterTomorrowNotificationService(MessageSender messageSender,
                                            CallRestService callRestService,
                                            BotUserRepository botUserRepository,
                                            ExamPeriodAfterTomorrowNotificationRepository examPeriodAfterTomorrowNotificationRepository,
                                            ExtramuralEventAfterTomorrowNotificationRepository extramuralEventAfterTomorrowNotificationRepository) {
        this.messageSender = messageSender;
        this.callRestService = callRestService;
        this.botUserRepository = botUserRepository;
        this.examPeriodAfterTomorrowNotificationRepository = examPeriodAfterTomorrowNotificationRepository;
        this.extramuralEventAfterTomorrowNotificationRepository = extramuralEventAfterTomorrowNotificationRepository;
    }

    @Scheduled(cron = "${scheduled.schedule-after-tomorrow-notification-service.cron}")
    public void sendSchedule() throws Exception {
        sendExamPeriodSchedule();
    }

    private void sendExamPeriodSchedule() throws Exception {
        Calendar calendar = CalendarUtils.getCalendar();
        calendar.add(Calendar.DAY_OF_MONTH, 2);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.DAY_OF_MONTH, -2);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        log.info("Start to send after tomorrow exam period schedule for hour {}", hourOfDay);
        List<ExamPeriodAfterTomorrowNotification> examPeriodAfterTomorrowNotifications =
                examPeriodAfterTomorrowNotificationRepository.findByHourForSendAndEnabled(hourOfDay);

        if(!CollectionUtils.isEmpty(examPeriodAfterTomorrowNotifications)) {
            log.info("Send after tomorrow exam period schedule for hour {}", hourOfDay);
            for(ExamPeriodAfterTomorrowNotification notification : examPeriodAfterTomorrowNotifications) {
                BotUser botUser = botUserRepository.findOneById(notification.getUserId());
                if(BotMessageUtils.isBotUserFullTime(botUser)) {
                    ExamPeriodEventDto examPeriodEventDto = callRestService.getFullTimeExamPeriodEventByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            month,
                            day
                    );
                    BotMessage botMessage;
                    botMessage = BotMessageUtils.getBotMessageForFullTimeExamPeriod(examPeriodEventDto, CommandText.AFTER_TOMORROW);
                    messageSender.send(botMessage, botUser.getUserId());
                    Thread.sleep(51); //20 messages per second
                }
            }
        }
        else {
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
        log.info("Start to send tomorrow extramural schedule for hour {}", hourOfDay);
        List<ExtramuralEventAfterTomorrowNotification> extramuralEventAfterTomorrowNotifications =
                extramuralEventAfterTomorrowNotificationRepository.findByHourForSendAndEnabled(hourOfDay);

        if (!CollectionUtils.isEmpty(extramuralEventAfterTomorrowNotifications)) {
            log.info("Send extramural schedule for hour {}", hourOfDay);
            for (ExtramuralEventAfterTomorrowNotification notification : extramuralEventAfterTomorrowNotifications) {
                BotUser botUser = botUserRepository.findOneById(notification.getUserId());
                if (!BotMessageUtils.isBotUserFullTime(botUser)) {
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
