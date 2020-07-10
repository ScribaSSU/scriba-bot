package com.scribassu.scribabot.services.bot;

import com.scribassu.scribabot.dto.ExamPeriodEventDto;
import com.scribassu.scribabot.dto.TeacherExamPeriodEventDto;
import com.scribassu.scribabot.entities.BotUser;
import com.scribassu.scribabot.services.CallRestService;
import com.scribassu.scribabot.services.bot.BotMessageService;
import com.scribassu.scribabot.text.CommandText;
import com.scribassu.scribabot.util.BotMessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ExamPeriodService implements BotMessageService {

    private final CallRestService callRestService;

    @Autowired
    public ExamPeriodService(CallRestService callRestService) {
        this.callRestService = callRestService;
    }


    @Override
    public Map<String, String> getBotMessage(String message, BotUser botUser) {
        if(botUser.wantTeacherSchedule()) {
            return getTeacherBotMessage(message, botUser);
        }
        else {
            return getStudentBotMessage(message, botUser);
        }

    }

    private Map<String, String> getTeacherBotMessage(String message, BotUser botUser) {
        String teacherId = botUser.getPreviousUserMessage().split(" ")[2];
        if(message.equalsIgnoreCase(CommandText.EXAMS)) {
            TeacherExamPeriodEventDto examPeriodEventDto = callRestService.getTeacherExamPeriodEvents(
                    teacherId
            );
            if(examPeriodEventDto == null || examPeriodEventDto.getExamPeriodEvents().isEmpty()) {
                return BotMessageUtils.getBotMessageForEmptyFullTimeExamPeriod();
            }
            else {
                return BotMessageUtils.getBotMessageForTeacherExamPeriod(examPeriodEventDto);
            }
        }
        else {
            return BotMessageUtils.getBotMessageForEmptyFullTimeExamPeriod();
        }
    }

    private Map<String, String> getStudentBotMessage(String message, BotUser botUser) {
        if(message.equalsIgnoreCase(CommandText.EXAMS)) {
            ExamPeriodEventDto examPeriodEventDto = callRestService.getFullTimeExamPeriodEvent(
                    botUser.getDepartment(),
                    botUser.getGroupNumber()
            );
            if(examPeriodEventDto == null || examPeriodEventDto.getExamPeriodEvents().isEmpty()) {
                return BotMessageUtils.getBotMessageForEmptyFullTimeExamPeriod();
            }
            else {
                return BotMessageUtils.getBotMessageForFullTimeExamPeriod(examPeriodEventDto);
            }
        }
        else {
            return BotMessageUtils.getBotMessageForEmptyFullTimeExamPeriod();
        }
    }


}
