package com.scribassu.scribabot.generators;

import com.scribassu.scribabot.dto.InnerBotUser;
import com.scribassu.scribabot.entities.*;
import com.scribassu.scribabot.repositories.*;
import com.scribassu.scribabot.util.BotMessageUtils;
import com.scribassu.scribabot.util.BotUserSource;
import com.scribassu.tracto.domain.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

import static com.scribassu.scribabot.util.Constants.MAX_KEYBOARD_TEXT_LENGTH;
import static com.scribassu.scribabot.util.TgKeyboardEmoji.*;

@Service
public class TgKeyboardGenerator {

    private final ExamPeriodAfterTomorrowNotificationRepository examPeriodAfterTomorrowNotificationRepository;
    private final ExamPeriodTomorrowNotificationRepository examPeriodTomorrowNotificationRepository;
    private final ExamPeriodTodayNotificationRepository examPeriodTodayNotificationRepository;
    private final ScheduleTodayNotificationRepository scheduleTodayNotificationRepository;
    private final ScheduleTomorrowNotificationRepository scheduleTomorrowNotificationRepository;
    private final ExtramuralEventTodayNotificationRepository extramuralEventTodayNotificationRepository;
    private final ExtramuralEventTomorrowNotificationRepository extramuralEventTomorrowNotificationRepository;
    private final ExtramuralEventAfterTomorrowNotificationRepository extramuralEventAfterTomorrowNotificationRepository;

    @Autowired
    public TgKeyboardGenerator(ExamPeriodAfterTomorrowNotificationRepository examPeriodAfterTomorrowNotificationRepository,
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

    private static final KeyboardButton MAIN_MENU_BUTTON = new KeyboardButton(String.format("%1$s Главное меню %1$s", BACK_EMOJI));
    private static final KeyboardButton SETTINGS_BUTTON = new KeyboardButton(String.format("%1$s Настройки %1$s", SETTINGS_EMOJI));
    private static final KeyboardButton CHOOSE_DEPARTMENT_AND_GROUP_BUTTON = new KeyboardButton(String.format("%1$s Выбрать факультет и группу %1$s", SETTINGS_EMOJI));
    private static final KeyboardButton TODAY_SCHEDULE_BUTTON = new KeyboardButton("Сегодня");
    private static final KeyboardButton TOMORROW_SCHEDULE_BUTTON = new KeyboardButton("Завтра");
    private static final KeyboardButton YESTERDAY_SCHEDULE_BUTTON = new KeyboardButton("Вчера");

    /*
    TODO:


    DONE:
    main menu
    education forms
    courses
    schedule fulltime
    schedule extramural
    departments
    confirm deletion
    teachers
    group numbers
    hours
    settings notification schedule
    settings notification exams
    settings
     */

    public static ReplyKeyboardMarkup mainMenu() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            rows.add(new KeyboardRow());
        //rows.get(0).add(new KeyboardButton("\uD83D\uDDD3 Расписание студентов \uD83D\uDDD3"));
        //rows.get(1).add(new KeyboardButton("\uD83C\uDF93 Расписание преподавателей \uD83C\uDF93"));
        rows.get(0).add(new KeyboardButton(String.format("%1$s Расписание студентов %1$s", STUDENT_SCHEDULE_EMOJI)));
        rows.get(1).add(new KeyboardButton(String.format("%1$s Расписание преподавателей %1$s", TEACHER_SCHEDULE_EMOJI)));
        rows.get(2).add(SETTINGS_BUTTON);
        rows.get(3).add(new KeyboardButton(String.format("%1$s Справка %1$s", HELP_EMOJI)));
        replyKeyboardMarkup.setKeyboard(rows);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup educationForms() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        for (int i = 0; i < 2; i++)
            rows.add(new KeyboardRow());
        rows.get(0).add(new KeyboardButton("Дневная"));
        rows.get(0).add(new KeyboardButton("Заочная"));
        rows.get(1).add(MAIN_MENU_BUTTON);
        replyKeyboardMarkup.setKeyboard(rows);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup courses() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            rows.add(new KeyboardRow());
        rows.get(0).add(new KeyboardButton("1 курс"));
        rows.get(0).add(new KeyboardButton("2 курс"));
        rows.get(1).add(new KeyboardButton("3 курс"));
        rows.get(1).add(new KeyboardButton("4 курс"));
        rows.get(2).add(new KeyboardButton("5 курс"));
        rows.get(2).add(new KeyboardButton("Другое"));
        rows.get(3).add(MAIN_MENU_BUTTON);
        replyKeyboardMarkup.setKeyboard(rows);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup departments() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        for (int i = 0; i < 9; i++)
            rows.add(new KeyboardRow());
        rows.get(0).add(new KeyboardButton("БИОЛОГ"));
        rows.get(0).add(new KeyboardButton("ГЕОГРАФ"));
        rows.get(0).add(new KeyboardButton("ГЕОЛОГ"));

        rows.get(1).add(new KeyboardButton("ИДПО"));
        rows.get(1).add(new KeyboardButton("И-Т ИСКУССТВ"));
        rows.get(1).add(new KeyboardButton("ИИИМО"));

        rows.get(2).add(new KeyboardButton("И-Т ФИЗИКИ"));
        rows.get(2).add(new KeyboardButton("И-Т ФИЗ КУЛ"));
        rows.get(2).add(new KeyboardButton("ИФИЖ"));

        rows.get(3).add(new KeyboardButton("И-Т ХИМИИ"));
        rows.get(3).add(new KeyboardButton("МЕХМАТ"));
        rows.get(3).add(new KeyboardButton("СОЦФАК"));

        rows.get(4).add(new KeyboardButton("ФИЯИЛ"));
        rows.get(4).add(new KeyboardButton("КНИИТ"));
        rows.get(4).add(new KeyboardButton("ФНБМТ"));

        rows.get(5).add(new KeyboardButton("ФНП"));
        rows.get(5).add(new KeyboardButton("ПСИХОЛОГ"));
        rows.get(5).add(new KeyboardButton("ППИСО"));

        rows.get(6).add(new KeyboardButton("ФИЛОСОФ"));
        rows.get(6).add(new KeyboardButton("ЭКОНОМ"));
        rows.get(6).add(new KeyboardButton("ЮРФАК"));

        rows.get(7).add(new KeyboardButton("ГЕОЛОГ К-Ж"));
        rows.get(7).add(new KeyboardButton("К-Ж ЯБЛОЧКОВА"));

        rows.get(8).add(MAIN_MENU_BUTTON);

        replyKeyboardMarkup.setKeyboard(rows);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup confirmDeletion() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            rows.add(new KeyboardRow());
        rows.get(0).add(new KeyboardButton(String.format("%1$s Да %1$s", YES_EMOJI)));
        rows.get(1).add(new KeyboardButton(String.format("%1$s Нет %1$s", NO_EMOJI)));
        rows.get(2).add(SETTINGS_BUTTON);
        rows.get(3).add(MAIN_MENU_BUTTON);
        replyKeyboardMarkup.setKeyboard(rows);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup fullTimeSchedule() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        for (int i = 0; i < 6; i++)
            rows.add(new KeyboardRow());
        rows.get(0).add(TODAY_SCHEDULE_BUTTON);
        rows.get(0).add(TOMORROW_SCHEDULE_BUTTON);
        rows.get(0).add(YESTERDAY_SCHEDULE_BUTTON);

        rows.get(1).add(new KeyboardButton("Пн"));
        rows.get(1).add(new KeyboardButton("Вт"));
        rows.get(1).add(new KeyboardButton("Ср"));

        rows.get(2).add(new KeyboardButton("Чт"));
        rows.get(2).add(new KeyboardButton("Пт"));
        rows.get(2).add(new KeyboardButton("Сб"));

        rows.get(3).add(new KeyboardButton(String.format("%1$s Сессия %1$s", FIRE_EMOJI)));

        rows.get(4).add(CHOOSE_DEPARTMENT_AND_GROUP_BUTTON);

        rows.get(5).add(MAIN_MENU_BUTTON);

        replyKeyboardMarkup.setKeyboard(rows);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup extramuralSchedule() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            rows.add(new KeyboardRow());
        rows.get(0).add(new KeyboardButton("Все занятия"));

        rows.get(1).add(TODAY_SCHEDULE_BUTTON);
        rows.get(1).add(TOMORROW_SCHEDULE_BUTTON);
        rows.get(1).add(YESTERDAY_SCHEDULE_BUTTON);

        rows.get(2).add(CHOOSE_DEPARTMENT_AND_GROUP_BUTTON);

        rows.get(3).add(MAIN_MENU_BUTTON);

        replyKeyboardMarkup.setKeyboard(rows);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup teacherSchedule() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        for (int i = 0; i < 6; i++)
            rows.add(new KeyboardRow());
        rows.get(0).add(TODAY_SCHEDULE_BUTTON);
        rows.get(0).add(TOMORROW_SCHEDULE_BUTTON);
        rows.get(0).add(YESTERDAY_SCHEDULE_BUTTON);

        rows.get(1).add(new KeyboardButton("Пн"));
        rows.get(1).add(new KeyboardButton("Вт"));
        rows.get(1).add(new KeyboardButton("Ср"));

        rows.get(2).add(new KeyboardButton("Чт"));
        rows.get(2).add(new KeyboardButton("Пт"));
        rows.get(2).add(new KeyboardButton("Сб"));

        rows.get(3).add(new KeyboardButton(String.format("%1$s Сессия %1$s", FIRE_EMOJI)));

        rows.get(4).add(new KeyboardButton("Занятия заочников"));

        rows.get(5).add(MAIN_MENU_BUTTON);

        replyKeyboardMarkup.setKeyboard(rows);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup hours() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        int hour = 1;
        for (int i = 0; i < 6; i++) {
            rows.add(new KeyboardRow());
            for (int k = 0; k < 4; k++) {
                rows.get(i).add(new KeyboardButton(hour + " ч"));
                hour++;
            }
        }
        rows.get(5).set(3, "0 ч");

        rows.add(new KeyboardRow());
        rows.add(new KeyboardRow());
        rows.get(6).add(SETTINGS_BUTTON);
        rows.get(7).add(MAIN_MENU_BUTTON);

        replyKeyboardMarkup.setKeyboard(rows);
        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup groupNumbers(List<String> groupNumbers) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        rows.add(new KeyboardRow());
        int i = 0;
        int row = 0;

        int mod = groupNumbers.get(0).length() > 5 ? 3 : 5; //to make long numbers visible
        int mmod = mod - 1;

        while (i < groupNumbers.size()) {
            if (i % mod == mmod) {
                row++;
                rows.add(new KeyboardRow());
            }
            rows.get(row).add(new KeyboardButton("г " + groupNumbers.get(i)));
            i++;
        }

        rows.add(new KeyboardRow());
        rows.get(rows.size() - 1).add(MAIN_MENU_BUTTON);

        replyKeyboardMarkup.setKeyboard(rows);
        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup teachers(List<Teacher> teachers) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        rows.add(new KeyboardRow());
        int i = 0;
        int row = 0;

        while (i < teachers.size()) {
            if (i % 5 == 4) {
                row++;
                rows.add(new KeyboardRow());
            }
            String teacher = teachers.get(i).getId() + " "
                    + teachers.get(i).getSurname() + " "
                    + teachers.get(i).getName() + " "
                    + teachers.get(i).getPatronymic();
            teacher = teacher.length() > MAX_KEYBOARD_TEXT_LENGTH ? teacher.substring(0, MAX_KEYBOARD_TEXT_LENGTH) : teacher;
            rows.get(row).add(new KeyboardButton(teacher));
            i++;
        }

        rows.add(new KeyboardRow());
        rows.get(rows.size() - 1).add(MAIN_MENU_BUTTON);

        replyKeyboardMarkup.setKeyboard(rows);
        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup settingsExamNotification(InnerBotUser botUser) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            rows.add(new KeyboardRow());
        }

        String userId = botUser.getUserId();
        BotUserSource source = botUser.getSource();

        rows.get(0).add(new KeyboardButton("Уст. время рассылки сессии на сегодня"));
        rows.get(2).add(new KeyboardButton("Уст. время рассылки сессии на завтра"));
        rows.get(4).add(new KeyboardButton("Уст. время рас-ки сессии на послезавтра"));
        rows.get(6).add(SETTINGS_BUTTON);
        rows.get(7).add(MAIN_MENU_BUTTON);

        if (BotMessageUtils.isBotUserFullTime(botUser)) {
            ExamPeriodTodayNotification examPeriodTodayNotification = examPeriodTodayNotificationRepository.findByUserIdAndUserSource(userId, source);
            ExamPeriodTomorrowNotification examPeriodTomorrowNotification = examPeriodTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);
            ExamPeriodAfterTomorrowNotification examPeriodAfterTomorrowNotification = examPeriodAfterTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);

            if (null != examPeriodTodayNotification && examPeriodTodayNotification.isEnabled()) {
                rows.get(1).add(new KeyboardButton(String.format("%1$s Выкл. рассылку сессии на сегодня %1$s", NO_EMOJI)));
            } else {
                rows.get(1).add(new KeyboardButton(String.format("%1$s Вкл. рассылку сессии на сегодня %1$s", YES_EMOJI)));
            }

            if (null != examPeriodTomorrowNotification && examPeriodTomorrowNotification.isEnabled()) {
                rows.get(3).add(new KeyboardButton(String.format("%1$s Выкл. рассылку сессии на завтра %1$s", NO_EMOJI)));
            } else {
                rows.get(3).add(new KeyboardButton(String.format("%1$s Вкл. рассылку сессии на завтра %1$s", YES_EMOJI)));
            }

            if (null != examPeriodAfterTomorrowNotification && examPeriodAfterTomorrowNotification.isEnabled()) {
                rows.get(5).add(new KeyboardButton(String.format("%1$s Выкл. рассылку сессии на послезавтра %1$s", NO_EMOJI)));
            } else {
                rows.get(5).add(new KeyboardButton(String.format("%1$s Вкл. рассылку сессии на послезавтра %1$s", YES_EMOJI)));
            }
        }

        if (BotMessageUtils.isBotUserExtramural(botUser)) {
            ExtramuralEventTodayNotification extramuralEventTodayNotification = extramuralEventTodayNotificationRepository.findByUserIdAndUserSource(userId, source);
            ExtramuralEventTomorrowNotification extramuralEventTomorrowNotification = extramuralEventTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);
            ExtramuralEventAfterTomorrowNotification extramuralEventAfterTomorrowNotification = extramuralEventAfterTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);

            if (null != extramuralEventTodayNotification && extramuralEventTodayNotification.isEnabled()) {
                rows.get(1).add(new KeyboardButton(String.format("%1$s Выкл. рассылку сессии на сегодня %1$s", NO_EMOJI)));
            } else {
                rows.get(1).add(new KeyboardButton(String.format("%1$s Вкл. рассылку сессии на сегодня %1$s", YES_EMOJI)));
            }

            if (null != extramuralEventTomorrowNotification && extramuralEventTomorrowNotification.isEnabled()) {
                rows.get(3).add(new KeyboardButton(String.format("%1$s Выкл. рассылку сессии на завтра %1$s", NO_EMOJI)));
            } else {
                rows.get(3).add(new KeyboardButton(String.format("%1$s Вкл. рассылку сессии на завтра %1$s", YES_EMOJI)));
            }


            if (null != extramuralEventAfterTomorrowNotification && extramuralEventAfterTomorrowNotification.isEnabled()) {
                rows.get(5).add(new KeyboardButton(String.format("%1$s Выкл. рассылку сессии на послезавтра %1$s", NO_EMOJI)));
            } else {
                rows.get(5).add(new KeyboardButton(String.format("%1$s Вкл. рассылку сессии на послезавтра %1$s", YES_EMOJI)));
            }
        }

        replyKeyboardMarkup.setKeyboard(rows);
        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup settingsScheduleNotification(InnerBotUser botUser) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            rows.add(new KeyboardRow());
        }

        String userId = botUser.getUserId();
        BotUserSource source = botUser.getSource();

        ScheduleTodayNotification scheduleTodayNotification = scheduleTodayNotificationRepository.findByUserIdAndUserSource(userId, source);
        ScheduleTomorrowNotification scheduleTomorrowNotification = scheduleTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);

        rows.get(0).add(new KeyboardButton("Уст. время рассылки расп-я на сегодня"));

        if (null != scheduleTodayNotification && scheduleTodayNotification.isEnabled()) {
            rows.get(1).add(new KeyboardButton(String.format("%1$s Выкл. рассылку расп-я на сегодня %1$s", NO_EMOJI)));
        } else {
            rows.get(1).add(new KeyboardButton(String.format("%1$s Вкл. рассылку расп-я на сегодня %1$s", YES_EMOJI)));
        }

        rows.get(2).add(new KeyboardButton("Уст. время рассылки расп-я на завтра"));

        if (null != scheduleTomorrowNotification && scheduleTomorrowNotification.isEnabled()) {
            rows.get(3).add(new KeyboardButton(String.format("%1$s Выкл. рассылку расп-я на завтра %1$s", NO_EMOJI)));
        } else {
            rows.get(3).add(new KeyboardButton(String.format("%1$s Вкл. рассылку расп-я на завтра %1$s", YES_EMOJI)));
        }

        rows.get(4).add(SETTINGS_BUTTON);
        rows.get(5).add(MAIN_MENU_BUTTON);

        replyKeyboardMarkup.setKeyboard(rows);
        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup settings(InnerBotUser user) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            rows.add(new KeyboardRow());
        }

        // 0 row is checked in the end

        rows.get(1).add(new KeyboardButton(String.format("%1$s Рассылка расписания сессии %1$s", NOTIFICATION_EMOJI)));

        if (user.isFilterNomDenom()) {
            rows.get(2).add(new KeyboardButton(String.format("%1$s Выкл. фильтр по типу недели %1$s", NO_EMOJI)));
        } else {
            rows.get(2).add(new KeyboardButton(String.format("%1$s Вкл. фильтр по типу недели %1$s", YES_EMOJI)));
        }

        rows.get(3).add(CHOOSE_DEPARTMENT_AND_GROUP_BUTTON);
        rows.get(3).add(new KeyboardButton(String.format("%1$s Текущие настройки пользователя %1$s", PROFILE_EMOJI)));
        rows.get(4).add(new KeyboardButton(String.format("%1$s Удалить профиль %1$s", DELETE_PROFILE_EMOJI)));
        rows.get(5).add(MAIN_MENU_BUTTON);

        if (!BotMessageUtils.isBotUserExtramural(user)) {
            rows.get(0).add(new KeyboardButton(String.format("%1$s Рассылка расписания занятий %1$s", NOTIFICATION_EMOJI)));
        } else {
            rows.remove(0);
        }

        replyKeyboardMarkup.setKeyboard(rows);
        return replyKeyboardMarkup;
    }
}
