package com.scribassu.scribabot.text;

public class MessageText {
    public static final String DEFAULT_MESSAGE = "Сообщение не распознано или недостаточно данных :( Зато это сообщение увидит админ и поможет.";
    public static final String GREETING_WITH_CHOOSE_DEPARTMENT = "Привет! Для начала работы с ботом нужно выбрать факультет и группу. Если клавиатура для выбора факультета не пришла, напишите сюда же любое сообщение, чтобы оповестить админа, админ увидит и поможет.\n\n" +
            "Также вы можете ввести факультет вручную: биолог, географ, геолог, идпо, и-т искусств, ииимо, и-т физ кул, ифиж, и-т химии, мехмат, фияил, книит, соцфак, фнбмт, фнп, психолог, пписо, физфак, философ, эконом, юрфак, геолог к-ж, к-ж яблочкова";
    public static final String THIS_IS_MAIN_MENU = "Это главное меню бота. Отсюда вы можете узнать расписание, задать настройки и не только.";
    public static final String CHOOSE_DEPARTMENT = "Выберите факультет. Если клавиатура для выбора факультета не пришла, напишите сюда же любое сообщение, чтобы оповестить админа, админ увидит и поможет.\n\n" +
            "Также вы можете ввести факультет вручную: биолог, географ, геолог, идпо, и-т искусств, ииимо, и-т физ кул, ифиж, и-т химии, мехмат, фияил, книит, соцфак, фнбмт, фнп, психолог, пписо, физфак, философ, эконом, юрфак, геолог к-ж, к-ж яблочкова";
    public static final String CHOOSE_COURSE = "Выберите курс. В магистратуре и аспирантуре курсы тоже отсчитываются с первого!";
    public static final String CHOOSE_STUDENT_GROUP = "Введите номер группы в формате 'г номергруппы'. Буква 'г' и пробел после нее обязательны! Например, г 123";
    public static final String CHOOSE_SCHEDULE_NOTIFICATION_TIME = "Выберите удобное время для рассылки расписания на %s.";
    public static final String SCHEDULE_WILL_BE_SENT = "Теперь расписание на %s будет приходить в ";
    public static final String SCHEDULE_IS_ENABLED_DOUBLE = "Рассылка расписания на %s уже включена.";
    public static final String SCHEDULE_IS_DISABLED_DOUBLE = "Рассылка расписания на %s уже выключена.";
    public static final String SCHEDULE_IS_DISABLED = "Рассылка расписания на %s выключена.";
    public static final String CHOOSE_EXAM_PERIOD_NOTIFICATION_TIME = "Выберите удобное время для рассылки сессии на %s.";
    public static final String EXAM_PERIOD_WILL_BE_SENT = "Теперь расписание сессии на %s будет приходить в ";
    public static final String EXAM_PERIOD_IS_ENABLED_DOUBLE = "Рассылка расписания сессии на %s уже включена.";
    public static final String EXAM_PERIOD_IS_DISABLED_DOUBLE = "Рассылка расписания сессии на %s уже выключена.";
    public static final String EXAM_PERIOD_IS_DISABLED = "Рассылка расписания сессии на %s выключена.";
    public static final String CANNOT_GET_TEACHERS = "Не удалось получить список преподавателей.";
    public static final String FINISH_SET_GROUP = "Отлично, вы настроили свою группу. Теперь вы можете узнавать расписание.";
    public static final String CANNOT_GET_SCHEDULE_GROUP_NOT_SET = "Извините, ваша форма обучения пока не поддерживается, или вы недонастроили группу :( Перенастройте, пожалуйста. Если вы заочник или вечерник, тогда ждите, работа с вашими расписаниями в разработке.";

    public static final String DO_NOT_SEND = "DO NOT SEND";
}
