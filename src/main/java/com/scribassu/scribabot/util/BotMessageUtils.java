package com.scribassu.scribabot.util;

import com.scribassu.scribabot.dto.BotMessage;
import com.scribassu.scribabot.dto.rest.*;
import com.scribassu.scribabot.entities.BotUser;
import com.scribassu.tracto.domain.EducationForm;
import org.springframework.util.StringUtils;

import static com.scribassu.scribabot.keyboard.KeyboardType.ButtonExtramuralSchedule;
import static com.scribassu.scribabot.keyboard.KeyboardType.ButtonFullTimeSchedule;

public class BotMessageUtils {

    public static BotMessage getBotMessageForFullTimeLessons(FullTimeLessonDto fullTimeLessonDto,
                                                             String day,
                                                             boolean filterWeekType) {
        return new BotMessage(
                Templates.makeFullTimeLessonTemplate(fullTimeLessonDto, day, filterWeekType),
                ButtonFullTimeSchedule);
    }

    public static BotMessage getBotMessageForUnsupportedLessons() {
        return new BotMessage(
                "Ваш вид расписания пока не поддерживается или вы указали недостаточно информации для выдачи расписания.",
                ButtonFullTimeSchedule);
    }

    public static BotMessage getBotMessageForFullTimeExamPeriod(ExamPeriodEventDto examPeriodEventDto, String day) {
        return new BotMessage(Templates.makeFullTimeExamPeriodTemplate(examPeriodEventDto, day), ButtonFullTimeSchedule);
    }

    public static BotMessage getBotMessageForEmptyFullTimeExamPeriod() {
        return new BotMessage("Расписание сессии для вашей группы отсутствует.", ButtonFullTimeSchedule);
    }

    public static BotMessage getBotMessageForTeacherFullTimeLessons(TeacherFullTimeLessonDto fullTimeLessonDto,
                                                                    String day,
                                                                    boolean filterWeekType) {
        return new BotMessage(
                Templates.makeTeacherFullTimeLessonTemplate(fullTimeLessonDto, day, filterWeekType),
                ButtonFullTimeSchedule);
    }

    public static BotMessage getBotMessageForTeacherExamPeriod(TeacherExamPeriodEventDto examPeriodEventDto) {
        return new BotMessage(Templates.makeTeacherExamPeriodTemplate(examPeriodEventDto), ButtonFullTimeSchedule);
    }

    public static BotMessage getBotMessageForExtramuralEvent(ExtramuralDto extramuralDto, String day) {
        return new BotMessage(
                Templates.makeExtramuralEventTemplate(extramuralDto, day),
                ButtonExtramuralSchedule
        );
    }

    public static BotMessage getBotMessageForEmptyExtramuralEvents() {
        return new BotMessage("Расписание для вашей группы отсутствует.", ButtonExtramuralSchedule);
    }

    public static boolean isBotUserFullTime(BotUser botUser) {
        return botUser != null
                && botUser.getEducationForm() != null
                && EducationForm.DO.getGroupType().equalsIgnoreCase(botUser.getEducationForm())
                && !StringUtils.isEmpty(botUser.getGroupNumber());
    }

    public static boolean isBotUserExtramural(BotUser botUser) {
        return botUser != null
                && botUser.getEducationForm() != null
                && EducationForm.ZO.getGroupType().equalsIgnoreCase(botUser.getEducationForm())
                && !StringUtils.isEmpty(botUser.getGroupNumber());
    }
}
