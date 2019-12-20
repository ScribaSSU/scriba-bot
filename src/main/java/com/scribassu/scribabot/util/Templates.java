package com.scribassu.scribabot.util;

import com.scribassu.tracto.domain.FullTimeLesson;
import com.scribassu.tracto.domain.LessonType;

import java.util.List;

public class Templates {
    private static final int lessonsCount = 8;
    public String makeTemplate(List<FullTimeLesson> fullTimeLessons)
    {
        fullTimeLessons.sort((o1, o2) -> (int) (o1.getId() - o2.getId()));
        StringBuilder stringBuilder = new StringBuilder();
        for (FullTimeLesson fullTimeLesson : fullTimeLessons)
        {
            stringBuilder.append("Сегодня \n").append("Неделя - ").append(fullTimeLesson.getWeekType())
                    .append("\uD83D\uDD4A \n").append("Группа № ").append(fullTimeLesson.getStudentGroup()).append("\n");
            for (int i = 0; i < lessonsCount; i++)
            {
                stringBuilder.append(fullTimeLesson.getLessonTime().getTimeStart()).append(" - ")
                        .append(fullTimeLesson.getLessonTime().getTimeFinish()).append("\n");
                if (fullTimeLesson.getLessonType().equals(LessonType.LECTURE))
                    stringBuilder.append("Лекция \uD83D\uDCD7 \n");
                else if (fullTimeLesson.getLessonType().equals(LessonType.PRACTICE))
                    stringBuilder.append("Практика \uD83D\uDCD8 \n");
                else if (fullTimeLesson.getLessonType().equals(LessonType.LABORATORY))
                    stringBuilder.append("Лабораторная \uD83D\uDCD5 \n");
                stringBuilder.append(fullTimeLesson.getName()).append("\n")
                        .append(fullTimeLesson.getTeacher().getSurname()).append(" ")
                        .append(fullTimeLesson.getTeacher().getName()).append(" ")
                        .append(fullTimeLesson.getTeacher().getPatronymic()).append("\n")
                        .append(fullTimeLesson.getPlace()).append("\n \n");
            }
        }
        
        return stringBuilder.toString();
    }
}
