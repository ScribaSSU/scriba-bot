package com.scribassu.scribabot.services.bot;

import com.scribassu.scribabot.dto.BotMessage;
import com.scribassu.scribabot.dto.InnerBotUser;
import com.scribassu.scribabot.dto.rest.TeacherListDto;
import com.scribassu.scribabot.entities.TgBotUser;
import com.scribassu.scribabot.entities.VkBotUser;
import com.scribassu.scribabot.keyboard.TgKeyboardGenerator;
import com.scribassu.scribabot.keyboard.VkKeyboardGenerator;
import com.scribassu.scribabot.repositories.TgBotUserRepository;
import com.scribassu.scribabot.repositories.VkBotUserRepository;
import com.scribassu.scribabot.services.CallRestService;
import com.scribassu.scribabot.text.CommandText;
import com.scribassu.scribabot.util.Constants;
import com.scribassu.tracto.domain.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.scribassu.scribabot.text.MessageText.CANNOT_GET_TEACHERS;

@Service
public class TeacherService implements BotMessageService {

    private final CallRestService callRestService;
    private final VkBotUserRepository vkBotUserRepository;
    private final TgBotUserRepository tgBotUserRepository;
    private final VkKeyboardGenerator vkKeyboardGenerator;
    private final TgKeyboardGenerator tgKeyboardGenerator;

    @Autowired
    public TeacherService(CallRestService callRestService,
                          VkBotUserRepository vkBotUserRepository,
                          TgBotUserRepository tgBotUserRepository,
                          VkKeyboardGenerator vkKeyboardGenerator,
                          TgKeyboardGenerator tgKeyboardGenerator) {
        this.callRestService = callRestService;
        this.vkBotUserRepository = vkBotUserRepository;
        this.tgBotUserRepository = tgBotUserRepository;
        this.vkKeyboardGenerator = vkKeyboardGenerator;
        this.tgKeyboardGenerator = tgKeyboardGenerator;
    }

    @Override
    public BotMessage getBotMessage(String message, InnerBotUser botUser) {
        BotMessage botMessage = new BotMessage();

        if (null != botUser
                && null != botUser.getPreviousUserMessage()
                && botUser.getPreviousUserMessage().equalsIgnoreCase(CommandText.TEACHER_SCHEDULE)) {
            TeacherListDto teacherListDto = callRestService.getTeachersByWord(message);
            if (null != teacherListDto.getTeachers()) {
                if (teacherListDto.getTeachers().isEmpty()) {
                    botMessage = new BotMessage("По вашему запросу ничего не нашлось.");
                    if(botUser.fromVk()) {
                        botMessage.setVkKeyboard(VkKeyboardGenerator.mainMenu);
                    } else {
                        botMessage.setTgKeyboard(TgKeyboardGenerator.mainMenu());
                    }
                } else {
                    List<Teacher> teachers = teacherListDto.getTeachers();
                    if (teachers.size() > Constants.MAX_VK_KEYBOARD_SIZE_FOR_LISTS) {
                        botMessage = new BotMessage(
                                "Искомый список преподавателей слишком большой для клавиатуры VK. " +
                                        "Попробуйте запросить точнее.");
                        if(botUser.fromVk()) {
                            botMessage.setVkKeyboard(VkKeyboardGenerator.mainMenu);
                        } else {
                            botMessage.setTgKeyboard(TgKeyboardGenerator.mainMenu());
                        }
                    } else {
                        try {
                            botMessage = new BotMessage(
                                    "Выберите, для какого преподавателя хотите узнать расписание.");
                            if(botUser.fromVk()) {
                                botMessage.setVkKeyboard(vkKeyboardGenerator.teachers(teachers));
                            } else {
                                botMessage.setTgKeyboard(tgKeyboardGenerator.teachers(teachers));
                            }
                        } catch (Exception e) {
                            botMessage = new BotMessage(CANNOT_GET_TEACHERS);
                            if(botUser.fromVk()) {
                                botMessage.setVkKeyboard(VkKeyboardGenerator.mainMenu);
                            } else {
                                botMessage.setTgKeyboard(TgKeyboardGenerator.mainMenu());
                            }
                        }
                    }
                }
            } else {
                botMessage = new BotMessage(CANNOT_GET_TEACHERS);
                if(botUser.fromVk()) {
                    botMessage.setVkKeyboard(VkKeyboardGenerator.mainMenu);
                } else {
                    botMessage.setTgKeyboard(TgKeyboardGenerator.mainMenu());
                }
            }
        }

        if (message.equals(CommandText.TEACHER_SCHEDULE)) {
            botMessage = new BotMessage(
                    "Введите полностью или частично что-либо из ФИО преподавателя. " +
                            "Например, по запросу 'Ива' найдутся и 'Иванова', и 'Иван', и 'Иванович'. " +
                            "По запросу 'Иванов Ев' найдется 'Иванов Евгений', но не 'Иванова Евгения'.");
            if (botUser.fromVk()) {
                VkBotUser vkBotUser = vkBotUserRepository.findOneById(botUser.getUserId());
                vkBotUser.setPreviousUserMessage(CommandText.TEACHER_SCHEDULE);
                vkBotUserRepository.save(vkBotUser);
            } else {
                TgBotUser tgBotUser = tgBotUserRepository.findOneById(botUser.getUserId());
                tgBotUser.setPreviousUserMessage(CommandText.TEACHER_SCHEDULE);
                tgBotUserRepository.save(tgBotUser);
            }
        }

        if (message.startsWith(CommandText.TEACHER_ID_PAYLOAD)) {
            if (botUser.fromVk()) {
                VkBotUser vkBotUser = vkBotUserRepository.findOneById(botUser.getUserId());
                vkBotUser.setPreviousUserMessage(message);
                vkBotUserRepository.save(vkBotUser);
                botMessage = new BotMessage(
                        "Выберите, для чего хотите узнать расписание преподавателя. Для сброса поиска выберите на клавиатуре 'Главное меню' или введите вручную 'меню'.",
                        VkKeyboardGenerator.fullTimeSchedule);
            } else {
                TgBotUser tgBotUser = tgBotUserRepository.findOneById(botUser.getUserId());
                tgBotUser.setPreviousUserMessage(message);
                tgBotUserRepository.save(tgBotUser);
                botMessage = new BotMessage(
                        "Выберите, для чего хотите узнать расписание преподавателя. Для сброса поиска выберите на клавиатуре 'Главное меню' или введите вручную 'меню'.",
                        TgKeyboardGenerator.fullTimeSchedule());
            }
        }

        return botMessage;
    }
}
