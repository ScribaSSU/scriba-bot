package com.scribassu.scribabot.text;

import java.util.regex.Pattern;

public class CommandText {
    public static final String HELLO = "привет";
    public static final String HELP = "справка";
    public static final String FULL_TIME_SCHEDULE = "расписание студентов";
    public static final String EXTRAMURAL_SCHEDULE = "расписание заочного отделения";
    public static final String TEACHER_SCHEDULE = "расписание преподавателей";
    public static final String SETTINGS = "настройки";
    public static final String MAIN_MENU = "главное меню";
    public static final String SHORT_MAIN_MENU = "меню";
    public static final String THANKS = "спасибо";
    public static final String EXAMS = "сессия";
    public static final String CHOOSE_DEPARTMENT = "выбрать факультет и группу";
    public static final String CHOOSE_STUDENT_GROUP = "выбор группы";

    public static final String GROUP_NUMBER_INPUT = "г ";

    public static final String TODAY = "сегодня";
    public static final String TOMORROW = "завтра";
    public static final String YESTERDAY = "вчера";
    public static final String AFTER_TOMORROW = "послезавтра";

    public static final String MONDAY = "пн";
    public static final String TUESDAY = "вт";
    public static final String WEDNESDAY = "ср";
    public static final String THURSDAY = "чт";
    public static final String FRIDAY = "пт";
    public static final String SATURDAY = "сб";

    public static final String FULL_TIME = "дневная";
    public static final String EXTRAMURAL = "заочная";
    public static final String EVENING = "вечерняя";
    public static final String TEACHER = "преподаватель";

    public static final String SET_SEND_SCHEDULE_TIME_TODAY = "уст. время рассылки расп-я на сегодня";
    public static final String ENABLE_SEND_SCHEDULE_TODAY = "вкл. рассылку расп-я на сегодня";
    public static final String DISABLE_SEND_SCHEDULE_TODAY = "выкл. рассылку расп-я на сегодня";
    public static final String SET_SEND_SCHEDULE_TIME_TOMORROW = "уст. время рассылки расп-я на завтра";
    public static final String ENABLE_SEND_SCHEDULE_TOMORROW = "вкл. рассылку расп-я на завтра";
    public static final String DISABLE_SEND_SCHEDULE_TOMORROW = "выкл. рассылку расп-я на завтра";

    public static final String SET_SEND_EXAM_PERIOD_TIME_TODAY = "уст. время рассылки сессии на сегодня";
    public static final String ENABLE_SEND_EXAM_PERIOD_TODAY = "вкл. рассылку сессии на сегодня";
    public static final String DISABLE_SEND_EXAM_PERIOD_TODAY = "выкл. рассылку сессии на сегодня";
    public static final String SET_SEND_EXAM_PERIOD_TIME_TOMORROW = "уст. время рассылки сессии на завтра";
    public static final String ENABLE_SEND_EXAM_PERIOD_TOMORROW = "вкл. рассылку сессии на завтра";
    public static final String DISABLE_SEND_EXAM_PERIOD_TOMORROW = "выкл. рассылку сессии на завтра";
    public static final String SET_SEND_EXAM_PERIOD_TIME_AFTER_TOMORROW = "уст. время рас-ки сессии на послезавтра";
    public static final String ENABLE_SEND_EXAM_PERIOD_AFTER_TOMORROW = "вкл. рассылку сессии на послезавтра";
    public static final String DISABLE_SEND_EXAM_PERIOD_AFTER_TOMORROW = "выкл. рассылку сессии на послезавтра";

    public static final String ENABLE_FILTER_WEEK_TYPE = "вкл. фильтр по типу недели";
    public static final String DISABLE_FILTER_WEEK_TYPE = "выкл. фильтр по типу недели";
    public static final String CURRENT_USER_SETTINGS = "текущие настройки пользователя";
    public static final String SEND_EXAM_PERIOD = "рассылка расписания сессии";
    public static final String SEND_SCHEDULE = "рассылка расписания занятий";

    private static final String HOUR = "^\\d*\\s*ч$";
    private static final String COURSE = "\\d{1}\\s*курс";
    private static final String DEPARTMENT = "биолог|географ|геолог|идпо|и-т искусств|ииимо" +
            "|и-т физ кул|ифиж|и-т химии|мехмат|фияил|книит|соцфак" +
            "|фнбмт|фнп|психолог|пписо|физфак|философ|эконом|юрфак|геолог к-ж|к-ж яблочкова";

    public static final Pattern HOUR_PATTERN = Pattern.compile(HOUR);
    public static final Pattern COURSE_PATTERN = Pattern.compile(COURSE);
    public static final Pattern DEPARTMENT_PATTERN = Pattern.compile(DEPARTMENT);

    public static final String DEPARTMENT_PAYLOAD = "dep";
    public static final String COURSE_PAYLOAD = "course";
    public static final String TEACHER_ID_PAYLOAD = "teacher id";
}
