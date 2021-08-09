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
public class VkKeyboardGenerator {

    private final ExamPeriodAfterTomorrowNotificationRepository examPeriodAfterTomorrowNotificationRepository;
    private final ExamPeriodTomorrowNotificationRepository examPeriodTomorrowNotificationRepository;
    private final ExamPeriodTodayNotificationRepository examPeriodTodayNotificationRepository;
    private final ScheduleTodayNotificationRepository scheduleTodayNotificationRepository;
    private final ScheduleTomorrowNotificationRepository scheduleTomorrowNotificationRepository;
    private final ExtramuralEventTodayNotificationRepository extramuralEventTodayNotificationRepository;
    private final ExtramuralEventTomorrowNotificationRepository extramuralEventTomorrowNotificationRepository;
    private final ExtramuralEventAfterTomorrowNotificationRepository extramuralEventAfterTomorrowNotificationRepository;

    @Autowired
    public VkKeyboardGenerator(ExamPeriodAfterTomorrowNotificationRepository examPeriodAfterTomorrowNotificationRepository,
                               ExamPeriodTomorrowNotificationRepository examPeriodTomorrowNotificationRepository,
                               ExamPeriodTodayNotificationRepository examPeriodTodayNotificationRepository,
                               ScheduleTodayNotificationRepository scheduleTodayNotificationRepository,
                               ScheduleTomorrowNotificationRepository scheduleTomorrowNotificationRepository,
                               ExtramuralEventTodayNotificationRepository extramuralEventTodayNotificationRepository,
                               ExtramuralEventTomorrowNotificationRepository extramuralEventTomorrowNotificationRepository,
                               ExtramuralEventAfterTomorrowNotificationRepository extramuralEventAfterTomorrowNotificationRepository) {
        this.examPeriodAfterTomorrowNotificationRepository = examPeriodAfterTomorrowNotificationRepository;
        this.examPeriodTomorrowNotificationRepository = examPeriodTomorrowNotificationRepository;
        this.examPeriodTodayNotificationRepository = examPeriodTodayNotificationRepository;
        this.scheduleTodayNotificationRepository = scheduleTodayNotificationRepository;
        this.scheduleTomorrowNotificationRepository = scheduleTomorrowNotificationRepository;
        this.extramuralEventTodayNotificationRepository = extramuralEventTodayNotificationRepository;
        this.extramuralEventTomorrowNotificationRepository = extramuralEventTomorrowNotificationRepository;
        this.extramuralEventAfterTomorrowNotificationRepository = extramuralEventAfterTomorrowNotificationRepository;
    }

    /*
    TODO:
    schedule fulltime
    schedule extramural
    hours

    DONE:
    main menu
    teachers
    settings
    settings notification schedule
    settings notification exams
    education forms
    courses
    group numbers
    departments
    confirm deletion
     */

    public static final VkKeyboard mainMenu = new VkKeyboard(List.of(
            List.of(
                    new VkKeyboardButton(
                    new VkKeyboardButtonActionText(
                            "Расписание студентов",
                            "{\"button\": \"1\"}",
                            VkKeyboardButtonActionType.TEXT
                    ), VkKeyboardButtonColor.POSITIVE)
            ),
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Расписание преподавателей",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.POSITIVE)
            ),
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Настройки",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            ),
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Справка",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            )
    ), false);

    public static final VkKeyboard confirmDeletion = new VkKeyboard(List.of(
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Да",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.POSITIVE),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Нет",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.NEGATIVE)
            )
    ), false);

    public static final VkKeyboard educationForms = new VkKeyboard(List.of(
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Дневная",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Заочная",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            ),
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Главное меню",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            )
    ), false);

    public static final VkKeyboard courses = new VkKeyboard(List.of(
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "1 курс",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "2 курс",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            ),
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "3 курс",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "4 курс",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            ),
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "5 курс",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Другое",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            ),
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Главное меню",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            )
    ), false);


    public static final VkKeyboard departments = new VkKeyboard(List.of(
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "БИОЛОГ",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "ГЕОГРАФ",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "ГЕОЛОГ",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            ),
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "ИДПО",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "И-Т ИСКУССТВ",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "ИИИМО",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            ),
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "И-Т ФИЗ КУЛ",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "ИФИЖ",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "И-Т ХИМИИ",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            ),
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "МЕХМАТ",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "СОЦФАК",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "ФИЯИЛ",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            ),
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "КНИИТ",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "ФНБМТ",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "ФНП",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            ),
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "ПСИХОЛОГ",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "ППИСО",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "ФИЗФАК",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            ),
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "ФИЛОСОФ",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "ЭКОНОМ",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "ЮРФАК",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            ),
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "ГЕОЛОГ К-Ж",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "К-Ж ЯБЛОЧКОВА",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            ),
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Главное меню",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            )
    ), false);

    public VkKeyboard teachers(List<Teacher> teachers) {
        List<List<VkKeyboardButton>> vkKeyboardButtons = new ArrayList<>();

        int i = 0;
        int row = 0;
        vkKeyboardButtons.add(new ArrayList<>());

        while (i < teachers.size()) {
            if (i % 5 == 4) {
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

        return new VkKeyboard(vkKeyboardButtons, false);
    }

    public VkKeyboard groupNumbers(List<String> groupNumbers) {
        List<List<VkKeyboardButton>> vkKeyboardButtons = new ArrayList<>();

        int i = 0;
        int row = 0;
        vkKeyboardButtons.add(new ArrayList<>());
        int mod = groupNumbers.get(0).length() > 5 ? 3 : 5; //to make long numbers visible
        int mmod = mod - 1;

        while (i < groupNumbers.size()) {
            if (i % mod == mmod) {
                row++;
                vkKeyboardButtons.add(new ArrayList<>());
            }
            vkKeyboardButtons.get(row).add(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    groupNumbers.get(i),
                                    String.format(Constants.PAYLOAD, CommandText.CHOOSE_STUDENT_GROUP),
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

    public VkKeyboard settings(BotUser user) {
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

        if (user.isFilterNomDenom()) {
            vkKeyboardButtons.get(2).add(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Выкл. фильтр по типу недели",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.NEGATIVE
                    )
            );
        } else {
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

        if (!BotMessageUtils.isBotUserExtramural(user)) {
            vkKeyboardButtons.get(0).add(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Рассылка расписания занятий",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY
                    )
            );
        } else {
            vkKeyboardButtons.remove(0);
        }

        return new VkKeyboard(vkKeyboardButtons, false);
    }

    public VkKeyboard settingsExamNotification(BotUser botUser) {
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

        vkKeyboardButtons.get(0).add(
                new VkKeyboardButton(
                        new VkKeyboardButtonActionText(
                                "Уст. время рассылки сессии на сегодня",
                                "{\"button\": \"1\"}",
                                VkKeyboardButtonActionType.TEXT
                        ), VkKeyboardButtonColor.PRIMARY
                )
        );

        vkKeyboardButtons.get(2).add(
                new VkKeyboardButton(
                        new VkKeyboardButtonActionText(
                                "Уст. время рассылки сессии на завтра",
                                "{\"button\": \"1\"}",
                                VkKeyboardButtonActionType.TEXT
                        ), VkKeyboardButtonColor.PRIMARY
                )
        );

        vkKeyboardButtons.get(4).add(
                new VkKeyboardButton(
                        new VkKeyboardButtonActionText(
                                "Уст. время рас-ки сессии на послезавтра",
                                "{\"button\": \"1\"}",
                                VkKeyboardButtonActionType.TEXT
                        ), VkKeyboardButtonColor.PRIMARY
                )
        );

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

        if (BotMessageUtils.isBotUserFullTime(botUser)) {
            ExamPeriodTodayNotification examPeriodTodayNotification = examPeriodTodayNotificationRepository.findByUserId(userId);
            ExamPeriodTomorrowNotification examPeriodTomorrowNotification = examPeriodTomorrowNotificationRepository.findByUserId(userId);
            ExamPeriodAfterTomorrowNotification examPeriodAfterTomorrowNotification = examPeriodAfterTomorrowNotificationRepository.findByUserId(userId);

            if (null != examPeriodTodayNotification && examPeriodTodayNotification.isEnabled()) {
                vkKeyboardButtons.get(1).add(
                        new VkKeyboardButton(
                                new VkKeyboardButtonActionText(
                                        "Выкл. рассылку сессии на сегодня",
                                        "{\"button\": \"1\"}",
                                        VkKeyboardButtonActionType.TEXT
                                ), VkKeyboardButtonColor.NEGATIVE
                        )
                );
            } else {
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

            if (null != examPeriodTomorrowNotification && examPeriodTomorrowNotification.isEnabled()) {
                vkKeyboardButtons.get(3).add(
                        new VkKeyboardButton(
                                new VkKeyboardButtonActionText(
                                        "Выкл. рассылку сессии на завтра",
                                        "{\"button\": \"1\"}",
                                        VkKeyboardButtonActionType.TEXT
                                ), VkKeyboardButtonColor.NEGATIVE
                        )
                );
            } else {
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

            if (null != examPeriodAfterTomorrowNotification && examPeriodAfterTomorrowNotification.isEnabled()) {
                vkKeyboardButtons.get(5).add(
                        new VkKeyboardButton(
                                new VkKeyboardButtonActionText(
                                        "Выкл. рассылку сессии на послезавтра",
                                        "{\"button\": \"1\"}",
                                        VkKeyboardButtonActionType.TEXT
                                ), VkKeyboardButtonColor.NEGATIVE
                        )
                );
            } else {
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
        }

        if (BotMessageUtils.isBotUserExtramural(botUser)) {
            ExtramuralEventTodayNotification extramuralEventTodayNotification = extramuralEventTodayNotificationRepository.findByUserId(userId);
            ExtramuralEventTomorrowNotification extramuralEventTomorrowNotification = extramuralEventTomorrowNotificationRepository.findByUserId(userId);
            ExtramuralEventAfterTomorrowNotification extramuralEventAfterTomorrowNotification = extramuralEventAfterTomorrowNotificationRepository.findByUserId(userId);

            if (null != extramuralEventTodayNotification && extramuralEventTodayNotification.isEnabled()) {
                vkKeyboardButtons.get(1).add(
                        new VkKeyboardButton(
                                new VkKeyboardButtonActionText(
                                        "Выкл. рассылку сессии на сегодня",
                                        "{\"button\": \"1\"}",
                                        VkKeyboardButtonActionType.TEXT
                                ), VkKeyboardButtonColor.NEGATIVE
                        )
                );
            } else {
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

            if (null != extramuralEventTomorrowNotification && extramuralEventTomorrowNotification.isEnabled()) {
                vkKeyboardButtons.get(3).add(
                        new VkKeyboardButton(
                                new VkKeyboardButtonActionText(
                                        "Выкл. рассылку сессии на завтра",
                                        "{\"button\": \"1\"}",
                                        VkKeyboardButtonActionType.TEXT
                                ), VkKeyboardButtonColor.NEGATIVE
                        )
                );
            } else {
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

            if (null != extramuralEventAfterTomorrowNotification && extramuralEventAfterTomorrowNotification.isEnabled()) {
                vkKeyboardButtons.get(5).add(
                        new VkKeyboardButton(
                                new VkKeyboardButtonActionText(
                                        "Выкл. рассылку сессии на послезавтра",
                                        "{\"button\": \"1\"}",
                                        VkKeyboardButtonActionType.TEXT
                                ), VkKeyboardButtonColor.NEGATIVE
                        )
                );
            } else {
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
        }

        return new VkKeyboard(vkKeyboardButtons, false);
    }

    public VkKeyboard settingsScheduleNotification(BotUser botUser) {
        List<List<VkKeyboardButton>> vkKeyboardButtons = new ArrayList<>();
        vkKeyboardButtons.add(new ArrayList<>());
        vkKeyboardButtons.add(new ArrayList<>());
        vkKeyboardButtons.add(new ArrayList<>());
        vkKeyboardButtons.add(new ArrayList<>());
        vkKeyboardButtons.add(new ArrayList<>());
        vkKeyboardButtons.add(new ArrayList<>());

        String userId = botUser.getUserId();

        ScheduleTodayNotification scheduleTodayNotification = scheduleTodayNotificationRepository.findByUserId(userId);
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

        if (null != scheduleTodayNotification && scheduleTodayNotification.isEnabled()) {
            vkKeyboardButtons.get(1).add(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Выкл. рассылку расп-я на сегодня",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.NEGATIVE
                    )
            );
        } else {
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

        if (null != scheduleTomorrowNotification && scheduleTomorrowNotification.isEnabled()) {
            vkKeyboardButtons.get(3).add(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Выкл. рассылку сессии на завтра",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.NEGATIVE
                    )
            );
        } else {
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
