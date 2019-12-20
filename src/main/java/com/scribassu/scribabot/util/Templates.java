package com.scribassu.scribabot.util;

import com.scribassu.tracto.domain.FullTimeLesson;
import com.scribassu.tracto.domain.LessonType;

import java.util.List;

public class Templates {

    public static String makeTemplate(List<FullTimeLesson> fullTimeLessons)
    {
        fullTimeLessons.sort((o1, o2) -> (o1.getLessonTime().getLessonNumber() - o2.getLessonTime().getLessonNumber()));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Сегодня ").append(fullTimeLessons.get(0).getDay().getWeekDay().getDay()).append("\nНеделя - ");
        stringBuilder.append(fullTimeLessons.get(0).getWeekType().getType()).append("\uD83D\uDD4A\n");
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
