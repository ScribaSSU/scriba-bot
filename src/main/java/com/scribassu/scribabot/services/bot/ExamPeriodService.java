package com.scribassu.scribabot.services.bot;

import com.scribassu.scribabot.dto.rest.ExamPeriodEventDto;
import com.scribassu.scribabot.dto.rest.TeacherExamPeriodEventDto;
import com.scribassu.scribabot.generators.BotMessageGenerator;
import com.scribassu.scribabot.model.BotMessage;
import com.scribassu.scribabot.model.InnerBotUser;
import com.scribassu.scribabot.services.CallRestService;
import com.scribassu.scribabot.text.CommandText;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@Data
public class ExamPeriodService implements BotMessageService {

    private final CallRestService callRestService;
    private final BotMessageGenerator botMessageGenerator;

    @Override
    public CompletableFuture<BotMessage> getBotMessage(String message, InnerBotUser botUser) {
        if (botUser.wantTeacherSchedule()) {
            return getTeacherBotMessage(message, botUser);
        } else {
            return getStudentBotMessage(message, botUser);
        }
    }

    private CompletableFuture<BotMessage> getTeacherBotMessage(String message, InnerBotUser botUser) {
        String teacherId = botUser.getPreviousUserMessage().split(" ")[1];
        if (message.equalsIgnoreCase(CommandText.EXAMS)) {
            TeacherExamPeriodEventDto examPeriodEventDto = callRestService.getTeacherExamPeriodEvents(teacherId);
            if (null == examPeriodEventDto || examPeriodEventDto.getExamPeriodEvents().isEmpty()) {
                return CompletableFuture.completedFuture(botMessageGenerator.getBotMessageForEmptyFullTimeExamPeriod(botUser));
            } else {
                return CompletableFuture.completedFuture(botMessageGenerator.getBotMessageForTeacherExamPeriod(examPeriodEventDto, botUser));
            }
        } else {
            return CompletableFuture.completedFuture(botMessageGenerator.getBotMessageForEmptyFullTimeExamPeriod(botUser));
        }
    }

    private CompletableFuture<BotMessage> getStudentBotMessage(String message, InnerBotUser botUser) {
        if (message.equalsIgnoreCase(CommandText.EXAMS)) {
            ExamPeriodEventDto examPeriodEventDto = callRestService.getFullTimeExamPeriodEvent(
                    botUser.getDepartment(),
                    botUser.getGroupNumber()
            );
            if (null == examPeriodEventDto || examPeriodEventDto.getExamPeriodEvents().isEmpty()) {
                return CompletableFuture.completedFuture(botMessageGenerator.getBotMessageForEmptyFullTimeExamPeriod(botUser));
            } else {
                return CompletableFuture.completedFuture(botMessageGenerator.getBotMessageForFullTimeExamPeriod(examPeriodEventDto, "", botUser));
            }
        } else {
            return CompletableFuture.completedFuture(botMessageGenerator.getBotMessageForEmptyFullTimeExamPeriod(botUser));
        }
    }


}
