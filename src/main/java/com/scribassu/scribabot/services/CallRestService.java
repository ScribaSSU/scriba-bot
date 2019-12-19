package com.scribassu.scribabot.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.scribassu.tracto.domain.FullTimeLesson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@Service
public class CallRestService {

    @Value("${call.rest.prefix}")
    private String prefix;

    private String FULL_DEP_EDU_GROUP_URI = "schedule/full/%s/%s";
    private String FULL_DEP_EDU_GROUP_DAY_URI = "schedule/full/%s/%s/%s";
    private String FULL_DEP_EDU_GROUP_DAY_LESSON_URI = "schedule/full/%s/%s/%s/%s";

    public List<FullTimeLesson> getFullLessonsByDayAndLesson(String department,
                                          String groupNumber,
                                          String dayNumber,
                                          String lessonNumber) {
        RestTemplate restTemplate = new RestTemplate();
        String uri = prefix +
                String.format(
                        FULL_DEP_EDU_GROUP_DAY_LESSON_URI,
                        department,
                        groupNumber,
                        dayNumber,
                        lessonNumber
                );


        String result = restTemplate.getForObject(uri, String.class);
        return mapToListFullTimeLesson(result);
    }

    public List<FullTimeLesson> getFullLessonsByDay(String department,
                                                    String groupNumber,
                                                    String dayNumber) {
        RestTemplate restTemplate = new RestTemplate();
        String uri = prefix +
                String.format(
                        FULL_DEP_EDU_GROUP_DAY_URI,
                        department,
                        groupNumber,
                        dayNumber
                );

        String result = restTemplate.getForObject(uri, String.class);
        return mapToListFullTimeLesson(result);
    }

    public List<FullTimeLesson> getFullLessonsByGroup(String department,
                                                    String groupNumber) {
        RestTemplate restTemplate = new RestTemplate();
        String uri = prefix +
                String.format(
                        FULL_DEP_EDU_GROUP_URI,
                        department,
                        groupNumber
                );

        String result = restTemplate.getForObject(uri, String.class);
        return mapToListFullTimeLesson(result);
    }

    private List<FullTimeLesson> mapToListFullTimeLesson(String string) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            CollectionType collectionType = typeFactory.constructCollectionType(
                    List.class, FullTimeLesson.class);
            return objectMapper.readValue(string, collectionType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
