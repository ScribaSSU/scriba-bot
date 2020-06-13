package com.scribassu.scribabot.text;

import java.util.regex.Pattern;

public class CommandText {
    public static final String HELLO = "привет";
    public static final String HELP = "справка";
    public static final String SCHEDULE = "расписание";
    public static final String SETTINGS = "настройки";
    public static final String MAIN_MENU = "главное меню";
    public static final String SHORT_MAIN_MENU = "меню";
    public static final String THANKS = "спасибо";

    public static final String LESSONS = "занятия";
    public static final String EXAMS = "сессия";
    public static final String CHOOSE_DEPARTMENT = "выбрать факультет и т. д.";
    public static final String CHOOSE_STUDENT_GROUP = "выбор группы";

    public static final String GROUP_NUMBER_INPUT = "г ";

    public static final String TODAY = "сегодня";
    public static final String TOMORROW = "завтра";
    public static final String YESTERDAY = "вчера";

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
    public static final String ENABLE_FILTER_WEEK_TYPE = "вкл. фильтр по типу недели";
    public static final String DISABLE_FILTER_WEEK_TYPE = "выкл. фильтр по типу недели";
    public static final String CURRENT_USER_SETTINGS = "текущие настройки пользователя";

    private static final String DEPARTMENT = "биолог\\.|географ\\.|геолог\\.|идпо|и-т искусств|ииимо" +
            "|и-т физ\\. кул\\.|ифиж|и-т химии|мехмат|фияил|книит|соцфак" +
            "|фнбмт|фнп|психолог\\.|пписо|физфак|философ\\.|эконом\\.|юрфак";
    private static final String HOUR = "^\\d*\\s*ч$";
    private static final String COURSE = "\\d{1}\\s*курс";

    public static final Pattern DEPARTMENT_PATTERN = Pattern.compile(DEPARTMENT);
    public static final Pattern HOUR_PATTERN = Pattern.compile(HOUR);
    public static final Pattern COURSE_PATTERN = Pattern.compile(COURSE);

    public static final String COURSE_PAYLOAD = "{\"button\": \"курс\"}";
}
