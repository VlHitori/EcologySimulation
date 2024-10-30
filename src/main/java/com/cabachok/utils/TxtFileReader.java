package com.cabachok.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class TxtFileReader {
    public static List<String> read(String filePath) {
        try {
            return Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
