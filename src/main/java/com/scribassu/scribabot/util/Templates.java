package com.scribassu.scribabot.util;

import com.scribassu.tracto.domain.FullTimeLesson;
import com.scribassu.tracto.domain.LessonType;
import com.scribassu.tracto.domain.WeekType;

import java.util.List;

public class Templates {
    public String makeTemplate(List<FullTimeLesson> fullTimeLessons)
    {
        fullTimeLessons.sort((o1, o2) -> (int) (o1.getLessonTime().getId() - o2.getLessonTime().getId()));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Сегодня ").append(fullTimeLessons.get(0).getDay().getWeekDay().getDay()).append("\n Неделя - ");
        if (fullTimeLessons.get(0).getWeekType().equals(WeekType.NOM))
            stringBuilder.append("числитель ").append("\uD83D\uDD4A \n");
        else if (fullTimeLessons.get(0).getWeekType().equals(WeekType.DENOM))
            stringBuilder.append("знаменатель ").append("\uD83D\uDD4A \n");
        stringBuilder.append("Группа № ").append(fullTimeLessons.get(0).getStudentGroup().getGroupNumber()).append("\n");
        for (FullTimeLesson fullTimeLesson : fullTimeLessons) {
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

        return stringBuilder.toString();
    }
}
