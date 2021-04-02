package com.scribassu.scribabot.dto.rest;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CsvDto {
    String pathToFile;

    public CsvDto(String pathToFile) {
        this.pathToFile = pathToFile;
    }
}
