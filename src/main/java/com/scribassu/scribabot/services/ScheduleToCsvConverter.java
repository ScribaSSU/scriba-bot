package com.scribassu.scribabot.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.scribassu.scribabot.dto.BotMessage;
import com.scribassu.scribabot.dto.rest.CsvDto;
import com.scribassu.scribabot.dto.rest.FullTimeLessonDto;
import com.scribassu.tracto.domain.StudentGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ScheduleToCsvConverter {
    private CallRestService callRestService;

    @Autowired
    public ScheduleToCsvConverter() {
        callRestService = new CallRestService();
    }

    public BotMessage urlToCsv(String departmentUrl, String groupNumber){
        FullTimeLessonDto fullTimeLessonDto = callRestService.getFullTimeLessonsByGroup(departmentUrl, groupNumber);
        String json = new Gson().toJson(fullTimeLessonDto);
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(json).getAsJsonObject();

        CsvDto csvDto = callRestService.getCsvSchedule(jsonObject);

        return new BotMessage(csvDto.getPathToFile());

    }
}
