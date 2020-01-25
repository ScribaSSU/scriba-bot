package com.scribassu.scribabot.services.messages;

import com.scribassu.scribabot.commands.CommandText;
import com.scribassu.scribabot.entities.BotUser;
import com.scribassu.scribabot.keyboard.KeyboardMap;
import com.scribassu.scribabot.keyboard.KeyboardType;
import com.scribassu.scribabot.repositories.BotUserRepository;
import com.scribassu.scribabot.services.CallRestService;
import com.scribassu.scribabot.services.SymbolConverter;
import com.scribassu.scribabot.services.bot.HelpService;
import com.scribassu.scribabot.util.Constants;
import com.scribassu.scribabot.util.DepartmentConverter;
import com.scribassu.scribabot.util.Templates;
import com.scribassu.tracto.domain.EducationForm;
import com.scribassu.tracto.domain.FullTimeLesson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MessageHandlerImpl implements MessageHandler {

    private final SymbolConverter symbolConverter;
    private final CallRestService callRestService;
    private final HelpService helpService;
    private final BotUserRepository botUserRepository;

    @Autowired
    public MessageHandlerImpl(SymbolConverter symbolConverter,
                              CallRestService callRestService,
                              HelpService helpService,
                              BotUserRepository botUserRepository) {
        this.symbolConverter = symbolConverter;
        this.callRestService = callRestService;
        this.helpService = helpService;
        this.botUserRepository = botUserRepository;
    }

    private static final Calendar calendar = Calendar.getInstance();

    @Override
    public Map<String, String> getBotMessage(String message, String userId) {
        Map<String, String> botMessage = new HashMap<>();
        message = message.toLowerCase();
        System.out.println("Get message: " + message);

        BotUser botUser = botUserRepository.findOneById(userId);

        switch(message) {
            case CommandText.HELLO:
                if(botUser == null) {
                    botUserRepository.save(new BotUser(userId));
                }
                botMessage.put(
                        Constants.KEY_MESSAGE,
                        "Привет!");
                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonActions).getJsonText());
                break;
            case CommandText.MAIN_MENU:
            case CommandText.SHORT_MAIN_MENU:
                if(botUser == null) {
                    botUserRepository.save(new BotUser(userId));
                }
                botMessage.put(
                        Constants.KEY_MESSAGE,
                        "Возврат в главное меню");
                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonActions).getJsonText());
                break;
            case CommandText.HELP:
                botMessage.put(
                        Constants.KEY_MESSAGE,
                        helpService.getHelp());
                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonActions).getJsonText());
                break;
            case CommandText.SCHEDULE:
                botMessage.put(
                        Constants.KEY_MESSAGE,
                        "Хотите расписание пар или сессии?");
                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonSchedule).getJsonText());
                break;
            case CommandText.LESSONS:
                if(botUser != null && botUser.getEducationForm().equalsIgnoreCase(EducationForm.DO.getGroupType())) {
                    botMessage.put(
                            Constants.KEY_MESSAGE,
                            "Выберите день, для которого хотите узнать расписание");
                    botMessage.put(
                            Constants.KEY_KEYBOARD,
                            KeyboardMap.keyboards.get(KeyboardType.ButtonFullTimeSchedule).getJsonText());
                }
                else {
                    botMessage.put(
                            Constants.KEY_MESSAGE,
                            "Извините, ваша форма обучения пока не поддерживается");
                }
                break;
            case CommandText.CHOOSE_DEPARTMENT:
                botMessage.put(
                        Constants.KEY_MESSAGE,
                        "Выберите факультет");
                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonDepartment).getJsonText());
                break;
            case CommandText.FULL_TIME:
                botUserRepository.updateEducationForm(EducationForm.DO.getGroupType(), userId);
                botMessage.put(
                        Constants.KEY_MESSAGE,
                        "Введите номер группы в формате 'г номер_группы'");
                break;
            case CommandText.EXTRAMURAL:
                botUserRepository.updateEducationForm(EducationForm.ZO.getGroupType(), userId);
                botMessage.put(
                        Constants.KEY_MESSAGE,
                        "Введите номер группы в формате 'г номер_группы'");
                break;
            case CommandText.EVENING:
                botUserRepository.updateEducationForm(EducationForm.VO.getGroupType(), userId);
                botMessage.put(
                        Constants.KEY_MESSAGE,
                        "Введите номер группы в формате 'г номер_группы'");
                break;
            case CommandText.MONDAY:
                if(botUser != null && botUser.getEducationForm().equalsIgnoreCase(EducationForm.DO.getGroupType())) {
                    List<FullTimeLesson> lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            "1"
                    );
                    botMessage = getBotMessageForFullTimeLessons(lessons);
                }
                break;
            case CommandText.TUESDAY:
                if(botUser != null && botUser.getEducationForm().equalsIgnoreCase(EducationForm.DO.getGroupType())) {
                    List<FullTimeLesson> lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            "2"
                    );
                    botMessage = getBotMessageForFullTimeLessons(lessons);
                }
                break;
            case CommandText.WEDNESDAY:
                if(botUser != null && botUser.getEducationForm().equalsIgnoreCase(EducationForm.DO.getGroupType())) {
                    List<FullTimeLesson> lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            "3"
                    );
                    botMessage = getBotMessageForFullTimeLessons(lessons);
                }
                break;
            case CommandText.THURSDAY:
                if(botUser != null && botUser.getEducationForm().equalsIgnoreCase(EducationForm.DO.getGroupType())) {
                    List<FullTimeLesson> lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            "4"
                    );
                    botMessage = getBotMessageForFullTimeLessons(lessons);
                }
                break;
            case CommandText.FRIDAY:
                if(botUser != null && botUser.getEducationForm().equalsIgnoreCase(EducationForm.DO.getGroupType())) {
                    List<FullTimeLesson> lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            "5"
                    );
                    botMessage = getBotMessageForFullTimeLessons(lessons);
                }
                break;
            case CommandText.SATURDAY:
                if(botUser != null && botUser.getEducationForm().equalsIgnoreCase(EducationForm.DO.getGroupType())) {
                    List<FullTimeLesson> lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            "6"
                    );
                    botMessage = getBotMessageForFullTimeLessons(lessons);
                }
                break;
            case CommandText.TODAY:
                if(botUser != null && botUser.getEducationForm().equalsIgnoreCase(EducationForm.DO.getGroupType())) {
                    List<FullTimeLesson> lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            String.valueOf(calendar.get(Calendar.DAY_OF_WEEK))
                    );
                    botMessage = getBotMessageForFullTimeLessons(lessons);
                }
                break;
            case CommandText.TOMORROW:
                if(botUser != null && botUser.getEducationForm().equalsIgnoreCase(EducationForm.DO.getGroupType())) {
                    calendar.add(Calendar.DAY_OF_WEEK, 1);
                    List<FullTimeLesson> lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            String.valueOf(calendar.get(Calendar.DAY_OF_WEEK))
                    );
                    botMessage = getBotMessageForFullTimeLessons(lessons);
                    calendar.add(Calendar.DAY_OF_WEEK, -1);
                }
                break;
            case "т":
                botMessage.put(Constants.KEY_MESSAGE, "test");
                break;
        }

        if(CommandText.DEPARTMENT_PATTERN.matcher(message).matches()) {
            botUserRepository.updateDepartment(DepartmentConverter.convertToUrl(message), userId);
            botMessage.put(
                    Constants.KEY_MESSAGE,
                    "Выберите форму расписания");
            botMessage.put(
                    Constants.KEY_KEYBOARD,
                    KeyboardMap.keyboards.get(KeyboardType.ButtonGroupType).getJsonText()
            );
        }

        if(message.startsWith(CommandText.GROUP_NUMBER_INPUT)) {
            botUserRepository.updateGroupNumber(message.substring(2), userId);
            botMessage.put(
                    Constants.KEY_MESSAGE,
                    "Хотите расписание пар или сессии?");
            botMessage.put(
                    Constants.KEY_KEYBOARD,
                    KeyboardMap.keyboards.get(KeyboardType.ButtonSchedule).getJsonText()
            );
        }

        if(message.startsWith("р ")) {
            String[] params = message.split(" ");
            List<FullTimeLesson> lessons = callRestService.getFullTimeLessonsByDay(
                    params[1],
                    params[2],
                    params[3]
            );

            if(CollectionUtils.isEmpty(lessons)) {
                botMessage.put(Constants.KEY_MESSAGE, "Информация отсутствует.");
            }
            else {
                botMessage.put(Constants.KEY_MESSAGE, Templates.makeTemplate(lessons));
            }
        }

        if(botMessage.isEmpty()
                || !botMessage.containsKey(Constants.KEY_MESSAGE)
                && !botMessage.containsKey(Constants.KEY_KEYBOARD)) {
            botMessage.put(Constants.KEY_MESSAGE, "Сообщение не распознано или недостаточно данных.");
        }

        return botMessage;
    }

    private Map<String, String> getBotMessageForFullTimeLessons(List<FullTimeLesson> lessons) {
        Map<String, String> botMessage = new HashMap<>();
        if(CollectionUtils.isEmpty(lessons)) {
            botMessage.put(Constants.KEY_MESSAGE, "Информация отсутствует.");
        }
        else {
            botMessage.put(Constants.KEY_MESSAGE, Templates.makeTemplate(lessons));
        }
        return botMessage;
    }
}
