package com.scribassu.scribabot.services.bot;

import com.scribassu.scribabot.entities.BotUser;
import com.scribassu.scribabot.entities.ScheduleDailyNotification;
import com.scribassu.scribabot.repositories.BotUserRepository;
import com.scribassu.scribabot.repositories.ScheduleDailyNotificationRepository;
import com.scribassu.scribabot.services.CallRestService;
import com.scribassu.scribabot.services.messages.MessageSender;
import com.scribassu.scribabot.util.BotMessageUtils;
import com.scribassu.tracto.domain.EducationForm;
import com.scribassu.tracto.domain.FullTimeLesson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScheduleDailyNotificationService {

    /*
    Every sendSchedule() iteration increments value by 1.
    Current hour for sending schedules.
     */
    @Value("${scheduled.daily-notification-service.hour}")
    private int hour;

    private final MessageSender messageSender;
    private final CallRestService callRestService;
    private final BotUserRepository botUserRepository;
    private final ScheduleDailyNotificationRepository scheduleDailyNotificationRepository;
    private static final Calendar calendar = Calendar.getInstance();

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
        List<ScheduleDailyNotification> scheduleDailyNotifications =
                scheduleDailyNotificationRepository.findByHourForSendAndEnabled(hour);

        if(!CollectionUtils.isEmpty(scheduleDailyNotifications)) {
            for(ScheduleDailyNotification notification : scheduleDailyNotifications) {
                BotUser botUser = botUserRepository.findOneById(notification.getUserId());
                if(botUser != null && botUser.getEducationForm().equalsIgnoreCase(EducationForm.DO.getGroupType())) {
                    List<FullTimeLesson> lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            String.valueOf(calendar.get(Calendar.DAY_OF_WEEK))
                    );
                    Map<String, String> botMessage = BotMessageUtils.getBotMessageForFullTimeLessons(lessons);
                    messageSender.send(botMessage, botUser.getUserId());
                }
            }
        }

        hour = hour == 24 ? 1 : hour + 1;
    }
}
