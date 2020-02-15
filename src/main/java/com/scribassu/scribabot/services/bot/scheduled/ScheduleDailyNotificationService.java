package com.scribassu.scribabot.services.bot.scheduled;

import com.scribassu.scribabot.text.CommandText;
import com.scribassu.scribabot.entities.BotUser;
import com.scribassu.scribabot.entities.ScheduleDailyNotification;
import com.scribassu.scribabot.repositories.BotUserRepository;
import com.scribassu.scribabot.repositories.ScheduleDailyNotificationRepository;
import com.scribassu.scribabot.services.CallRestService;
import com.scribassu.scribabot.services.messages.MessageSender;
import com.scribassu.scribabot.util.BotMessageUtils;
import com.scribassu.scribabot.util.CalendarUtils;
import com.scribassu.tracto.domain.FullTimeLesson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Service
public class ScheduleDailyNotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleDailyNotificationService.class);

    private final MessageSender messageSender;
    private final CallRestService callRestService;
    private final BotUserRepository botUserRepository;
    private final ScheduleDailyNotificationRepository scheduleDailyNotificationRepository;

    @Autowired
    public ScheduleDailyNotificationService(MessageSender messageSender,
                                            CallRestService callRestService,
                                            BotUserRepository botUserRepository,
                                            ScheduleDailyNotificationRepository scheduleDailyNotificationRepository) {
        this.messageSender = messageSender;
        this.callRestService = callRestService;
        this.botUserRepository = botUserRepository;
        this.scheduleDailyNotificationRepository = scheduleDailyNotificationRepository;
    }

    @Scheduled(cron = "${scheduled.daily-notification-service.cron}")
    public void sendSchedule() throws Exception {
        Calendar calendar = CalendarUtils.getCalendar();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        LOGGER.info("Start to send full time schedule for hour {}", hourOfDay);
        List<ScheduleDailyNotification> scheduleDailyNotifications =
                scheduleDailyNotificationRepository.findByHourForSendAndEnabled(hourOfDay);

        if(!CollectionUtils.isEmpty(scheduleDailyNotifications)) {
            LOGGER.info("Send full time schedule for hour {}", hourOfDay);
            final String dayNumber = String.valueOf(CalendarUtils.getDayOfWeekStartsFromMonday(calendar));
            for(ScheduleDailyNotification notification : scheduleDailyNotifications) {
                BotUser botUser = botUserRepository.findOneById(notification.getUserId());
                if(BotMessageUtils.isBotUserFullTime(botUser)) {
                    List<FullTimeLesson> lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            dayNumber
                    );
                    Map<String, String> botMessage = BotMessageUtils.getBotMessageForFullTimeLessons(lessons, CommandText.TODAY, dayNumber);
                    messageSender.send(botMessage, botUser.getUserId());
                }
            }
        }
        else {
            LOGGER.info("No need to send full time schedule for hour {}", hourOfDay);
        }
        LOGGER.info("Finish sending full time schedule for hour {}", hourOfDay);
    }
}
