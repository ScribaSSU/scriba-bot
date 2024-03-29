package com.scribassu.scribabot.generators;

import com.scribassu.scribabot.text.CommandText;
import com.scribassu.scribabot.text.MessageText;
import com.scribassu.scribabot.util.CalendarUtils;
import com.scribassu.tracto.dto.*;
import org.springframework.util.CollectionUtils;

import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.scribassu.scribabot.text.MessageText.NO_EXAMS;
import static com.scribassu.scribabot.text.MessageText.NO_LESSONS;

public class MessageGenerator {

    public static String makeFullTimeLessonTemplate(FullTimeLessonListDto fullTimeLessonDto,
                                                    String day,
                                                    boolean filterWeekType) {
        StringBuilder stringBuilder = new StringBuilder();
        Calendar calendar = CalendarUtils.getCalendar();

        if (day.equalsIgnoreCase(CommandText.TODAY)) {
            stringBuilder.append("Сегодня ");
        }
        if (day.equalsIgnoreCase(CommandText.TOMORROW)) {
            stringBuilder.append("Завтра ");
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        if (day.equalsIgnoreCase(CommandText.YESTERDAY)) {
            stringBuilder.append("Вчера ");
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }

        stringBuilder.append(fullTimeLessonDto.getDay().getWeekDay().getDay()).append(" ");

        String data = calendar.get(Calendar.DAY_OF_MONTH) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR);

        if (day.equalsIgnoreCase(CommandText.TODAY)
                || day.equalsIgnoreCase(CommandText.TOMORROW)
                || day.equalsIgnoreCase(CommandText.YESTERDAY)) {
            stringBuilder.append(data);
        }
        stringBuilder.append("\n");
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) { // for week type determination
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }
        WeekType currentWeekType = CalendarUtils.getWeekType(calendar, fullTimeLessonDto.getStudentGroup().getDepartment().isWeekShift());

        if (null != fullTimeLessonDto.getStudentGroup()) {
            stringBuilder.append("Неделя: ").append(CalendarUtils.weekTypeToLongString(currentWeekType)).append("\n");
            stringBuilder.append("Группа № ").append(fullTimeLessonDto.getStudentGroup().getGroupNumberRus()).append("\n \n");
        } else {
            stringBuilder.append("Группа отсутствует").append("\n \n");
        }

        if (CollectionUtils.isEmpty(fullTimeLessonDto.getLessons())) {
            stringBuilder.append(NO_LESSONS);
        } else {
            List<FullTimeLessonDto> fullTimeLessons = fullTimeLessonDto.getLessons();
            if (filterWeekType) {
                final WeekType finalCurrentWeekType = currentWeekType;
                fullTimeLessons = fullTimeLessons
                        .stream()
                        .filter(f -> WeekType.FULL.equals(f.getWeekType()) || finalCurrentWeekType.equals(f.getWeekType()))
                        .collect(Collectors.toList());
            }
            // it could be empty after week type filter
            if (fullTimeLessons.isEmpty()) {
                stringBuilder.append(NO_LESSONS);
            } else {
                fullTimeLessons.sort(Comparator.comparingInt(o -> o.getLessonTime().getLessonNumber()));
                for (var fullTimeLesson : fullTimeLessons) {
                    stringBuilder.append(appendTime(fullTimeLesson.getLessonTime())).append("\n")
                            .append(fullTimeLesson.getLessonType().getType())
                            .append(" ")
                            .append(appendLessonTypeEmoji(fullTimeLesson))
                            .append("\n");

                    if (!fullTimeLesson.getWeekType().equals(WeekType.FULL)) {
                        stringBuilder.append(fullTimeLesson.getWeekType().getType()).append("\n");
                    }
                    if (!fullTimeLesson.getSubGroup().isBlank()) {
                        stringBuilder.append(fullTimeLesson.getSubGroup().trim()).append("\n");
                    }
                    stringBuilder.append(fullTimeLesson.getName()).append("\n");
                    stringBuilder.append(appendTeacher(fullTimeLesson.getTeacher())).append("\n");
                    stringBuilder.append(fullTimeLesson.getPlace()).append("\n \n");
                }
            }
        }

        return stringBuilder.toString();
    }

    public static String makeFullTimeLessonTemplateLessonsAll(FullTimeLessonListDto fullTimeLessonDto,
                                                              boolean filterWeekType) {
        StringBuilder stringBuilder = new StringBuilder();
        Calendar calendar = CalendarUtils.getCalendar();
        WeekType currentWeekType = CalendarUtils.getWeekType(calendar, fullTimeLessonDto.getStudentGroup().getDepartment().isWeekShift());

        if (null != fullTimeLessonDto.getStudentGroup()) {
            // kgl has another week type
            if (fullTimeLessonDto.getStudentGroup().getDepartment().getUrl().equalsIgnoreCase("kgl")) {
                currentWeekType = currentWeekType.equals(WeekType.NOM) ? WeekType.DENOM : WeekType.NOM;
            }
            stringBuilder.append("Группа № ").append(fullTimeLessonDto.getStudentGroup().getGroupNumberRus()).append("\n \n");
        } else {
            stringBuilder.append("Группа отсутствует").append("\n \n");
        }

        if (CollectionUtils.isEmpty(fullTimeLessonDto.getLessons())) {
            stringBuilder.append("А пар-то нету :)");
        } else {
            List<FullTimeLessonDto> fullTimeLessons = fullTimeLessonDto.getLessons();
            if (filterWeekType) {
                final WeekType finalCurrentWeekType = currentWeekType;
                fullTimeLessons = fullTimeLessons
                        .stream()
                        .filter(f -> f.getWeekType().equals(WeekType.FULL) || f.getWeekType().equals(finalCurrentWeekType))
                        .collect(Collectors.toList());
            }

            //TODO: refactor may be
            List<FullTimeLessonDto> monday = fullTimeLessons.stream().filter(l -> l.getDay().getWeekDay().equals(WeekDay.MONDAY)).collect(Collectors.toList());
            List<FullTimeLessonDto> tuesday = fullTimeLessons.stream().filter(l -> l.getDay().getWeekDay().equals(WeekDay.TUESDAY)).collect(Collectors.toList());
            List<FullTimeLessonDto> wednesday = fullTimeLessons.stream().filter(l -> l.getDay().getWeekDay().equals(WeekDay.WEDNESDAY)).collect(Collectors.toList());
            List<FullTimeLessonDto> thursday = fullTimeLessons.stream().filter(l -> l.getDay().getWeekDay().equals(WeekDay.THURSDAY)).collect(Collectors.toList());
            List<FullTimeLessonDto> friday = fullTimeLessons.stream().filter(l -> l.getDay().getWeekDay().equals(WeekDay.FRIDAY)).collect(Collectors.toList());
            List<FullTimeLessonDto> saturday = fullTimeLessons.stream().filter(l -> l.getDay().getWeekDay().equals(WeekDay.SATURDAY)).collect(Collectors.toList());

            List<List<FullTimeLessonDto>> weekLessons = List.of(monday, tuesday, wednesday, thursday, friday, saturday);
            List<String> days = List.of("ПОНЕДЕЛЬНИК", "ВТОРНИК", "СРЕДА", "ЧЕТВЕРГ", "ПЯТНИЦА", "СУББОТА");
            int dayNumber = 0;

            for (List<FullTimeLessonDto> day : weekLessons) {
                stringBuilder.append(days.get(dayNumber)).append("\n\n");
                dayNumber++;
                if (CollectionUtils.isEmpty(day)) {
                    stringBuilder.append("А пар-то нету :)").append("\n\n");
                } else {
                    day.sort(Comparator.comparingInt(o -> o.getLessonTime().getLessonNumber()));
                    for (var fullTimeLesson : day) {
                        stringBuilder.append(appendTime(fullTimeLesson.getLessonTime())).append("\n")
                                .append(fullTimeLesson.getLessonType().getType())
                                .append(" ")
                                .append(appendLessonTypeEmoji(fullTimeLesson))
                                .append("\n");

                        if (!fullTimeLesson.getWeekType().equals(WeekType.FULL)) {
                            stringBuilder.append(fullTimeLesson.getWeekType().getType()).append("\n");
                        }
                        if (!fullTimeLesson.getSubGroup().isBlank()) {
                            stringBuilder.append(fullTimeLesson.getSubGroup().trim()).append("\n");
                        }
                        stringBuilder.append(fullTimeLesson.getName()).append("\n");
                        stringBuilder.append(appendTeacher(fullTimeLesson.getTeacher())).append("\n");
                        stringBuilder.append(fullTimeLesson.getPlace()).append("\n \n");
                    }
                }
            }
        }

        return stringBuilder.toString();
    }

    public static String makeFullTimeExamPeriodTemplate(ExamPeriodEventListDto examPeriodEventDto,
                                                        String day) {
        StringBuilder stringBuilder = new StringBuilder();
        if (day.equalsIgnoreCase(CommandText.TODAY)) {
            stringBuilder.append("Сегодня").append("\n\n");
        }
        if (day.equalsIgnoreCase(CommandText.TOMORROW)) {
            stringBuilder.append("Завтра").append("\n\n");
        }
        if (day.equalsIgnoreCase(CommandText.AFTER_TOMORROW)) {
            stringBuilder.append("Послезавтра").append("\n\n");
        }
        stringBuilder.append("Группа № ").append(examPeriodEventDto.getStudentGroup().getGroupNumberRus()).append("\n \n");

        if (CollectionUtils.isEmpty(examPeriodEventDto.getExamPeriodEvents())) {
            Calendar calendar = CalendarUtils.getCalendar();

            if (day.equalsIgnoreCase(CommandText.TOMORROW)) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
            if (day.equalsIgnoreCase(CommandText.AFTER_TOMORROW)) {
                calendar.add(Calendar.DAY_OF_MONTH, 2);
            }

            String data = calendar.get(Calendar.DAY_OF_MONTH) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR);
            stringBuilder.append(data).append("\n");
            stringBuilder.append(NO_EXAMS);
        } else {
            List<ExamPeriodEventDto> examPeriodEvents = examPeriodEventDto.getExamPeriodEvents();
            examPeriodEvents.sort((e1, e2) -> (int) (e1.getId() - e2.getId()));

            for (ExamPeriodEventDto examPeriodEvent : examPeriodEvents) {
                if (examPeriodEvent.getDay() != -1) {
                    stringBuilder
                            .append(examPeriodEvent.getDay())
                            .append(" ")
                            .append(examPeriodEvent.getMonth().getRusGenitive())
                            .append(" ")
                            .append(examPeriodEvent.getYear())
                            .append("\n");
                } else {
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
        }
        return stringBuilder.toString();
    }

    public static String makeTeacherFullTimeLessonTemplate(TeacherFullTimeLessonListDto fullTimeLessonDto,
                                                           String day,
                                                           boolean filterWeekType) {
        StringBuilder stringBuilder = new StringBuilder();
        Calendar calendar = CalendarUtils.getCalendar();

        if (day.equalsIgnoreCase(CommandText.TODAY)) {
            stringBuilder.append("Сегодня ");
        }
        if (day.equalsIgnoreCase(CommandText.TOMORROW)) {
            stringBuilder.append("Завтра ");
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        if (day.equalsIgnoreCase(CommandText.YESTERDAY)) {
            stringBuilder.append("Вчера ");
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }

        stringBuilder.append(fullTimeLessonDto.getDay().getWeekDay().getDay()).append(" ");

        String data = calendar.get(Calendar.DAY_OF_MONTH) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR);

        if (day.equalsIgnoreCase(CommandText.TODAY)
                || day.equalsIgnoreCase(CommandText.TOMORROW)
                || day.equalsIgnoreCase(CommandText.YESTERDAY)) {
            stringBuilder.append(data);
        }
        stringBuilder.append("\n");
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) { // for week type determination
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }
        // TODO set weekShift for every lesson separately?
        var teacher = fullTimeLessonDto.getTeacher();
        stringBuilder.append(appendTeacher(teacher)).append("\n\n");

        if (CollectionUtils.isEmpty(fullTimeLessonDto.getLessons())) {
            stringBuilder.append(NO_LESSONS);
        } else {
            List<FullTimeLessonDto> fullTimeLessons = fullTimeLessonDto.getLessons();
//            if (filterWeekType) {
//                fullTimeLessons = fullTimeLessons
//                        .stream()
//                        .filter(f -> f.getWeekType().equals(WeekType.FULL) || f.getWeekType().equals(currentWeekType))
//                        .collect(Collectors.toList());
//            }
            fullTimeLessons.sort(Comparator.comparingInt(o -> o.getLessonTime().getLessonNumber()));
            for (var fullTimeLesson : fullTimeLessons) {
                stringBuilder.append(appendTime(fullTimeLesson.getLessonTime())).append("\n")
                        .append(fullTimeLesson.getLessonType().getType())
                        .append(" ")
                        .append(appendLessonTypeEmoji(fullTimeLesson))
                        .append("\n");

                if (!fullTimeLesson.getWeekType().equals(WeekType.FULL)) {
                    stringBuilder.append(fullTimeLesson.getWeekType().getType()).append("\n");
                }
                stringBuilder.append(fullTimeLesson.getName()).append("\n");
                stringBuilder.append("Группа № ").append(fullTimeLesson.getStudentGroup().getGroupNumberRus()).append(" ");
                if (!fullTimeLesson.getSubGroup().isBlank()) {
                    stringBuilder.append(fullTimeLesson.getSubGroup().trim());
                }
                stringBuilder.append("\n");
                stringBuilder.append(fullTimeLesson.getPlace()).append("\n \n");
            }
        }

        return stringBuilder.toString();
    }

    public static String makeTeacherExamPeriodTemplate(TeacherExamPeriodEventListDto examPeriodEventDto) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(appendTeacher(examPeriodEventDto.getTeacher())).append("\n\n");
        List<ExamPeriodEventDto> examPeriodEvents = examPeriodEventDto.getExamPeriodEvents();
        examPeriodEvents.sort((e1, e2) -> (int) (e1.getId() - e2.getId()));

        for (var examPeriodEvent : examPeriodEvents) {
            if (examPeriodEvent.getDay() != -1) {
                stringBuilder
                        .append(examPeriodEvent.getDay())
                        .append(" ")
                        .append(examPeriodEvent.getMonth().getRusGenitive())
                        .append(" ")
                        .append(examPeriodEvent.getYear())
                        .append("\n");
            } else {
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

    public static String makeExtramuralEventTemplate(ExtramuralEventListDto extramuralDto, String day) {
        StringBuilder stringBuilder = new StringBuilder();

        if (day.equalsIgnoreCase(CommandText.TODAY)) {
            stringBuilder.append("Сегодня").append("\n\n");
        }
        if (day.equalsIgnoreCase(CommandText.TOMORROW)) {
            stringBuilder.append("Завтра").append("\n\n");
        }
        if (day.equalsIgnoreCase(CommandText.AFTER_TOMORROW)) {
            stringBuilder.append("Послезавтра").append("\n\n");
        }
        stringBuilder.append("Группа № ").append(extramuralDto.getStudentGroup().getGroupNumberRus()).append("\n \n");

        if (CollectionUtils.isEmpty(extramuralDto.getExtramuralEvents())) {
            Calendar calendar = CalendarUtils.getCalendar();

            if (day.equalsIgnoreCase(CommandText.TOMORROW)) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
            if (day.equalsIgnoreCase(CommandText.AFTER_TOMORROW)) {
                calendar.add(Calendar.DAY_OF_MONTH, 2);
            }

            String data = calendar.get(Calendar.DAY_OF_MONTH) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR);
            stringBuilder.append(data).append("\n");
            stringBuilder.append(NO_EXAMS);
        } else {
            List<ExtramuralEventDto> extramuralEvents = extramuralDto.getExtramuralEvents();
            extramuralEvents.sort(Comparator.comparingLong(ExtramuralEventDto::getId));

            for (var extramuralEvent : extramuralEvents) {
                if (extramuralEvent.getDay() != -1) {
                    stringBuilder
                            .append(extramuralEvent.getDay())
                            .append(" ")
                            .append(extramuralEvent.getMonth().getRusGenitive())
                            .append(" ")
                            .append(extramuralEvent.getYear())
                            .append("\n");
                } else {
                    stringBuilder.append("\n");
                }
                if (extramuralEvent.getStartHour() != -1) {
                    stringBuilder.append(extramuralEvent.getStartHour());
                    if (extramuralEvent.getStartMinute() != -1) {
                        stringBuilder.append(":").append(extramuralEvent.getStartMinute() < 10 ?
                                "0" + extramuralEvent.getStartMinute() :
                                extramuralEvent.getStartMinute());
                    } else {
                        stringBuilder.append("ч.");
                    }

                    if (extramuralEvent.getEndHour() != -1) {
                        stringBuilder.append(" - ").append(extramuralEvent.getEndHour());
                        if (extramuralEvent.getEndMinute() != -1) {
                            stringBuilder.append(":").append(extramuralEvent.getEndMinute() < 10 ?
                                    "0" + extramuralEvent.getEndMinute() :
                                    extramuralEvent.getEndMinute());
                        } else {
                            stringBuilder.append("ч");
                        }
                    }
                    stringBuilder.append("\n");
                }

                stringBuilder
                        .append(extramuralEvent.getEventType().getType())
                        .append(" ")
                        .append(appendExtramuralEventTypeEmoji(extramuralEvent));

                stringBuilder.append("\n");
                stringBuilder.append(extramuralEvent.getName()).append("\n");
                stringBuilder.append(extramuralEvent.getTeacher()).append("\n");
                stringBuilder.append(extramuralEvent.getPlace()).append("\n \n");
            }
        }
        return stringBuilder.toString();
    }

    public static String makeExtramuralEventTemplateTeacher(TeacherExtramuralEventListDto extramuralDto) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(appendTeacher(extramuralDto.getTeacher())).append("\n\n");
        List<ExtramuralEventDto> extramuralEvents = extramuralDto.getExtramuralEvents();

        if (CollectionUtils.isEmpty(extramuralEvents)) {
            stringBuilder.append(MessageText.NO_EXAM_PERIOD_SCHEDULE_TEACHER);
        } else {
            extramuralEvents.sort(Comparator.comparingLong(ExtramuralEventDto::getId));

            for (var extramuralEvent : extramuralEvents) {
                if (extramuralEvent.getDay() != -1) {
                    stringBuilder
                            .append(extramuralEvent.getDay())
                            .append(" ")
                            .append(extramuralEvent.getMonth().getRusGenitive())
                            .append(" ")
                            .append(extramuralEvent.getYear())
                            .append("\n");
                } else {
                    stringBuilder.append("\n");
                }
                if (extramuralEvent.getStartHour() != -1) {
                    stringBuilder.append(extramuralEvent.getStartHour());
                    if (extramuralEvent.getStartMinute() != -1) {
                        stringBuilder.append(":").append(extramuralEvent.getStartMinute() < 10 ?
                                "0" + extramuralEvent.getStartMinute() :
                                extramuralEvent.getStartMinute());
                    } else {
                        stringBuilder.append("ч.");
                    }

                    if (extramuralEvent.getEndHour() != -1) {
                        stringBuilder.append(" - ").append(extramuralEvent.getEndHour());
                        if (extramuralEvent.getEndMinute() != -1) {
                            stringBuilder.append(":").append(extramuralEvent.getEndMinute() < 10 ?
                                    "0" + extramuralEvent.getEndMinute() :
                                    extramuralEvent.getEndMinute());
                        } else {
                            stringBuilder.append("ч");
                        }
                    }
                    stringBuilder.append("\n");
                }

                stringBuilder
                        .append(extramuralEvent.getEventType().getType())
                        .append(" ")
                        .append(appendExtramuralEventTypeEmoji(extramuralEvent));

                stringBuilder.append("\n");
                stringBuilder.append(extramuralEvent.getName()).append("\n");
                stringBuilder.append("Группа № ").append(extramuralEvent.getStudentGroup().getGroupNumberRus()).append("\n");
                stringBuilder.append(extramuralEvent.getPlace()).append("\n \n");
            }
        }
        return stringBuilder.toString();
    }

    private static String appendTeacher(TeacherDto teacher) {
        return teacher.getSurname() + " " +
                (teacher.getName().isBlank() ? " " : teacher.getName()) +
                " " +
                (teacher.getPatronymic().isBlank() ? " " : teacher.getPatronymic());
    }

    private static String appendTime(LessonTimeDto lessonTime) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(lessonTime.getHourStart()).append(":");
        if (lessonTime.getMinuteStart() < 10) {
            stringBuilder.append("0");
        }
        stringBuilder.append(lessonTime.getMinuteStart()).append(" - ").append(lessonTime.getHourEnd()).append(":");
        if (lessonTime.getMinuteEnd() < 10) {
            stringBuilder.append("0");
        }
        stringBuilder.append(lessonTime.getMinuteEnd());
        return stringBuilder.toString();
    }

    private static String appendExamPeriodEventTypeEmoji(ExamPeriodEventDto examPeriodEvent) {
        if (examPeriodEvent.getExamPeriodEventType().equals(ExamPeriodEventType.MIDTERM))
            return "\uD83D\uDCA1";
        if (examPeriodEvent.getExamPeriodEventType().equals(ExamPeriodEventType.MIDTERM_WITH_MARK))
            return "\uD83D\uDD25";
        if (examPeriodEvent.getExamPeriodEventType().equals(ExamPeriodEventType.CONSULTATION))
            return "\uD83D\uDCD3";
        if (examPeriodEvent.getExamPeriodEventType().equals(ExamPeriodEventType.EXAM))
            return "\uD83C\uDF40";
        return "";
    }

    private static String appendLessonTypeEmoji(FullTimeLessonDto fullTimeLesson) {
        if (fullTimeLesson.getLessonType().equals(LessonType.LECTURE))
            return "\uD83D\uDCD7";
        else if (fullTimeLesson.getLessonType().equals(LessonType.PRACTICE))
            return "\uD83D\uDCD8";
        else if (fullTimeLesson.getLessonType().equals(LessonType.LABORATORY))
            return "\uD83D\uDCD5";
        return "";
    }

    private static String appendExtramuralEventTypeEmoji(ExtramuralEventDto extramuralEvent) {
        switch (extramuralEvent.getEventType()) {
            case EXAM:
                return "\uD83C\uDF40";
            case LECTURE:
                return "\uD83D\uDCD7";
            case MIDTERM:
                return "\uD83D\uDCA1";
            case PRACTICE:
                return "\uD83D\uDCD8";
            case LABORATORY:
                return "\uD83D\uDCD5";
            case CONSULTATION:
                return "\uD83D\uDCD3";
            case MIDTERM_WITH_MARK:
                return "\uD83D\uDD25";
            default:
                return "";
        }
    }
}
