package com.scribassu.scribabot.generators;

import com.scribassu.scribabot.model.InnerBotUser;
import com.scribassu.scribabot.model.inner_keyboard.InnerKeyboard;
import com.scribassu.scribabot.model.inner_keyboard.InnerKeyboardButton;
import com.scribassu.scribabot.entities.notifications.*;
import com.scribassu.scribabot.repositories.notifications.*;
import com.scribassu.scribabot.util.BotUserSource;
import com.scribassu.tracto.domain.Teacher;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.scribassu.scribabot.util.Constants.MAX_KEYBOARD_TEXT_LENGTH;
import static com.scribassu.scribabot.model.inner_keyboard.KeyboardEmoji.*;

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

    private static final InnerKeyboardButton MAIN_MENU_BUTTON = new InnerKeyboardButton(String.format("%1$s Главное меню %1$s", BACK_EMOJI));
    private static final InnerKeyboardButton SETTINGS_BUTTON = new InnerKeyboardButton(String.format("%1$s Настройки %1$s", SETTINGS_EMOJI));
    private static final InnerKeyboardButton CHOOSE_DEPARTMENT_AND_GROUP_BUTTON = new InnerKeyboardButton(String.format("%1$s Выбрать факультет и группу %1$s", SETTINGS_EMOJI));
    private static final InnerKeyboardButton TODAY_SCHEDULE_BUTTON = new InnerKeyboardButton("Сегодня");
    private static final InnerKeyboardButton TOMORROW_SCHEDULE_BUTTON = new InnerKeyboardButton("Завтра");
    private static final InnerKeyboardButton YESTERDAY_SCHEDULE_BUTTON = new InnerKeyboardButton("Вчера");

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

    public InnerKeyboard mainMenu() {
        var innerKeyboard = new InnerKeyboard();
        for (var i = 0; i < 4; i++)
            innerKeyboard.add(new ArrayList<>());
        innerKeyboard.get(0).add(new InnerKeyboardButton(String.format("%1$s Расписание студентов %1$s", STUDENT_SCHEDULE_EMOJI), Optional.empty()));
        innerKeyboard.get(1).add(new InnerKeyboardButton(String.format("%1$s Расписание преподавателей %1$s", TEACHER_SCHEDULE_EMOJI)));
        innerKeyboard.get(2).add(SETTINGS_BUTTON);
        innerKeyboard.get(3).add(new InnerKeyboardButton(String.format("%1$s Справка %1$s", HELP_EMOJI)));

        return innerKeyboard;
    }

    public InnerKeyboard educationForms() {
        var innerKeyboard = new InnerKeyboard();
        for (var i = 0; i < 2; i++)
            innerKeyboard.add(new ArrayList<>());
        innerKeyboard.get(0).add(new InnerKeyboardButton("Дневная"));
        innerKeyboard.get(0).add(new InnerKeyboardButton("Заочная"));
        innerKeyboard.get(1).add(MAIN_MENU_BUTTON);

        return innerKeyboard;
    }

    public InnerKeyboard courses() {
        var innerKeyboard = new InnerKeyboard();
        for (var i = 0; i < 4; i++)
            innerKeyboard.add(new ArrayList<>());
        innerKeyboard.get(0).add(new InnerKeyboardButton("1 курс"));
        innerKeyboard.get(0).add(new InnerKeyboardButton("2 курс"));
        innerKeyboard.get(1).add(new InnerKeyboardButton("3 курс"));
        innerKeyboard.get(1).add(new InnerKeyboardButton("4 курс"));
        innerKeyboard.get(2).add(new InnerKeyboardButton("5 курс"));
        innerKeyboard.get(2).add(new InnerKeyboardButton("Другое"));
        innerKeyboard.get(3).add(MAIN_MENU_BUTTON);
        
        return innerKeyboard;
    }

    public InnerKeyboard departments() {
        var innerKeyboard = new InnerKeyboard();
        for (var i = 0; i < 9; i++)
            innerKeyboard.add(new ArrayList<>());
        innerKeyboard.get(0).add(new InnerKeyboardButton("БИОЛОГ"));
        innerKeyboard.get(0).add(new InnerKeyboardButton("ГЕОГРАФ"));
        innerKeyboard.get(0).add(new InnerKeyboardButton("ГЕОЛОГ"));

        innerKeyboard.get(1).add(new InnerKeyboardButton("ИДПО"));
        innerKeyboard.get(1).add(new InnerKeyboardButton("И-Т ИСКУССТВ"));
        innerKeyboard.get(1).add(new InnerKeyboardButton("ИИИМО"));

        innerKeyboard.get(2).add(new InnerKeyboardButton("И-Т ФИЗИКИ"));
        innerKeyboard.get(2).add(new InnerKeyboardButton("И-Т ФИЗ КУЛ"));
        innerKeyboard.get(2).add(new InnerKeyboardButton("ИФИЖ"));

        innerKeyboard.get(3).add(new InnerKeyboardButton("И-Т ХИМИИ"));
        innerKeyboard.get(3).add(new InnerKeyboardButton("МЕХМАТ"));
        innerKeyboard.get(3).add(new InnerKeyboardButton("СОЦФАК"));

        innerKeyboard.get(4).add(new InnerKeyboardButton("ФИЯИЛ"));
        innerKeyboard.get(4).add(new InnerKeyboardButton("КНИИТ"));
        innerKeyboard.get(4).add(new InnerKeyboardButton("ФНБМТ"));

        innerKeyboard.get(5).add(new InnerKeyboardButton("ФНП"));
        innerKeyboard.get(5).add(new InnerKeyboardButton("ПСИХОЛОГ"));
        innerKeyboard.get(5).add(new InnerKeyboardButton("ППИСО"));

        innerKeyboard.get(6).add(new InnerKeyboardButton("ФИЛОСОФ"));
        innerKeyboard.get(6).add(new InnerKeyboardButton("ЭКОНОМ"));
        innerKeyboard.get(6).add(new InnerKeyboardButton("ЮРФАК"));

        innerKeyboard.get(7).add(new InnerKeyboardButton("ГЕОЛОГ К-Ж"));
        innerKeyboard.get(7).add(new InnerKeyboardButton("К-Ж ЯБЛОЧКОВА"));

        innerKeyboard.get(8).add(MAIN_MENU_BUTTON);

        return innerKeyboard;
    }

    public InnerKeyboard confirmDeletion() {
        var innerKeyboard = new InnerKeyboard();
        for (var i = 0; i < 4; i++)
            innerKeyboard.add(new ArrayList<>());
        innerKeyboard.get(0).add(new InnerKeyboardButton(String.format("%1$s Да %1$s", YES_EMOJI)));
        innerKeyboard.get(1).add(new InnerKeyboardButton(String.format("%1$s Нет %1$s", NO_EMOJI)));
        innerKeyboard.get(2).add(SETTINGS_BUTTON);
        innerKeyboard.get(3).add(MAIN_MENU_BUTTON);
        
        return innerKeyboard;
    }

    public InnerKeyboard fullTimeSchedule() {
        var innerKeyboard = new InnerKeyboard();
        for (var i = 0; i < 6; i++)
            innerKeyboard.add(new ArrayList<>());
        innerKeyboard.get(0).add(TODAY_SCHEDULE_BUTTON);
        innerKeyboard.get(0).add(TOMORROW_SCHEDULE_BUTTON);
        innerKeyboard.get(0).add(YESTERDAY_SCHEDULE_BUTTON);

        innerKeyboard.get(1).add(new InnerKeyboardButton("Пн"));
        innerKeyboard.get(1).add(new InnerKeyboardButton("Вт"));
        innerKeyboard.get(1).add(new InnerKeyboardButton("Ср"));

        innerKeyboard.get(2).add(new InnerKeyboardButton("Чт"));
        innerKeyboard.get(2).add(new InnerKeyboardButton("Пт"));
        innerKeyboard.get(2).add(new InnerKeyboardButton("Сб"));

        innerKeyboard.get(3).add(new InnerKeyboardButton(String.format("%1$s Сессия %1$s", FIRE_EMOJI)));

        innerKeyboard.get(4).add(CHOOSE_DEPARTMENT_AND_GROUP_BUTTON);

        innerKeyboard.get(5).add(MAIN_MENU_BUTTON);
        
        return innerKeyboard;
    }

    public InnerKeyboard extramuralSchedule() {
        var innerKeyboard = new InnerKeyboard();
        for (var i = 0; i < 4; i++)
            innerKeyboard.add(new ArrayList<>());
        innerKeyboard.get(0).add(new InnerKeyboardButton("Все занятия"));

        innerKeyboard.get(1).add(TODAY_SCHEDULE_BUTTON);
        innerKeyboard.get(1).add(TOMORROW_SCHEDULE_BUTTON);
        innerKeyboard.get(1).add(YESTERDAY_SCHEDULE_BUTTON);

        innerKeyboard.get(2).add(CHOOSE_DEPARTMENT_AND_GROUP_BUTTON);

        innerKeyboard.get(3).add(MAIN_MENU_BUTTON);
        
        return innerKeyboard;
    }

    public InnerKeyboard teacherSchedule() {
        var innerKeyboard = new InnerKeyboard();
        for (var i = 0; i < 6; i++)
            innerKeyboard.add(new ArrayList<>());
        innerKeyboard.get(0).add(TODAY_SCHEDULE_BUTTON);
        innerKeyboard.get(0).add(TOMORROW_SCHEDULE_BUTTON);
        innerKeyboard.get(0).add(YESTERDAY_SCHEDULE_BUTTON);

        innerKeyboard.get(1).add(new InnerKeyboardButton("Пн"));
        innerKeyboard.get(1).add(new InnerKeyboardButton("Вт"));
        innerKeyboard.get(1).add(new InnerKeyboardButton("Ср"));

        innerKeyboard.get(2).add(new InnerKeyboardButton("Чт"));
        innerKeyboard.get(2).add(new InnerKeyboardButton("Пт"));
        innerKeyboard.get(2).add(new InnerKeyboardButton("Сб"));

        innerKeyboard.get(3).add(new InnerKeyboardButton(String.format("%1$s Сессия %1$s", FIRE_EMOJI)));

        innerKeyboard.get(4).add(new InnerKeyboardButton("Занятия заочников"));

        innerKeyboard.get(5).add(MAIN_MENU_BUTTON);
        
        return innerKeyboard;
    }

    public InnerKeyboard hours() {
        var innerKeyboard = new InnerKeyboard();
        int hour = 1;
        for (var i = 0; i < 6; i++) {
            innerKeyboard.add(new ArrayList<>());
            for (int k = 0; k < 4; k++) {
                innerKeyboard.get(i).add(new InnerKeyboardButton(hour + " ч"));
                hour++;
            }
        }
        innerKeyboard.get(5).set(3, new InnerKeyboardButton("0 ч"));

        innerKeyboard.add(new ArrayList<>());
        innerKeyboard.add(new ArrayList<>());
        innerKeyboard.get(6).add(SETTINGS_BUTTON);
        innerKeyboard.get(7).add(MAIN_MENU_BUTTON);
        
        return innerKeyboard;
    }

    public InnerKeyboard groupNumbers(List<String> groupNumbers) {
        var innerKeyboard = new InnerKeyboard();
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
            innerKeyboard.get(row).add(new InnerKeyboardButton("Г " + groupNumbers.get(i)));
            i++;
        }

        innerKeyboard.add(new ArrayList<>());
        innerKeyboard.get(innerKeyboard.getButtons().size() - 1).add(MAIN_MENU_BUTTON);
        
        return innerKeyboard;
    }

    public InnerKeyboard teachers(List<Teacher> teachers) {
        var innerKeyboard = new InnerKeyboard();
        innerKeyboard.add(new ArrayList<>());
        var i = 0;
        int row = 0;

        while (i < teachers.size()) {
            if (i % 5 == 4) {
                row++;
                innerKeyboard.add(new ArrayList<>());
            }
            String teacher = teachers.get(i).getId() + " "
                    + teachers.get(i).getSurname() + " "
                    + teachers.get(i).getName() + " "
                    + teachers.get(i).getPatronymic();
            teacher = teacher.length() > MAX_KEYBOARD_TEXT_LENGTH ? teacher.substring(0, MAX_KEYBOARD_TEXT_LENGTH) : teacher;
            innerKeyboard.get(row).add(new InnerKeyboardButton(teacher));
            i++;
        }

        innerKeyboard.add(new ArrayList<>());
        innerKeyboard.get(innerKeyboard.getButtons().size() - 1).add(MAIN_MENU_BUTTON);
        
        return innerKeyboard;
    }

    public InnerKeyboard settingsExamNotification(InnerBotUser botUser) {
        var innerKeyboard = new InnerKeyboard();
        for (var i = 0; i < 8; i++) {
            innerKeyboard.add(new ArrayList<>());
        }

        String userId = botUser.getUserId();
        BotUserSource source = botUser.getSource();

        innerKeyboard.get(0).add(new InnerKeyboardButton("Уст. время рассылки сессии на сегодня"));
        innerKeyboard.get(2).add(new InnerKeyboardButton("Уст. время рассылки сессии на завтра"));
        innerKeyboard.get(4).add(new InnerKeyboardButton("Уст. время рас-ки сессии на послезавтра"));
        innerKeyboard.get(6).add(SETTINGS_BUTTON);
        innerKeyboard.get(7).add(MAIN_MENU_BUTTON);

        if (InnerBotUser.isBotUserFullTime(botUser)) {
            ExamPeriodTodayNotification examPeriodTodayNotification = examPeriodTodayNotificationRepository.findByUserIdAndUserSource(userId, source);
            ExamPeriodTomorrowNotification examPeriodTomorrowNotification = examPeriodTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);
            ExamPeriodAfterTomorrowNotification examPeriodAfterTomorrowNotification = examPeriodAfterTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);

            if (null != examPeriodTodayNotification && examPeriodTodayNotification.isEnabled()) {
                innerKeyboard.get(1).add(new InnerKeyboardButton(String.format("%1$s Выкл. рассылку сессии на сегодня %1$s", NO_EMOJI)));
            } else {
                innerKeyboard.get(1).add(new InnerKeyboardButton(String.format("%1$s Вкл. рассылку сессии на сегодня %1$s", YES_EMOJI)));
            }

            if (null != examPeriodTomorrowNotification && examPeriodTomorrowNotification.isEnabled()) {
                innerKeyboard.get(3).add(new InnerKeyboardButton(String.format("%1$s Выкл. рассылку сессии на завтра %1$s", NO_EMOJI)));
            } else {
                innerKeyboard.get(3).add(new InnerKeyboardButton(String.format("%1$s Вкл. рассылку сессии на завтра %1$s", YES_EMOJI)));
            }

            if (null != examPeriodAfterTomorrowNotification && examPeriodAfterTomorrowNotification.isEnabled()) {
                innerKeyboard.get(5).add(new InnerKeyboardButton(String.format("%1$s Выкл. рассылку сессии на послезавтра %1$s", NO_EMOJI)));
            } else {
                innerKeyboard.get(5).add(new InnerKeyboardButton(String.format("%1$s Вкл. рассылку сессии на послезавтра %1$s", YES_EMOJI)));
            }
        }

        if (InnerBotUser.isBotUserExtramural(botUser)) {
            ExtramuralEventTodayNotification extramuralEventTodayNotification = extramuralEventTodayNotificationRepository.findByUserIdAndUserSource(userId, source);
            ExtramuralEventTomorrowNotification extramuralEventTomorrowNotification = extramuralEventTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);
            ExtramuralEventAfterTomorrowNotification extramuralEventAfterTomorrowNotification = extramuralEventAfterTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);

            if (null != extramuralEventTodayNotification && extramuralEventTodayNotification.isEnabled()) {
                innerKeyboard.get(1).add(new InnerKeyboardButton(String.format("%1$s Выкл. рассылку сессии на сегодня %1$s", NO_EMOJI)));
            } else {
                innerKeyboard.get(1).add(new InnerKeyboardButton(String.format("%1$s Вкл. рассылку сессии на сегодня %1$s", YES_EMOJI)));
            }

            if (null != extramuralEventTomorrowNotification && extramuralEventTomorrowNotification.isEnabled()) {
                innerKeyboard.get(3).add(new InnerKeyboardButton(String.format("%1$s Выкл. рассылку сессии на завтра %1$s", NO_EMOJI)));
            } else {
                innerKeyboard.get(3).add(new InnerKeyboardButton(String.format("%1$s Вкл. рассылку сессии на завтра %1$s", YES_EMOJI)));
            }

            if (null != extramuralEventAfterTomorrowNotification && extramuralEventAfterTomorrowNotification.isEnabled()) {
                innerKeyboard.get(5).add(new InnerKeyboardButton(String.format("%1$s Выкл. рассылку сессии на послезавтра %1$s", NO_EMOJI)));
            } else {
                innerKeyboard.get(5).add(new InnerKeyboardButton(String.format("%1$s Вкл. рассылку сессии на послезавтра %1$s", YES_EMOJI)));
            }
        }

        return innerKeyboard;
    }

    public InnerKeyboard settingsScheduleNotification(InnerBotUser botUser) {
        var innerKeyboard = new InnerKeyboard();
        for (var i = 0; i < 6; i++) {
            innerKeyboard.add(new ArrayList<>());
        }

        String userId = botUser.getUserId();
        BotUserSource source = botUser.getSource();

        ScheduleTodayNotification scheduleTodayNotification = scheduleTodayNotificationRepository.findByUserIdAndUserSource(userId, source);
        ScheduleTomorrowNotification scheduleTomorrowNotification = scheduleTomorrowNotificationRepository.findByUserIdAndUserSource(userId, source);

        innerKeyboard.get(0).add(new InnerKeyboardButton("Уст. время рассылки расп-я на сегодня"));

        if (null != scheduleTodayNotification && scheduleTodayNotification.isEnabled()) {
            innerKeyboard.get(1).add(new InnerKeyboardButton(String.format("%1$s Выкл. рассылку расп-я на сегодня %1$s", NO_EMOJI)));
        } else {
            innerKeyboard.get(1).add(new InnerKeyboardButton(String.format("%1$s Вкл. рассылку расп-я на сегодня %1$s", YES_EMOJI)));
        }

        innerKeyboard.get(2).add(new InnerKeyboardButton("Уст. время рассылки расп-я на завтра"));

        if (null != scheduleTomorrowNotification && scheduleTomorrowNotification.isEnabled()) {
            innerKeyboard.get(3).add(new InnerKeyboardButton(String.format("%1$s Выкл. рассылку расп-я на завтра %1$s", NO_EMOJI)));
        } else {
            innerKeyboard.get(3).add(new InnerKeyboardButton(String.format("%1$s Вкл. рассылку расп-я на завтра %1$s", YES_EMOJI)));
        }

        innerKeyboard.get(4).add(SETTINGS_BUTTON);
        innerKeyboard.get(5).add(MAIN_MENU_BUTTON);

        
        return innerKeyboard;
    }

    public InnerKeyboard settings(InnerBotUser user) {
        var innerKeyboard = new InnerKeyboard();
        for (var i = 0; i < 8; i++) {
            innerKeyboard.add(new ArrayList<>());
        }

        // 0 row is checked in the end

        innerKeyboard.get(1).add(new InnerKeyboardButton(String.format("%1$s Рассылка расписания сессии %1$s", NOTIFICATION_EMOJI)));

        if (user.isSilentEmptyDays()) {
            innerKeyboard.get(3).add(new InnerKeyboardButton(String.format("%1$s Присылать рассылку, когда пар нет %1$s", YES_EMOJI)));
        } else {
            innerKeyboard.get(3).add(new InnerKeyboardButton(String.format("%1$s Не присылать рассылку, когда пар нет %1$s", NO_EMOJI)));
        }

        if (user.isSentKeyboard()) {
            innerKeyboard.get(4).add(new InnerKeyboardButton(String.format("%1$s Не присылать клавиатуру %1$s", NO_EMOJI)));
        } else {
            innerKeyboard.get(4).add(new InnerKeyboardButton(String.format("%1$s Присылать клавиатуру %1$s", YES_EMOJI)));
        }

        innerKeyboard.get(5).add(CHOOSE_DEPARTMENT_AND_GROUP_BUTTON);
        innerKeyboard.get(5).add(new InnerKeyboardButton(String.format("%1$s Текущие настройки пользователя %1$s", PROFILE_EMOJI)));
        innerKeyboard.get(6).add(new InnerKeyboardButton(String.format("%1$s Удалить профиль %1$s", DELETE_PROFILE_EMOJI)));
        innerKeyboard.get(7).add(MAIN_MENU_BUTTON);

        if (!InnerBotUser.isBotUserExtramural(user)) {
            innerKeyboard.get(0).add(new InnerKeyboardButton(String.format("%1$s Рассылка расписания занятий %1$s", NOTIFICATION_EMOJI)));

            if (user.isFilterNomDenom()) {
                innerKeyboard.get(2).add(new InnerKeyboardButton(String.format("%1$s Выкл. фильтр по типу недели %1$s", NO_EMOJI)));
            } else {
                innerKeyboard.get(2).add(new InnerKeyboardButton(String.format("%1$s Вкл. фильтр по типу недели %1$s", YES_EMOJI)));
            }
        } else {
            innerKeyboard.remove(2); // week type filter
            innerKeyboard.remove(0); // schedule notifications
        }
        
        return innerKeyboard;
    }
}
