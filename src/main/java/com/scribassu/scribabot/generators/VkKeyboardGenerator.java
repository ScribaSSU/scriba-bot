package com.scribassu.scribabot.generators;

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

import static com.scribassu.scribabot.util.Constants.DEFAULT_PAYLOAD;
import static com.scribassu.scribabot.util.Constants.MAX_KEYBOARD_TEXT_LENGTH;

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
                    "Главное меню",DEFAULT_PAYLOAD, VkKeyboardButtonActionType.TEXT
            ), VkKeyboardButtonColor.PRIMARY);

    private static final VkKeyboardButton SETTINGS_BUTTON = new VkKeyboardButton(
            new VkKeyboardButtonActionText(
                    "Настройки", DEFAULT_PAYLOAD, VkKeyboardButtonActionType.TEXT
            ), VkKeyboardButtonColor.PRIMARY);

    private static final VkKeyboardButton CHOOSE_DEPARTMENT_AND_GROUP_BUTTON = new VkKeyboardButton(
            new VkKeyboardButtonActionText(
                    "Выбрать факультет и группу", DEFAULT_PAYLOAD, VkKeyboardButtonActionType.TEXT
            ), VkKeyboardButtonColor.POSITIVE);

    private static final VkKeyboardButton TODAY_SCHEDULE_BUTTON = new VkKeyboardButton(
            new VkKeyboardButtonActionText(
                    "Сегодня", DEFAULT_PAYLOAD, VkKeyboardButtonActionType.TEXT
            ), VkKeyboardButtonColor.PRIMARY);

    private static final VkKeyboardButton TOMORROW_SCHEDULE_BUTTON = new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
            "Завтра", DEFAULT_PAYLOAD, VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY);

    private static final VkKeyboardButton YESTERDAY_SCHEDULE_BUTTON = new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
            "Вчера",DEFAULT_PAYLOAD, VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY);

    /*
    TODO:

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
                            DEFAULT_PAYLOAD,
                            VkKeyboardButtonActionType.TEXT
                    ), VkKeyboardButtonColor.POSITIVE)
            ),
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Расписание преподавателей",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.POSITIVE)
            ),
            List.of(SETTINGS_BUTTON),
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Справка",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            )
    ), false);

    public static final VkKeyboard confirmDeletion = new VkKeyboard(List.of(
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Да",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.POSITIVE)
            ),
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Нет",
                                    DEFAULT_PAYLOAD,
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
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Заочная",
                                    DEFAULT_PAYLOAD,
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
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "2 курс",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            ),
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "3 курс",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "4 курс",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            ),
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "5 курс",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Другое",
                                    DEFAULT_PAYLOAD,
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
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "ГЕОГРАФ",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "ГЕОЛОГ",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            ),
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "ИДПО",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "И-Т ИСКУССТВ",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "ИИИМО",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            ),
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "И-Т ФИЗИКИ",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "И-Т ФИЗ КУЛ",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "ИФИЖ",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            ),
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "И-Т ХИМИИ",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "МЕХМАТ",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "СОЦФАК",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            ),
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "ФИЯИЛ",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "КНИИТ",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "ФНБМТ",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            ),
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "ФНП",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "ПСИХОЛОГ",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "ППИСО",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            ),
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "ФИЛОСОФ",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "ЭКОНОМ",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "ЮРФАК",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            ),
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "ГЕОЛОГ К-Ж",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "К-Ж ЯБЛОЧКОВА",
                                    DEFAULT_PAYLOAD,
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
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "2 ч",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "3 ч",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "4 ч",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            ),
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "5 ч",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "6 ч",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "7 ч",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "8 ч",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            ),
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "9 ч",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "10 ч",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "11 ч",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "12 ч",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            ),
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "13 ч",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "14 ч",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "15 ч",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "16 ч",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            ),
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "17 ч",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "18 ч",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "19 ч",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "20 ч",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            ),
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "21 ч",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "22 ч",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "23 ч",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "0 ч",
                                    DEFAULT_PAYLOAD,
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
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            ),
            List.of(TODAY_SCHEDULE_BUTTON, TOMORROW_SCHEDULE_BUTTON, YESTERDAY_SCHEDULE_BUTTON),
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Пн",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Вт",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Ср",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            ),
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Чт",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Пт",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY),
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Сб",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            ),
            List.of(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Сессия",
                                    DEFAULT_PAYLOAD,
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
                                    DEFAULT_PAYLOAD,
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
            String teacher = teachers.get(i).getId() + " "
                    + teachers.get(i).getSurname() + " "
                    + teachers.get(i).getName() + " "
                    + teachers.get(i).getPatronymic();
            teacher = teacher.length() > MAX_KEYBOARD_TEXT_LENGTH ? teacher.substring(0, MAX_KEYBOARD_TEXT_LENGTH) : teacher;
            vkKeyboardButtons.get(row).add(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(teacher,
                                    DEFAULT_PAYLOAD,
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
                                DEFAULT_PAYLOAD,
                                VkKeyboardButtonActionType.TEXT
                        ), VkKeyboardButtonColor.PRIMARY
                )
        );

        if (user.isFilterNomDenom()) {
            vkKeyboardButtons.get(2).add(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Выкл. фильтр по типу недели",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.NEGATIVE
                    )
            );
        } else {
            vkKeyboardButtons.get(2).add(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Вкл. фильтр по типу недели",
                                    DEFAULT_PAYLOAD,
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
                                DEFAULT_PAYLOAD,
                                VkKeyboardButtonActionType.TEXT
                        ), VkKeyboardButtonColor.PRIMARY
                )
        );

        vkKeyboardButtons.get(4).add(
                new VkKeyboardButton(
                        new VkKeyboardButtonActionText(
                                "Удалить профиль",
                                DEFAULT_PAYLOAD,
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
                                    DEFAULT_PAYLOAD,
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
                                DEFAULT_PAYLOAD,
                                VkKeyboardButtonActionType.TEXT
                        ), VkKeyboardButtonColor.PRIMARY
                )
        );

        vkKeyboardButtons.get(2).add(
                new VkKeyboardButton(
                        new VkKeyboardButtonActionText(
                                "Уст. время рассылки сессии на завтра",
                                DEFAULT_PAYLOAD,
                                VkKeyboardButtonActionType.TEXT
                        ), VkKeyboardButtonColor.PRIMARY
                )
        );

        vkKeyboardButtons.get(4).add(
                new VkKeyboardButton(
                        new VkKeyboardButtonActionText(
                                "Уст. время рас-ки сессии на послезавтра",
                                DEFAULT_PAYLOAD,
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
                                        DEFAULT_PAYLOAD,
                                        VkKeyboardButtonActionType.TEXT
                                ), VkKeyboardButtonColor.NEGATIVE
                        )
                );
            } else {
                vkKeyboardButtons.get(1).add(
                        new VkKeyboardButton(
                                new VkKeyboardButtonActionText(
                                        "Вкл. рассылку сессии на сегодня",
                                        DEFAULT_PAYLOAD,
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
                                        DEFAULT_PAYLOAD,
                                        VkKeyboardButtonActionType.TEXT
                                ), VkKeyboardButtonColor.NEGATIVE
                        )
                );
            } else {
                vkKeyboardButtons.get(3).add(
                        new VkKeyboardButton(
                                new VkKeyboardButtonActionText(
                                        "Вкл. рассылку сессии на завтра",
                                        DEFAULT_PAYLOAD,
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
                                        DEFAULT_PAYLOAD,
                                        VkKeyboardButtonActionType.TEXT
                                ), VkKeyboardButtonColor.NEGATIVE
                        )
                );
            } else {
                vkKeyboardButtons.get(5).add(
                        new VkKeyboardButton(
                                new VkKeyboardButtonActionText(
                                        "Вкл. рассылку сессии на послезавтра",
                                        DEFAULT_PAYLOAD,
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
                                        DEFAULT_PAYLOAD,
                                        VkKeyboardButtonActionType.TEXT
                                ), VkKeyboardButtonColor.NEGATIVE
                        )
                );
            } else {
                vkKeyboardButtons.get(1).add(
                        new VkKeyboardButton(
                                new VkKeyboardButtonActionText(
                                        "Вкл. рассылку сессии на сегодня",
                                        DEFAULT_PAYLOAD,
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
                                        DEFAULT_PAYLOAD,
                                        VkKeyboardButtonActionType.TEXT
                                ), VkKeyboardButtonColor.NEGATIVE
                        )
                );
            } else {
                vkKeyboardButtons.get(3).add(
                        new VkKeyboardButton(
                                new VkKeyboardButtonActionText(
                                        "Вкл. рассылку сессии на завтра",
                                        DEFAULT_PAYLOAD,
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
                                        DEFAULT_PAYLOAD,
                                        VkKeyboardButtonActionType.TEXT
                                ), VkKeyboardButtonColor.NEGATIVE
                        )
                );
            } else {
                vkKeyboardButtons.get(5).add(
                        new VkKeyboardButton(
                                new VkKeyboardButtonActionText(
                                        "Вкл. рассылку сессии на послезавтра",
                                        DEFAULT_PAYLOAD,
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
                                DEFAULT_PAYLOAD,
                                VkKeyboardButtonActionType.TEXT
                        ), VkKeyboardButtonColor.PRIMARY
                )
        );

        if (null != scheduleTodayNotification && scheduleTodayNotification.isEnabled()) {
            vkKeyboardButtons.get(1).add(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Выкл. рассылку расп-я на сегодня",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.NEGATIVE
                    )
            );
        } else {
            vkKeyboardButtons.get(1).add(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Вкл. рассылку расп-я на сегодня",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.POSITIVE
                    )
            );
        }

        vkKeyboardButtons.get(2).add(
                new VkKeyboardButton(
                        new VkKeyboardButtonActionText(
                                "Уст. время рассылки расп-я на завтра",
                                DEFAULT_PAYLOAD,
                                VkKeyboardButtonActionType.TEXT
                        ), VkKeyboardButtonColor.PRIMARY
                )
        );

        if (null != scheduleTomorrowNotification && scheduleTomorrowNotification.isEnabled()) {
            vkKeyboardButtons.get(3).add(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Выкл. рассылку расп-я на завтра",
                                    DEFAULT_PAYLOAD,
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.NEGATIVE
                    )
            );
        } else {
            vkKeyboardButtons.get(3).add(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    "Вкл. рассылку расп-я на завтра",
                                    DEFAULT_PAYLOAD,
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
