package com.scribassu.scribabot.services;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Service
public class KeyboardFileReaderImpl implements CustomFileReader {

    @Override
    public String readAsString(String path) {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(path);
            return new BufferedReader(new InputStreamReader(is))
                    .lines()
                    .collect(Collectors.joining("\n"));
    }
}
