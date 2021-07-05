package com.scribassu.scribabot.keyboard;

import com.scribassu.scribabot.dto.vkkeyboard.*;
import com.scribassu.scribabot.entities.*;
import com.scribassu.scribabot.repositories.*;
import com.scribassu.scribabot.text.CommandText;
import com.scribassu.scribabot.util.BotMessageUtils;
import com.scribassu.scribabot.util.Constants;
import com.scribassu.tracto.domain.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class KeyboardGenerator {

    private final ExamPeriodAfterTomorrowNotificationRepository examPeriodAfterTomorrowNotificationRepository;
    private final ExamPeriodTomorrowNotificationRepository examPeriodTomorrowNotificationRepository;
    private final ExamPeriodDailyNotificationRepository examPeriodDailyNotificationRepository;
    private final ScheduleDailyNotificationRepository scheduleDailyNotificationRepository;
    private final ScheduleTomorrowNotificationRepository scheduleTomorrowNotificationRepository;

    @Autowired
    public KeyboardGenerator(ExamPeriodAfterTomorrowNotificationRepository examPeriodAfterTomorrowNotificationRepository,
                             ExamPeriodTomorrowNotificationRepository examPeriodTomorrowNotificationRepository,
                             ExamPeriodDailyNotificationRepository examPeriodDailyNotificationRepository,
                             ScheduleDailyNotificationRepository scheduleDailyNotificationRepository,
                             ScheduleTomorrowNotificationRepository scheduleTomorrowNotificationRepository,
                             BotUserRepository botUserRepository) {
        this.examPeriodAfterTomorrowNotificationRepository = examPeriodAfterTomorrowNotificationRepository;
        this.examPeriodTomorrowNotificationRepository = examPeriodTomorrowNotificationRepository;
        this.examPeriodDailyNotificationRepository = examPeriodDailyNotificationRepository;
        this.scheduleDailyNotificationRepository = scheduleDailyNotificationRepository;
        this.scheduleTomorrowNotificationRepository = scheduleTomorrowNotificationRepository;
    }

    public VkKeyboard buildTeachers(List<Teacher> teachers) {
        List<List<VkKeyboardButton>> vkKeyboardButtons = new ArrayList<>();

        int i = 0;
        int row = 0;
        vkKeyboardButtons.add(new ArrayList<>());

        while(i < teachers.size()) {
            if(i % 5 == 4) {
                row++;
                vkKeyboardButtons.add(new ArrayList<>());
            }
            vkKeyboardButtons.get(row).add(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    teachers.get(i).getSurname() + " " + teachers.get(i).getName() + " " + teachers.get(i).getPatronymic(),
                                    String.format(Constants.PAYLOAD, CommandText.TEACHER_ID_PAYLOAD + " " + teachers.get(i).getId()),
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            );
            i++;
        }

        vkKeyboardButtons.add(new ArrayList<>());
        vkKeyboardButtons.get(vkKeyboardButtons.size() - 1).add(
                new VkKeyboardButton(
                        new VkKeyboardButtonActionText(
                                "Главное меню",
                                "",
                                VkKeyboardButtonActionType.TEXT
                        ), VkKeyboardButtonColor.POSITIVE)
        );

        return new VkKeyboard(vkKeyboardButtons, true);
    }

    public VkKeyboard buildSettings(BotUser user) {
        List<List<VkKeyboardButton>> vkKeyboardButtons = new ArrayList<>();
        vkKeyboardButtons.add(new ArrayList<>());
        vkKeyboardButtons.add(new ArrayList<>());
        vkKeyboardButtons.add(new ArrayList<>());
        vkKeyboardButtons.add(new ArrayList<>());
        vkKeyboardButtons.add(new ArrayList<>());
        vkKeyboardButtons.add(new ArrayList<>());

        // 0 row is checked in the end

        vkKeyboardButtons.get(1).add(
                new VkKeyboardButton(
                        new VkKeyboardButtonActionText(
                                "Рассылка расписания сессии",
                                "{\"button\": \"1\"}",
                                VkKeyboardButtonActionType.TEXT
                        ), VkKeyboardButtonColor.PRIMARY
                )
        );

        if(user.isFilterNomDenom()) {
            vkKeyboardButtons.get(2).add(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Выкл. фильтр по типу недели",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.NEGATIVE
                    )
            );
        }
        else {
            vkKeyboardButtons.get(2).add(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Вкл. фильтр по типу недели",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.POSITIVE
                    )
            );
        }

        vkKeyboardButtons.get(3).add(
                new VkKeyboardButton(
                        new VkKeyboardButtonActionText(
                                "Выбрать факультет и группу",
                                "{\"button\": \"1\"}",
                                VkKeyboardButtonActionType.TEXT
                        ), VkKeyboardButtonColor.POSITIVE
                )
        );

        vkKeyboardButtons.get(3).add(
                new VkKeyboardButton(
                        new VkKeyboardButtonActionText(
                                "Текущие настройки пользователя",
                                "{\"button\": \"1\"}",
                                VkKeyboardButtonActionType.TEXT
                        ), VkKeyboardButtonColor.PRIMARY
                )
        );

        vkKeyboardButtons.get(4).add(
                new VkKeyboardButton(
                        new VkKeyboardButtonActionText(
                                "Удалить профиль",
                                "{\"button\": \"1\"}",
                                VkKeyboardButtonActionType.TEXT
                        ), VkKeyboardButtonColor.NEGATIVE
                )
        );

        vkKeyboardButtons.get(5).add(
                new VkKeyboardButton(
                        new VkKeyboardButtonActionText(
                                "Главное меню",
                                "{\"button\": \"1\"}",
                                VkKeyboardButtonActionType.TEXT
                        ), VkKeyboardButtonColor.PRIMARY
                )
        );

        if(!BotMessageUtils.isBotUserExtramural(user)) {
            vkKeyboardButtons.get(0).add(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Рассылка расписания занятий",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY
                    )
            );
        }
        else {
            vkKeyboardButtons.remove(0);
        }

        return new VkKeyboard(vkKeyboardButtons, false);
    }

    public VkKeyboard buildSettingsExamNotification(BotUser botUser) {
        List<List<VkKeyboardButton>> vkKeyboardButtons = new ArrayList<>();
        vkKeyboardButtons.add(new ArrayList<>());
        vkKeyboardButtons.add(new ArrayList<>());
        vkKeyboardButtons.add(new ArrayList<>());
        vkKeyboardButtons.add(new ArrayList<>());
        vkKeyboardButtons.add(new ArrayList<>());
        vkKeyboardButtons.add(new ArrayList<>());
        vkKeyboardButtons.add(new ArrayList<>());
        vkKeyboardButtons.add(new ArrayList<>());

        String userId = botUser.getUserId();

        ExamPeriodDailyNotification examPeriodDailyNotification = examPeriodDailyNotificationRepository.findByUserId(userId);
        ExamPeriodTomorrowNotification examPeriodTomorrowNotification = examPeriodTomorrowNotificationRepository.findByUserId(userId);
        ExamPeriodAfterTomorrowNotification examPeriodAfterTomorrowNotification = examPeriodAfterTomorrowNotificationRepository.findByUserId(userId);

        vkKeyboardButtons.get(0).add(
                new VkKeyboardButton(
                        new VkKeyboardButtonActionText(
                                "Уст. время рассылки сессии на сегодня",
                                "{\"button\": \"1\"}",
                                VkKeyboardButtonActionType.TEXT
                        ), VkKeyboardButtonColor.PRIMARY
                )
        );

        if(null != examPeriodDailyNotification && examPeriodDailyNotification.isEnabled()) {
            vkKeyboardButtons.get(1).add(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Выкл. рассылку сессии на сегодня",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.NEGATIVE
                    )
            );
        }
        else {
            vkKeyboardButtons.get(1).add(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Вкл. рассылку сессии на сегодня",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.POSITIVE
                    )
            );
        }

        vkKeyboardButtons.get(2).add(
                new VkKeyboardButton(
                        new VkKeyboardButtonActionText(
                                "Уст. время рассылки сессии на завтра",
                                "{\"button\": \"1\"}",
                                VkKeyboardButtonActionType.TEXT
                        ), VkKeyboardButtonColor.PRIMARY
                )
        );

        if(null != examPeriodTomorrowNotification && examPeriodTomorrowNotification.isEnabled()) {
            vkKeyboardButtons.get(3).add(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Выкл. рассылку сессии на завтра",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.NEGATIVE
                    )
            );
        }
        else {
            vkKeyboardButtons.get(3).add(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Вкл. рассылку сессии на завтра",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.POSITIVE
                    )
            );
        }

        vkKeyboardButtons.get(4).add(
                new VkKeyboardButton(
                        new VkKeyboardButtonActionText(
                                "Уст. время рас-ки сессии на послезавтра",
                                "{\"button\": \"1\"}",
                                VkKeyboardButtonActionType.TEXT
                        ), VkKeyboardButtonColor.PRIMARY
                )
        );

        if(null != examPeriodAfterTomorrowNotification && examPeriodAfterTomorrowNotification.isEnabled()) {
            vkKeyboardButtons.get(5).add(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Выкл. рассылку сессии на послезавтра",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.NEGATIVE
                    )
            );
        }
        else {
            vkKeyboardButtons.get(5).add(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Вкл. рассылку сессии на послезавтра",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.POSITIVE
                    )
            );
        }

        vkKeyboardButtons.get(6).add(
                new VkKeyboardButton(
                        new VkKeyboardButtonActionText(
                                "Настройки",
                                "{\"button\": \"1\"}",
                                VkKeyboardButtonActionType.TEXT
                        ), VkKeyboardButtonColor.PRIMARY
                )
        );

        vkKeyboardButtons.get(7).add(
                new VkKeyboardButton(
                        new VkKeyboardButtonActionText(
                                "Главное меню",
                                "{\"button\": \"1\"}",
                                VkKeyboardButtonActionType.TEXT
                        ), VkKeyboardButtonColor.PRIMARY
                )
        );

        return new VkKeyboard(vkKeyboardButtons, false);
    }

    public VkKeyboard buildSettingsScheduleNotification(BotUser botUser) {
        List<List<VkKeyboardButton>> vkKeyboardButtons = new ArrayList<>();
        vkKeyboardButtons.add(new ArrayList<>());
        vkKeyboardButtons.add(new ArrayList<>());
        vkKeyboardButtons.add(new ArrayList<>());
        vkKeyboardButtons.add(new ArrayList<>());
        vkKeyboardButtons.add(new ArrayList<>());
        vkKeyboardButtons.add(new ArrayList<>());

        String userId = botUser.getUserId();

        ScheduleDailyNotification scheduleDailyNotification = scheduleDailyNotificationRepository.findByUserId(userId);
        ScheduleTomorrowNotification scheduleTomorrowNotification = scheduleTomorrowNotificationRepository.findByUserId(userId);

        vkKeyboardButtons.get(0).add(
                new VkKeyboardButton(
                        new VkKeyboardButtonActionText(
                                "Уст. время рассылки расп-я на сегодня",
                                "{\"button\": \"1\"}",
                                VkKeyboardButtonActionType.TEXT
                        ), VkKeyboardButtonColor.PRIMARY
                )
        );

        if(null != scheduleDailyNotification && scheduleDailyNotification.isEnabled()) {
            vkKeyboardButtons.get(1).add(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Выкл. рассылку расп-я на сегодня",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.NEGATIVE
                    )
            );
        }
        else {
            vkKeyboardButtons.get(1).add(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Вкл. рассылку расп-я на сегодня",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.POSITIVE
                    )
            );
        }

        vkKeyboardButtons.get(2).add(
                new VkKeyboardButton(
                        new VkKeyboardButtonActionText(
                                "Уст. время рассылки расп-я на завтра",
                                "{\"button\": \"1\"}",
                                VkKeyboardButtonActionType.TEXT
                        ), VkKeyboardButtonColor.PRIMARY
                )
        );

        if(null != scheduleTomorrowNotification && scheduleTomorrowNotification.isEnabled()) {
            vkKeyboardButtons.get(3).add(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Выкл. рассылку сессии на завтра",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.NEGATIVE
                    )
            );
        }
        else {
            vkKeyboardButtons.get(3).add(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Вкл. рассылку расп-я на завтра",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.POSITIVE
                    )
            );
        }

        vkKeyboardButtons.get(4).add(
                new VkKeyboardButton(
                        new VkKeyboardButtonActionText(
                                "Настройки",
                                "{\"button\": \"1\"}",
                                VkKeyboardButtonActionType.TEXT
                        ), VkKeyboardButtonColor.PRIMARY
                )
        );

        vkKeyboardButtons.get(5).add(
                new VkKeyboardButton(
                        new VkKeyboardButtonActionText(
                                "Главное меню",
                                "{\"button\": \"1\"}",
                                VkKeyboardButtonActionType.TEXT
                        ), VkKeyboardButtonColor.PRIMARY
                )
        );

        return new VkKeyboard(vkKeyboardButtons, false);
    }
}
