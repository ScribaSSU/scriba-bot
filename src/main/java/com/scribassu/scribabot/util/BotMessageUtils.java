package com.scribassu.scribabot.util;

import com.scribassu.scribabot.dto.BotMessage;
import com.scribassu.scribabot.dto.InnerBotUser;
import com.scribassu.scribabot.dto.rest.*;
import com.scribassu.scribabot.generators.MessageGenerator;
import com.scribassu.scribabot.generators.TgKeyboardGenerator;
import com.scribassu.scribabot.generators.VkKeyboardGenerator;
import com.scribassu.tracto.domain.EducationForm;
import org.springframework.util.StringUtils;

import static com.scribassu.scribabot.text.MessageText.*;

public class BotMessageUtils {

    public static BotMessage getBotMessageForFullTimeLessons(FullTimeLessonDto fullTimeLessonDto,
                                                             String day,
                                                             boolean filterWeekType,
                                                             InnerBotUser botUser) {
        if (botUser.fromVk()) {
            return new BotMessage(
                    MessageGenerator.makeFullTimeLessonTemplate(fullTimeLessonDto, day, filterWeekType),
                    VkKeyboardGenerator.fullTimeSchedule);
        } else {
            return new BotMessage(
                    MessageGenerator.makeFullTimeLessonTemplate(fullTimeLessonDto, day, filterWeekType),
                    TgKeyboardGenerator.fullTimeSchedule());
        }
    }

    public static BotMessage getBotMessageForUnsupportedLessons(InnerBotUser botUser) {
        return botUser.fromVk() ?
                new BotMessage(UNSUPPORTED_LESSONS, VkKeyboardGenerator.departments)
                : new BotMessage(UNSUPPORTED_LESSONS, TgKeyboardGenerator.departments());
    }

    public static BotMessage getBotMessageForFullTimeExamPeriod(ExamPeriodEventDto examPeriodEventDto,
                                                                String day,
                                                                InnerBotUser botUser) {
        if (botUser.fromVk()) {
            return new BotMessage(
                    MessageGenerator.makeFullTimeExamPeriodTemplate(examPeriodEventDto, day),
                    VkKeyboardGenerator.fullTimeSchedule);
        } else {
            return new BotMessage(
                    MessageGenerator.makeFullTimeExamPeriodTemplate(examPeriodEventDto, day),
                    TgKeyboardGenerator.fullTimeSchedule());
        }
    }

    public static BotMessage getBotMessageForEmptyFullTimeExamPeriod(InnerBotUser botUser) {
        return botUser.fromVk() ?
                new BotMessage(NO_EXAM_PERIOD_SCHEDULE, VkKeyboardGenerator.fullTimeSchedule)
                : new BotMessage(NO_EXAM_PERIOD_SCHEDULE, TgKeyboardGenerator.fullTimeSchedule());
    }

    public static BotMessage getBotMessageForTeacherFullTimeLessons(TeacherFullTimeLessonDto fullTimeLessonDto,
                                                                    String day,
                                                                    boolean filterWeekType,
                                                                    InnerBotUser botUser) {
        if (botUser.fromVk()) {
            return new BotMessage(
                    MessageGenerator.makeTeacherFullTimeLessonTemplate(fullTimeLessonDto, day, filterWeekType),
                    VkKeyboardGenerator.teacherSchedule);
        } else {
            return new BotMessage(
                    MessageGenerator.makeTeacherFullTimeLessonTemplate(fullTimeLessonDto, day, filterWeekType),
                    TgKeyboardGenerator.teacherSchedule());
        }
    }

    public static BotMessage getBotMessageForTeacherExamPeriod(TeacherExamPeriodEventDto examPeriodEventDto,
                                                               InnerBotUser botUser) {
        if (botUser.fromVk()) {
            return new BotMessage(
                    MessageGenerator.makeTeacherExamPeriodTemplate(examPeriodEventDto),
                    VkKeyboardGenerator.teacherSchedule);
        } else {
            return new BotMessage(
                    MessageGenerator.makeTeacherExamPeriodTemplate(examPeriodEventDto),
                    TgKeyboardGenerator.teacherSchedule());
        }
    }

    public static BotMessage getBotMessageForExtramuralEvent(ExtramuralDto extramuralDto,
                                                             String day,
                                                             InnerBotUser botUser) {
        if (botUser.fromVk()) {
            return new BotMessage(
                    MessageGenerator.makeExtramuralEventTemplate(extramuralDto, day),
                    VkKeyboardGenerator.extramuralSchedule);
        } else {
            return new BotMessage(
                    MessageGenerator.makeExtramuralEventTemplate(extramuralDto, day),
                    TgKeyboardGenerator.extramuralSchedule());
        }
    }

    public static BotMessage getBotMessageForEmptyExtramuralEvent(InnerBotUser botUser) {
        return botUser.fromVk() ?
                new BotMessage(NO_EXAM_PERIOD_SCHEDULE, VkKeyboardGenerator.extramuralSchedule)
                : new BotMessage(NO_EXAM_PERIOD_SCHEDULE, TgKeyboardGenerator.extramuralSchedule());
    }

    public static BotMessage getBotMessageForEmptyExtramuralEventTeacher(InnerBotUser botUser) {
        return botUser.fromVk() ?
                new BotMessage(NO_EXAM_PERIOD_SCHEDULE_TEACHER, VkKeyboardGenerator.teacherSchedule)
                : new BotMessage(NO_EXAM_PERIOD_SCHEDULE_TEACHER, TgKeyboardGenerator.teacherSchedule());
    }

    public static BotMessage getBotMessageForExtramuralEventTeacher(TeacherExtramuralEventDto extramuralDto, InnerBotUser botUser) {
        if (botUser.fromVk()) {
            return new BotMessage(
                    MessageGenerator.makeExtramuralEventTemplateTeacher(extramuralDto),
                    VkKeyboardGenerator.teacherSchedule);
        } else {
            return new BotMessage(
                    MessageGenerator.makeExtramuralEventTemplateTeacher(extramuralDto),
                    TgKeyboardGenerator.teacherSchedule());
        }
    }

    public static boolean isBotUserFullTime(InnerBotUser botUser) {
        return botUser != null
                && botUser.getEducationForm() != null
                && EducationForm.DO.getGroupType().equalsIgnoreCase(botUser.getEducationForm())
                && !StringUtils.isEmpty(botUser.getGroupNumber());
    }

    public static boolean isBotUserExtramural(InnerBotUser botUser) {
        return botUser != null
                && botUser.getEducationForm() != null
                && EducationForm.ZO.getGroupType().equalsIgnoreCase(botUser.getEducationForm())
                && !StringUtils.isEmpty(botUser.getGroupNumber());
    }
}
