package com.scribassu.scribabot.util;

import com.scribassu.scribabot.dto.BotMessage;
import com.scribassu.scribabot.dto.InnerBotUser;
import com.scribassu.scribabot.dto.rest.*;
import com.scribassu.scribabot.generators.MessageGenerator;
import com.scribassu.scribabot.generators.TgKeyboardGenerator;
import com.scribassu.scribabot.generators.VkKeyboardGenerator;
import com.scribassu.tracto.domain.EducationForm;
import org.springframework.util.StringUtils;

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
        if (botUser.fromVk()) {
            return new BotMessage(
                    "Ваш вид расписания пока не поддерживается или вы указали недостаточно информации для выдачи расписания.",
                    VkKeyboardGenerator.fullTimeSchedule);
        } else {
            return new BotMessage(
                    "Ваш вид расписания пока не поддерживается или вы указали недостаточно информации для выдачи расписания.",
                    TgKeyboardGenerator.fullTimeSchedule());
        }
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
        if (botUser.fromVk()) {
            return new BotMessage(
                    "Расписание сессии для вашей группы отсутствует.",
                    VkKeyboardGenerator.fullTimeSchedule);
        } else {
            return new BotMessage(
                    "Расписание сессии для вашей группы отсутствует.",
                    TgKeyboardGenerator.fullTimeSchedule());
        }
    }

    public static BotMessage getBotMessageForTeacherFullTimeLessons(TeacherFullTimeLessonDto fullTimeLessonDto,
                                                                    String day,
                                                                    boolean filterWeekType,
                                                                    InnerBotUser botUser) {
        if (botUser.fromVk()) {
            return new BotMessage(
                    MessageGenerator.makeTeacherFullTimeLessonTemplate(fullTimeLessonDto, day, filterWeekType),
                    VkKeyboardGenerator.fullTimeSchedule);
        } else {
            return new BotMessage(
                    MessageGenerator.makeTeacherFullTimeLessonTemplate(fullTimeLessonDto, day, filterWeekType),
                    TgKeyboardGenerator.fullTimeSchedule());
        }
    }

    public static BotMessage getBotMessageForTeacherExamPeriod(TeacherExamPeriodEventDto examPeriodEventDto,
                                                               InnerBotUser botUser) {
        if (botUser.fromVk()) {
            return new BotMessage(
                    MessageGenerator.makeTeacherExamPeriodTemplate(examPeriodEventDto),
                    VkKeyboardGenerator.fullTimeSchedule);
        } else {
            return new BotMessage(
                    MessageGenerator.makeTeacherExamPeriodTemplate(examPeriodEventDto),
                    TgKeyboardGenerator.fullTimeSchedule());
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

    public static BotMessage getBotMessageForEmptyExtramuralEvents(InnerBotUser botUser) {
        if (botUser.fromVk()) {
            return new BotMessage(
                    "Расписание для вашей группы отсутствует.",
                    VkKeyboardGenerator.extramuralSchedule);
        } else {
            return new BotMessage(
                    "Расписание для вашей группы отсутствует.",
                    TgKeyboardGenerator.extramuralSchedule());
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
