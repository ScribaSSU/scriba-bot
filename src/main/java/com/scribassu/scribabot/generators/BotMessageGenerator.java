package com.scribassu.scribabot.generators;

import com.scribassu.scribabot.dto.rest.*;
import com.scribassu.scribabot.model.BotMessage;
import com.scribassu.scribabot.model.BotUser;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.scribassu.scribabot.text.MessageText.*;

@Service
@Slf4j
@Data
public class BotMessageGenerator {

    private final InnerKeyboardGenerator innerKeyboardGenerator;

    public BotMessage getBotMessageForFullTimeLessons(FullTimeLessonDto fullTimeLessonDto,
                                                      String day,
                                                      BotUser botUser) {
        return new BotMessage(
                MessageGenerator.makeFullTimeLessonTemplate(fullTimeLessonDto, day, botUser.isFilterNomDenom()),
                innerKeyboardGenerator.fullTimeSchedule(), botUser);
    }

    public BotMessage getBotMessageForFullTimeLessonsAll(FullTimeLessonDto fullTimeLessonDto,
                                                         BotUser botUser) {
        return new BotMessage(
                MessageGenerator.makeFullTimeLessonTemplateLessonsAll(fullTimeLessonDto, botUser.isFilterNomDenom()),
                innerKeyboardGenerator.fullTimeSchedule(), botUser);
    }

    public BotMessage getBotMessageForUnsupportedLessons(BotUser botUser) {
        return new BotMessage(UNSUPPORTED_LESSONS, innerKeyboardGenerator.departments(), botUser);
    }

    public BotMessage getBotMessageForFullTimeExamPeriod(ExamPeriodEventDto examPeriodEventDto,
                                                         String day, BotUser botUser) {
        return new BotMessage(
                MessageGenerator.makeFullTimeExamPeriodTemplate(examPeriodEventDto, day),
                innerKeyboardGenerator.fullTimeSchedule(), botUser);
    }

    public BotMessage getBotMessageForEmptyFullTimeExamPeriod(BotUser botUser) {
        return new BotMessage(NO_EXAM_PERIOD_SCHEDULE, innerKeyboardGenerator.fullTimeSchedule(), botUser);
    }

    public BotMessage getBotMessageForTeacherFullTimeLessons(TeacherFullTimeLessonDto fullTimeLessonDto,
                                                             String day,
                                                             BotUser botUser) {
        return new BotMessage(
                MessageGenerator.makeTeacherFullTimeLessonTemplate(fullTimeLessonDto, day, botUser.isFilterNomDenom()),
                innerKeyboardGenerator.teacherSchedule(), botUser);
    }

    public BotMessage getBotMessageForTeacherExamPeriod(TeacherExamPeriodEventDto examPeriodEventDto, BotUser botUser) {
        return new BotMessage(
                MessageGenerator.makeTeacherExamPeriodTemplate(examPeriodEventDto),
                innerKeyboardGenerator.teacherSchedule(), botUser);
    }

    public BotMessage getBotMessageForExtramuralEvent(ExtramuralDto extramuralDto,
                                                      String day, BotUser botUser) {
        return new BotMessage(
                MessageGenerator.makeExtramuralEventTemplate(extramuralDto, day),
                innerKeyboardGenerator.extramuralSchedule(), botUser);
    }

    public BotMessage getBotMessageForEmptyExtramuralEvent(BotUser botUser) {
        return new BotMessage(NO_EXAM_PERIOD_SCHEDULE, innerKeyboardGenerator.extramuralSchedule(), botUser);
    }

    public BotMessage getBotMessageForEmptyExtramuralEventTeacher(BotUser botUser) {
        return new BotMessage(NO_EXAM_PERIOD_SCHEDULE_TEACHER, innerKeyboardGenerator.teacherSchedule(), botUser);
    }

    public BotMessage getBotMessageForExtramuralEventTeacher(TeacherExtramuralEventDto extramuralDto, BotUser botUser) {
        return new BotMessage(
                MessageGenerator.makeExtramuralEventTemplateTeacher(extramuralDto),
                innerKeyboardGenerator.teacherSchedule(), botUser);
    }
}
