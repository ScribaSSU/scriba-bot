package com.scribassu.scribabot.generators;

import com.scribassu.scribabot.entities.notifications.*;
import com.scribassu.scribabot.model.BotUser;
import com.scribassu.scribabot.model.BotUserSource;
import com.scribassu.scribabot.model.keyboard.Keyboard;
import com.scribassu.scribabot.model.keyboard.KeyboardButton;
import com.scribassu.scribabot.repositories.notifications.*;
import com.scribassu.tracto.dto.TeacherDto;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.scribassu.scribabot.model.keyboard.KeyboardEmoji.*;
import static com.scribassu.scribabot.text.CommandText.TEACHER_PREFIX;
import static com.scribassu.scribabot.util.Constants.MAX_KEYBOARD_TEXT_LENGTH;

@Service
@Data
public class InnerKeyboardGenerator {

    private final ExamPeriodAfterTomorrowNotificationRepository examPeriodAfterTomorrowNotificationRepository;
    private final ExamPeriodTomorrowNotificationRepository examPeriodTomorrowNotificationRepository;
    private final ExamPeriodTodayNotificationRepository examPeriodTodayNotificationRepository;
    private final ScheduleTodayNotificationRepository scheduleTodayNotificationRepository;
    private final ScheduleTomorrowNotificationRepository scheduleTomorrowNotificationRepository;
    private final ExtramuralEventTodayNotificationRepository extramuralEventTodayNotificationRepository;
    private final ExtramuralEventTomorrowNotificationRepository extramuralEventTomorrowNotificationRepository;
    private final ExtramuralEventAfterTomorrowNotificationRepository extramuralEventAfterTomorrowNotificationRepository;

    private static final KeyboardButton MAIN_MENU_BUTTON = new KeyboardButton(String.format("%1$s Главное меню %1$s", BACK_EMOJI));
    private static final KeyboardButton SETTINGS_BUTTON = new KeyboardButton(String.format("%1$s Настройки %1$s", SETTINGS_EMOJI));
    private static final KeyboardButton CHOOSE_DEPARTMENT_AND_GROUP_BUTTON = new KeyboardButton(String.format("%1$s Выбрать факультет и группу %1$s", SETTINGS_EMOJI));
    private static final KeyboardButton TODAY_SCHEDULE_BUTTON = new KeyboardButton("Сегодня");
    private static final KeyboardButton TOMORROW_SCHEDULE_BUTTON = new KeyboardButton("Завтра");
    private static final KeyboardButton YESTERDAY_SCHEDULE_BUTTON = new KeyboardButton("Вчера");
    private static final KeyboardButton EXIT_TEACHER_SCHEDULE_MODE = new KeyboardButton("Выйти из расписания преподавателя");

    /*
    TODO:
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

    DONE:
   
     */

    public Keyboard mainMenu(BotUser botUser) {
        var innerKeyboard = new Keyboard();
        for (var i = 0; i < 4; i++)
            innerKeyboard.add(new ArrayList<>());
        innerKeyboard.get(0).add(new KeyboardButton(String.format("%1$s Расписание студентов %1$s", STUDENT_SCHEDULE_EMOJI), Optional.empty()));
        innerKeyboard.get(1).add(new KeyboardButton(String.format("%1$s Расписание преподавателей %1$s", TEACHER_SCHEDULE_EMOJI)));
        innerKeyboard.get(2).add(SETTINGS_BUTTON);
        innerKeyboard.get(3).add(new KeyboardButton(String.format("%1$s Справка %1$s", HELP_EMOJI)));

        if (botUser.wantTeacherSchedule()) {
            innerKeyboard.add(new ArrayList<>());
            innerKeyboard.get(4).add(EXIT_TEACHER_SCHEDULE_MODE);
        }

        return innerKeyboard;
    }

    public Keyboard educationForms() {
        var innerKeyboard = new Keyboard();
        for (var i = 0; i < 2; i++)
            innerKeyboard.add(new ArrayList<>());
        innerKeyboard.get(0).add(new KeyboardButton("Дневная"));
        innerKeyboard.get(0).add(new KeyboardButton("Заочная"));
        innerKeyboard.get(1).add(MAIN_MENU_BUTTON);

        return innerKeyboard;
    }

    public Keyboard courses() {
        var innerKeyboard = new Keyboard();
        for (var i = 0; i < 4; i++)
            innerKeyboard.add(new ArrayList<>());
        innerKeyboard.get(0).add(new KeyboardButton("1 курс"));
        innerKeyboard.get(0).add(new KeyboardButton("2 курс"));
        innerKeyboard.get(1).add(new KeyboardButton("3 курс"));
        innerKeyboard.get(1).add(new KeyboardButton("4 курс"));
        innerKeyboard.get(2).add(new KeyboardButton("5 курс"));
        innerKeyboard.get(2).add(new KeyboardButton("Другое"));
        innerKeyboard.get(3).add(MAIN_MENU_BUTTON);
        
        return innerKeyboard;
    }

    public Keyboard departments() {
        var innerKeyboard = new Keyboard();
        for (var i = 0; i < 9; i++)
            innerKeyboard.add(new ArrayList<>());
        innerKeyboard.get(0).add(new KeyboardButton("БИОЛОГ"));
        innerKeyboard.get(0).add(new KeyboardButton("ГЕОГРАФ"));
        innerKeyboard.get(0).add(new KeyboardButton("ГЕОЛОГ"));

        innerKeyboard.get(1).add(new KeyboardButton("ИДПО"));
        innerKeyboard.get(1).add(new KeyboardButton("И-Т ИСКУССТВ"));
        innerKeyboard.get(1).add(new KeyboardButton("ИИИМО"));

        innerKeyboard.get(2).add(new KeyboardButton("И-Т ФИЗИКИ"));
        innerKeyboard.get(2).add(new KeyboardButton("И-Т ФИЗ КУЛ"));
        innerKeyboard.get(2).add(new KeyboardButton("ИФИЖ"));

        innerKeyboard.get(3).add(new KeyboardButton("И-Т ХИМИИ"));
        innerKeyboard.get(3).add(new KeyboardButton("МЕХМАТ"));
        innerKeyboard.get(3).add(new KeyboardButton("СОЦФАК"));

        innerKeyboard.get(4).add(new KeyboardButton("ФИЯИЛ"));
        innerKeyboard.get(4).add(new KeyboardButton("КНИИТ"));
        innerKeyboard.get(4).add(new KeyboardButton("ФНБМТ"));

        innerKeyboard.get(5).add(new KeyboardButton("ФНП"));
        innerKeyboard.get(5).add(new KeyboardButton("ПСИХОЛОГ"));
        innerKeyboard.get(5).add(new KeyboardButton("ППИСО"));

        innerKeyboard.get(6).add(new KeyboardButton("ФИЛОСОФ"));
        innerKeyboard.get(6).add(new KeyboardButton("ЭКОНОМ"));
        innerKeyboard.get(6).add(new KeyboardButton("ЮРФАК"));

        innerKeyboard.get(7).add(new KeyboardButton("ГЕОЛОГ К-Ж"));
        innerKeyboard.get(7).add(new KeyboardButton("К-Ж ЯБЛОЧКОВА"));

        innerKeyboard.get(8).add(MAIN_MENU_BUTTON);

        return innerKeyboard;
    }

    public Keyboard confirmDeletion() {
        var innerKeyboard = new Keyboard();
        for (var i = 0; i < 4; i++)
            innerKeyboard.add(new ArrayList<>());
        innerKeyboard.get(0).add(new KeyboardButton(String.format("%1$s Да %1$s", YES_EMOJI)));
        innerKeyboard.get(1).add(new KeyboardButton(String.format("%1$s Нет %1$s", NO_EMOJI)));
        innerKeyboard.get(2).add(SETTINGS_BUTTON);
        innerKeyboard.get(3).add(MAIN_MENU_BUTTON);
        
        return innerKeyboard;
    }

    public Keyboard fullTimeSchedule() {
        var innerKeyboard = new Keyboard();
        for (var i = 0; i < 6; i++)
            innerKeyboard.add(new ArrayList<>());
        innerKeyboard.get(0).add(TODAY_SCHEDULE_BUTTON);
        innerKeyboard.get(0).add(TOMORROW_SCHEDULE_BUTTON);
        innerKeyboard.get(0).add(YESTERDAY_SCHEDULE_BUTTON);

        innerKeyboard.get(1).add(new KeyboardButton("Пн"));
        innerKeyboard.get(1).add(new KeyboardButton("Вт"));
        innerKeyboard.get(1).add(new KeyboardButton("Ср"));

        innerKeyboard.get(2).add(new KeyboardButton("Чт"));
        innerKeyboard.get(2).add(new KeyboardButton("Пт"));
        innerKeyboard.get(2).add(new KeyboardButton("Сб"));

        innerKeyboard.get(3).add(new KeyboardButton(String.format("%1$s Сессия %1$s", FIRE_EMOJI)));

        innerKeyboard.get(4).add(CHOOSE_DEPARTMENT_AND_GROUP_BUTTON);

        innerKeyboard.get(5).add(MAIN_MENU_BUTTON);
        
        return innerKeyboard;
    }

    public Keyboard extramuralSchedule() {
        var innerKeyboard = new Keyboard();
        for (var i = 0; i < 4; i++)
            innerKeyboard.add(new ArrayList<>());
        innerKeyboard.get(0).add(new KeyboardButton("Все занятия"));

        innerKeyboard.get(1).add(TODAY_SCHEDULE_BUTTON);
        innerKeyboard.get(1).add(TOMORROW_SCHEDULE_BUTTON);
        innerKeyboard.get(1).add(YESTERDAY_SCHEDULE_BUTTON);

        innerKeyboard.get(2).add(CHOOSE_DEPARTMENT_AND_GROUP_BUTTON);

        innerKeyboard.get(3).add(MAIN_MENU_BUTTON);
        
        return innerKeyboard;
    }

    public Keyboard teacherSchedule(BotUser botUser) {
        var innerKeyboard = new Keyboard();
        for (var i = 0; i < 6; i++)
            innerKeyboard.add(new ArrayList<>());
        innerKeyboard.get(0).add(TODAY_SCHEDULE_BUTTON);
        innerKeyboard.get(0).add(TOMORROW_SCHEDULE_BUTTON);
        innerKeyboard.get(0).add(YESTERDAY_SCHEDULE_BUTTON);

        innerKeyboard.get(1).add(new KeyboardButton("Пн"));
        innerKeyboard.get(1).add(new KeyboardButton("Вт"));
        innerKeyboard.get(1).add(new KeyboardButton("Ср"));

        innerKeyboard.get(2).add(new KeyboardButton("Чт"));
        innerKeyboard.get(2).add(new KeyboardButton("Пт"));
        innerKeyboard.get(2).add(new KeyboardButton("Сб"));

        innerKeyboard.get(3).add(new KeyboardButton(String.format("%1$s Сессия %1$s", FIRE_EMOJI)));

        innerKeyboard.get(4).add(new KeyboardButton("Занятия заочников"));

        innerKeyboard.get(5).add(MAIN_MENU_BUTTON);

        if (botUser.wantTeacherSchedule()) {
            innerKeyboard.add(new ArrayList<>());
            innerKeyboard.get(6).add(EXIT_TEACHER_SCHEDULE_MODE);
        }
        
        return innerKeyboard;
    }

    public Keyboard hours() {
        var innerKeyboard = new Keyboard();
        int hour = 1;
        for (var i = 0; i < 6; i++) {
            innerKeyboard.add(new ArrayList<>());
            for (int k = 0; k < 4; k++) {
                innerKeyboard.get(i).add(new KeyboardButton(hour + " ч"));
                hour++;
            }
        }
        innerKeyboard.get(5).set(3, new KeyboardButton("0 ч"));

        innerKeyboard.add(new ArrayList<>());
        innerKeyboard.add(new ArrayList<>());
        innerKeyboard.get(6).add(SETTINGS_BUTTON);
        innerKeyboard.get(7).add(MAIN_MENU_BUTTON);
        
        return innerKeyboard;
    }

    public Keyboard groupNumbers(List<String> groupNumbers) {
        var innerKeyboard = new Keyboard();
        innerKeyboard.add(new ArrayList<>());
        var i = 0;
        int row = 0;

        int mod = groupNumbers.get(0).length() > 5 ? 3 : 5; //to make long numbers visible
        int mmod = mod - 1;

        while (i < groupNumbers.size()) {
            if (i % mod == mmod) {
                row++;
                innerKeyboard.add(new ArrayList<>());
            }
            innerKeyboard.get(row).add(new KeyboardButton("Г " + groupNumbers.get(i)));
            i++;
        }

        innerKeyboard.add(new ArrayList<>());
        innerKeyboard.get(innerKeyboard.getButtons().size() - 1).add(MAIN_MENU_BUTTON);
        
        return innerKeyboard;
    }

    public Keyboard teachers(List<TeacherDto> teachers) {
        var innerKeyboard = new Keyboard();
        innerKeyboard.add(new ArrayList<>());
        var i = 0;
        int row = 0;

        while (i < teachers.size()) {
            if (i % 5 == 4) {
                row++;
                innerKeyboard.add(new ArrayList<>());
            }
            var curTeacher = teachers.get(i);
            var teacher = String.format("%s %s %s %s %s", TEACHER_PREFIX, curTeacher.getId(), curTeacher.getSurname(), curTeacher.getName(), curTeacher.getPatronymic());
            teacher = teacher.length() > MAX_KEYBOARD_TEXT_LENGTH ? teacher.substring(0, MAX_KEYBOARD_TEXT_LENGTH) : teacher;
            innerKeyboard.get(row).add(new KeyboardButton(teacher));
            i++;
        }

        innerKeyboard.add(new ArrayList<>());
        innerKeyboard.get(innerKeyboard.getButtons().size() - 1).add(MAIN_MENU_BUTTON);
        
        return innerKeyboard;
    }

    public Keyboard settingsExamNotification(BotUser botUser) {
        var innerKeyboard = new Keyboard();
        for (var i = 0; i < 8; i++) {
            innerKeyboard.add(new ArrayList<>());
        }

        String userId = botUser.getUserId();
        BotUserSource source = botUser.getSource();

        innerKeyboard.get(0).add(new KeyboardButton("Уст. время рассылки сессии на сегодня"));
        innerKeyboard.get(2).add(new KeyboardButton("Уст. время рассылки сессии на завтра"));
        innerKeyboard.get(4).add(new KeyboardButton("Уст. время рас-ки сессии на послезавтра"));
        innerKeyboard.get(6).add(SETTINGS_BUTTON);
        innerKeyboard.get(7).add(MAIN_MENU_BUTTON);

        if (BotUser.isBotUserFullTime(botUser)) {
            ExamPeriodTodayNotification examPeriodTodayNotification = examPeriodTodayNotificationRepository.findByUserIdAndUserSource(userId, source);
            ExamPeriodTomorrowNotification examPeriodTomorrowNotification = examPeriodTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);
            ExamPeriodAfterTomorrowNotification examPeriodAfterTomorrowNotification = examPeriodAfterTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);

            if (null != examPeriodTodayNotification && examPeriodTodayNotification.isEnabled()) {
                innerKeyboard.get(1).add(new KeyboardButton(String.format("%1$s Выкл. рассылку сессии на сегодня %1$s", NO_EMOJI)));
            } else {
                innerKeyboard.get(1).add(new KeyboardButton(String.format("%1$s Вкл. рассылку сессии на сегодня %1$s", YES_EMOJI)));
            }

            if (null != examPeriodTomorrowNotification && examPeriodTomorrowNotification.isEnabled()) {
                innerKeyboard.get(3).add(new KeyboardButton(String.format("%1$s Выкл. рассылку сессии на завтра %1$s", NO_EMOJI)));
            } else {
                innerKeyboard.get(3).add(new KeyboardButton(String.format("%1$s Вкл. рассылку сессии на завтра %1$s", YES_EMOJI)));
            }

            if (null != examPeriodAfterTomorrowNotification && examPeriodAfterTomorrowNotification.isEnabled()) {
                innerKeyboard.get(5).add(new KeyboardButton(String.format("%1$s Выкл. рассылку сессии на послезавтра %1$s", NO_EMOJI)));
            } else {
                innerKeyboard.get(5).add(new KeyboardButton(String.format("%1$s Вкл. рассылку сессии на послезавтра %1$s", YES_EMOJI)));
            }
        }

        if (BotUser.isBotUserExtramural(botUser)) {
            ExtramuralEventTodayNotification extramuralEventTodayNotification = extramuralEventTodayNotificationRepository.findByUserIdAndUserSource(userId, source);
            ExtramuralEventTomorrowNotification extramuralEventTomorrowNotification = extramuralEventTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);
            ExtramuralEventAfterTomorrowNotification extramuralEventAfterTomorrowNotification = extramuralEventAfterTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);

            if (null != extramuralEventTodayNotification && extramuralEventTodayNotification.isEnabled()) {
                innerKeyboard.get(1).add(new KeyboardButton(String.format("%1$s Выкл. рассылку сессии на сегодня %1$s", NO_EMOJI)));
            } else {
                innerKeyboard.get(1).add(new KeyboardButton(String.format("%1$s Вкл. рассылку сессии на сегодня %1$s", YES_EMOJI)));
            }

            if (null != extramuralEventTomorrowNotification && extramuralEventTomorrowNotification.isEnabled()) {
                innerKeyboard.get(3).add(new KeyboardButton(String.format("%1$s Выкл. рассылку сессии на завтра %1$s", NO_EMOJI)));
            } else {
                innerKeyboard.get(3).add(new KeyboardButton(String.format("%1$s Вкл. рассылку сессии на завтра %1$s", YES_EMOJI)));
            }

            if (null != extramuralEventAfterTomorrowNotification && extramuralEventAfterTomorrowNotification.isEnabled()) {
                innerKeyboard.get(5).add(new KeyboardButton(String.format("%1$s Выкл. рассылку сессии на послезавтра %1$s", NO_EMOJI)));
            } else {
                innerKeyboard.get(5).add(new KeyboardButton(String.format("%1$s Вкл. рассылку сессии на послезавтра %1$s", YES_EMOJI)));
            }
        }

        return innerKeyboard;
    }

    public Keyboard settingsScheduleNotification(BotUser botUser) {
        var innerKeyboard = new Keyboard();
        for (var i = 0; i < 6; i++) {
            innerKeyboard.add(new ArrayList<>());
        }

        String userId = botUser.getUserId();
        BotUserSource source = botUser.getSource();

        var scheduleTodayNotification = scheduleTodayNotificationRepository.findByUserIdAndUserSource(userId, source);
        ScheduleTomorrowNotification scheduleTomorrowNotification = scheduleTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);

        innerKeyboard.get(0).add(new KeyboardButton("Уст. время рассылки расп-я на сегодня"));

        if (scheduleTodayNotification.isPresent() && scheduleTodayNotification.get().isEnabled()) {
            innerKeyboard.get(1).add(new KeyboardButton(String.format("%1$s Выкл. рассылку расп-я на сегодня %1$s", NO_EMOJI)));
        } else {
            innerKeyboard.get(1).add(new KeyboardButton(String.format("%1$s Вкл. рассылку расп-я на сегодня %1$s", YES_EMOJI)));
        }

        innerKeyboard.get(2).add(new KeyboardButton("Уст. время рассылки расп-я на завтра"));

        if (null != scheduleTomorrowNotification && scheduleTomorrowNotification.isEnabled()) {
            innerKeyboard.get(3).add(new KeyboardButton(String.format("%1$s Выкл. рассылку расп-я на завтра %1$s", NO_EMOJI)));
        } else {
            innerKeyboard.get(3).add(new KeyboardButton(String.format("%1$s Вкл. рассылку расп-я на завтра %1$s", YES_EMOJI)));
        }

        innerKeyboard.get(4).add(SETTINGS_BUTTON);
        innerKeyboard.get(5).add(MAIN_MENU_BUTTON);

        
        return innerKeyboard;
    }

    public Keyboard settings(BotUser user) {
        var innerKeyboard = new Keyboard();
        for (var i = 0; i < 8; i++) {
            innerKeyboard.add(new ArrayList<>());
        }

        // 0 row is checked in the end

        innerKeyboard.get(1).add(new KeyboardButton(String.format("%1$s Рассылка расписания сессии %1$s", NOTIFICATION_EMOJI)));

        if (user.isSilentEmptyDays()) {
            innerKeyboard.get(3).add(new KeyboardButton(String.format("%1$s Присылать рассылку, когда пар нет %1$s", YES_EMOJI)));
        } else {
            innerKeyboard.get(3).add(new KeyboardButton(String.format("%1$s Не присылать рассылку, когда пар нет %1$s", NO_EMOJI)));
        }

        if (user.isSentKeyboard()) {
            innerKeyboard.get(4).add(new KeyboardButton(String.format("%1$s Не присылать клавиатуру %1$s", NO_EMOJI)));
        } else {
            innerKeyboard.get(4).add(new KeyboardButton(String.format("%1$s Присылать клавиатуру %1$s", YES_EMOJI)));
        }

        innerKeyboard.get(5).add(CHOOSE_DEPARTMENT_AND_GROUP_BUTTON);
        innerKeyboard.get(5).add(new KeyboardButton(String.format("%1$s Текущие настройки пользователя %1$s", PROFILE_EMOJI)));
        innerKeyboard.get(6).add(new KeyboardButton(String.format("%1$s Удалить профиль %1$s", DELETE_PROFILE_EMOJI)));
        innerKeyboard.get(7).add(MAIN_MENU_BUTTON);

        if (!BotUser.isBotUserExtramural(user)) {
            innerKeyboard.get(0).add(new KeyboardButton(String.format("%1$s Рассылка расписания занятий %1$s", NOTIFICATION_EMOJI)));

            if (user.isFilterNomDenom()) {
                innerKeyboard.get(2).add(new KeyboardButton(String.format("%1$s Выкл. фильтр по типу недели %1$s", NO_EMOJI)));
            } else {
                innerKeyboard.get(2).add(new KeyboardButton(String.format("%1$s Вкл. фильтр по типу недели %1$s", YES_EMOJI)));
            }
        } else {
            innerKeyboard.remove(2); // week type filter
            innerKeyboard.remove(0); // schedule notifications
        }
        
        return innerKeyboard;
    }
}
