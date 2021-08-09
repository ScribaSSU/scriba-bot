package com.scribassu.scribabot.keyboard;

import com.scribassu.scribabot.dto.vkkeyboard.*;
import com.scribassu.scribabot.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

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

    private static final String YES_EMOJI = "✅";
    private static final String NO_EMOJI = "❌";

    /*
    TODO:
    schedule fulltime
    schedule extramural
    departments
    group numbers
    settings
    teachers
    confirm deletion
    hours
    settings notification schedule
    settings notification exams

    DONE:
    main menu
    education forms
    courses
     */

    public static ReplyKeyboardMarkup mainMenu() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        for(int i = 0; i < 4; i++)
            rows.add(new KeyboardRow());
        //rows.get(0).add(new KeyboardButton("\uD83D\uDDD3 Расписание студентов \uD83D\uDDD3"));
        //rows.get(1).add(new KeyboardButton("\uD83C\uDF93 Расписание преподавателей \uD83C\uDF93"));
        rows.get(0).add(new KeyboardButton("\uD83C\uDF1A Расписание студентов \uD83C\uDF1A"));
        rows.get(1).add(new KeyboardButton("\uD83C\uDF1D Расписание преподавателей \uD83C\uDF1D"));
        rows.get(2).add(new KeyboardButton("⚙ Настройки ⚙"));
        rows.get(3).add(new KeyboardButton("❓ Справка ❓"));
        replyKeyboardMarkup.setKeyboard(rows);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup educationForms() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        for(int i = 0; i < 2; i++)
            rows.add(new KeyboardRow());
        rows.get(0).add(new KeyboardButton("Дневная"));
        rows.get(0).add(new KeyboardButton("Заочная"));
        rows.get(1).add(new KeyboardButton("Главное меню"));
        replyKeyboardMarkup.setKeyboard(rows);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup courses() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        for(int i = 0; i < 4; i++)
            rows.add(new KeyboardRow());
        rows.get(0).add(new KeyboardButton("1 курс"));
        rows.get(0).add(new KeyboardButton("2 курс"));
        rows.get(1).add(new KeyboardButton("3 курс"));
        rows.get(1).add(new KeyboardButton("4 курс"));
        rows.get(2).add(new KeyboardButton("5 курс"));
        rows.get(2).add(new KeyboardButton("Другое"));
        rows.get(3).add(new KeyboardButton("Главное меню"));
        replyKeyboardMarkup.setKeyboard(rows);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup departments() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        for(int i = 0; i < 9; i++)
            rows.add(new KeyboardRow());
        rows.get(0).add(new KeyboardButton("БИОЛОГ"));
        rows.get(0).add(new KeyboardButton("ГЕОГРАФ"));
        rows.get(0).add(new KeyboardButton("ГЕОЛОГ"));

        rows.get(1).add(new KeyboardButton("ИДПО"));
        rows.get(1).add(new KeyboardButton("И-Т ИСКУССТВ"));
        rows.get(1).add(new KeyboardButton("ИИИМО"));

        rows.get(2).add(new KeyboardButton("И-Т ФИЗ КУЛ"));
        rows.get(2).add(new KeyboardButton("ИФИЖ"));
        rows.get(2).add(new KeyboardButton("И-Т ХИМИИ"));

        rows.get(3).add(new KeyboardButton("МЕХМАТ"));
        rows.get(3).add(new KeyboardButton("СОЦФАК"));
        rows.get(3).add(new KeyboardButton("ФИЯИЛ"));

        rows.get(4).add(new KeyboardButton("КНИИТ"));
        rows.get(4).add(new KeyboardButton("ФНБМТ"));
        rows.get(4).add(new KeyboardButton("ФНП"));

        rows.get(5).add(new KeyboardButton("ПСИХОЛОГ"));
        rows.get(5).add(new KeyboardButton("ППИСО"));
        rows.get(5).add(new KeyboardButton("ФИЗФАК"));

        rows.get(6).add(new KeyboardButton("ФИЛОСОФ"));
        rows.get(6).add(new KeyboardButton("ЭКОНОМ"));
        rows.get(6).add(new KeyboardButton("ЮРФАК"));

        rows.get(7).add(new KeyboardButton("ГЕОЛОГ К-Ж"));
        rows.get(7).add(new KeyboardButton("К-Ж ЯБЛОЧКОВА"));

        rows.get(8).add(new KeyboardButton("Главное меню"));

        replyKeyboardMarkup.setKeyboard(rows);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup confirmDeletion() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        for(int i = 0; i < 2; i++)
            rows.add(new KeyboardRow());
        rows.get(0).add(new KeyboardButton(String.format("%1$s Да %1$s", YES_EMOJI)));
        rows.get(1).add(new KeyboardButton(String.format("%1$s Нет %1$s", NO_EMOJI)));
        replyKeyboardMarkup.setKeyboard(rows);
        return replyKeyboardMarkup;
    }

    private static ReplyKeyboardMarkup settings() {
        ReplyKeyboardMarkup r = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        for(int i = 0; i < 2; i++)
            rows.add(new KeyboardRow());
        rows.get(0).add(new KeyboardButton(String.format("%1$s включить %1$s", YES_EMOJI)));
        rows.get(1).add(new KeyboardButton("❌ выключить ❌"));
        r.setKeyboard(rows);
        return r;
    }
}
