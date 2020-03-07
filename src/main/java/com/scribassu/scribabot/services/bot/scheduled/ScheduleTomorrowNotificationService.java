package com.scribassu.scribabot.services.bot.scheduled;

import com.scribassu.scribabot.dto.FullTimeLessonDto;
import com.scribassu.scribabot.entities.BotUser;
import com.scribassu.scribabot.entities.ScheduleTomorrowNotification;
import com.scribassu.scribabot.repositories.BotUserRepository;
import com.scribassu.scribabot.repositories.ScheduleTomorrowNotificationRepository;
import com.scribassu.scribabot.services.CallRestService;
import com.scribassu.scribabot.services.messages.MessageSender;
import com.scribassu.scribabot.text.CommandText;
import com.scribassu.scribabot.util.BotMessageUtils;
import com.scribassu.scribabot.util.CalendarUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ScheduleTomorrowNotificationService {

    private final MessageSender messageSender;
    private final CallRestService callRestService;
    private final BotUserRepository botUserRepository;
    private final ScheduleTomorrowNotificationRepository scheduleTomorrowNotificationRepository;

    @Autowired
    public ScheduleTomorrowNotificationService(MessageSender messageSender,
                                               CallRestService callRestService,
                                               BotUserRepository botUserRepository,
                                               ScheduleTomorrowNotificationRepository scheduleTomorrowNotificationRepository) {
        this.messageSender = messageSender;
        this.callRestService = callRestService;
        this.botUserRepository = botUserRepository;
        this.scheduleTomorrowNotificationRepository = scheduleTomorrowNotificationRepository;
    }

    @Scheduled(cron = "${scheduled.schedule-tomorrow-notification-service.cron}")
    public void sendSchedule() throws Exception {
        Calendar calendar = CalendarUtils.getCalendar();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        log.info("Start to send tomorrow full time schedule for hour {}", hourOfDay);
        List<ScheduleTomorrowNotification> scheduleTomorrowNotifications =
                scheduleTomorrowNotificationRepository.findByHourForSendAndEnabled(hourOfDay);

        if(!CollectionUtils.isEmpty(scheduleTomorrowNotifications)) {
            log.info("Send tomorrow full time schedule for hour {}", hourOfDay);
            final String dayNumber = String.valueOf(CalendarUtils.getDayOfWeekStartsFromMonday(calendar)) + 1;
            for(ScheduleTomorrowNotification notification : scheduleTomorrowNotifications) {
                BotUser botUser = botUserRepository.findOneById(notification.getUserId());
                if(BotMessageUtils.isBotUserFullTime(botUser)) {
                    FullTimeLessonDto lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            dayNumber
                    );
                    Map<String, String> botMessage = BotMessageUtils.getBotMessageForFullTimeLessons(lessons, CommandText.TOMORROW);
                    messageSender.send(botMessage, botUser.getUserId());
                }
            }
        }
        else {
            log.info("No need to send tomorrow full time schedule for hour {}", hourOfDay);
        }
        log.info("Finish sending tomorrow full time schedule for hour {}", hourOfDay);
    }
    
}
