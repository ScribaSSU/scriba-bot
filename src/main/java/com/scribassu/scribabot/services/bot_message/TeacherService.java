package com.scribassu.scribabot.services.bot_message;

import com.scribassu.scribabot.dto.rest.TeacherListDto;
import com.scribassu.scribabot.generators.InnerKeyboardGenerator;
import com.scribassu.scribabot.model.BotMessage;
import com.scribassu.scribabot.model.BotUser;
import com.scribassu.scribabot.services.CallRestService;
import com.scribassu.scribabot.services.BotMessageService;
import com.scribassu.scribabot.services.BotUserService;
import com.scribassu.scribabot.text.CommandText;
import com.scribassu.scribabot.util.Constants;
import com.scribassu.tracto.domain.Teacher;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.scribassu.scribabot.text.MessageText.*;

@Service
@Slf4j
@Data
public class TeacherService implements BotMessageService {

    private final CallRestService callRestService;
    private final BotUserService botUserService;
    private final InnerKeyboardGenerator innerKeyboardGenerator;

    @Override
    public CompletableFuture<BotMessage> getBotMessage(String message, BotUser botUser) {
        var botMessage = CompletableFuture.completedFuture(new BotMessage());
        final String userId = botUser.getUserId();

        if (message.equals(CommandText.TEACHER_SCHEDULE)) {
            botMessage = CompletableFuture.completedFuture(new BotMessage(TEACHER_NAME_PROMPT, botUser));
            botUserService.updatePreviousUserMessage(CommandText.TEACHER_SCHEDULE, botUser);
        }

        if (null != botUser.getPreviousUserMessage()
                && botUser.getPreviousUserMessage().equalsIgnoreCase(CommandText.TEACHER_SCHEDULE)) {
            TeacherListDto teacherListDto = callRestService.getTeachersByWord(message);
            if (null != teacherListDto.getTeachers()) {
                if (teacherListDto.getTeachers().isEmpty()) {
                    return CompletableFuture.completedFuture(new BotMessage(EMPTY_TEACHER_LIST, innerKeyboardGenerator.mainMenu(), botUser));
                } else {
                    List<Teacher> teachers = teacherListDto.getTeachers();
                    if (teachers.size() > Constants.MAX_VK_KEYBOARD_SIZE_FOR_LISTS) {
                        return CompletableFuture.completedFuture(new BotMessage(TOO_LONG_TEACHER_LIST, innerKeyboardGenerator.mainMenu(), botUser));
                    } else {
                        try {
                            StringBuilder sb = new StringBuilder();
                            sb.append(CHOOSE_TEACHER_TO_GET_SCHEDULE).append("\n\n");
                            for (Teacher teacher : teachers) {
                                sb
                                        .append(teacher.getId()).append(" ")
                                        .append(teacher.getSurname()).append(" ")
                                        .append(teacher.getName()).append(" ")
                                        .append(teacher.getPatronymic()).append("\n");
                            }
                            String teacherList = sb.toString();
                            botUserService.updatePreviousUserMessage(teacherList, botUser);
                            return CompletableFuture.completedFuture(new BotMessage(teacherList, innerKeyboardGenerator.teachers(teachers), botUser));
                        } catch (Exception e) {
                            return CompletableFuture.completedFuture(new BotMessage(CANNOT_GET_TEACHERS, innerKeyboardGenerator.mainMenu(), botUser));
                        }
                    }
                }
            } else {
                return CompletableFuture.completedFuture(new BotMessage(CANNOT_GET_TEACHERS, innerKeyboardGenerator.mainMenu(), botUser));
            }
        }

        if (null != botUser.getPreviousUserMessage()
                && botUser.getPreviousUserMessage().equals(CHOOSE_TEACHER_TO_GET_SCHEDULE)) {
            try {
                String teacherId = message.substring(0, message.indexOf(" "));
                botUserService.updatePreviousUserMessage(Constants.TEACHER_ID + teacherId, botUser);
                return CompletableFuture.completedFuture(new BotMessage(
                        CHOOSE_DAY_FOR_TEACHER_SCHEDULE,
                        innerKeyboardGenerator.teacherSchedule(), botUser));
            } catch (StringIndexOutOfBoundsException e) {
                return CompletableFuture.completedFuture(new BotMessage(CANNOT_GET_TEACHERS, innerKeyboardGenerator.mainMenu(), botUser));
            }
        }

        return botMessage;
    }
}
