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

import static com.scribassu.scribabot.text.MessageText.*;

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
        final String userId = botUser.getUserId();

        if (message.equals(CommandText.TEACHER_SCHEDULE)) {
            botMessage = new BotMessage(TEACHER_NAME_PROMPT);
            if (botUser.fromVk()) {
                vkBotUserRepository.updatePreviousUserMessage(CommandText.TEACHER_SCHEDULE, userId);
            } else {
                tgBotUserRepository.updatePreviousUserMessage(CommandText.TEACHER_SCHEDULE, userId);
            }
        }

        if (null != botUser.getPreviousUserMessage()
                && botUser.getPreviousUserMessage().equalsIgnoreCase(CommandText.TEACHER_SCHEDULE)) {
            TeacherListDto teacherListDto = callRestService.getTeachersByWord(message);
            if (null != teacherListDto.getTeachers()) {
                if (teacherListDto.getTeachers().isEmpty()) {
                    botMessage = botUser.fromVk() ?
                            new BotMessage(EMPTY_TEACHER_LIST, VkKeyboardGenerator.mainMenu)
                            : new BotMessage(EMPTY_TEACHER_LIST, TgKeyboardGenerator.mainMenu());
                } else {
                    List<Teacher> teachers = teacherListDto.getTeachers();
                    if (teachers.size() > Constants.MAX_VK_KEYBOARD_SIZE_FOR_LISTS) {
                        botMessage = botUser.fromVk() ?
                                new BotMessage(TOO_LONG_TEACHER_LIST, VkKeyboardGenerator.mainMenu)
                                : new BotMessage(TOO_LONG_TEACHER_LIST, TgKeyboardGenerator.mainMenu());
                    } else {
                        try {
                            botMessage = new BotMessage(CHOOSE_TEACHER_TO_GET_SCHEDULE);
                            if(botUser.fromVk()) {
                                vkBotUserRepository.updatePreviousUserMessage(CHOOSE_TEACHER_TO_GET_SCHEDULE, userId);
                                botMessage.setVkKeyboard(vkKeyboardGenerator.teachers(teachers));
                            } else {
                                tgBotUserRepository.updatePreviousUserMessage(CHOOSE_TEACHER_TO_GET_SCHEDULE, userId);
                                botMessage.setTgKeyboard(tgKeyboardGenerator.teachers(teachers));
                                System.out.println("I'll give you a Batraeva teacher");
                            }
                        } catch (Exception e) {
                            botMessage = botUser.fromVk() ?
                                    new BotMessage(CANNOT_GET_TEACHERS, VkKeyboardGenerator.mainMenu)
                                    : new BotMessage(CANNOT_GET_TEACHERS, TgKeyboardGenerator.mainMenu());
                        }
                    }
                }
            } else {
                botMessage = botUser.fromVk() ?
                        new BotMessage(CANNOT_GET_TEACHERS, VkKeyboardGenerator.mainMenu)
                        : new BotMessage(CANNOT_GET_TEACHERS, TgKeyboardGenerator.mainMenu());
            }
        }

        if (null != botUser.getPreviousUserMessage()
                && botUser.getPreviousUserMessage().equals(CHOOSE_TEACHER_TO_GET_SCHEDULE)) {
            try {
                String teacherId = message.substring(0, message.indexOf(" "));
                if (botUser.fromVk()) {
                    vkBotUserRepository.updatePreviousUserMessage("TEACHER_ID " + teacherId, userId);
                    botMessage = new BotMessage(
                            CHOOSE_DAY_FOR_TEACHER_SCHEDULE,
                            VkKeyboardGenerator.fullTimeSchedule);
                } else {
                    tgBotUserRepository.updatePreviousUserMessage("TEACHER_ID " + teacherId, userId);
                    botMessage = new BotMessage(
                            CHOOSE_DAY_FOR_TEACHER_SCHEDULE,
                            TgKeyboardGenerator.fullTimeSchedule());
                }
            } catch (StringIndexOutOfBoundsException e) {
                botMessage = botUser.fromVk() ?
                        new BotMessage(CANNOT_GET_TEACHERS, VkKeyboardGenerator.mainMenu)
                        : new BotMessage(CANNOT_GET_TEACHERS, TgKeyboardGenerator.mainMenu());
            }
        }

        return botMessage;
    }
}
