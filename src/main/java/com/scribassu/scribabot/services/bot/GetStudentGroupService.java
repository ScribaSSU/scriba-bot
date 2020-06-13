package com.scribassu.scribabot.services.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scribassu.scribabot.dto.GroupNumbersDto;
import com.scribassu.scribabot.dto.vkkeyboard.*;
import com.scribassu.scribabot.entities.BotUser;
import com.scribassu.scribabot.keyboard.KeyboardMap;
import com.scribassu.scribabot.keyboard.KeyboardType;
import com.scribassu.scribabot.services.CallRestService;
import com.scribassu.scribabot.text.CommandText;
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

@Service
public class GetStudentGroupService implements BotMessageService {

    private final CallRestService callRestService;

    @Autowired
    public GetStudentGroupService(CallRestService callRestService) {
        this.callRestService = callRestService;
    }

    @Override
    public Map<String, String> getBotMessage(String message, BotUser botUser) {
        Map<String, String> botMessage = new HashMap<>();
        GroupNumbersDto groupNumbersDto = new GroupNumbersDto();
        if(message.equalsIgnoreCase("Другое")) {
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
            System.out.println(groupNumbersDto);
            if(CollectionUtils.isEmpty(groupNumbersDto.getGroupNumbers())) {
                botMessage.put(
                        Constants.KEY_MESSAGE,
                        "Группы не найдены.");
                botMessage.put(
                        Constants.KEY_KEYBOARD,
                        KeyboardMap.keyboards.get(KeyboardType.ButtonCourse).getJsonText());
            }
            else {
                VkKeyboard vkKeyboard = buildVkKeyboardFromGroupNumbers(groupNumbersDto.getGroupNumbers());
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    botMessage.put(Constants.KEY_KEYBOARD, objectMapper.writeValueAsString(vkKeyboard));
                    botMessage.put(Constants.KEY_MESSAGE, "Выберите группу.");
                }
                catch(Exception e) {
                    botMessage.put(Constants.KEY_MESSAGE, "Не удалось загрузить список групп.");
                }
            }
        return botMessage;
    }

    private VkKeyboard buildVkKeyboardFromGroupNumbers(List<String> groupNumbers) {
        List<List<VkKeyboardButton>> vkKeyboardButtons = new ArrayList<>();

        int i = 0;
        int row = 0;
        vkKeyboardButtons.add(new ArrayList<>());

        while(i < groupNumbers.size()) {
            if(i % 5 == 4) {
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
