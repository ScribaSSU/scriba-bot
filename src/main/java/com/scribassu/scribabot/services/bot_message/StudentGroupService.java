package com.scribassu.scribabot.services.bot_message;

import com.scribassu.scribabot.generators.InnerKeyboardGenerator;
import com.scribassu.scribabot.model.BotMessage;
import com.scribassu.scribabot.model.BotUser;
import com.scribassu.scribabot.services.BotMessageService;
import com.scribassu.scribabot.services.BotUserService;
import com.scribassu.scribabot.services.CallRestService;
import com.scribassu.scribabot.text.CommandText;
import com.scribassu.scribabot.text.MessageText;
import com.scribassu.scribabot.util.Constants;
import com.scribassu.tracto.dto.EducationForm;
import com.scribassu.tracto.dto.GroupNumbersDto;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.concurrent.CompletableFuture;

import static com.scribassu.scribabot.text.MessageText.GREETING_WITH_CHOOSE_DEPARTMENT;
import static com.scribassu.scribabot.text.MessageText.THIS_IS_MAIN_MENU;

@Service
@Slf4j
@Data
public class StudentGroupService implements BotMessageService {

    private final CallRestService callRestService;
    private final BotUserService botUserService;
    private final InnerKeyboardGenerator innerKeyboardGenerator;

    private final String ANOTHER_GROUP_PATTERN_COMMAND = "другое";

    @Override
    public boolean shouldAccept(String message, BotUser botUser) {
        return message.equalsIgnoreCase(ANOTHER_GROUP_PATTERN_COMMAND)
                || message.startsWith(CommandText.GROUP_NUMBER_INPUT)
                || CommandText.COURSE_PATTERN.matcher(message).matches();
    }

    @Override
    public CompletableFuture<BotMessage> getBotMessage(String message, BotUser botUser) {
        GroupNumbersDto groupNumbersDto = new GroupNumbersDto();

        if (message.startsWith(CommandText.GROUP_NUMBER_INPUT)) {
            botUserService.updateGroupNumber(message.substring(2), botUser);

            if (null != botUser.getPreviousUserMessage()
                    && botUser.getPreviousUserMessage().equalsIgnoreCase(GREETING_WITH_CHOOSE_DEPARTMENT)) {
                botUserService.resetPreviousUserMessage(botUser);
                return CompletableFuture.completedFuture(new BotMessage(THIS_IS_MAIN_MENU, innerKeyboardGenerator.mainMenu(botUser), botUser));
            } else {
                if (BotUser.isBotUserFullTime(botUser)) {
                    return CompletableFuture.completedFuture(new BotMessage(MessageText.FINISH_SET_GROUP, innerKeyboardGenerator.fullTimeSchedule(botUser), botUser));
                }
                if (BotUser.isBotUserExtramural(botUser)) {
                    return CompletableFuture.completedFuture(new BotMessage(MessageText.FINISH_SET_GROUP, innerKeyboardGenerator.extramuralSchedule(botUser), botUser));
                }
            }
        }

        if (message.equalsIgnoreCase(ANOTHER_GROUP_PATTERN_COMMAND)) {
            if (botUser != null
                    && !botUser.getDepartment().isBlank()
                    && !botUser.getEducationForm().isBlank()) {
                groupNumbersDto =
                        callRestService.getOtherGroupNumbersByDepartmentUrlAndEducationForm(
                                botUser.getDepartment(),
                                EducationForm.fromGroupType(botUser.getEducationForm()).toString()
                        );
            }
        } else {
            String course = message.substring(0, 1);
            if (departmentAndEducationFormAreSet(botUser)) {
                groupNumbersDto =
                        callRestService.getGroupNumbersByDepartmentUrlAndEducationFormAndCourse(
                                botUser.getDepartment(),
                                EducationForm.fromGroupType(botUser.getEducationForm()).toString(),
                                course
                        );
            }
        }
        if (CollectionUtils.isEmpty(groupNumbersDto.getGroupNumbers())) {
            return CompletableFuture.completedFuture(new BotMessage("Группы не найдены.", innerKeyboardGenerator.courses(), botUser));
        } else if (groupNumbersDto.getGroupNumbers().size() > Constants.MAX_VK_KEYBOARD_SIZE_FOR_LISTS) {
            StringBuilder stringBuilder = new StringBuilder("Извините, нашлось слишком много групп, " +
                    "и они не могут отобразиться через клавиатуру из-за ограничений клавиатур ботов :( " +
                    "Пожалуйста, введите номер группы в формате Г номергруппы, например, Г 123, " +
                    "чтобы бот все-таки записал вашу группу. Доступные номера по вашему запросу:\n\n");
            for (String groupNumber : groupNumbersDto.getGroupNumbers()) {
                stringBuilder.append(groupNumber).append(", ");
            }
            return CompletableFuture.completedFuture(new BotMessage(stringBuilder.toString(), botUser));
        } else {
            return CompletableFuture.completedFuture(new BotMessage(MessageText.CHOOSE_STUDENT_GROUP, innerKeyboardGenerator.groupNumbers(groupNumbersDto.getGroupNumbers()), botUser));
        }
    }

    private boolean departmentAndEducationFormAreSet(BotUser botUser) {
        return botUser != null
                && !botUser.getDepartment().isBlank()
                && !botUser.getEducationForm().isBlank();
    }
}
