package com.scribassu.scribabot.services;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.stream.Collectors;

@Service
public class KeyboardFileReaderImpl implements KeyboardFileReader {

    @Override
    public String readAsString(String path) {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(path);
            return new BufferedReader(new InputStreamReader(is))
                    .lines()
                    .collect(Collectors.joining("\n"));
    }
}
