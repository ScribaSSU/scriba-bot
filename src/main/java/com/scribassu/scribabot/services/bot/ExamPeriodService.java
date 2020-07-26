package com.scribassu.scribabot.services.bot;

import com.scribassu.scribabot.dto.BotMessage;
import com.scribassu.scribabot.dto.rest.ExamPeriodEventDto;
import com.scribassu.scribabot.dto.rest.TeacherExamPeriodEventDto;
import com.scribassu.scribabot.entities.BotUser;
import com.scribassu.scribabot.services.CallRestService;
import com.scribassu.scribabot.text.CommandText;
import com.scribassu.scribabot.util.BotMessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExamPeriodService implements BotMessageService {

    private final CallRestService callRestService;

    @Autowired
    public ExamPeriodService(CallRestService callRestService) {
        this.callRestService = callRestService;
    }


    @Override
    public BotMessage getBotMessage(String message, BotUser botUser) {
        if(botUser.wantTeacherSchedule()) {
            return getTeacherBotMessage(message, botUser);
        }
        else {
            return getStudentBotMessage(message, botUser);
        }

    }

    private BotMessage getTeacherBotMessage(String message, BotUser botUser) {
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

    private BotMessage getStudentBotMessage(String message, BotUser botUser) {
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
