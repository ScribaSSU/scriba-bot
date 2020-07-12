package com.scribassu.scribabot.services.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scribassu.scribabot.dto.TeacherListDto;
import com.scribassu.scribabot.dto.vkkeyboard.*;
import com.scribassu.scribabot.entities.BotUser;
import com.scribassu.scribabot.keyboard.KeyboardMap;
import com.scribassu.scribabot.keyboard.KeyboardType;
import com.scribassu.scribabot.repositories.BotUserRepository;
import com.scribassu.scribabot.services.CallRestService;
import com.scribassu.scribabot.text.CommandText;
import com.scribassu.scribabot.util.Constants;
import com.scribassu.tracto.domain.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TeacherService implements BotMessageService {

    private final CallRestService callRestService;
    private final BotUserRepository botUserRepository;

    @Autowired
    public TeacherService(CallRestService callRestService,
                          BotUserRepository botUserRepository) {
        this.callRestService = callRestService;
        this.botUserRepository = botUserRepository;
    }

    @Override
    public Map<String, String> getBotMessage(String message, BotUser botUser) {
        Map<String, String> botMessage = new HashMap<>();

        if(null != botUser
                && null != botUser.getPreviousUserMessage()
                && botUser.getPreviousUserMessage().equalsIgnoreCase(CommandText.TEACHER_SCHEDULE)) {
            TeacherListDto teacherListDto = callRestService.getTeachersByWord(message);
            if(null != teacherListDto.getTeachers()) {
                if(teacherListDto.getTeachers().isEmpty()) {
                    botMessage.put(Constants.KEY_MESSAGE, "По вашему запросу ничего не нашлось.");
                    botMessage.put(
                            Constants.KEY_KEYBOARD,
                            KeyboardMap.keyboards.get(KeyboardType.ButtonActions).getJsonText());
                }
                else {
                    List<Teacher> teachers = teacherListDto.getTeachers();
                    if(teachers.size() > Constants.MAX_VK_KEYBOARD_SIZE_FOR_LISTS) {
                        botMessage.put(Constants.KEY_MESSAGE,
                                "Искомый список преподавателей слишком большой для клавиатуры VK. " +
                                        "Попробуйте запросить точнее.");
                        botMessage.put(
                                Constants.KEY_KEYBOARD,
                                KeyboardMap.keyboards.get(KeyboardType.ButtonActions).getJsonText());
                    }
                    else {
                        ObjectMapper objectMapper = new ObjectMapper();
                        try {
                            botMessage.put(Constants.KEY_MESSAGE, "Выберите, для какого преподавателя хотите узнать расписание.");
                            botMessage.put(
                                    Constants.KEY_KEYBOARD,
                                    objectMapper.writeValueAsString(buildVkKeyboardFromTeachers(teachers)));
                        }
                        catch(Exception e) {
                            botMessage.put(Constants.KEY_MESSAGE, "Не удалось получить список преподавателей.");
                            botMessage.put(
                                    Constants.KEY_KEYBOARD,
                                    KeyboardMap.keyboards.get(KeyboardType.ButtonActions).getJsonText());
                        }
                    }
                }
            }
            else {
                botMessage.put(Constants.KEY_MESSAGE, "Не удалось получить список преподавателей.");
                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonActions).getJsonText());
            }
        }

        if(message.equals(CommandText.TEACHER_SCHEDULE)) {
            botMessage.put(Constants.KEY_MESSAGE, "Введите полностью или частично что-либо из ФИО преподавателя. " +
                    "Например, по запросу 'Ива' найдутся и 'Иванова', и 'Иван', и 'Иванович'. " +
                    "По запросу 'Иванов Ев' найдется 'Иванов Евгений', но не 'Иванова Евгения'.");
            botUser.setPreviousUserMessage(CommandText.TEACHER_SCHEDULE);
            botUserRepository.save(botUser);
        }

        if(message.startsWith(CommandText.TEACHER_ID_PAYLOAD)) {
            botUser.setPreviousUserMessage(message);
            botUserRepository.save(botUser);
            botMessage.put(
                    Constants.KEY_MESSAGE,
                    "Выберите, для чего хотите узнать расписание преподавателя.");
            botMessage.put(
                    Constants.KEY_KEYBOARD,
                    KeyboardMap.keyboards.get(KeyboardType.ButtonFullTimeSchedule).getJsonText());
        }

        return botMessage;
    }

    private VkKeyboard buildVkKeyboardFromTeachers(List<Teacher> teachers) {
        List<List<VkKeyboardButton>> vkKeyboardButtons = new ArrayList<>();

        int i = 0;
        int row = 0;
        vkKeyboardButtons.add(new ArrayList<>());

        while(i < teachers.size()) {
            if(i % 5 == 4) {
                row++;
                vkKeyboardButtons.add(new ArrayList<>());
            }
            vkKeyboardButtons.get(row).add(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    teachers.get(i).getSurname() + " " + teachers.get(i).getName() + " " + teachers.get(i).getPatronymic(),
                                    String.format(Constants.PAYLOAD, CommandText.TEACHER_ID_PAYLOAD + " " + teachers.get(i).getId()),
                                    VkKeyboardButtonActionType.TEXT
                            ), VkKeyboardButtonColor.PRIMARY)
            );
            i++;
        }

        vkKeyboardButtons.add(new ArrayList<>());
        vkKeyboardButtons.get(vkKeyboardButtons.size() - 1).add(
                new VkKeyboardButton(
                        new VkKeyboardButtonActionText(
                                "Главное меню",
                                "",
                                VkKeyboardButtonActionType.TEXT
                        ), VkKeyboardButtonColor.POSITIVE)
        );

        return new VkKeyboard(vkKeyboardButtons, true);
    }
}
