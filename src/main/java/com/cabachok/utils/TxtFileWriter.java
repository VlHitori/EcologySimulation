package com.cabachok.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public final class TxtFileWriter {
    public static void write(String filePath, List<String> lines) {
        try {
            Files.write(Paths.get(filePath), lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
