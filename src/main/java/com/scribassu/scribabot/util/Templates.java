package com.scribassu.scribabot.util;

import com.scribassu.scribabot.commands.CommandText;
import com.scribassu.tracto.domain.FullTimeLesson;
import com.scribassu.tracto.domain.LessonType;
import com.scribassu.tracto.domain.WeekType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Templates {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public static String makeTemplate(List<FullTimeLesson> fullTimeLessons, String day)
    {
        fullTimeLessons.sort((o1, o2) -> (o1.getLessonTime().getLessonNumber() - o2.getLessonTime().getLessonNumber()));
        StringBuilder stringBuilder = new StringBuilder();

        if(day.equalsIgnoreCase(CommandText.TODAY)) {
            stringBuilder.append("Сегодня ");
        }
        if(day.equalsIgnoreCase(CommandText.TOMORROW)) {
            stringBuilder.append("Завтра ");
        }
        if(day.equalsIgnoreCase(CommandText.YESTERDAY)) {
            stringBuilder.append("Вчера ");
        }

        Date date = new Date();

        stringBuilder
                .append(fullTimeLessons.get(0).getDay().getWeekDay().getDay())
                .append(" ")
                .append(dateFormat.format(date))
                .append("\n");
        //stringBuilder.append("Сегодня ").append(fullTimeLessons.get(0).getDay().getWeekDay().getDay()).append("\nНеделя - ");
        //stringBuilder.append(fullTimeLessons.get(0).getWeekType().getType()).append("\uD83D\uDD4A\n");
        stringBuilder.append("Группа № ").append(fullTimeLessons.get(0).getStudentGroup().getGroupNumber()).append("\n \n");
        for (FullTimeLesson fullTimeLesson : fullTimeLessons) {
            stringBuilder.append(fullTimeLesson.getLessonTime().getTimeStart()).append(" - ")
                   .append(fullTimeLesson.getLessonTime().getTimeFinish()).append("\n")
                   .append(fullTimeLesson.getLessonType().getType());
            if (fullTimeLesson.getLessonType().equals(LessonType.LECTURE))
                stringBuilder.append(" \uD83D\uDCD7\n");
            else if (fullTimeLesson.getLessonType().equals(LessonType.PRACTICE))
                 stringBuilder.append(" \uD83D\uDCD8\n");
            else if (fullTimeLesson.getLessonType().equals(LessonType.LABORATORY))
                 stringBuilder.append(" \uD83D\uDCD5\n");
            if(!fullTimeLesson.getWeekType().equals(WeekType.FULL)) {
                stringBuilder.append(fullTimeLesson.getWeekType().getType()).append("\n");
            }
            if (!fullTimeLesson.getSubGroup().isEmpty())
                stringBuilder.append(fullTimeLesson.getSubGroup().trim()).append("\n");
            stringBuilder.append(fullTimeLesson.getName()).append("\n")
                    .append(fullTimeLesson.getTeacher().getSurname()).append(" ")
                    .append(fullTimeLesson.getTeacher().getName()).append(" ")
                    .append(fullTimeLesson.getTeacher().getPatronymic()).append("\n")
                    .append(fullTimeLesson.getPlace()).append("\n \n");
        }

        return stringBuilder.toString();
    }
}
