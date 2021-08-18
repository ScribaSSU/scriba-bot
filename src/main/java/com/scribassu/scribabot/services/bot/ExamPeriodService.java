package com.scribassu.scribabot.services.bot;

import com.scribassu.scribabot.dto.BotMessage;
import com.scribassu.scribabot.dto.InnerBotUser;
import com.scribassu.scribabot.dto.rest.ExamPeriodEventDto;
import com.scribassu.scribabot.dto.rest.TeacherExamPeriodEventDto;
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
    public BotMessage getBotMessage(String message, InnerBotUser botUser) {
        if (botUser.wantTeacherSchedule()) {
            return getTeacherBotMessage(message, botUser);
        } else {
            return getStudentBotMessage(message, botUser);
        }
    }

    private BotMessage getTeacherBotMessage(String message, InnerBotUser botUser) {
        String teacherId = botUser.getPreviousUserMessage().split(" ")[1];
        if (message.equalsIgnoreCase(CommandText.EXAMS)) {
            TeacherExamPeriodEventDto examPeriodEventDto = callRestService.getTeacherExamPeriodEvents(teacherId);
            if (null == examPeriodEventDto || examPeriodEventDto.getExamPeriodEvents().isEmpty()) {
                return BotMessageUtils.getBotMessageForEmptyFullTimeExamPeriod(botUser);
            } else {
                return BotMessageUtils.getBotMessageForTeacherExamPeriod(examPeriodEventDto, botUser);
            }
        } else {
            return BotMessageUtils.getBotMessageForEmptyFullTimeExamPeriod(botUser);
        }
    }

    private BotMessage getStudentBotMessage(String message, InnerBotUser botUser) {
        if (message.equalsIgnoreCase(CommandText.EXAMS)) {
            ExamPeriodEventDto examPeriodEventDto = callRestService.getFullTimeExamPeriodEvent(
                    botUser.getDepartment(),
                    botUser.getGroupNumber()
            );
            if (null == examPeriodEventDto || examPeriodEventDto.getExamPeriodEvents().isEmpty()) {
                return BotMessageUtils.getBotMessageForEmptyFullTimeExamPeriod(botUser);
            } else {
                return BotMessageUtils.getBotMessageForFullTimeExamPeriod(examPeriodEventDto, "", botUser);
            }
        } else {
            return BotMessageUtils.getBotMessageForEmptyFullTimeExamPeriod(botUser);
        }
    }


}
