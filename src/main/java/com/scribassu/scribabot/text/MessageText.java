package com.scribassu.scribabot.text;

public class MessageText {
    public static final String DEFAULT_MESSAGE = "Сообщение не распознано или недостаточно данных :( Зато это сообщение увидит админ и поможет.";
    public static final String GREETING_WITH_CHOOSE_DEPARTMENT = "Привет! Для начала работы с ботом нужно выбрать факультет и группу. Если клавиатура для выбора факультета не пришла, напишите сюда же любое сообщение, чтобы оповестить админа, админ увидит и поможет.\n\n" +
            "Также вы можете ввести факультет вручную: биолог, географ, геолог, идпо, и-т искусств, ииимо, и-т физики, и-т физ кул, ифиж, и-т химии, мехмат, фияил, книит, соцфак, фнбмт, фнп, психолог, пписо, философ, эконом, юрфак, геолог к-ж, к-ж яблочкова";
    public static final String GREETING_WITH_PROMPT = "Для начала работы с ботом напишите 'Привет', чтобы настроить факультет и группу.";
    public static final String GREETING = "Привет!";
    public static final String YOU_ARE_WELCOME = "Пожалуйста :)";
    public static final String RETURN_MAIN_MENU = "Возврат в главное меню.";
    public static final String SETTINGS_MENU = "Здесь вы можете настроить бота под себя.";
    public static final String CHOOSE_WANTED_SCHEDULE = "Выберите, для чего хотите узнать расписание.";
    public static final String THIS_IS_MAIN_MENU = "Это главное меню бота. Отсюда вы можете узнать расписание, задать настройки и не только.";
    public static final String CHOOSE_DEPARTMENT = "Выберите факультет. Если клавиатура для выбора факультета не пришла, напишите сюда же любое сообщение, чтобы оповестить админа, админ увидит и поможет.\n\n" +
            "Также вы можете ввести факультет вручную: биолог, географ, геолог, идпо, и-т искусств, ииимо, и-т физики, и-т физ кул, ифиж, и-т химии, мехмат, фияил, книит, соцфак, фнбмт, фнп, психолог, пписо, философ, эконом, юрфак, геолог к-ж, к-ж яблочкова";
    public static final String CHOOSE_EDUCATION_FORM = "Выберите форму расписания. Также вы можете ввести форму расписания вручную. Доступные варианты: дневная.";
    public static final String CHOOSE_COURSE = "Выберите курс. В магистратуре и аспирантуре курсы тоже отсчитываются с первого! Также вы можете ввести курс вручную в формате 'номеркурса курс', без кавычек. Например, 1 курс.";
    public static final String CHOOSE_STUDENT_GROUP = "Выберите группу. Также вы можете ввести номер группы вручную в формате 'г номергруппы', без кавычек. Например, г 123.";
    public static final String CHOOSE_SCHEDULE_NOTIFICATION_TIME = "Выберите удобное время для рассылки расписания на %s.";
    public static final String SCHEDULE_WILL_BE_SENT = "Теперь расписание на %s будет приходить в ";
    public static final String SCHEDULE_IS_DISABLED = "Рассылка расписания на %s выключена.";
    public static final String CHOOSE_EXAM_PERIOD_NOTIFICATION_TIME = "Выберите удобное время для рассылки сессии на %s.";
    public static final String EXAM_PERIOD_WILL_BE_SENT = "Теперь расписание сессии на %s будет приходить в ";
    public static final String EXAM_PERIOD_IS_DISABLED = "Рассылка расписания сессии на %s выключена.";
    public static final String CANNOT_GET_TEACHERS = "Не удалось получить список преподавателей.";
    public static final String FINISH_SET_GROUP = "Отлично, вы настроили свою группу. Теперь вы можете узнавать расписание.";
    public static final String CANNOT_GET_SCHEDULE_GROUP_NOT_SET = "Извините, вы недонастроили группу или ваша форма обучения пока не поддерживается :( Перенастройте, пожалуйста. Если вы вечерник, тогда ждите, работа с вашими расписаниями в разработке.";
    public static final String DELETE_CONFIRMATION = "Текущее действие удалит ваши данные из базы Скрибы, но не сам диалог. Если вы захотите снова пользоваться Скрибой, напишите \"Привет\". Вы точно хотите удалить профиль?";
    public static final String BYE_MESSAGE = "Надеюсь, Скриба была полезной. До новых встреч!";
    public static final String NOT_BYE_MESSAGE = "Спасибо, что остаётесь с нами!";
    public static final String HERE_EXAM_PERIOD_NOTIFICATION = "Здесь вы можете настроить рассылку расписания сессии.";
    public static final String HERE_SCHEDULE_NOTIFICATION = "Здесь вы можете настроить рассылку расписания занятий.";
    public static final String ENABLE_FILTER_WEEK_TYPE = "Включена фильтрация по типу недели.";
    public static final String DISABLE_FILTER_WEEK_TYPE = "Выключена фильтрация по типу недели.";
    public static final String SCHEDULE_WILL_BE_SENT_ABSTRACT = "Теперь расписание будет приходить в ";
    public static final String CHOOSE_TEACHER_TO_GET_SCHEDULE = "Выберите, для какого преподавателя хотите узнать расписание.";
    public static final String CHOOSE_DAY_FOR_TEACHER_SCHEDULE = "Выберите, для чего хотите узнать расписание преподавателя. Для сброса поиска выберите на клавиатуре 'Главное меню' или введите вручную 'меню'.";
    public static final String TOO_LONG_TEACHER_LIST = "Искомый список преподавателей слишком большой для клавиатуры. Попробуйте запросить точнее.";
    public static final String EMPTY_TEACHER_LIST = "По вашему запросу ничего не нашлось.";
    public static final String TEACHER_NAME_PROMPT = "Введите полностью или частично что-либо из ФИО преподавателя. " +
            "Например, по запросу 'Ива' найдутся и 'Иванова', и 'Иван', и 'Иванович'. " +
            "По запросу 'Иванов Ев' найдется 'Иванов Евгений', но не 'Иванова Евгения'.";
    public static final String UNSUPPORTED_LESSONS = "Ваш вид расписания пока не поддерживается или вы указали недостаточно информации для выдачи расписания.";
    public static final String NO_EXAM_PERIOD_SCHEDULE = "Расписание сессии для вашей группы отсутствует.";
    public static final String NO_EXAM_PERIOD_SCHEDULE_TEACHER = "Расписание сессии для этого преподавателя отсутствует.";

    public static final String NO_LESSONS = "А пар-то нету :)";

    public static final String NOT_ENABLE_SCHEDULE_NOTIFICATION_TODAY = "Вы еще не подключали рассылку расписания на сегодня. Подключите через '";
    public static final String NOT_ENABLE_EXAM_PERIOD_NOTIFICATION_TODAY = "Вы еще не подключали рассылку расписания сессии на сегодня. Подключите через '";
    public static final String NOT_ENABLE_SCHEDULE_NOTIFICATION_TOMORROW = "Вы еще не подключали рассылку расписания на завтра. Подключите через '";
    public static final String NOT_ENABLE_EXAM_PERIOD_NOTIFICATION_TOMORROW = "Вы еще не подключали рассылку расписания сессии на завтра. Подключите через '";
    public static final String NOT_ENABLE_EXAM_PERIOD_NOTIFICATION_AFTER_TOMORROW = "Вы еще не подключали рассылку расписания сессии на послезавтра. Подключите через '";
    public static final String H_DOT = " ч.";
    public static final String QUOTE_DOT = "'.";

    public static final String NOT_ENABLE_SCHEDULE_NOTIFICATION_TODAY_FRMTD = NOT_ENABLE_SCHEDULE_NOTIFICATION_TODAY + CommandText.SET_SEND_SCHEDULE_TIME_TODAY + QUOTE_DOT;
    public static final String NOT_ENABLE_EXAM_PERIOD_NOTIFICATION_TODAY_FRMTD = NOT_ENABLE_EXAM_PERIOD_NOTIFICATION_TODAY + CommandText.SET_SEND_EXAM_PERIOD_TIME_TODAY + QUOTE_DOT;
    public static final String NOT_ENABLE_SCHEDULE_NOTIFICATION_TOMORROW_FRMTD = NOT_ENABLE_SCHEDULE_NOTIFICATION_TOMORROW + CommandText.SET_SEND_SCHEDULE_TIME_TOMORROW + QUOTE_DOT;
    public static final String NOT_ENABLE_EXAM_PERIOD_NOTIFICATION_TOMORROW_FRMTD = NOT_ENABLE_EXAM_PERIOD_NOTIFICATION_TOMORROW + CommandText.SET_SEND_EXAM_PERIOD_TIME_TOMORROW + QUOTE_DOT;
    public static final String NOT_ENABLE_EXAM_PERIOD_NOTIFICATION_AFTER_TOMORROW_FRMTD = NOT_ENABLE_EXAM_PERIOD_NOTIFICATION_AFTER_TOMORROW + CommandText.SET_SEND_EXAM_PERIOD_TIME_AFTER_TOMORROW + QUOTE_DOT;

    public static final String TODAY = "сегодня";
    public static final String TOMORROW = "завтра";
    public static final String AFTER_TOMORROW = "послезавтра";

    public static final String CHOOSE_SCHEDULE_NOTIFICATION_TIME_TODAY = String.format(CHOOSE_SCHEDULE_NOTIFICATION_TIME, TODAY);
    public static final String CHOOSE_SCHEDULE_NOTIFICATION_TIME_TOMORROW = String.format(CHOOSE_SCHEDULE_NOTIFICATION_TIME, TOMORROW);
    public static final String CHOOSE_EXAM_PERIOD_NOTIFICATION_TIME_TODAY = String.format(CHOOSE_EXAM_PERIOD_NOTIFICATION_TIME, TODAY);
    public static final String CHOOSE_EXAM_PERIOD_NOTIFICATION_TIME_TOMORROW = String.format(CHOOSE_EXAM_PERIOD_NOTIFICATION_TIME, TOMORROW);
    public static final String CHOOSE_EXAM_PERIOD_NOTIFICATION_TIME_AFTER_TOMORROW = String.format(CHOOSE_EXAM_PERIOD_NOTIFICATION_TIME, AFTER_TOMORROW);

    public static final String DO_NOT_SEND = "DO NOT SEND";
}
