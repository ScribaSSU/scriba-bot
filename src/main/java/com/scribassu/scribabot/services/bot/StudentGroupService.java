package com.scribassu.scribabot.services.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scribassu.scribabot.dto.BotMessage;
import com.scribassu.scribabot.dto.rest.GroupNumbersDto;
import com.scribassu.scribabot.dto.vkkeyboard.*;
import com.scribassu.scribabot.entities.BotUser;
import com.scribassu.scribabot.keyboard.KeyboardMap;
import com.scribassu.scribabot.keyboard.KeyboardType;
import com.scribassu.scribabot.services.CallRestService;
import com.scribassu.scribabot.text.CommandText;
import com.scribassu.scribabot.text.MessageText;
import com.scribassu.scribabot.util.Constants;
import com.scribassu.tracto.domain.EducationForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.scribassu.scribabot.keyboard.KeyboardType.ButtonCourse;
import static com.scribassu.scribabot.keyboard.KeyboardType.ButtonDepartment;

@Service
public class StudentGroupService implements BotMessageService {

    private final CallRestService callRestService;

    @Autowired
    public StudentGroupService(CallRestService callRestService) {
        this.callRestService = callRestService;
    }

    @Override
    public BotMessage getBotMessage(String message, BotUser botUser) {
        BotMessage botMessage;
        GroupNumbersDto groupNumbersDto = new GroupNumbersDto();
        if(message.equalsIgnoreCase("другое")) {
            if(botUser != null
                    && !StringUtils.isEmpty(botUser.getDepartment())
                    && !StringUtils.isEmpty(botUser.getEducationForm())) {
                groupNumbersDto =
                        callRestService.getOtherGroupNumbersByDepartmentUrlAndEducationForm(
                                botUser.getDepartment(),
                                EducationForm.fromGroupType(botUser.getEducationForm()).toString()
                        );
            }
        }
        else {
            String course = message.substring(0, 1);
            if(botUser != null
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
        if(CollectionUtils.isEmpty(groupNumbersDto.getGroupNumbers())) {
            botMessage = new BotMessage("Группы не найдены.", ButtonCourse);
        }
        else if(groupNumbersDto.getGroupNumbers().size() > Constants.MAX_VK_KEYBOARD_SIZE_FOR_LISTS) {
            StringBuilder stringBuilder = new StringBuilder("Извините, нашлось слишком много групп, " +
                    "и они не могут отобразиться через клавиатуру из-за ограничений VK. " +
                    "Пожалуйста, введите номер группы в формате Г НОМЕР, например, Г 123, " +
                    "чтобы бот все-таки записал вашу группу. Доступные номера по вашему запросу:\n\n");
            for(String groupNumber : groupNumbersDto.getGroupNumbers()) {
                stringBuilder.append(groupNumber).append(", ");
            }
            botMessage = new BotMessage(stringBuilder.toString());
        }
        else {
            VkKeyboard vkKeyboard = buildVkKeyboardFromGroupNumbers(groupNumbersDto.getGroupNumbers());
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                botMessage = new BotMessage("Выберите группу.", objectMapper.writeValueAsString(vkKeyboard));
            }
            catch(Exception e) {
                botMessage = new BotMessage("Не удалось загрузить список групп :(", ButtonCourse);
            }
        }

        return botMessage;
    }

    private VkKeyboard buildVkKeyboardFromGroupNumbers(List<String> groupNumbers) {
        List<List<VkKeyboardButton>> vkKeyboardButtons = new ArrayList<>();

        int i = 0;
        int row = 0;
        vkKeyboardButtons.add(new ArrayList<>());
        int mod = groupNumbers.get(0).length() > 5 ? 3 : 5; //to make long numbers visible
        int mmod = mod - 1;

        while(i < groupNumbers.size()) {
            if(i % mod == mmod) {
                row++;
                vkKeyboardButtons.add(new ArrayList<>());
            }
            vkKeyboardButtons.get(row).add(
                    new VkKeyboardButton(
                            new VkKeyboardButtonActionText(
                                    groupNumbers.get(i),
                                    String.format(Constants.PAYLOAD, CommandText.CHOOSE_STUDENT_GROUP),
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
