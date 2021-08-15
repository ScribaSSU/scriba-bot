package com.scribassu.scribabot.keyboard;

import com.scribassu.scribabot.dto.InnerBotUser;
import com.scribassu.scribabot.dto.vkkeyboard.*;
import com.scribassu.scribabot.entities.*;
import com.scribassu.scribabot.repositories.*;
import com.scribassu.scribabot.text.CommandText;
import com.scribassu.scribabot.util.BotMessageUtils;
import com.scribassu.scribabot.util.BotUserSource;
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

    private static final VkKeyboardButton MAIN_MENU_BUTTON = new VkKeyboardButton(
            new VkKeyboardButtonActionText(
                    "Главное меню","{\"button\": \"1\"}", VkKeyboardButtonActionType.TEXT
            ), VkKeyboardButtonColor.PRIMARY);

    private static final VkKeyboardButton SETTINGS_BUTTON = new VkKeyboardButton(
            new VkKeyboardButtonActionText(
                    "Настройки", "{\"button\": \"1\"}", VkKeyboardButtonActionType.TEXT
            ), VkKeyboardButtonColor.PRIMARY);

    private static final VkKeyboardButton CHOOSE_DEPARTMENT_AND_GROUP_BUTTON = new VkKeyboardButton(
            new VkKeyboardButtonActionText(
                    "Выбрать факультет и группу", "{\"button\": \"1\"}", VkKeyboardButtonActionType.TEXT
            ), VkKeyboardButtonColor.POSITIVE);

    private static final VkKeyboardButton TODAY_SCHEDULE_BUTTON = new VkKeyboardButton(
            new VkKeyboardButtonActionText(
                    "Сегодня", "{\"button\": \"1\"}", VkKeyboardButtonActionType.TEXT
            ), VkKeyboardButtonColor.PRIMARY);

    private static final VkKeyboardButton TOMORROW_SCHEDULE_BUTTON = new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
            "Завтра", "{\"button\": \"1\"}", VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY);

    private static final VkKeyboardButton YESTERDAY_SCHEDULE_BUTTON = new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
            "Вчера","{\"button\": \"1\"}", VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY);

    /*
    TODO:
    UPDATE TEACHER KEYBOARD AS TG


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
    hours
    schedule fulltime
    schedule extramural
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
            List.of(SETTINGS_BUTTON),
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
                            ), VkKeyboardButtonColor.POSITIVE)
            ),
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Нет",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.NEGATIVE)
            ),
            List.of(SETTINGS_BUTTON),
            List.of(MAIN_MENU_BUTTON)
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
            List.of(MAIN_MENU_BUTTON)
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
            List.of(MAIN_MENU_BUTTON)
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
            List.of(MAIN_MENU_BUTTON)
    ), false);

    public static final VkKeyboard hours = new VkKeyboard(List.of(
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "1 ч",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "2 ч",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "3 ч",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "4 ч",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            ),
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "5 ч",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "6 ч",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "7 ч",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "8 ч",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            ),
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "9 ч",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "10 ч",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "11 ч",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "12 ч",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            ),
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "13 ч",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "14 ч",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "15 ч",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "16 ч",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            ),
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "17 ч",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "18 ч",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "19 ч",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "20 ч",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            ),
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "21 ч",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "22 ч",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "23 ч",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "0 ч",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            ),
            List.of(MAIN_MENU_BUTTON)
    ), false);

    public static final VkKeyboard fullTimeSchedule = new VkKeyboard(List.of(
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Все занятия",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            ),
            List.of(TODAY_SCHEDULE_BUTTON, TOMORROW_SCHEDULE_BUTTON, YESTERDAY_SCHEDULE_BUTTON),
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Пн",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Вт",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Ср",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            ),
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Чт",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Пт",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Сб",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            ),
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Сессия",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            ),
            List.of(CHOOSE_DEPARTMENT_AND_GROUP_BUTTON),
            List.of(MAIN_MENU_BUTTON)
    ), false);

    public static final VkKeyboard extramuralSchedule = new VkKeyboard(List.of(
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Все занятия",
                                    "{\"button\": \"1\"}",
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            ),
            List.of(TODAY_SCHEDULE_BUTTON, TOMORROW_SCHEDULE_BUTTON, YESTERDAY_SCHEDULE_BUTTON),
            List.of(CHOOSE_DEPARTMENT_AND_GROUP_BUTTON),
            List.of(MAIN_MENU_BUTTON)
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
        vkKeyboardButtons.get(vkKeyboardButtons.size() - 1).add(MAIN_MENU_BUTTON);

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
        vkKeyboardButtons.get(vkKeyboardButtons.size() - 1).add(MAIN_MENU_BUTTON);

        return new VkKeyboard(vkKeyboardButtons, true);
    }

    public VkKeyboard settings(InnerBotUser user) {
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

        vkKeyboardButtons.get(3).add(CHOOSE_DEPARTMENT_AND_GROUP_BUTTON);

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

        vkKeyboardButtons.get(5).add(MAIN_MENU_BUTTON);

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

    public VkKeyboard settingsExamNotification(InnerBotUser botUser) {
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
        BotUserSource source = botUser.getSource();

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

        vkKeyboardButtons.get(6).add(SETTINGS_BUTTON);
        vkKeyboardButtons.get(7).add(MAIN_MENU_BUTTON);

        if (BotMessageUtils.isBotUserFullTime(botUser)) {
            ExamPeriodTodayNotification examPeriodTodayNotification = examPeriodTodayNotificationRepository.findByUserIdAndUserSource(userId, source);
            ExamPeriodTomorrowNotification examPeriodTomorrowNotification = examPeriodTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);
            ExamPeriodAfterTomorrowNotification examPeriodAfterTomorrowNotification = examPeriodAfterTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);

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
            ExtramuralEventTodayNotification extramuralEventTodayNotification = extramuralEventTodayNotificationRepository.findByUserIdAndUserSource(userId, source);
            ExtramuralEventTomorrowNotification extramuralEventTomorrowNotification = extramuralEventTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);
            ExtramuralEventAfterTomorrowNotification extramuralEventAfterTomorrowNotification = extramuralEventAfterTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);

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

    public VkKeyboard settingsScheduleNotification(InnerBotUser botUser) {
        List<List<VkKeyboardButton>> vkKeyboardButtons = new ArrayList<>();
        vkKeyboardButtons.add(new ArrayList<>());
        vkKeyboardButtons.add(new ArrayList<>());
        vkKeyboardButtons.add(new ArrayList<>());
        vkKeyboardButtons.add(new ArrayList<>());
        vkKeyboardButtons.add(new ArrayList<>());
        vkKeyboardButtons.add(new ArrayList<>());

        String userId = botUser.getUserId();
        BotUserSource source = botUser.getSource();

        ScheduleTodayNotification scheduleTodayNotification = scheduleTodayNotificationRepository.findByUserIdAndUserSource(userId, source);
        ScheduleTomorrowNotification scheduleTomorrowNotification = scheduleTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);

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
                                    "Выкл. рассылку расп-я на завтра",
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

        vkKeyboardButtons.get(4).add(SETTINGS_BUTTON);
        vkKeyboardButtons.get(5).add(MAIN_MENU_BUTTON);

        return new VkKeyboard(vkKeyboardButtons, false);
    }
}
