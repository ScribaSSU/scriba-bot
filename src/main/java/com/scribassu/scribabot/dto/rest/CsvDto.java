package com.scribassu.scribabot.dto.rest;

import com.scribassu.tracto.domain.StudentGroup;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CsvDto {
    StudentGroup studentGroup;
    String pathToFile;

    public CsvDto(StudentGroup studentGroup, String pathToFile) {
        this.studentGroup = studentGroup;
        this.pathToFile = pathToFile;
    }
}
