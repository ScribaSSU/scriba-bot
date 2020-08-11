package com.scribassu.scribabot.util;

import com.scribassu.scribabot.dto.rest.ExamPeriodEventDto;
import com.scribassu.scribabot.dto.rest.FullTimeLessonDto;
import com.scribassu.scribabot.dto.rest.TeacherExamPeriodEventDto;
import com.scribassu.scribabot.dto.rest.TeacherFullTimeLessonDto;
import com.scribassu.scribabot.text.CommandText;
import com.scribassu.tracto.domain.*;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Templates {

    public static String makeFullTimeLessonTemplate(FullTimeLessonDto fullTimeLessonDto,
                                                    String day,
                                                    boolean filterWeekType) {
        StringBuilder stringBuilder = new StringBuilder();
        Calendar calendar = CalendarUtils.getCalendar();

        if(day.equalsIgnoreCase(CommandText.TODAY)) {
            stringBuilder.append("Сегодня ");
        }
        if(day.equalsIgnoreCase(CommandText.TOMORROW)) {
            stringBuilder.append("Завтра ");
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        if(day.equalsIgnoreCase(CommandText.YESTERDAY)) {
            stringBuilder.append("Вчера ");
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }

        stringBuilder.append(fullTimeLessonDto.getDay().getWeekDay().getDay()).append(" ");

        String data = calendar.get(Calendar.DAY_OF_MONTH) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR);

        if(day.equalsIgnoreCase(CommandText.TODAY)
                || day.equalsIgnoreCase(CommandText.TOMORROW)
                || day.equalsIgnoreCase(CommandText.YESTERDAY)) {
            stringBuilder.append(data);
        }
        stringBuilder.append("\n");
        if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) { // for week type determination
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }
        WeekType currentWeekType = WeekTypeUtils.getWeekType(calendar);
        stringBuilder.append("Неделя: ").append(WeekTypeUtils.weekTypeToLongString(currentWeekType)).append("\n");

        stringBuilder.append("Группа № ").append(fullTimeLessonDto.getStudentGroup().getGroupNumberRus()).append("\n \n");

        if(CollectionUtils.isEmpty(fullTimeLessonDto.getLessons())) {
            stringBuilder.append("А пар-то нету :)");
        }
        else {
            List<FullTimeLesson> fullTimeLessons = fullTimeLessonDto.getLessons();
            if(filterWeekType) {
                fullTimeLessons = fullTimeLessons
                        .stream()
                        .filter(f -> f.getWeekType().equals(WeekType.FULL) || f.getWeekType().equals(currentWeekType))
                        .collect(Collectors.toList());
            }
            fullTimeLessons.sort(Comparator.comparingInt(o -> o.getLessonTime().getLessonNumber()));
            for (FullTimeLesson fullTimeLesson : fullTimeLessons) {
                stringBuilder.append(appendTime(fullTimeLesson.getLessonTime())).append("\n")
                        .append(fullTimeLesson.getLessonType().getType())
                        .append(" ")
                        .append(appendLessonTypeEmoji(fullTimeLesson))
                        .append("\n");

                if(!fullTimeLesson.getWeekType().equals(WeekType.FULL)) {
                    stringBuilder.append(fullTimeLesson.getWeekType().getType()).append("\n");
                }
                if (!StringUtils.isEmpty(fullTimeLesson.getSubGroup())) {
                    stringBuilder.append(fullTimeLesson.getSubGroup().trim()).append("\n");
                }
                stringBuilder.append(fullTimeLesson.getName()).append("\n");
                stringBuilder.append(appendTeacher(fullTimeLesson.getTeacher())).append("\n");
                stringBuilder.append(fullTimeLesson.getPlace()).append("\n \n");
            }
        }

        return stringBuilder.toString();
    }

    public static String makeFullTimeExamPeriodTemplate(ExamPeriodEventDto examPeriodEventDto,
                                                        String day) {
        StringBuilder stringBuilder = new StringBuilder();
        if(day.equalsIgnoreCase(CommandText.TODAY)) {
            stringBuilder.append("Сегодня").append("\n\n");
        }
        if(day.equalsIgnoreCase(CommandText.TOMORROW)) {
            stringBuilder.append("Завтра").append("\n\n");
        }
        if(day.equalsIgnoreCase(CommandText.AFTER_TOMORROW)) {
            stringBuilder.append("Послезавтра").append("\n\n");
        }
        stringBuilder.append("Группа № ").append(examPeriodEventDto.getStudentGroup().getGroupNumberRus()).append("\n \n");
        List<ExamPeriodEvent> examPeriodEvents = examPeriodEventDto.getExamPeriodEvents();
        examPeriodEvents.sort((e1, e2) -> (int) (e1.getId() - e2.getId()));

        for(ExamPeriodEvent examPeriodEvent : examPeriodEvents) {
            if(examPeriodEvent.getDay() != -1) {
                stringBuilder
                        .append(examPeriodEvent.getDay())
                        .append(" ")
                        .append(examPeriodEvent.getMonth().getRusGenitive())
                        .append(" ")
                        .append(examPeriodEvent.getYear())
                        .append("\n");
            }
            else {
                stringBuilder.append("\n");
            }
            stringBuilder
                    .append(examPeriodEvent.getHour())
                    .append(":")
                    .append(
                            (examPeriodEvent.getMinute() < 10 ?
                                    "0" + examPeriodEvent.getMinute() :
                                    examPeriodEvent.getMinute())
                    )
                    .append("\n");
            stringBuilder
                    .append(examPeriodEvent.getExamPeriodEventType().getType())
                    .append(" ")
                    .append(appendExamPeriodEventTypeEmoji(examPeriodEvent));

            stringBuilder.append("\n");
            stringBuilder.append(examPeriodEvent.getSubjectName()).append("\n");
            stringBuilder.append(appendTeacher(examPeriodEvent.getTeacher())).append("\n");
            stringBuilder.append(examPeriodEvent.getPlace()).append("\n \n");
        }
        return stringBuilder.toString();
    }

    public static String makeTeacherFullTimeLessonTemplate(TeacherFullTimeLessonDto fullTimeLessonDto,
                                                           String day,
                                                           boolean filterWeekType) {
        StringBuilder stringBuilder = new StringBuilder();
        Calendar calendar = CalendarUtils.getCalendar();

        if(day.equalsIgnoreCase(CommandText.TODAY)) {
            stringBuilder.append("Сегодня ");
        }
        if(day.equalsIgnoreCase(CommandText.TOMORROW)) {
            stringBuilder.append("Завтра ");
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        if(day.equalsIgnoreCase(CommandText.YESTERDAY)) {
            stringBuilder.append("Вчера ");
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }

        stringBuilder.append(fullTimeLessonDto.getDay().getWeekDay().getDay()).append(" ");

        String data = calendar.get(Calendar.DAY_OF_MONTH) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR);

        if(day.equalsIgnoreCase(CommandText.TODAY)
                || day.equalsIgnoreCase(CommandText.TOMORROW)
                || day.equalsIgnoreCase(CommandText.YESTERDAY)) {
            stringBuilder.append(data);
        }
        stringBuilder.append("\n");
        if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) { // for week type determination
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }
        WeekType currentWeekType = WeekTypeUtils.getWeekType(calendar);
        stringBuilder.append("Неделя: ").append(WeekTypeUtils.weekTypeToLongString(currentWeekType)).append("\n");

        Teacher teacher = fullTimeLessonDto.getTeacher();
        stringBuilder.append(appendTeacher(teacher)).append("\n\n");

        if(CollectionUtils.isEmpty(fullTimeLessonDto.getLessons())) {
            stringBuilder.append("А пар-то нету :)");
        }
        else {
            List<FullTimeLesson> fullTimeLessons = fullTimeLessonDto.getLessons();
            if(filterWeekType) {
                fullTimeLessons = fullTimeLessons
                        .stream()
                        .filter(f -> f.getWeekType().equals(WeekType.FULL) || f.getWeekType().equals(currentWeekType))
                        .collect(Collectors.toList());
            }
            fullTimeLessons.sort(Comparator.comparingInt(o -> o.getLessonTime().getLessonNumber()));
            for (FullTimeLesson fullTimeLesson : fullTimeLessons) {
                stringBuilder.append(appendTime(fullTimeLesson.getLessonTime())).append("\n")
                        .append(fullTimeLesson.getLessonType().getType())
                        .append(" ")
                        .append(appendLessonTypeEmoji(fullTimeLesson))
                        .append("\n");

                if(!fullTimeLesson.getWeekType().equals(WeekType.FULL)) {
                    stringBuilder.append(fullTimeLesson.getWeekType().getType()).append("\n");
                }
                stringBuilder.append(fullTimeLesson.getName()).append("\n");
                stringBuilder.append("Группа № ").append(fullTimeLesson.getStudentGroup().getGroupNumberRus()).append(" ");
                if (!StringUtils.isEmpty(fullTimeLesson.getSubGroup())) {
                    stringBuilder.append(fullTimeLesson.getSubGroup().trim());
                }
                stringBuilder.append("\n");
                stringBuilder.append(fullTimeLesson.getPlace()).append("\n \n");
            }
        }

        return stringBuilder.toString();
    }

    public static String makeTeacherExamPeriodTemplate(TeacherExamPeriodEventDto examPeriodEventDto) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(appendTeacher(examPeriodEventDto.getTeacher())).append("\n\n");
        List<ExamPeriodEvent> examPeriodEvents = examPeriodEventDto.getExamPeriodEvents();
        examPeriodEvents.sort((e1, e2) -> (int) (e1.getId() - e2.getId()));

        for(ExamPeriodEvent examPeriodEvent : examPeriodEvents) {
            if(examPeriodEvent.getDay() != -1) {
                stringBuilder
                        .append(examPeriodEvent.getDay())
                        .append(" ")
                        .append(examPeriodEvent.getMonth().getRusGenitive())
                        .append(" ")
                        .append(examPeriodEvent.getYear())
                        .append("\n");
            }
            else {
                stringBuilder.append("\n");
            }
            stringBuilder
                    .append(examPeriodEvent.getHour())
                    .append(":")
                    .append(
                            (examPeriodEvent.getMinute() < 10 ?
                                    "0" + examPeriodEvent.getMinute() :
                                    examPeriodEvent.getMinute())
                    )
                    .append("\n");
            stringBuilder
                    .append(examPeriodEvent.getExamPeriodEventType().getType())
                    .append(" ")
                    .append(appendExamPeriodEventTypeEmoji(examPeriodEvent))
                    .append("\n");

            stringBuilder.append(examPeriodEvent.getSubjectName()).append("\n");
            stringBuilder.append("Группа № ").append(examPeriodEvent.getStudentGroup().getGroupNumberRus()).append("\n");
            stringBuilder.append(examPeriodEvent.getPlace()).append("\n \n");
        }
        return stringBuilder.toString();
    }

    private static String appendTeacher(Teacher teacher) {
        return teacher.getSurname() + " " +
                (StringUtils.isEmpty(teacher.getName()) ? " " : teacher.getName()) +
                " " +
                (StringUtils.isEmpty(teacher.getPatronymic()) ? " " : teacher.getPatronymic());
    }

    private static String appendTime(LessonTime lessonTime) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(lessonTime.getHourStart()).append(":");
        if(lessonTime.getMinuteStart() < 10) {
            stringBuilder.append("0");
        }
        stringBuilder.append(lessonTime.getMinuteStart()).append(" - ").append(lessonTime.getHourEnd()).append(":");
        if(lessonTime.getMinuteEnd() < 10) {
            stringBuilder.append("0");
        }
        stringBuilder.append(lessonTime.getMinuteEnd());
        return stringBuilder.toString();
    }

    private static String appendExamPeriodEventTypeEmoji(ExamPeriodEvent examPeriodEvent) {
        if(examPeriodEvent.getExamPeriodEventType().equals(ExamPeriodEventType.MIDTERM))
            return "\uD83D\uDCA1";
        if(examPeriodEvent.getExamPeriodEventType().equals(ExamPeriodEventType.MIDTERM_WITH_MARK))
            return "\uD83D\uDD25";
        if(examPeriodEvent.getExamPeriodEventType().equals(ExamPeriodEventType.CONSULTATION))
            return "\uD83D\uDCD3";
        if(examPeriodEvent.getExamPeriodEventType().equals(ExamPeriodEventType.EXAM))
            return "\uD83C\uDF40";
        return "";
    }

    private static String appendLessonTypeEmoji(FullTimeLesson fullTimeLesson) {
        if (fullTimeLesson.getLessonType().equals(LessonType.LECTURE))
            return "\uD83D\uDCD7";
        else if (fullTimeLesson.getLessonType().equals(LessonType.PRACTICE))
            return "\uD83D\uDCD8";
        else if (fullTimeLesson.getLessonType().equals(LessonType.LABORATORY))
            return "\uD83D\uDCD5";
        return "";
    }
}
