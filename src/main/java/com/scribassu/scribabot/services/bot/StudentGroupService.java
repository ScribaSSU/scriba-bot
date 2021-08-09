package com.scribassu.scribabot.services.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scribassu.scribabot.dto.BotMessage;
import com.scribassu.scribabot.dto.rest.GroupNumbersDto;
import com.scribassu.scribabot.dto.vkkeyboard.*;
import com.scribassu.scribabot.entities.BotUser;
import com.scribassu.scribabot.keyboard.TgKeyboardGenerator;
import com.scribassu.scribabot.keyboard.VkKeyboardGenerator;
import com.scribassu.scribabot.services.CallRestService;
import com.scribassu.scribabot.text.MessageText;
import com.scribassu.scribabot.util.Constants;
import com.scribassu.tracto.domain.EducationForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import static com.scribassu.scribabot.keyboard.KeyboardType.ButtonCourse;

@Service
public class StudentGroupService implements BotMessageService {

    private final CallRestService callRestService;
    private final VkKeyboardGenerator vkKeyboardGenerator;
    private final TgKeyboardGenerator tgKeyboardGenerator;

    @Autowired
    public StudentGroupService(CallRestService callRestService,
                               VkKeyboardGenerator vkKeyboardGenerator,
                               TgKeyboardGenerator tgKeyboardGenerator) {
        this.callRestService = callRestService;
        this.vkKeyboardGenerator = vkKeyboardGenerator;
        this.tgKeyboardGenerator = tgKeyboardGenerator;
    }

    @Override
    public BotMessage getBotMessage(String message, BotUser botUser) {
        BotMessage botMessage;
        GroupNumbersDto groupNumbersDto = new GroupNumbersDto();
        if (message.equalsIgnoreCase("другое")) {
            if (botUser != null
                    && !StringUtils.isEmpty(botUser.getDepartment())
                    && !StringUtils.isEmpty(botUser.getEducationForm())) {
                groupNumbersDto =
                        callRestService.getOtherGroupNumbersByDepartmentUrlAndEducationForm(
                                botUser.getDepartment(),
                                EducationForm.fromGroupType(botUser.getEducationForm()).toString()
                        );
            }
        } else {
            String course = message.substring(0, 1);
            if (botUser != null
                    && !StringUtils.isEmpty(botUser.getDepartment())
                    && !StringUtils.isEmpty(botUser.getEducationForm())) {
                groupNumbersDto =
                        callRestService.getGroupNumbersByDepartmentUrlAndEducationFormAndCourse(
                                botUser.getDepartment(),
                                EducationForm.fromGroupType(botUser.getEducationForm()).toString(),
                                course
                        );
            }
        }
        if (CollectionUtils.isEmpty(groupNumbersDto.getGroupNumbers())) {
            if(botUser.fromVk()) {
                botMessage = new BotMessage("Группы не найдены.", VkKeyboardGenerator.courses);
            } else {
                botMessage = new BotMessage("Группы не найдены.", TgKeyboardGenerator.courses());
            }
        } else if (groupNumbersDto.getGroupNumbers().size() > Constants.MAX_VK_KEYBOARD_SIZE_FOR_LISTS) {
            StringBuilder stringBuilder = new StringBuilder("Извините, нашлось слишком много групп, " +
                    "и они не могут отобразиться через клавиатуру из-за ограничений VK :( " +
                    "Пожалуйста, введите номер группы в формате г номергруппы, например, г 123, " +
                    "чтобы бот все-таки записал вашу группу. Доступные номера по вашему запросу:\n\n");
            for (String groupNumber : groupNumbersDto.getGroupNumbers()) {
                stringBuilder.append(groupNumber).append(", ");
            }
            botMessage = new BotMessage(stringBuilder.toString());
        } else {
            if(botUser.fromVk()) {
                VkKeyboard vkKeyboard = vkKeyboardGenerator.groupNumbers(groupNumbersDto.getGroupNumbers());
                botMessage = new BotMessage(MessageText.CHOOSE_STUDENT_GROUP, vkKeyboard);
            } else {
                ReplyKeyboardMarkup tgKeyboard = tgKeyboardGenerator.groupNumbers(groupNumbersDto.getGroupNumbers());
                botMessage = new BotMessage(MessageText.CHOOSE_STUDENT_GROUP, tgKeyboard);
            }
        }

        return botMessage;
    }


}
