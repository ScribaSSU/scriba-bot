package com.scribassu.scribabot.services.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scribassu.scribabot.dto.BotMessage;
import com.scribassu.scribabot.dto.rest.TeacherListDto;
import com.scribassu.scribabot.dto.vkkeyboard.*;
import com.scribassu.scribabot.entities.BotUser;
import com.scribassu.scribabot.repositories.BotUserRepository;
import com.scribassu.scribabot.services.CallRestService;
import com.scribassu.scribabot.text.CommandText;
import com.scribassu.scribabot.util.Constants;
import com.scribassu.tracto.domain.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.scribassu.scribabot.keyboard.KeyboardType.ButtonActions;
import static com.scribassu.scribabot.keyboard.KeyboardType.ButtonFullTimeSchedule;
import static com.scribassu.scribabot.text.MessageText.CANNOT_GET_TEACHERS;

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
    public BotMessage getBotMessage(String message, BotUser botUser) {
        BotMessage botMessage = new BotMessage();

        if(null != botUser
                && null != botUser.getPreviousUserMessage()
                && botUser.getPreviousUserMessage().equalsIgnoreCase(CommandText.TEACHER_SCHEDULE)) {
            TeacherListDto teacherListDto = callRestService.getTeachersByWord(message);
            if(null != teacherListDto.getTeachers()) {
                if(teacherListDto.getTeachers().isEmpty()) {
                    botMessage = new BotMessage("По вашему запросу ничего не нашлось.", ButtonActions);
                }
                else {
                    List<Teacher> teachers = teacherListDto.getTeachers();
                    if(teachers.size() > Constants.MAX_VK_KEYBOARD_SIZE_FOR_LISTS) {
                        botMessage = new BotMessage(
                                "Искомый список преподавателей слишком большой для клавиатуры VK. " +
                                        "Попробуйте запросить точнее.",
                                ButtonActions);
                    }
                    else {
                        ObjectMapper objectMapper = new ObjectMapper();
                        try {
                            botMessage = new BotMessage(
                                    "Выберите, для какого преподавателя хотите узнать расписание.",
                                    objectMapper.writeValueAsString(buildVkKeyboardFromTeachers(teachers)));
                        }
                        catch(Exception e) {
                            botMessage = new BotMessage(CANNOT_GET_TEACHERS, ButtonActions);
                        }
                    }
                }
            }
            else {
                botMessage = new BotMessage(CANNOT_GET_TEACHERS, ButtonActions);
            }
        }

        if(message.equals(CommandText.TEACHER_SCHEDULE)) {
            botMessage = new BotMessage(
                    "Введите полностью или частично что-либо из ФИО преподавателя. " +
                    "Например, по запросу 'Ива' найдутся и 'Иванова', и 'Иван', и 'Иванович'. " +
                    "По запросу 'Иванов Ев' найдется 'Иванов Евгений', но не 'Иванова Евгения'.");
            botUser.setPreviousUserMessage(CommandText.TEACHER_SCHEDULE);
            botUserRepository.save(botUser);
        }

        if(message.startsWith(CommandText.TEACHER_ID_PAYLOAD)) {
            botUser.setPreviousUserMessage(message);
            botUserRepository.save(botUser);
            botMessage = new BotMessage(
                    "Выберите, для чего хотите узнать расписание преподавателя.",
                    ButtonFullTimeSchedule);
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
